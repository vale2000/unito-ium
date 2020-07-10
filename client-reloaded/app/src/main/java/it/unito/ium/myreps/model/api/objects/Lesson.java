package it.unito.ium.myreps.model.api.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

import it.unito.ium.myreps.util.RecyclerViewRow;

public final class Lesson extends RecyclerViewRow implements Serializable {
    public static final SimpleDateFormat DATE_YEAR_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
    public static final SimpleDateFormat DATE_HOUR_FORMAT = new SimpleDateFormat("HH:mm", Locale.UK);
    public static final SimpleDateFormat DATE_WEEK_FORMAT = new SimpleDateFormat("EEEE", Locale.UK);

    private static final long serialVersionUID = -1609365345742781595L;

    private final Date date;
    private final long longDay;
    private final Course course;
    private final int teachersNum;
    private final User[] teachers;

    public Lesson(JSONObject jsonLesson) {
        try {
            this.course = jsonLesson.has("course") ? new Course(jsonLesson.getJSONObject("course")) : null;

            long longDay = -1;
            Date date = null;
            if (jsonLesson.has("day")) {
                long day = jsonLesson.getLong("day");
                int hour = jsonLesson.has("hour") ? jsonLesson.getInt("hour") : 0;
                Instant instant = Instant.ofEpochSecond(day + hour);
                date = Date.from(instant);
                longDay = day;
            }
            this.longDay = longDay;
            this.date = date;

            User[] teachers = null;
            if (jsonLesson.has("teachers")) {
                JSONArray jsonTeachers = jsonLesson.getJSONArray("teachers");
                teachers = new User[jsonTeachers.length()];
                for (int i = 0; i < teachers.length; i++) {
                    JSONObject course = jsonTeachers.getJSONObject(i);
                    teachers[i] = new User(course);
                }
            }
            this.teachers = teachers;

            if (jsonLesson.has("free_teachers")) {
                this.teachersNum = jsonLesson.getInt("free_teachers");
            } else if (this.teachers != null) {
                this.teachersNum = this.teachers.length;
            } else {
                this.teachersNum = 0;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public Date getDate() {
        return date;
    }

    public long getDay() {
        return longDay;
    }

    public String getYearDay() {
        return DATE_YEAR_FORMAT.format(date);
    }

    public String getWeekDay() {
        return DATE_WEEK_FORMAT.format(date);
    }

    public String getHour() {
        return DATE_HOUR_FORMAT.format(date);
    }

    public Course getCourse() {
        return course;
    }

    public int getTeachersNum() {
        return teachersNum;
    }

    public User getTeacher(int i) {
        return teachers[i];
    }

    public User[] getTeachers() {
        return teachers;
    }
}