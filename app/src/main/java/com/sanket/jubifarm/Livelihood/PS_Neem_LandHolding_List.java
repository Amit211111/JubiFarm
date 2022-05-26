package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.sanket.jubifarm.Livelihood.Adapter.Adapter_PS_LandHolding;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class PS_Neem_LandHolding_List extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<PSLandHoldingPojo> arrayList = new ArrayList<>();
    SqliteHelper sqliteHelper;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps_neem_land_holding_list2);

        sqliteHelper = new SqliteHelper(this);
        recyclerView = findViewById(R.id.rvLandHoldning1);


       arrayList = sqliteHelper.PSgetRegistrationData();
        Adapter_PS_LandHolding adapter_ps_landHolding = new Adapter_PS_LandHolding(context, arrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter_ps_landHolding);



    }
}