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

    private List<Item> itemList;
    private OnQuantityChangeListener onQuantityChangeListener;

    public ItemAdapter(List<Item> itemList, OnQuantityChangeListener onQuantityChangeListener) {
        this.itemList = itemList;
        this.onQuantityChangeListener = onQuantityChangeListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.itemNameTextView.setText(item.getName());
        holder.itemPriceTextView.setText(String.format("%.2f Kč", item.getPrice()));
        holder.itemQuantityTextView.setText(String.valueOf(item.getQuantity()));

        holder.plusButton.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            holder.itemQuantityTextView.setText(String.valueOf(item.getQuantity()));
            onQuantityChangeListener.onQuantityChange();
        });

        holder.minusButton.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                holder.itemQuantityTextView.setText(String.valueOf(item.getQuantity()));
                onQuantityChangeListener.onQuantityChange();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView itemPriceTextView;
        TextView itemQuantityTextView;
        Button plusButton;
        Button minusButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
            itemQuantityTextView = itemView.findViewById(R.id.itemQuantityTextView);
            plusButton = itemView.findViewById(R.id.plusButton);
            minusButton = itemView.findViewById(R.id.minusButton);
        }
    }

    public interface OnQuantityChangeListener {
        void onQuantityChange();
    }
}