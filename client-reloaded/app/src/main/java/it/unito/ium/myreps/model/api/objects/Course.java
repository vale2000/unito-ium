package it.unito.ium.myreps.model.api.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import it.unito.ium.myreps.util.RecyclerViewRow;

public final class Course extends RecyclerViewRow implements Serializable {
    private static final long serialVersionUID = 6246000221548638750L;

    private final int id;
    private final String name;

    private final User[] teachers;

    public Course(JSONObject jsonCourse) {
        try {
            this.id = jsonCourse.has("id") ? jsonCourse.getInt("id") : -1;
            this.name = jsonCourse.has("name") ? jsonCourse.getString("name") : null;

            User[] teachers = null;
            if (jsonCourse.has("teachers")) {
                JSONArray jsonTeachers = jsonCourse.getJSONArray("teachers");
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

    public String getName() {
        return name;
    }

    public User getTeacher(int i) {
        return teachers[i];
    }

    public User[] getTeachers() {
        return teachers;
    }
}
