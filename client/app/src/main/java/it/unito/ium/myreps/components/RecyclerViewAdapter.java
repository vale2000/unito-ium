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

public final class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {
    private List<RecyclerViewRow> dataset;
    private List<RecyclerViewRow> datasetShown;

    private ItemClickListener itemClickListener;

    public RecyclerViewAdapter() {
        this.itemClickListener = null;
        this.dataset = new ArrayList<>();
        this.datasetShown = this.dataset;
    }

    public void setDataSet(List<RecyclerViewRow> dataset) {
        this.dataset = dataset;
        this.datasetShown = dataset;
        notifyDataSetChanged();
    }

    public void setItemClickListener(@NonNull ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        RecyclerViewRow item = datasetShown.get(position);
        holder.headerText.setText(item.getSubject());
        holder.hoursText.setText(RecyclerViewRow.minutesToString(item.getMinutes()));
        holder.profText.setText(item.getProfessor());
    }

    @Override
    public int getItemCount() {
        return datasetShown.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                String charString = charSequence.toString().toLowerCase();

                if (charString.isEmpty()) {
                    results.values = dataset;
                } else {
                    List<RecyclerViewRow> filteredList = new ArrayList<>();
                    for (RecyclerViewRow row : dataset) {
                        if (row.getSubject().toLowerCase().contains(charString) ||
                                row.getProfessor().toLowerCase().contains(charString)) {
                            filteredList.add(row);
                        }

                    }
                    results.values = filteredList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                datasetShown = (ArrayList<RecyclerViewRow>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView headerText;
        private final TextView hoursText;
        private final TextView profText;

        ViewHolder(View v) {
            super(v);

            headerText = v.findViewById(R.id.rv_row_header);
            hoursText = v.findViewById(R.id.rv_row_hours);
            profText = v.findViewById(R.id.rv_row_prof);

            v.setOnClickListener(v1 -> {
                if (itemClickListener != null)
                    itemClickListener.onClick(v, datasetShown.get(getAdapterPosition()));
            });
        }
    }

    @FunctionalInterface
    public interface ItemClickListener {
        void onClick(View view, RecyclerViewRow item);
    }
}
