package it.unito.ium.myreps.model.services.config;

import android.app.Application;

public final class ConfigManagerFactory {
    public static ConfigManager instantiate(Application application) {
        return new ConfigManagerImpl(application);
    }
}
