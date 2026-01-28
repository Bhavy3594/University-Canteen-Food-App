package com.example.unicanteen.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unicanteen.R;
import com.example.unicanteen.models.OrderModel;
import com.example.unicanteen.utils.OrderManager;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private final Context context;
    private final List<OrderModel> orderList;

    public OrderHistoryAdapter(Context context, List<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Using your specific item layout for order history
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        // Binding basic data as per your model
        holder.txtOrderId.setText(order.getOrderId());
        holder.txtItems.setText(order.getSummary());
        holder.txtTotal.setText("₹ " + order.getTotalAmount());
        holder.txtStatus.setText("Status: " + order.getStatus());
        holder.txtOrderDateTime.setText(order.getDateTime());

        // 🔥 STATUS UI LOGIC (Maintaining your exact color and visibility flow)
        if ("Cancelled".equals(order.getStatus())) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFCDD2"));
            holder.btnCancel.setVisibility(View.GONE);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.btnCancel.setVisibility(View.VISIBLE);
        }

        // ❌ CANCEL ORDER BUTTON (Maintaining your exact AlertDialog and OrderManager logic)
        holder.btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Cancel Order")
                    .setMessage("Are you sure you want to cancel this order?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Calling your custom utility
                        OrderManager.cancelOrder(order.getOrderId());

                        // Updating local UI state
                        order.setStatus("Cancelled");
                        notifyItemChanged(position);

                        Toast.makeText(context, "Order Cancelled", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // 🔹 VIEW HOLDER (Matching your premium XML IDs)
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtItems, txtTotal, txtOrderDateTime, txtStatus;
        Button btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtItems = itemView.findViewById(R.id.txtOrderItems);
            txtTotal = itemView.findViewById(R.id.txtOrderTotal);
            txtOrderDateTime = itemView.findViewById(R.id.txtOrderDateTime);
            txtStatus = itemView.findViewById(R.id.txtOrderStatus);
            btnCancel = itemView.findViewById(R.id.btnCancelOrder);
        }
    }
}