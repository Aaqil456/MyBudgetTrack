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
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class BajetAdapter extends RecyclerView.Adapter<BajetAdapter.ViewHolder> {

    private List<BajetItem> mData;

    public BajetAdapter(List<BajetItem> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bajet_recycler_view, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BajetItem model = mData.get(position);
        holder.namaBajet.setText(model.getNamaBajet());
        holder.jumlahBajet.setText(model.getJumlahBajet());
        holder.tempohBajet.setText(model.getTempohBajet());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView namaBajet;
        TextView jumlahBajet;
        TextView tempohBajet;


        public ViewHolder(View itemView) {
            super(itemView);
            namaBajet = itemView.findViewById(R.id.nama_bajet);
            jumlahBajet = itemView.findViewById(R.id.jumlah_bajet);
            tempohBajet = itemView.findViewById(R.id.tempoh_bajet);

        }
    }
}
