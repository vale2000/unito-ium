package it.unito.ium.myreps.ui.booking;

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

import java.util.Optional;

import butterknife.BindView;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.logic.api.ApiManager;
import it.unito.ium.myreps.logic.api.SrvStatus;
import it.unito.ium.myreps.logic.api.objects.Booking;
import it.unito.ium.myreps.ui.BaseFragment;

public final class BookingListFragment extends BaseFragment {
    @BindView(R.id.fragment_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_list_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_list_text_empty)
    TextView emptyText;
    private BookingListAdapter bookingListAdapter;

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
        bookingListAdapter.setItemClickListener((view, pos, item) -> editBooking(pos, (Booking) item));

        swipeRefreshLayout.setOnRefreshListener(this::loadBookingList);
    }

    private void loadBookingList() {
        swipeRefreshLayout.setRefreshing(true);

        ApiManager apiManager = getModel().getApiManager();
        apiManager.loadBookingList((status, response) -> runOnUiThread(() -> {
            if (status == SrvStatus.OK) {
                if (!(response == null || response.isEmpty())) {
                    bookingListAdapter.setDataSet(response);
                    emptyText.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(getContext(), status.toString(), Toast.LENGTH_SHORT).show();
                if (bookingListAdapter.getItemCount() == 0) {
                    emptyText.setVisibility(View.VISIBLE);
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }));
    }

    private void editBooking(int position, Booking item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.fragment_booking_set_status);
        builder.setItems(R.array.fragment_booking_status, (dialog, which) -> {
            Optional<Booking.Status> opStatus = Booking.Status.valueOf(which + 1);
            if (opStatus.isPresent()) {
                if (item.getStatus() == opStatus.get()) {
                    Toast.makeText(getContext(), R.string.fragment_booking_already_set, Toast.LENGTH_SHORT).show();
                    return;
                }

                ApiManager apiManager = getModel().getApiManager();
                apiManager.updateBooking(item.getID(), opStatus.get().toString(), (status, response) -> {
                    if (status == SrvStatus.OK && response) {
                        item.setStatus(opStatus.get());
                        runOnUiThread(() -> bookingListAdapter.notifyItemChanged(position, item));
                    } else {
                        runOnUiThread(() -> Toast.makeText(getContext(), R.string.fragment_booking_update_failed, Toast.LENGTH_SHORT).show());
                    }
                });
            }
        });

        builder.create().show();
    }
}
