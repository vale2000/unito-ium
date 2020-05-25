package it.unito.ium.myreps.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class JWTReader {
    public static JSONObject read(String tokenB64) {
        String tokenString = new String(Base64.getDecoder().decode(tokenB64), StandardCharsets.US_ASCII);
        String tokenDataB64 = tokenString.substring(0, tokenString.indexOf('.'));
        String tokenData = new String(Base64.getDecoder().decode(tokenDataB64), StandardCharsets.UTF_8);
        try {
            return new JSONObject(tokenData);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
