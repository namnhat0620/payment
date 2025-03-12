package payment.src;

import payment.src.enumeration.Command;
import payment.src.service.payment.PaymentFactory;
import payment.src.service.payment.PaymentService;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Please input your command");
        }

        PaymentFactory paymentFactory = new PaymentFactory();
        PaymentService paymentService = paymentFactory.getPaymentService(Command.fromValue(args[0]));
        paymentService.excute(args);
    }
}
