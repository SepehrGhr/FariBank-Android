package ir.ac.kntu;

import java.io.Serializable;

public class SavingFund extends Fund implements Serializable {

    public SavingFund(User owner) {
        super(owner);
    }

    @Override
    public void deposit(long amount) {
        if (Main.getUsers().getCurrentUser().getAccount().getBalance() >= amount) {
            setBalance(getBalance() + amount);
            Main.getUsers().getCurrentUser().getAccount().withdrawMoney(amount, getOwner());
        } else {
            System.out.println(Color.RED + "Your account's balance is not enough" + Color.RESET);
        }
    }

    @Override
    public boolean withdraw(long amount) {
        if (getBalance() >= amount) {
            setBalance(getBalance() - amount);
            Main.getUsers().getCurrentUser().getAccount().setBalance(getOwner().getAccount().getBalance() + amount);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getType(){
        return Color.PURPLE + "Saving Fund" + Color.RESET;
    }

    @Override
    public String getName() {
        return "Saving Fund";
    }

    @Override
    public String toString() {
        return "Saving Fund:" + '\n' + "Balance: "
                +  getBalance() + '\n';
    }
}
