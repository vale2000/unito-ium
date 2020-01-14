package it.unito.ium.myreps.services.api;

public final class ApiManagerFactory {
    public static ApiManager instantiate() {
        return new ApiManagerImpl();
    }
}
