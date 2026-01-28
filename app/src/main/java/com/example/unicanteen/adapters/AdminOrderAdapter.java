package com.example.unicanteen.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.ViewHolder> {

    private final Context context;
    private final List<OrderModel> orderList;
    private OnItemClickListener listener;

    // 🔥 CLICK LISTENER INTERFACE (Maintaining your logic for Status Updates)
    public interface OnItemClickListener {
        void onItemClick(OrderModel order);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AdminOrderAdapter(Context context, List<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Using your specific item layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        // Setting data using your existing model fields
        holder.txtOrderId.setText(order.getOrderId());
        holder.txtItems.setText(order.getSummary());
        holder.txtTotal.setText("₹ " + order.getTotalAmount());
        holder.txtDateTime.setText(order.getDateTime());
        holder.txtStatus.setText("Status: " + order.getStatus());

        // 📞 Maintaining your contact display logic
        if (order.getContact() != null) {
            holder.txtContact.setText("📞 " + order.getContact());
        } else {
            holder.txtContact.setText("📞 N/A");
        }

        // ☎️ CALL USER BUTTON (Maintaining ACTION_DIAL intent logic)
        holder.btnCallUser.setOnClickListener(v -> {
            String phone = order.getContact();
            if (phone == null || phone.isEmpty()) {
                Toast.makeText(context, "Contact number not available", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            context.startActivity(intent);
        });

        // 🔥 ITEM CLICK (Maintaining logic for Status Change Dialog)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtItems, txtTotal, txtDateTime, txtStatus, txtContact;
        Button btnCallUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Binding your IDs from the premium XML
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtItems = itemView.findViewById(R.id.txtOrderItems);
            txtTotal = itemView.findViewById(R.id.txtOrderTotal);
            txtDateTime = itemView.findViewById(R.id.txtOrderDateTime);
            txtStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtContact = itemView.findViewById(R.id.txtContact);

        }
    }
}