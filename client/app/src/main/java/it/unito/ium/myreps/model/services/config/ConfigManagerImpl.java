package it.unito.ium.myreps.model.services.config;

import android.app.Application;
import android.content.SharedPreferences;

final class ConfigManagerImpl implements ConfigManager {
    private final SharedPreferences sharedPreferences;

    ConfigManagerImpl(Application application) {
        this.sharedPreferences = application.getSharedPreferences("app_config_data", 0x0000 /* MODE_PRIVATE */);
    }

    @Override
    public boolean contains(String key) {
        return this.sharedPreferences.contains(key);
    }

    @Override
    public String getString(String key) {
        return this.sharedPreferences.getString(key, null);
    }

    @Override
    public void setString(String key, String value) {
        this.sharedPreferences.edit()
                .putString(key, value)
                .apply();
    }

    @Override
    public boolean getBoolean(String key) {
        return this.sharedPreferences.getBoolean(key, false);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        this.sharedPreferences.edit()
                .putBoolean(key, value)
                .apply();
    }

    @Override
    public int getInteger(String key) {
        return this.sharedPreferences.getInt(key, -1);
    }

    @Override
    public void setInteger(String key, int value) {
        this.sharedPreferences.edit()
                .putInt(key, value)
                .apply();
    }
}
