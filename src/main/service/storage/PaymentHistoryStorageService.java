package main.service.storage;

import java.time.LocalDate;

import main.domain.Constants;
import main.domain.PaymentHistory;
import main.enumeration.State;

public class PaymentHistoryStorageService extends TxtStorageService<PaymentHistory> {
    @Override
    public String getEntityName() {
        return "paymentHistory";
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
