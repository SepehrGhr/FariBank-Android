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
