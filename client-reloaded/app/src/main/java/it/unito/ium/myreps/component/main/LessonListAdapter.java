package it.unito.ium.myreps.component.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.model.api.objects.Lesson;
import it.unito.ium.myreps.util.RecyclerItemBreak;
import it.unito.ium.myreps.util.RecyclerViewRow;

public final class LessonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<RecyclerViewRow> dataSet;
    private LessonListAdapter.itemClickListener itemClickListener;

    public LessonListAdapter() {
        dataSet = new ArrayList<>();
        itemClickListener = null;
    }

    public void setDataSet(ArrayList<RecyclerViewRow> dataSet) {
        this.dataSet = dataSet != null ? dataSet : new ArrayList<>();
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
        int layout = viewType == 0 ? R.layout.rv_row_lesson : R.layout.rv_row_break;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return viewType == 0 ? new LessonListAdapter.ViewHolder(view) : new RecyclerItemBreak.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerViewRow item = dataSet.get(position);

        switch (item.getType()) {
            case 0:
                LessonListAdapter.ViewHolder itemVH = (LessonListAdapter.ViewHolder) holder;
                Lesson itemData = (Lesson) item;
                itemVH.setTitleText(itemData.getCourse().getName());
                itemVH.setDescText(itemVH.itemView.getContext()
                        .getString(R.string.activity_main_rv_row_desc, itemData.getTeachersNum()));
                if (itemClickListener != null) {
                    itemVH.itemView.setOnClickListener(v -> itemClickListener.onClick(v, position, itemData));
                }
                break;
            case 1:
                RecyclerItemBreak.ViewHolder breakVH = (RecyclerItemBreak.ViewHolder) holder;
                RecyclerItemBreak breakData = (RecyclerItemBreak) item;
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
        void onClick(View view, int position, RecyclerViewRow item);
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
