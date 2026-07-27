package com.example.unicanteen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unicanteen.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 🔥 Warm up product catalog database cache during splash screen delay
        try {
            FirebaseDatabase.getInstance().getReference("menuItems").keepSynced(true);
        } catch (Exception ignored) {}

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            boolean isAdminLoggedIn = getSharedPreferences("ADMIN_SESSION", MODE_PRIVATE)
                    .getBoolean("IS_ADMIN_LOGGED_IN", false);

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            boolean isAdminUser = currentUser != null && "admin@unicanteen.com".equalsIgnoreCase(currentUser.getEmail());

            if (isAdminLoggedIn || isAdminUser) {
                // ✅ Authenticated Admin -> Admin Dashboard
                startActivity(new Intent(SplashActivity.this, AdminDashboardActivity.class));
            } else if (currentUser != null) {
                // ✅ Normal User -> User Dashboard
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
            } else {
                // ❌ Unauthenticated -> Login Choice Screen
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }

            finish();

        }, 2000); // 2 sec splash delay
    }
}
