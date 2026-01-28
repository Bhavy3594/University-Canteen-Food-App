package com.example.unicanteen.models;

public class OrderModel {

    // 🔐 CORE ORDER DATA (Logic & Flow Preserved)
    private String orderId;
    private String summary;
    private int totalAmount;
    private String dateTime;
    private String status;

    // 📍 EXTRA DETAILS (Maintaining your structure for Admin/User sides)
    private String location;
    private String contact;
    private String payment;
    private String userId;

    // ✅ EMPTY CONSTRUCTOR (MANDATORY FOR FIREBASE)
    public OrderModel() {}

    // ✅ COMPATIBILITY CONSTRUCTOR (Maintaining your exact logic for old code support)
    public OrderModel(String orderId, String summary, int totalAmount, String dateTime) {
        this.orderId = orderId;
        this.summary = summary;
        this.totalAmount = totalAmount;
        this.dateTime = dateTime;
        this.status = "Pending"; // Defaulting as per your logic

        // Defaults preserved
        this.location = null;
        this.contact = null;
        this.payment = null;
        this.userId = null;
    }

    // 🔹 GETTERS (Maintaining access for all Adapters and Activities)
    public String getOrderId() { return orderId; }
    public String getSummary() { return summary; }
    public int getTotalAmount() { return totalAmount; }
    public String getDateTime() { return dateTime; }
    public String getStatus() { return status; }
    public String getLocation() { return location; }
    public String getContact() { return contact; }
    public String getPayment() { return payment; }
    public String getUserId() { return userId; }

    // 🔹 SETTERS (Logic required by Firebase mapping remains untouched)
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public void setStatus(String status) { this.status = status; }
    public void setLocation(String location) { this.location = location; }
    public void setContact(String contact) { this.contact = contact; }
    public void setPayment(String payment) { this.payment = payment; }
    public void setUserId(String userId) { this.userId = userId; }
}