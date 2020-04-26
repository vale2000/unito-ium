package it.unito.ium.myreps.model;

import android.app.Application;
import android.content.Context;

import it.unito.ium.myreps.model.services.api.ApiManager;
import it.unito.ium.myreps.model.services.api.ApiManagerFactory;
import it.unito.ium.myreps.model.services.config.ConfigManager;
import it.unito.ium.myreps.model.services.config.ConfigManagerFactory;

public final class AppModel extends Application implements Model {
    private ConfigManager configManager;
    private ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        configManager = ConfigManagerFactory.instantiate(this);
        apiManager = ApiManagerFactory.instantiate(this);
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public ApiManager getApiManager() {
        return apiManager;
    }
}
