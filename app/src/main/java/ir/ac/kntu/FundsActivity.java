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

        if (Main.getUsers().getCurrentUser() != null) {
            funds = (ArrayList<Fund>) Main.getUsers().getCurrentUser().getFunds();
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



        savingFundButton.setOnClickListener(v -> {
            Main.getUsers().getCurrentUser().addFund(new SavingFund(Main.getUsers().getCurrentUser()));
            fundsAdapter.notifyItemInserted(funds.size() - 1);
            dialog.dismiss();
        });

        interestFundButton.setOnClickListener(v -> {
            createNewInterestFund();
            dialog.dismiss();
        });

        remainderFundButton.setOnClickListener(v -> {
            if (Main.getUsers().getCurrentUser().isHasRemainderFund()) {
                Toast.makeText(this, "You already have a remainder fund", Toast.LENGTH_SHORT).show();
            } else {
                Main.getUsers().getCurrentUser().addFund(new RemainderFund(Main.getUsers().getCurrentUser()));
                Main.getUsers().getCurrentUser().setHasRemainderFund(true);
                fundsAdapter.notifyItemInserted(funds.size() - 1);
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void createNewInterestFund() {
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

            if (amount > Main.getUsers().getCurrentUser().getAccount().getBalance()) {
                Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                return;
            }

            Main.getUsers().getCurrentUser().getAccount().setBalance(Main.getUsers().getCurrentUser().getAccount().getBalance() - amount);
            InterestFund newFund = new InterestFund(Main.getUsers().getCurrentUser(), monthCount, amount);
            Main.getUsers().getCurrentUser().addFund(newFund);
            Main.getManagerData().addNewInterestFund(newFund);
            fundsAdapter.notifyDataSetChanged();
            dialog.dismiss();
            Toast.makeText(this, "Interest fund created successfully", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }
}
