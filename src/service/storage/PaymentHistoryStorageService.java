package src.service.storage;

import java.time.LocalDate;

import src.domain.Constants;
import src.domain.PaymentHistory;
import src.enumeration.State;

public class PaymentHistoryStorageService extends TxtStorageService<PaymentHistory> {
    @Override
    public String getFileName() {
        return BASE_PATH + "paymentHistory.txt";
    }

    @Override
    public PaymentHistory fromString(String line) {
        String[] parts = line.split(Constants.DELIMITER);

        int id = Integer.parseInt(parts[0]);
        int amount = Integer.parseInt(parts[1]);
        LocalDate date = LocalDate.parse(parts[2], Constants.DATE_FORMATTER);
        State state = State.fromValue(parts[3]);
        int billId = Integer.parseInt(parts[4]);

        return new PaymentHistory(id, amount, date, state, billId);
    }

}
