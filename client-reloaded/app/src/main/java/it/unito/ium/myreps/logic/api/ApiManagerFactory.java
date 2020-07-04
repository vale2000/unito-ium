package it.unito.ium.myreps.logic.api;

public final class ApiManagerFactory {
    public static ApiManager newInstance() {
        return new ApiManagerImpl();
    }
}
