package it.unito.ium.myreps.model;

import android.app.Application;

import it.unito.ium.myreps.model.api.ApiManager;
import it.unito.ium.myreps.model.api.ApiManagerFactory;
import it.unito.ium.myreps.model.storage.KVStorage;
import it.unito.ium.myreps.model.storage.KVStorageFactory;

public final class Model extends Application {
    private KVStorage kvStorage;
    private ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        kvStorage = KVStorageFactory.newInstance(getApplicationContext());
        apiManager = ApiManagerFactory.newInstance(this);
    }

    public KVStorage getKVStorage() {
        return kvStorage;
    }

    public ApiManager getApiManager() {
        return apiManager;
    }
}
