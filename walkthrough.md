# Adding Image Display for Items

I've successfully modified the application to display specific photos for menu items on both the admin and user sides!

## Changes Made:
- **Image Loading Library**: I've integrated `Glide` (v4.16.0) into [app/build.gradle.kts](file:///D:/UniCanteen/app/build.gradle.kts) for smooth and efficient remote image loading.
- **Data Models**: I've modified [AdminMenuItemModel](file:///d:/UniCanteen/app/src/main/java/com/example/unicanteen/models/AdminMenuItemModel.java#3-76), [MenuItemModel](file:///d:/UniCanteen/app/src/main/java/com/example/unicanteen/models/MenuItemModel.java#3-41), and [CartItemModel](file:///d:/UniCanteen/app/src/main/java/com/example/unicanteen/models/CartItemModel.java#3-61) to include an `imageUrl` property. To prevent crashes with older app usage, I also maintained their legacy constructors by defaulting old data sets to display a placeholder image.
- **Admin Item Creation**: I updated the [dialog_add_item.xml](file:///d:/UniCanteen/app/src/main/res/layout/dialog_add_item.xml) layout to include an "Image URL" input box. Now, the [AdminManageItemsActivity.java](file:///d:/UniCanteen/app/src/main/java/com/example/unicanteen/activities/AdminManageItemsActivity.java) parses this URL during item creation and passes it along to the Firebase database.
- **Adapters & Views**:
  - Inserted new `ShapeableImageView` components gracefully spanning the left bounds of [row_admin_item.xml](file:///d:/UniCanteen/app/src/main/res/layout/row_admin_item.xml) (admin view), [row_menu_item.xml](file:///d:/UniCanteen/app/src/main/res/layout/row_menu_item.xml) (user view), and [item_cart.xml](file:///d:/UniCanteen/app/src/main/res/layout/item_cart.xml) (shopping cart view).
  - Wired [AdminItemsAdapter](file:///d:/UniCanteen/app/src/main/java/com/example/unicanteen/adapters/AdminItemsAdapter.java#27-207), [MenuAdapter](file:///d:/UniCanteen/app/src/main/java/com/example/unicanteen/adapters/MenuAdapter.java#23-97), and [CartAdapter](file:///d:/UniCanteen/app/src/main/java/com/example/unicanteen/adapters/CartAdapter.java#20-109) to grab `imageUrl` out of their respective models and paint them securely with placeholder handling using the `Glide` library.

## Validation Results:
- Project successfully built and executed all tasks without encountering regression failures.
- Ensured logic preserved core interactions around floor specification and existing "Add to Cart" functions.

Please restart the application! You can now edit existing items or add new items as Admin, providing a direct image URL for the item (e.g. `https://example.com/masala-dosa-image.png`). The picture will seamlessly show up for both admins and users!
