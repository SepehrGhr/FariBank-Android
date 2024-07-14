package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

enum Filter {NAME, LASTNAME, USERNAME, PHONE_NUMBER, RULE}

public class ManagerData {
    private final List<Manager> managers;
    private final List<InterestFund> interestFunds;
    private final List<Loan> allLoans;
    private final List<Paya> pendingPaya;
    private Manager currentManager;
    private final FeeRate feeRate;
    private double interestRate;
    private boolean processStarted;

    public ManagerData() {
        this.feeRate = new FeeRate();
        this.interestRate = 0.27;
        this.managers = new ArrayList<>();
        this.interestFunds = new ArrayList<>();
        this.pendingPaya = new ArrayList<>();
        this.allLoans = new ArrayList<>();
        this.processStarted = false;
    }

    public Manager getCurrentManager() {
        return currentManager;
    }

    public FeeRate getFeeRate() {
        return feeRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void addLoan(Loan loan) {
        allLoans.add(loan);
        if (allLoans.size() == 1){
            startLoanProcess();
        }
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void addNewInterestFund(InterestFund newFund) {
        interestFunds.add(newFund);
        if (interestFunds.size() == 1 && !processStarted) {
            startInterestProcess();
            processStarted = true;
        }
    }

    public void addNewPaya(Paya newPaya) {
        pendingPaya.add(newPaya);
        startScheduler(newPaya);
    }

    public void removePaya(Paya newPaya) {
        pendingPaya.remove(newPaya);
    }

    public void startScheduler(Paya newPaya) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            newPaya.doTransfer();
            scheduler.shutdown();
        };
        scheduler.schedule(task, 30, TimeUnit.SECONDS);
    }

