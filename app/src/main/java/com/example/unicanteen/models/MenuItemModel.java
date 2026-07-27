package com.example.unicanteen.models;

import com.example.unicanteen.utils.ImageUtils;

public class MenuItemModel {

    private String name;
    private int price;
    private String imageUrl;
    private String category;
    private String description;
    private String rating;
    private boolean isVeg = true;
    
    // Required empty constructor for Firebase mapping
    public MenuItemModel() {}

    // Maintaining existing 2-arg constructor for fallback
    public MenuItemModel(String name, int price) {
        this.name = name;
        this.price = price;
        this.imageUrl = ImageUtils.getImageUrl(name, null);
        this.category = ImageUtils.getCategory(name);
        this.description = "Freshly prepared campus " + name;
        this.rating = "4.8 ★";
        this.isVeg = true;
    }

    // Full constructor
    public MenuItemModel(String name, int price, String imageUrl, String category, String description, String rating, boolean isVeg) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.description = description;
        this.rating = rating;
        this.isVeg = isVeg;
    }

    // 🔹 Getters
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            return imageUrl.trim();
        }
        return ImageUtils.getImageUrl(name, null);
    }

    public String getCategory() {
        return category != null && !category.isEmpty() ? category : ImageUtils.getCategory(name);
    }

    public String getDescription() {
        return description != null && !description.isEmpty() ? description : "Freshly prepared campus " + name;
    }

    public String getRating() {
        return rating != null && !rating.isEmpty() ? rating : "4.8 ★";
    }

    public boolean isVeg() {
        return isVeg;
    }

    // 🔹 Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setVeg(boolean veg) {
        isVeg = veg;
    }
}