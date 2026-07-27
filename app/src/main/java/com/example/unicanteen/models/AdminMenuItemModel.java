package com.example.unicanteen.models;

import com.example.unicanteen.utils.ImageUtils;

public class AdminMenuItemModel {

    // 🔹 PUBLIC FIELDS (Firebase auto-mapping + existing code safe)
    public String id;
    public String name;
    public int price;
    public String floor;   // 🔥 VERY IMPORTANT (for floor-wise menu)
    public String imageUrl;

    // 🔹 REQUIRED EMPTY CONSTRUCTOR (Firebase needs this)
    public AdminMenuItemModel() {
    }

    // Fallback constructor
    public AdminMenuItemModel(String id, String name, int price, String floor) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.floor = floor;
        this.imageUrl = ImageUtils.getImageUrl(name, null);
    }

    public AdminMenuItemModel(String id, String name, int price, String floor, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.floor = floor;
        this.imageUrl = imageUrl;
    }

    // ================= GETTERS =================
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getFloor() {
        return floor;
    }

    public String getImageUrl() {
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            return imageUrl.trim();
        }
        return ImageUtils.getImageUrl(name, null);
    }

    // ================= SETTERS =================
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
