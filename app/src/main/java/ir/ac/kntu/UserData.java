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

    public void transferByAccountID() {
        //System.out.println(Color.WHITE + "Please enter the account ID/card number you want to transfer money to" + Color.RESET);
        String input = getAccIdInput();
        User destination;
        boolean fromFariBank;
        boolean isAccountID;
        if (input.length() == 12) {
            destination = findUserByAccountID(input).getKey();
            fromFariBank = findUserByAccountID(input).getValue();
            isAccountID = true;
        } else {
            destination = findUserByCardNumber(input).getKey();
            fromFariBank = findUserByCardNumber(input).getValue();
            isAccountID = false;
        }
        if (destination == null) {
            //System.out.println(Color.RED + "There is no user with this account ID/card number" + Color.RESET);
        } else if (destination.equals(Main.getUsers().getCurrentUser())) {
            //System.out.println(Color.RED + "You cant transfer money to yourself!!" + Color.RESET);
        } else if (destination.isBlocked()) {
            //System.out.println(Color.RED + "Selected user's account is blocked and you cant transfer money to them" + Color.RESET);
        } else {
            doTransfer(destination, fromFariBank, isAccountID);
        }
    }

    private Entry<User, Boolean> findUserByCardNumber(String cardNumber) {
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

    private String getAccIdInput() {
        String input = InputManager.getInput();
        while (!Main.getUsers().getCurrentUser().getAccount().cardNumberOrIDValidity(input)) {
            //System.out.println(Color.RED + "Please enter your account ID/card number correctly" + Color.RESET);
            //input = InputManager.getInput();
        }
        return input;
    }

    private long transferMoney(User destination, boolean fromFari, boolean isAccountID) {
        System.out.println(Color.WHITE + "Please enter the amount you want to transfer (Maximum : 8 million)");
        String amount = InputManager.getInput();
        while (!InputManager.chargeAmountValidity(amount) || amount.length() > 8 || Long.parseLong(amount) > 8000000) {
            System.out.println(Color.RED + "Please enter a valid amount (Maximum : 10 million)" + Color.RESET);
            amount = InputManager.getInput();
        }
        String method = selectTransferMethod(fromFari, isAccountID);
        long amountAfterFee = handleMethodLimits(Long.parseLong(amount), method, destination);
        if (amountAfterFee == -1) {
            return -1;
        }
        boolean confirmation = printConfirmation(destination, Long.parseLong(amount), amountAfterFee);
        if (!confirmation) {
            return -1;
        }
        if (amountAfterFee > currentUser.getAccount().getBalance()) {
            //System.out.println(Color.RED + "Your balance is not enough. Current Balance : " +
            //        Color.GREEN + currentUser.getAccount().getBalance() + Color.RED + "required balance:"
            //        + Color.GREEN + amountAfterFee + Color.RESET);
            return -1;
        }
        updateBalances(destination, amount, amountAfterFee);
        currentUser.addToRecentUsers(destination);
        return Long.parseLong(amount);
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
                //System.out.println(Color.RED + "The maximum transfer amount for Pol method id 5 Million" + Color.RESET);
                return -1;
            }
            return amount + (long) (amount * Main.getManagerData().getFeeRate().getPolFee());
        }
        if (amount > 100000) {
            //System.out.println(Color.RED + "The maximum transfer amount for non-fari bank cart to card is 100000" + Color.RESET);
            return -1;
        }
        return amount + Main.getManagerData().getFeeRate().getCardFee();
    }

    public void handlePayaMethod(long amount, User destination, boolean confirm) {
        if (amount > 5000000) {
            //System.out.println(Color.RED + "The maximum transfer amount for Paya method is 5 Million" + Color.RESET);
            return;
        } else if (amount + Main.getManagerData().getFeeRate().getPayaFee() > currentUser.getAccount().getBalance()) {
            //System.out.println(Color.RED + "Your balance is not enough. Current Balance : " +
            //        Color.GREEN + currentUser.getAccount().getBalance() + Color.RED + "required balance:"
            //        + Color.GREEN + (amount + Main.getManagerData().getFeeRate().getPayaFee()) + Color.RESET);
            return;
        }
        if (!confirm) {
            confirm = printConfirmation(destination, amount, amount + Main.getManagerData().getFeeRate().getPayaFee());
        }
        if (!confirm) {
            return;
        }
        Paya newPaya = new Paya(destination, Main.getUsers().getCurrentUser(), amount);
        currentUser.getAccount().withdrawMoney(amount + Main.getManagerData().getFeeRate().getPayaFee(), currentUser);
        Main.getManagerData().addNewPaya(newPaya);
        //System.out.println(Color.GREEN + "Your Paya transfer request has been submitted and will be done in less than 48 hours!"
        //        + Color.RESET);
    }

    private String selectTransferMethod(boolean fromFari, boolean isAccountID) {
        if (fromFari) {
            return "FariToFari";
        }
        String selection = "";
        if (isAccountID) {
//            System.out.println(Color.WHITE + "Please select your transfer method" + Color.RESET);
//            System.out.println(Color.WHITE + "1-Paya " + Color.BLUE + "(will take some time)" + Color.RESET);
//            System.out.println(Color.WHITE + "2-Pol" + Color.RESET);
//            selection = InputManager.getSelection(2);
        } else {
//            System.out.println(Color.WHITE + "Please select your transfer method" + Color.RESET);
//            System.out.println(Color.WHITE + "1-Paya " + Color.BLUE + "(will take some time)" + Color.RESET);
//            System.out.println(Color.WHITE + "2-Pol" + Color.RESET);
//            System.out.println(Color.WHITE + "3-Card to card" + Color.RESET);
//            selection = InputManager.getSelection(3);
        }
        String[] options = {"Paya", "Pol", "Card to card"};
        return options[Integer.parseInt(selection) - 1];
    }

    private boolean printConfirmation(User destination, long amount, long amountAfterFee) {
//        System.out.println(Color.YELLOW + "<>".repeat(20) + Color.RESET);
//        System.out.println(Color.WHITE + "Your transfer's details:" + Color.RESET);
//        System.out.println(Color.WHITE + "Amount: " + Color.GREEN + amount + Color.WHITE + " + " + Color.RED + "(" + (amountAfterFee - amount) + ")" + Color.RESET);
//        System.out.println(Color.WHITE + "Destination name : " + Color.BLUE + destination.getName() + " "
//                + destination.getLastName() + Color.RESET);
//        System.out.println(Color.YELLOW + "<>".repeat(20) + Color.RESET);
//        System.out.println(Color.WHITE + "Enter " + Color.GREEN + "1 " + Color.WHITE + "to confirm and " + Color.RED +
//                "2 " + Color.WHITE + "to cancel" + Color.RESET);
        String selection = InputManager.getSelection(2);
        return "1".equals(selection);
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

    public void transferByContact() {
        long amount = 0;
        Contact selected = currentUser.selectContactFromList();
        if (selected == null) {
            return;
        }
        if (selected.getUser().equals(Main.getUsers().getCurrentUser())) {
            //System.out.println(Color.RED + "You cant transfer money to yourself!!" + Color.RESET);
            return;
        } else if (selected.getUser().isBlocked()) {
            //System.out.println(Color.RED + "Selected user's account is blocked and you cant transfer money to them" + Color.RESET);
            return;
        }
        if (selected.getUser().haveInContacts(selected) && selected.getUser().isContactsActivated()) {
            amount = transferMoney(selected.getUser(), true, true);
            if (amount != -1) {
                TransferReceipt newReceipt = new TransferReceipt(amount, Main.getUsers().getCurrentUser(), selected.getUser(), Method.CONTACT);
                Main.getUsers().getCurrentUser().addReceipt(newReceipt);
                TransferReceipt destReceipt = new TransferReceipt(amount, Main.getUsers().getCurrentUser(), selected.getUser(), Method.ACCOUNT);
                selected.getUser().addReceipt(destReceipt);
                //System.out.println(newReceipt);
            }
        } else {
            //System.out.println(Color.RED + "Selected contact does not have you in their contacts or they have deactivated getting money from contacts" + Color.RESET);
        }
    }

    public void transferByRecentUser() {
        User selected = currentUser.selectRecentUserFromList();
        if (selected != null && selected.isBlocked()) {
            //System.out.println(Color.RED + "Selected user's account is blocked and you cant transfer money to them" + Color.RESET);
            return;
        }
        if (selected != null) {
            boolean fromFari = allUsers.contains(selected);
            doTransfer(selected, fromFari, true);
        }
    }

    private void doTransfer(User selected, boolean fromFari, boolean isAccountID) {
        long amount;
        amount = transferMoney(selected, fromFari, isAccountID);
        if (amount != -1) {
            TransferReceipt newReceipt = new TransferReceipt(amount, Main.getUsers().getCurrentUser(), selected, Method.ACCOUNT);
            Main.getUsers().getCurrentUser().addReceipt(newReceipt);
            selected.addReceipt(newReceipt);
            System.out.println(newReceipt);
        }
    }

    public void handleAdminUserInput() {
        String selection = InputManager.getSelection(3);
        switch (selection) {
            case "1" -> {
                User selected = Display.pageShow(allUsers, user -> System.out.println(Color.BLUE + user.getName() + " " +
                        user.getLastName() + Color.RESET));
                if (selected != null) {
                    System.out.println(selected);
                }
                Menu.printMenu(OptionEnums.AdminUserMenu.values(), Main.getUsers()::handleAdminUserInput);
            }
            case "2" -> {
                Menu.printMenu(OptionEnums.SearchMethods.values(), Main.getUsers()::getCurrentUser);
                handleSearchMethod();
            }
            default -> Menu.printMenu(OptionEnums.AdminMenu.values(), InputManager::handleAdminInput);
        }
    }

    private void handleSearchMethod() {
        String selection = InputManager.getSelection(5);
        switch (selection) {
            case "1" -> {
                System.out.println(Color.WHITE + "Please enter the name your searching for" + Color.RESET);
                List<User> matched = searchByName(InputManager.getInput());
                printMatched(matched);
                Menu.printMenu(OptionEnums.AdminUserMenu.values(), Main.getUsers()::handleAdminUserInput);
            }
            case "2" -> {
                System.out.println(Color.WHITE + "Please enter the lastname your searching for" + Color.RESET);
                List<User> matched = searchByLastname(InputManager.getInput());
                printMatched(matched);
                Menu.printMenu(OptionEnums.AdminUserMenu.values(), Main.getUsers()::handleAdminUserInput);
            }
            case "3" -> {
                System.out.println(Color.WHITE + "Please enter the phone number your searching for" + Color.RESET);
                List<User> matched = searchByPhoneNumber(InputManager.getInput());
                printMatched(matched);
                Menu.printMenu(OptionEnums.AdminUserMenu.values(), Main.getUsers()::handleAdminUserInput);
            }
            case "4" -> {
                List<User> matched = combinedSearch();
                printMatched(matched);
                Menu.printMenu(OptionEnums.AdminUserMenu.values(), Main.getUsers()::handleAdminUserInput);
            }
            default -> Menu.printMenu(OptionEnums.AdminMenu.values(), InputManager::handleAdminInput);
        }
    }

    private List<User> combinedSearch() {
        System.out.println(Color.WHITE + "Please enter the name your searching for" + Color.RESET);
        String name = InputManager.getInput();
        System.out.println(Color.WHITE + "Please enter the lastname your searching for" + Color.RESET);
        String lastName = InputManager.getInput();
        System.out.println(Color.WHITE + "Please enter the phone number your searching for" + Color.RESET);
        String phoneNumber = InputManager.getInput();
        String lookingFor = name + lastName + phoneNumber;
        List<User> matched = new ArrayList<>();
        for (User user : allUsers) {
            if (lookingFor.equals(user.getName() + user.getLastName() + user.getPhoneNumber())) {
                matched.add(user);
            }
        }
        if (matched.size() == 0) {
            for (User user : allUsers) {
                if (Display.distance(lookingFor, user.getName() + user.getLastName() + user.getPhoneNumber()) < 7) {
                    matched.add(user);
                }
            }
        }
        return matched;
    }

    private void printMatched(List<User> matched) {
        if (matched.size() == 0) {
            //System.out.println(Color.RED + "No user with this information was found" + Color.RESET);
            return;
        }
        User selected = Display.pageShow(matched, user -> System.out.println(Color.BLUE + user.getName() + " " +
                user.getLastName() + Color.RESET));
        if (selected != null) {
            System.out.println(selected);
        }
    }

    public List<User> searchByPhoneNumber(String phoneNumber) {
        List<User> matched = new ArrayList<>();
        for (User user : allUsers) {
            if (phoneNumber.equals(user.getPhoneNumber())) {
                matched.add(user);
            }
        }
        if (matched.size() == 0) {
            for (User user : allUsers) {
                if (Display.distance(phoneNumber, user.getPhoneNumber()) < 2) {
                    matched.add(user);
                }
            }
        }
        return matched;
    }

    public List<User> searchByLastname(String lastname) {
        List<User> matched = new ArrayList<>();
        for (User user : allUsers) {
            if (lastname.equals(user.getLastName())) {
                matched.add(user);
            }
        }
        if (matched.size() == 0) {
            for (User user : allUsers) {
                if (Display.distance(lastname, user.getLastName()) < 4) {
                    matched.add(user);
                }
            }
        }
        return matched;
    }

    public List<User> searchByName(String name) {
        List<User> matched = new ArrayList<>();
        for (User user : allUsers) {
            if (name.equals(user.getName())) {
                matched.add(user);
            }
        }
        if (matched.size() == 0) {
            for (User user : allUsers) {
                if (Display.distance(name, user.getName()) < 4) {
                    matched.add(user);
                }
            }
        }
        return matched;
    }

    public void addNewUserToDatabase(User newUser) {
        AuthenticationRequest newRequest = new AuthenticationRequest(newUser);
        Ticket newTicket = new Ticket("", Type.AUTHENTICATION, newUser);
        Main.getAdminData().addNewTicket(newTicket);
        Main.getAdminData().addAuthenticationRequest(newRequest);
        Main.getUsers().addUser(newUser);
    }

    public void removeUser(User user) {
        allUsers.remove(user);
    }

    public void chargeSimByNumber() {
        //System.out.println(Color.WHITE + "Please enter the phone number you want to charge" + Color.RESET);
        String phoneNumber = InputManager.getInput();
        while (!Menu.checkPhoneNumberValidity(phoneNumber)) {
            //System.out.println(Color.RED + "Please enter your phone number correctly" + Color.RESET);
            phoneNumber = InputManager.getInput();
        }
        for (User user : allUsers) {
            if (user.getPhoneNumber().equals(phoneNumber)) {
                user.getSimCard().printChargeSimCard();
                return;
            }
        }
        handleUnregisteredNumber(phoneNumber);
    }

    private void handleUnregisteredNumber(String phoneNumber) {
        for (PhoneNumber number : unregistereds) {
            if (number.getNumber().equals(phoneNumber)) {
                number.printChargeSimCard();
                return;
            }
        }
        PhoneNumber newNumber = new PhoneNumber(phoneNumber, 0);
        unregistereds.add(newNumber);
        newNumber.printChargeSimCard();
    }

    public void addAllUsers(List<Object> everyone) {
        everyone.addAll(allUsers);
    }

    public void addUsersToList(List<Object> matched) {
        matched.addAll(allUsers);
    }
}
