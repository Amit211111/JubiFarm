package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanket.jubifarm.Activity.CropPlanning;
import com.sanket.jubifarm.Activity.FarmerDeatilActivity;
import com.sanket.jubifarm.Activity.HomeAcivity;
import com.sanket.jubifarm.Activity.LandHoldingActivity;
import com.sanket.jubifarm.Activity.ViewLandActivity;
import com.sanket.jubifarm.Adapter.CropPlanningAdapter;
import com.sanket.jubifarm.Adapter.LandHoldingAdapter;
import com.sanket.jubifarm.Livelihood.Adapter.Adapter_PS_LandHolding;
import com.sanket.jubifarm.Livelihood.Adapter.NeemPlantationAdapter;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.Modal.CropPlaningPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class PS_NeemPlantationList extends AppCompatActivity {

    ImageButton fab;
    RecyclerView rv_neem_Plantation;
    SqliteHelper sqliteHelper;
    String screenType;
    SharedPrefHelper sharedPrefHelper;
    ArrayList<PSNeemPlantationPojo> psNeemPlantationPojos = new ArrayList<>();
    String land_id = "";
    private ArrayList<PSLandHoldingPojo> arrayList = new ArrayList<>();
    private HashMap<String, Integer> LandNameHM;
    private String farmer_id = "";
    String screen_type = "";
    Context context = this;

//    String selected_farmer;
//    private String land_ = "", land_id_id = "", crop_ = "";
//    NeemPlantationAdapter neemPlantationAdapter;
//    private ArrayList<PSNeemPlantationPojo> psNeemPlantationPojoArrayList;
//    private ArrayList<PSNeemPlantationPojo> filter_List;
//    TextView PlantCount, tv_no_data;
    ImageView cropplaning_filter;
    private ArrayList<String> landArrayList;
    HashMap<String, Integer> landHM;
    private ArrayList<String> farmarArrayList;
    private HashMap<String, Integer> farmarNameHM;
//    private int farmerId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps_neem_plantation_list);
        getSupportActionBar().setTitle("Neem Plantation List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = findViewById(R.id.fab);
        sharedPrefHelper = new SharedPrefHelper(this);
        initiliaze();

        rv_neem_Plantation = findViewById(R.id.rv_neem_Plantation);


        screen_type = sharedPrefHelper.getString("prayawran_screenType", "");

        if (screen_type.equals("land")) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }

        /*get intent value here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            land_id = bundle.getString("land_ID", "");
        }

        if (sharedPrefHelper.getString("plantation_list", "").equals("plantation")) {
            psNeemPlantationPojos = sqliteHelper.getneemplantation("");
        } else {
            psNeemPlantationPojos = sqliteHelper.getneemplantation(land_id);
        }

        if (psNeemPlantationPojos.size() > 0) {
            NeemPlantationAdapter neemPlantationAdapter = new NeemPlantationAdapter(this, psNeemPlantationPojos, screenType);
            rv_neem_Plantation.setHasFixedSize(true);
            rv_neem_Plantation.setLayoutManager(new LinearLayoutManager(this));
            rv_neem_Plantation.setAdapter(neemPlantationAdapter);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PS_NeemPlantationList.this, NeemPlantation.class);
                sharedPrefHelper.getString("plantation_screenType", "view");
                startActivity(intent);
            }
        });

        cropplaning_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PS_NeemPlantationList.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_ps_neem_filter, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                MaterialSpinner btn_filter_farmerfiltert = (MaterialSpinner) dialogView.findViewById(R.id.btn_filter_farmer_filtert);
                TextView tv_search = (TextView) dialogView.findViewById(R.id.tv_search);

                farmarArrayList.clear();
                farmarNameHM = sqliteHelper.getPSFarmerspinner();

                for (int i = 0; i < farmarNameHM.size(); i++) {
                    farmarArrayList.add(farmarNameHM.keySet().toArray()[i].toString().trim());
                }
                farmarArrayList.add(0, getString(R.string.select_farmer));
                ArrayAdapter arrayAdapterFarmer = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, farmarArrayList);
                btn_filter_farmerfiltert.setAdapter(arrayAdapterFarmer);

                farmer_id = "";
                btn_filter_farmerfiltert.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        if (!item.equals(getString(R.string.select_farmer))) {
                            farmer_id = String.valueOf(farmarNameHM.get(btn_filter_farmerfiltert.getText().toString().trim()));
                        }
                    }
                });


                MaterialSpinner btn_filter_landfiltert = (MaterialSpinner) dialogView.findViewById(R.id.btn_filter_landfiltert);
                TextView tv_serach = (TextView) dialogView.findViewById(R.id.tv_search);

                landArrayList.clear();
                landHM = sqliteHelper.getPSNeemspinner();

                for (int i = 0; i < landHM.size(); i++) {
                    landArrayList.add(landHM.keySet().toArray()[i].toString().trim());
                }
                landArrayList.add(0, getString(R.string.select_land));
                ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, landArrayList);
                btn_filter_landfiltert.setAdapter(arrayAdapter);

                land_id = "";
                btn_filter_landfiltert.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        if (!item.equals(getString(R.string.select_land))) {
                            land_id = String.valueOf(landHM.get(btn_filter_landfiltert.getText().toString().trim()));
                        }
                    }
                });

                alertDialog.show();
                tv_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        arrayList.clear();
                        arrayList = sqliteHelper.PSgetRegistrationData(farmer_id);
                        Adapter_PS_LandHolding adapter_ps_landHolding = new Adapter_PS_LandHolding(context, arrayList, screenType);
                        rv_neem_Plantation.setHasFixedSize(true);
                        rv_neem_Plantation.setLayoutManager(new LinearLayoutManager(PS_NeemPlantationList.this));
                        rv_neem_Plantation.setAdapter(adapter_ps_landHolding);
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                if(sharedPrefHelper.getString("plantation_list","").equals("plantation") ) {

                    Intent intent = new Intent(PS_NeemPlantationList.this, ParyavaranSakhiHome.class);
                    startActivity(intent);
                    finish();

                }else{
                    Intent intent = new Intent(PS_NeemPlantationList.this,PS_LandHoldingList.class);
                    startActivity(intent);
                }

                return true;
            case R.id.home_menu:
                Intent intent1 = new Intent(this,ParyavaranSakhiHome.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setLandList() {
        arrayList = sqliteHelper.PSgetRegistrationData("");
        Adapter_PS_LandHolding adapter_ps_landHolding = new Adapter_PS_LandHolding(context, arrayList, screenType);
        rv_neem_Plantation.setHasFixedSize(true);
        rv_neem_Plantation.setLayoutManager(new LinearLayoutManager(this));
        rv_neem_Plantation.setAdapter(adapter_ps_landHolding);
    }

    private void Filter_LandHolding() {
    }





    private void initiliaze() {
        sharedPrefHelper = new SharedPrefHelper(this);
        psNeemPlantationPojos = new ArrayList<>();
        rv_neem_Plantation = findViewById(R.id.rv_neem_Plantation);
        sqliteHelper = new SqliteHelper(this);
        //PlantCount = findViewById(R.id.PlantCount);
        cropplaning_filter = findViewById(R.id.cropplaning_filter);
      //  tv_no_data = findViewById(R.id.tv_no_data);
        landArrayList=new ArrayList<>();
        landHM=new HashMap<>();
        farmarArrayList=new ArrayList<>();
        farmarNameHM=new HashMap<>();
       // filter_List=new ArrayList<>();
        cropplaning_filter = findViewById(R.id.cropplaning_filter);
    }
    @Override
    public void onBackPressed() {

        if(sharedPrefHelper.getString("plantation_list","").equals("plantation") ) {

            Intent intent = new Intent(PS_NeemPlantationList.this, ParyavaranSakhiHome.class);
            startActivity(intent);
            finish();

        }else{
            Intent intent = new Intent(PS_NeemPlantationList.this,PS_LandHoldingList.class);
            startActivity(intent);
        }

    }
    }



