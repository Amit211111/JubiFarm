package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sanket.jubifarm.Livelihood.Adapter.Adapter_PS_LandHolding;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class PS_Neem_LandHolding_List extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<PSLandHoldingPojo> arrayList = new ArrayList<>();
    SqliteHelper sqliteHelper;
    String screenType = "";
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps_neem_land_holding_list2);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            screenType = bundle.getString("monitoring", "");
        }

        sqliteHelper = new SqliteHelper(this);
        recyclerView = findViewById(R.id.rvLandHoldning1);



       arrayList = sqliteHelper.PSgetRegistrationData("");
        Adapter_PS_LandHolding adapter_ps_landHolding = new Adapter_PS_LandHolding(context, arrayList, screenType);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter_ps_landHolding);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        menu.findItem(R.id.home_filter).setVisible(true);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home_menu) {

            Intent intent = new Intent(this, ParyavaranSakhiHome.class);
            this.startActivity(intent);
        }
        return true;

    }
}