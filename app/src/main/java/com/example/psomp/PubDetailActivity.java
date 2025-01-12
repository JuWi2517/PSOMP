package com.example.psomp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class PubDetailActivity extends AppCompatActivity implements ItemAdapter.OnQuantityChangeListener {

    private static final String TAG = "PubDetailActivity";
    private List<Item> itemList;
    private RecyclerView itemContainer;
    private ItemAdapter itemAdapter;
    private TextView totalPriceTextView;
    private ImageView trashCanIcon;
    private double totalPrice = 0.0;
    private int pubId;
    private Pub pub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_detail);

        pubId = getIntent().getIntExtra("PUB_ID", -1);
        String pubName = getIntent().getStringExtra("PUB_NAME");
        TextView pubNameTextView = findViewById(R.id.pubNameTextView);
        pubNameTextView.setText(pubName);

        pub = new Pub(pubName); // Initialize the pub object

        itemList = new ArrayList<>();
        itemContainer = findViewById(R.id.itemContainer);
        itemContainer.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter(itemList, this);
        itemContainer.setAdapter(itemAdapter);

        totalPriceTextView = findViewById(R.id.totalPriceTextView);

        updateTotalPrice();

        FloatingActionButton addItemButton = findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(v -> showAddItemDialog());

        FloatingActionButton clearItemsButton = findViewById(R.id.clearItemsButton);
        clearItemsButton.setOnClickListener(v -> clearItemList());

        FloatingActionButton resetQuantitiesButton = findViewById(R.id.resetQuantitiesButton);
        resetQuantitiesButton.setOnClickListener(v -> resetItemQuantities());

        loadItemsFromJson();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                1, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT) {
                    int position = viewHolder.getAdapterPosition();
                    itemList.remove(position);
                    itemAdapter.notifyItemRemoved(position);
                    updateTotalPrice();
                    saveItemsToJson();

                }
            }

        });

        itemTouchHelper.attachToRecyclerView(itemContainer);
    }

    private void clearPubsJson() {
        try {
            // Write an empty array to the JSON file to clear all data
            FileUtil.saveToFile(this, "pubs.json", "[]");
            Log.d(TAG, "pubs.json has been cleared");
        } catch (Exception e) {
            Log.e(TAG, "Error clearing pubs.json", e);
        }
    }

    private void loadItemsFromJson() {
        String json = FileUtil.readFromFile(this, "pubs.json");
        List<PubWithItems> pubList = JsonUtil.fromJson(json);

        for (PubWithItems pubWithItems : pubList) {
            if (pubWithItems.getPub().getName().equals(pub.getName())) {
                pub = new Pub(pubWithItems.getPub().getName());
                for (ItemEntity itemEntity : pubWithItems.getItems()) {
                    Item item = new Item(itemEntity.getName(), itemEntity.getPrice(), itemEntity.getQuantity());
                    itemList.add(item);
                }
                itemAdapter.notifyDataSetChanged();
                break;
            }
        }
        updateTotalPrice();
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Přidat položku");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_item, null);
        EditText itemNameInput = dialogView.findViewById(R.id.itemNameInput);
        EditText itemPriceInput = dialogView.findViewById(R.id.itemPriceInput);

        builder.setView(dialogView);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String itemName = itemNameInput.getText().toString().trim();
            String itemPriceStr = itemPriceInput.getText().toString().trim();

            if (!itemName.isEmpty() && !itemPriceStr.isEmpty()) {
                double itemPrice = Double.parseDouble(itemPriceStr);
                addItemToPub(itemName, itemPrice);
            }
        });
        builder.setNegativeButton("Zrušit", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addItemToPub(String name, double price) {
        Item newItem = new Item(name, price, 0);
        pub.addItem(newItem);
        itemList.add(newItem);
        itemAdapter.notifyItemInserted(itemList.size() - 1);
        saveItemsToJson();
        updateTotalPrice();
    }

    private void clearItemList() {
        itemList.clear();
        itemAdapter.notifyDataSetChanged();
        saveItemsToJson();
        updateTotalPrice();
    }

    private void resetItemQuantities() {
        for (Item item : itemList) {
            item.setQuantity(0);
        }
        itemAdapter.notifyDataSetChanged();
        saveItemsToJson();
        updateTotalPrice();
    }

    private void saveItemsToJson() {
        String json = FileUtil.readFromFile(this, "pubs.json");
        List<PubWithItems> pubList = JsonUtil.fromJson(json);
        for (PubWithItems pubWithItems : pubList) {
            if (pubWithItems.getPub().getName().equals(pub.getName())) {
                pubWithItems.getItems().clear();
                for (Item item : itemList) {
                    ItemEntity itemEntity = new ItemEntity();
                    itemEntity.setName(item.getName());
                    itemEntity.setPrice(item.getPrice());
                    itemEntity.setQuantity(item.getQuantity());
                    pubWithItems.getItems().add(itemEntity);
                }
                break;
            }
        }
        String updatedJson = JsonUtil.toJson(pubList);
        FileUtil.saveToFile(this, "pubs.json", updatedJson);
    }

    private void updateTotalPrice() {
        totalPrice = 0.0;
        for (Item item : itemList) {
            totalPrice += item.getQuantity() * item.getPrice();
        }
        totalPriceTextView.setText("Celková cena: " + String.format("%.2f Kč", totalPrice));
    }

    @Override
    public void onQuantityChange() {
        updateTotalPrice();
        saveItemsToJson();
    }
}