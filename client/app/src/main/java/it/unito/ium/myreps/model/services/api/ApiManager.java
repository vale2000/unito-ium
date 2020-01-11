package it.unito.ium.myreps.model.services.api;

import it.unito.ium.myreps.utils.CallBack;

public interface ApiManager {
    String doLogin(String email, String password);

    String doRegistration(String email, String password);
}
