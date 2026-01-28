package com.example.unicanteen.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class OrderManager {

    // ✅ CANCEL ORDER (Maintaining your exact Firebase path and logic)
    public static void cancelOrder(String orderId) {

        // Logic preserved: Getting current user ID for the path
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

        String uid = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();

        // Directly updating the "status" child to "Cancelled"
        FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(uid)
                .child(orderId)
                .child("status")
                .setValue("Cancelled");
    }
}