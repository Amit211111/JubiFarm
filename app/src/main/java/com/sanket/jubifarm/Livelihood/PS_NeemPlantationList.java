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
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.shamanland.fab.FloatingActionButton;

import java.util.ArrayList;

public class PS_NeemPlantationList extends AppCompatActivity {

  ImageButton fab;
  RecyclerView rv_neem_Plantation;
  SqliteHelper sqliteHelper;
  String screenType;
  SharedPrefHelper sharedPrefHelper;
  ArrayList<PSNeemPlantationPojo> psNeemPlantationPojos;
  String land_id = "";



    String screen_type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps_neem_plantation_list);
        getSupportActionBar().setTitle("Neem Plantation List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab=findViewById(R.id.fab);
        sharedPrefHelper = new SharedPrefHelper(this);
        initiliaze();
        rv_neem_Plantation = findViewById(R.id.rv_neem_Plantation);


        screen_type = sharedPrefHelper.getString("prayawran_screenType", "");

        if(screen_type.equals("land")){
            fab.setVisibility(View.VISIBLE);
        }else {
            fab.setVisibility(View.GONE);
        }

        /*get intent value here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            land_id = bundle.getString("land_ID", "");
        }

        if(sharedPrefHelper.getString("plantation_list","").equals("plantation")){
            psNeemPlantationPojos = sqliteHelper.getneemplantation("");
        }else{
            psNeemPlantationPojos = sqliteHelper.getneemplantation(land_id);
        }

        if(psNeemPlantationPojos.size()>0)
        {
            NeemPlantationAdapter neemPlantationAdapter = new NeemPlantationAdapter(this, psNeemPlantationPojos, screenType);
            rv_neem_Plantation.setHasFixedSize(true);
            rv_neem_Plantation.setLayoutManager(new LinearLayoutManager(this));
            rv_neem_Plantation.setAdapter(neemPlantationAdapter);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(PS_NeemPlantationList.this, NeemPlantation.class);
                sharedPrefHelper.getString("plantation_screenType", "view");
                startActivity(intent);
            }
        });
    }

    private void initiliaze() {
        sharedPrefHelper=new SharedPrefHelper(this);
        psNeemPlantationPojos = new ArrayList<>();
        rv_neem_Plantation = findViewById(R.id.rv_neem_Plantation);
        sqliteHelper = new SqliteHelper(this);
    }
}