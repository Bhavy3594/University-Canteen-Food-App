# University Canteen Food App

> **UniCanteen** - A modern, high-performance Android application designed for campus dining, express food ordering, and administrative canteen management.

[![Android SDK](https://img.shields.io/badge/Android%20SDK-34-059669.svg?style=flat-square&logo=android)](https://developer.android.com)
[![Java](https://img.shields.io/badge/Language-Java-10B981.svg?style=flat-square&logo=openjdk)](https://www.java.com)
[![Material Design 3](https://img.shields.io/badge/UI-Material%20Design%203-06B6D4.svg?style=flat-square&logo=materialdesign)](https://m3.material.io)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-F59E0B.svg?style=flat-square&logo=firebase)](https://firebase.google.com)

---

## 📌 Overview

**University Canteen Food App** connects students, faculty, and university staff with multi-floor campus canteens. It provides seamless mobile food ordering, express pickup/delivery options, real-time order status tracking, and a unified control center for canteen administrators to manage menu items and active orders.

---

## ✨ Key Features

### 👤 User Features
- **Unified Authentication**: Single Sign In screen for both regular Users and Admins with automatic role determination.
- **Multi-Floor Canteen Menu**: Browse curated menus organized by campus floor locations.
- **Interactive Shopping Cart**: Dynamic item quantity modification and instant price calculation.
- **Flexible Payment Methods**: Support for UPI, Credit/Debit Cards, and Cash on Delivery.
- **Real-Time Order Tracking**: Live status updates (*Pending*, *Preparing*, *Ready*, *Delivered*) with order history timeline.

### 🔐 Admin Features
- **Admin Control Center**: Executive dashboard for managing active orders and canteen menu inventory.
- **Menu Item Management**: Add, update, and remove menu items with custom pricing, categories, and availability toggles.
- **Live Order Management**: Update order statuses in real time to notify campus customers instantly.
- **Direct Phone Contact**: Quick-dial feature to contact customers regarding order updates.

---

## 🛠️ Technology Stack

- **Platform**: Android Native (Gradle / AndroidX)
- **Language**: Java
- **UI Framework**: Material Design 3 (Obsidian & Emerald Glow Theme)
- **Backend & Database**: Firebase Realtime Database
- **Authentication**: Firebase Authentication
- **Media & Assets**: Android VectorDrawables & High-Resolution PNG Assets

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (or newer) / JDK 17+
- Android SDK Tooling (API Level 34 / Android 14)
- Physical device or Android Emulator (Android 7.0 / API 24 minimum)

### Installation & Build

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Bhavy3594/University-Canteen-Food-App.git
   cd "University-Canteen-Food-App"
   ```

2. **Open in Android Studio**:
   - Open Android Studio and select **Open**.
   - Navigate to the cloned project folder and open it.

3. **Build & Run**:
   - Sync Project with Gradle Files.
   - Run on your device or emulator via **Run 'app'** (`Shift + F10`).

---

## 📂 Project Structure

```
UniCanteen/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/unicanteen/
│   │   │   │   ├── activities/       # Screen Activities (Splash, Main, Dashboards, Orders)
│   │   │   │   ├── adapters/         # RecyclerView Adapters (Menu, Cart, Orders)
│   │   │   │   ├── models/           # Data Models (OrderModel, MenuItemModel)
│   │   │   │   └── utils/            # Utilities & Helper Classes
│   │   │   └── res/                  # UI Layouts, Vector Drawables, Values & Assets
│   └── build.gradle.kts              # Application Build Configuration
├── README.md                         # Project Documentation
└── settings.gradle.kts               # Gradle Project Settings
```

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
