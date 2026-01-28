package com.example.unicanteen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unicanteen.R;
import com.example.unicanteen.models.CartItemModel;
import com.example.unicanteen.models.MenuItemModel;
import com.example.unicanteen.utils.CartManager;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private final Context context;
    private final List<MenuItemModel> list;

    public MenuAdapter(Context context, List<MenuItemModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Using your specific row layout for menu items
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItemModel item = list.get(position);

        // Binding data as per your model
        holder.txtName.setText(item.getName());
        holder.txtPrice.setText("₹" + item.getPrice());

        // ✅ ADD TO CART LOGIC (Maintaining your exact logic and flow)
        holder.btnAddToCart.setOnClickListener(v -> {

            // Maintaining your 3-argument constructor logic
            CartItemModel cartItem = new CartItemModel(item.getName(), item.getPrice(), 1);

            // Adding to cart via your CartManager utility
            CartManager.addToCart(cartItem);

            Toast.makeText(context,
                    item.getName() + " added to cart",
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // 🔹 VIEW HOLDER (Matching your XML IDs for the menu row)
    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        Button btnAddToCart;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}