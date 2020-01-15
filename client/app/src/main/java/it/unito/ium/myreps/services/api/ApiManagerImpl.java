package it.unito.ium.myreps.services.api;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import it.unito.ium.myreps.Model;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// TODO Async Calls
final class ApiManagerImpl implements ApiManager {
    private static final String SERVER_HOST = "http://127.0.0.1/";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final Model appModel;
    private final OkHttpClient client;

    private String email;
    private String password;

    ApiManagerImpl(Model appModel) {
        this.appModel = appModel;
        this.client = new OkHttpClient();
    }

    @Override
    public ApiManager setCredentials(String email, String password) {
        this.email = email;
        this.password = password;
        return this;
    }

    @Override
    public void doLogin() {
        String postBody = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        Request request = newPostRequest("user/login", postBody);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    @Override
    public void doRegistration() {
        String postBody = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        Request request = newPostRequest("user/register", postBody);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    // Private Methods

    private Request newPostRequest(String path, String jsonData) {
        return new Request.Builder()
                .url(SERVER_HOST + path)
                .post(RequestBody.create(jsonData, JSON))
                .build();
    }

    private Request newGetRequest(String path) {
        return new Request.Builder()
                .url(SERVER_HOST + path)
                .build();
    }
}
