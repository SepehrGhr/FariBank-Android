package ir.ac.kntu;

public class Paya {
    private boolean done;
    private long amount;
    private User destination;
    private User transmitter;

    public Paya(User destination, User transmitter, long amount){
        this.destination = destination;
        this.transmitter = transmitter;
        this.amount = amount;
        this.done = false;
    }

    public boolean isDone() {
        return done;
    }

    public void doTransfer(){
        if(this.done){
            return;
        }
        destination.getAccount().setBalance(destination.getAccount().getBalance() + amount);
        transmitter.addToRecentUsers(destination);
        TransferReceipt newReceipt = new TransferReceipt(amount, transmitter, destination, Method.ACCOUNT);
        transmitter.addReceipt(newReceipt);
        destination.addReceipt(newReceipt);
        this.done = true;
        Main.getManagerData().removePaya(this);
    }
}
