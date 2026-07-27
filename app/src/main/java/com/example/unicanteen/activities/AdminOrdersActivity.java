package com.example.unicanteen.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unicanteen.R;
import com.example.unicanteen.adapters.AdminOrderAdapter;
import com.example.unicanteen.models.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;

public class AdminOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<OrderModel> orderList;
    private AdminOrderAdapter adapter;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        // 1. View Initialization
        initViews();

        // 2. Firebase Reference
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        // 3. Load Data
        loadAllOrders();

        // 4. Status Update Logic
        setupStatusUpdateListener();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewAdminOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        orderList = new ArrayList<>();
        adapter = new AdminOrderAdapter(this, orderList);
        recyclerView.setAdapter(adapter);
    }

    private void setupStatusUpdateListener() {
        adapter.setOnItemClickListener(order -> showUpdateStatusDialog(order));
    }

    private void showUpdateStatusDialog(OrderModel order) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_update_status, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        TextView txtCurrentStatusBadge = view.findViewById(R.id.txtCurrentStatusBadge);
        TextView txtOrderIdSub = view.findViewById(R.id.txtOrderIdSub);

        if (order.getOrderId() != null) {
            txtOrderIdSub.setText("Order ID: " + order.getOrderId());
        }

        String currentStatus = order.getStatus() != null ? order.getStatus() : "Pending";
        txtCurrentStatusBadge.setText(currentStatus);

        MaterialCardView cardPending = view.findViewById(R.id.cardStatusPending);
        MaterialCardView cardPreparing = view.findViewById(R.id.cardStatusPreparing);
        MaterialCardView cardReady = view.findViewById(R.id.cardStatusReady);
        MaterialCardView cardDelivered = view.findViewById(R.id.cardStatusDelivered);

        TextView txtCheckPending = view.findViewById(R.id.txtCheckPending);
        TextView txtCheckPreparing = view.findViewById(R.id.txtCheckPreparing);
        TextView txtCheckReady = view.findViewById(R.id.txtCheckReady);
        TextView txtCheckDelivered = view.findViewById(R.id.txtCheckDelivered);

        // Highlight current active status
        highlightActiveCard("Pending".equalsIgnoreCase(currentStatus), cardPending, txtCheckPending);
        highlightActiveCard("Preparing".equalsIgnoreCase(currentStatus), cardPreparing, txtCheckPreparing);
        highlightActiveCard("Ready".equalsIgnoreCase(currentStatus), cardReady, txtCheckReady);
        highlightActiveCard("Delivered".equalsIgnoreCase(currentStatus), cardDelivered, txtCheckDelivered);

        cardPending.setOnClickListener(v -> updateOrderStatus(order, "Pending", dialog));
        cardPreparing.setOnClickListener(v -> updateOrderStatus(order, "Preparing", dialog));
        cardReady.setOnClickListener(v -> updateOrderStatus(order, "Ready", dialog));
        cardDelivered.setOnClickListener(v -> updateOrderStatus(order, "Delivered", dialog));

        Button btnClose = view.findViewById(R.id.btnCloseStatusDialog);
        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }

    private void highlightActiveCard(boolean isActive, MaterialCardView card, TextView checkView) {
        if (card == null) return;
        if (isActive) {
            card.setStrokeColor(Color.parseColor("#059669"));
            card.setStrokeWidth(4);
            card.setCardBackgroundColor(Color.parseColor("#ECFDF5"));
            if (checkView != null) checkView.setVisibility(View.VISIBLE);
        } else {
            card.setStrokeColor(Color.parseColor("#E2E8F0"));
            card.setStrokeWidth(2);
            card.setCardBackgroundColor(Color.parseColor("#F8FAFC"));
            if (checkView != null) checkView.setVisibility(View.GONE);
        }
    }

    private void updateOrderStatus(OrderModel order, String newStatus, AlertDialog dialog) {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();

        // Immediate local status update
        order.setStatus(newStatus);
        adapter.notifyDataSetChanged();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.child("orders")
                .child(order.getOrderId())
                .child("status")
                .setValue(newStatus)
                .addOnSuccessListener(aVoid -> {
                    if (order.getUserId() != null) {
                        rootRef.child("userOrders")
                                .child(order.getUserId())
                                .child(order.getOrderId())
                                .child("status")
                                .setValue(newStatus);
                    }
                    Toast.makeText(this, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadAllOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                orderList.clear();

                for (DataSnapshot orderSnap : snapshot.getChildren()) {
                    if (!orderSnap.hasChild("orderId")) {
                        continue;
                    }

                    OrderModel order = orderSnap.getValue(OrderModel.class);
                    if (order != null) {
                        orderList.add(order);
                    }
                }

                // 🔥 SORT NEWEST -> OLDEST (Newest incoming orders appear at the TOP)
                Collections.reverse(orderList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AdminOrdersActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }
}