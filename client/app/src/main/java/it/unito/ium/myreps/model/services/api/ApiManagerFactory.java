package it.unito.ium.myreps.model.services.api;

import it.unito.ium.myreps.AppConfig;

public final class ApiManagerFactory {
    public static ApiManager instantiate() {
        return new ApiManagerImpl(AppConfig.SERVER_HOST);
    }
}
