package it.unito.ium.myreps.model.api;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.unito.ium.myreps.configuration.ApiConf;
import it.unito.ium.myreps.configuration.StorageConf;
import it.unito.ium.myreps.model.Model;
import it.unito.ium.myreps.model.api.objects.Booking;
import it.unito.ium.myreps.model.api.objects.Lesson;
import it.unito.ium.myreps.model.api.objects.User;
import it.unito.ium.myreps.model.storage.KVStorage;
import it.unito.ium.myreps.util.RecyclerItemBreak;
import it.unito.ium.myreps.util.RecyclerViewRow;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

final class ApiManagerImpl implements ApiManager {
    private static final MediaType JSON = MediaType.get("application/json;charset=utf-8");

    private final KVStorage kvStorage;
    private final OkHttpClient client;

    private String email;
    private String password;
    private String token;

    ApiManagerImpl(Model model) {
        kvStorage = model.getKVStorage();
        client = new OkHttpClient();

        token = kvStorage.getString(StorageConf.ACCOUNT_JWT, null);
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
                .url(ApiConf.HOST + ApiConf.LOGIN_ENDPOINT)
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

                            kvStorage.setInteger(StorageConf.ACCOUNT_ID, data.getInt("user_id"));
                            kvStorage.setString(StorageConf.ACCOUNT_JWT, token = data.getString("token"));
                            kvStorage.setString(StorageConf.ACCOUNT_EMAIL, email);

                            callback.execute(SrvStatus.OK, data);
                        } else {
                            SrvStatus srvStatus = SrvStatus.fromString(jsonObject.getString("error"));
                            checkAuthToken(srvStatus);
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
                .url(ApiConf.HOST + ApiConf.PROFILE_ENDPOINT)
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
                            checkAuthToken(srvStatus);
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
                .url(ApiConf.HOST + ApiConf.LESSONS_ENDPOINT)
                .build();

        // TODO ...
    }

    @Override
    public void loadLessonList(long day, Callback<ArrayList<RecyclerViewRow>> callback) {
        Request request = new Request.Builder()
                .url(String.format(Locale.getDefault(), "%s%s/%d", ApiConf.HOST, ApiConf.LESSONS_ENDPOINT, day))
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.execute(SrvStatus.SERVER_OFFLINE, new ArrayList<>());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.getBoolean("ok")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            ArrayList<RecyclerViewRow> list = new ArrayList<>();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonLesson = data.getJSONObject(i);
                                Lesson lesson = new Lesson(jsonLesson);
                                list.add(lesson);
                            }

                            callback.execute(SrvStatus.OK, list);
                        } else {
                            SrvStatus srvStatus = SrvStatus.fromString(jsonObject.getString("error"));
                            checkAuthToken(srvStatus);
                            callback.execute(srvStatus, new ArrayList<>());
                        }
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.execute(SrvStatus.UNKNOWN_ERROR, new ArrayList<>());
            }
        });
    }

    @Override
    public void loadLesson(long day, int course, Callback<Lesson> callback) {
        String payload = String.format(Locale.getDefault(), "/%d/%d", day, course);
        Request request = new Request.Builder()
                .url(ApiConf.HOST + ApiConf.LESSONS_ENDPOINT + payload)
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
                            callback.execute(SrvStatus.OK, new Lesson(data));
                        } else {
                            SrvStatus srvStatus = SrvStatus.fromString(jsonObject.getString("error"));
                            checkAuthToken(srvStatus);
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
    public void newBooking(int teacher, int course, long day, List<Integer> hours, Callback<Boolean> callback) {
        try {
            JSONObject payload = new JSONObject()
                    .put("teacher_id", teacher)
                    .put("course_id", course)
                    .put("day", day);

            JSONArray jsonArray = new JSONArray();
            for (Integer h : hours) jsonArray.put(h);
            payload.put("hours", jsonArray);

            Log.i("PAYLOAD", payload.toString());

            Request request = new Request.Builder()
                    .url(ApiConf.HOST + ApiConf.BOOKINGS_ENDPOINT)
                    .header("Authorization", "Bearer " + token)
                    .post(RequestBody.create(payload.toString(), JSON))
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    callback.execute(SrvStatus.SERVER_OFFLINE, false);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());

                            if (jsonObject.getBoolean("ok")) {
                                callback.execute(SrvStatus.OK, true);
                            } else {
                                SrvStatus srvStatus = SrvStatus.fromString(jsonObject.getString("error"));
                                checkAuthToken(srvStatus);
                                callback.execute(srvStatus, false);
                            }
                            return;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callback.execute(SrvStatus.UNKNOWN_ERROR, false);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.execute(SrvStatus.UNKNOWN_ERROR, false);
        }
    }

    @Override
    public void loadBookingList(Callback<ArrayList<RecyclerViewRow>> callback) {
        Request request = new Request.Builder()
                .url(ApiConf.HOST + ApiConf.BOOKINGS_ENDPOINT)
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
                            String lastDay = "NULL";
                            JSONArray data = jsonObject.getJSONArray("data");
                            ArrayList<RecyclerViewRow> bookingList = new ArrayList<>();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonBooking = data.getJSONObject(i);
                                Booking booking = new Booking(jsonBooking);

                                if (!lastDay.equals(booking.getLesson().getYearDay())) {
                                    lastDay = booking.getLesson().getYearDay();
                                    bookingList.add(new RecyclerItemBreak(booking.getLesson().getWeekDay() + ", " + booking.getLesson().getYearDay()));
                                }

                                bookingList.add(new Booking(jsonBooking));
                            }

                            callback.execute(SrvStatus.OK, bookingList);
                        } else {
                            SrvStatus srvStatus = SrvStatus.fromString(jsonObject.getString("error"));
                            checkAuthToken(srvStatus);
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
    public void updateBooking(int id, String status, Callback<Boolean> callback) {
        String payload = String.format(Locale.getDefault(), "{\"status\": \"%s\"}", status);
        Request request = new Request.Builder()
                .url(String.format(Locale.getDefault(), "%s%s/%d", ApiConf.HOST, ApiConf.BOOKINGS_ENDPOINT, id))
                .header("Authorization", "Bearer " + token)
                .put(RequestBody.create(payload, JSON))
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
                            callback.execute(SrvStatus.OK, true);
                        } else {
                            SrvStatus srvStatus = SrvStatus.fromString(jsonObject.getString("error"));
                            checkAuthToken(srvStatus);
                            callback.execute(srvStatus, false);
                        }
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.execute(SrvStatus.UNKNOWN_ERROR, false);
            }
        });
    }

    private void checkAuthToken(SrvStatus srvStatus) {
        if (srvStatus == SrvStatus.AUTH_FAILED || srvStatus == SrvStatus.LOGIN_FAILED) {
            kvStorage.setString(StorageConf.ACCOUNT_JWT, token = null);
        }
    }
}
