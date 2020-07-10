package it.unito.ium.myreps.model.api.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Locale;

import it.unito.ium.myreps.util.RecyclerViewRow;

public final class User extends RecyclerViewRow implements Serializable {
    private static final long serialVersionUID = -5003270936286810786L;

    private final int id;
    private final String email;
    private final String name;
    private final String surname;
    private final Gender gender;
    private final Role role;
    private final Course[] courses;
    private final int[] freeHours;

    public User(JSONObject jsonUser) {
        try {
            this.id = jsonUser.has("id") ? jsonUser.getInt("id") : -1;
            this.email = jsonUser.has("email") ? jsonUser.getString("email") : null;
            this.name = jsonUser.has("name") ? jsonUser.getString("name") : null;
            this.surname = jsonUser.has("surname") ? jsonUser.getString("surname") : null;
            this.gender = jsonUser.has("gender") ? genderFromInt(jsonUser.getInt("gender")) : null;

            this.role = jsonUser.has("role") ? new Role(jsonUser.getJSONObject("role")) : null;

            Course[] courses = null;
            if (jsonUser.has("courses")) {
                JSONArray jsonCourses = jsonUser.getJSONArray("courses");
                courses = new Course[jsonCourses.length()];
                for (int i = 0; i < courses.length; i++) {
                    JSONObject course = jsonCourses.getJSONObject(i);
                    courses[i] = new Course(course);
                }
            }
            this.courses = courses;

            int[] freeHours = null;
            if (jsonUser.has("available_on")) {
                JSONArray availableOn = jsonUser.getJSONArray("available_on");
                freeHours = new int[availableOn.length()];
                for (int i = 0; i < freeHours.length; i++) {
                    freeHours[i] = availableOn.getInt(i);
                }
            }
            this.freeHours = freeHours;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private Gender genderFromInt(int gender) {
        switch (gender) {
            case 1:
                return Gender.MALE;
            case 2:
                return Gender.FEMALE;
            default:
                return Gender.OTHER;
        }
    }

    public int getID() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getFullName() {
        return String.format(Locale.getDefault(), "%s %s", name, surname);
    }

    public Gender getGender() {
        return gender;
    }

    public Role getRole() {
        return role;
    }

    public Course[] getCourses() {
        return courses;
    }

    public int[] getFreeHours() {
        return freeHours;
    }

    // --------------------------------------------------------
    // Private Objects
    public enum Gender {
        OTHER(0),
        MALE(1),
        FEMALE(2);

        private final int intVal;

        Gender(int intVal) {
            this.intVal = intVal;
        }

        public int asInteger() {
            return intVal;
        }
    }

    public final static class Role {
        private final int id;
        private final String name;

        Role(JSONObject jsonRole) {
            try {
                this.id = jsonRole.has("id") ? jsonRole.getInt("id") : -1;
                this.name = jsonRole.has("name") ? jsonRole.getString("name") : null;
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
    }
}
