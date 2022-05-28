package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.sanket.jubifarm.Livelihood.Adapter.NeemPlantationAdapter;
import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.shamanland.fab.FloatingActionButton;

import java.util.ArrayList;

public class PS_NeemPlantationList extends AppCompatActivity {

  ImageButton fab;
  RecyclerView rv_neem_Plantation;
  SqliteHelper sqliteHelper;
  ArrayList<PSNeemPlantationPojo> psNeemPlantationPojos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps_neem_plantation_list);
        getSupportActionBar().setTitle("Neem Plantation List");
        fab=findViewById(R.id.fab);
        initiliaze();
        rv_neem_Plantation = findViewById(R.id.rv_neem_Plantation);


        psNeemPlantationPojos = sqliteHelper.getneemplantation();
        if(psNeemPlantationPojos.size()>0)
        {
            NeemPlantationAdapter neemPlantationAdapter = new NeemPlantationAdapter(this, psNeemPlantationPojos);
            rv_neem_Plantation.setHasFixedSize(true);
            rv_neem_Plantation.setLayoutManager(new LinearLayoutManager(this));
            rv_neem_Plantation.setAdapter(neemPlantationAdapter);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(PS_NeemPlantationList.this, NeemPlantation.class);
                startActivity(intent);
            }
        });
    }

    private void initiliaze() {
        psNeemPlantationPojos = new ArrayList<>();
        rv_neem_Plantation = findViewById(R.id.rv_neem_Plantation);
        sqliteHelper = new SqliteHelper(this);
    }
}