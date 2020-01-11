package it.unito.ium.myreps.model;

import it.unito.ium.myreps.model.services.api.ApiManager;
import it.unito.ium.myreps.model.services.config.ConfigManager;

public interface Model {
    ConfigManager getConfigManager();

    ApiManager getApiManager();
}
