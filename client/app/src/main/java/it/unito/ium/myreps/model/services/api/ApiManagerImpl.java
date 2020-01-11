package it.unito.ium.myreps.model.services.api;

final class ApiManagerImpl implements ApiManager {
    private final String serverHost;

    ApiManagerImpl(String serverHost) {
        this.serverHost = serverHost;
    }
}
