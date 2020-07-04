package it.unito.ium.myreps.logic;

import android.app.Application;

import it.unito.ium.myreps.logic.api.ApiManager;
import it.unito.ium.myreps.logic.api.ApiManagerFactory;
import it.unito.ium.myreps.logic.storage.KVStorage;
import it.unito.ium.myreps.logic.storage.KVStorageFactory;

public final class Model extends Application {
    private KVStorage kvStorage;
    private ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        kvStorage = KVStorageFactory.newInstance(getApplicationContext());
        apiManager = ApiManagerFactory.newInstance();
    }

    public KVStorage getKVStorage() {
        return kvStorage;
    }

    public ApiManager getApiManager() {
        return apiManager;
    }
}
