package ir.ac.kntu;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserLoansActivity extends AppCompatActivity {
    private LoanAdapter loanAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_loans);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_user_loans);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loanAdapter = new LoanAdapter(this, Main.getUsers().getCurrentUser().getLoans());
        recyclerView.setAdapter(loanAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loanAdapter.notifyDataSetChanged();
    }
}
