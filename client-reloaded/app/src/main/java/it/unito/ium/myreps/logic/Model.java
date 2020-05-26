package it.unito.ium.myreps.logic;

import android.app.Application;

import it.unito.ium.myreps.logic.api.ApiManager;
import it.unito.ium.myreps.logic.config.KVStorage;

public final class Model extends Application {
    private KVStorage kvStorage;
    private ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        kvStorage = new KVStorage(getApplicationContext());
        apiManager = new ApiManager();
    }

    public KVStorage getKvStorage() {
        return kvStorage;
    }

    public ApiManager getApiManager() {
        return apiManager;
    }
}
