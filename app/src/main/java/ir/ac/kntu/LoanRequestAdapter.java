package ir.ac.kntu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LoanRequestAdapter extends RecyclerView.Adapter<LoanRequestAdapter.ViewHolder> {
    private final List<LoanRequest> loanRequests;

    public LoanRequestAdapter(List<LoanRequest> loanRequests) {
        this.loanRequests = loanRequests;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loan_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LoanRequest loanRequest = loanRequests.get(position);
        holder.amount.setText(String.valueOf(loanRequest.getAmount()));
        holder.months.setText(String.valueOf(loanRequest.getMonths()));
        holder.status.setText(loanRequest.getStatus().toString());
    }

    @Override
    public int getItemCount() {
        return loanRequests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView amount, months, status;

        public ViewHolder(View view) {
            super(view);
            amount = view.findViewById(R.id.loan_request_amount);
            months = view.findViewById(R.id.loan_request_months);
            status = view.findViewById(R.id.loan_request_status);
        }
    }
}

