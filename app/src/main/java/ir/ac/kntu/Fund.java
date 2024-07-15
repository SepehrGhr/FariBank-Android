package ir.ac.kntu;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Fund implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final AtomicLong ID_COUNTER = new AtomicLong(1);

    private final long id;
    private long balance;
    private User owner;

    public Fund(User owner) {
        this.owner = owner;
        this.id = ID_COUNTER.getAndIncrement();
    }

    public long getId() {
        return id;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = Math.max(0, balance);
    }

    public User getOwner() {
        return owner;
    }

    public abstract void deposit(long amount);

    public abstract boolean withdraw(long amount);

    public abstract String getType();

    public abstract String getName();
}
