package com.example.unicanteen.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unicanteen.R;
import com.example.unicanteen.adapters.OrderHistoryAdapter;
import com.example.unicanteen.models.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerOrderHistory;
    private List<OrderModel> orderList = new ArrayList<>();
    private OrderHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // 1. Initialize Views
        initViews();

        // 2. Load User Orders (Maintaining your exact logic)
        loadUserOrders();
    }

    private void initViews() {
        recyclerOrderHistory = findViewById(R.id.recyclerOrderHistory);
        recyclerOrderHistory.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OrderHistoryAdapter(this, orderList);
        recyclerOrderHistory.setAdapter(adapter);
    }

    private void loadUserOrders() {
        // Keeping your auth check logic
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Accessing orders specific to the logged-in user
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                orderList.clear();

                if (!snapshot.exists()) {
                    adapter.notifyDataSetChanged();
                    return;
                }

                // Maintaining your loop and reverse collection logic
                for (DataSnapshot ds : snapshot.getChildren()) {
                    OrderModel order = ds.getValue(OrderModel.class);
                    if (order != null) {
                        orderList.add(order);
                    }
                }

                // Sorting to show the most recent orders first
                Collections.reverse(orderList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(OrderHistoryActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }
}