package test.service.payment;

import main.service.payment.ListPaymentService;
import main.service.storage.PaymentHistoryStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ListPaymentServiceTest {

    private PaymentHistoryStorageService paymentHistoryStorageService;
    private ListPaymentService listPaymentService;

    @BeforeEach
    public void setUp() {
        paymentHistoryStorageService = Mockito.mock(PaymentHistoryStorageService.class);
        listPaymentService = new ListPaymentService(paymentHistoryStorageService);
    }

    @Test
    public void testConstructor() {
        assertNotNull(listPaymentService);
    }

    @Test
    public void testListPayment() {
        listPaymentService.execute(new String[] {});
        Mockito.verify(paymentHistoryStorageService, Mockito.times(1)).findAll();
    }
}