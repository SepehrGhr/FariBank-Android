package ir.ac.kntu;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private TextView userBalance;
    private TextView cardDetails;
    private RecyclerView receiptsRecyclerView;
    private ReceiptsAdapter receiptsAdapter;

    private final ActivityResultLauncher<Intent> fundsActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    updateReceiptsAndBalance();
                }
            }
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userBalance = findViewById(R.id.userBalance);
        cardDetails = findViewById(R.id.cardDetails);
        receiptsRecyclerView = findViewById(R.id.receiptsRecyclerView);

        MaterialButton addMoneyButton = findViewById(R.id.addMoneyButton);
        MaterialButton contactsButton = findViewById(R.id.contactsButton);
        MaterialButton supportButton = findViewById(R.id.supportButton);
        MaterialButton logoutButton = findViewById(R.id.logoutButton);
        MaterialButton transferButton = findViewById(R.id.transferButton);
        MaterialButton fundsButton = findViewById(R.id.fundsButton);
        MaterialButton loansButton = findViewById(R.id.loansButton);

        User currentUser = Main.getUsers().getCurrentUser();
        if (currentUser != null) {
            userBalance.setText(String.format("$%s", currentUser.getAccount().getBalance()));
            cardDetails.setText(currentUser.getAccount().getCreditCard().getCardNumber());
            setupReceiptsRecyclerView((ArrayList<Receipt>) currentUser.getReceipts());
        }

        addMoneyButton.setOnClickListener(v -> showChargeAccountDialog(currentUser));

        contactsButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ContactsActivity.class);
            startActivity(intent);
        });

        supportButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SupportsActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            Main.getUsers().setCurrentUser(null);
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        transferButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, TransferActivity.class);
            startActivity(intent);
            updateReceiptsAndBalance();
        });

        fundsButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, FundsActivity.class);
            fundsActivityLauncher.launch(intent);
            updateReceiptsAndBalance();
        });

        loansButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LoansActivity.class);
            startActivity(intent);
        });
    }

    private void setupReceiptsRecyclerView(ArrayList<Receipt> receipts) {
        receiptsAdapter = new ReceiptsAdapter(receipts);
        receiptsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        receiptsRecyclerView.setAdapter(receiptsAdapter);
    }

    private void showChargeAccountDialog(User currentUser) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_charge_account);

        EditText chargeAmountEditText = dialog.findViewById(R.id.chargeAmountEditText);
        MaterialButton chargeButton = dialog.findViewById(R.id.chargeButton);

        chargeButton.setOnClickListener(v -> {
            String amountStr = chargeAmountEditText.getText().toString();
            if (!amountStr.isEmpty()) {
                long amount = Long.parseLong(amountStr);
                currentUser.getAccount().chargeAccount(amount);
                userBalance.setText(String.format("$%s", currentUser.getAccount().getBalance()));
                setupReceiptsRecyclerView((ArrayList<Receipt>) currentUser.getReceipts());
                dialog.dismiss();
                Toast.makeText(DashboardActivity.this, "Account charged successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DashboardActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    protected void onResume(){
        super.onResume();
        updateReceiptsAndBalance();
    }

    public void updateReceiptsAndBalance() {
        User currentUser = Main.getUsers().getCurrentUser();
        if (currentUser != null) {
            userBalance.setText(String.format("$%s", currentUser.getAccount().getBalance()));
            setupReceiptsRecyclerView((ArrayList<Receipt>) currentUser.getReceipts());
        }
    }
}
