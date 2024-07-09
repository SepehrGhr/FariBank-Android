package ir.ac.kntu;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FundsActivity extends AppCompatActivity {

    private RecyclerView fundsRecyclerView;
    private FundsAdapter fundsAdapter;
    private ArrayList<Fund> funds;

    private final ActivityResultLauncher<Intent> manageFundLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Fund updatedFund = (Fund) result.getData().getSerializableExtra("updatedFund");
                    if (updatedFund != null) {
                        updateFundInList(updatedFund);
                    }
                }
            }
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funds);

        fundsRecyclerView = findViewById(R.id.fundsRecyclerView);
        FloatingActionButton addFundButton = findViewById(R.id.addFundButton);

        fundsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        User currentUser = Main.getUsers().getCurrentUser();
        if (currentUser != null) {
            funds = (ArrayList<Fund>) currentUser.getFunds();
            fundsAdapter = new FundsAdapter(funds, fund -> {
                Intent intent = new Intent(FundsActivity.this, ManageFundActivity.class);
                intent.putExtra("fund", fund);
                manageFundLauncher.launch(intent);
            });
            fundsRecyclerView.setAdapter(fundsAdapter);
        }

        addFundButton.setOnClickListener(v -> showAddFundDialog());
    }

    private void updateFundInList(Fund updatedFund) {
        for (int i = 0; i < funds.size(); i++) {
            if (funds.get(i).getId() == updatedFund.getId()) {
                funds.set(i, updatedFund);
                fundsAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    private void showAddFundDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_fund);

        Button savingFundButton = dialog.findViewById(R.id.savingFundButton);
        Button interestFundButton = dialog.findViewById(R.id.interestFundButton);
        Button remainderFundButton = dialog.findViewById(R.id.remainderFundButton);

        User currentUser = Main.getUsers().getCurrentUser();

        savingFundButton.setOnClickListener(v -> {
            currentUser.addFund(new SavingFund(currentUser));
            fundsAdapter.notifyItemInserted(funds.size() - 1);
            dialog.dismiss();
        });

        interestFundButton.setOnClickListener(v -> {
            createNewInterestFund();
            dialog.dismiss();
        });

        remainderFundButton.setOnClickListener(v -> {
            if (currentUser.isHasRemainderFund()) {
                Toast.makeText(this, "You already have a remainder fund", Toast.LENGTH_SHORT).show();
            } else {
                currentUser.addFund(new RemainderFund(currentUser));
                currentUser.setHasRemainderFund(true);
                fundsAdapter.notifyItemInserted(funds.size() - 1);
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void createNewInterestFund() {
        // Show dialog to get the month count and initial amount
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_create_interest_fund);

        EditText monthCountEditText = dialog.findViewById(R.id.monthCountEditText);
        EditText amountEditText = dialog.findViewById(R.id.amountEditText);

        dialog.findViewById(R.id.createButton).setOnClickListener(v -> {
            String monthCountStr = monthCountEditText.getText().toString();
            String amountStr = amountEditText.getText().toString();

            if (monthCountStr.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Please enter both month count and amount", Toast.LENGTH_SHORT).show();
                return;
            }

            int monthCount = Integer.parseInt(monthCountStr);
            long amount = Long.parseLong(amountStr);
            User currentUser = Main.getUsers().getCurrentUser();

            if (amount > currentUser.getAccount().getBalance()) {
                Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                return;
            }

            currentUser.getAccount().setBalance(currentUser.getAccount().getBalance() - amount);
            InterestFund newFund = new InterestFund(currentUser, monthCount, amount);
            currentUser.addFund(newFund);
            Main.getManagerData().addNewInterestFund(newFund);
            fundsAdapter.notifyDataSetChanged();
            dialog.dismiss();
            Toast.makeText(this, "Interest fund created successfully", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }
}
