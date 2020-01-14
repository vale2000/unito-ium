package it.unito.ium.myreps;

import android.app.Application;

import it.unito.ium.myreps.services.api.ApiManager;
import it.unito.ium.myreps.services.api.ApiManagerFactory;
import it.unito.ium.myreps.services.config.ConfigManager;
import it.unito.ium.myreps.services.config.ConfigManagerFactory;

public final class AppModel extends Application implements Model {
    private ConfigManager configManager;
    private ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        this.configManager = ConfigManagerFactory.instantiate( this);
        this.apiManager = ApiManagerFactory.instantiate();
    }

    @Override
    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    @Override
    public ApiManager getApiManager() {
        return this.apiManager;
    }
}
