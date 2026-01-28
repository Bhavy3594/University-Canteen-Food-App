package com.example.unicanteen.models;

public class AdminMenuItemModel {

    // 🔹 PUBLIC FIELDS (Firebase auto-mapping + existing code safe)
    public String id;
    public String name;
    public int price;
    public String floor;   // 🔥 VERY IMPORTANT (for floor-wise menu)

    // 🔹 REQUIRED EMPTY CONSTRUCTOR (Firebase needs this)
    public AdminMenuItemModel() {
    }

    // 🔹 FULL CONSTRUCTOR (Admin add / edit use)
    public AdminMenuItemModel(String id, String name, int price, String floor) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.floor = floor;
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
}
