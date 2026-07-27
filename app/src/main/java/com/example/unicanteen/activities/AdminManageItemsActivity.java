package com.example.unicanteen.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unicanteen.R;
import com.example.unicanteen.adapters.AdminItemsAdapter;
import com.example.unicanteen.models.AdminMenuItemModel;
import com.example.unicanteen.utils.ImageUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminManageItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExtendedFloatingActionButton fabAdd;
    private final List<AdminMenuItemModel> itemList = new ArrayList<>();
    private AdminItemsAdapter adapter;
    private DatabaseReference ref;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Camera & Gallery launchers & Permissions
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<String> cameraPermissionLauncher;

    // Active dialog references for image picking
    private ImageView activePreviewImageView;
    private Bitmap selectedBitmap;
    private Uri selectedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_items);

        initViews();
        registerImageLaunchers();

        ref = FirebaseDatabase.getInstance().getReference("menuItems");
        ref.keepSynced(true);

        loadItems();

        fabAdd.setOnClickListener(v -> showAddOrEditDialog(null));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerAdminItems);
        fabAdd = findViewById(R.id.fabAddItem);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new AdminItemsAdapter(itemList, item -> showAddOrEditDialog(item));
        recyclerView.setAdapter(adapter);
    }

    private void registerImageLaunchers() {
        // Camera Permission Launcher
        cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        launchCameraIntent();
                    } else {
                        Toast.makeText(this, "Camera permission is required to capture food photos", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Camera launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent dataIntent = result.getData();
                        Bundle extras = dataIntent.getExtras();
                        Bitmap capturedBitmap = null;
                        if (extras != null) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                capturedBitmap = extras.getParcelable("data", Bitmap.class);
                            } else {
                                @SuppressWarnings("deprecation")
                                Bitmap bitmap = extras.getParcelable("data");
                                capturedBitmap = bitmap;
                            }
                        }
                        if (capturedBitmap != null) {
                            Bitmap finalBitmap = capturedBitmap;
                            executor.execute(() -> {
                                selectedBitmap = scaleBitmap(finalBitmap, 1024);
                                selectedUri = null;
                                runOnUiThread(() -> {
                                    if (activePreviewImageView != null) {
                                        activePreviewImageView.setImageBitmap(selectedBitmap);
                                    }
                                });
                            });
                        } else if (dataIntent.getData() != null) {
                            selectedUri = dataIntent.getData();
                            executor.execute(() -> {
                                selectedBitmap = decodeUriToBitmap(selectedUri);
                                runOnUiThread(() -> {
                                    if (activePreviewImageView != null) {
                                        if (selectedBitmap != null) {
                                            activePreviewImageView.setImageBitmap(selectedBitmap);
                                        } else {
                                            ImageUtils.loadImage(this, selectedUri.toString(), activePreviewImageView, R.drawable.ic_food_placeholder);
                                        }
                                    }
                                });
                            });
                        }
                    }
                }
        );

        // Gallery launcher
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedUri = uri;
                        executor.execute(() -> {
                            selectedBitmap = decodeUriToBitmap(uri);
                            runOnUiThread(() -> {
                                if (activePreviewImageView != null) {
                                    if (selectedBitmap != null) {
                                        activePreviewImageView.setImageBitmap(selectedBitmap);
                                    } else {
                                        ImageUtils.loadImage(this, uri.toString(), activePreviewImageView, R.drawable.ic_food_placeholder);
                                    }
                                }
                            });
                        });
                    }
                }
        );
    }

    private void launchCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            cameraLauncher.launch(takePictureIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to launch camera app", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadItems() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    AdminMenuItemModel item = ds.getValue(AdminMenuItemModel.class);
                    if (item != null) itemList.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AdminManageItemsActivity.this, "Failed to load items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddOrEditDialog(AdminMenuItemModel existingItem) {
        selectedBitmap = null;
        selectedUri = null;

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);

        TextView txtTitle = view.findViewById(R.id.txtDialogTitle);
        TextView txtSubtitle = view.findViewById(R.id.txtDialogSubtitle);
        ImageButton btnClose = view.findViewById(R.id.btnCloseDialog);
        ImageView imgPreview = view.findViewById(R.id.imgItemPreview);
        Button btnCamera = view.findViewById(R.id.btnCamera);
        Button btnGallery = view.findViewById(R.id.btnGallery);
        EditText etName = view.findViewById(R.id.etItemName);
        EditText etPrice = view.findViewById(R.id.etItemPrice);
        AutoCompleteTextView etFloor = view.findViewById(R.id.etItemFloor);
        EditText etImageUrl = view.findViewById(R.id.etItemImageUrl);
        Button btnAddAction = view.findViewById(R.id.btnAddItemAction);
        Button btnCancelAction = view.findViewById(R.id.btnCancelAction);

        activePreviewImageView = imgPreview;

        // Set floor selection listener to open custom modal
        etFloor.setFocusable(false);
        etFloor.setClickable(true);
        etFloor.setOnClickListener(v -> showFloorSelectionDialog(etFloor));

        if (existingItem != null) {
            if (txtTitle != null) txtTitle.setText("Edit Item");
            if (txtSubtitle != null) txtSubtitle.setText("Update product details and pricing");
            etName.setText(existingItem.name);
            etPrice.setText(String.valueOf(existingItem.price));
            if (existingItem.floor != null) etFloor.setText(normalizeFloor(existingItem.floor), false);
            if (existingItem.imageUrl != null && etImageUrl != null) etImageUrl.setText(existingItem.imageUrl);

            if (imgPreview != null) {
                ImageUtils.loadImage(this, existingItem.getImageUrl(), imgPreview, R.drawable.ic_food_placeholder);
            }

            btnAddAction.setText("Save Changes");
        } else {
            if (txtTitle != null) txtTitle.setText("Add New Item");
            if (txtSubtitle != null) txtSubtitle.setText("Fill in product details to update canteen inventory");
            btnAddAction.setText("Add New Product");
        }

        // Camera click with permission check
        if (btnCamera != null) {
            btnCamera.setOnClickListener(v -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    launchCameraIntent();
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
                }
            });
        }

        // Gallery click
        if (btnGallery != null) {
            btnGallery.setOnClickListener(v -> {
                try {
                    galleryLauncher.launch("image/*");
                } catch (Exception e) {
                    Toast.makeText(this, "Gallery not available", Toast.LENGTH_SHORT).show();
                }
            });
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dialog.dismiss());
        }

        btnAddAction.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String rawFloor = etFloor.getText().toString().trim();
            String manualUrl = (etImageUrl != null) ? etImageUrl.getText().toString().trim() : "";

            if (name.isEmpty() || priceStr.isEmpty() || rawFloor.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int price;
            try {
                price = Integer.parseInt(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
                return;
            }

            String floor = normalizeFloor(rawFloor);

            btnAddAction.setEnabled(false);
            processImageAndSave(existingItem, name, price, floor, manualUrl, dialog, btnAddAction);
        });

        if (btnCancelAction != null) {
            btnCancelAction.setOnClickListener(v -> dialog.dismiss());
        }
        dialog.show();
    }

    private void showFloorSelectionDialog(AutoCompleteTextView etFloor) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_floor, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        String currentFloor = etFloor.getText() != null ? etFloor.getText().toString().trim() : "";

        MaterialCardView cardFloor1 = view.findViewById(R.id.cardFloor1);
        MaterialCardView cardFloor2 = view.findViewById(R.id.cardFloor2);
        MaterialCardView cardFloor3 = view.findViewById(R.id.cardFloor3);
        MaterialCardView cardFloor4 = view.findViewById(R.id.cardFloor4);
        MaterialCardView cardFloor5 = view.findViewById(R.id.cardFloor5);
        MaterialCardView cardFloor6 = view.findViewById(R.id.cardFloor6);

        TextView txtCheck1 = view.findViewById(R.id.txtCheckFloor1);
        TextView txtCheck2 = view.findViewById(R.id.txtCheckFloor2);
        TextView txtCheck3 = view.findViewById(R.id.txtCheckFloor3);
        TextView txtCheck4 = view.findViewById(R.id.txtCheckFloor4);
        TextView txtCheck5 = view.findViewById(R.id.txtCheckFloor5);
        TextView txtCheck6 = view.findViewById(R.id.txtCheckFloor6);

        // Highlight active floor card
        highlightFloorCard("Floor 1".equalsIgnoreCase(currentFloor), cardFloor1, txtCheck1);
        highlightFloorCard("Floor 2".equalsIgnoreCase(currentFloor), cardFloor2, txtCheck2);
        highlightFloorCard("Floor 3".equalsIgnoreCase(currentFloor), cardFloor3, txtCheck3);
        highlightFloorCard("Floor 4".equalsIgnoreCase(currentFloor), cardFloor4, txtCheck4);
        highlightFloorCard("Floor 5".equalsIgnoreCase(currentFloor), cardFloor5, txtCheck5);
        highlightFloorCard("Floor 6".equalsIgnoreCase(currentFloor), cardFloor6, txtCheck6);

        if (cardFloor1 != null) cardFloor1.setOnClickListener(v -> selectFloor("Floor 1", etFloor, dialog));
        if (cardFloor2 != null) cardFloor2.setOnClickListener(v -> selectFloor("Floor 2", etFloor, dialog));
        if (cardFloor3 != null) cardFloor3.setOnClickListener(v -> selectFloor("Floor 3", etFloor, dialog));
        if (cardFloor4 != null) cardFloor4.setOnClickListener(v -> selectFloor("Floor 4", etFloor, dialog));
        if (cardFloor5 != null) cardFloor5.setOnClickListener(v -> selectFloor("Floor 5", etFloor, dialog));
        if (cardFloor6 != null) cardFloor6.setOnClickListener(v -> selectFloor("Floor 6", etFloor, dialog));

        Button btnClose = view.findViewById(R.id.btnCloseFloorDialog);
        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }

    private void highlightFloorCard(boolean isActive, MaterialCardView card, TextView checkView) {
        if (card == null) return;
        if (isActive) {
            card.setStrokeColor(Color.parseColor("#059669"));
            card.setStrokeWidth(4);
            card.setCardBackgroundColor(Color.parseColor("#ECFDF5"));
            if (checkView != null) checkView.setVisibility(View.VISIBLE);
        } else {
            card.setStrokeColor(Color.parseColor("#E2E8F0"));
            card.setStrokeWidth(2);
            card.setCardBackgroundColor(Color.parseColor("#F8FAFC"));
            if (checkView != null) checkView.setVisibility(View.GONE);
        }
    }

    private void selectFloor(String floorName, AutoCompleteTextView etFloor, AlertDialog dialog) {
        if (etFloor != null) {
            etFloor.setText(floorName, false);
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private String normalizeFloor(String rawFloor) {
        if (rawFloor == null || rawFloor.trim().isEmpty()) return "Floor 1";
        String trimmed = rawFloor.trim();
        if (trimmed.startsWith("Floor ")) return trimmed;
        if (trimmed.startsWith("floor ")) return "Floor " + trimmed.substring(6).trim();
        try {
            int floorNum = Integer.parseInt(trimmed);
            if (floorNum >= 1 && floorNum <= 6) {
                return "Floor " + floorNum;
            }
        } catch (Exception ignored) {}
        return trimmed;
    }

    private void processImageAndSave(AdminMenuItemModel existingItem, String name, int price, String floor, String manualUrl, AlertDialog dialog, Button btnAddAction) {
        Toast.makeText(this, "Saving item...", Toast.LENGTH_SHORT).show();

        String itemId = (existingItem != null && existingItem.id != null && !existingItem.id.isEmpty())
                ? existingItem.id
                : ref.push().getKey();
        if (itemId == null) itemId = "item_" + System.currentTimeMillis();

        final String targetId = itemId;

        executor.execute(() -> {
            byte[] imageBytes = getImageBytes();

            runOnUiThread(() -> {
                if (imageBytes != null && imageBytes.length > 0) {
                    StorageReference storageRef = FirebaseStorage.getInstance()
                            .getReference("menu_images")
                            .child(targetId + ".jpg");

                    UploadTask uploadTask = storageRef.putBytes(imageBytes);
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            saveFinalItem(targetId, name, price, floor, downloadUri.toString(), dialog, btnAddAction);
                        }).addOnFailureListener(e -> {
                            String base64Image = "data:image/jpeg;base64," + Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                            saveFinalItem(targetId, name, price, floor, base64Image, dialog, btnAddAction);
                        });
                    }).addOnFailureListener(e -> {
                        String base64Image = "data:image/jpeg;base64," + Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                        saveFinalItem(targetId, name, price, floor, base64Image, dialog, btnAddAction);
                    });
                } else {
                    String finalUrl = manualUrl;
                    if (finalUrl.isEmpty() && existingItem != null && existingItem.imageUrl != null) {
                        finalUrl = existingItem.imageUrl;
                    }
                    if (finalUrl.isEmpty()) {
                        finalUrl = ImageUtils.getImageUrl(name, null);
                    }

                    saveFinalItem(targetId, name, price, floor, finalUrl, dialog, btnAddAction);
                }
            });
        });
    }

    private byte[] getImageBytes() {
        try {
            if (selectedBitmap != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
                return baos.toByteArray();
            } else if (selectedUri != null) {
                Bitmap bm = decodeUriToBitmap(selectedUri);
                if (bm != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 85, baos);
                    return baos.toByteArray();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap decodeUriToBitmap(Uri uri) {
        if (uri == null) return null;
        try (InputStream is = getContentResolver().openInputStream(uri)) {
            Bitmap original = BitmapFactory.decodeStream(is);
            if (original != null) {
                return scaleBitmap(original, 1024);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap scaleBitmap(Bitmap bitmap, int maxDimension) {
        if (bitmap == null) return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= maxDimension && height <= maxDimension) return bitmap;

        float ratio = Math.min((float) maxDimension / width, (float) maxDimension / height);
        int newWidth = Math.round(ratio * width);
        int newHeight = Math.round(ratio * height);

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    private void saveFinalItem(String id, String name, int price, String floor, String imageUrl, AlertDialog dialog, Button btnAddAction) {
        AdminMenuItemModel item = new AdminMenuItemModel(id, name, price, floor, imageUrl);

        ref.child(id).setValue(item).addOnCompleteListener(task -> {
            if (btnAddAction != null) btnAddAction.setEnabled(true);
            if (task.isSuccessful()) {
                Toast.makeText(this, "Item saved successfully!", Toast.LENGTH_SHORT).show();
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to save item to database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}