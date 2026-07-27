package com.example.unicanteen.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unicanteen.R;
import com.example.unicanteen.models.CartItemModel;
import com.example.unicanteen.models.OrderModel;
import com.example.unicanteen.utils.ImageUtils;

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
        holder.txtTotal.setText("₹ " + order.getTotalAmount());
        holder.txtDateTime.setText(order.getDateTime() != null ? order.getDateTime() : "");

        String status = order.getStatus() != null ? order.getStatus() : "Pending";
        holder.txtStatus.setText(status);

        // 📞 Contact
        if (order.getContact() != null && !order.getContact().isEmpty()) {
            holder.txtContact.setText("📞 " + order.getContact());
        } else {
            holder.txtContact.setText("📞 N/A");
        }

        // 📷 RENDER ORDERED FOOD ITEMS WITH IMAGES FOR ADMIN
        if (holder.layoutOrderItemsContainer != null) {
            holder.layoutOrderItemsContainer.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(context);

            if (order.getItems() != null && !order.getItems().isEmpty()) {
                holder.txtItems.setVisibility(View.GONE);
                for (CartItemModel item : order.getItems()) {
                    View itemView = inflater.inflate(R.layout.row_order_item_preview, holder.layoutOrderItemsContainer, false);
                    ImageView img = itemView.findViewById(R.id.imgItemPreview);
                    TextView txtName = itemView.findViewById(R.id.txtItemName);
                    TextView txtQtyPrice = itemView.findViewById(R.id.txtItemQtyPrice);

                    txtName.setText(item.getName());
                    txtQtyPrice.setText("₹" + item.getPrice() + " x " + item.getQuantity() + " = ₹" + (item.getPrice() * item.getQuantity()));

                    if (img != null) {
                        ImageUtils.loadImage(context, item.getImageUrl(), img, R.drawable.ic_food_placeholder);
                    }
                    holder.layoutOrderItemsContainer.addView(itemView);
                }
            } else if (order.getSummary() != null && !order.getSummary().isEmpty()) {
                // Legacy order fallback: parse lines and resolve images via ImageUtils
                holder.txtItems.setText(order.getSummary());
                holder.txtItems.setVisibility(View.VISIBLE);

                String[] lines = order.getSummary().split("\n");
                for (String line : lines) {
                    if (line.contains("x") && !line.startsWith("📍") && !line.startsWith("📞") && !line.startsWith("💳")) {
                        View itemView = inflater.inflate(R.layout.row_order_item_preview, holder.layoutOrderItemsContainer, false);
                        ImageView img = itemView.findViewById(R.id.imgItemPreview);
                        TextView txtName = itemView.findViewById(R.id.txtItemName);
                        TextView txtQtyPrice = itemView.findViewById(R.id.txtItemQtyPrice);

                        txtName.setText(line.trim());
                        txtQtyPrice.setText("Included in order");

                        String itemName = line.split("x")[0].trim();
                        String imageUrl = ImageUtils.getImageUrl(itemName, null);

                        if (img != null) {
                            ImageUtils.loadImage(context, imageUrl, img, R.drawable.ic_food_placeholder);
                        }
                        holder.layoutOrderItemsContainer.addView(itemView);
                    }
                }
            } else {
                holder.txtItems.setVisibility(View.VISIBLE);
                holder.txtItems.setText("No item details");
            }
        }

        // 🔥 STATUS BADGE STYLING FOR ADMIN VIEW
        if ("Cancelled".equalsIgnoreCase(status)) {
            holder.txtStatus.setBackgroundResource(R.drawable.bg_status_cancelled);
            holder.txtStatus.setTextColor(Color.parseColor("#DC2626"));
        } else if ("Delivered".equalsIgnoreCase(status)) {
            holder.txtStatus.setBackgroundResource(R.drawable.bg_status_delivered);
            holder.txtStatus.setTextColor(Color.parseColor("#059669"));
        } else if ("Preparing".equalsIgnoreCase(status) || "Ready".equalsIgnoreCase(status)) {
            holder.txtStatus.setBackgroundResource(R.drawable.bg_status_preparing);
            holder.txtStatus.setTextColor(Color.parseColor("#D97706"));
        } else {
            holder.txtStatus.setBackgroundResource(R.drawable.bg_status_pending);
            holder.txtStatus.setTextColor(Color.parseColor("#2563EB"));
        }

        // Hide cancel order button on admin side (admin uses status update dialog)
        if (holder.btnCancelOrder != null) {
            holder.btnCancelOrder.setVisibility(View.GONE);
        }

        // ☎️ CALL USER BUTTON
        holder.btnCallUser.setVisibility(View.VISIBLE);
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

        // 🔥 Card click (status update dialog)
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
        LinearLayout layoutOrderItemsContainer;
        Button btnCallUser, btnCancelOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderId   = itemView.findViewById(R.id.txtOrderId);
            txtItems     = itemView.findViewById(R.id.txtOrderItems);
            txtTotal     = itemView.findViewById(R.id.txtOrderTotal);
            txtDateTime  = itemView.findViewById(R.id.txtOrderDateTime);
            txtStatus    = itemView.findViewById(R.id.txtOrderStatus);
            txtContact   = itemView.findViewById(R.id.txtContact);
            btnCallUser  = itemView.findViewById(R.id.btnCallUser);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
            layoutOrderItemsContainer = itemView.findViewById(R.id.layoutOrderItemsContainer);
        }
    }
}
