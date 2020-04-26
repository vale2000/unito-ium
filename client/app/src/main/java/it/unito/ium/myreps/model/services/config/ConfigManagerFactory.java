package it.unito.ium.myreps.model.services.config;

import it.unito.ium.myreps.model.Model;

public final class ConfigManagerFactory {
    public static ConfigManager instantiate(Model model) {
        return new ConfigManagerImpl(model);
    }
}
