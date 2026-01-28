package com.example.unicanteen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unicanteen.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    private MaterialButton btnOrders, btnItems, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnOrders = findViewById(R.id.btnAdminOrders);
        btnItems = findViewById(R.id.btnAdminItems);
        btnLogout = findViewById(R.id.btnAdminLogout);
    }

    private void setupClickListeners() {

        // View Orders
        btnOrders.setOnClickListener(v ->
                startActivity(new Intent(
                        AdminDashboardActivity.this,
                        AdminOrdersActivity.class))
        );

        // Manage Menu Items (CRASH FIXED)
        btnItems.setOnClickListener(v -> {
            try {
                startActivity(new Intent(
                        AdminDashboardActivity.this,
                        AdminManageItemsActivity.class));
            } catch (Exception e) {
                Toast.makeText(this,
                        "Manage Items screen not found!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    AdminLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
