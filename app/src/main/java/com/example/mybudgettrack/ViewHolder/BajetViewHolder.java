package com.example.mybudgettrack.ViewHolder;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgettrack.R;

public class BajetViewHolder extends RecyclerView.ViewHolder {

    public TextView tvWang;
    public TextView tvTarikh;
    public TextView tvPenerangan;
    View mView;

    public BajetViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        //item long click listener
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());

                return false;
            }
        });

        //initialize view with model_layout.xml
        tvWang = itemView.findViewById(R.id.tvWang);
        tvTarikh = itemView.findViewById(R.id.tvTarikh);
        tvPenerangan = itemView.findViewById(R.id.tvPenerangan);
    }

    private BajetViewHolder.ClickListener mClickListener;

    //interface for click listener
    public  interface  ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(BajetViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
