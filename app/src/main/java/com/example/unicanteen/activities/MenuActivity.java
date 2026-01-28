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
import com.example.unicanteen.adapters.MenuAdapter;
import com.example.unicanteen.models.AdminMenuItemModel;
import com.example.unicanteen.models.MenuItemModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvFloorHeader;
    private Button btnCart, btnOrderHistory;

    private final List<MenuItemModel> menuList = new ArrayList<>();
    private MenuAdapter adapter;

    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initViews();

        String floor = getIntent().getStringExtra("floor");

        if (floor == null) {
            Toast.makeText(this, "Floor not selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvFloorHeader.setText(floor + " Menu");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MenuAdapter(this, menuList);
        recyclerView.setAdapter(adapter);

        // 🔹 1️⃣ ADD DEFAULT ITEMS (YOUR EXISTING LOGIC)
        addDefaultItems(floor);

        // 🔹 2️⃣ LOAD ADMIN ITEMS FROM FIREBASE
        ref = FirebaseDatabase.getInstance().getReference("menuItems");
        loadAdminItems(floor);

        btnCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        btnOrderHistory.setOnClickListener(v ->
                startActivity(new Intent(this, OrderHistoryActivity.class)));
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerMenu);
        tvFloorHeader = findViewById(R.id.tvFloorHeader);
        btnCart = findViewById(R.id.btnCart);
        btnOrderHistory = findViewById(R.id.btnOrderHistory);
    }

    // ================= DEFAULT MENU (UNCHANGED) =================
    private void addDefaultItems(String floor) {

        switch (floor) {
            case "Floor 1":
                menuList.add(new MenuItemModel("Samosa", 15));
                menuList.add(new MenuItemModel("Tea", 10));
                break;

            case "Floor 2":
                menuList.add(new MenuItemModel("Burger", 50));
                menuList.add(new MenuItemModel("Cold Coffee", 40));
                break;

            case "Floor 3":
                menuList.add(new MenuItemModel("Pizza", 120));
                break;

            case "Floor 4":
                menuList.add(new MenuItemModel("Gujarati Thali", 90));
                break;

            case "Floor 5":
                menuList.add(new MenuItemModel("Dosa", 60));
                break;

            case "Floor 6":
                menuList.add(new MenuItemModel("Noodles", 80));
                break;
        }
    }

    // ================= ADMIN ITEMS FROM FIREBASE =================
    private void loadAdminItems(String floor) {

        ref.orderByChild("floor")
                .equalTo(floor)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        for (DataSnapshot ds : snapshot.getChildren()) {

                            AdminMenuItemModel adminItem =
                                    ds.getValue(AdminMenuItemModel.class);

                            if (adminItem != null) {
                                // 🔥 ADD ADMIN ITEM TO SAME LIST
                                menuList.add(new MenuItemModel(
                                        adminItem.name,
                                        adminItem.price
                                ));
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(MenuActivity.this,
                                "Failed to load admin items",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
