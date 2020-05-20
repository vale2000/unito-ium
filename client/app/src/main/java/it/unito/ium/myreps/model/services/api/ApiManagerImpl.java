package it.unito.ium.myreps.model.services.api;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import it.unito.ium.myreps.model.Model;
import it.unito.ium.myreps.model.services.config.ConfigKey;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

final class ApiManagerImpl implements ApiManager {
    private static final String SERVER_HOST = "http://192.168.1.145:8080/";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final Model model;
    private final OkHttpClient client;

    private String email;
    private String password;
    private String token;

    ApiManagerImpl(Model model) {
        this.model = model;
        this.client = new OkHttpClient();
        this.token = model.getConfigManager().getString(ConfigKey.AUTH_TOKEN);
    }

    @Override
    public ApiManager setCredentials(String email, String password) {
        this.email = email;
        this.password = password;
        return this;
    }

    @Override
    public ApiManager setAuthToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public void doLogin(Callback callback) {
        String jsonBody = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        newPostRequest("account/login", jsonBody, callback);
    }

    @Override
    public void doRegistration(Callback callback) {
        String jsonBody = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        newPostRequest("account/register", jsonBody, callback);
    }

    @Override
    public void getProfileInfo(Callback callback) {
        newGetRequest("account/profile", callback);
    }

    @Override
    public void loadLessons(Callback callback) {
        newGetRequest("lessons", callback);
    }

    @Override
    public void loadLesson(int id, Callback callback) {
        newGetRequest("lessons/" + id, callback);
    }

    @Override
    public void loadBookings(Callback callback) {
        newGetRequest("bookings", callback);
    }

    @Override
    public void loadBooking(int id, Callback callback) {
        newGetRequest("bookings/" + id, callback);
    }


    private void newGetRequest(String path, Callback callback) {
        Request request = new Request.Builder()
                .url(SERVER_HOST + path)
                .header("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(callbackHandler(callback));
    }

    private void newPostRequest(String path, String jsonBody, Callback callback) {
        Request request = new Request.Builder()
                .url(SERVER_HOST + path)
                .header("Authorization", "Bearer " + token)
                .post(RequestBody.create(jsonBody, JSON))
                .build();
        client.newCall(request).enqueue(callbackHandler(callback));
    }

    private void newPutRequest(String path, String jsonBody, Callback callback) {
        Request request = new Request.Builder()
                .url(SERVER_HOST + path)
                .header("Authorization", "Bearer " + token)
                .put(RequestBody.create(jsonBody, JSON))
                .build();
        client.newCall(request).enqueue(callbackHandler(callback));
    }

    private void newDeleteRequest(String path, String jsonBody, Callback callback) {
        Request request = new Request.Builder()
                .url(SERVER_HOST + path)
                .header("Authorization", "Bearer " + token)
                .delete(RequestBody.create(jsonBody, JSON))
                .build();
        client.newCall(request).enqueue(callbackHandler(callback));
    }

    private okhttp3.Callback callbackHandler(Callback callback) {
        return new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.execute(false, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.body() != null) {
                    try {
                        String responseString = response.body().string();
                        JSONObject responseJson = new JSONObject(responseString);
                        callback.execute(true, responseJson);
                        return;
                    } catch (NullPointerException | JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                callback.execute(false, null);
            }
        };
    }
}
