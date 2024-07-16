package ir.ac.kntu;

import java.io.Serializable;

public class Loan implements Serializable {
    private long amount;
    private int months;
    private int delayedCount;
    private User receiver;
    private int paymentCount;
    private boolean finished = false;
    private long paymentAmount;
    private int monthsPassed;

    public Loan(long amount, int months, User receiver) {
        this.amount = calculateAmount(amount);
        this.months = months;
        this.receiver = receiver;
        this.paymentAmount = calculatePayment(this.amount);
    }

    public long getAmount() {
        return amount;
    }

    public int getMonths() {
        return months;
    }

    public int getDelayedCount() {
        return delayedCount;
    }

    public User getReceiver() {
        return receiver;
    }

    public int getPaymentCount() {
        return paymentCount;
    }

    public boolean isFinished() {
        return finished;
    }

    public long getPaymentAmount() {
        return paymentAmount;
    }

    public int getMonthsPassed() {
        return monthsPassed;
    }

    private long calculatePayment(long amount) {
        return amount / months;
    }

    private long calculateAmount(long amount) {
        return (long) (amount + amount * 0.17);
    }

    public void firstOfMonthOperation() {
        if (!finished) {
            monthsPassed++;
            if (paymentCount < monthsPassed && monthsPassed <= months) {
                delayedCount++;
            }
        }
    }

    public void doPayment() {
        if (!finished) {
            paymentCount++;
            if (delayedCount > 0) {
                delayedCount--;
            }
            if (paymentCount == months) {
                finished = true;
            }
        }
    }
}
