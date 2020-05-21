package it.unito.ium.myreps.components;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.unito.ium.myreps.R;

public class RvRowSeparator extends RecyclerViewRow {
    private final String header;

    public RvRowSeparator(String header) {
        this.header = header;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public String getHeader() {
        return header;
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
