package it.unito.ium.myreps.model;

import android.app.Application;

import it.unito.ium.myreps.Controller;
import it.unito.ium.myreps.model.services.api.ApiManager;
import it.unito.ium.myreps.model.services.api.ApiManagerFactory;
import it.unito.ium.myreps.model.services.config.ConfigManager;
import it.unito.ium.myreps.model.services.config.ConfigManagerFactory;

final class ModelImpl implements Model {
    private final ConfigManager configManager;
    private final ApiManager apiManager;

    ModelImpl(Controller appController) {
        this.configManager = ConfigManagerFactory.instantiate((Application) appController);
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
