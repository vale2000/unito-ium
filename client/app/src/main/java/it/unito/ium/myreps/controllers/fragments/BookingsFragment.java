package it.unito.ium.myreps.controllers.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.components.RecyclerViewRow;
import it.unito.ium.myreps.components.RvBookingAdapter;
import it.unito.ium.myreps.controllers.LessonView;
import it.unito.ium.myreps.model.services.api.ServerError;
import it.unito.ium.myreps.model.services.api.objects.Booking;

public final class BookingsFragment extends BaseFragment {
    private RvBookingAdapter rvBookingAdapter;
    private SearchView searchView;

    @BindView(R.id.fragment_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fragment_list_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.fragment_list_text_empty)
    TextView emptyText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createView(inflater, R.layout.fragment_list, container);
        setHasOptionsMenu(true);

        emptyText.setText(R.string.fragment_bookings_list_rv_empty);

        initRecyclerView();
        loadRecyclerView();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_rv_list, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchView = (SearchView) menu.findItem(R.id.fragment_rv_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setOnQueryTextListener(mOnQueryTextListener);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    private final SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            rvBookingAdapter.getFilter().filter(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            rvBookingAdapter.getFilter().filter(query);
            return true;
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return item.getItemId() == R.id.fragment_rv_search;
    }

    private void closeSearchView() {
        if (!searchView.hasFocus()) return;

        searchView.clearFocus();
        searchView.setIconified(true);

        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        rvBookingAdapter = new RvBookingAdapter();

        rvBookingAdapter.setRowClickListener((view, item) -> {
            Intent i = new Intent(getContext(), LessonView.class);
            i.putExtra("lesson_id", (Booking) item);
            startActivity(i);
        });

        recyclerView.setAdapter(rvBookingAdapter);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            closeSearchView();
            loadRecyclerView();
        });
    }

    private void loadRecyclerView() {
        swipeRefreshLayout.setRefreshing(true);
        getModel().getApiManager().loadBookings((valid, response) -> {
            ServerError serverError = ServerError.SERVER_OFFLINE;
            try {
                if (valid) {
                    boolean ok = response.getBoolean("ok");
                    if (ok) {
                        ArrayList<RecyclerViewRow> bookingList = new ArrayList<>();
                        JSONArray data = response.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonLesson = data.getJSONObject(i);
                            bookingList.add(new Booking(jsonLesson));
                        }

                        runOnUiThread(() -> {
                            rvBookingAdapter.setDataSet(bookingList);
                            swipeRefreshLayout.setRefreshing(false);
                            emptyText.setVisibility(View.GONE);
                        });
                        return;
                    }

                    String error = response.getString("error");
                    serverError = ServerError.fromString(error);
                    if (serverError == null) {
                        serverError = ServerError.UNKNOWN_ERROR;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                serverError = ServerError.UNKNOWN_ERROR;
            }

            ServerError finalServerError = serverError;
            runOnUiThread(() -> {
                Toast.makeText(getContext(), finalServerError.toString(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
                rvBookingAdapter.setDataSet(new ArrayList<>());
                emptyText.setVisibility(View.VISIBLE);
            });
        });
    }
}
