package it.unito.ium.myreps.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.model.services.api.objects.Booking;

public final class RvBookingAdapter extends RecyclerView.Adapter<RvBookingAdapter.ViewHolder> implements Filterable {
    private List<RecyclerViewRow> dataSet;
    private List<RecyclerViewRow> dataSetShown;

    private RowClickListener rowClickListener;

    public RvBookingAdapter() {
        this.dataSet = new ArrayList<>();
        this.dataSetShown = this.dataSet;
        this.rowClickListener = null;
    }

    public void setDataSet(List<RecyclerViewRow> dataSet) {
        this.dataSet = dataSet;
        this.dataSetShown = dataSet;
        notifyDataSetChanged();
    }

    public void setRowClickListener(@NonNull RowClickListener rowClickListener) {
        this.rowClickListener = rowClickListener;
    }

    @NonNull
    @Override
    public RvBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvBookingAdapter.ViewHolder holder, int position) {
        Booking item = (Booking)dataSetShown.get(position);
        String teacher = item.getTeacher().getName() + " " + item.getTeacher().getSurname();

        holder.bookingName.setText(item.getCourse().getName());
        holder.bookingStatus.setText(item.getStatus().name());
        holder.bookingTeacher.setText(teacher);
        holder.bookingDate.setText(""+item.getLesson().getDate());
    }

    @Override
    public int getItemCount() {
        return dataSetShown.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                String charString = charSequence.toString().toLowerCase();

                if (charString.isEmpty()) {
                    results.values = dataSet;
                } else {
                    List<RecyclerViewRow> filteredList = new ArrayList<>();
                    for (RecyclerViewRow row : dataSet) {
                        Booking booking = (Booking) row;

                        if (!booking.getCourse().getName().toLowerCase().contains(charString)) continue;

                        filteredList.add(row);
                    }
                    results.values = filteredList;
                }

                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataSetShown = (ArrayList<RecyclerViewRow>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView bookingName;
        private final TextView bookingStatus;
        private final TextView bookingTeacher;
        private final TextView bookingDate;

        ViewHolder(@NonNull View v) {
            super(v);

            bookingName = v.findViewById(R.id.rv_row_booking_header);
            bookingStatus = v.findViewById(R.id.rv_row_booking_status);
            bookingTeacher = v.findViewById(R.id.rv_row_booking_teacher);
            bookingDate = v.findViewById(R.id.rv_row_booking_date);

            v.setOnClickListener(v1 -> {
                if (rowClickListener != null)
                    rowClickListener.onClick(v1, dataSetShown.get(getAdapterPosition()));
            });
        }
    }

    @FunctionalInterface
    public interface RowClickListener {
        void onClick(View view, RecyclerViewRow item);
    }
}
