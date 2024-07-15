package ir.ac.kntu;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class UserData {

    private final List<User> allUsers;
    private final List<User> foreignUsers;
    private User currentUser;
    private final List<PhoneNumber> unregistereds;

    public UserData() {
        this.allUsers = new ArrayList<>();
        this.foreignUsers = new ArrayList<>();
        this.unregistereds = new ArrayList<>();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void addUser(User newUser) {
        allUsers.add(newUser);
    }

    public void addForeignUser(User newUser) {
        foreignUsers.add(newUser);
    }

    public boolean unregisteredAlreadyExists(String phoneNumber) {
        for (PhoneNumber number : unregistereds) {
            if (phoneNumber.equals(number.getNumber())) {
                return true;
            }
        }
        return false;
    }

    public PhoneNumber getUnregistered(String phoneNumber) {
        for (PhoneNumber number : unregistereds) {
            if (number.getNumber().equals(phoneNumber)) {
                unregistereds.remove(number);
                return number;
            }
        }
        return null;
    }

    public User findUserByPhoneNumber(String phoneNumber) {
        for (User user : allUsers) {
            if (user.getPhoneNumber().equals(phoneNumber)) {
                return user;
            }
        }
        return null;
    }

    public User findUserBySecurityNumber(String securityNumber) {
        for (User user : allUsers) {
            if (user.getSecurityNumber().equals(securityNumber)) {
                return user;
            }
        }
        return null;
    }

    public boolean accountIdAlreadyExists(String accID) {
        for (User user : allUsers) {
            if (user.isAuthenticated() && user.getAccount().getAccountID().equals(accID)) {
                return true;
            }
        }
        return false;
    }

    Entry<User, Boolean> findUserByCardNumber(String cardNumber) {
        for (User user : allUsers) {
            if (user.isAuthenticated() && user.getAccount().getCreditCard().getCardNumber().equals(cardNumber)) {
                return new SimpleEntry<>(user, true);
            }
        }
        for (User user : foreignUsers) {
            if (user.isAuthenticated() && user.getAccount().getCreditCard().getCardNumber().equals(cardNumber)) {
                return new SimpleEntry<>(user, false);
            }
        }
        return new SimpleEntry<>(null, false);
    }



    public long handleMethodLimits(long amount, String method, User destination) {
        if ("FariToFari".equals(method)) {
            return amount + Main.getManagerData().getFeeRate().getFariFee();
        }
        if ("Paya".equals(method)) {
            handlePayaMethod(amount, destination, false);
            return -1;
        }
        if ("Pol".equals(method)) {
            if (amount > 5000000) {
                System.out.println(Color.RED + "The maximum transfer amount for Pol method id 5 Million" + Color.RESET);
                return -1;
            }
            return amount + (long) (amount * Main.getManagerData().getFeeRate().getPolFee());
        }
        if (amount > 100000) {
            System.out.println(Color.RED + "The maximum transfer amount for non-fari bank cart to card is 100000" + Color.RESET);
            return -1;
        }
        return amount + Main.getManagerData().getFeeRate().getCardFee();
    }

    public void handlePayaMethod(long amount, User destination, boolean confirm) {
        if (amount > 5000000) {
            System.out.println(Color.RED + "The maximum transfer amount for Paya method is 5 Million" + Color.RESET);
            return;
        } else if (amount + Main.getManagerData().getFeeRate().getPayaFee() > currentUser.getAccount().getBalance()) {
            System.out.println(Color.RED + "Your balance is not enough. Current Balance : " +
                    Color.GREEN + currentUser.getAccount().getBalance() + Color.RED + "required balance:"
                    + Color.GREEN + (amount + Main.getManagerData().getFeeRate().getPayaFee()) + Color.RESET);
            return;
        }
//        if (!confirm) {
//            confirm = printConfirmation(destination, amount, amount + Main.getManagerData().getFeeRate().getPayaFee());
//        }
//        if (!confirm) {
//            return;
//        }
        Paya newPaya = new Paya(destination, Main.getUsers().getCurrentUser(), amount);
        currentUser.getAccount().withdrawMoney(amount + Main.getManagerData().getFeeRate().getPayaFee(), currentUser);
        Main.getManagerData().addNewPaya(newPaya);
        System.out.println(Color.GREEN + "Your Paya transfer request has been submitted and will be done in less than 48 hours!"
                + Color.RESET);
    }

    private String selectTransferMethod(boolean fromFari, boolean isAccountID) {
        if (fromFari) {
            return "FariToFari";
        }
        String selection = "";
        if (isAccountID) {
            System.out.println(Color.WHITE + "Please select your transfer method" + Color.RESET);
            System.out.println(Color.WHITE + "1-Paya " + Color.BLUE + "(will take some time)" + Color.RESET);
            System.out.println(Color.WHITE + "2-Pol" + Color.RESET);
            selection = InputManager.getSelection(2);
        } else {
            System.out.println(Color.WHITE + "Please select your transfer method" + Color.RESET);
            System.out.println(Color.WHITE + "1-Paya " + Color.BLUE + "(will take some time)" + Color.RESET);
            System.out.println(Color.WHITE + "2-Pol" + Color.RESET);
            System.out.println(Color.WHITE + "3-Card to card" + Color.RESET);
            selection = InputManager.getSelection(3);
        }
        String[] options = {"Paya", "Pol", "Card to card"};
        return options[Integer.parseInt(selection) - 1];
    }


    private void updateBalances(User destination, String amount, long amountAfterFee) {
        destination.getAccount().setBalance(destination.getAccount().getBalance() + Long.parseLong(amount));
        currentUser.getAccount().withdrawMoney(amountAfterFee, currentUser);
        //System.out.println(Color.GREEN + "The money has been transferred successfully!!" + Color.RESET);
    }

    public Entry<User, Boolean> findUserByAccountID(String accountID) {
        for (User user : allUsers) {
            if (user.isAuthenticated() && user.getAccount().getAccountID().equals(accountID)) {
                return new SimpleEntry<>(user, true);
            }
        }
        for (User user : foreignUsers) {
            if (user.isAuthenticated() && user.getAccount().getAccountID().equals(accountID)) {
                return new SimpleEntry<>(user, false);
            }
        }
        return null;
    }

    public void removeUser(User user) {
        allUsers.remove(user);
    }

    public List<User> getAllUsers() {
        return allUsers;
    }
}
