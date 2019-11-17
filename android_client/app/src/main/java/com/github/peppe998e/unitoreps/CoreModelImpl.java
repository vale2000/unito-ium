package com.github.peppe998e.unitoreps;

import android.app.Application;
import android.content.SharedPreferences;

import com.github.peppe998e.unitoreps.modules.authcache.AuthCache;
import com.github.peppe998e.unitoreps.modules.authcache.AuthCacheImpl;
import com.github.peppe998e.unitoreps.modules.apiservice.ApiService;
import com.github.peppe998e.unitoreps.modules.apiservice.ApiServiceImpl;
import com.github.peppe998e.unitoreps.utils.Config;

/**
 * This class is the main MODEL of the application,
 * the main feature is that of being a mono-instance,
 * therefore all presenters share the same variables and methods
 *
 */
public class CoreModelImpl extends Application implements CoreModel {
    private AuthCache ac;
    private ApiService net;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize AuthCache Module
        SharedPreferences sp = getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        this.ac = new AuthCacheImpl(sp);

        // Initialize Network Module
        this.net = new ApiServiceImpl(Config.API_BASE_URL);
    }

    @Override
    public void onTerminate() {
        this.ac = null;
        this.net = null;

        super.onTerminate();
    }

    @Override
    public AuthCache getAuthCache() {
        return this.ac;
    }

    @Override
    public ApiService getNetwork() {
        return this.net;
    }
}