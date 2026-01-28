package com.example.unicanteen.models;

public class CartItemModel {

    private String name;
    private int price;
    private int quantity;

    // Default constructor for Firebase or serialization consistency
    public CartItemModel() {
    }

    // 🔹 Maintaining your exact 3-argument Constructor (Logic Preserved)
    public CartItemModel(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
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

    // 🔹 Getter & Setter - Quantity (Maintaining your logic for Cart updates)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}