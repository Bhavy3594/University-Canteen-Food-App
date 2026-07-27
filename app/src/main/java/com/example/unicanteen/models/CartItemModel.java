package com.example.unicanteen.models;

import com.example.unicanteen.utils.ImageUtils;

public class CartItemModel {

    private String name;
    private int price;
    private int quantity;
    private String imageUrl;
    private String category;
    private boolean isVeg = true;

    // Default constructor for Firebase or serialization consistency
    public CartItemModel() {
    }

    // 🔹 Maintaining exact 3-argument Constructor for fallback
    public CartItemModel(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = ImageUtils.getImageUrl(name, null);
        this.category = ImageUtils.getCategory(name);
        this.isVeg = true;
    }

    public CartItemModel(String name, int price, int quantity, String imageUrl) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.category = ImageUtils.getCategory(name);
        this.isVeg = true;
    }

    // 🔹 Getter & Setter - Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 🔹 Getter & Setter - Price
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    // 🔹 Getter & Setter - Quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // 🔹 Getter & Setter - Image & Metadata
    public String getImageUrl() {
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            return imageUrl.trim();
        }
        return ImageUtils.getImageUrl(name, null);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category != null && !category.isEmpty() ? category : ImageUtils.getCategory(name);
    }

    public boolean isVeg() {
        return isVeg;
    }
}