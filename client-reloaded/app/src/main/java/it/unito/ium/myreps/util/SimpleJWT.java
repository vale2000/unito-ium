package it.unito.ium.myreps.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class SimpleJWT {
    public static JSONObject read(String tokenB64) throws JSONException {
        Base64.Decoder decoder = Base64.getDecoder();
        String tokenString = new String(decoder.decode(tokenB64), StandardCharsets.US_ASCII);
        String tokenDataB64 = tokenString.substring(0, tokenString.indexOf('.'));
        String tokenData = new String(decoder.decode(tokenDataB64), StandardCharsets.UTF_8);
        return new JSONObject(tokenData);
    }
}
