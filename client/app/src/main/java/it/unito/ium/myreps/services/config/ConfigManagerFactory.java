package it.unito.ium.myreps.services.config;

import android.app.Application;

public final class ConfigManagerFactory {
    public static ConfigManager instantiate(Application application) {
        return new ConfigManagerImpl(application);
    }
}
