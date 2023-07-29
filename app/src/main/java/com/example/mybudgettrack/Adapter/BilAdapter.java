package com.example.mybudgettrack.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgettrack.Bil;
import com.example.mybudgettrack.BilListActivity;
import com.example.mybudgettrack.Model.BilModel;
import com.example.mybudgettrack.R;
import com.example.mybudgettrack.ViewHolder.BilViewHolder;

import java.util.List;

public class BilAdapter extends RecyclerView.Adapter<BilViewHolder>{
    BilListActivity listActivity;
    List<BilModel> modelList;
    Context context;

    public BilAdapter(BilListActivity listActivity, List<BilModel> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public BilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bil_model_layout,parent,false);

        BilViewHolder bilViewHolder = new BilViewHolder(itemView);


        //handle item click here
        bilViewHolder.setOnClickListener(new BilViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String wang = modelList.get(position).getWangBil();
                String tarikh = modelList.get(position).getTarikhBayar();
                String penerangan = modelList.get(position).getPeneranganBil();
                Toast.makeText(listActivity, wang +"\n"+tarikh+"\n"+penerangan
                        , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //creating alert dialog
                AlertDialog.Builder builder= new AlertDialog.Builder(listActivity);
                //options to display in dialog
                String[] options = {"Kemaskini","Sudah bayar"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            //update is clicked
                            String id = modelList.get(position).getId();
                            String wang = modelList.get(position).getWangBil();
                            String tarikh = modelList.get(position).getTarikhBayar();
                            String penerangan = modelList.get(position).getPeneranganBil();

                            //intent to start activity
                            Intent intent = new Intent(listActivity, Bil.class);
                            //put data in intent
                            intent.putExtra("pid",id);
                            intent.putExtra("pwang",wang);
                            intent.putExtra("ptarikh",tarikh);
                            intent.putExtra("ppenerangan",penerangan);

                            listActivity.startActivity(intent);

                        }

                        if(i == 1){
                            //delete is clicked

                            listActivity.deleteData(position);
                        }
                    }
                }).create().show();
            }
        });

        return bilViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BilViewHolder holder, int position) {
        //bind view set data
        holder.tvWang.setText(modelList.get(position).getWangBil());
        holder.tvTarikh.setText(modelList.get(position).getTarikhBayar());
        holder.tvPenerangan.setText(modelList.get(position).getPeneranganBil());



    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}

