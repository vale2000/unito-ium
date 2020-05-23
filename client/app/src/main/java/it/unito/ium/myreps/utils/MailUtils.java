package it.unito.ium.myreps.utils;

import android.util.Patterns;

public final class MailUtils {
    public static boolean validate(String s) {
        /*
        Pattern mailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$", Pattern.CASE_INSENSITIVE);
        return mailPattern.matcher(s).find();
         */
        return Patterns.EMAIL_ADDRESS.matcher(s).find();
    }
}
