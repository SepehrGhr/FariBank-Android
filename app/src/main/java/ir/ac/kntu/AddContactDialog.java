package ir.ac.kntu;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.button.MaterialButton;

public class AddContactDialog extends DialogFragment {

    private OnContactAddedListener listener;

    public interface OnContactAddedListener {
        void onContactAdded(Contact contact);
    }

    public void setOnContactAddedListener(OnContactAddedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_add_contact, null);
        builder.setView(view);

        EditText nameEditText = view.findViewById(R.id.nameEditText);
        EditText lastNameEditText = view.findViewById(R.id.lastNameEditText);
        EditText phoneNumberEditText = view.findViewById(R.id.phoneNumberEditText);
        MaterialButton addButton = view.findViewById(R.id.addButton);

        addButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String phoneNumber = phoneNumberEditText.getText().toString();
            User contactUser = Main.getUsers().findUserByPhoneNumber(phoneNumber);
            if (contactUser != null) {
                Contact contact = new Contact(contactUser, name, lastName, phoneNumber);
                listener.onContactAdded(contact);
                dismiss();
            } else {
                phoneNumberEditText.setError("There is no user with this phone number in out bank");
            }
        });

        return builder.create();
    }
}
