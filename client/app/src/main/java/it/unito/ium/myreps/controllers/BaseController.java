package it.unito.ium.myreps.controllers;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import it.unito.ium.myreps.Model;
import it.unito.ium.myreps.R;

abstract class BaseController extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme); // Restore AppTheme
        super.onCreate(savedInstanceState);
    }

    protected Model getAppModel() {
        return (Model) getApplication();
    }

}
