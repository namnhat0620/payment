package src.service.payment;

import java.util.List;
import java.util.stream.Collectors;

import src.domain.Balance;
import src.domain.Bill;
import src.domain.Constants;
import src.domain.PaymentHistory;
import src.helper.BillPaymentProcessor;
import src.service.storage.BalanceStorageService;
import src.service.storage.BillStorageService;
import src.service.storage.PaymentHistoryStorageService;
import src.validator.BillValidator;

public class PayBillService implements PaymentService {

    private final BillStorageService billStorageService = new BillStorageService();
    private final BalanceStorageService balanceStorageService = new BalanceStorageService();
    private final PaymentHistoryStorageService paymentHistoryStorageService = new PaymentHistoryStorageService();
    private final BillValidator billValidator = new BillValidator();
    private final BillPaymentProcessor billPaymentProcessor = new BillPaymentProcessor(
            balanceStorageService,
            billStorageService);

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
