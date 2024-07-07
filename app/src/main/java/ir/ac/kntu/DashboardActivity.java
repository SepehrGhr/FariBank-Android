package ir.ac.kntu;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private TextView userBalance;
    private RecyclerView receiptsRecyclerView;
    private ReceiptsAdapter receiptsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userBalance = findViewById(R.id.userBalance);
        receiptsRecyclerView = findViewById(R.id.receiptsRecyclerView);
        MaterialButton chargeAccountButton = findViewById(R.id.chargeAccountButton);
        MaterialButton manageFinancesButton = findViewById(R.id.manageFinancesButton);
        MaterialButton boxButton = findViewById(R.id.boxButton);

        // Assuming Main.getUsers().getCurrentUser() gives us the current user
        User currentUser = Main.getUsers().getCurrentUser();
        if (currentUser != null) {
            userBalance.setText(String.format("$%s", currentUser.getAccount().getBalance()));
            setupReceiptsRecyclerView((ArrayList<Receipt>) currentUser.getReceipts());
        }

        // Set up button listeners
        chargeAccountButton.setOnClickListener(v -> {
            // Handle account charge
        });

        manageFinancesButton.setOnClickListener(v -> {
            // Handle finance management
        });

        boxButton.setOnClickListener(v -> {
            // Handle box actions
        });
    }

    private void setupReceiptsRecyclerView(ArrayList<Receipt> receipts) {
        receiptsAdapter = new ReceiptsAdapter(receipts);
        receiptsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        receiptsRecyclerView.setAdapter(receiptsAdapter);
    }
}
