package it.unito.ium.myreps.model.services.api.objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import it.unito.ium.myreps.components.RecyclerViewRow;

public final class Booking extends RecyclerViewRow implements Serializable {
    private static final long serialVersionUID = 3818033132373876246L;

    private final int id;
    private final Status status;
    private final Lesson lesson;
    private final Course course;
    private final User teacher;

    public Booking(JSONObject jsonBooking) {
        try {
            this.id = jsonBooking.has("id") ? jsonBooking.getInt("id") : -1;
            this.status = jsonBooking.has("status") ? statusFromString(jsonBooking.getString("status")) : null;

            this.lesson = jsonBooking.has("lesson") ? new Lesson(jsonBooking.getJSONObject("lesson")) : null;
            this.course = jsonBooking.has("course") ? new Course(jsonBooking.getJSONObject("course")) : null;
            this.teacher = jsonBooking.has("teacher") ? new User(jsonBooking.getJSONObject("teacher")) : null;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private Status statusFromString(String s) {
        switch (s) {
            case "DONE":
                return Status.DONE;
            case "CANCELED":
                return Status.CANCELED;
            default:
                return Status.RESERVED;
        }
    }

    public int getID() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public Course getCourse() {
        return course;
    }

    public User getTeacher() {
        return teacher;
    }

    // --------------------------------------------------------
    // Private Objects
    public enum Status {RESERVED, CANCELED, DONE}
}
