package com.example.mybudgettrack.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgettrack.R;
import com.example.mybudgettrack.User.User;
import com.example.mybudgettrack.UserListActivity;
import com.example.mybudgettrack.ViewHolder.UserViewHolder;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder>{
    UserListActivity listActivity;
    List<User> modelList;
    Context context;

    public UserAdapter(UserListActivity listActivity, List<User> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_model_layout,parent,false);

        UserViewHolder userViewHolder = new UserViewHolder(itemView);




        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        //bind view set data
        holder.tvUserName.setText(modelList.get(position).getUserName());
        holder.tvTotalSavings.setText(Double.toString(modelList.get(position).getUserTotalSaving()));



    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
