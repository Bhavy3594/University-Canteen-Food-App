# 🍔 University Canteen Food App

> **Smart Campus Food Ordering & Canteen Management Native Android Application**

A feature-packed native Android application written in **Java** and powered by **Firebase**, designed to digitize university canteen ordering, eliminate rush-hour queue wait times, enable persistent cart management, and provide real-time order status tracking for students and canteen staff.

---

## 📌 Overview

Traditional university canteens face severe rush-hour congestion, delayed order fulfillment, and manual paper-based order management. **University Canteen Food App** solves this problem by providing a modern mobile digital ordering workflow:
- **For Students:** Browse interactive categorized menus (Breakfast, Snacks, Meals, Beverages), customize cart quantities, select floor delivery/pickup points, place orders, and track live order fulfillment statuses in real time.
- **For Canteen Admin:** Dedicated management interface to add/edit menu items, update pricing, process incoming customer orders, and update fulfillment stages (Pending ➔ Preparing ➔ Ready for Pickup ➔ Delivered).

---

## ✨ Key Features

- 📜 **Interactive Food Menu:** Filter items by category (Breakfast, Snacks, Drinks), view pricing, and detailed item descriptions.
- 🛒 **Persistent Shopping Cart:** Dynamic quantity adjustment, total price calculation, floor selection, and order checkout managed by `CartManager.java`.
- 💳 **Flexible Payment Workflows:** Multi-payment mode simulation (UPI, Credit/Debit Card, Cash on Delivery).
- 📍 **Campus Location & Floor Selection:** Custom floor location dialogs (`dialog_select_floor.xml`) for targeted campus delivery and pickup routing.
- ⚡ **Real-Time Order Sync:** Firebase-driven live status synchronization (Pending, Preparing, Ready, Delivered, Cancelled) with historical order logs.
- 🛠️ **Canteen Staff Admin Portal:** Comprehensive administrative dashboard (`AdminDashboardActivity.java`) to add, edit, or disable menu items and manage live order queues.

---

## 🛠️ Tech Stack & Architecture

### **Technology Stack**
- **Language:** Java (JDK 17)
- **Framework / SDK:** Android SDK (API Level 24+ / Android 7.0+)
- **Cloud Database:** Firebase Realtime Database (`FirebaseDatabase`)
- **Authentication:** Firebase Authentication (`FirebaseAuth`)
- **UI & Layouts:** XML Layouts, Material Design Components, Custom Dialogs
- **Build System:** Gradle (Kotlin DSL - `build.gradle.kts`)

### **Software Architecture**
The application implements an Android client architecture integrated with Firebase cloud synchronization:
```
[ XML Activities & Adapters ]  <--->  [ Local State / CartManager ]  <--->  [ Firebase Realtime DB & Auth ]
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
│   │   │   │   ├── adapters/       # MenuAdapter, CartAdapter, UserOrdersAdapter, AdminOrderAdapter
│   │   │   │   ├── models/         # MenuItemModel, CartItemModel, OrderModel, AdminMenuItemModel
│   │   │   │   └── utils/          # CartManager, OrderManager, OrderHistoryManager, ImageUtils
│   │   │   ├── res/
│   │   │   │   ├── layout/         # UI XML Activity & Item Row definitions
│   │   │   │   ├── drawable/       # Custom XML vector drawables & status badges
│   │   │   │   └── values/         # Colors, Strings, and Material Themes
│   │   │   └── AndroidManifest.xml
│   │   └── google-services.json.example
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
   git clone https://github.com/BhavyAtkotiya/University-Canteen-Food-App.git
   ```
2. **Configure Firebase Credentials:**
   - Copy `app/google-services.json.example` to `app/google-services.json`.
   - Add your Firebase project credentials into `app/google-services.json`.
3. **Open in Android Studio:**
   - Launch Android Studio and click **Open**.
   - Select the cloned `University-Canteen-Food-App` directory.
4. **Sync Gradle:**
   - Allow Gradle to sync dependencies automatically.
5. **Run on Emulator / Physical Device:**
   - Select an Android Virtual Device (AVD) or connect a physical Android device via USB debugging.
   - Click the green **Run ▶** button (or press `Shift + F10`).

---

## 📄 License

This project is open-source software licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.
