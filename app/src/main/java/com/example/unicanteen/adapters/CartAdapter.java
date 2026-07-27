package com.example.unicanteen.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.unicanteen.R;
import com.example.unicanteen.models.CartItemModel;
import com.example.unicanteen.utils.CartManager;
import com.example.unicanteen.utils.ImageUtils;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<CartItemModel> cartList;
    private final Runnable updateTotalCallback;

    public CartAdapter(List<CartItemModel> cartList, Runnable updateTotalCallback) {
        this.cartList = cartList;
        this.updateTotalCallback = updateTotalCallback;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemModel item = cartList.get(position);

        holder.txtName.setText(item.getName());
        holder.txtPrice.setText("₹ " + item.getPrice());
        holder.txtQty.setText(String.valueOf(item.getQuantity()));

        // 📷 Image Loading (Supports Network URLs & Base64)
        if (holder.imgCartItem != null) {
            ImageUtils.loadImage(holder.itemView.getContext(), item.getImageUrl(), holder.imgCartItem, R.drawable.ic_food_placeholder);
        }

        // ➕ PLUS BUTTON
        holder.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            if (updateTotalCallback != null) {
                updateTotalCallback.run();
            }
        });

        // ➖ MINUS BUTTON
        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
            } else {
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

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQty;
        ImageView imgCartItem;
        Button btnPlus, btnMinus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtCartName);
            txtPrice = itemView.findViewById(R.id.txtCartPrice);
            txtQty = itemView.findViewById(R.id.txtCartQty);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);

            imgCartItem = itemView.findViewById(R.id.imgCartItem);
        }
    }
}