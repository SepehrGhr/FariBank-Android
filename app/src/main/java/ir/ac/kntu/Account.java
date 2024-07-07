package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Account {
    private String accountID;
    private CreditCard creditCard;
    private long balance;
    private List<Long> balances;

    public Account() {
        setAccountID();
        creditCard = new CreditCard();
        this.balance = 0;
        balances = new ArrayList<>();
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public String generateAccountID() {
        String accID = "0";
        Random random = new Random();
        for (int i = 1; i < 10; i++) {
            accID += Integer.toString(random.nextInt(10));
        }
        return accID + "000";
    }

    public void setAccountID() {
        String accID = generateAccountID();
        while (Main.getUsers().accountIdAlreadyExists(accID)) {
            accID = generateAccountID();
        }
        accountID = accID;
    }

    public void setAccountID(String accID) {
        accountID = accID;
    }

    public String getAccountID() {
        return accountID;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
        balances.add(getBalance());
    }

    public boolean cardNumberOrIDValidity(String idOrNumber) {
        String idRegex = "\\d+";
        Pattern idPattern = Pattern.compile(idRegex);
        Matcher idMatcher = idPattern.matcher(idOrNumber);
        return idMatcher.matches()  && (idOrNumber.length() == 12 || idOrNumber.length() == 16);
    }

    public void printChargeAccount() {
        System.out.println(Color.WHITE + "Please enter the amount your trying to charge your account (Maximum 12 digits)" + Color.RESET);
        String input = InputManager.getInput();
        while (!InputManager.chargeAmountValidity(input)) {
            System.out.println(Color.RED + "Please enter a valid number (Maximum 12 digits , Minimum 1)" + Color.RESET);
            input = InputManager.getInput();
        }
        chargeAccount(Long.parseLong(input));
    }

    public void chargeAccount(long amount) {
        setBalance(getBalance() + amount);
        ChargeReceipt newReceipt = new ChargeReceipt(amount, balance);
        Main.getUsers().getCurrentUser().addReceipt(newReceipt);
        System.out.println(newReceipt);
    }

    public void displayBalance() {
        System.out.println(Color.WHITE + "Your current balance is " + Color.GREEN + getBalance() + "$" + Color.RESET);
    }

    public void withdrawMoney(long amount, User currentUser) {
        if(!currentUser.isHasRemainderFund()){
            currentUser.getAccount().setBalance(currentUser.getAccount().getBalance() - amount);
            return;
        }
        currentUser.getRemainderFund().handleAccountWithdraw(amount);
    }
}
