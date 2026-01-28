package com.example.unicanteen.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unicanteen.R;
import com.example.unicanteen.adapters.UserOrdersAdapter;
import com.example.unicanteen.models.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<OrderModel> orderList;
    private UserOrdersAdapter adapter;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        // 1. Initialize Views and RecyclerView
        initViews();

        // 2. Firebase Initialization (Maintaining your exact logic)
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        // 3. Load Orders with Real-time Updates
        loadOrders();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerUserOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        adapter = new UserOrdersAdapter(orderList);
        recyclerView.setAdapter(adapter);
    }

    private void loadOrders() {
        // Maintaining your exact ValueEventListener and data mapping
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    OrderModel order = snap.getValue(OrderModel.class);
                    if (order != null) {
                        orderList.add(order);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Logic preserved as per your original file
            }
        });
    }
}