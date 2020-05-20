package it.unito.ium.myreps.model.services.config;

public enum ConfigKey {
    // Userdata
    USERDATA_CACHED,   // Boolean
    USERDATA_EMAIL,    // String
    USERDATA_NAME,     // String
    USERDATA_SURNAME,  // String
    USERDATA_PASSWORD, // String
    // Auth token
    AUTH_TOKEN;        // String

    // --------------------

    public static ConfigKey fromString(String s) {
        ConfigKey configKey = null;
        try {
            configKey = ConfigKey.valueOf(s.toUpperCase());
        } catch (EnumConstantNotPresentException ignored) {}
        return configKey;
    }

    @Override
    public String toString() {
        return this.name().toUpperCase();
    }
}
