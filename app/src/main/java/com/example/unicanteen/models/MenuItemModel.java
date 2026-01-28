package com.example.unicanteen.models;

public class MenuItemModel {

    private String name;
    private int price;

    // Maintaining your exact 2-argument constructor (Logic Preserved)
    public MenuItemModel(String name, int price) {
        this.name = name;
        this.price = price;
    }

    // 🔹 Getter - Name
    public String getName() {
        return name;
    }

    // 🔹 Getter - Price
    public int getPrice() {
        return price;
    }
}