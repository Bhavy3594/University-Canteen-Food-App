package com.example.unicanteen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unicanteen.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private FirebaseAuth auth;

    // 🔐 ADMIN CREDENTIALS (ONLY HERE)
    private static final String ADMIN_EMAIL = "admin@unicanteen.com";
    private static final String ADMIN_PASSWORD = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Email & Password required", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔥 ADMIN LOGIN CHECK FIRST
            if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
                // Save admin session flag
                getSharedPreferences("ADMIN_SESSION", MODE_PRIVATE)
                        .edit()
                        .putBoolean("IS_ADMIN_LOGGED_IN", true)
                        .apply();

                // We MUST authenticate with Firebase so Storage Rules (request.auth != null) pass!
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                // Auto-register the admin so they have a valid Auth Token
                                auth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(regTask -> {
                                            if (regTask.isSuccessful()) {
                                                Toast.makeText(this, "Admin Auth Registered & Logged In", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(this, "Admin Auth Failed: " + regTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });
                return;
            }

            // 🔐 USER LOGIN (Firebase)
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            // Clear any admin session flag for standard users
                            getSharedPreferences("ADMIN_SESSION", MODE_PRIVATE)
                                    .edit()
                                    .clear()
                                    .apply();

                            Toast.makeText(this, "User Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this,
                                    "Invalid credentials",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
    }
}
