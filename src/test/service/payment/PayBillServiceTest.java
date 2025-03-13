package test.service.payment;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.domain.Balance;
import main.domain.Bill;
import main.domain.PaymentHistory;
import main.enumeration.State;
import main.helper.BillPaymentProcessor;
import main.service.payment.PayBillService;
import main.service.storage.BalanceStorageService;
import main.service.storage.BillStorageService;
import main.service.storage.PaymentHistoryStorageService;
import main.validator.BillValidator;

public class PayBillServiceTest {

    private BillStorageService billStorageService;
    private BalanceStorageService balanceStorageService;
    private PaymentHistoryStorageService paymentHistoryStorageService;
    private BillValidator billValidator;
    private BillPaymentProcessor billPaymentProcessor;
    private PayBillService payBillService;

    @BeforeEach
    public void setUp() {
        billStorageService = mock(BillStorageService.class);
        balanceStorageService = mock(BalanceStorageService.class);
        paymentHistoryStorageService = mock(PaymentHistoryStorageService.class);
        billValidator = mock(BillValidator.class);
        billPaymentProcessor = mock(BillPaymentProcessor.class);
        payBillService = new PayBillService(billStorageService, balanceStorageService, paymentHistoryStorageService,
                billValidator, billPaymentProcessor);
    }

    @Test
    public void testConstructor() {
        new PayBillService(billStorageService, balanceStorageService, paymentHistoryStorageService, billValidator,
                billPaymentProcessor);
    }

    @Test
    public void testExecuteWithInvalidArgs() {
        String[] args = { "pay" };
        assertThrows(IllegalArgumentException.class, () -> payBillService.execute(args));
    }

    @Test
    public void testExecuteWithValidArgs() {
        String[] args = { "pay", "1" };
        List<Bill> bills = List.of(new Bill(1, "Electricity", 200, LocalDate.now(), State.PENDING, "Provider"));
        Balance balance = new Balance(1, 200);

        when(billStorageService.findByIds(List.of(1))).thenReturn(bills);
        when(billValidator.validateBills(List.of(1), bills)).thenReturn(true);
        when(balanceStorageService.findFirst()).thenReturn(Optional.of(balance));
        when(billPaymentProcessor.payBill(bills, balance)).thenReturn(true);

        payBillService.execute(args);

        verify(billStorageService).findByIds(List.of(1));
        verify(billValidator).validateBills(List.of(1), bills);
        verify(balanceStorageService, times(2)).findFirst();
        verify(billPaymentProcessor).payBill(bills, balance);
        verify(paymentHistoryStorageService, times(1)).save(any(PaymentHistory.class));
    }

    @Test
    public void testExecuteWithValidArgsAndNoBalance() {
        String[] args = { "pay", "1" };
        List<Bill> bills = List.of(new Bill(1, "Electricity", 200, LocalDate.now(), State.PENDING, "Provider"));

        when(billStorageService.findByIds(List.of(1))).thenReturn(bills);
        when(billValidator.validateBills(List.of(1), bills)).thenReturn(true);
        when(balanceStorageService.findFirst()).thenReturn(Optional.empty());

        payBillService.execute(args);

        verify(billStorageService).findByIds(List.of(1));
        verify(billValidator).validateBills(List.of(1), bills);
        verify(balanceStorageService, times(1)).findFirst();
        verify(billPaymentProcessor, never()).payBill(bills, null);
        verify(paymentHistoryStorageService, times(1)).save(any(PaymentHistory.class));
    }

    @Test
    public void testExecuteWithValidArgsAndNoBills() {
        String[] args = { "pay", "1" };

        when(billStorageService.findByIds(List.of(1))).thenReturn(List.of());
        when(billValidator.validateBills(List.of(1), List.of())).thenReturn(false);

        payBillService.execute(args);

        verify(billStorageService).findByIds(List.of(1));
        verify(billValidator).validateBills(List.of(1), List.of());
        verify(balanceStorageService, never()).findFirst();
        verify(billPaymentProcessor, never()).payBill(anyList(), any(Balance.class));
        verify(paymentHistoryStorageService, never()).save(any(PaymentHistory.class));
    }

    @Test
    public void testExecuteWithValidArgsAndInvalidBills() {
        String[] args = { "pay", "1" };
        List<Bill> bills = List.of(new Bill(1, "Electricity", 200, LocalDate.now(), State.PROCESSED, "Provider"));

        when(billStorageService.findByIds(List.of(1))).thenReturn(bills);
        when(billValidator.validateBills(List.of(1), bills)).thenReturn(false);

        payBillService.execute(args);

        verify(billStorageService).findByIds(List.of(1));
        verify(billValidator).validateBills(List.of(1), bills);
        verify(balanceStorageService, never()).findFirst();
        verify(billPaymentProcessor, never()).payBill(anyList(), any(Balance.class));
        verify(paymentHistoryStorageService, never()).save(any(PaymentHistory.class));
    }

    @Test
    public void testExecuteWithValidArgsAndPaymentFailed() {
        String[] args = { "pay", "1" };
        List<Bill> bills = List.of(new Bill(1, "Electricity", 200, LocalDate.now(), State.PENDING, "Provider"));
        Balance balance = new Balance(1, 200);

        when(billStorageService.findByIds(List.of(1))).thenReturn(bills);
        when(billValidator.validateBills(List.of(1), bills)).thenReturn(true);
        when(balanceStorageService.findFirst()).thenReturn(Optional.of(balance));
        when(billPaymentProcessor.payBill(bills, balance)).thenReturn(false);

        payBillService.execute(args);

        verify(billStorageService).findByIds(List.of(1));
        verify(billValidator).validateBills(List.of(1), bills);
        verify(balanceStorageService, times(1)).findFirst();
        verify(billPaymentProcessor).payBill(bills, balance);
        verify(paymentHistoryStorageService, times(1)).save(any(PaymentHistory.class));
    }
}