package it.unito.ium.myreps.ui.booking;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.util.RecyclerViewRow;

public final class BookingListItem implements RecyclerViewRow {
    private final String title;
    private final String status;
    private final String teacher;
    private final Date date;

    public BookingListItem(String title, String status, String teacher, Date date) {
        this.title = title;
        this.status = status;
        this.teacher = teacher;
        this.date = date;
    }

    @Override
    public String getTitleText() {
        return title;
    }

    public String getStatusText() {
        return status;
    }

    public String getTeacherText(){
        return teacher;
    }

    public String getDateText() {
        return null; // TODO SimpleDateFormat
    }

    @Override
    public int getType() {
        return 0;
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
