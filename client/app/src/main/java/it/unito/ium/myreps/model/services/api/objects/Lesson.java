package it.unito.ium.myreps.model.services.api.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import it.unito.ium.myreps.components.RecyclerViewRow;

public final class Lesson extends RecyclerViewRow implements Serializable {
    private static final long serialVersionUID = 1795672724728252255L;

    private final int id;
    private final long date;
    private final int hour;
    private final Course course;
    private final User[] teachers;

    public Lesson(JSONObject jsonLesson) {
        try {
            this.id = jsonLesson.has("id") ? jsonLesson.getInt("id") : -1;

            this.date = jsonLesson.has("unix_day") ? jsonLesson.getLong("unix_day") : -1L;
            this.hour = jsonLesson.has("init_hour") ? jsonLesson.getInt("init_hour") : -1;
            this.course = jsonLesson.has("course") ? new Course(jsonLesson.getJSONObject("course")) : null;

            User[] teachers = null;
            if (jsonLesson.has("teachers_free")) {
                JSONArray jsonTeachers = jsonLesson.getJSONArray("available_teachers");
                teachers = new User[jsonTeachers.length()];
                for (int i = 0; i < teachers.length; i++) {
                    JSONObject course = jsonTeachers.getJSONObject(i);
                    teachers[i] = new User(course);
                }
            }
            this.teachers = teachers;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public int getID() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public Course getCourse() {
        return course;
    }

    public User[] getTeachers() {
        return teachers;
    }
}
