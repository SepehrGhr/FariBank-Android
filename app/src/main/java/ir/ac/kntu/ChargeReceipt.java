package ir.ac.kntu;

public class ChargeReceipt extends Receipt {
    private long newBalance;

    public ChargeReceipt(long amount, long newBalance) {
        super(amount);
        this.newBalance = newBalance;
    }

    @Override
    public String toString() {
        return super.toString() + "New balance : "  + newBalance + '\n';
    }
}

