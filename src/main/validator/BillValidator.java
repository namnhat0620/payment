package main.validator;

import java.util.List;
import main.domain.Bill;
import main.enumeration.State;

public class BillValidator {
    public boolean validateBills(List<Integer> billIds, List<Bill> bills) {
        if (bills.size() != billIds.size()) {
            System.out.println("Sorry! Not found a bill with such ID.");
            return false;
        }
        if (bills.stream().anyMatch(bill -> State.PROCESSED.equals(bill.getState()))) {
            System.out.println("Some of the bills have been processed.");
            return false;
        }
        return true;
    }
}
