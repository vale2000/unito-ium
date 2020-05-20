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
    private List<RecyclerViewRow> dataSet;
    private List<RecyclerViewRow> dataSetShown;

    private ItemClickListener itemClickListener;

    public RecyclerViewAdapter() {
        this.itemClickListener = null;
        this.dataSet = new ArrayList<>();
        this.dataSetShown = this.dataSet;
    }

    public void setDataSet(List<RecyclerViewRow> dataset) {
        this.dataSet = dataset;
        this.dataSetShown = dataset;
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
        RecyclerViewRow item = dataSetShown.get(position);
        holder.headerText.setText(item.getHeader());
        holder.descText.setText(item.getDescription());
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
                        if (row.getHeader().toLowerCase().contains(charString)) {
                            filteredList.add(row);
                        }
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
        private final TextView headerText;
        private final TextView descText;

        ViewHolder(View v) {
            super(v);

            headerText = v.findViewById(R.id.rv_row_header);
            descText = v.findViewById(R.id.rv_row_description);

            v.setOnClickListener(v1 -> {
                if (itemClickListener != null)
                    itemClickListener.onClick(v, dataSetShown.get(getAdapterPosition()));
            });
        }
    }

    @FunctionalInterface
    public interface ItemClickListener {
        void onClick(View view, RecyclerViewRow item);
    }
}
