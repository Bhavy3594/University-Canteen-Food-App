package com.example.unicanteen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unicanteen.R;

public class PaymentActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // 1. Initialize Views
        initViews();

        // 2. GET DATA FROM LOCATION SCREEN (Maintaining your exact logic)
        String location = getIntent().getStringExtra("location");
        String contact  = getIntent().getStringExtra("contact");

        // 3. Setup Click Listener (Maintaining flow)
        btnConfirm.setOnClickListener(v -> {
            int id = radioGroup.getCheckedRadioButtonId();

            // Validation logic
            if (id == -1) {
                Toast.makeText(this, "Please select payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton rb = findViewById(id);
            String payment = rb.getText().toString();

            // ✅ GO TO PLACE ORDER SCREEN (Maintaining your intent extra logic)
            Intent intent = new Intent(PaymentActivity.this, PlaceOrderActivity.class);

            // ✅ PASSING ALL ACCUMULATED DATA
            intent.putExtra("location", location);
            intent.putExtra("contact", contact);
            intent.putExtra("payment", payment);

            startActivity(intent);
        });
    }

    private void initViews() {
        radioGroup = findViewById(R.id.radioGroup);
        btnConfirm = findViewById(R.id.btnConfirm);
    }
}