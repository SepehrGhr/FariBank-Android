package ir.ac.kntu;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Fund implements Serializable {
    private static final long serialVersionUID = 1L; // Serializable ID
    private static final AtomicLong ID_COUNTER = new AtomicLong(1); // Atomic counter for unique IDs

    private final long id; // Unique identifier for each instance
    private long balance;
    private User owner;

    // Constructor to initialize owner and generate a unique ID
    public Fund(User owner) {
        this.owner = owner;
        this.id = ID_COUNTER.getAndIncrement(); // Generate a unique ID
    }

    // Getter for ID
    public long getId() {
        return id;
    }

    // Getter and setter for balance
    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = Math.max(0, balance);
    }

    // Getter for owner
    public User getOwner() {
        return owner;
    }

    // Abstract methods to be implemented by subclasses
    public abstract void deposit(long amount);

    public abstract boolean withdraw(long amount);

    public abstract String getType();

    public void openManagement() {
        Menu.printMenu(OptionEnums.SelectedFundMenu.values(), this::handleManagementInput);
    }

    public void handleManagementInput() {
        String selection = InputManager.getSelection(4);
        switch (selection) {
            case "1" -> {
                String amount = getAmount();
                this.deposit(Long.parseLong(amount));
            }
            case "2" -> {
                String amount = getAmount();
                this.withdraw(Long.parseLong(amount));
            }
            case "3" -> {
                this.showBalance();
            }
            default -> {
            }
        }
    }

    public String getAmount() {
        System.out.println(Color.WHITE + "Please enter the amount you want" + Color.RESET);
        String amount = InputManager.getInput();
        while (!InputManager.chargeAmountValidity(amount)) {
            System.out.println(Color.RED + "Please enter a valid number (Maximum 12 digits , Minimum 1)" + Color.RESET);
            amount = InputManager.getInput();
        }
        return amount;
    }

    public abstract String getName();

    public abstract void showBalance();
}
