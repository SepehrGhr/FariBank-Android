package ir.ac.kntu;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private List<Ticket> ticketList;

    public TicketAdapter(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        holder.bind(ticket);
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvStatus;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_type);
            tvStatus = itemView.findViewById(R.id.tv_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Ticket ticket = ticketList.get(position);
                    showTicketDetailsDialog(ticket, itemView.getContext());
                }
            });
        }

        void bind(Ticket ticket) {
            tvType.setText(ticket.getType().toString());
            tvStatus.setText(ticket.getStatus().toString());
        }
    }

    private void showTicketDetailsDialog(Ticket ticket, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ticket Details");

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_ticket_details, null);
        builder.setView(dialogView);

        TextView tvUserMessage = dialogView.findViewById(R.id.tv_user_message);
        TextView tvAdminMessage = dialogView.findViewById(R.id.tv_admin_message);

        tvUserMessage.setText("Your message : " + ticket.getUserMessage());
        tvAdminMessage.setText("Admin message : " + ticket.getAdminMessage());

        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
