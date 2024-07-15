package ir.ac.kntu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText firstNameEditText;
    private TextInputEditText lastNameEditText;
    private TextInputEditText securityNumberEditText;
    private TextInputEditText phoneNumberEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        securityNumberEditText = findViewById(R.id.securityNumberEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean validateInputs() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String securityNumber = securityNumberEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (firstName.isEmpty() || !firstName.matches("[a-zA-Z]+")) {
            firstNameEditText.setError("Invalid first name");
            return false;
        }

        if (lastName.isEmpty() || !lastName.matches("[a-zA-Z]+")) {
            lastNameEditText.setError("Invalid last name");
            return false;
        }

        if (securityNumber.length() != 10 || !securityNumber.matches("\\d+")) {
            securityNumberEditText.setError("Invalid security number");
            return false;
        }

        if (phoneNumber.isEmpty() || !Patterns.PHONE.matcher(phoneNumber).matches() || Main.getUsers().findUserByPhoneNumber(phoneNumber) != null) {
            phoneNumberEditText.setError("Invalid or already registered phone number");
            return false;
        }

        if (!validatePassword(password)) {
            passwordEditText.setError("Password must be at least 8 characters long and contain an upper-case letter, a lower-case letter, a digit, and a special character");
            return false;
        }
        User newUser = new User(firstName, lastName, new PhoneNumber(phoneNumber, 0), securityNumber, password);
        newUser.setAuthenticated(true);
        newUser.setAccount();
        Main.getUsers().addUser(newUser);
        Main.getUsers().setCurrentUser(newUser);
        return true;
    }

    private boolean validatePassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#\\$%^&*()].*");
    }
}
