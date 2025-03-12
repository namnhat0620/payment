package payment.src.domain;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String DELIMITER = ",";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Constants() {
    }
}
