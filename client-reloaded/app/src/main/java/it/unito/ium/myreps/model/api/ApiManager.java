package it.unito.ium.myreps.model.api;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.unito.ium.myreps.model.api.objects.Lesson;
import it.unito.ium.myreps.model.api.objects.User;
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

    void loadLessonList(long day, Callback<ArrayList<RecyclerViewRow>> callback);

    void loadLesson(long day, int course, Callback<Lesson> callback);

    void newBooking(int teacher, int course, long day, List<Integer> hours, Callback<Boolean> callback);

    void loadBookingList(Callback<ArrayList<RecyclerViewRow>> callback);

    void updateBooking(int id, String status, Callback<Boolean> callback);

    interface Callback<T> {
        void execute(SrvStatus status, T response);
    }
}
