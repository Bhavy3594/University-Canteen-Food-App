package com.example.unicanteen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unicanteen.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 🔥 VERY IMPORTANT: Clear admin session on every app start
        getSharedPreferences("ADMIN_SESSION", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        new Handler().postDelayed(() -> {

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                // ✅ Firebase user = NORMAL USER ONLY
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
            } else {
                // ❌ No login → Login screen
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }

            finish();

        }, 2000); // 2 sec splash delay
    }
}
