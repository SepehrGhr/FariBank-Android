package ir.ac.kntu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ViewHolder> {
    private List<Loan> loanList;
    private Context context;

    public LoanAdapter(Context context, List<Loan> loanList) {
        this.context = context;
        this.loanList = loanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loan_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Loan loan = loanList.get(position);
        holder.amount.setText(String.valueOf(loan.getAmount()));
        holder.paymentCount.setText(String.valueOf(loan.getMonths()));
        holder.itemView.setOnClickListener(v -> {
            showLoanDetailsDialog(loan);
        });
    }

    @Override
    public int getItemCount() {
        return loanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView amount, paymentCount;

        public ViewHolder(View view) {
            super(view);
            amount = view.findViewById(R.id.loan_amount);
            paymentCount = view.findViewById(R.id.loan_payment_count);
        }
    }

    private void showLoanDetailsDialog(Loan loan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Loan Details");

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_loan_details, null);
        builder.setView(dialogView);

        TextView tvAmount = dialogView.findViewById(R.id.tv_amount);
        TextView tvPaymentCount = dialogView.findViewById(R.id.tv_payment_count);
        TextView tvDelayedPayments = dialogView.findViewById(R.id.tv_delayed_payments);
        TextView tvPaymentAmount = dialogView.findViewById(R.id.tv_payment_amount);
        Button btnDoPayment = dialogView.findViewById(R.id.btn_do_payment);

        tvAmount.setText("Amount: " + loan.getAmount());
        tvPaymentCount.setText("Payments Made: " + loan.getPaymentCount());
        tvDelayedPayments.setText("Delayed Payments: " + loan.getDelayedCount());
        tvPaymentAmount.setText("Payment Amount: " + loan.getPaymentAmount());

        btnDoPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Main.getUsers().getCurrentUser().getAccount().getBalance() >= loan.getPaymentAmount()) {
                    loan.doPayment();
                    Main.getUsers().getCurrentUser().getAccount().withdrawMoney(loan.getPaymentAmount(), Main.getUsers().getCurrentUser());
                    Toast.makeText(context, "Payment was successfully done", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Your balance isn't enough", Toast.LENGTH_SHORT).show();
                }
                //dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

