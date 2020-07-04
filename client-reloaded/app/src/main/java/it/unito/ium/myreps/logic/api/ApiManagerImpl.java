package it.unito.ium.myreps.logic.api;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import it.unito.ium.myreps.config.ApiConfiguration;
import it.unito.ium.myreps.config.KVConfiguration;
import it.unito.ium.myreps.logic.Model;
import it.unito.ium.myreps.logic.api.objects.Booking;
import it.unito.ium.myreps.logic.api.objects.Lesson;
import it.unito.ium.myreps.logic.api.objects.User;
import it.unito.ium.myreps.logic.storage.KVStorage;
import it.unito.ium.myreps.ui.main.LessonListItemBreak;
import it.unito.ium.myreps.util.RecyclerViewRow;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

final class ApiManagerImpl implements ApiManager {
    private static final MediaType JSON = MediaType.get("application/json;charset=utf-8");

    private final Model model;
    private final OkHttpClient client;

    private String email;
    private String password;
    private String token;

    ApiManagerImpl(Model model) {
        this.model = model;
        this.client = new OkHttpClient();

        token = model.getKVStorage().getString(KVConfiguration.ACCOUNT_JWT, null);
        email = null;
        password = null;
    }

    @Override
    public ApiManager setCredentials(String email, String password) {
        this.email = email;
        this.password = password;
        this.token = null;
        return this;
    }

    @Override
    public void doLogin(Callback<JSONObject> callback) {
        String payload = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        Request request = new Request.Builder()
                .url(ApiConfiguration.HOST + ApiConfiguration.LOGIN_ENDPOINT)
                .post(RequestBody.create(payload, JSON))
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.execute(SrvStatus.SERVER_OFFLINE, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.getBoolean("ok")) {
                            JSONObject data = jsonObject.getJSONObject("data");

                            KVStorage kvStorage = model.getKVStorage();
                            kvStorage.setInteger(KVConfiguration.ACCOUNT_ID, data.getInt("user_id"));
                            kvStorage.setString(KVConfiguration.ACCOUNT_JWT, token = data.getString("token"));
                            kvStorage.setString(KVConfiguration.ACCOUNT_EMAIL, email);

                            callback.execute(SrvStatus.OK, data);
                        } else {
                            SrvStatus srvStatus = SrvStatus.fromString(jsonObject.getString("error"));
                            callback.execute(srvStatus, null);
                        }
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.execute(SrvStatus.UNKNOWN_ERROR, null);
            }
        });
    }

    @Override
    public void loadProfile(Callback<User> callback) {
        Request request = new Request.Builder()
                .url(ApiConfiguration.HOST + ApiConfiguration.PROFILE_ENDPOINT)
                .header("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.execute(SrvStatus.SERVER_OFFLINE, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.getBoolean("ok")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            callback.execute(SrvStatus.OK, new User(data));
                        } else {
                            SrvStatus srvStatus = SrvStatus.fromString(jsonObject.getString("error"));
                            callback.execute(srvStatus, null);
                        }
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.execute(SrvStatus.UNKNOWN_ERROR, null);
            }
        });
    }

    @Override
    public void loadLessonList(Callback<ArrayList<RecyclerViewRow>> callback) {
        Request request = new Request.Builder()
                .url(ApiConfiguration.HOST + ApiConfiguration.LESSONS_ENDPOINT)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.execute(SrvStatus.SERVER_OFFLINE, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.getBoolean("ok")) {
                            String lastDay = "NULL";
                            JSONArray data = jsonObject.getJSONArray("data");
                            ArrayList<RecyclerViewRow> lessonList = new ArrayList<>();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonLesson = data.getJSONObject(i);
                                Lesson lesson = new Lesson(jsonLesson);

                                if (!lastDay.equals(lesson.getYearDay())) {
                                    lastDay = lesson.getYearDay();
                                    lessonList.add(new LessonListItemBreak(lesson.getWeekDay() + ", " + lesson.getYearDay()));
                                }

                                lessonList.add(lesson);
                            }

                            callback.execute(SrvStatus.OK, lessonList);
                        } else {
                            SrvStatus srvStatus = SrvStatus.fromString(jsonObject.getString("error"));
                            callback.execute(srvStatus, null);
                        }
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.execute(SrvStatus.UNKNOWN_ERROR, null);
            }
        });
    }

    @Override
    public void loadLesson(int id, Callback<Lesson> callback) {

    }

    @Override
    public void loadBookingList(Callback<ArrayList<RecyclerViewRow>> callback) {

    }

    @Override
    public void loadBooking(int id, Callback<Booking> callback) {

    }
}
