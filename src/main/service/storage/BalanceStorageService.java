package main.service.storage;

import main.domain.Balance;
import main.domain.Constants;

public class BalanceStorageService extends TxtStorageService<Balance> {
    @Override
    protected String getEntityName() {
        return "balance";
    }

    @Override
    protected Balance fromString(String line) {
        String[] parts = line.split(Constants.DELIMITER);

        int id = Integer.parseInt(parts[0]);
        int amount = Integer.parseInt(parts[1]);

        return new Balance(id, amount);
    }

}
