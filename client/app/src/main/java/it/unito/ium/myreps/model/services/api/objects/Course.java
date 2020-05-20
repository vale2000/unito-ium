package it.unito.ium.myreps.model.services.api.objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import it.unito.ium.myreps.components.RecyclerViewRow;

public final class Course extends RecyclerViewRow implements Serializable {
    private static final long serialVersionUID = 6806588222144750919L;

    private final int id;
    private final String name;

    public Course(JSONObject jsonCourse) {
        try {
            this.id = jsonCourse.has("id") ? jsonCourse.getInt("id") : -1;
            this.name = jsonCourse.has("name") ? jsonCourse.getString("name") : null;
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

    // --------------------------------------------------------
    // RecyclerViewRow Methods
    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getHeader() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
