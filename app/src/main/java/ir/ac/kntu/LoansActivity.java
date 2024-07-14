package ir.ac.kntu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoansActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans);

        Button loanRequestsButton = findViewById(R.id.btn_loan_requests);
        loanRequestsButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoansActivity.this, LoanRequestActivity.class);
            startActivity(intent);
        });

        Button loansButton = findViewById(R.id.btn_loans);
        loansButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoansActivity.this, UserLoansActivity.class);
            startActivity(intent);
        });
    }
}
