package ir.ac.kntu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private final ArrayList<Contact> contacts;
    private final OnContactClickListener clickListener;
    private final OnContactDeleteListener deleteListener;

    public interface OnContactClickListener {
        void onContactClick(Contact contact, int position);
    }

    public interface OnContactDeleteListener {
        void onContactDelete(int position);
    }

    public ContactsAdapter(ArrayList<Contact> contacts, OnContactClickListener clickListener, OnContactDeleteListener deleteListener) {
        this.contacts = contacts;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact, clickListener, deleteListener, position);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView phoneNumberTextView;
        private final MaterialButton deleteButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(Contact contact, OnContactClickListener clickListener, OnContactDeleteListener deleteListener, int position) {
            nameTextView.setText(contact.getName());
            phoneNumberTextView.setText(contact.getPhoneNumber());
            itemView.setOnClickListener(v -> clickListener.onContactClick(contact, position));
            deleteButton.setOnClickListener(v -> deleteListener.onContactDelete(position));
        }
    }

    public void updatePositions(int deletedPosition) {
        notifyItemRangeChanged(deletedPosition, contacts.size() - deletedPosition);
    }
}
