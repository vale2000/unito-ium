package it.unito.ium.myreps.controllers;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.model.services.api.objects.Lesson;

public final class LessonView extends BaseView {
    @BindView(R.id.view_lesson_header_course)
    TextView headerCourse;

    @BindView(R.id.view_lesson_header_extra)
    TextView headerExtra;

    @BindView(R.id.view_lesson_loading_bar)
    RelativeLayout loadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_lesson);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadLessonData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return false;
    }

    private void loadLessonData() {
        Lesson lesson = (Lesson) getIntent().getSerializableExtra("data");

        headerCourse.setText(lesson.getCourse().getName());
        headerExtra.setText(getString(R.string.rv_row_lesson_description, lesson.getTeachersNum()));

        getModel().getApiManager().loadLesson(0, (valid, response) -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            runOnUiThread(() ->
                    loadingView.setVisibility(View.GONE));
        });
    }
}
