// PubAdapter.java
package com.example.psomp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PubAdapter extends RecyclerView.Adapter<PubAdapter.PubViewHolder> {

    private final List<PubWithItems> pubList;
    private final OnPubClickListener clickListener;

    public interface OnPubClickListener {
        void onPubClick(String pubName);
    }

    public PubAdapter(List<PubWithItems> pubList, OnPubClickListener clickListener) {
        this.pubList = pubList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pub, parent, false);
        return new PubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PubViewHolder holder, int position) {
        holder.bind(pubList.get(position));
    }

    @Override
    public int getItemCount() {
        return pubList.size();
    }

    class PubViewHolder extends RecyclerView.ViewHolder {
        private final TextView pubName;

        public PubViewHolder(@NonNull View itemView) {
            super(itemView);
            pubName = itemView.findViewById(R.id.pubName);
        }

        public void bind(PubWithItems pubWithItems) {
            pubName.setText(pubWithItems.getPub().getName());
            itemView.setOnClickListener(v -> clickListener.onPubClick(pubWithItems.getPub().getName()));
        }
    }
}