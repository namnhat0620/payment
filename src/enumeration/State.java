package payment.src.enumeration;

public enum State {
    NOT_PAID,
    PROCESSED,
    PENDING;

    public static State fromValue(String value) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid State: " + value);
    }
}
