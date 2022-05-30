package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.sanket.jubifarm.Livelihood.Adapter.Adapter_PS_LandHolding;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class PS_LandHoldingList extends AppCompatActivity {

    ImageButton fab;

    RecyclerView recyclerView;
    ArrayList<PSLandHoldingPojo> arrayList = new ArrayList<>();
    SqliteHelper sqliteHelper;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps_land_holding_list);
        getSupportActionBar().setTitle("Land Holding List");
        fab=findViewById(R.id.fab);

        sqliteHelper = new SqliteHelper(this);
        recyclerView = findViewById(R.id.rvLandHoldning);


        arrayList = sqliteHelper.PSgetRegistrationData("");
        Adapter_PS_LandHolding adapter_ps_landHolding = new Adapter_PS_LandHolding(context, arrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter_ps_landHolding);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1=new Intent(PS_LandHoldingList.this,PS_LandHolding.class);
                startActivity(intent1);
            }
        });
    }
}