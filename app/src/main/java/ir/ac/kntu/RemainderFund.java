package ir.ac.kntu;

public class RemainderFund extends Fund {

    public RemainderFund(User owner) {
        super(owner);
    }

    @Override
    public void deposit(long amount) {
        if (getOwner().getAccount().getBalance() >= amount) {
            setBalance(getBalance() + amount);
            getOwner().getAccount().setBalance(getOwner().getAccount().getBalance() - amount);
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
    public void withdraw(long amount) {
        if (getBalance() >= amount) {
            setBalance(getBalance() - amount);
            getOwner().getAccount().setBalance(getOwner().getAccount().getBalance() + amount);
            System.out.println(Color.GREEN + "Selected amount was successfully withdrew from your remainder fund" + Color.RESET);
        } else {
            System.out.println(Color.RED + "Selected Fund's balance is not enough" + Color.RESET);
        }
    }

    @Override
    public String getType() {
        return Color.YELLOW + "Remainder Fund" + Color.RESET;
    }

    @Override
    public void showBalance() {
        System.out.println(Color.WHITE + "Selected remainder fund's current balance : " + Color.GREEN + getBalance() + Color.RESET);
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
