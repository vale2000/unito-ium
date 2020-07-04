package it.unito.ium.myreps.ui.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.util.RecyclerViewRow;

final class BookingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
        int layout = R.layout.rv_row_booking;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new BookingListItem.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BookingListItem.ViewHolder itemVH = (BookingListItem.ViewHolder) holder;
        BookingListItem itemData = (BookingListItem) dataSet.get(position);

        itemVH.setTitleText(itemData.getTitleText());
        itemVH.setStatusText(itemData.getStatusText());
        itemVH.setTeacherText(itemData.getTeacherText());
        itemVH.setDateText(itemData.getDateText());

        if (itemClickListener != null) {
            itemVH.itemView.setOnClickListener(v -> itemClickListener.onClick(v, itemData));
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @FunctionalInterface
    public interface itemClickListener {
        void onClick(View view, RecyclerViewRow item);
    }
}
