package ir.ac.kntu;

import android.os.Build;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Serializable {
    private String name;
    private String lastName;
    private PhoneNumber phoneNumber;
    private String securityNumber;
    private String password;
    private List<Contact> contacts;
    private List<User> recentUsers;
    private List<Ticket> tickets;
    private List<Receipt> receipts;
    private List<Fund> funds;
    private Account account;
    private RemainderFund remainderFund;
    private boolean authenticated = false;
    private boolean contactsActivated = true;
    private boolean hasRemainderFund = false;
    private boolean blocked = false;

    public User(String name, String lastName, PhoneNumber phoneNumber, String securityNumber, String password) {
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.securityNumber = securityNumber;
        this.password = password;
        this.contacts = new ArrayList<>();
        this.recentUsers = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.receipts = new ArrayList<>();
        this.funds = new ArrayList<>();
    }

    public String getPhoneNumber() {
        return phoneNumber.getNumber();
    }

    public PhoneNumber getSimCard() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public List<Fund> getFunds() {
        return funds;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Account getAccount() {
        return account;
    }

    public List<Contact> getContacts(){
        return contacts;
    }

    public List<Receipt> getReceipts(){
        return receipts;
    }

    public void setAccount() {
        this.account = new Account();
    }

    public RemainderFund getRemainderFund() {
        return remainderFund;
    }

    public void setRemainderFund(RemainderFund remainderFund) {
        this.remainderFund = remainderFund;
    }

    public boolean isHasRemainderFund() {
        return hasRemainderFund;
    }

    public void setHasRemainderFund(boolean hasRemainderFund) {
        this.hasRemainderFund = hasRemainderFund;
    }

    public String getName() {
        return name;
    }

    public void setContactsActivated(boolean contactsActivated) {
        this.contactsActivated = contactsActivated;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSecurityNumber() {
        return securityNumber;
    }

    public boolean isContactsActivated() {
        return contactsActivated;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<User> getRecentUsers() {
        return recentUsers;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSecurityNumber(String securityNumber) {
        this.securityNumber = securityNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addReceipt(Receipt newReceipt) {
        receipts.add(newReceipt);
    }

    public void addFund(Fund newFund) {
        funds.add(newFund);
    }

    public void addNewContact(Contact newContact) {
        contacts.add(newContact);
    }

    public boolean contactAlreadyExists(String phoneNumber) {
        for (Contact contact : contacts) {
            if (contact.getPhoneNumber().equals(phoneNumber)) {
                return true;
            }
        }
        return false;
    }

    public void addToRecentUsers(User destination) {
        if (!recentUsers.contains(destination)) {
            recentUsers.add(destination);
        }
    }

    public Contact selectContactFromList() {
        if (contacts.size() == 0) {
            System.out.println(Color.RED + "You don't have any contacts yet!" + Color.RESET);
            return null;
        }
        return Display.pageShow(contacts, contact -> System.out.println(Color.BLUE + contact.getName() + " " + contact.getLastName()));
    }

    public boolean haveInContacts(User selected) {
        for (Contact contact : contacts) {
            if (contact.getUser().getPhoneNumber().equals(selected.getPhoneNumber())) {
                return true;
            }
        }
        return false;
    }

    public User selectRecentUserFromList() {
        if (recentUsers.size() == 0) {
            System.out.println(Color.RED + "You haven't transferred money to anyone yet" + Color.RESET);
            return null;
        }
        return Display.pageShow(recentUsers, user -> System.out.println(Color.BLUE + user.getName() + " " +
                user.getLastName() + Color.RESET));
    }

    private String getMenuSelection(int size) {
        String selection = InputManager.getInput();
        if ("-1".equals(selection)) {
            return selection;
        }
        while (!InputManager.isInputValid(selection, size)) {
            System.out.println(Color.RED + "Please enter a valid number or enter -1" + Color.RESET);
            selection = InputManager.getInput();
            if ("-1".equals(selection)) {
                return selection;
            }
        }
        return selection;
    }

    public void changePassword() {
        System.out.println(Color.WHITE + "Please enter your new password" + Color.RESET);
        password = Menu.setPassword();
        System.out.println(Color.GREEN + "Your password has been successfully changed" + Color.RESET);
    }

    public void changeCreditPassword() {
        System.out.println(Color.WHITE + "Please enter your new password" + Color.RESET);
        account.getCreditCard().setPassword(getNewCreditPass());
        System.out.println(Color.GREEN + "Your credit card password has been successfully changed" + Color.RESET);
    }

    private String getNewCreditPass() {
        String password = InputManager.getInput();
        while (!creditPassValidity(password)) {
            System.out.println(Color.RED + "Please enter a valid credit card password . it must contain 4 digits");
            password = InputManager.getInput();
        }
        return password;
    }

    private boolean creditPassValidity(String password) {
        String regex = "\\d{4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public void changeContactStatus() {
        setContactsActivated(!isContactsActivated());
        System.out.println(Color.GREEN + "Your contacts option has been successfully changed" + Color.RESET);
    }

    public void addNewTicket(Ticket newTicket) {
        tickets.add(newTicket);
    }

    public void displayTickets() {
        if (tickets.size() == 0) {
            System.out.println(Color.RED + "You don't have any tickets yet!" + Color.RESET);
            return;
        }
        Ticket selected = Display.pageShow(tickets, ticket -> System.out.println(Color.BLUE + ticket.getType().toString() + Color.RESET));
        if (selected != null) {
            System.out.println(selected);
        }
    }

    @Override
    public String toString() {
        return Color.CYAN + "*".repeat(35) + '\n' + Color.WHITE + "FullName : " + Color.BLUE +
                name + " " + lastName + '\n' + Color.WHITE + "Phone number : " + Color.BLUE +
                getPhoneNumber() + '\n' + Color.WHITE + "Account ID : " + Color.BLUE + account.getAccountID() +
                '\n' + Color.CYAN + "*".repeat(35) + Color.RESET;
    }

    public String fullInfo() {
        return Color.CYAN + "*".repeat(35) + '\n' + Color.WHITE + "FullName : " + Color.BLUE +
                name + " " + lastName + '\n' + Color.WHITE + "Phone number : " + Color.BLUE +
                getPhoneNumber() + '\n' + Color.WHITE + "Password: " + Color.BLUE + password + '\n' +
                Color.WHITE + "Security Number: " + Color.BLUE + securityNumber + '\n' + Color.WHITE +
                "Account ID : " + Color.BLUE + account.getAccountID() +
                '\n' + Color.CYAN + "*".repeat(35) + Color.RESET;
    }

    public void displayReceipts() {
        if (receipts.size() == 0) {
            System.out.println(Color.RED + "There is no receipts for you yet!" + Color.RESET);
            return;
        }
        Collections.reverse(receipts);
        Receipt selected = Display.pageShow(receipts, Receipt::printSimpleReceipt);
        Collections.reverse(receipts);
        if (selected == null) {
            return;
        }
        System.out.println(selected);
    }

    public void filterReceipt() {
        System.out.println(Color.WHITE + "Please enter the start date for your filter in YYYY-MM-DD format" + Color.RESET);
        Instant start = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            start = Receipt.getDateInput();
        }
        System.out.println(Color.WHITE + "Please enter the end date for your filter in YYYY-MM-DD format" + Color.RESET);
        Instant end = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            end = Receipt.getDateInput();
        }
        List<Receipt> matchedReceipts = new ArrayList<>();
        for (Receipt receipt : receipts) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (receipt.getTime().isAfter(start) && end.isAfter(receipt.getTime())) {
                    matchedReceipts.add(receipt);
                }
            }
        }
        Receipt selected = Display.pageShow(matchedReceipts, Receipt::printSimpleReceipt);
        if (selected != null) {
            System.out.println(selected);
        }
    }

    public void generateReport() {
        String chartFilePath = Main.getUsers().getCurrentUser().getName() + "_" +
                Main.getUsers().getCurrentUser().getLastName() + "_balance_chart.jpg";
        String htmlFilePath = Main.getUsers().getCurrentUser().getName() + "_" +
                Main.getUsers().getCurrentUser().getLastName() + "_account_report.html";
//        ChartHelper chartHelper = new ChartHelper();
//        chartHelper.generateBalanceChart(chartFilePath);
//        chartHelper.generateHtmlReport(htmlFilePath, chartFilePath, Main.getUsers().getCurrentUser());
    }

//    public void addReceiptsToHTML(StringBuilder htmlContent, DateTimeFormatter formatter) {
//        for (Receipt receipt : receipts) {
//            htmlContent.append("<tr>")
//                    .append("<td>").append(formatter.format(receipt.getTime())).append("</td>")
//                    .append("<td>").append(receipt.getAmount()).append("</td>")
//                    .append("</tr>");
//        }
//    }

//    public void addReceiptsToChart(DefaultCategoryDataset dataset, DateTimeFormatter formatter) {
//        for (Receipt receipt : receipts) {
//            dataset.addValue(receipt.getAmount(), "Balance", receipt.getTime().atZone(ZoneId.systemDefault()).format(formatter));
//        }
//    }

    public void showAndSelectFunds() {
        if (funds.size() == 0) {
            System.out.println(Color.RED + "You don't have any funds yet!" + Color.RESET);
            return;
        }
        int count = 1;
        System.out.println(Color.CYAN + "*".repeat(35) + Color.RESET);
        for (Fund fund : funds) {
            System.out.println(Color.WHITE + count + "-" + fund.getType() + Color.RESET);
            count++;
        }
        System.out.println(Color.CYAN + "*".repeat(35) + Color.RESET);
        Fund selected = selectFundFromList();
        if (selected == null) {
            return;
        }
        selected.openManagement();
    }

    public Fund selectFundFromList() {
        System.out.println(Color.WHITE + "Enter the number of the fund you want to select or enter -1 to return to last menu" + Color.RESET);
        String selection = getMenuSelection(funds.size());
        if ("-1".equals(selection)) {
            return null;
        }
        return funds.get(Integer.parseInt(selection) - 1);
    }

    public void removeFund(Fund fund) {
        funds.remove(fund);
    }

    public Contact findContact(String phoneNumber) {
        for (Contact contact : contacts) {
            if (contact.getPhoneNumber().equals(phoneNumber)) {
                return contact;
            }
        }
        return null;
    }

    public void updateContact(String oldPhoneNumber, Contact newContact) {
        Contact oldContact = findContact(oldPhoneNumber);
        if (oldContact != null) {
            oldContact.setName(newContact.getName());
            oldContact.setLastName(newContact.getLastName());
            oldContact.setPhoneNumber(newContact.getPhoneNumber());
            oldContact.setUser(Main.getUsers().findUserByPhoneNumber(newContact.getPhoneNumber()));
        }
    }
}
