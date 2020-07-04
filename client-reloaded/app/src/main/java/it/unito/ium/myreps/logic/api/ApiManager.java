package it.unito.ium.myreps.logic.api;

import org.json.JSONObject;

import java.util.ArrayList;

import it.unito.ium.myreps.logic.api.objects.Booking;
import it.unito.ium.myreps.logic.api.objects.Lesson;
import it.unito.ium.myreps.logic.api.objects.User;
import it.unito.ium.myreps.util.RecyclerViewRow;

public interface ApiManager {
    ApiManager setCredentials(String email, String password);

    void doLogin(Callback<JSONObject> callback);

    @Deprecated
    default boolean doRegistration() {
        return false;
    }

    void loadProfile(Callback<User> callback);

    void loadLessonList(Callback<ArrayList<RecyclerViewRow>> callback);

    void loadLesson(int id, Callback<Lesson> callback);

    void loadBookingList(Callback<ArrayList<RecyclerViewRow>> callback);

    void loadBooking(int id, Callback<Booking> callback);

    interface Callback<T> {
        void execute(SrvStatus status, T response);
    }
}
