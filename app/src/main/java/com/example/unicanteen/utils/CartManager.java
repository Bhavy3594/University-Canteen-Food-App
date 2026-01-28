package com.example.unicanteen.utils;

import com.example.unicanteen.models.CartItemModel;
import java.util.ArrayList;
import java.util.List;

public class CartManager {

    // Maintaining your exact static list for app-wide persistence
    private static final List<CartItemModel> cartList = new ArrayList<>();

    // ✅ ADD TO CART (Maintaining your exact duplicate check logic)
    public static void addToCart(CartItemModel item) {
        for (CartItemModel cartItem : cartList) {
            // Logic preserved: if item exists, increment quantity instead of adding new row
            if (cartItem.getName().equals(item.getName())) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                return;
            }
        }
        cartList.add(item);
    }

    // ✅ GET ITEMS
    public static List<CartItemModel> getCartItems() {
        return cartList;
    }

    // ✅ TOTAL CALCULATION (Maintaining Price * Quantity logic)
    public static int getTotalAmount() {
        int total = 0;
        for (CartItemModel item : cartList) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    // ✅ REMOVE ITEM
    public static void removeItem(CartItemModel item) {
        cartList.remove(item);
    }

    // ✅ CLEAR CART (Used after order placement)
    public static void clearCart() {
        cartList.clear();
    }
}