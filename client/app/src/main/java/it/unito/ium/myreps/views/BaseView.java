package it.unito.ium.myreps.views;

import androidx.appcompat.app.AppCompatActivity;

import it.unito.ium.myreps.Controller;

abstract class BaseView extends AppCompatActivity {

    protected Controller getAppController() {
        return (Controller) getApplication();
    }

}
