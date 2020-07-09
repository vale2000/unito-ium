package it.unito.ium.myreps.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.constants.StorageConstants;
import it.unito.ium.myreps.logic.api.ApiManager;
import it.unito.ium.myreps.logic.api.SrvStatus;
import it.unito.ium.myreps.logic.api.objects.Lesson;
import it.unito.ium.myreps.logic.api.objects.User;
import it.unito.ium.myreps.logic.storage.KVStorage;
import it.unito.ium.myreps.ui.BaseActivity;
import it.unito.ium.myreps.ui.account.AccountActivity;
import it.unito.ium.myreps.ui.login.LoginActivity;

public final class MainActivity extends BaseActivity {
    private LessonListAdapter lessonListAdapter;

    @BindView(R.id.fragment_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fragment_list_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.fragment_list_text_empty)
    TextView emptyText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme); // Restore App Theme
        setContentView(R.layout.activity_main);

        emptyText.setText(R.string.activity_main_rv_empty);

        initLessonList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLessonList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        KVStorage kvStorage = getModel().getKVStorage();
        boolean loggedIn = kvStorage.getString(StorageConstants.ACCOUNT_JWT) != null;
        if (item.getItemId() == R.id.activity_main_account_goto) {
            startActivity(new Intent(this, loggedIn ? AccountActivity.class : LoginActivity.class));
            return true;
        }
        return false;
    }

    private void initLessonList() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lessonListAdapter = new LessonListAdapter();
        recyclerView.setAdapter(lessonListAdapter);
        lessonListAdapter.setItemClickListener((view, pos, item) -> {
            loadLesson((Lesson) item);
        });

        swipeRefreshLayout.setOnRefreshListener(this::loadLessonList);
    }

    private void loadLessonList() {
        swipeRefreshLayout.setRefreshing(true);

        ApiManager apiManager = getModel().getApiManager();
        apiManager.loadLessonList((status, response) -> runOnUiThread(() -> {
            if (status == SrvStatus.OK) {
                lessonListAdapter.setDataSet(response);
            } else {
                Toast.makeText(this, status.toString(), Toast.LENGTH_LONG).show();
                lessonListAdapter.setDataSet(null);
            }

            emptyText.setVisibility((response == null || response.isEmpty()) ? View.VISIBLE : View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }));
    }

    private void loadLesson(Lesson item) {
        ApiManager apiManager = getModel().getApiManager();
        apiManager.loadLesson(item.getDay(), item.getCourse().getID(), (status, response) -> {
            if (status == SrvStatus.OK) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.activity_main_select_teacher);

                if (response.getTeachersNum() > 0) {
                    String[] teachers = new String[response.getTeachersNum()];
                    for (int i = 0; i < teachers.length; i++) {
                        teachers[i] = response.getTeacher(i).getFullName();
                    }

                    builder.setItems(teachers, (dialog, which) -> {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
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

                            builder2.setPositiveButton("OK", (dialog1, which1) -> apiManager
                                    .newBooking(teacher.getID(), item.getCourse().getID(), item.getDay(),
                                            selectedHours, (status1, response1) -> runOnUiThread(() -> {
                                                if (status1 == SrvStatus.OK && response1) {
                                                    Toast.makeText(this, R.string.activity_main_booking_added, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(this, status1.toString(), Toast.LENGTH_LONG).show();
                                                }
                                            })));
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
                runOnUiThread(() -> Toast.makeText(this, status.toString(), Toast.LENGTH_LONG).show());
            }
        });
    }
}
