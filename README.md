# 🍔 University Canteen Food App

> **Smart Campus Food Ordering & Canteen Management Native Android Application**

A feature-packed native Android application written in **Java** designed to digitize university canteen ordering, eliminate long queue wait times, enable seamless cart persistence, and provide real-time order status management for students and canteen staff.

---

## 📌 Overview

Traditional university canteens face severe rush-hour congestion, delayed order fulfillment, and manual paper-based order management. **University Canteen Food App** solves this problem by providing a modern mobile digital ordering workflow:
- **For Students:** Browse interactive categorized menus (Breakfast, Snacks, Meals, Beverages), customize cart quantities, select floor delivery/pickup points, and track live order fulfillment statuses.
- **For Canteen Admin:** Dedicated management interface to update food item availability, modify pricing, process incoming customer orders, and update fulfillment stages (Pending ➔ Preparing ➔ Ready for Pickup ➔ Delivered).

---

## ✨ Key Features

- 📜 **Interactive Food Menu:** Filter items by category (Breakfast, Snacks, Drinks), view dietary badges (Veg/Non-Veg), pricing, and detailed item descriptions.
- 🛒 **Persistent Shopping Cart:** Dynamic quantity adjustment, total price calculation, floor selection, and order checkout.
- 💳 **Flexible Payment Workflows:** Multi-payment mode simulation (UPI, Credit/Debit Card, Cash on Delivery).
- 📍 **Campus Location & Floor Selection:** Custom floor location dialogs (`dialog_select_floor.xml`) for targeted campus delivery and pickup routing.
- ⚡ **Real-Time Order Tracking:** View order status badges (Pending, Preparing, Ready, Delivered, Cancelled) with historical order logs.
- 🛠️ **Canteen Staff Admin Portal:** Comprehensive administrative dashboard to add, edit, or disable menu items, view total revenue stats, and manage live order queues.

---

## 🛠️ Tech Stack & Architecture

### **Technology Stack**
- **Language:** Java (JDK 17)
- **Framework / SDK:** Android SDK (API Level 24+ / Android 7.0+)
- **Database:** Local SQLite (`SQLiteOpenHelper`) for transactional local state
- **UI & Layouts:** XML Layouts, Material Design Components, Custom Dialogs
- **Build System:** Gradle (Kotlin DSL - `build.gradle.kts`)

### **Software Architecture**
The application adheres to a clean **MVC (Model-View-Controller)** Android architectural design:
```
[ XML Layouts / Activities ]  <--->  [ Adapters & View Controllers ]  <--->  [ Local SQLite Database Helper ]
```

---

## 📂 Project Structure

```
University-Canteen-Food-App/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/unicanteen/
│   │   │   │   ├── activities/     # Main, Menu, Cart, Payment, AdminDashboard, OrderHistory
│   │   │   │   ├── adapters/       # MenuAdapter, CartAdapter, OrderAdapter
│   │   │   │   ├── database/       # DatabaseHelper (SQLite CRUD operations)
│   │   │   │   └── models/         # FoodItem, CartItem, Order, User
│   │   │   ├── res/
│   │   │   │   ├── layout/         # UI XML Activity & Item Row definitions
│   │   │   │   ├── drawable/       # Custom XML vector drawables & status badges
│   │   │   │   └── values/         # Colors, Strings, and Material Themes
│   │   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🚀 Installation & Setup

### Prerequisites
- [Android Studio Electric Eel (or newer)](https://developer.android.com/studio)
- Android SDK version 24 or higher
- Java Development Kit (JDK 17+)

### Steps
1. **Clone the repository:**
   ```bash
   git clone https://github.com/Bhavy3594/University-Canteen-Food-App.git
   ```
2. **Open in Android Studio:**
   - Launch Android Studio and click **Open**.
   - Select the cloned `University-Canteen-Food-App` directory.
3. **Sync Gradle:**
   - Allow Gradle to sync dependencies automatically (`gradle/libs.versions.toml`).
4. **Run on Emulator / Physical Device:**
   - Select an Android Virtual Device (AVD) or connect a physical Android device via USB debugging.
   - Click the green **Run ▶** button (or press `Shift + F10`).

---

## 🔮 Future Improvements

- 🔔 **Firebase Cloud Messaging (FCM):** Push notifications when order status changes to "Ready for Pickup".
- 💸 **Razorpay / Stripe SDK Integration:** Real gateway payments for live production deployments.
- 📊 **Analytics Dashboard:** Graphical daily revenue and top-selling food item reports for management.

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.
