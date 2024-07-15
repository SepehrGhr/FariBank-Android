package ir.ac.kntu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class SupportsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TicketAdapter ticketAdapter;
    private ArrayList<Ticket> ticketList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supports);

        ticketList = (ArrayList<Ticket>) Main.getUsers().getCurrentUser().getTickets();

        recyclerView = findViewById(R.id.recycler_view_tickets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ticketAdapter = new TicketAdapter(ticketList);
        recyclerView.setAdapter(ticketAdapter);

        FloatingActionButton fabAddTicket = findViewById(R.id.fab_add_ticket);
        fabAddTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTicketDialog();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        ticketAdapter.notifyDataSetChanged();
    }

    private void showAddTicketDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Ticket");

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_ticket, null);
        builder.setView(dialogView);

        Spinner spinnerType = dialogView.findViewById(R.id.spinner_ticket_type);
        EditText editTextMessage = dialogView.findViewById(R.id.edit_text_message);

        ArrayAdapter<Type> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Type.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Type type = (Type) spinnerType.getSelectedItem();
                String message = editTextMessage.getText().toString();

                if (!message.isEmpty()) {
                    Ticket newTicket = new Ticket(message, type, Main.getUsers().getCurrentUser());
                    Main.getUsers().getCurrentUser().addNewTicket(newTicket);
                    Main.getAdminData().addNewTicket(newTicket);
                    ticketAdapter.notifyDataSetChanged();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
}
