package it.unito.ium.myreps.model.services.config;

public interface ConfigManager<T> {
    boolean contains(T key);

    String getString(T key);

    void setString(T key, String value);

    boolean getBoolean(T key);

    void setBoolean(T key, boolean value);

    int getInteger(T key);

    void setInteger(T key, int value);
}
