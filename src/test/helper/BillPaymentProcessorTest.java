package test.helper;

import main.domain.Balance;
import main.domain.Bill;
import main.enumeration.State;
import main.helper.BillPaymentProcessor;
import main.service.storage.BalanceStorageService;
import main.service.storage.BillStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BillPaymentProcessorTest {

    private BalanceStorageService balanceStorageService;
    private BillStorageService billStorageService;
    private BillPaymentProcessor billPaymentProcessor;

    @BeforeEach
    public void setUp() {
        balanceStorageService = Mockito.mock(BalanceStorageService.class);
        billStorageService = Mockito.mock(BillStorageService.class);
        billPaymentProcessor = new BillPaymentProcessor(balanceStorageService, billStorageService);
    }

    @Test
    public void testPayBill_Success() {
        Balance balance = new Balance(1, 1000);

        Bill bill1 = new Bill(1, "Electricity", 200, null, State.PENDING, "Electricity Provider");
        Bill bill2 = new Bill(2, "Water", 300, null, State.PENDING, "Water Provider");
        List<Bill> bills = Arrays.asList(bill1, bill2);

        boolean result = billPaymentProcessor.payBill(bills, balance);

        assertTrue(result);
        verify(balanceStorageService, times(2)).update(any(Balance.class));
        verify(billStorageService, times(2)).update(any(Bill.class));
        assertTrue(State.PROCESSED.equals(bill1.getState()));
        assertTrue(State.PROCESSED.equals(bill2.getState()));
    }

    @Test
    public void testPayBill_InsufficientBalance() {
        Balance balance = new Balance(1, 100);

        Bill bill1 = new Bill(1, "Electricity", 200, null, State.PENDING, "Electricity Provider");
        List<Bill> bills = Arrays.asList(bill1);

        boolean result = billPaymentProcessor.payBill(bills, balance);

        assertFalse(result);
        verify(balanceStorageService, never()).update(any(Balance.class));
        verify(billStorageService, never()).update(any(Bill.class));
    }

    @Test
    public void testPayBill_BalanceUpdateFails() {
        Balance balance = new Balance(1, 1000);

        Bill bill1 = new Bill(1, "Electricity", 200, null, State.PENDING, "Electricity Provider");
        List<Bill> bills = Arrays.asList(bill1);

        doThrow(new RuntimeException()).when(balanceStorageService).update(any(Balance.class));

        boolean result = billPaymentProcessor.payBill(bills, balance);

        assertFalse(result);
        verify(balanceStorageService, times(1)).update(any(Balance.class));
        verify(billStorageService, never()).update(any(Bill.class));
    }

    @Test
    public void testPayBill_BillSaveFails() {
        Balance balance = new Balance(1, 1000);

        Bill bill1 = new Bill(1, "Electricity", 200, null, State.PENDING, "Electricity Provider");
        List<Bill> bills = Arrays.asList(bill1);

        doThrow(new RuntimeException()).when(billStorageService).update(any(Bill.class));

        boolean result = billPaymentProcessor.payBill(bills, balance);

        assertFalse(result);
        verify(balanceStorageService, times(2)).update(any(Balance.class)); // Once for deduction, once for rollback
        verify(billStorageService, times(1)).update(any(Bill.class));
    }
}