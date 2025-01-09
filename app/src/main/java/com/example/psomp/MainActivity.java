package com.example.psomp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PubAdapter.OnPubClickListener {

    private List<String> pubList;
    private PubAdapter pubAdapter;
    private RecyclerView pubRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pubList = new ArrayList<>();

        // Initialize the adapter
        pubAdapter = new PubAdapter(pubList, this);

        // Initialize the RecyclerView
        pubRecyclerView = findViewById(R.id.pubRecyclerView);
        pubRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pubRecyclerView.setAdapter(pubAdapter);

        // Load pubs from the JSON file
        loadPubsFromJson();

        // Handle adding new pubs
        FloatingActionButton addPubButton = findViewById(R.id.addPubButton);
        addPubButton.setOnClickListener(v -> showAddPubDialog());
    }

    private void loadPubsFromJson() {
        String json = FileUtil.readFromFile(this, "pubs.json");
        List<PubWithItems> pubWithItemsList = new ArrayList<>();
        if (json != null && !json.isEmpty()) {
            List<PubWithItems> parsedList = JsonUtil.fromJson(json);
            if (parsedList != null) {
                pubWithItemsList = parsedList;
            }
        }
        for (PubWithItems pubWithItems : pubWithItemsList) {
            pubList.add(pubWithItems.getPub().getName());
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
                pubList.add(pubName);
                pubAdapter.notifyItemInserted(pubList.size() - 1);

                // Save the pub to the JSON file
                addPubToJson(pubName);
            }
        });
        builder.setNegativeButton("Zrušit", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addPubToJson(String pubName) {
        String json = FileUtil.readFromFile(this, "pubs.json");
        List<PubWithItems> pubWithItemsList = new ArrayList<>();
        if (json != null && !json.isEmpty()) {
            List<PubWithItems> parsedList = JsonUtil.fromJson(json);
            if (parsedList != null) {
                pubWithItemsList = parsedList;
            }
        }

        PubEntity newPub = new PubEntity();
        newPub.setId(pubWithItemsList.size() + 1); // Assign a new ID
        newPub.setName(pubName);

        PubWithItems newPubWithItems = new PubWithItems();
        newPubWithItems.setPub(newPub);
        newPubWithItems.setItems(new ArrayList<>());

        pubWithItemsList.add(newPubWithItems);

        String updatedJson = JsonUtil.toJson(pubWithItemsList);
        FileUtil.saveToFile(this, "pubs.json", updatedJson);
    }

    @Override
    public void onPubClick(int position) {
        Intent intent = new Intent(this, PubDetailActivity.class);
        intent.putExtra("PUB_ID", position + 1); // Pass the pub ID
        intent.putExtra("PUB_NAME", pubList.get(position));
        startActivity(intent);
    }
}