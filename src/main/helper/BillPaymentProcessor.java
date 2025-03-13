package main.helper;

import java.util.List;

import main.domain.Balance;
import main.domain.Bill;
import main.enumeration.State;
import main.service.storage.BalanceStorageService;
import main.service.storage.BillStorageService;

public class BillPaymentProcessor {

    private final BalanceStorageService balanceStorageService;
    private final BillStorageService billStorageService;

    public BillPaymentProcessor(BalanceStorageService balanceStorageService, BillStorageService billStorageService) {
        this.balanceStorageService = balanceStorageService;
        this.billStorageService = billStorageService;
    }

    public boolean payBill(List<Bill> bills, Balance balance) {
        if (!checkBalance(bills, balance)) {
            return false;
        }

        for (Bill bill : bills) {
            // Deduct balance
            int baseAmount = balance.getAmount();
            int remainingAmount = baseAmount - bill.getAmount();

            try {
                balance.setAmount(remainingAmount);
                balanceStorageService.update(balance);
            } catch (Exception e) {
                System.out.println("Failed to pay bill. Please try again.");
                return false;
            }

            try {
                bill.setState(State.PROCESSED);
                billStorageService.update(bill);
            } catch (Exception e) {
                System.out.println("Failed to pay bill. Please try again.");

                // Rollback balance
                balance.setAmount(baseAmount);
                balanceStorageService.update(balance);
                return false;
            }
        }
        return true;
    }

    private boolean checkBalance(List<Bill> bills, Balance balance) {
        int totalAmount = bills.stream().mapToInt(Bill::getAmount).sum();
        if (balance.getAmount() < totalAmount) {
            System.out.println("Sorry! Not enough fund to proceed with payment.");
            return false;
        }
        return true;
    }
}
