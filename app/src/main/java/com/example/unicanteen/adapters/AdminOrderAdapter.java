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

public class AdminOrderAdapter
        extends RecyclerView.Adapter<AdminOrderAdapter.ViewHolder> {

    private final Context context;
    private final List<OrderModel> orderList;
    private OnItemClickListener listener;

    // 🔥 Status click listener
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
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order_history, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder, int position) {

        OrderModel order = orderList.get(position);

        holder.txtOrderId.setText(order.getOrderId());
        holder.txtItems.setText(order.getSummary());
        holder.txtTotal.setText("₹ " + order.getTotalAmount());
        holder.txtDateTime.setText(order.getDateTime());
        holder.txtStatus.setText("Status: " + order.getStatus());

        // 📞 Contact
        if (order.getContact() != null && !order.getContact().isEmpty()) {
            holder.txtContact.setText("📞 " + order.getContact());
        } else {
            holder.txtContact.setText("📞 N/A");
        }

        // ☎️ CALL USER BUTTON (SAFE)
        holder.btnCallUser.setOnClickListener(v -> {
            String phone = order.getContact();
            if (phone == null || phone.isEmpty()) {
                Toast.makeText(
                        context,
                        "Contact number not available",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            context.startActivity(intent);
        });

        // 🔥 Card click (status update dialog etc.)
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

    // 🔥 ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderId, txtItems, txtTotal,
                txtDateTime, txtStatus, txtContact;

        Button btnCallUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderId   = itemView.findViewById(R.id.txtOrderId);
            txtItems     = itemView.findViewById(R.id.txtOrderItems);
            txtTotal     = itemView.findViewById(R.id.txtOrderTotal);
            txtDateTime  = itemView.findViewById(R.id.txtOrderDateTime);
            txtStatus    = itemView.findViewById(R.id.txtOrderStatus);
            txtContact   = itemView.findViewById(R.id.txtContact);

            // 🔥 THIS LINE WAS MISSING (CAUSE OF CRASH)
            btnCallUser  = itemView.findViewById(R.id.btnCallUser);
        }
    }
}
