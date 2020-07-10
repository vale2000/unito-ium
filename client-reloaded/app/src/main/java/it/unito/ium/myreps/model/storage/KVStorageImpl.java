package it.unito.ium.myreps.model.storage;

import android.content.Context;
import android.content.SharedPreferences;

import it.unito.ium.myreps.configuration.StorageConf;

final class KVStorageImpl implements KVStorage {
    private final SharedPreferences sharedPreferences;

    public KVStorageImpl(Context context) {
        sharedPreferences = context.getSharedPreferences(StorageConf.STORAGE_KEY, Context.MODE_PRIVATE);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public void setString(String key, String value) {
        sharedPreferences.edit()
                .putString(key, value)
                .apply();
    }

    public int getInteger(String key) {
        return getInteger(key, -1);
    }

    public int getInteger(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public void setInteger(String key, int value) {
        sharedPreferences.edit()
                .putInt(key, value)
                .apply();
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public void setBoolean(String key, boolean value) {
        sharedPreferences.edit()
                .putBoolean(key, value)
                .apply();
    }
}
