package ir.ac.kntu;

import java.io.Serializable;
import java.util.Random;

public class CreditCard implements Serializable {
    private String password;
    private String cardNumber;

    public CreditCard() {
        this.password = generatePassword();
        this.cardNumber = generateCardNumber();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    private String generateCardNumber() {
        String number = "5892";
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            number += random.nextInt(10);
        }
        return number;
    }

    private String generatePassword() {
        String pass = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            pass += random.nextInt(10);
        }
        return pass;
    }
}
