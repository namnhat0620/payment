package test.service.payment;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import main.service.payment.ListBillService;
import main.service.storage.BillStorageService;

public class ListBillServiceTest {

    private BillStorageService billStorageService;
    private ListBillService listBillService;

    @BeforeEach
    public void setUp() {
        billStorageService = Mockito.mock(BillStorageService.class);
        listBillService = new ListBillService(billStorageService);
    }

    @Test
    public void testConstructor() {
        assertNotNull(listBillService);
    }

    @Test
    public void testExecute() {
        listBillService.execute(new String[] {});
        verify(billStorageService, times(1)).findAll();
    }
}