package com.example.mybudgettrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mybudgettrack.Adapter.BajetAdapter;
import com.example.mybudgettrack.Item.BajetItem;

import java.util.ArrayList;
import java.util.List;

public class Bajet extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BajetAdapter adapter;
    private ArrayList<BajetItem> mExampleList;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajet);

        btnAdd=findViewById(R.id.add_button);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<BajetItem> items = new ArrayList<>();
        items.add(new BajetItem("Bajet 1", "2000","2 bulan"));
        items.add(new BajetItem("Bajet 2", "12334","3 hari"));
        items.add(new BajetItem("Bajet 3", "12332","4 tahun"));
        adapter = new BajetAdapter(items);
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Bajet.this,CiptaBajet.class));
            }
        });
    }
}