package com.example.unicanteen.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unicanteen.R;
import com.example.unicanteen.models.AdminMenuItemModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminItemsAdapter
        extends RecyclerView.Adapter<AdminItemsAdapter.ViewHolder> {

    private final List<AdminMenuItemModel> itemList;

    private final DatabaseReference ref =
            FirebaseDatabase.getInstance().getReference("menuItems");

    public AdminItemsAdapter(List<AdminMenuItemModel> itemList) {
        this.itemList = itemList;
    }

    // ================= CREATE VIEW =================
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_admin_item, parent, false);

        return new ViewHolder(view);
    }

    // ================= BIND DATA =================
    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder, int position) {

        AdminMenuItemModel item = itemList.get(position);

        // ✅ ITEM NAME (null-safe)
        holder.txtItemName.setText(
                item.name != null ? item.name : "Unnamed Item"
        );

        // ✅ FLOOR NULL SAFE
        String floorText = (item.floor != null && !item.floor.isEmpty())
                ? item.floor
                : "Floor N/A";

        // ✅ PRICE + FLOOR
        holder.txtItemPrice.setText(
                "₹ " + item.price + " • " + floorText
        );

        // ✏️ EDIT ITEM
        holder.btnEdit.setOnClickListener(v ->
                showEditDialog(v.getContext(), item)
        );

        // 🗑 DELETE ITEM
        holder.btnDelete.setOnClickListener(v ->
                showDeleteDialog(v.getContext(), item)
        );
    }

    // ================= EDIT DIALOG =================
    private void showEditDialog(Context context, AdminMenuItemModel item) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_add_item, null);

        EditText etName = view.findViewById(R.id.etItemName);
        EditText etPrice = view.findViewById(R.id.etItemPrice);
        AutoCompleteTextView etFloor = view.findViewById(R.id.etItemFloor);

        etName.setText(item.name);
        etPrice.setText(String.valueOf(item.price));

        if (item.floor != null) {
            etFloor.setText(item.floor, false);
        }

        // ✅ FLOORS 1–6
        String[] floors = {
                "Floor 1",
                "Floor 2",
                "Floor 3",
                "Floor 4",
                "Floor 5",
                "Floor 6"
        };

        ArrayAdapter<String> floorAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1,
                floors
        );
        etFloor.setAdapter(floorAdapter);

        new AlertDialog.Builder(context)
                .setTitle("Edit Item")
                .setView(view)
                .setPositiveButton("Update", (d, w) -> {

                    String name = etName.getText().toString().trim();
                    String priceStr = etPrice.getText().toString().trim();
                    String floor = etFloor.getText().toString().trim();

                    if (name.isEmpty() || priceStr.isEmpty() || floor.isEmpty()) {
                        Toast.makeText(context,
                                "Please fill all fields",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int price;
                    try {
                        price = Integer.parseInt(priceStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(context,
                                "Invalid price",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 🔥 UPDATE FIREBASE
                    ref.child(item.id).child("name").setValue(name);
                    ref.child(item.id).child("price").setValue(price);
                    ref.child(item.id).child("floor").setValue(floor);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ================= DELETE DIALOG =================
    private void showDeleteDialog(Context context, AdminMenuItemModel item) {

        new AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", (d, w) ->
                        ref.child(item.id).removeValue()
                )
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ================= VIEW HOLDER =================
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName, txtItemPrice;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItemName = itemView.findViewById(R.id.txtItemName);
            txtItemPrice = itemView.findViewById(R.id.txtItemPriceDisplay);
            btnEdit = itemView.findViewById(R.id.btnEditItem);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}
