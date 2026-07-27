package com.example.unicanteen.utils;

import android.content.Context;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.Map;

public class ImageUtils {

    private static final Map<String, String> FOOD_IMAGE_MAP = new HashMap<>();
    private static final Map<String, String> FOOD_CATEGORY_MAP = new HashMap<>();

    static {
        // High-resolution Unsplash Food CDN images (Default Canteen Catalog Fallback)
        FOOD_IMAGE_MAP.put("samosa", "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=600&auto=format&fit=crop&q=80");
        FOOD_IMAGE_MAP.put("tea", "https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=600&auto=format&fit=crop&q=80");
        FOOD_IMAGE_MAP.put("chai", "https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=600&auto=format&fit=crop&q=80");
        FOOD_IMAGE_MAP.put("burger", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600&auto=format&fit=crop&q=80");
        FOOD_IMAGE_MAP.put("cold coffee", "https://images.unsplash.com/photo-1517701604599-bb29b565090c?w=600&auto=format&fit=crop&q=80");
        FOOD_IMAGE_MAP.put("pizza", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600&auto=format&fit=crop&q=80");
        FOOD_IMAGE_MAP.put("gujarati thali", "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=600&auto=format&fit=crop&q=80");
        FOOD_IMAGE_MAP.put("thali", "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=600&auto=format&fit=crop&q=80");
        FOOD_IMAGE_MAP.put("dosa", "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=600&auto=format&fit=crop&q=80");
        FOOD_IMAGE_MAP.put("noodles", "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=600&auto=format&fit=crop&q=80");
        FOOD_IMAGE_MAP.put("sandwich", "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=600&auto=format&fit=crop&q=80");
        FOOD_IMAGE_MAP.put("fries", "https://images.unsplash.com/photo-1576107232684-1279f390859f?w=600&auto=format&fit=crop&q=80");
        FOOD_IMAGE_MAP.put("pasta", "https://images.unsplash.com/photo-1621996346565-e3d5d6281286?w=600&auto=format&fit=crop&q=80");
        FOOD_IMAGE_MAP.put("coffee", "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=600&auto=format&fit=crop&q=80");

        FOOD_CATEGORY_MAP.put("samosa", "Snacks");
        FOOD_CATEGORY_MAP.put("tea", "Beverages");
        FOOD_CATEGORY_MAP.put("chai", "Beverages");
        FOOD_CATEGORY_MAP.put("burger", "Fast Food");
        FOOD_CATEGORY_MAP.put("cold coffee", "Beverages");
        FOOD_CATEGORY_MAP.put("pizza", "Italian");
        FOOD_CATEGORY_MAP.put("gujarati thali", "Main Course");
        FOOD_CATEGORY_MAP.put("thali", "Main Course");
        FOOD_CATEGORY_MAP.put("dosa", "South Indian");
        FOOD_CATEGORY_MAP.put("noodles", "Asian");
    }

    // 🔥 CRITICAL FIX: If a customUrl is provided (uploaded image / camera / gallery / web URL), ALWAYS PRESERVE IT!
    public static String getImageUrl(String itemName, String customUrl) {
        if (customUrl != null && !customUrl.trim().isEmpty()) {
            return customUrl.trim();
        }

        if (itemName == null) return "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&auto=format&fit=crop&q=80";

        String key = itemName.trim().toLowerCase();
        for (Map.Entry<String, String> entry : FOOD_IMAGE_MAP.entrySet()) {
            if (key.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&auto=format&fit=crop&q=80";
    }

    public static String getCategory(String itemName) {
        if (itemName == null) return "Canteen Special";

        String key = itemName.trim().toLowerCase();
        for (Map.Entry<String, String> entry : FOOD_CATEGORY_MAP.entrySet()) {
            if (key.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "Canteen Special";
    }

    /**
     * Centralized Image Loader for rendering network URLs, local content URIs, and Base64 images.
     */
    public static void loadImage(Context context, String imageUrl, ImageView imageView, int placeholderRes) {
        if (context == null || imageView == null) return;

        if (imageUrl != null && imageUrl.startsWith("data:image/")) {
            try {
                int commaIndex = imageUrl.indexOf(",");
                if (commaIndex != -1) {
                    String base64Data = imageUrl.substring(commaIndex + 1);
                    byte[] imageBytes = Base64.decode(base64Data, Base64.DEFAULT);
                    Glide.with(context)
                            .load(imageBytes)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(placeholderRes)
                            .error(placeholderRes)
                            .into(imageView);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .into(imageView);
    }
}
