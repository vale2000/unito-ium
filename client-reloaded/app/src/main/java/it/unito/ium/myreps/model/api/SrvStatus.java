package it.unito.ium.myreps.model.api;

public enum SrvStatus {
    OK(null),
    LOGIN_DISABLED("Login for this user is disabled"),
    AUTH_FAILED("Your session is no longer valid"),
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
    SERVER_OFFLINE("Server seems offline"),
    UNKNOWN_ERROR("An exception occurred");

    // ---------------------------------------------------------------

    private final String humanReadable;

    SrvStatus(String humanReadable) {
        this.humanReadable = humanReadable;
    }

    public static SrvStatus fromString(String s) {
        try {
            return SrvStatus.valueOf(s.toUpperCase());
        } catch (EnumConstantNotPresentException ignored) {
            return UNKNOWN_ERROR;
        }
    }

    @Override
    public String toString() {
        return humanReadable;
    }
}
