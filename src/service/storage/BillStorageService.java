package src.service.storage;

import java.time.LocalDate;

import src.domain.Bill;
import src.domain.Constants;
import src.enumeration.State;

public class BillStorageService extends TxtStorageService<Bill> {
    @Override
    protected String getFileName() {
        return BASE_PATH + "bill.txt";
    }

    @Override
    protected Bill fromString(String line) {
        String[] parts = line.split(Constants.DELIMITER);

        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        int amount = Integer.parseInt(parts[2]);
        LocalDate dueDate = LocalDate.parse(parts[3], Constants.DATE_FORMATTER);
        State state = State.fromValue(parts[4]);
        String provider = parts[5];

        return new Bill(id, type, amount, dueDate, state, provider);
    }
}
