package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanket.jubifarm.Adapter.PlantGrowthAdapter;
import com.sanket.jubifarm.Modal.PlantGrowthPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class PlantGrowthListActivity extends AppCompatActivity {
    RecyclerView rv_plantgrowth;
    ImageView Crop_filter;
    PlantGrowthAdapter plantGrowthAdapter;
    private ArrayList<PlantGrowthPojo> plantGrowthPojoArrayList;
    TextView cropCount;
    SqliteHelper sqliteHelper;
    ImageButton Button;
    String plant_id;
    String id;
    String farmer_id;
    String crop_type_catagory_id="";
    String crop_type_subcatagory_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_growth_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.plant_growth_list) + "</font>"));

        sqliteHelper = new SqliteHelper(this);

        Crop_filter = findViewById(R.id.cropplaning_filter);
        Button  = findViewById(R.id.fab);
        cropCount  = findViewById(R.id.cropCount);
        rv_plantgrowth  = findViewById(R.id.rv_plantgrowth);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            plant_id = bundle.getString("plant_id", "");
            id = bundle.getString("id", "");
            farmer_id = bundle.getString("farmer_id", "");
            crop_type_catagory_id = bundle.getString("crop_type_catagory_id", "");
            crop_type_subcatagory_id = bundle.getString("crop_type_subcatagory_id", "");
            Log.e("plantGrowthList","======"+crop_type_catagory_id);

        }
        plantGrowthPojoArrayList= sqliteHelper.getPlantgrwthList(id);
        plantGrowthAdapter  = new PlantGrowthAdapter(this, plantGrowthPojoArrayList);
        int counter = plantGrowthPojoArrayList.size();
        cropCount.setText(getString(R.string.Total_Crop) + " " + counter);
        rv_plantgrowth.setHasFixedSize(true);
        rv_plantgrowth.setLayoutManager(new LinearLayoutManager(this));
        rv_plantgrowth.setAdapter(plantGrowthAdapter);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Regintent = new Intent(PlantGrowthListActivity.this, AddPlantGrowthActivity.class);
                Regintent.putExtra("crop_planing_id" ,id);
                Regintent.putExtra("farmer_id",farmer_id);
                Regintent.putExtra("crop_type_catagory_id",crop_type_catagory_id);
                Regintent.putExtra("crop_type_subcatagory_id",crop_type_subcatagory_id);
                startActivity(Regintent);

            }
        });
        Crop_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(PlantGrowthListActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.crop_filter, viewGroup, false);
                builder.setView(dialogView);
                MaterialSpinner btn_cropfilter = (MaterialSpinner) dialogView .findViewById(R.id.btn_cropfilter);
                btn_cropfilter.setItems("--Select--","RajKumari","Sumitra Singh","Punam Panday", "Sita");

                AlertDialog alertDialog = builder.create();

                alertDialog.show();

            }
        });

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }


    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.home_menu) {

                Intent intent = new Intent(this, HomeAcivity.class);
                this.startActivity(intent);
                return true;
            }
            if (item.getItemId() == android.R.id.home) {
                finish();
            }
            return super.onOptionsItemSelected(item);
        }


    }
