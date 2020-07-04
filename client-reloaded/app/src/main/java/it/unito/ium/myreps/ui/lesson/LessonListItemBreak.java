package it.unito.ium.myreps.ui.lesson;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.util.RecyclerViewRow;

public final class LessonListItemBreak implements RecyclerViewRow {
    private final String text;

    public LessonListItemBreak(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public int getType() {
        return 1;
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(@NonNull View v) {
            super(v);
            textView = v.findViewById(R.id.rv_row_separator_text);
        }

        public void setBreakText(String s) {
            textView.setText(s);
        }
    }
}