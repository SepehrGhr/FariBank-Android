package ir.ac.kntu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ManageFundActivity extends AppCompatActivity {

    private TextView fundTypeTextView;
    private TextView fundBalanceTextView;
    private EditText amountEditText;
    private Button depositButton;
    private Button withdrawButton;

    private Fund selectedFund;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_fund);

        fundTypeTextView = findViewById(R.id.fundTypeTextView);
        fundBalanceTextView = findViewById(R.id.fundBalanceTextView);
        amountEditText = findViewById(R.id.amountEditText);
        depositButton = findViewById(R.id.depositButton);
        withdrawButton = findViewById(R.id.withdrawButton);

        selectedFund = (Fund) getIntent().getSerializableExtra("fund");
        if (selectedFund != null) {
            fundTypeTextView.setText(selectedFund.getClass().getSimpleName());
            fundBalanceTextView.setText(String.valueOf(selectedFund.getBalance()));
        }

        for(Fund fund : Main.getUsers().getCurrentUser().getFunds()){
            long o = fund.getId();
            long b = selectedFund.getId();
            if(fund.getId() == selectedFund.getId()){
                selectedFund = fund;
            }
        }

        depositButton.setOnClickListener(v -> {
            String amountStr = amountEditText.getText().toString();
            if (!amountStr.isEmpty()) {
                long amount = Long.parseLong(amountStr);
                selectedFund.deposit(amount);
                Toast.makeText(ManageFundActivity.this, "deposit made successfully", Toast.LENGTH_SHORT).show();
                fundBalanceTextView.setText(String.valueOf(selectedFund.getBalance()));
                updateBalance();
            } else {
                Toast.makeText(ManageFundActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            }
        });

        withdrawButton.setOnClickListener(v -> {
            String amountStr = amountEditText.getText().toString();
            if (!amountStr.isEmpty()) {
                long amount = Long.parseLong(amountStr);
                selectedFund.withdraw(amount);
                fundBalanceTextView.setText(String.valueOf(selectedFund.getBalance()));
                updateBalance();
            } else {
                Toast.makeText(ManageFundActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBalance() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedFund", selectedFund);
        setResult(RESULT_OK, resultIntent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        for(Fund fund : Main.getUsers().getCurrentUser().getFunds()){
            if(fund.getId() == selectedFund.getId()){
                selectedFund = fund;
            }
        }
    }

    @Override
    public void onBackPressed() {
        updateBalance();
        super.onBackPressed();
    }
}
