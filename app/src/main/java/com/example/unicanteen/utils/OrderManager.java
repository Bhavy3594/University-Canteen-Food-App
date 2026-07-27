package com.example.unicanteen.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderManager {

    // ✅ CANCEL ORDER (Fixed: Updates both user history and admin master orders)
    public static void cancelOrder(String orderId) {

        if (orderId == null || orderId.isEmpty()) return;

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        // 1. Update global orders tree for Admin
        rootRef.child("orders")
                .child(orderId)
                .child("status")
                .setValue("Cancelled");

        // 2. Update user's personal orders tree
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            rootRef.child("userOrders")
                    .child(uid)
                    .child(orderId)
                    .child("status")
                    .setValue("Cancelled");
        }
    }
}