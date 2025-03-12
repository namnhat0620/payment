package payment.src.service.payment;

import payment.src.enumeration.Command;

public class PaymentFactory {
    public PaymentService getPaymentService(Command command) {
        switch (command) {
            case CASH_IN:
                return new CashInService();
            case LIST_BILL:
                return new ListBillService();
            default:
                break;
        }
        return null; // Throw error
    }
}
