package com.example.unicanteen.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unicanteen.R;
import com.example.unicanteen.models.CartItemModel;
import com.example.unicanteen.utils.CartManager;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<CartItemModel> cartList;
    private final Runnable updateTotalCallback;

    // ✅ CONSTRUCTOR (Maintaining your logic for total updates)
    public CartAdapter(List<CartItemModel> cartList, Runnable updateTotalCallback) {
        this.cartList = cartList;
        this.updateTotalCallback = updateTotalCallback;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Using your specific row layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemModel item = cartList.get(position);

        // Binding data as per your model fields
        holder.txtName.setText(item.getName());
        holder.txtPrice.setText("₹ " + item.getPrice());
        holder.txtQty.setText(String.valueOf(item.getQuantity()));

        // ➕ PLUS BUTTON (Maintaining quantity increment logic)
        holder.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            if (updateTotalCallback != null) {
                updateTotalCallback.run();
            }
        });

        // ➖ MINUS BUTTON (Maintaining removal and decrement logic)
        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
            } else {
                // Removing item if quantity is less than 1
                CartManager.removeItem(item);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartList.size());
            }
            if (updateTotalCallback != null) {
                updateTotalCallback.run();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    // 🔹 VIEW HOLDER (Matching your premium XML IDs)
    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQty;
        Button btnPlus, btnMinus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtCartName);
            txtPrice = itemView.findViewById(R.id.txtCartPrice);
            txtQty = itemView.findViewById(R.id.txtCartQty);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}