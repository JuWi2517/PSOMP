package com.example.psomp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final List<Item> itemList;
    private final Runnable updateTotalPriceCallback;

    public ItemAdapter(List<Item> itemList, Runnable updateTotalPriceCallback) {
        this.itemList = itemList;
        this.updateTotalPriceCallback = updateTotalPriceCallback;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.itemNameTextView.setText(item.getName());
        holder.itemPriceTextView.setText(item.getPrice() + " Kč");
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));

        holder.plusButton.setOnClickListener(v -> {
            item.incrementQuantity();
            notifyItemChanged(position);
            updateTotalPriceCallback.run();
        });

        holder.minusButton.setOnClickListener(v -> {
            if (item.getQuantity() > 0) {
                item.decrementQuantity();
                notifyItemChanged(position);
                updateTotalPriceCallback.run();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView, itemPriceTextView, quantityTextView;
        Button plusButton, minusButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            plusButton = itemView.findViewById(R.id.plusButton);
            minusButton = itemView.findViewById(R.id.minusButton);
        }
    }
}