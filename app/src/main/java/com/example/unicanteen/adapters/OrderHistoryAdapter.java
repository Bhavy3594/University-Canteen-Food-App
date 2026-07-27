package com.example.unicanteen.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.unicanteen.R;
import com.example.unicanteen.models.CartItemModel;
import com.example.unicanteen.models.OrderModel;
import com.example.unicanteen.utils.ImageUtils;
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
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        holder.txtOrderId.setText(order.getOrderId());
        holder.txtTotal.setText("₹ " + order.getTotalAmount());
        holder.txtOrderDateTime.setText(order.getDateTime() != null ? order.getDateTime() : "");

        String status = order.getStatus() != null ? order.getStatus() : "Pending";
        holder.txtStatus.setText(status);

        // 📞 SHOW CONTACT NUMBER
        if (order.getContact() != null && !order.getContact().isEmpty()) {
            holder.txtContact.setText("📞 " + order.getContact());
            holder.txtContact.setVisibility(View.VISIBLE);
        } else {
            holder.txtContact.setVisibility(View.GONE);
        }

        // 📷 RENDER ORDERED FOOD ITEMS WITH IMAGES
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
                // Fallback for legacy orders: parse text summary and display images via ImageUtils
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

        // 🔥 STATUS BADGE STYLING
        if ("Cancelled".equalsIgnoreCase(status)) {
            holder.txtStatus.setBackgroundResource(R.drawable.bg_status_cancelled);
            holder.txtStatus.setTextColor(Color.parseColor("#DC2626"));
            holder.btnCancel.setVisibility(View.GONE);
        } else if ("Delivered".equalsIgnoreCase(status)) {
            holder.txtStatus.setBackgroundResource(R.drawable.bg_status_delivered);
            holder.txtStatus.setTextColor(Color.parseColor("#059669"));
            holder.btnCancel.setVisibility(View.GONE);
        } else if ("Preparing".equalsIgnoreCase(status) || "Ready".equalsIgnoreCase(status)) {
            holder.txtStatus.setBackgroundResource(R.drawable.bg_status_preparing);
            holder.txtStatus.setTextColor(Color.parseColor("#D97706"));
            holder.btnCancel.setVisibility(View.GONE);
        } else {
            holder.txtStatus.setBackgroundResource(R.drawable.bg_status_pending);
            holder.txtStatus.setTextColor(Color.parseColor("#2563EB"));
            holder.btnCancel.setVisibility(View.VISIBLE);
        }

        // ❌ CANCEL ORDER BUTTON (Modern Custom Confirmation Dialog)
        holder.btnCancel.setOnClickListener(v -> showCancelConfirmationDialog(context, order, position));
    }

    private void showCancelConfirmationDialog(Context context, OrderModel order, int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_cancel_order, null);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        Button btnKeepOrder = dialogView.findViewById(R.id.btnKeepOrder);
        Button btnConfirmCancel = dialogView.findViewById(R.id.btnConfirmCancel);

        btnKeepOrder.setOnClickListener(v -> dialog.dismiss());

        btnConfirmCancel.setOnClickListener(v -> {
            dialog.dismiss();
            OrderManager.cancelOrder(order.getOrderId());
            order.setStatus("Cancelled");
            notifyItemChanged(position);
            Toast.makeText(context, "Order Cancelled", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtItems, txtTotal, txtOrderDateTime, txtStatus, txtContact;
        LinearLayout layoutOrderItemsContainer;
        Button btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtItems = itemView.findViewById(R.id.txtOrderItems);
            txtTotal = itemView.findViewById(R.id.txtOrderTotal);
            txtOrderDateTime = itemView.findViewById(R.id.txtOrderDateTime);
            txtStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtContact = itemView.findViewById(R.id.txtContact);
            btnCancel = itemView.findViewById(R.id.btnCancelOrder);
            layoutOrderItemsContainer = itemView.findViewById(R.id.layoutOrderItemsContainer);
        }
    }
}