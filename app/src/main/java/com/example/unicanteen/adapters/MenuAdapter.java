package com.example.unicanteen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.unicanteen.R;
import com.example.unicanteen.models.CartItemModel;
import com.example.unicanteen.models.MenuItemModel;
import com.example.unicanteen.utils.CartManager;
import com.example.unicanteen.utils.ImageUtils;

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
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItemModel item = list.get(position);

        holder.txtName.setText(item.getName());
        holder.txtPrice.setText("₹" + item.getPrice());

        if (holder.txtCategory != null) {
            holder.txtCategory.setText(item.getCategory());
        }
        if (holder.txtRating != null) {
            holder.txtRating.setText(item.getRating());
        }
        if (holder.txtDescription != null) {
            holder.txtDescription.setText(item.getDescription());
        }

        // 📷 Image Loading (Supports Network URLs & Base64)
        if (holder.imgItem != null) {
            ImageUtils.loadImage(context, item.getImageUrl(), holder.imgItem, R.drawable.ic_food_placeholder);
        }

        // ✅ ADD TO CART LOGIC
        holder.btnAddToCart.setOnClickListener(v -> {
            CartItemModel cartItem = new CartItemModel(item.getName(), item.getPrice(), 1, item.getImageUrl());
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

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtCategory, txtRating, txtDescription;
        ImageView imgItem, imgVegStatus;
        Button btnAddToCart;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);

            imgItem = itemView.findViewById(R.id.imgItem);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtRating = itemView.findViewById(R.id.txtRating);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            imgVegStatus = itemView.findViewById(R.id.imgVegStatus);
        }
    }
}