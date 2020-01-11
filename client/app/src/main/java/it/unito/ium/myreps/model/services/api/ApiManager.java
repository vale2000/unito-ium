package it.unito.ium.myreps.model.services.api;

import org.json.JSONObject;

public interface ApiManager {
    JSONObject doLogin(String email, String password);

    JSONObject doRegistration(String email, String password);
}
