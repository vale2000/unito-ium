package it.unito.ium.myreps.ui.lesson;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.unito.ium.myreps.R;

public final class LessonListSeparator {
    private final String header;

    public LessonListSeparator(String header) {
        this.header = header;
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView separatorHeader;

        public ViewHolder(@NonNull View v) {
            super(v);
            separatorHeader = v.findViewById(R.id.rv_row_separator_header);
        }

        public void setHeader(String s) {
            separatorHeader.setText(s);
        }
    }
}