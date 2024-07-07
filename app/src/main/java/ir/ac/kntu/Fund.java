package ir.ac.kntu;

public abstract class Fund {
    private long balance;
    private User owner;

    public Fund(User owner) {
        this.owner = owner;
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

    public abstract void withdraw(long amount);

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

    public abstract void showBalance();
}
