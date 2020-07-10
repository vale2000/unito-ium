package it.unito.ium.myreps.model.api.objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

import it.unito.ium.myreps.util.RecyclerViewRow;

public final class Booking extends RecyclerViewRow implements Serializable {
    private static final long serialVersionUID = -4401831976372141659L;

    private final int id;
    private final Lesson lesson;
    private Status status;

    public Booking(JSONObject jsonBooking) {
        try {
            this.id = jsonBooking.has("id") ? jsonBooking.getInt("id") : -1;
            this.status = jsonBooking.has("status") ? statusFromString(jsonBooking.getString("status")) : null;

            this.lesson = jsonBooking.has("lesson") ? new Lesson(jsonBooking.getJSONObject("lesson")) : null;
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public Lesson getLesson() {
        return lesson;
    }

    // --------------------------------------------------------
    // Private Objects
    public enum Status {
        RESERVED(0),
        DONE(1),
        CANCELED(2);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        public static Optional<Status> valueOf(int value) {
            return Arrays.stream(values())
                    .filter(Status -> Status.value == value)
                    .findFirst();
        }
    }
}
