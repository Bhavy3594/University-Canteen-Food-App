package com.example.unicanteen.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unicanteen.R;
import com.example.unicanteen.models.OrderModel;

import java.util.List;

public class UserOrdersAdapter extends RecyclerView.Adapter<UserOrdersAdapter.ViewHolder> {

    private final List<OrderModel> orderList;

    public UserOrdersAdapter(List<OrderModel> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Using your specific item layout for user orders
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        // Binding data as per your model fields (Logic Preserved)
        holder.txtOrderId.setText(order.getOrderId());
        holder.txtItems.setText(order.getSummary());
        holder.txtTotal.setText("₹ " + order.getTotalAmount());
        holder.txtDateTime.setText(order.getDateTime());
        holder.txtStatus.setText(order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // 🔹 VIEW HOLDER (Maintaining your fixed IDs)
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtItems, txtTotal, txtDateTime, txtStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Matching the IDs you fixed in your code
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtItems = itemView.findViewById(R.id.txtOrderItems);
            txtTotal = itemView.findViewById(R.id.txtOrderTotal);
            txtDateTime = itemView.findViewById(R.id.txtOrderDateTime);
            txtStatus = itemView.findViewById(R.id.txtOrderStatus);
        }
    }
}