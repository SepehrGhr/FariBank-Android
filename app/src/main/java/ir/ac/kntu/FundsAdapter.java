package ir.ac.kntu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FundsAdapter extends RecyclerView.Adapter<FundsAdapter.FundViewHolder> {

    private ArrayList<Fund> funds;
    private OnFundClickListener onFundClickListener;

    public FundsAdapter(ArrayList<Fund> funds, OnFundClickListener onFundClickListener) {
        this.funds = funds;
        this.onFundClickListener = onFundClickListener;
    }

    @NonNull
    @Override
    public FundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fund, parent, false);
        return new FundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FundViewHolder holder, int position) {
        Fund fund = funds.get(position);
        holder.bind(fund);
    }

    @Override
    public int getItemCount() {
        return funds.size();
    }

    public void updateFunds(ArrayList<Fund> funds) {
        this.funds = funds;
        notifyDataSetChanged();
    }

    class FundViewHolder extends RecyclerView.ViewHolder {

        private TextView fundTypeTextView;
        private TextView fundBalanceTextView;

        public FundViewHolder(@NonNull View itemView) {
            super(itemView);
            fundTypeTextView = itemView.findViewById(R.id.fundTypeTextView);
            fundBalanceTextView = itemView.findViewById(R.id.fundBalanceTextView);

            itemView.setOnClickListener(v -> {
                if (onFundClickListener != null) {
                    onFundClickListener.onFundClick(funds.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Fund fund) {
            fundTypeTextView.setText(fund.getClass().getSimpleName());
            fundBalanceTextView.setText(String.valueOf(fund.getBalance()));
        }
    }

    public interface OnFundClickListener {
        void onFundClick(Fund fund);
    }
}
