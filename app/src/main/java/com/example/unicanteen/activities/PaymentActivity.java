package com.example.unicanteen.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unicanteen.R;
import com.example.unicanteen.utils.CartManager;

public class PaymentActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button btnConfirm;

    private String location;
    private String contact;
    private String payment;

    private ActivityResultLauncher<Intent> upiLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // 1. Initialize Views
        initViews();

        upiLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if (data != null) {
                        String response = data.getStringExtra("response");
                        if (response == null) {
                            Toast.makeText(this, "Payment failed or cancelled.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String status = "";
                        String[] parts = response.split("&");
                        for (String part : parts) {
                            String[] keyValue = part.split("=");
                            if (keyValue.length >= 2 && keyValue[0].equalsIgnoreCase("Status")) {
                                status = keyValue[1].toLowerCase();
                                break;
                            }
                        }

                        if ("success".equals(status) || "submitted".equals(status)) {
                            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
                            proceedToPlaceOrder();
                        } else {
                            Toast.makeText(this, "Payment failed.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Payment failed or cancelled.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // 2. GET DATA FROM LOCATION SCREEN (Maintaining your exact logic)
        location = getIntent().getStringExtra("location");
        contact  = getIntent().getStringExtra("contact");

        // 3. Setup Click Listener (Maintaining flow)
        btnConfirm.setOnClickListener(v -> {
            int id = radioGroup.getCheckedRadioButtonId();

            // Validation logic
            if (id == -1) {
                Toast.makeText(this, "Please select payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton rb = findViewById(id);
            payment = rb.getText().toString();

            if (id == R.id.rbUPI) {
                int totalAmount = CartManager.getTotalAmount();
                Uri uri = Uri.parse("upi://pay?pa=unicanteen@upi&pn=UniCanteen&am=" + totalAmount + "&cu=INR");
                Intent upiIntent = new Intent(Intent.ACTION_VIEW, uri);
                Intent chooser = Intent.createChooser(upiIntent, "Pay with UPI");
                try {
                    upiLauncher.launch(chooser);
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(PaymentActivity.this, "No UPI app found", Toast.LENGTH_SHORT).show();
                    proceedToPlaceOrder();
                }
            } else {
                proceedToPlaceOrder();
            }
        });
    }

    private void proceedToPlaceOrder() {
        // ✅ GO TO PLACE ORDER SCREEN (Maintaining your intent extra logic)
        Intent intent = new Intent(PaymentActivity.this, PlaceOrderActivity.class);

        // ✅ PASSING ALL ACCUMULATED DATA
        intent.putExtra("location", location);
        intent.putExtra("contact", contact);
        intent.putExtra("payment", payment);

        startActivity(intent);
    }

    private void initViews() {
        radioGroup = findViewById(R.id.radioGroup);
        btnConfirm = findViewById(R.id.btnConfirm);
    }
}