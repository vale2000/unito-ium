package it.unito.ium.myreps.services.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import it.unito.ium.myreps.Model;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// TODO Async Calls
final class ApiManagerImpl implements ApiManager {
    private static final String SERVER_HOST = "http://127.0.0.1/";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client;

    ApiManagerImpl(Model model) {
        this.client = new OkHttpClient();
    }

    @Override
    public JSONObject doLogin(String email, String password) {
        String postBody = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        try {
            String result = newPostRequest("user/login", postBody);
            return new JSONObject(result);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject doRegistration(String email, String password) {
        String postBody = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";

        try {
            String result = newPostRequest("user/registration", postBody);
            return new JSONObject(result);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Private Methods

    private synchronized String newPostRequest(String path, String jsonData) throws IOException {
        RequestBody body = RequestBody.create(jsonData, JSON);
        Request request = new Request.Builder()
                .url(SERVER_HOST + path)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private synchronized String newGetRequest(String path) throws IOException {
        Request request = new Request.Builder()
                .url(SERVER_HOST + path)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
