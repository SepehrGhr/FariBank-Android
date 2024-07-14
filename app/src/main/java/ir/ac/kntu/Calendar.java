package ir.ac.kntu;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;

@RequiresApi(api = Build.VERSION_CODES.O)
public final class Calendar {
    public static final int TIME_SPEED = 24000;

    private static Instant start = Instant.now();

    private Calendar() {
    }

    public static Instant now() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Instant.ofEpochMilli(start.toEpochMilli() + (Instant.now().toEpochMilli() - start.toEpochMilli()) * TIME_SPEED);
        }
        return null;
    }
}