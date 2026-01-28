package com.example.unicanteen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unicanteen.R;
import com.example.unicanteen.models.CartItemModel;
import com.example.unicanteen.utils.CartManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlaceOrderActivity extends AppCompatActivity {

    private Button btnConfirmOrder, btnCancelOrder;
    private TextView txtSummary, txtTotal;
    private String location, contact, payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        initViews();
        loadIntentData();
        generateOrderSummary();
        setupClickListeners();
    }

    private void initViews() {
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        btnCancelOrder  = findViewById(R.id.btnCancelOrder);
        txtSummary      = findViewById(R.id.txtSummary);
        txtTotal        = findViewById(R.id.txtTotal);
    }

    private void loadIntentData() {
        location = getIntent().getStringExtra("location");
        contact  = getIntent().getStringExtra("contact");
        payment  = getIntent().getStringExtra("payment");

        if (location == null) location = "N/A";
        if (contact == null)  contact  = "N/A";
        if (payment == null)  payment  = "Not selected";
    }

    private void generateOrderSummary() {
        StringBuilder summary = new StringBuilder();

        for (CartItemModel item : CartManager.getCartItems()) {
            summary.append(item.getName())
                    .append(" x ")
                    .append(item.getQuantity())
                    .append("\n");
        }

        summary.append("\n📍 Location: ").append(location);
        summary.append("\n📞 Contact: ").append(contact);
        summary.append("\n💳 Payment: ").append(payment);

        int total = CartManager.getTotalAmount();
        txtSummary.setText(summary.toString());

        // UPDATED: Removed "Total Amount: " prefix
        txtTotal.setText("₹" + total);
    }

    private void setupClickListeners() {
        int total = CartManager.getTotalAmount();
        String summaryText = txtSummary.getText().toString();

        btnConfirmOrder.setOnClickListener(v -> placeOrder(total, summaryText));

        btnCancelOrder.setOnClickListener(v -> {
            Toast.makeText(this, "Order cancelled", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void placeOrder(int total, String summary) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "Session expired. Login again.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String orderId = "ORD" + System.currentTimeMillis();

        String dateTime = new SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
        ).format(new Date());

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(uid)
                .child(orderId);

        ref.child("orderId").setValue(orderId);
        ref.child("summary").setValue(summary);
        ref.child("totalAmount").setValue(total);
        ref.child("dateTime").setValue(dateTime);
        ref.child("status").setValue("Pending");
        ref.child("location").setValue(location);
        ref.child("contact").setValue(contact);
        ref.child("payment").setValue(payment);

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("contact")
                .setValue(contact);

        CartManager.clearCart();

        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}