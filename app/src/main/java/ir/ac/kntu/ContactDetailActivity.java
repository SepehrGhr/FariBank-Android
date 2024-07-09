package ir.ac.kntu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class ContactDetailActivity extends AppCompatActivity {

    private static final String TAG = "ContactDetailActivity";
    private EditText nameEditText;
    private EditText lastNameEditText;
    private EditText phoneNumberEditText;
    private Contact contact;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        nameEditText = findViewById(R.id.nameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        MaterialButton editButton = findViewById(R.id.editButton);

        contact = (Contact) getIntent().getSerializableExtra("contact");
        position = getIntent().getIntExtra("position", -1);
        Log.d(TAG, "Received contact: " + contact + " at position: " + position);

        if (contact != null) {
            nameEditText.setText(contact.getName());
            lastNameEditText.setText(contact.getLastName());
            phoneNumberEditText.setText(contact.getPhoneNumber());
        }

        editButton.setOnClickListener(v -> {
            String newName = nameEditText.getText().toString();
            String newLastName = lastNameEditText.getText().toString();
            String newPhoneNumber = phoneNumberEditText.getText().toString();

            contact.edit(newName, newLastName, newPhoneNumber);
            Log.d(TAG, "Edited contact: " + contact);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("contact", contact);
            resultIntent.putExtra("position", position);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
