package com.example.unicanteen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.unicanteen.R;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private Button floor1, floor2, floor3, floor4, floor5, floor6;
    private Button btnOrderHistory, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // 1. Initialize Views
        initViews();

        // 2. Setup Click Listeners (Maintaining your exact flow)
        setupClickListeners();
    }

    private void initViews() {
        // Floors initialization
        floor1 = findViewById(R.id.floor1);
        floor2 = findViewById(R.id.floor2);
        floor3 = findViewById(R.id.floor3);
        floor4 = findViewById(R.id.floor4);
        floor5 = findViewById(R.id.floor5);
        floor6 = findViewById(R.id.floor6);

        btnOrderHistory = findViewById(R.id.btnOrderHistory);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void setupClickListeners() {
        // Floors click (Passing data to MenuActivity)
        floor1.setOnClickListener(v -> openMenu("Floor 1"));
        floor2.setOnClickListener(v -> openMenu("Floor 2"));
        floor3.setOnClickListener(v -> openMenu("Floor 3"));
        floor4.setOnClickListener(v -> openMenu("Floor 4"));
        floor5.setOnClickListener(v -> openMenu("Floor 5"));
        floor6.setOnClickListener(v -> openMenu("Floor 6"));

        // Order history navigation
        btnOrderHistory.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, OrderHistoryActivity.class))
        );

        // 🔐 LOGOUT (Using your specific flags logic)
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void openMenu(String floorName) {
        // Maintaining your intent extra logic
        Intent intent = new Intent(DashboardActivity.this, MenuActivity.class);
        intent.putExtra("floor", floorName);
        startActivity(intent);
    }
}