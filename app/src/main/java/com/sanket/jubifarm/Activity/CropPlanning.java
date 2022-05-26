package com.sanket.jubifarm.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanket.jubifarm.Adapter.CropPlanningAdapter;
import com.sanket.jubifarm.Modal.CropPlaningPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CropPlanning extends AppCompatActivity {
    RecyclerView rv_cropplanning;
    ImageView search_farmer;
    CropPlanningAdapter cropPlanningAdapter;
    private ArrayList<CropPlaningPojo> cropPlaningPojoArrayList;
    private ArrayList<CropPlaningPojo> filter_List;
    SharedPrefHelper sharedPrefHelper;
    TextView PlantCount, tv_no_data;
    ImageView cropplaning_filter;
    SqliteHelper sqliteHelper;
    ArrayList<String> cropCategoryList;
    HashMap<String, Integer> cropCategoryHM;
    FloatingActionButton Button;
    private String crop_ = "";
    private String land_ = "", land_id_id = "", screen_type = "", cropdetails = "";
    ArrayList<String> landArrayList;
    HashMap<String, Integer> landHM;
    String lngTypt = "en";
    private ArrayList<String> farmarArrayList;
    private HashMap<String, Integer> farmarNameHM;
    private int farmerId = 0;

    String selected_farmer;
    String farmer_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_planning);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.crop_planning) + "</font>"));

        initViews();

        //get intent values here



        if (lngTypt.equals("en")) {
            setLanguage("en");
        } else if (lngTypt.equals("hin")) {
            setLanguage("hin");
        } else if (lngTypt.equals("guj")) {
            setLanguage("guj");
        } else if (lngTypt.equals("kan")) {
            setLanguage("kan");
        } else {
            //
        }
        selected_farmer = sharedPrefHelper.getString("selected_farmer", "");
        selected_farmer=sharedPrefHelper.getString("selected_farmer","");
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null) {
            land_id_id=bundle.getString("land_id_id", "");
            screen_type=bundle.getString("screen_type", "");
            selected_farmer=bundle.getString("farmer_id", "");
            farmer_id=bundle.getString("farmer_id", "");
            cropdetails=bundle.getString("FormmerDetials", "");

        }
        setCropPlanningAdapter();
        if(screen_type.equals("view_land")){
            Button.setVisibility(View.GONE);
        }else {
            Button.setVisibility(View.VISIBLE);
        }

        fabbuttonAdd();
        Cropfilter();
    }

    private void setCropPlanningAdapter() {
        if (screen_type.equals("view_land")) {
            filter_List.clear();
            filter_List = sqliteHelper.filterPlantList(crop_, land_id_id, farmerId);
            if (filter_List.size() > 0 && !filter_List.get(0).getTotal_plant().equals("0")) {
                tv_no_data.setVisibility(View.GONE);
                cropPlanningAdapter = new CropPlanningAdapter(CropPlanning.this, filter_List,land_id_id,screen_type,selected_farmer);
                int counter = filter_List.size();
                PlantCount.setText(" " + getString(R.string.Crop) +": " + counter);
                rv_cropplanning.setHasFixedSize(true);
                rv_cropplanning.setLayoutManager(new LinearLayoutManager(CropPlanning.this));
                rv_cropplanning.setAdapter(cropPlanningAdapter);
            }
        } else {
            cropPlaningPojoArrayList = sqliteHelper.getPlantList(selected_farmer,"","");
            if (cropPlaningPojoArrayList.size()>0) {
                cropPlanningAdapter = new CropPlanningAdapter(this, cropPlaningPojoArrayList,land_id_id,screen_type,selected_farmer);
                int counter = cropPlaningPojoArrayList.size();
                PlantCount.setText(" " + getString(R.string.Crop) + ": "+ counter);
                rv_cropplanning.setHasFixedSize(true);
                rv_cropplanning.setLayoutManager(new LinearLayoutManager(this));
                rv_cropplanning.setAdapter(cropPlanningAdapter);
            }
        }
    }

    private void initViews() {
        sharedPrefHelper = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);
        lngTypt = sharedPrefHelper.getString("languageID","");

        Button = findViewById(R.id.fab);
        PlantCount = (TextView) findViewById(R.id.PlantCount);
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
        rv_cropplanning = (RecyclerView) findViewById(R.id.rv_cropplanning);
        cropCategoryList = new ArrayList<>();
        cropCategoryHM = new HashMap<>();
        landArrayList=new ArrayList<>();
        landHM=new HashMap<>();
        farmarArrayList=new ArrayList<>();
        farmarNameHM=new HashMap<>();
        filter_List=new ArrayList<>();
        cropplaning_filter = findViewById(R.id.cropplaning_filter);
    }

    private void Cropfilter() {
        cropplaning_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CropPlanning.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.farmer_filter, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();

                LinearLayout ll_farmer=dialogView.findViewById(R.id.ll_farmer);
                String user_type = sharedPrefHelper.getString("user_type", "");
                if (user_type.equals("Farmer") || !selected_farmer.equals("")) {
                    ll_farmer.setVisibility(View.GONE);
                }
                //farmer spinner
                Spinner spn_farmer = dialogView.findViewById(R.id.spn_farmer);
                farmarArrayList.clear();
                farmarNameHM = sqliteHelper.getFarmerspinner();
                for (int i = 0; i < farmarNameHM.size(); i++) {
                    farmarArrayList.add(farmarNameHM.keySet().toArray()[i].toString().trim());

                }
                farmarArrayList.add(0, getString(R.string.select_farmer));
                ArrayAdapter arrayAdapterFarmer = new ArrayAdapter(CropPlanning.this, android.R.layout.simple_dropdown_item_1line, farmarArrayList);
                spn_farmer.setAdapter(arrayAdapterFarmer);

                farmerId=0;
                spn_farmer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!spn_farmer.getSelectedItem().toString().equals(getString(R.string.select_farmer))) {
                            farmerId = farmarNameHM.get(spn_farmer.getSelectedItem().toString().trim());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                Spinner category_cropfilter = (Spinner) dialogView.findViewById(R.id.category_cropfilter);
                cropCategoryList.clear();
                SharedPrefHelper spf = new SharedPrefHelper(CropPlanning.this);
                String language = spf.getString("languageID","");
                if(language.equalsIgnoreCase("hin"))
                {
                    cropCategoryHM = sqliteHelper.getAllCategoryType(2);
                }else if(language.equalsIgnoreCase("kan"))
                {
                    cropCategoryHM = sqliteHelper.getAllCategoryType(3);
                }
                else
                {
                    cropCategoryHM = sqliteHelper.getAllCategoryType(1);
                }
                for (int i = 0; i < cropCategoryHM.size(); i++) {
                    cropCategoryList.add(cropCategoryHM.keySet().toArray()[i].toString().trim());

                }
                cropCategoryList.add(0, getString(R.string.select_crop_category));
                ArrayAdapter arrayAdapterCC = new ArrayAdapter(CropPlanning.this, android.R.layout.simple_dropdown_item_1line, cropCategoryList);
                category_cropfilter.setAdapter(arrayAdapterCC);

                crop_="";
                category_cropfilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!category_cropfilter.getSelectedItem().toString().equals(getString(R.string.select_crop_category))) {
                            crop_ = String.valueOf(cropCategoryHM.get(category_cropfilter.getSelectedItem().toString()));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                Spinner category_landfiltert = (Spinner) dialogView.findViewById(R.id.category_landfiltert);

                landArrayList.clear();
                landHM = sqliteHelper.getLandHoldingList();

                for (int i = 0; i < landHM.size(); i++) {
                    landArrayList.add(landHM.keySet().toArray()[i].toString().trim());
                }
                landArrayList.add(0, getString(R.string.select_land));

                final ArrayAdapter Adapter = new ArrayAdapter(CropPlanning.this, R.layout.spinner_list, landArrayList);
                category_landfiltert.setAdapter(Adapter);

                land_="";
                category_landfiltert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!category_landfiltert.getSelectedItem().toString().equals(getString(R.string.select_land))) {
                            land_ = String.valueOf(landHM.get(category_landfiltert.getSelectedItem().toString().trim()));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                TextView search_filter = (TextView) dialogView.findViewById(R.id.search_filter);
                search_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filter_List.clear();
                        filter_List = sqliteHelper.filterPlantList(crop_, land_, farmerId);
                        if (filter_List.size()>0 && !filter_List.get(0).getTotal_plant().equals("0") ) {
                            tv_no_data.setVisibility(View.GONE);
                            cropPlanningAdapter = new CropPlanningAdapter(CropPlanning.this, filter_List,land_id_id,screen_type,selected_farmer);
                            int counter = filter_List.size();
                            PlantCount.setText(" " + getString(R.string.Crop) + ": " + counter);
                            rv_cropplanning.setHasFixedSize(true);
                            rv_cropplanning.setLayoutManager(new LinearLayoutManager(CropPlanning.this));
                            rv_cropplanning.setAdapter(cropPlanningAdapter);
                            alertDialog.dismiss();
                        }else {
                            filter_List.clear();
                            cropPlanningAdapter = new CropPlanningAdapter(CropPlanning.this, filter_List,land_id_id,screen_type,selected_farmer);
                            int counter = filter_List.size();
                            PlantCount.setText(" "+getString(R.string.Crop)+": "+ counter);
                            rv_cropplanning.setHasFixedSize(true);
                            rv_cropplanning.setLayoutManager(new LinearLayoutManager(CropPlanning.this));
                            rv_cropplanning.setAdapter(cropPlanningAdapter);
                            tv_no_data.setVisibility(View.VISIBLE);
                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void fabbuttonAdd() {
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Regintent = new Intent(CropPlanning.this, AddPlantAcivity.class);
                Regintent.putExtra("farmer_id",farmer_id);
                Regintent.putExtra("screen_type","fromcropplanning");
                startActivity(Regintent);
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home_menu) {


                super.onBackPressed();
                Intent intentLandHoldingActivity = new Intent(CropPlanning.this, HomeAcivity.class);
                startActivity(intentLandHoldingActivity);
                finish();

        }

        if (item.getItemId() == android.R.id.home) {
            if (screen_type.equals("farmer_details")){
                Intent intentLandHoldingActivity = new Intent(CropPlanning.this, FarmerDeatilActivity.class);
                intentLandHoldingActivity.putExtra("land_id_id",land_id_id);
                String user_id=sqliteHelper.getUserID(Integer.parseInt(farmer_id));
                intentLandHoldingActivity.putExtra("user_id",user_id);
                startActivity(intentLandHoldingActivity);
                finish();
            }else
            if (screen_type != null && !screen_type.equals("")) {
                super.onBackPressed();
                Intent intentLandHoldingActivity = new Intent(CropPlanning.this, ViewLandActivity.class);
                intentLandHoldingActivity.putExtra("land_id_id",land_id_id);
                intentLandHoldingActivity.putExtra("farmer_id",farmer_id);
                startActivity(intentLandHoldingActivity);
                finish();
            }  else  {
                super.onBackPressed();
                Intent intentLandHoldingActivity = new Intent(CropPlanning.this, HomeAcivity.class);
                startActivity(intentLandHoldingActivity);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void setLanguage(String languageToLoad) {
        if (!languageToLoad.equals("")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = getBaseContext().getResources().getConfiguration();
                //config.locale = locale;
                config.setLocale(locale);
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

            } else {
                Resources resources = getBaseContext().getResources();
                Configuration configuration = resources.getConfiguration();
                //configuration.setLocale(new Locale(lang));
                configuration.locale = new Locale(languageToLoad);
                getBaseContext().getApplicationContext().createConfigurationContext(configuration);
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (screen_type.equals("crop_monitoring")){

        }else if (screen_type.equals("farmer_details")){
            Intent intentLandHoldingActivity = new Intent(CropPlanning.this, FarmerDeatilActivity.class);
            intentLandHoldingActivity.putExtra("land_id_id",land_id_id);
            String user_id=sqliteHelper.getUserID(Integer.parseInt(farmer_id));
            intentLandHoldingActivity.putExtra("user_id",user_id);
            startActivity(intentLandHoldingActivity);
            finish();
        }else
        if (screen_type != null && !screen_type.equals("")) {
            super.onBackPressed();
            Intent intentLandHoldingActivity = new Intent(CropPlanning.this, ViewLandActivity.class);
            intentLandHoldingActivity.putExtra("land_id_id",land_id_id);
            intentLandHoldingActivity.putExtra("farmer_id",farmer_id);
            startActivity(intentLandHoldingActivity);
            finish();
        }  else  {
            super.onBackPressed();
            Intent intentLandHoldingActivity = new Intent(CropPlanning.this, HomeAcivity.class);
            startActivity(intentLandHoldingActivity);
            finish();
        }

    }
}