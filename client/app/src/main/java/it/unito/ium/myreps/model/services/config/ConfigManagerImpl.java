package it.unito.ium.myreps.model.services.config;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import it.unito.ium.myreps.model.Model;

final class ConfigManagerImpl<T> implements ConfigManager<T> {
    private final SharedPreferences sharedPreferences;

    ConfigManagerImpl(Model model) {
        sharedPreferences = ((Application) model).getSharedPreferences("APP_CONFIG_DATA", Context.MODE_PRIVATE);
    }

    @Override
    public synchronized boolean contains(T key) {
        return sharedPreferences.contains(key.toString());
    }

    @Override
    public synchronized String getString(T key) {
        return sharedPreferences.getString(key.toString(), null);
    }

    @Override
    public synchronized void setString(T key, String value) {
        sharedPreferences.edit()
                .putString(key.toString(), value)
                .apply();
    }

    @Override
    public synchronized boolean getBoolean(T key) {
        return sharedPreferences.getBoolean(key.toString(), false);
    }

    @Override
    public synchronized void setBoolean(T key, boolean value) {
        sharedPreferences.edit()
                .putBoolean(key.toString(), value)
                .apply();
    }

    @Override
    public synchronized int getInteger(T key) {
        return sharedPreferences.getInt(key.toString(), -1);
    }

    @Override
    public synchronized void setInteger(T key, int value) {
        sharedPreferences.edit()
                .putInt(key.toString(), value)
                .apply();
    }
}
