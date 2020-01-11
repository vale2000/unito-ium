package it.unito.ium.myreps.model.services.api;

public final class ApiManagerFactory {
    public static ApiManager instantiate() {
        return new ApiManagerImpl();
    }
}
