package it.unito.ium.myreps.model;

import android.content.Context;

import it.unito.ium.myreps.model.services.api.ApiManager;
import it.unito.ium.myreps.model.services.config.ConfigManager;

public interface Model {
    Context getAppContext();

    ConfigManager getConfigManager();

    ApiManager getApiManager();
}
