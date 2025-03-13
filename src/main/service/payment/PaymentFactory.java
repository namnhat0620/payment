package main.service.payment;

import main.enumeration.Command;
import main.helper.BillPaymentProcessor;
import main.service.storage.BalanceStorageService;
import main.service.storage.BillStorageService;
import main.service.storage.PaymentHistoryStorageService;
import main.validator.BillValidator;

public class PaymentFactory {

    private final BalanceStorageService balanceStorageService;
    private final BillStorageService billStorageService;
    private final PaymentHistoryStorageService paymentHistoryStorageService;
    private final BillValidator billValidator;
    private final BillPaymentProcessor billPaymentProcessor;

    public PaymentFactory(BalanceStorageService balanceStorageService, BillStorageService billStorageService,
            PaymentHistoryStorageService paymentHistoryStorageService,
            BillValidator billValidator, BillPaymentProcessor billPaymentProcessor) {
        this.balanceStorageService = balanceStorageService;
        this.billStorageService = billStorageService;
        this.paymentHistoryStorageService = paymentHistoryStorageService;
        this.billValidator = billValidator;
        this.billPaymentProcessor = billPaymentProcessor;
    }

    public PaymentService getPaymentService(Command command) {
        switch (command) {
            case CASH_IN:
                return new CashInService(balanceStorageService);
            case LIST_BILL:
                return new ListBillService(billStorageService);
            case PAY:
                return new PayBillService(billStorageService, balanceStorageService, paymentHistoryStorageService,
                        billValidator, billPaymentProcessor);
            case LIST_PAYMENT:
                return new ListPaymentService(paymentHistoryStorageService);
            default:
                break;
        }
        return null; // Throw error
    }
}
