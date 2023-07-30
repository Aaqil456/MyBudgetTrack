package com.example.mybudgettrack.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgettrack.R;

public class UserViewHolder extends RecyclerView.ViewHolder{
    public TextView tvUserName;
    public TextView tvTotalSavings;
    View mView;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;


        //initialize view with model_layout.xml
        tvUserName = itemView.findViewById(R.id.tvUsername);
        tvTotalSavings = itemView.findViewById(R.id.tvTotalSavings);
    }

    private UserViewHolder.ClickListener mClickListener;

    //interface for click listener
    public  interface  ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(UserViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
