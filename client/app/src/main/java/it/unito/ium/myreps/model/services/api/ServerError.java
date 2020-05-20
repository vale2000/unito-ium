package it.unito.ium.myreps.model.services.api;

import org.jetbrains.annotations.NotNull;

public enum ServerError {
    LOGIN_DISABLED("Login for this user is disabled"),
    LOGIN_FAILED("Wrong email or password"),
    USER_ALREADY_EXIST("This user is already registered"),
    USER_NOT_FOUND("User not found"),
    BOOKING_NOT_FOUND("Booking not found"),
    LESSON_NOT_FOUND("Lesson not found"),
    COURSE_NOT_FOUND("Course not found"),
    BAD_REQUEST("Bad request format"),
    UNAUTHORIZED("You are not authorized"),
    NOT_FOUND("Endpoint not found"),
    METHOD_NOT_ALLOWED("Method not allowed"),
    SERVER_ERROR("Server error"),
    UNKNOWN_ERROR("Unknown error"),
    SERVER_OFFLINE("Server seems offline");

    // ---------------------------------------------------------------

    private final String humanReadable;

    ServerError(String humanReadable) {
        this.humanReadable = humanReadable;
    }

    public static ServerError fromString(String s) {
        ServerError serverError = null;
        try {
            serverError = ServerError.valueOf(s.toUpperCase());
        } catch (EnumConstantNotPresentException ignored) {
        }
        return serverError;
    }

    @NotNull
    @Override
    public String toString() {
        return humanReadable;
    }
}
