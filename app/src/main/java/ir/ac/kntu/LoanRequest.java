package ir.ac.kntu;

import java.io.Serializable;

enum LoanStatus {PENDING, REJECTED, ACCEPTED}

public class LoanRequest implements Serializable {
    private long amount;
    private User receiver;
    private int months;
    private LoanStatus status;

    public LoanRequest(long amount, User receiver, int months){
        this.amount = amount;
        this.receiver = receiver;
        this.months = months;
        this.status = LoanStatus.PENDING;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public long getAmount() {
        return amount;
    }

    public User getReceiver() {
        return receiver;
    }

    public int getMonths() {
        return months;
    }

    public void checkApproval(){
        for(Loan loan: receiver.getLoans()){
            if(loan.getDelayedCount() > 2){
                status = LoanStatus.REJECTED;
                return;
            }
        }
        status = LoanStatus.ACCEPTED;
        Loan newLoan = new Loan(amount, months, receiver);
        Main.getUsers().getCurrentUser().addLoan(newLoan);
        Main.getManagerData().addLoan(newLoan);
        Main.getUsers().getCurrentUser().getAccount().setBalance(Main.getUsers().getCurrentUser().getAccount().getBalance() + amount);
    }
}
