package it.unito.ium.myreps.ui.lesson;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.util.RecyclerViewRow;

public final class LessonListItem implements RecyclerViewRow {
    private final String title;
    private final String description;

    public LessonListItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitleText() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int getType() {
        return 0;
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView descTextView;

        public ViewHolder(@NonNull View v) {
            super(v);
            titleTextView = v.findViewById(R.id.rv_row_lesson_header);
            descTextView = v.findViewById(R.id.rv_row_lesson_description);
        }

        public void setTitleText(String s) {
            titleTextView.setText(s);
        }

        public void setDescText(String s) {
            descTextView.setText(s);
        }
    }
}
