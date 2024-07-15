package ir.ac.kntu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class TransferActivity extends AppCompatActivity {

    private EditText etAccountIdCardNumber, etTransferAmount;
    private Button btnSelectRecentUser, btnSelectContact, btnTransfer;
    private RecyclerView rvRecentUsersContacts;
    private UserData userData;
    private User currentUser;
    private User selectedUser;
    private boolean fromFariBank;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        etAccountIdCardNumber = findViewById(R.id.et_account_id_card_number);
        etTransferAmount = findViewById(R.id.et_transfer_amount);
        btnSelectRecentUser = findViewById(R.id.btn_select_recent_user);
        btnSelectContact = findViewById(R.id.btn_select_contact);
        btnTransfer = findViewById(R.id.btn_transfer);
        rvRecentUsersContacts = findViewById(R.id.rv_recent_users_contacts);

        userData = Main.getUsers();
        currentUser = userData.getCurrentUser();

        btnSelectRecentUser.setOnClickListener(view -> showRecentUsers());
        btnSelectContact.setOnClickListener(view -> showContacts());
        btnTransfer.setOnClickListener(view -> validateAndTransfer());

        rvRecentUsersContacts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showRecentUsers() {
        List<User> recentUsers = currentUser.getRecentUsers();
        if (recentUsers.isEmpty()) {
            Toast.makeText(this, "No recent users found", Toast.LENGTH_SHORT).show();
        } else {
            rvRecentUsersContacts.setAdapter(new UserAdapter(recentUsers, this::onUserSelected));
            rvRecentUsersContacts.setVisibility(View.VISIBLE);
        }
    }

    private void showContacts() {
        List<Contact> contacts = currentUser.getContacts();
        List<User> validContacts = new ArrayList<>();
        for (Contact contact : contacts) {
            if (contact.getUser().haveInContacts(currentUser) && contact.getUser().isContactsActivated()) {
                validContacts.add(contact.getUser());
            }
        }
        if (validContacts.isEmpty()) {
            Toast.makeText(this, "No valid contacts found", Toast.LENGTH_SHORT).show();
        } else {
            rvRecentUsersContacts.setAdapter(new UserAdapter(validContacts, this::onUserSelected));
            rvRecentUsersContacts.setVisibility(View.VISIBLE);
        }
    }

    private void onUserSelected(User user) {
        selectedUser = user;
        etAccountIdCardNumber.setText(user.getAccount().getAccountID());
        rvRecentUsersContacts.setVisibility(View.GONE);
    }

    private void validateAndTransfer() {
        String accountOrCard = etAccountIdCardNumber.getText().toString().trim();
        String amountStr = etTransferAmount.getText().toString().trim();
        if (TextUtils.isEmpty(accountOrCard)) {
            Toast.makeText(this, "Please enter account ID/card number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(amountStr)) {
            Toast.makeText(this, "Please enter transfer amount", Toast.LENGTH_SHORT).show();
            return;
        }
        long amount = Long.parseLong(amountStr);
        if (amount <= 0) {
            Toast.makeText(this, "Amount must be greater than zero", Toast.LENGTH_SHORT).show();
            return;
        }

        if (accountOrCard.length() == 13) {
            transferByAccountID(accountOrCard, amount);
        } else {
            transferByCardNumber(accountOrCard, amount);
        }
    }

    private void transferByAccountID(String accountId, long amount) {
        Entry<User, Boolean> result = userData.findUserByAccountID(accountId);
        if (result.getKey() != null) {
            selectedUser = result.getKey();
            fromFariBank = result.getValue();
            processTransfer(amount);
        } else {
            Toast.makeText(this, "No user found with this account ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void transferByCardNumber(String cardNumber, long amount) {
        Entry<User, Boolean> result = userData.findUserByCardNumber(cardNumber);
        if (result.getKey() != null) {
            selectedUser = result.getKey();
            fromFariBank = result.getValue();
            processTransfer(amount);
        } else {
            Toast.makeText(this, "No user found with this card number", Toast.LENGTH_SHORT).show();
        }
    }

    private void processTransfer(long amount) {
        if (selectedUser.equals(currentUser)) {
            Toast.makeText(this, "You can't transfer money to yourself", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedUser.isBlocked()) {
            Toast.makeText(this, "User's account is blocked", Toast.LENGTH_SHORT).show();
            return;
        }

        long fee = fromFariBank ? 0 : Main.getManagerData().getFeeRate().getCardFee();
        long totalAmount = amount + fee;
        if (currentUser.getAccount().getBalance() < totalAmount) {
            Toast.makeText(this, "Your balance isn't enough", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.getAccount().withdrawMoney(totalAmount, currentUser);
        selectedUser.getAccount().setBalance( (selectedUser.getAccount().getBalance() + amount));
        currentUser.addReceipt(new TransferReceipt( amount, currentUser, selectedUser, Method.ACCOUNT));
        selectedUser.addReceipt(new TransferReceipt( amount, currentUser, selectedUser, Method.ACCOUNT));

        Toast.makeText(this, "Transfer successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(TransferActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
