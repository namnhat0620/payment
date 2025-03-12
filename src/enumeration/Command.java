package payment.src.enumeration;

public enum Command {
    CASH_IN,
    LIST_BILL,
    PAY_BILL;

    public static Command fromValue(String value) {
        for (Command command : values()) {
            if (command.name().equalsIgnoreCase(value)) {
                return command;
            }
        }
        throw new IllegalArgumentException("Invalid command: " + value);
    }
}
