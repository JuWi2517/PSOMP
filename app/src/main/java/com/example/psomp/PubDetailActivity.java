package com.example.psomp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.psomp.room.ItemEntity;
import com.example.psomp.room.PubDatabase;
import com.example.psomp.room.PubDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class PubDetailActivity extends AppCompatActivity {

    private List<Item> itemList;
    private ItemAdapter itemAdapter;
    private TextView totalPriceTextView;
    private double totalPrice = 0.0;
    private PubDao pubDao;
    private int pubId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_detail);

        // Initialize the database and DAO
        PubDatabase db = PubDatabase.getInstance(this);
        pubDao = db.pubDao();

        // Get the pubId from the intent or savedInstanceState
        pubId = getIntent().getIntExtra("PUB_ID", -1);

        String pubName = getIntent().getStringExtra("PUB_NAME");
        TextView pubNameTextView = findViewById(R.id.pubNameTextView);
        pubNameTextView.setText(pubName);

        itemList = new ArrayList<>();
        RecyclerView itemRecyclerView = findViewById(R.id.itemRecyclerView);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemAdapter = new ItemAdapter(itemList, this::updateTotalPrice);
        itemRecyclerView.setAdapter(itemAdapter);

        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        updateTotalPrice();

        FloatingActionButton addItemButton = findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(v -> showAddItemDialog());

        // Load items from the database
        loadItemsFromDatabase();
    }

    private void loadItemsFromDatabase() {
        new Thread(() -> {
            List<ItemEntity> items = pubDao.getItemsByPubId(pubId);
            runOnUiThread(() -> {
                for (ItemEntity itemEntity : items) {
                    itemList.add(new Item(itemEntity.getName(), itemEntity.getPrice(), itemEntity.getQuantity()));
                }
                itemAdapter.notifyDataSetChanged();
                updateTotalPrice();
            });
        }).start();
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Přidat položku");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_item, null);
        EditText itemNameInput = dialogView.findViewById(R.id.itemNameInput);
        EditText itemPriceInput = dialogView.findViewById(R.id.itemPriceInput);
        EditText itemQuantityInput = dialogView.findViewById(R.id.itemQuantityInput); // Add this input

        builder.setView(dialogView);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String itemName = itemNameInput.getText().toString().trim();
            String itemPriceStr = itemPriceInput.getText().toString().trim();
            String itemQuantityStr = itemQuantityInput.getText().toString().trim(); // Get quantity

            if (!itemName.isEmpty() && !itemPriceStr.isEmpty() && !itemQuantityStr.isEmpty()) {
                double itemPrice = Double.parseDouble(itemPriceStr);
                int itemQuantity = Integer.parseInt(itemQuantityStr); // Parse quantity
                addItemToPub(pubId, itemName, itemPrice, itemQuantity);
                itemList.add(new Item(itemName, itemPrice, itemQuantity));
                itemAdapter.notifyItemInserted(itemList.size() - 1);
                updateTotalPrice();
            }
        });
        builder.setNegativeButton("Zrušit", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addItemToPub(int pubId, String name, double price, int quantity) {
        ItemEntity newItem = new ItemEntity(pubId, name, "", price, quantity);
        new Thread(() -> pubDao.insertItem(newItem)).start();
    }

    private void updateTotalPrice() {
        totalPrice = 0.0;
        for (Item item : itemList) {
            totalPrice += item.getQuantity() * item.getPrice();
        }
        totalPriceTextView.setText("Celková cena: " + String.format("%.2f Kč", totalPrice));
    }
}