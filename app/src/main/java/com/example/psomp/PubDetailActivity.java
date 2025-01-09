package com.example.psomp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class PubDetailActivity extends AppCompatActivity implements ItemAdapter.OnQuantityChangeListener {

    private List<Item> itemList;
    private ItemAdapter itemAdapter;
    private TextView totalPriceTextView;
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

        itemList = new ArrayList<>();
        RecyclerView itemRecyclerView = findViewById(R.id.itemRecyclerView);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemAdapter = new ItemAdapter(itemList, this);
        itemRecyclerView.setAdapter(itemAdapter);

        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        updateTotalPrice();

        FloatingActionButton addItemButton = findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(v -> showAddItemDialog());

        loadItemsFromJson();
    }

    private void loadItemsFromJson() {
        String json = FileUtil.readFromFile(this, "pubs.json");
        List<PubWithItems> pubList = JsonUtil.fromJson(json);
        for (PubWithItems pubWithItems : pubList) {
            if (pubWithItems.getPub().getId() == pubId) {
                pub = new Pub(pubWithItems.getPub().getName());
                for (ItemEntity itemEntity : pubWithItems.getItems()) {
                    itemList.add(new Item(itemEntity.getName(), itemEntity.getPrice(), itemEntity.getQuantity()));
                }
                break;
            }
        }
        itemAdapter.notifyDataSetChanged();
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
                itemList.add(new Item(itemName, itemPrice));
                itemAdapter.notifyItemInserted(itemList.size() - 1);
                updateTotalPrice();
            }
        });
        builder.setNegativeButton("Zrušit", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addItemToPub(String name, double price) {
        Item newItem = new Item(name, price, 1);
        pub.addItem(newItem);
        itemList.add(newItem); // Add to itemList
        saveItemsToJson();
        itemAdapter.notifyItemInserted(itemList.size() - 1); // Notify adapter
        updateTotalPrice(); // Update total price
    }

    private void saveItemsToJson() {
        String json = FileUtil.readFromFile(this, "pubs.json");
        List<PubWithItems> pubList = JsonUtil.fromJson(json);
        for (PubWithItems pubWithItems : pubList) {
            if (pubWithItems.getPub().getId() == pubId) {
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
    }
}