    public void loanReqScheduler(LoanRequest loanReq){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            loanReq.checkApproval();
            scheduler.shutdown();
        };
        scheduler.schedule(task, 20, TimeUnit.SECONDS);
    }

    public void addLoanReq(LoanRequest loanRequest){
        loanReqScheduler(loanRequest);
    }

    public void removeInterestFund(InterestFund fund) {
        interestFunds.remove(fund);
    }

    private void startInterestProcess() {
        InterestDepositRunnable interestRunnable = new InterestDepositRunnable();
        Thread interestThread = new Thread(interestRunnable);
        interestThread.start();
    }

    private void startLoanProcess() {
        LoanRunnable loanRunnable = new LoanRunnable();
        Thread loanThread = new Thread(loanRunnable);
        loanThread.start();
    }

    public void setCurrentManager(Manager currentManager) {
        this.currentManager = currentManager;
    }

    public Manager findManagerByUsername(String input) {
        for (Manager manager : managers) {
            if (manager.getName().equals(input)) {
                return manager;
            }
        }
        return null;
    }

    public void managerSetup() {
        managers.add(new Manager("Sepehr", "12345", 1));
    }

    public void depositMonthlyInterest() {
        for (InterestFund fund : interestFunds) {
            if (fund.getReceivedCount() < fund.getMustReceiveCount()) {
                fund.depositInterest();
            }
        }
    }

    public void handleLoansFirstOfMonth() {
        for (Loan loan : allLoans) {
            loan.firstOfMonthOperation();
        }
    }

    public void doAllPayas() {
        if (pendingPaya.size() == 0) {
            System.out.println(Color.RED + "There is no pending Paya transfer" + Color.RESET);
            return;
        }
        for (Paya paya : pendingPaya) {
            paya.doTransfer();
        }
        System.out.println(Color.GREEN + "All pending Paya transfers are done" + Color.RESET);
    }

    public void showListOfEveryone() {
        List<Object> everyone = new ArrayList<>(managers);
        Main.getAdminData().addAllAdmins(everyone);
        Main.getUsers().addAllUsers(everyone);
        displayListAfterFilter(everyone);
    }

    private void chooseEditOrNot(Object selected) {
        System.out.println(Color.WHITE + "Please enter" + Color.BLUE + " 1 " + Color.WHITE + "to edit the selected person or"
                + Color.BLUE + " 2 " + Color.WHITE + "to return" + Color.RESET);
        String selection = InputManager.getSelection(2);
        if ("1".equals(selection)) {
            if (selected instanceof User) {
                currentManager.editUserMenu((User) selected);
            } else if (selected instanceof Admin) {
                currentManager.editAdminMenu((Admin) selected);
            } else if (selected instanceof Manager) {
                currentManager.editManagerMenu((Manager) selected);
            }
        }
    }

    public void showListWithFilter() {
        Filter filter = selectFilter();
        List<Object> matched = new ArrayList<>();
        switch (filter) {
            case NAME -> addAllMatchingNames(matched);

            case LASTNAME -> {
                addMatchedLastnames(matched);
            }
            case USERNAME -> {
                System.out.println(Color.WHITE + "Please enter the username your looking for" + Color.RESET);
                String username = InputManager.getInput();
                matched.addAll(Main.getManagerData().searchByName(username));
                matched.addAll(Main.getAdminData().searchByName(username));
            }
            case RULE -> matched.addAll(filterByRule());
            case PHONE_NUMBER -> {
                System.out.println(Color.WHITE + "Please enter the phone number your looking for" + Color.RESET);
                matched.addAll(Main.getUsers().searchByPhoneNumber(InputManager.getInput()));
            }
            default -> {
            }
        }
        displayListAfterFilter(matched);
    }

    private void addMatchedLastnames(List<Object> matched) {
        System.out.println(Color.WHITE + "Please enter the lastname your looking for" + Color.RESET);
        String lastname = InputManager.getInput();
        matched.addAll(Main.getUsers().searchByLastname(lastname));
        matched.addAll(Main.getManagerData().searchByName(lastname));
        matched.addAll(Main.getAdminData().searchByName(lastname));
    }

    private void addAllMatchingNames(List<Object> matched) {
        System.out.println(Color.WHITE + "Please enter the name your looking for" + Color.RESET);
        String name = InputManager.getInput();
        matched.addAll(Main.getUsers().searchByName(name));
        matched.addAll(Main.getManagerData().searchByName(name));
        matched.addAll(Main.getAdminData().searchByName(name));
    }

    private List<Object> filterByRule() {
        List<Object> matched = new ArrayList<>();
        Menu.printMenu(OptionEnums.Rules.values(), Main.getManagerData().getCurrentManager()::getName);
        String selection = InputManager.getSelection(3);
        switch (selection) {
            case "1" -> Main.getUsers().addUsersToList(matched);
            case "2" -> matched.addAll(managers);
            case "3" -> Main.getAdminData().addAdminsToList(matched);
            default -> {
            }
        }
        return matched;
    }

    private Filter selectFilter() {
        Menu.printMenu(Filter.values(), Main.getManagerData().getCurrentManager()::getName);
        String selection = InputManager.getSelection(5);
        Filter[] options = Filter.values();
        return options[Integer.parseInt(selection) - 1];
    }

    public List<Manager> searchByName(String name) {
        List<Manager> matched = new ArrayList<>();
        for (Manager manager : managers) {
            if (name.equals(manager.getName())) {
                matched.add(manager);
            }
        }
        return matched;
    }

    public void displayListAfterFilter(List<Object> matched) {
        Object selected = Display.pageShow(matched,
                theOne -> {
                    if (theOne instanceof User) {
                        System.out.println(Color.GREEN + ((User) theOne).getName() + " " + ((User) theOne).getLastName() + Color.RESET);
                    } else if (theOne instanceof Admin) {
                        System.out.println(Color.YELLOW + ((Admin) theOne).getName() + Color.RESET);
                    } else if (theOne instanceof Manager) {
                        System.out.println(Color.RED + ((Manager) theOne).getName() + Color.RESET);
                    }
                });
        if (selected == null) {
            return;
        }
        if (selected instanceof User) {
            System.out.println(((User) selected).fullInfo());
        } else if (selected instanceof Admin) {
            System.out.println(selected);
        } else if (selected instanceof Manager) {
            System.out.println(selected);
        }
        chooseEditOrNot(selected);
    }

    public void handleManagerAdd() {
        String selection = InputManager.getSelection(3);
        switch (selection) {
            case "1" -> addNewManager();
            case "2" -> addNewAdmin();
            default -> {
            }
        }
    }

    private void addNewAdmin() {
        System.out.println(Color.WHITE + "Please enter the new admin name" + Color.RESET);
        String name = InputManager.setUserName();
        while (Main.getAdminData().nameAlreadyExists(name)) {
            System.out.println(Color.RED + "There is already an admin with this name.please enter another name" + Color.RESET);
            name = InputManager.setUserName();
        }
        System.out.println(Color.WHITE + "Please enter the new admin password" + Color.RESET);
        String password = Menu.setPassword();
        Admin newAdmin = new Admin(name, name, password);
        Main.getAdminData().addAdmin(newAdmin);
        System.out.println(Color.GREEN + "New admin has been added successfully" + Color.RESET);
    }

    private void addNewManager() {
        System.out.println(Color.WHITE + "Please enter the new manager's name" + Color.RESET);
        String name = InputManager.setUserName();
        while (nameAlreadyExists(name)) {
            System.out.println(Color.RED + "There is already a manager with this name, please enter another name" + Color.RESET);
            name = InputManager.setUserName();
        }
        System.out.println(Color.WHITE + "Please enter the new manager's password" + Color.RESET);
        String password = Menu.setPassword();
        Manager newManager = new Manager(name, password, currentManager.getLevel() + 1);
        managers.add(newManager);
        System.out.println(Color.GREEN + "New manager has been added successfully" + Color.RESET);
    }

    public void addNewManager(Manager newManager) {
        managers.add(newManager);
    }

    private boolean nameAlreadyExists(String name) {
        for (Manager manager : managers) {
            if (manager.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
