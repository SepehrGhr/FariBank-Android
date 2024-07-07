package ir.ac.kntu;

public class PhoneNumber {

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

    public void printChargeSimCard() {
        //System.out.println(Color.WHITE + "Please enter the amount your trying to charge the selected number (Maximum 12 digits)" + Color.RESET);
        String amount = InputManager.getInput();
        //while (!InputManager.chargeAmountValidity(amount)) {
            //System.out.println(Color.RED + "Please enter a valid number (Maximum 12 digits , Minimum 1)" + Color.RESET);
            //amount = InputManager.getInput();
        //}
        if (!printConfirmation(this, amount)) {
            return;
        }
        checkIsBalanceEnough(this, amount);
    }

    public void checkIsBalanceEnough(PhoneNumber phoneNumber, String amount) {
        long currentBalance = Main.getUsers().getCurrentUser().getAccount().getBalance();
        if (currentBalance > Long.parseLong(amount) + Main.getManagerData().getFeeRate().getSimCardFee()) {
            phoneNumber.charge(Long.parseLong(amount));
            Main.getUsers().getCurrentUser().getAccount().withdrawMoney(Long.parseLong(amount) +
                    Main.getManagerData().getFeeRate().getSimCardFee(), Main.getUsers().getCurrentUser());
            SimcardReceipt newReceipt = new SimcardReceipt(Long.parseLong(amount), phoneNumber.getNumber());
            Main.getUsers().getCurrentUser().addReceipt(newReceipt);
            //System.out.println(Color.GREEN + "Selected number has been charged successfully!!" + Color.RESET);
        } else {
            //System.out.println(Color.RED + "Your account balance is not enough!" + Color.RESET);
        }
    }

    private boolean printConfirmation(PhoneNumber destination, String amount) {
        System.out.println(Color.YELLOW + "<>".repeat(20) + Color.RESET);
        System.out.println(Color.WHITE + "Your Transaction details:" + Color.RESET);
        System.out.println(Color.WHITE + "Amount: " + Color.GREEN + amount + " + "
                + Main.getManagerData().getFeeRate().getSimCardFee() + Color.WHITE + " (fee)" + Color.RESET);
        System.out.println(Color.WHITE + "Destination number : " + Color.BLUE + destination.getNumber() + Color.RESET);
        System.out.println(Color.YELLOW + "<>".repeat(20) + Color.RESET);
        System.out.println(Color.WHITE + "Enter " + Color.GREEN + "1 " + Color.WHITE + "to confirm and " + Color.RED +
                "2 " + Color.WHITE + "to cancel" + Color.RESET);
        String selection = InputManager.getSelection(2);
        return "1".equals(selection);
    }

    public void displayBalance() {
        //System.out.println(Color.WHITE + "Your sim card's current balance is: " + Color.GREEN + balance + Color.RESET);
    }
}
