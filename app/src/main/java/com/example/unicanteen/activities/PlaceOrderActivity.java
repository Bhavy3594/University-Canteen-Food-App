package com.example.unicanteen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.unicanteen.R;
import com.example.unicanteen.models.CartItemModel;
import com.example.unicanteen.utils.CartManager;
import com.example.unicanteen.utils.ImageUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlaceOrderActivity extends AppCompatActivity {

    private Button btnConfirmOrder, btnCancelOrder;
    private TextView txtSummary, txtTotal, txtLocationValue, txtContactValue, txtPaymentValue;
    private LinearLayout layoutOrderItemsContainer;

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
        txtLocationValue = findViewById(R.id.txtLocationValue);
        txtContactValue  = findViewById(R.id.txtContactValue);
        txtPaymentValue  = findViewById(R.id.txtPaymentValue);
        layoutOrderItemsContainer = findViewById(R.id.layoutOrderItemsContainer);
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

        // Dynamically add food items with images using row_order_item_preview
        if (layoutOrderItemsContainer != null) {
            layoutOrderItemsContainer.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);

            for (CartItemModel item : CartManager.getCartItems()) {
                View itemView = inflater.inflate(R.layout.row_order_item_preview, layoutOrderItemsContainer, false);

                ImageView img = itemView.findViewById(R.id.imgItemPreview);
                TextView txtName = itemView.findViewById(R.id.txtItemName);
                TextView txtQtyPrice = itemView.findViewById(R.id.txtItemQtyPrice);
                TextView txtBadgeQty = itemView.findViewById(R.id.txtItemBadgeQty);

                if (txtName != null) {
                    txtName.setText(item.getName());
                }

                if (txtQtyPrice != null) {
                    txtQtyPrice.setText("₹" + item.getPrice() + " × " + item.getQuantity() + " = ₹" + (item.getPrice() * item.getQuantity()));
                }

                if (txtBadgeQty != null) {
                    txtBadgeQty.setText("Qty: " + item.getQuantity());
                }

                if (img != null) {
                    ImageUtils.loadImage(this, item.getImageUrl(), img, R.drawable.ic_food_placeholder);
                }

                layoutOrderItemsContainer.addView(itemView);
            }
        }

        if (txtLocationValue != null) txtLocationValue.setText(location);
        if (txtContactValue != null) txtContactValue.setText(contact);
        if (txtPaymentValue != null) txtPaymentValue.setText(payment);
        if (txtSummary != null) txtSummary.setText("📍 Location: " + location + "\n📞 Contact: " + contact + "\n💳 Payment: " + payment);
        if (txtTotal != null) txtTotal.setText("₹" + CartManager.getTotalAmount());
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

        if (btnConfirmOrder != null) {
            btnConfirmOrder.setEnabled(false);
            btnConfirmOrder.setText("Submitting Order...");
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String orderId = "ORD" + System.currentTimeMillis();

        String dateTime = new SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
        ).format(new Date());

        int total = CartManager.getTotalAmount();

        // Build item text summary for backward compatibility
        StringBuilder itemsSummary = new StringBuilder();
        List<Map<String, Object>> itemsList = new ArrayList<>();

        for (CartItemModel item : CartManager.getCartItems()) {
            itemsSummary.append(item.getName())
                    .append(" x ")
                    .append(item.getQuantity())
                    .append("\n");

            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("name", item.getName());
            itemMap.put("price", item.getPrice());
            itemMap.put("quantity", item.getQuantity());
            itemMap.put("imageUrl", item.getImageUrl());
            itemsList.add(itemMap);
        }

        itemsSummary.append("\n📍 Location: ").append(location);
        itemsSummary.append("\n📞 Contact: ").append(contact);
        itemsSummary.append("\n💳 Payment: ").append(payment);

        // 🔥 Order object
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderId", orderId);
        orderMap.put("userId", uid);
        orderMap.put("summary", itemsSummary.toString());
        orderMap.put("totalAmount", total);
        orderMap.put("dateTime", dateTime);
        orderMap.put("status", "Pending");
        orderMap.put("location", location);
        orderMap.put("contact", contact);
        orderMap.put("payment", payment);
        orderMap.put("items", itemsList);

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
                .addOnFailureListener(e -> {
                    if (btnConfirmOrder != null) {
                        btnConfirmOrder.setEnabled(true);
                        btnConfirmOrder.setText("Confirm & Dispatch Order");
                    }
                    Toast.makeText(
                            PlaceOrderActivity.this,
                            "Failed: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}
