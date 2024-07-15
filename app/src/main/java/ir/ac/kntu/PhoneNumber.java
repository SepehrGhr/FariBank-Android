package ir.ac.kntu;

import java.io.Serializable;

public class PhoneNumber implements Serializable {

    private long balance;
    private String number;

    public PhoneNumber(String number, long balance) {
        this.balance = balance;
        this.number = number;
    }

    public long getBalance() {
        return balance;
    }

    public String getNumber() {
        return number;
    }

    public void setBalance(int balance) {
        this.balance = Math.max(balance, 0);
    }

    public void charge(long amount) {
        this.balance = getBalance() + amount;
    }
}
