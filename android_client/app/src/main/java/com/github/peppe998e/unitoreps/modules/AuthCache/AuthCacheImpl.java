package com.github.peppe998e.unitoreps.modules.AuthCache;

import android.content.SharedPreferences;

public class AuthCacheImpl implements AuthCache {
    private SharedPreferences sp;

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
        editor.putString("token", null);
        return editor.commit();
    }
}
