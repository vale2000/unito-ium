package com.github.peppe998e.unitoreps.modules.authcache;

public interface AuthCache {
    Boolean saveToken(String token);
    String getToken();
    Boolean clearToken();

    /*
    Boolean saveAuth(String username, String password);
    String getUserName();
    String getPassword();
    Boolean clearAuth();
     */
}
