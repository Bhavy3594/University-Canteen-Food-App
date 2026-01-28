package com.example.unicanteen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unicanteen.R;
import com.example.unicanteen.adapters.CartAdapter;
import com.example.unicanteen.utils.CartManager;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView txtTotalAmount;
    private Button btnPlaceOrder, btnClearCart;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // 1. Initialize Views
        initViews();

        // 2. Setup RecyclerView & Adapter
        setupRecyclerView();

        // 3. Update initial total
        updateTotal();

        // 4. Click Listeners
        setupClickListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerCart);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnClearCart = findViewById(R.id.btnClearCart);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CartAdapter(
                CartManager.getCartItems(),
                this::updateTotal
        );

        recyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnPlaceOrder.setOnClickListener(v -> {
            if (CartManager.getCartItems().isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, LocationActivity.class);
            startActivity(intent);
        });

        btnClearCart.setOnClickListener(v -> {
            CartManager.clearCart();
            adapter.notifyDataSetChanged();
            updateTotal();
        });
    }

    private void updateTotal() {
        int total = CartManager.getTotalAmount();
        // UPDATED: Removed "Total Amount: " to prevent UI overlap
        txtTotalAmount.setText("₹" + total);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            updateTotal();
        }
    }
}