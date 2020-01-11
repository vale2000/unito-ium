package it.unito.ium.myreps.model.services.api;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

final class ApiManagerImpl implements ApiManager {
    private static final String SERVER_HOST = "https://127.0.0.1/";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client;

    ApiManagerImpl() {
        this.client = new OkHttpClient();
    }



    private synchronized String newPostRequest(@NonNull String path, @NonNull String jsonData) throws IOException {
        RequestBody body = RequestBody.create(jsonData, JSON);
        Request request = new Request.Builder()
                .url(SERVER_HOST + path)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private synchronized String newGetRequest(@NonNull String path) throws IOException {
        Request request = new Request.Builder()
                .url(SERVER_HOST + path)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
