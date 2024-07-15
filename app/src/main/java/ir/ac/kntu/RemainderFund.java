package ir.ac.kntu;

import java.io.Serializable;

public class RemainderFund extends Fund implements Serializable {

    public RemainderFund(User owner) {
        super(owner);
    }

    @Override
    public void deposit(long amount) {
        if (Main.getUsers().getCurrentUser().getAccount().getBalance() >= amount) {
            setBalance(getBalance() + amount);
            Main.getUsers().getCurrentUser().getAccount().withdrawMoney(amount, Main.getUsers().getCurrentUser());
            System.out.println(Color.GREEN + "Selected amount was successfully deposited to your remainder fund" + Color.RESET);
        } else {
            System.out.println(Color.RED + "Your account's balance is not enough" + Color.RESET);
        }
    }

    public long separateWorthlessDigits(long number) {
        int count = (int) (0.75 * Long.toString(number).length());
        return Long.parseLong(Long.toString(number).substring(Long.toString(number).length() - count));
    }

    public long findNearestTenPower(long number) {
        int power = 0;
        while (true) {
            if (Math.pow(10, power) > number) {
                return (long) Math.pow(10, power);
            }
            power++;
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
    public String getType() {
        return Color.YELLOW + "Remainder Fund" + Color.RESET;
    }

    @Override
    public String getName() {
        return "Remainder Fund";
    }

    public void handleAccountWithdraw(long amount) {
        getOwner().getAccount().setBalance(getOwner().getAccount().getBalance() - amount);
        long worthless = separateWorthlessDigits(amount);
        long remainder = findNearestTenPower(worthless) - worthless;
        if(remainder <= getOwner().getAccount().getBalance()){
            setBalance(getBalance() + remainder);
            getOwner().getAccount().setBalance(getOwner().getAccount().getBalance() - remainder);
        }
    }
}
