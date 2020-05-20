package it.unito.ium.myreps.model;

import android.app.Application;
import android.content.Context;

import it.unito.ium.myreps.model.services.api.ApiManager;
import it.unito.ium.myreps.model.services.api.ApiManagerFactory;
import it.unito.ium.myreps.model.services.config.ConfigKey;
import it.unito.ium.myreps.model.services.config.ConfigManager;
import it.unito.ium.myreps.model.services.config.ConfigManagerFactory;

public final class AppModel extends Application implements Model {
    private ConfigManager<ConfigKey> configManager;
    private ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        configManager = ConfigManagerFactory.newInstance(this);
        apiManager = ApiManagerFactory.newInstance(this);
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public ConfigManager<ConfigKey> getConfigManager() {
        return configManager;
    }

    @Override
    public ApiManager getApiManager() {
        return apiManager;
    }
}
