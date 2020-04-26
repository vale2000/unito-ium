package it.unito.ium.myreps.utils;

import java.util.regex.Pattern;

public final class MailUtils {
    public static boolean validate(String s) {
        return Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", Pattern.CASE_INSENSITIVE)
                .matcher(s)
                .find();
    }
}
