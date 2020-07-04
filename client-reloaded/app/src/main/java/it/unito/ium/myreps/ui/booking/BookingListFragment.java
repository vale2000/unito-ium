package it.unito.ium.myreps.ui.booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.logic.api.ApiManager;
import it.unito.ium.myreps.logic.api.SrvStatus;
import it.unito.ium.myreps.ui.BaseFragment;

public final class BookingListFragment extends BaseFragment {
    private BookingListAdapter bookingListAdapter;

    @BindView(R.id.fragment_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fragment_list_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.fragment_list_text_empty)
    TextView emptyText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = bindView(R.layout.fragment_list, inflater, container);

        emptyText.setText(R.string.fragment_booking_list_rv_empty);

        initBookingList();
        loadBookingList();

        return view;
    }

    private void initBookingList() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bookingListAdapter = new BookingListAdapter();
        recyclerView.setAdapter(bookingListAdapter);
        bookingListAdapter.setItemClickListener((view, item) -> {
            // TODO on CLICK
        });

        swipeRefreshLayout.setOnRefreshListener(this::loadBookingList);
    }

    private void loadBookingList() {
        swipeRefreshLayout.setRefreshing(true);

        ApiManager apiManager = getModel().getApiManager();
        apiManager.loadBookingList((status, response) -> runOnUiThread(() -> {
            if (status == SrvStatus.OK) bookingListAdapter.setDataSet(response);
            else Toast.makeText(getContext(), status.toString(), Toast.LENGTH_LONG).show();

            emptyText.setVisibility((response == null || response.isEmpty()) ? View.VISIBLE : View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }));
    }
}
