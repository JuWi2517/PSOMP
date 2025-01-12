// MainActivity.java
package com.example.psomp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private List<PubWithItems> pubList;
    private RecyclerView pubContainer;
    private PubAdapter pubAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pubList = new ArrayList<>();
        pubContainer = findViewById(R.id.pubContainer);
        pubContainer.setLayoutManager(new LinearLayoutManager(this));
        pubAdapter = new PubAdapter(pubList, this::onPubClick);
        pubContainer.setAdapter(pubAdapter);

        FloatingActionButton addPubButton = findViewById(R.id.addPubButton);
        addPubButton.setOnClickListener(v -> showAddPubDialog());

        loadPubsFromJson();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT) {
                    int position = viewHolder.getAdapterPosition();
                    pubList.remove(position);
                    pubAdapter.notifyItemRemoved(position);
                    savePubsToJson();
                    Log.d(TAG, "Pub swiped right, deleted, and saved to JSON");
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(pubContainer);

        ;
    }

    private void loadPubsFromJson() {
        String json = FileUtil.readFromFile(this, "pubs.json");
        Log.d(TAG, "loadPubsFromJson: "+ json);

        if (json != null && !json.isEmpty()) {
            List<PubWithItems> parsedList = JsonUtil.fromJson(json);
            if (parsedList != null) {
                pubList.clear();
                pubList.addAll(parsedList);
            }
        }
        pubAdapter.notifyDataSetChanged();
    }

    private void showAddPubDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Přidat novou hospodu");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String pubName = input.getText().toString().trim();
            if (!pubName.isEmpty()) {
                PubEntity newPub = new PubEntity();
                newPub.setName(pubName);
                newPub.setQuantity(0);

                PubWithItems newPubWithItems = new PubWithItems();
                newPubWithItems.setPub(newPub);
                newPubWithItems.setItems(new ArrayList<>());

                pubList.add(newPubWithItems);
                pubAdapter.notifyItemInserted(pubList.size() - 1);
                savePubsToJson();
            }
        });

        builder.setNegativeButton("Zrušit", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void savePubsToJson() {
        String updatedJson = JsonUtil.toJson(pubList);
        Log.d(TAG, "savePubsToJson: "+ updatedJson);
        FileUtil.saveToFile(this, "pubs.json", updatedJson);
    }



    private void onPubClick(String pubName) {
        Intent intent = new Intent(this, PubDetailActivity.class);
        intent.putExtra("PUB_NAME", pubName);
        startActivity(intent);
    }
}