package com.example.unicanteen.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unicanteen.R;
import com.example.unicanteen.adapters.AdminItemsAdapter;
import com.example.unicanteen.models.AdminMenuItemModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminManageItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExtendedFloatingActionButton fabAdd;
    private final List<AdminMenuItemModel> itemList = new ArrayList<>();
    private AdminItemsAdapter adapter;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_items);

        initViews();

        // Firebase reference
        ref = FirebaseDatabase.getInstance().getReference("menuItems");

        loadItems();

        fabAdd.setOnClickListener(v -> showAddDialog());
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerAdminItems);
        fabAdd = findViewById(R.id.fabAddItem);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminItemsAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }

    // ================= LOAD ITEMS =================
    private void loadItems() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    AdminMenuItemModel item = ds.getValue(AdminMenuItemModel.class);
                    if (item != null) {
                        itemList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AdminManageItemsActivity.this, "Failed to load items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ================= ADD ITEM DIALOG (FIXED) =================
    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);

        EditText etName = view.findViewById(R.id.etItemName);
        EditText etPrice = view.findViewById(R.id.etItemPrice);
        AutoCompleteTextView etFloor = view.findViewById(R.id.etItemFloor);

        // Custom Buttons from your XML
        Button btnAddAction = view.findViewById(R.id.btnAddItemAction);
        Button btnCancelAction = view.findViewById(R.id.btnCancelAction);

        // FLOORS 1–6
        String[] floors = {"Floor 1", "Floor 2", "Floor 3", "Floor 4", "Floor 5", "Floor 6"};
        ArrayAdapter<String> floorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, floors);
        etFloor.setAdapter(floorAdapter);
        etFloor.setOnClickListener(v -> etFloor.showDropDown());

        // Create Dialog without default buttons to avoid "Double Buttons"
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        // Handle "Add to Menu" Button Click
        btnAddAction.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String floor = etFloor.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty() || floor.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int price;
            try {
                price = Integer.parseInt(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
                return;
            }

            String id = ref.push().getKey();
            if (id != null) {
                AdminMenuItemModel item = new AdminMenuItemModel(id, name, price, floor);
                ref.child(id).setValue(item).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss(); // Close dialog on success
                    }
                });
            }
        });

        // Handle "Cancel" Button Click
        btnCancelAction.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}