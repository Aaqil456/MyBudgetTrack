package com.example.mybudgettrack.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgettrack.BajetListActivity;
import com.example.mybudgettrack.Model.BajetModel;
import com.example.mybudgettrack.R;
import com.example.mybudgettrack.ViewHolder.BajetViewHolder;

import java.util.List;

public class BajetAdapter extends RecyclerView.Adapter<BajetViewHolder> {

    BajetListActivity listActivity;
    List<BajetModel> modelList;
    Context context;

    public BajetAdapter(BajetListActivity listActivity, List<BajetModel> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;
    }



    @NonNull
    @Override
    public BajetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bajet_model_layout,parent,false);

        BajetViewHolder bajetViewHolder = new BajetViewHolder(itemView);

        //handle item click here

        bajetViewHolder.setOnClickListener(new BajetViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String wang = modelList.get(position).getWangBajet();
                String tarikh = modelList.get(position).getTarikhBajet();
                String penerangan = modelList.get(position).getPeneranganBajet();
                Toast.makeText(listActivity, wang +"\n"+tarikh+"\n"+penerangan
                        , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        return new BajetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BajetViewHolder holder, int position) {
        //bind view set data
        holder.tvWang.setText(modelList.get(position).getWangBajet());
        holder.tvTarikh.setText(modelList.get(position).getTarikhBajet());
        holder.tvPenerangan.setText(modelList.get(position).getPeneranganBajet());



    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
