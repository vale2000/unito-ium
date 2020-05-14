package it.unito.ium.myreps.components;

import java.io.Serializable;
import java.util.Objects;

public final class RecyclerViewRow implements Serializable {
    private final int id;
    private final String subject;
    private final String professor;
    private final int minutes;

    public RecyclerViewRow(int id, String subject, String professor, int minutes) {
        this.id = id;
        this.subject = subject;
        this.professor = professor;
        this.minutes = minutes;
    }

    private int getID() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getProfessor() {
        return professor;
    }

    int getMinutes() {
        return minutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecyclerViewRow that = (RecyclerViewRow) o;
        return id == that.getID() &&
                minutes == that.getMinutes() &&
                Objects.equals(subject, that.getSubject()) &&
                Objects.equals(professor, that.getProfessor());
    }

    @Override
    public int hashCode() {
        int result = id;
        result += 31 * subject.hashCode();
        result += 31 * professor.hashCode();
        result += 31 * minutes;
        return result;
    }

    static String minutesToString(int i) {
        int hours = i / 60;
        int minutes = i % 60;

        StringBuilder result = new StringBuilder();
        result.append(hours).append('h');
        if (minutes > 0) result.append(' ').append(minutes).append('\'');
        return result.toString();
    }
}