package ir.ac.kntu;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LoanRequestActivity extends AppCompatActivity {
    private LoanRequestAdapter loanRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_request);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_loan_requests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loanRequestAdapter = new LoanRequestAdapter(Main.getUsers().getCurrentUser().getLoanRequests());
        recyclerView.setAdapter(loanRequestAdapter);

        Button addLoanRequestButton = findViewById(R.id.btn_add_loan_request);
        addLoanRequestButton.setOnClickListener(v -> {
            showAddLoanRequestDialog();
        });
    }

    private void showAddLoanRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Loan Request");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_loan_request, null);
        builder.setView(dialogView);

        final EditText etAmount = dialogView.findViewById(R.id.et_amount);
        final EditText etDuration = dialogView.findViewById(R.id.et_duration);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String amountText = etAmount.getText().toString();
                String durationText = etDuration.getText().toString();
                if (!amountText.isEmpty() && !durationText.isEmpty()) {
                    long amount = Long.parseLong(amountText);
                    int duration = Integer.parseInt(durationText);

                    LoanRequest newRequest = new LoanRequest(amount, Main.getUsers().getCurrentUser(), duration);
                    Main.getUsers().getCurrentUser().addLoanRequest(newRequest);
                    Main.getManagerData().addLoanReq(newRequest);

                    loanRequestAdapter.notifyDataSetChanged();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

