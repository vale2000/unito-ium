package it.unito.ium.myreps.model.services.api.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import it.unito.ium.myreps.components.RecyclerViewRow;

public final class Lesson extends RecyclerViewRow implements Serializable {
    private static final long serialVersionUID = 1795672724728252255L;

    private final int id;
    private final long unixDay;
    private final int initHour;
    private final Course course;
    private final User[] teachers;

    public Lesson(JSONObject jsonLesson) {
        try {
            this.id = jsonLesson.has("id") ? jsonLesson.getInt("id") : -1;
            this.unixDay = jsonLesson.has("unix_day") ? jsonLesson.getLong("unix_day") : -1L;
            this.initHour = jsonLesson.has("init_hour") ? jsonLesson.getInt("init_hour") : -1;

            this.course = jsonLesson.has("course") ? new Course(jsonLesson.getJSONObject("course")) : null;

            User[] teachers = null;
            if (jsonLesson.has("available_teachers")) {
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

    public long getUnixDay() {
        return unixDay;
    }

    public int getInitHour() {
        return initHour;
    }

    public Course getCourse() {
        return course;
    }

    public User[] getTeachers() {
        return teachers;
    }

    // --------------------------------------------------------
    // RecyclerViewRow Methods
    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getHeader() {
        return course.getName();
    }

    @Override
    public String getDescription() {
        return "Lesson id:" + id;
    }
}
