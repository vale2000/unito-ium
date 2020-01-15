package it.unito.ium.myreps;

import android.content.Context;

import it.unito.ium.myreps.services.api.ApiManager;
import it.unito.ium.myreps.services.config.ConfigManager;

public interface Model {
    Context getAppContext();

    ConfigManager getConfigManager();

    ApiManager getApiManager();
}
