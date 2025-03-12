package payment.src.service.payment;

import java.util.List;

import payment.src.domain.Bill;
import payment.src.service.storage.BillStorageService;

public class ListBillService implements PaymentService {

    private final BillStorageService billStorageService = new BillStorageService();

    @Override
    public void excute(String[] args) {
        List<Bill> bills = billStorageService.findAll();

        // Print header
        System.out.println("Bill No.\tType\tAmount\tDue Date\tState\tProvider");

        // Print content
        bills.stream().forEach(bill -> System.out.println(bill.toString("\t")));
    }
}
