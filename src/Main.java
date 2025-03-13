package src;

import src.enumeration.Command;
import src.service.payment.PaymentFactory;
import src.service.payment.PaymentService;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Please input your command");
        }

        PaymentFactory paymentFactory = new PaymentFactory();
        PaymentService paymentService = paymentFactory.getPaymentService(Command.fromValue(args[0]));
        paymentService.execute(args);
    }
}
