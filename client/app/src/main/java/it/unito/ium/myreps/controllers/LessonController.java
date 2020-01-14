package it.unito.ium.myreps.controllers;

import android.os.Bundle;

import androidx.annotation.Nullable;

import it.unito.ium.myreps.R;

public final class LessonController extends BaseController {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_view);
    }
}
