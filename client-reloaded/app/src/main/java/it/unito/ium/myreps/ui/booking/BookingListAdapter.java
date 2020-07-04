package it.unito.ium.myreps.ui.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.logic.api.objects.Booking;
import it.unito.ium.myreps.util.RecyclerViewRow;

final class BookingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<RecyclerViewRow> dataSet;
    private itemClickListener itemClickListener;

    public BookingListAdapter() {
        dataSet = new ArrayList<>();
        itemClickListener = null;
    }

    public void setDataSet(ArrayList<RecyclerViewRow> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public void setItemClickListener(BookingListAdapter.itemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        RecyclerViewRow item = dataSet.get(position);
        return item.getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.rv_row_booking;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new BookingListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BookingListAdapter.ViewHolder itemVH = (BookingListAdapter.ViewHolder) holder;
        Booking itemData = (Booking) dataSet.get(position);

        itemVH.setTitleText(itemData.getLesson().getCourse().getName());
        itemVH.setStatusText(itemData.getStatus().toString());
        itemVH.setTeacherText(itemData.getLesson().getTeacher(0).getName());
        itemVH.setDateText(itemData.getLesson().getYearDay());

        if (itemClickListener != null) {
            itemVH.itemView.setOnClickListener(v -> itemClickListener.onClick(v, itemData));
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView statusTextView;
        private final TextView teacherTextView;
        private final TextView dateTextView;

        public ViewHolder(@NonNull View v) {
            super(v);
            titleTextView = v.findViewById(R.id.rv_row_booking_header);
            statusTextView = v.findViewById(R.id.rv_row_booking_status);
            teacherTextView = v.findViewById(R.id.rv_row_booking_teacher);
            dateTextView = v.findViewById(R.id.rv_row_booking_date);
        }

        public void setTitleText(String s) {
            titleTextView.setText(s);
        }

        public void setStatusText(String s) {
            statusTextView.setText(s);
        }

        public void setTeacherText(String s) {
            teacherTextView.setText(s);
        }

        public void setDateText(String s) {
            dateTextView.setText(s);
        }
    }

    @FunctionalInterface
    public interface itemClickListener {
        void onClick(View view, RecyclerViewRow item);
    }
}
