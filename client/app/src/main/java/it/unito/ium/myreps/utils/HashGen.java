package it.unito.ium.myreps.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashGen {
    public static String MD5(String input) {
        return HashGen.digest("MD5", input);
    }

    public static String SHA1(String input) {
        return HashGen.digest("SHA-1", input);
    }

    private static String digest(String method, String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(method);
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);

            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) hashText.insert(0, "0");
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
