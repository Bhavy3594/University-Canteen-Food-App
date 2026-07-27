package com.example.unicanteen;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class UniCanteenApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            // 🔥 Enable offline disk persistence for instant app startup data loading
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            
            // 🔥 Keep menuItems continuously synced in background
            FirebaseDatabase.getInstance().getReference("menuItems").keepSynced(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
