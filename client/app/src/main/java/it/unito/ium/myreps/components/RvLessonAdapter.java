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
import it.unito.ium.myreps.model.services.api.objects.Lesson;

public final class RvLessonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<RecyclerViewRow> dataSet;
    private List<RecyclerViewRow> dataSetShown;

    private RowClickListener rowClickListener;

    public RvLessonAdapter() {
        this.dataSet = new ArrayList<>();
        this.dataSetShown = this.dataSet;
        this.rowClickListener = null;
    }

    public void setDataSet(List<RecyclerViewRow> dataSet) {
        this.dataSet = dataSet;
        this.dataSetShown = dataSet;
        notifyDataSetChanged();
    }

    public void setRowClickListener(@NonNull RowClickListener rowClickListener) {
        this.rowClickListener = rowClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        RecyclerViewRow item = dataSetShown.get(position);
        return item.getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = viewType == 0 ? R.layout.rv_row_separator : R.layout.rv_row_lesson;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return viewType == 0 ? new RvRowSeparator.ViewHolder(view) : new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerViewRow item = dataSetShown.get(position);

        switch (item.getType()) {
            case 0:
                RvRowSeparator.ViewHolder viewHolder0 = (RvRowSeparator.ViewHolder) holder;
                viewHolder0.setHeader(dataSetShown.get(position).getHeader());
                break;
            case 1:
                RvLessonAdapter.ViewHolder viewHolder1 = (RvLessonAdapter.ViewHolder) holder;
                Lesson lesson = (Lesson) item;
                viewHolder1.courseName.setText(lesson.getCourse().getName());
                String description = viewHolder1.itemView.getContext().getString(R.string.rv_row_lesson_description, lesson.getTeachersNum());
                viewHolder1.bookingStatus.setText(description);
                break;
        }
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
                        if (row.getType() == 0) continue;

                        Lesson lesson = (Lesson) row;
                        if (!lesson.getCourse().getName().toLowerCase().contains(charString))
                            continue;

                        filteredList.add(row);
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
        private final TextView courseName;
        private final TextView bookingStatus;

        ViewHolder(@NonNull View v) {
            super(v);

            courseName = v.findViewById(R.id.rv_row_lesson_header);
            bookingStatus = v.findViewById(R.id.rv_row_lesson_description);

            v.setOnClickListener(v1 -> {
                if (rowClickListener != null)
                    rowClickListener.onClick(v1, dataSetShown.get(getAdapterPosition()));
            });
        }
    }

    @FunctionalInterface
    public interface RowClickListener {
        void onClick(View view, RecyclerViewRow item);
    }
}
