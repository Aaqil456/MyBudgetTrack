package com.example.mybudgettrack.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgettrack.Item.BajetItem;
import com.example.mybudgettrack.R;

import java.util.List;

public class BajetAdapter extends RecyclerView.Adapter<BajetAdapter.ItemViewHolder> {
    private static List<BajetItem> items;


    public BajetAdapter(List<BajetItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bajet_recycler_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        BajetItem item = items.get(position);
        holder.namaTextView.setText(item.getNamaBajet());
        holder.jumlahTextView.setText(item.getJumlahBajet());
        holder.tempohTextView.setText(item.getTempohBajet());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder  {
        public TextView namaTextView;
        public TextView jumlahTextView;
        public TextView tempohTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            namaTextView = itemView.findViewById(R.id.nama_bajet);
            jumlahTextView = itemView.findViewById(R.id.jumlah_bajet);
            tempohTextView= itemView.findViewById(R.id.tempoh_bajet);
        }


    }
}
