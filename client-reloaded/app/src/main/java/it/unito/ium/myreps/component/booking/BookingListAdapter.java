package it.unito.ium.myreps.component.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.model.api.objects.Booking;
import it.unito.ium.myreps.util.RecyclerItemBreak;
import it.unito.ium.myreps.util.RecyclerViewRow;

public final class BookingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
        int layout = viewType == 0 ? R.layout.rv_row_booking : R.layout.rv_row_break;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return viewType == 0 ? new BookingListAdapter.ViewHolder(view) : new RecyclerItemBreak.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerViewRow item = dataSet.get(position);

        switch (item.getType()) {
            case 0:
                BookingListAdapter.ViewHolder itemVH = (BookingListAdapter.ViewHolder) holder;
                Booking itemData = (Booking) dataSet.get(position);

                itemVH.setTitleText(itemData.getLesson().getCourse().getName());
                itemVH.setStatusText(itemData.getStatus().toString());
                itemVH.setTeacherText(itemData.getLesson().getTeacher(0).getFullName());
                itemVH.setDateText(String.format(Locale.getDefault(), "%s, %s",
                        itemData.getLesson().getYearDay(), itemData.getLesson().getHour()));

                if (itemClickListener != null) {
                    itemVH.itemView.setOnClickListener(v -> itemClickListener.onClick(v, position, itemData));
                }
                break;
            case 1:
                RecyclerItemBreak.ViewHolder breakVH = (RecyclerItemBreak.ViewHolder) holder;
                RecyclerItemBreak breakData = (RecyclerItemBreak) item;
                breakVH.setTitleText(breakData.getTitleText());
                break;
        }


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @FunctionalInterface
    public interface itemClickListener {
        void onClick(View view, int position, RecyclerViewRow item);
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
}
