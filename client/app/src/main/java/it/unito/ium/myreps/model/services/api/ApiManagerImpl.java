package it.unito.ium.myreps.model.services.api;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


final class ApiManagerImpl implements ApiManager {
    private static final String SERVER_HOST = "http://127.0.0.1/";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client;

    ApiManagerImpl() {
        this.client = new OkHttpClient();
    }

    @Override
    public String doLogin(String email, String password) {
        String postBody = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        try {
            return newPostRequest("user/login", postBody);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String doRegistration(String email, String password) {
        String postBody = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";

        try {
            return newPostRequest("user/registration", postBody);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Private Methods

    private synchronized String newPostRequest(String path, String jsonData) throws NullPointerException, IOException {
        RequestBody body = RequestBody.create(jsonData, JSON);
        Request request = new Request.Builder()
                .url(SERVER_HOST + path)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private synchronized String newGetRequest(String path) throws NullPointerException, IOException {
        Request request = new Request.Builder()
                .url(SERVER_HOST + path)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
