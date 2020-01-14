package it.unito.ium.myreps;

import it.unito.ium.myreps.services.api.ApiManager;
import it.unito.ium.myreps.services.config.ConfigManager;

public interface Model {
    ConfigManager getConfigManager();

    ApiManager getApiManager();
}
