package com.example.psomp;// PubAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PubAdapter extends RecyclerView.Adapter<PubAdapter.PubViewHolder> {

    private final List<String> pubList;
    private final OnPubClickListener clickListener;

    public interface OnPubClickListener {
        void onPubClick(int position);
    }

    public PubAdapter(List<String> pubList, OnPubClickListener clickListener) {
        this.pubList = pubList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PubViewHolder holder, int position) {
        holder.bind(pubList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return pubList.size();
    }

    class PubViewHolder extends RecyclerView.ViewHolder {
        private final TextView pubName;

        public PubViewHolder(@NonNull View itemView) {
            super(itemView);
            pubName = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String name, int position) {
            pubName.setText(name);
            itemView.setOnClickListener(v -> clickListener.onPubClick(position));
        }
    }
}
