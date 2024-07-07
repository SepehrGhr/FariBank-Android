package ir.ac.kntu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ReceiptViewHolder> {

    private final ArrayList<Receipt> receipts;

    public ReceiptsAdapter(ArrayList<Receipt> receipts) {
        this.receipts = receipts;
    }

    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receipt, parent, false);
        return new ReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {
        Receipt receipt = receipts.get(position);
        holder.bind(receipt);
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }

    class ReceiptViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView receiptType;
        private final TextView receiptTime;
        private final TextView receiptAmount;
        private Receipt receipt;

        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);
            receiptType = itemView.findViewById(R.id.receiptType);
            receiptTime = itemView.findViewById(R.id.receiptTime);
            receiptAmount = itemView.findViewById(R.id.receiptAmount);
            itemView.setOnClickListener(this);
        }

        public void bind(Receipt receipt) {
            this.receipt = receipt;

            if (receipt instanceof ChargeReceipt) {
                receiptType.setText("Charge");
            } else if (receipt instanceof TransferReceipt) {
                receiptType.setText("Transfer");
            } else if (receipt instanceof SimcardReceipt) {
                receiptType.setText("Simcard Charge");
            }

            receiptTime.setText(receipt.timeToString(receipt.getTime()));
            receiptAmount.setText(String.format("$%s", receipt.getAmount()));
        }

        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Receipt Details")
                    .setMessage(receipt.toString())
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
}
