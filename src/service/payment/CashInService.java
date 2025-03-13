package src.service.payment;

import src.domain.Balance;
import src.service.storage.BalanceStorageService;

public class CashInService implements PaymentService {

    private final BalanceStorageService balanceStorageService = new BalanceStorageService();

    public void execute(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Please input your cash.");
        }
        Balance balance = balanceStorageService.findFirst().orElse(new Balance(1, 0));
        balance.setAmount(balance.getAmount() + Integer.parseInt(args[1]));

        balanceStorageService.update(balance);

        System.out.println("Your available balance: " + balance);
    }
}
