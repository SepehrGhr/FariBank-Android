package ir.ac.kntu;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class LoanRunnable implements Runnable{
    @Override
    public void run() {
        while (true) {
            try {
                Instant now = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    now = Calendar.now();
                }
                ZonedDateTime currentDateTime = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    currentDateTime = now.atZone(ZoneId.systemDefault());
                }
                int dayOfMonth = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    dayOfMonth = currentDateTime.getDayOfMonth();
                }
                if (dayOfMonth == 1) {
                    Main.getManagerData().handleLoansFirstOfMonth();
                }
                Thread.sleep(24 * 60 * 60 * 1000 / 24000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
