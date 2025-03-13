package main.service.storage;

import java.time.LocalDate;

import main.domain.Bill;
import main.domain.Constants;
import main.enumeration.State;

public class BillStorageService extends TxtStorageService<Bill> {
    @Override
    protected String getEntityName() {
        return "bill";
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
