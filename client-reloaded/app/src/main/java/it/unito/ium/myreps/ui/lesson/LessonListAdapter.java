package it.unito.ium.myreps.ui.lesson;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

final class LessonListAdapter extends RecyclerView.Adapter<LessonListAdapter.ViewHolder> {
    @NonNull
    @Override
    public LessonListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull LessonListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View v) {
            super(v);
        }
    }

    @FunctionalInterface
    public interface onItemClickListener {
        void onClick(View view, String item);
    }
}
