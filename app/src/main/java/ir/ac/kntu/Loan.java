package ir.ac.kntu;

public class Loan {
    private long amount;
    private int months;
    private int delayedCount;
    private User receiver;
    private int paymentCount;
    private boolean finished;

    public Loan(long amount, int months, User receiver){
        this.amount = calculateAmount(amount);
        this.months = months;
        this.receiver = receiver;
    }

    private long calculateAmount(long amount) {
        return (long) (amount * 0.17);
    }
}
