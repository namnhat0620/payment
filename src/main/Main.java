package main;

import main.enumeration.Command;
import main.helper.BillPaymentProcessor;
import main.service.payment.PaymentFactory;
import main.service.payment.PaymentService;
import main.service.storage.BalanceStorageService;
import main.service.storage.BillStorageService;
import main.service.storage.PaymentHistoryStorageService;
import main.validator.BillValidator;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to Payment Service");
        if (args.length == 0) {
            throw new IllegalArgumentException("Please input your command");
        }

        // Create instance
        BalanceStorageService balanceStorageService = new BalanceStorageService();
        BillStorageService billStorageService = new BillStorageService();
        PaymentHistoryStorageService paymentHistoryStorageService = new PaymentHistoryStorageService();
        BillValidator billValidator = new BillValidator();
        BillPaymentProcessor billPaymentProcessor = new BillPaymentProcessor(balanceStorageService, billStorageService);

        // Create factory
        PaymentFactory paymentFactory = new PaymentFactory(balanceStorageService, billStorageService,
                paymentHistoryStorageService, billValidator, billPaymentProcessor);

        // Get service
        PaymentService paymentService = paymentFactory.getPaymentService(Command.fromValue(args[0]));
        paymentService.execute(args);
    }
}
