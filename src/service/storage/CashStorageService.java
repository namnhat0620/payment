package payment.src.service.storage;

public class CashStorageService extends JsonStorageService<Integer> {
    @Override
    public String getFileName() {
        return BASE_PATH + "cash.txt";
    }

    @Override
    public Integer fromString(String line) {
        return Integer.valueOf(line);
    }

}
