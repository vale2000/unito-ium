package it.unito.ium.myreps.views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import it.unito.ium.myreps.Controller;
import it.unito.ium.myreps.R;

abstract class BaseView extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    protected Controller getAppController() {
        return (Controller) getApplication();
    }

}
