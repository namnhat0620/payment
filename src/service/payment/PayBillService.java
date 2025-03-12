package payment.src.service.payment;

import java.util.List;
import java.util.stream.Collectors;

import payment.src.domain.Bill;
import payment.src.domain.Constants;
import payment.src.enumeration.State;
import payment.src.service.storage.BillStorageService;
import payment.src.service.storage.CashStorageService;

public class PayBillService implements PaymentService {

    private final BillStorageService billStorageService = new BillStorageService();
    private final CashStorageService cashStorageService = new CashStorageService();

    public void excute(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Please input your bill id.");
        }

        // Get bill
        List<Integer> billIds = getBillIds(args);
        List<Bill> bills = billStorageService.findByIds(billIds);
        if (bills.size() != billIds.size()) {
            System.out.println("Sorry! Not found a bill with such id");
            return;
        }
        int balance = cashStorageService.findFirst().orElse(0);

        // Pay bill
        if (payBill(bills, balance)) {
            System.out.println("Payment has been completed for Bill with id "
                    + billIds.stream().map(String::valueOf)
                            .collect(Collectors.joining(Constants.DELIMITER)));

            // Make sure to show the latest balance
            System.out.println("Your current balance is: " + cashStorageService.findFirst().orElse(0));
        }

        // Save payment history
    }

    private List<Integer> getBillIds(String[] args) {
        return List.of(args).subList(1, args.length).stream().map(Integer::valueOf).toList();
    }

    private boolean checkBalance(List<Bill> bills, int balance) {
        int totalAmount = bills.stream().mapToInt(Bill::getAmount).sum();
        if (balance < totalAmount) {
            System.out.println("Sorry! Not enough fund to proceed with payment.");
            return false;
        }
        return true;
    }

    private boolean payBill(List<Bill> bills, int balance) {
        checkBalance(bills, balance);

        for (Bill bill : bills) {
            // Deduct balance
            int remainingBalance = balance - bill.getAmount();
            try {
                cashStorageService.save(remainingBalance);
            } catch (Exception e) {
                System.out.println("Failed to pay bill. Please try again.");
                return false;
            }

            try {
                bill.setState(State.PROCESSED);
                billStorageService.save(bill);
            } catch (Exception e) {
                System.out.println("Failed to pay bill. Please try again.");

                // Rollback balance
                cashStorageService.save(balance);
                return false;
            }
        }
        return true;
    }
}
