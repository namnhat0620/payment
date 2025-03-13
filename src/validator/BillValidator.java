package src.validator;

import java.util.List;
import src.domain.Bill;

public class BillValidator {
    public boolean validateBills(List<Integer> billIds, List<Bill> bills) {
        if (bills.size() != billIds.size()) {
            System.out.println("Sorry! Not found a bill with such ID.");
            return false;
        }
        return true;
    }
}
