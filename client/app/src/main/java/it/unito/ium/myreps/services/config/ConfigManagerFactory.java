package it.unito.ium.myreps.services.config;

import it.unito.ium.myreps.Model;

public final class ConfigManagerFactory {
    public static ConfigManager instantiate(Model model) {
        return new ConfigManagerImpl(model);
    }
}
