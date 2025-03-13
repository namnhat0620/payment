package main.service.payment;

import java.util.List;

import main.domain.PaymentHistory;
import main.service.storage.PaymentHistoryStorageService;

public class ListPaymentService implements PaymentService {

    private final PaymentHistoryStorageService paymentHistoryStorageService;

    public ListPaymentService(PaymentHistoryStorageService paymentHistoryStorageService) {
        this.paymentHistoryStorageService = paymentHistoryStorageService;
    }

    public void execute(String[] args) {
        List<PaymentHistory> paymentHistories = paymentHistoryStorageService.findAll();

        // Print header
        System.out.println("No.\tAmount\tPayment Date\tState\tBill id");

        // Print content
        paymentHistories.stream().forEach(paymentHistory -> System.out.println(paymentHistory.toString("\t")));
    }
}
