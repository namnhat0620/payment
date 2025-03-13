package main.domain;

public class Balance extends IdentificationEntity {
    private int amount;

    public Balance(int id, int amount) {
        super(id);
        this.amount = amount;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(super.getId()).append(Constants.DELIMITER)
                .append(amount).append(Constants.DELIMITER)
                .toString();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
