package ir.ac.kntu;

public class SimcardReceipt extends Receipt {
    private String phoneNumber;

    public SimcardReceipt(long amount, String phoneNumber) {
        super(amount);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return super.toString() + "Phone Number : " + phoneNumber + '\n';
    }
}

