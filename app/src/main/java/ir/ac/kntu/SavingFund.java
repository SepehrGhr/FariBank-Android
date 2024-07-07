package ir.ac.kntu;

public class SavingFund extends Fund {

    public SavingFund(User owner) {
        super(owner);
    }

    @Override
    public void deposit(long amount) {
        if (getOwner().getAccount().getBalance() >= amount) {
            setBalance(getBalance() + amount);
            getOwner().getAccount().setBalance(getOwner().getAccount().getBalance() - amount);
            System.out.println(Color.GREEN + "Selected amount was successfully deposited to your saving fund" + Color.RESET);
        } else {
            System.out.println(Color.RED + "Your account's balance is not enough" + Color.RESET);
        }
    }

    @Override
    public void withdraw(long amount) {
        if (getBalance() >= amount) {
            setBalance(getBalance() - amount);
            getOwner().getAccount().setBalance(getOwner().getAccount().getBalance() + amount);
            System.out.println(Color.GREEN + "Selected amount was successfully withdrew from your saving fund" + Color.RESET);
        } else {
            System.out.println(Color.RED + "Selected Fund's balance is not enough" + Color.RESET);
        }
    }

    @Override
    public String getType(){
        return Color.PURPLE + "Saving Fund" + Color.RESET;
    }

    @Override
    public void showBalance() {
        System.out.println(Color.WHITE + "Selected saving fund's current balance : " + Color.GREEN + getBalance() + Color.RESET);
    }

    @Override
    public String toString() {
        return Color.CYAN + "*".repeat(35) + '\n' + Color.BLUE + "Saving Fund:" + '\n' + Color.WHITE + "Balance: "
                + Color.BLUE + getBalance() + '\n' + Color.CYAN + "*".repeat(35) + Color.RESET;
    }
}
