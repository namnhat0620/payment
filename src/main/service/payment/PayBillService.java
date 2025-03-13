package main.service.payment;

import java.util.List;
import java.util.stream.Collectors;

import main.domain.Balance;
import main.domain.Bill;
import main.domain.Constants;
import main.domain.PaymentHistory;
import main.helper.BillPaymentProcessor;
import main.service.storage.BalanceStorageService;
import main.service.storage.BillStorageService;
import main.service.storage.PaymentHistoryStorageService;
import main.validator.BillValidator;

public class PayBillService implements PaymentService {

    private final BillStorageService billStorageService;
    private final BalanceStorageService balanceStorageService;
    private final PaymentHistoryStorageService paymentHistoryStorageService;
    private final BillValidator billValidator;
    private final BillPaymentProcessor billPaymentProcessor;

    public PayBillService(BillStorageService billStorageService, BalanceStorageService balanceStorageService,
            PaymentHistoryStorageService paymentHistoryStorageService, BillValidator billValidator,
            BillPaymentProcessor billPaymentProcessor) {
        this.billStorageService = billStorageService;
        this.balanceStorageService = balanceStorageService;
        this.paymentHistoryStorageService = paymentHistoryStorageService;
        this.billValidator = billValidator;
        this.billPaymentProcessor = billPaymentProcessor;
    }

    public void execute(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Please input your bill id.");
        }

        // Get bill
        List<Integer> billIds = getBillIds(args);
        List<Bill> bills = billStorageService.findByIds(billIds);

        // Validate bill
        if (!billValidator.validateBills(billIds, bills)) {
            return;
        }

        // Assume that only 1 user use this application
        // TODO: Implement authentication and allow multiple users
        Balance balance = balanceStorageService.findFirst().orElse(new Balance(1, 0));

        // Pay bill
        if (billPaymentProcessor.payBill(bills, balance)) {
            System.out.println("Payment has been completed for Bill with id "
                    + billIds.stream().map(String::valueOf)
                            .collect(Collectors.joining(Constants.DELIMITER)));

            // Make sure to show the latest balance
            System.out.println(
                    "Your current balance is: " + balanceStorageService.findFirst().map(Balance::getAmount).orElse(0));
        }

        // Save payment history
        bills.stream().map(PaymentHistory::new).forEach(paymentHistoryStorageService::save);
    }

    private List<Integer> getBillIds(String[] args) {
        return List.of(args).subList(1, args.length).stream().map(Integer::valueOf).toList();
    }
}
