package src.service.storage;

import src.domain.Balance;
import src.domain.Constants;

public class BalanceStorageService extends TxtStorageService<Balance> {
    @Override
    protected String getFileName() {
        return BASE_PATH + "balance.txt";
    }

    @Override
    protected Balance fromString(String line) {
        String[] parts = line.split(Constants.DELIMITER);

        int id = Integer.parseInt(parts[0]);
        int amount = Integer.parseInt(parts[1]);

        return new Balance(id, amount);
    }

}
