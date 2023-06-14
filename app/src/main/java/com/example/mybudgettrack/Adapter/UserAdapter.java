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


//        //handle item click here
//        bilViewHolder.setOnClickListener(new BilViewHolder.ClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                String username = modelList.get(position).getUserName();
//                String totalSaving = Double.toString(modelList.get(position).getUserTotalSaving());
//                String penerangan = modelList.get(position).getPeneranganBil();
//                Toast.makeText(listActivity, wang +"\n"+tarikh+"\n"+penerangan
//                        , Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//                //creating alert dialog
//                AlertDialog.Builder builder= new AlertDialog.Builder(listActivity);
//                //options to display in dialog
//                String[] options = {"Update","Delete"};
//                builder.setItems(options, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if(i == 0){
//                            //update is clicked
//                            String id = modelList.get(position).getId();
//                            String wang = modelList.get(position).getWangBil();
//                            String tarikh = modelList.get(position).getTarikhBayar();
//                            String penerangan = modelList.get(position).getPeneranganBil();
//
//                            //intent to start activity
//                            Intent intent = new Intent(listActivity, Bil.class);
//                            //put data in intent
//                            intent.putExtra("pid",id);
//                            intent.putExtra("pwang",wang);
//                            intent.putExtra("ptarikh",tarikh);
//                            intent.putExtra("ppenerangan",penerangan);
//
//                            listActivity.startActivity(intent);
//
//                        }
//
//                        if(i == 1){
//                            //delete is clicked
//
//                            listActivity.deleteData(position);
//                        }
//                    }
//                }).create().show();
//            }
//        });

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
