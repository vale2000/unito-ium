package it.unito.ium.myreps.controllers;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import it.unito.ium.myreps.R;
import it.unito.ium.myreps.components.RecyclerViewRow;

public final class LessonController extends BaseController {
    private RecyclerViewRow lesson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lesson = (RecyclerViewRow) getIntent().getSerializableExtra("lesson");
        Toast.makeText(this, lesson.getSubject(), Toast.LENGTH_SHORT).show();
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
