package ir.ac.kntu;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.Instant;
import java.time.format.DateTimeParseException;

public class Receipt implements Serializable {
    private Instant time;
    private long amount;

    public Receipt(long amount) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.time = Calendar.now();
        }
        this.amount = amount;
    }

    public Instant getTime() {
        return time;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Date : " +  timeToString(time) +
                '\n' + "Amount : " + amount + '\n' ;
    }

    public String timeToString(Instant time) {
        return time.toString().substring(0, 10) + " " + time.toString().substring(11, 19);
    }

    public void printSimpleReceipt() {
        if (this instanceof ChargeReceipt) {
            System.out.println(Color.GREEN + this.timeToString(this.getTime()) + Color.RESET);
        } else if(this instanceof TransferReceipt) {
            System.out.println(Color.YELLOW + this.timeToString(this.getTime()) + Color.RESET);
        } else {
            System.out.println(Color.BLUE + this.timeToString(this.getTime()) + Color.RESET);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Instant getDateInput() {
        Instant instant = null;
        while (instant == null) {
            String input = InputManager.getInput();
            input += "T00:00:00.000Z";
            try {
                instant = Instant.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println(Color.RED + "Please enter a valid date. example: 2024-05-14" + Color.RESET);
            }
        }
        return instant;
    }
}
