package it.unito.ium.myreps.logic.config;

import android.content.Context;

public final class KVStorage {
    private final Context context;

    public KVStorage(Context context) {
        this.context = context;
    }

    // TODO rewrite but better!

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defValue) {
        return "";
    }

    public String getInteger(String key) {
        return getInteger(key, -1);
    }

    public String getInteger(String key, int defValue) {
        return "";
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return true;
    }
}
