package payment.src.service.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import payment.src.domain.Bill;
import payment.src.domain.Constants;
import payment.src.enumeration.State;

public class BillStorageService extends JsonStorageService<Bill> {
    @Override
    public String getFileName() {
        return BASE_PATH + "bill.txt";
    }

    @Override
    public Bill fromString(String line) {
        String[] parts = line.split(Constants.DELIMITER);

        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        int amount = Integer.parseInt(parts[2]);
        LocalDate dueDate = LocalDate.parse(parts[3], Constants.DATE_FORMATTER);
        State state = State.fromValue(parts[4]);
        String provider = parts[5];

        return new Bill(id, type, amount, dueDate, state, provider);
    }

    public List<Bill> findByIds(List<Integer> ids) {
        File file = new File(getFileName());
        List<Bill> bills = new LinkedList<>();
        if (!file.exists())
            return Collections.emptyList();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Bill bill = fromString(line);
                if (ids.contains(bill.getId())) {
                    bills.add(bill);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bills;
    }
}
