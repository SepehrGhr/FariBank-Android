package ir.ac.kntu;

import java.io.Serializable;

public class InterestFund extends Fund implements Serializable {
    private final int mustReceiveCount;
    private boolean canWithdraw;
    private int receivedCount;

    private long interestAmount;

    public InterestFund(User owner, int mustReceiveCount, long balance) {
        super(owner);
        this.mustReceiveCount = mustReceiveCount;
        this.canWithdraw = false;
        this.setInterestAmount(balance);
        setBalance(balance);
    }

    private void setInterestAmount(long balance) {
        interestAmount =(long)(balance * Main.getManagerData().getInterestRate()) / 12;
    }

    public int getMustReceiveCount() {
        return mustReceiveCount;
    }

    public int getReceivedCount() {
        return receivedCount;
    }

    @Override
    public void deposit(long amount) {
        if(canWithdraw){
            if (Main.getUsers().getCurrentUser().getAccount().getBalance() >= amount) {
                setBalance(getBalance() + amount);
                Main.getUsers().getCurrentUser().getAccount().withdrawMoney(amount, Main.getUsers().getCurrentUser());
                System.out.println(Color.GREEN + "Selected amount was successfully deposited to your interest fund" + Color.RESET);
            } else {
                System.out.println(Color.RED + "Your account's balance is not enough" + Color.RESET);
            }
        } else {
            System.out.println(Color.RED + "You cant currently deposit to this interest fund (sleep phase hasn't finished)" + Color.RESET);
        }
    }

    @Override
    public boolean withdraw(long amount) {
        if(canWithdraw){
            if (getBalance() >= amount) {
                setBalance(getBalance() - amount);
                Main.getUsers().getCurrentUser().getAccount().setBalance(getOwner().getAccount().getBalance() + amount);
                return true;
            } else {
                return false;
            }
        } else{
            return false;
        }

    }

    @Override
    public String getName() {
        return "Interest Fund";
    }

    @Override
    public String getType() {
        return Color.CYAN + "Interest Fund" + Color.RESET;
    }

    public void depositInterest() {
        getOwner().getAccount().setBalance(getOwner().getAccount().getBalance() + interestAmount);
        receivedCount++;
        if(receivedCount == mustReceiveCount){
            canWithdraw = true;
        }
    }
}
