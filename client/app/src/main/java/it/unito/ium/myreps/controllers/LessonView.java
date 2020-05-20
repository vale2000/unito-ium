package it.unito.ium.myreps.controllers;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.unito.ium.myreps.R;
import it.unito.ium.myreps.components.RecyclerViewRow;

public final class LessonView extends BaseView {
    private RecyclerViewRow lesson;

    @BindView(R.id.lesson_header_subject)
    TextView headerSubject;
    @BindView(R.id.lesson_header_prof)
    TextView headerProfessor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_lesson);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        lesson = (RecyclerViewRow) getIntent().getSerializableExtra("lesson");
        headerSubject.setText(lesson.getSubject());
        headerProfessor.setText(lesson.getProfessor());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
