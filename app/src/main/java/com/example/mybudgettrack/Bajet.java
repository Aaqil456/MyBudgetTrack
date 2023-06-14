package com.example.mybudgettrack;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Bajet extends AppCompatActivity {

    EditText etWang,  etPenerangan;
    TextView etTarikh;
    Button btnSimpan,mListButton;
    ProgressDialog pd;
    FirebaseFirestore db;
    String pId,pWang,pTarikh,pPenerangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajet);
        //action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tambah Data");
        //initialize
        etWang=findViewById(R.id.etWang);
        etTarikh=findViewById(R.id.etTarikh);
        etPenerangan=findViewById(R.id.etPenerangan);

        btnSimpan=findViewById(R.id.saveBtn);
        mListButton=findViewById(R.id.listBtn);


        /*if come here after clicking update in alert dialog of BajetListactivity
        * then get data id, wang, tarikh, penerangan
        * change title of action bar and add save button*/
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            //Update Data
            actionBar.setTitle("Kemaskini Perbelanjaan");
            btnSimpan.setText("Kemaskini");

            //get data
            pId= bundle.getString("pid");
            pWang= bundle.getString("pwang");
            pTarikh= bundle.getString("ptarikh");
            pPenerangan= bundle.getString("ppenerangan");

            //set data
            etWang.setText(pWang);
            etTarikh.setText(pTarikh);
            etPenerangan.setText(pPenerangan);

        }
        else{
            //new data
            actionBar.setTitle("Tambah Perbelanjaan");
            btnSimpan.setText("Tambah");
        }


        //progress dialog
        pd = new ProgressDialog(this);
        //firestore
        db =  FirebaseFirestore.getInstance();

        //click button to add data
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = getIntent().getExtras();
                if(bundle !=null){
                    //updating
                    //input data
                    String id= pId;
                    String wang = etWang.getText().toString().trim();
                    String tarikh = etTarikh.getText().toString().trim();
                    String penerangan = etPenerangan.getText().toString().trim();
                    //call update data function
                    updateData(id,wang,tarikh,penerangan);
                }
                else{
                    //adding new
                    //input data
                    String wang = etWang.getText().toString().trim();
                    String tarikh = etTarikh.getText().toString().trim();
                    String penerangan = etPenerangan.getText().toString().trim();
                    //call upload data function
                    uploadData(wang,tarikh,penerangan);
                }



            }
        });

        //click button to show list
        mListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Bajet.this,BajetListActivity.class));
                finish();
            }
        });




    }


    //date picker function
    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // monthOfYear is 0-indexed, so add 1 to get the correct month
                    String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                    etTarikh.setText(formattedDate);
                }
            };


    public void showDatePickerDialog(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void updateData(String id, String wang, String tarikh, String penerangan) {
        pd.setTitle("Kemaskini data");
        pd.show();

        db.collection("bajet1").document(id)
                .update("Wang perbelanjaan",wang
                        , "Tarikh perbelanjaan",tarikh.toLowerCase()
                ,"Penerangan perbelanjaan",penerangan).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(Bajet.this,"Telah dikemaskini",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Bajet.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadData(String wang, String tarikh, String penerangan) {
        pd.setTitle("Menambah data");
        pd.show();

        //random id for each data
        String id = UUID.randomUUID().toString();

        Map<String,Object> doc = new HashMap<>();
        doc.put("id",id);
        doc.put("Wang perbelanjaan", wang);
        doc.put("Tarikh perbelanjaan", tarikh);
        doc.put("Penerangan perbelanjaan", penerangan);

        //add this data
        db.collection("bajet1").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(Bajet.this,"Uploaded",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Bajet.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        // Handle the back button press
        Intent intent = new Intent(Bajet.this, BajetListActivity.class);
        startActivity(intent);
        finish(); // Optional: If you don't want to keep the SecondActivity in the back stack
    }
}