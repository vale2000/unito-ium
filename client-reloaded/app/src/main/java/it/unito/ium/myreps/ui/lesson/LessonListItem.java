package it.unito.ium.myreps.ui.lesson;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.util.RecyclerViewRow;

public class LessonListItem implements RecyclerViewRow {
    private final String titleText;
    private final String descText;

    public LessonListItem(String titleText, String descText) {
        this.titleText = titleText;
        this.descText = descText;
    }

    public String getTitleText() {
        return titleText;
    }

    public String getDescText() {
        return descText;
    }

    @Override
    public int getType() {
        return 0;
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView lessonTitle;
        private final TextView lessonDesc;

        public ViewHolder(@NonNull View v) {
            super(v);
            lessonTitle = v.findViewById(R.id.rv_row_lesson_header);
            lessonDesc = v.findViewById(R.id.rv_row_lesson_description);
        }

        public void setLessonTitle(String s) {
            lessonTitle.setText(s);
        }

        public void setLessonDesc(String s) {
            lessonDesc.setText(s);
        }
    }
}
