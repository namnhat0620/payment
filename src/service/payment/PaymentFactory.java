package src.service.payment;

import src.enumeration.Command;

public class PaymentFactory {
    public PaymentService getPaymentService(Command command) {
        switch (command) {
            case CASH_IN:
                return new CashInService();
            case LIST_BILL:
                return new ListBillService();
            case PAY:
                return new PayBillService();
            case LIST_PAYMENT:
                return new ListPaymentService();
            default:
                break;
        }
        return null; // Throw error
    }
}
