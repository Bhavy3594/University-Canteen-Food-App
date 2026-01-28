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
import java.util.List;

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

        // 4. Status Update Logic (Maintaining your flow)
        setupStatusUpdateListener();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewAdminOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        adapter = new AdminOrderAdapter(this, orderList);
        recyclerView.setAdapter(adapter);
    }

    private void setupStatusUpdateListener() {
        adapter.setOnItemClickListener(order -> {
            String[] statuses = {"Pending", "Preparing", "Ready", "Delivered"};

            new AlertDialog.Builder(this)
                    .setTitle("Update Order Status")
                    .setItems(statuses, (dialog, which) -> {
                        String newStatus = statuses[which];

                        // Maintaining your nested database logic
                        ordersRef.child(order.getUserId())
                                .child(order.getOrderId())
                                .child("status")
                                .setValue(newStatus)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(this, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show()
                                );
                    })
                    .show();
        });
    }

    private void loadAllOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                orderList.clear();

                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    String uid = userSnap.getKey();

                    for (DataSnapshot orderSnap : userSnap.getChildren()) {
                        // Your Crash Fix Logic
                        if (!orderSnap.hasChild("orderId")) {
                            continue;
                        }

                        OrderModel order = orderSnap.getValue(OrderModel.class);
                        if (order != null) {
                            order.setUserId(uid);
                            orderList.add(order);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AdminOrdersActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }
}