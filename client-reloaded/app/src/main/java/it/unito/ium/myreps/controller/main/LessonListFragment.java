package it.unito.ium.myreps.controller.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.component.main.LessonListAdapter;
import it.unito.ium.myreps.configuration.StorageConf;
import it.unito.ium.myreps.model.api.ApiManager;
import it.unito.ium.myreps.model.api.SrvStatus;
import it.unito.ium.myreps.model.api.objects.Lesson;
import it.unito.ium.myreps.model.api.objects.User;
import it.unito.ium.myreps.model.storage.KVStorage;
import it.unito.ium.myreps.controller.BaseFragment;
import it.unito.ium.myreps.util.RecyclerViewRow;

public final class LessonListFragment extends BaseFragment {
    @BindView(R.id.fragment_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fragment_list_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.fragment_list_text_empty)
    TextView emptyText;

    private final static SimpleDateFormat TITLE_FORMAT = new SimpleDateFormat("dd/MM", Locale.UK);
    private final LessonListAdapter lessonListAdapter;
    private final String title;
    private final long day;

    private ArrayList<RecyclerViewRow> listCache;

    public LessonListFragment(long day) {
        this.day = day;

        Date date = new Date(day * 1000);
        title = TITLE_FORMAT.format(date);

        lessonListAdapter = new LessonListAdapter();
        listCache = new ArrayList<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = bindView(R.layout.fragment_list, inflater, container);

        emptyText.setText(R.string.activity_main_rv_empty);
        initRecycler();
        loadListData(false);

        return view;
    }

    private void initRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(lessonListAdapter);

        lessonListAdapter.setItemClickListener((view, pos, item) -> loadLessonData((Lesson) item));
        swipeRefreshLayout.setOnRefreshListener(() -> loadListData(true));
    }

    private void loadListData(boolean force) {
        swipeRefreshLayout.setRefreshing(true);

        if (!force && !listCache.isEmpty()) {
            lessonListAdapter.setDataSet(listCache);
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        ApiManager apiManager = getModel().getApiManager();
        listCache = new ArrayList<>();
        apiManager.loadLessonList(day, (status, response) -> runOnUiThread(() -> {
            if (status == SrvStatus.OK) {
                if (response != null ) {
                    lessonListAdapter.setDataSet(response);
                    emptyText.setVisibility(response.isEmpty() ? View.VISIBLE : View.GONE);
                } else {
                    lessonListAdapter.setDataSet(new ArrayList<>());
                    emptyText.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(getContext(), status.toString(), Toast.LENGTH_SHORT).show();
                if (lessonListAdapter.getItemCount() == 0) {
                    emptyText.setVisibility(View.VISIBLE);
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }));
    }

    private void loadLessonData(Lesson item) {
        ApiManager apiManager = getModel().getApiManager();
        apiManager.loadLesson(item.getDay(), item.getCourse().getID(), (status, response) -> {
            if (status == SrvStatus.OK) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.activity_main_select_teacher);

                if (response.getTeachersNum() > 0) {
                    String[] teachers = new String[response.getTeachersNum()];
                    for (int i = 0; i < teachers.length; i++) {
                        teachers[i] = response.getTeacher(i).getFullName();
                    }

                    builder.setItems(teachers, (dialog, which) -> {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                        builder2.setTitle(R.string.activity_main_select_hours);

                        User teacher = response.getTeacher(which);
                        int[] freeHours = teacher.getFreeHours();

                        if (freeHours.length > 0) {
                            String[] vHours = new String[freeHours.length];
                            ArrayList<Integer> selectedHours = new ArrayList<>();

                            for (int j = 0; j < freeHours.length; j++) {
                                Date lessonDate = Date.from(Instant.ofEpochSecond(item.getDay() + freeHours[j]));
                                vHours[j] = Lesson.DATE_HOUR_FORMAT.format(lessonDate);
                            }

                            builder2.setNegativeButton("CANCEL", null)
                                    .setMultiChoiceItems(vHours, null, (dialog1, which1, isChecked) -> {
                                        if (isChecked) selectedHours.add(freeHours[which1]);
                                        else if (selectedHours.contains(freeHours[which1]))
                                            selectedHours.remove(Integer.valueOf(freeHours[which1]));
                                    });

                            builder2.setPositiveButton("OK", (dialog1, which1) -> {
                                KVStorage kvStorage = getModel().getKVStorage();
                                if (kvStorage.getString(StorageConf.ACCOUNT_JWT) == null) {
                                    Toast.makeText(getContext(), SrvStatus.UNAUTHORIZED.toString(), Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (selectedHours.isEmpty()) {
                                    Toast.makeText(getContext(), getString(R.string.activity_main_select_hours_please), Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                apiManager.newBooking(teacher.getID(), item.getCourse().getID(), item.getDay(),
                                        selectedHours, (status1, response1) -> runOnUiThread(() -> {
                                            if (status1 == SrvStatus.OK && response1) {
                                                Toast.makeText(getContext(), R.string.activity_main_booking_added, Toast.LENGTH_SHORT).show();
                                                loadListData(true);
                                            } else {
                                                Toast.makeText(getContext(), status1.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }));
                            });
                        } else {
                            builder2.setMessage(R.string.activity_main_no_free_hours);
                            builder2.setPositiveButton("OK", null);
                        }

                        runOnUiThread(() -> builder2.create().show());
                    });
                } else {
                    builder.setMessage(R.string.activity_main_no_free_teachers);
                    builder.setPositiveButton("OK", null);
                }

                runOnUiThread(() -> builder.create().show());
            } else {
                runOnUiThread(() -> Toast.makeText(getContext(), status.toString(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    public String getTitle() {
        return title;
    }
}
