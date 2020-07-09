package it.unito.ium.myreps.util;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.unito.ium.myreps.R;

public final class RecyclerItemBreak extends RecyclerViewRow {
    private final String title;

    public RecyclerItemBreak(String title) {
        this.title = title;
    }

    public String getTitleText() {
        return title;
    }

    @Override
    public int getType() {
        return 1;
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;

        public ViewHolder(@NonNull View v) {
            super(v);
            titleTextView = v.findViewById(R.id.rv_row_separator_text);
        }

        public void setTitleText(String s) {
            titleTextView.setText(s);
        }
    }
}