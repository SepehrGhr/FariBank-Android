package ir.ac.kntu;

public class FeeRate {
    private long payaFee;
    private double polFee;
    private long fariFee;
    private long cardFee;
    private long simCardFee;

    public FeeRate() {
        this.payaFee = 2000;
        this.polFee = 0.02;
        this.cardFee = 300;
        this.fariFee = 0;
        this.simCardFee = 100;
    }

    public long getPayaFee() {
        return payaFee;
    }

    public void setPayaFee(long payaFee) {
        this.payaFee = payaFee;
    }

    public double getPolFee() {
        return polFee;
    }

    public void setPolFee(double polFee) {
        this.polFee = polFee;
    }

    public long getFariFee() {
        return fariFee;
    }

    public void setFariFee(long fariFee) {
        this.fariFee = fariFee;
    }

    public long getCardFee() {
        return cardFee;
    }

    public void setCardFee(long cardFee) {
        this.cardFee = cardFee;
    }

    public long getSimCardFee() {
        return simCardFee;
    }

    public void setSimCardFee(long simCardFee) {
        this.simCardFee = simCardFee;
    }
}
