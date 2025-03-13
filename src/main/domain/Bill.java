package main.domain;

import java.time.LocalDate;

import main.enumeration.State;

public class Bill extends IdentificationEntity {
    private String type;
    private int amount;
    private LocalDate dueDate;
    private State state;
    private String provider;

    public Bill(int id, String type, int amount, LocalDate dueDate, State state, String provider) {
        super(id);
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.state = state;
        this.provider = provider;
    }

    public int getAmount() {
        return amount;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(super.getId()).append(Constants.DELIMITER)
                .append(type).append(Constants.DELIMITER)
                .append(amount).append(Constants.DELIMITER)
                .append(dueDate.format(Constants.DATE_FORMATTER)).append(Constants.DELIMITER)
                .append(state).append(Constants.DELIMITER)
                .append(provider).append(Constants.DELIMITER)
                .toString();
    }

    public String toString(String delimiter) {
        return new StringBuilder()
                .append(super.getId()).append(delimiter)
                .append(type).append(delimiter)
                .append(amount).append(delimiter)
                .append(dueDate.format(Constants.DATE_FORMATTER)).append(delimiter)
                .append(state).append(delimiter)
                .append(provider).append(delimiter)
                .toString();
    }
}
