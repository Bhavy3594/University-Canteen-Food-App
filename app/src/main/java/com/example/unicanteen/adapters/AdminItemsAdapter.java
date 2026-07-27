package com.example.unicanteen.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.unicanteen.R;
import com.example.unicanteen.models.AdminMenuItemModel;
import com.example.unicanteen.utils.ImageUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminItemsAdapter
        extends RecyclerView.Adapter<AdminItemsAdapter.ViewHolder> {

    public interface OnItemEditListener {
        void onEdit(AdminMenuItemModel item);
    }

    private final List<AdminMenuItemModel> itemList;
    private final OnItemEditListener editListener;

    private final DatabaseReference ref =
            FirebaseDatabase.getInstance().getReference("menuItems");

    public AdminItemsAdapter(List<AdminMenuItemModel> itemList, OnItemEditListener editListener) {
        this.itemList = itemList;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_admin_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder, int position) {

        AdminMenuItemModel item = itemList.get(position);

        holder.txtItemName.setText(
                item.name != null ? item.name : "Unnamed Item"
        );

        String floorText = (item.floor != null && !item.floor.isEmpty())
                ? item.floor
                : "Floor N/A";

        holder.txtItemPrice.setText(
                "₹ " + item.price + " • " + floorText
        );

        // 📷 Image Loading (Supports Network URLs & Base64)
        if (holder.imgAdminItem != null) {
            ImageUtils.loadImage(holder.itemView.getContext(), item.getImageUrl(), holder.imgAdminItem, R.drawable.ic_food_placeholder);
        }

        // ✏️ EDIT ITEM
        holder.btnEdit.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onEdit(item);
            }
        });

        // 🗑 DELETE ITEM
        holder.btnDelete.setOnClickListener(v ->
                showDeleteDialog(v.getContext(), item)
        );
    }

    private void showDeleteDialog(Context context, AdminMenuItemModel item) {

        new AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", (d, w) -> {
                    if (item != null && item.id != null && !item.id.isEmpty()) {
                        ref.child(item.id).removeValue();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName, txtItemPrice;
        ImageView imgAdminItem;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItemName = itemView.findViewById(R.id.txtItemName);
            txtItemPrice = itemView.findViewById(R.id.txtItemPriceDisplay);
            btnEdit = itemView.findViewById(R.id.btnEditItem);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
            imgAdminItem = itemView.findViewById(R.id.imgAdminItem);
        }
    }
}
