package ir.ac.kntu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private static final String TAG = "ContactsActivity";
    private RecyclerView contactsRecyclerView;
    private ContactsAdapter contactsAdapter;
    private ArrayList<Contact> contacts;
    private User currentUser;

    private final ActivityResultLauncher<Intent> editContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Contact updatedContact = (Contact) result.getData().getSerializableExtra("contact");
                    int position = result.getData().getIntExtra("position", -1);
                    Log.d(TAG, "Received updated contact: " + updatedContact + " at position: " + position);
                    if (updatedContact != null && position != -1) {
                        contacts.set(position, updatedContact);
                        contactsAdapter.notifyItemChanged(position);
                        currentUser.getContacts().set(position, updatedContact);
                    }
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contactsRecyclerView = findViewById(R.id.contactsRecyclerView);
        FloatingActionButton addContactButton = findViewById(R.id.addContactButton);

        currentUser = Main.getUsers().getCurrentUser();
        if (currentUser != null) {
            contacts = new ArrayList<>(currentUser.getContacts());
            setupContactsRecyclerView(contacts);
        }

        addContactButton.setOnClickListener(v -> showAddContactDialog());
    }

    private void setupContactsRecyclerView(ArrayList<Contact> contacts) {
        contactsAdapter = new ContactsAdapter(contacts, this::onContactSelected, this::onContactDeleted);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsRecyclerView.setAdapter(contactsAdapter);
    }

    private void showAddContactDialog() {
        AddContactDialog dialog = new AddContactDialog();
        dialog.setOnContactAddedListener(contact -> {
            contacts.add(contact);
            contactsAdapter.notifyItemInserted(contacts.size() - 1);
            currentUser.addNewContact(contact);
        });
        dialog.show(getSupportFragmentManager(), "AddContactDialog");
    }

    private void onContactSelected(Contact contact, int position) {
        Intent intent = new Intent(this, ContactDetailActivity.class);
        intent.putExtra("contact", contact);
        intent.putExtra("position", position);
        Log.d(TAG, "Starting ContactDetailActivity with contact: " + contact + " at position: " + position);
        editContactLauncher.launch(intent);
    }

    private void onContactDeleted(int position) {
        Contact removedContact = contacts.remove(position);
        contactsAdapter.notifyItemRemoved(position);
        currentUser.getContacts().remove(removedContact);
        contactsAdapter.notifyDataSetChanged();

        contactsAdapter.updatePositions(position);
    }
}
