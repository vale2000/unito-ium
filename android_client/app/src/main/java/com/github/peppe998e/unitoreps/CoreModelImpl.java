package com.github.peppe998e.unitoreps;

import android.app.Application;
import android.content.SharedPreferences;

import com.github.peppe998e.unitoreps.modules.AuthCache.AuthCache;
import com.github.peppe998e.unitoreps.modules.AuthCache.AuthCacheImpl;

/**
 * This class is the main MODEL of the application,
 * the main feature is that of being a mono-instance,
 * therefore all presenters share the same variables and methods
 *
 */
public class CoreModelImpl extends Application implements CoreModel {
    private AuthCache ac;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize AuthCache Module
        SharedPreferences sp = getSharedPreferences("AuthCache", MODE_PRIVATE);
        this.ac = new AuthCacheImpl(sp);
    }

    @Override
    public AuthCache getAuthCache() {
        return this.ac;
    }
}
