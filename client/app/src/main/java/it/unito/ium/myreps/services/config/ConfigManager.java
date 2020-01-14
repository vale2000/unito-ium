package it.unito.ium.myreps.services.config;

public interface ConfigManager {
    boolean contains(String key);

    String getString(String key);

    void setString(String key, String value);

    boolean getBoolean(String key);

    void setBoolean(String key, boolean value);

    int getInteger(String key);

    void setInteger(String key, int value);
}
