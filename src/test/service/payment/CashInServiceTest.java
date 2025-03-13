package test.service.payment;

import main.domain.Balance;
import main.service.payment.CashInService;
import main.service.storage.BalanceStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class CashInServiceTest {

    private CashInService cashInService;
    private BalanceStorageService balanceStorageService;

    @BeforeEach
    public void setUp() {
        balanceStorageService = Mockito.mock(BalanceStorageService.class);
        cashInService = new CashInService(balanceStorageService);
    }

    @Test
    public void testExecuteWithValidInput() {
        Balance balance = new Balance(1, 100);
        when(balanceStorageService.findFirst()).thenReturn(Optional.of(balance));

        cashInService.execute(new String[] { "cashin", "50" });

        assertEquals(150, balance.getAmount());
        verify(balanceStorageService).update(balance);
    }

    @Test
    public void testExecuteWithNoInput() {
        assertThrows(IllegalArgumentException.class, () -> cashInService.execute(new String[] {}));
    }

    @Test
    public void testExecuteWithNewBalance() {
        when(balanceStorageService.findFirst()).thenReturn(Optional.empty());

        cashInService.execute(new String[] { "cashin", "50" });

        verify(balanceStorageService).update(any(Balance.class));
    }
}
