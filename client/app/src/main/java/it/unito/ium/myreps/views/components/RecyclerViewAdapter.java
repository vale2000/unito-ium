package it.unito.ium.myreps.views.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unito.ium.myreps.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<String> mDataSet;
    private ItemClickListener itemClickListener;

    public RecyclerViewAdapter(List<String> mDataSet) {
        itemClickListener = null;
        this.mDataSet = mDataSet;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_repslist_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        String textName = mDataSet.get(position);
        holder.myTextView.setText(textName);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setDataSet(@NonNull List<String> mDataSet) {
        this.mDataSet = mDataSet;
        notifyDataSetChanged();
    }

    public void setItemClickListener(@NonNull ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private String getItem(int id) {
        return mDataSet.get(id);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView myTextView;

        ViewHolder(View v) {
            super(v);
            myTextView = v.findViewById(R.id.textName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onClick(v, getItem(getAdapterPosition()));
        }
    }

    public interface ItemClickListener {
        void onClick(View view, String item);
    }
}
