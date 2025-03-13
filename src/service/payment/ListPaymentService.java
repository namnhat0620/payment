package src.service.payment;

import java.util.List;

import src.domain.PaymentHistory;
import src.service.storage.PaymentHistoryStorageService;

public class ListPaymentService implements PaymentService {

    private final PaymentHistoryStorageService paymentHistoryStorageService = new PaymentHistoryStorageService();

    public void execute(String[] args) {
        List<PaymentHistory> paymentHistories = paymentHistoryStorageService.findAll();

        // Print header
        System.out.println("No.\tAmount\tPayment Date\tState\tBill id");

        // Print content
        paymentHistories.stream().forEach(paymentHistory -> System.out.println(paymentHistory.toString("\t")));
    }
}
