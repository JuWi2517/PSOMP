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
import com.example.psomp.room.PubDatabase;
import com.example.psomp.room.PubEntity;
import com.example.psomp.room.PubDao;

public class MainActivity extends AppCompatActivity implements PubAdapter.OnPubClickListener {

    private List<String> pubList;
    private PubAdapter pubAdapter;
    private RecyclerView pubRecyclerView;
    private PubDao pubDao; // Move pubDao to a class-level variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Room database and DAO
        PubDatabase database = PubDatabase.getInstance(this);
        pubDao = database.pubDao(); // Initialize pubDao

        pubList = new ArrayList<>();

        // Load pubs from the database
        new Thread(() -> {
            List<PubEntity> pubs = pubDao.getAllPubs();
            for (PubEntity pub : pubs) {
                pubList.add(pub.getName());
            }
            runOnUiThread(() -> pubAdapter.notifyDataSetChanged());
        }).start();

        // Initialize the RecyclerView
        pubRecyclerView = findViewById(R.id.pubRecyclerView);
        pubRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and set it to the RecyclerView
        pubAdapter = new PubAdapter(pubList, this);
        pubRecyclerView.setAdapter(pubAdapter);

        // Handle adding new pubs
        FloatingActionButton addPubButton = findViewById(R.id.addPubButton);
        addPubButton.setOnClickListener(v -> showAddPubDialog());
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

                // Save the pub to the database
                new Thread(() -> pubDao.insertPub(new PubEntity(pubName))).start();
            }
        });
        builder.setNegativeButton("Zrušit", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onPubClick(int position) {
        Intent intent = new Intent(this, PubDetailActivity.class);
        intent.putExtra("PUB_NAME", pubList.get(position));
        startActivity(intent);
    }
}
