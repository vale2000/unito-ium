package it.unito.ium.myreps.model.services.api;

import org.json.JSONObject;

public interface ApiManager {
    ApiManager setCredentials(String email, String password);

    ApiManager setAuthToken(String token);

    void doLogin(Callback callback);

    void doRegistration(Callback callback);

    void getProfileInfo(Callback callback);

    void loadLessons(Callback callback);

    void loadLesson(int id, Callback callback);

    void loadBookings(Callback callback);

    void loadBooking(int id, Callback callback);

    interface Callback {
        void execute(boolean valid, JSONObject response);
    }
}
