package it.unito.ium.myreps.model.storage;

public interface KVStorage {
    String getString(String key);

    String getString(String key, String defValue);

    void setString(String key, String value);

    int getInteger(String key);

    int getInteger(String key, int defValue);

    void setInteger(String key, int value);

    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean defValue);

    void setBoolean(String key, boolean value);
}
