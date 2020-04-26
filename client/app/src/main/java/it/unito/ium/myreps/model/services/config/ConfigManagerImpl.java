package it.unito.ium.myreps.model.services.config;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import it.unito.ium.myreps.model.Model;

final class ConfigManagerImpl implements ConfigManager {
    private final SharedPreferences sharedPreferences;

    ConfigManagerImpl(Model model) {
        this.sharedPreferences = ((Application) model).getSharedPreferences("app_config_data", Context.MODE_PRIVATE);
    }

    @Override
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    @Override
    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    @Override
    public void setString(String key, String value) {
        sharedPreferences.edit()
                .putString(key, value)
                .apply();
    }

    @Override
    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        sharedPreferences.edit()
                .putBoolean(key, value)
                .apply();
    }

    @Override
    public int getInteger(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    @Override
    public void setInteger(String key, int value) {
        sharedPreferences.edit()
                .putInt(key, value)
                .apply();
    }
}
