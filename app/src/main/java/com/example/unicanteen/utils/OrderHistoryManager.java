package com.example.unicanteen.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.unicanteen.models.CartItemModel;
import com.example.unicanteen.models.OrderModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderHistoryManager {

    private static final String PREF_NAME = "order_history";

    // ✅ SAVE ORDER (Maintaining your SharedPreferences and UUID logic)
    public static void saveOrder(Context context, List<CartItemModel> cart, int total) {

        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        StringBuilder items = new StringBuilder();
        for (CartItemModel c : cart) {
            items.append(c.getName())
                    .append(" x ")
                    .append(c.getQuantity())
                    .append(", ");
        }

        // Logic preserved: Creating a short 8-character unique ID
        String id = UUID.randomUUID().toString().substring(0, 8);

        pref.edit()
                .putString(id, items.toString() + "|" + total)
                .apply();
    }

    // ✅ GET ORDERS (Maintaining your split logic and dummy dateTime flow)
    public static List<OrderModel> getOrders(Context context) {

        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<OrderModel> list = new ArrayList<>();

        for (String key : pref.getAll().keySet()) {
            String value = pref.getString(key, "");
            if (value == null || value.isEmpty()) continue;

            // Logic preserved: splitting string by "|" pipe symbol
            String[] data = value.split("\\|");

            // ✅ MAINTAINING YOUR 4-PARAMETER CONSTRUCTOR FIX
            OrderModel order = new OrderModel(
                    key,                        // orderId
                    data[0],                    // summary
                    Integer.parseInt(data[1]),  // totalAmount
                    ""                          // dateTime dummy (local history)
            );

            list.add(order);
        }
        return list;
    }
}