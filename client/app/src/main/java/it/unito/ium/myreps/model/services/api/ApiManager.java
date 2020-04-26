package it.unito.ium.myreps.model.services.api;

public interface ApiManager {
    ApiManager setCredentials(String email, String password);

    void doLogin();

    void doRegistration();
}
