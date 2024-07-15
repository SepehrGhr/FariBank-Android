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
    private final List<Contact> contacts;
    private final List<User> recentUsers;
    private final List<Ticket> tickets;
    private final List<Receipt> receipts;
    private final List<Fund> funds;
    private final List<Loan> loans;
    private final List<LoanRequest> loanRequests;
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
        this.loans = new ArrayList<>();
        this.loanRequests = new ArrayList<>();
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

    public List<Loan> getLoans() {
        return loans;
    }

    public List<LoanRequest> getLoanRequests() {
        return loanRequests;
    }

    public void addLoan(Loan loan){
        loans.add(loan);
    }

    public void addLoanRequest(LoanRequest loanReq){
        loanRequests.add(loanReq);
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

    public boolean haveInContacts(User selected) {
        for (Contact contact : contacts) {
            if (contact.getUser().getPhoneNumber().equals(selected.getPhoneNumber())) {
                return true;
            }
        }
        return false;
    }

    public void addNewTicket(Ticket newTicket) {
        tickets.add(newTicket);
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

    public List<Ticket> getTickets() {
        return tickets;
    }
}
