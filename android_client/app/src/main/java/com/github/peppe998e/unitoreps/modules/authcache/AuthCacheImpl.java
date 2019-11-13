package com.github.peppe998e.unitoreps.modules.authcache;

import android.content.SharedPreferences;

public class AuthCacheImpl implements AuthCache {
    private final SharedPreferences sp;

    public AuthCacheImpl(SharedPreferences sp) {
        this.sp = sp;
    }

    @Override
    public Boolean saveToken(String token) {
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putString("token", token);
        return editor.commit();
    }

    @Override
    public String getToken() {
        return sp.getString("token", null);
    }

    @Override
    public Boolean clearToken() {
        SharedPreferences.Editor editor = this.sp.edit();
        editor.remove("token");
        return editor.commit();
    }
}
