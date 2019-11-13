package com.github.peppe998e.unitoreps;

import android.app.Application;

/**
 * This class is the main MODEL of the application,
 * the main feature is that of being a mono-instance,
 * therefore all presenters share the same variables and methods
 *
 */
public class CoreModelImp extends Application implements CoreModel {

    @Override
    public void onCreate() {
        // Do something when app starts
        super.onCreate();
    }

}
