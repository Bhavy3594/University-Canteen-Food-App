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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

        if (CartManager.getCartItems().isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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

        txtSummary.setText(summary.toString());
        txtTotal.setText("₹" + CartManager.getTotalAmount());
    }

    private void setupClickListeners() {

        btnConfirmOrder.setOnClickListener(v -> placeOrder());

        btnCancelOrder.setOnClickListener(v -> {
            Toast.makeText(this, "Order cancelled", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void placeOrder() {

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

        int total = CartManager.getTotalAmount();

        // 🔥 Order object
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderId", orderId);
        orderMap.put("userId", uid);
        orderMap.put("summary", txtSummary.getText().toString());
        orderMap.put("totalAmount", total);
        orderMap.put("dateTime", dateTime);
        orderMap.put("status", "Pending");
        orderMap.put("location", location);
        orderMap.put("contact", contact);
        orderMap.put("payment", payment);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        // ✅ Save for ADMIN (all orders)
        rootRef.child("orders")
                .child(orderId)
                .setValue(orderMap)
                .addOnSuccessListener(unused -> {

                    // ✅ Save for USER (history)
                    rootRef.child("userOrders")
                            .child(uid)
                            .child(orderId)
                            .setValue(orderMap);

                    CartManager.clearCart();

                    Toast.makeText(
                            PlaceOrderActivity.this,
                            "Order placed successfully!",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(
                            PlaceOrderActivity.this,
                            DashboardActivity.class
                    );
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                PlaceOrderActivity.this,
                                "Failed: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()
                );
    }
}
