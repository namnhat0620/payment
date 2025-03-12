package payment.src.service.payment;

import payment.src.service.storage.CashStorageService;

public class CashInService implements PaymentService {

    private final CashStorageService cashStorageService = new CashStorageService();

    public void excute(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Please input your cash.");
        }
        Integer balance = cashStorageService.findFirst().orElse(0) + Integer.valueOf(args[1]);
        cashStorageService.save(balance);

        System.out.println("Your available balance: " + balance);
    }
}
