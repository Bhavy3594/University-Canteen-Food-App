package com.example.unicanteen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.unicanteen.R;

public class LocationActivity extends AppCompatActivity {

    private EditText edtLocation, edtContact;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // 1. Initialize Views
        initViews();

        // 2. Click Listeners (Maintaining your exact flow)
        setupClickListeners();
    }

    private void initViews() {
        edtLocation = findViewById(R.id.edtLocation);
        edtContact = findViewById(R.id.edtContact);
        btnNext = findViewById(R.id.btnNext);
    }

    private void setupClickListeners() {
        btnNext.setOnClickListener(v -> {
            String location = edtLocation.getText().toString().trim();
            String contact = edtContact.getText().toString().trim();

            // Maintaining your validation logic
            if (location.isEmpty()) {
                edtLocation.setError("Location required");
                edtLocation.requestFocus();
                return;
            }

            if (contact.isEmpty()) {
                edtContact.setError("Contact number required");
                edtContact.requestFocus();
                return;
            }

            // Validating 10 digit Indian mobile number
            if (!contact.matches("^[6-9][0-9]{9}$")) {
                edtContact.setError("Enter valid 10 digit mobile number");
                edtContact.requestFocus();
                return;
            }

            // ✅ GO TO PAYMENT SCREEN (Maintaining your intent extra logic)
            Intent intent = new Intent(LocationActivity.this, PaymentActivity.class);
            intent.putExtra("location", location);
            intent.putExtra("contact", contact);
            startActivity(intent);
        });
    }
}