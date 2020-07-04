package it.unito.ium.myreps.ui.lesson;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.util.RecyclerViewRow;

final class LessonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<RecyclerViewRow> dataSet;
    private LessonListAdapter.itemClickListener itemClickListener;

    public LessonListAdapter() {
        dataSet = new ArrayList<>();
        itemClickListener = null;
    }

    public void setDataSet(ArrayList<RecyclerViewRow> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public void setItemClickListener(LessonListAdapter.itemClickListener itemClickListener) {
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
        int layout = viewType == 0 ? R.layout.rv_row_lesson : R.layout.rv_row_separator;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return viewType == 0 ? new LessonListItem.ViewHolder(view) : new LessonListItemBreak.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerViewRow item = dataSet.get(position);

        switch (item.getType()) {
            case 0:
                LessonListItem.ViewHolder itemVH = (LessonListItem.ViewHolder) holder;
                LessonListItem itemData = (LessonListItem) item;
                itemVH.setTitleText(itemData.getTitleText());
                itemVH.setDescText(itemData.getDescription());

                if (itemClickListener != null) {
                    itemVH.itemView.setOnClickListener(v -> itemClickListener.onClick(v, itemData));
                }
                break;
            case 1:
                LessonListItemBreak.ViewHolder breakVH = (LessonListItemBreak.ViewHolder) holder;
                LessonListItemBreak breakData = (LessonListItemBreak) item;
                breakVH.setTitleText(breakData.getTitleText());
                break;
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
