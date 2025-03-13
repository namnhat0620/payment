package src.domain;

import java.time.LocalDate;

import src.enumeration.State;

public class PaymentHistory extends IdentificationEntity {
    private int amount;
    private LocalDate paymentDate;
    private State state;
    private int billId;

    public PaymentHistory(int id, int amount, LocalDate date, State state, int billId) {
        super(id);
        this.amount = amount;
        this.paymentDate = date;
        this.state = state;
        this.billId = billId;
    }

    public PaymentHistory(Bill bill) {
        super(0);
        this.amount = bill.getAmount();
        this.paymentDate = LocalDate.now();
        this.state = getStateByBillState(bill.getState());
        this.billId = bill.getId();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    private State getStateByBillState(State state) {
        if (State.PROCESSED.equals(state)) {
            return State.PROCESSED;
        }
        return State.PENDING;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(super.getId()).append(Constants.DELIMITER)
                .append(amount).append(Constants.DELIMITER)
                .append(paymentDate.format(Constants.DATE_FORMATTER)).append(Constants.DELIMITER)
                .append(state).append(Constants.DELIMITER)
                .append(billId).append(Constants.DELIMITER)
                .toString();
    }

    public String toString(String delimiter) {
        return new StringBuilder()
                .append(super.getId()).append(delimiter)
                .append(amount).append(delimiter)
                .append(paymentDate.format(Constants.DATE_FORMATTER)).append(delimiter)
                .append(state).append(delimiter)
                .append(billId).append(delimiter)
                .toString();
    }
}
