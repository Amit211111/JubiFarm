package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanket.jubifarm.Adapter.CropMoneroringAdapter;
import com.sanket.jubifarm.Adapter.CropPlanningAdapter;
import com.sanket.jubifarm.Modal.CropPlaningPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class CropMonitoring extends AppCompatActivity {
    private RecyclerView rec_cropmonetoring;
    private ExtendedFloatingActionButton fab_visit_plant;
    private ImageView cropplaning_filter;
    private TextView tv_no_data, PlantCount;

    /*normal widgets*/
    private Context context = this;
    private SharedPrefHelper sharedPrefHelper;
    private SqliteHelper sqliteHelper;
    private ArrayList<CropPlaningPojo> cropPlaningPojoArrayList;
    private CropMoneroringAdapter cropmonetoringAdapter;
    private ArrayList<CropPlaningPojo> filter_List;
    private ArrayList<String> farmarArrayList;
    private HashMap<String, Integer> farmarNameHM;
    private int farmerId = 0;
    private ArrayList<String> cropCategoryList;
    private HashMap<String, Integer> cropCategoryHM;
    private String crop_ = "";
    private ArrayList<String> landArrayList;
    private HashMap<String, Integer> landHM;
    private String land_ = "";
    private String selected_farmer = "";
    private String from_view_land = "";
    private String  land_id_id = "", screen_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_monitoring);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.Crop_Monitoring) + "</font>"));

        initViews();
        Bundle bundle=getIntent().getExtras();
        selected_farmer=sharedPrefHelper.getString("selected_farmer","");
        if (bundle!=null) {
            land_id_id=bundle.getString("land_id_id", "");
            screen_type=bundle.getString("screen_type", "");
            selected_farmer=bundle.getString("farmer_id", "");
            from_view_land=bundle.getString("from_view_land", "");
            land_id_id=bundle.getString("land_id_id", "");

        }

        setAdapter();
        cropMonitoringFilter();
        setVisitPlantClick();
    }

    private void setVisitPlantClick() {
        fab_visit_plant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVisitPlant=new Intent(context, VisitPlant.class);
                startActivity(intentVisitPlant);
                finish();
            }
        });
    }

    private void cropMonitoringFilter() {
        cropplaning_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CropMonitoring.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.farmer_filter, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();

                LinearLayout ll_farmer = dialogView.findViewById(R.id.ll_farmer);
                String user_type = sharedPrefHelper.getString("user_type", "");
                if (user_type.equals("Farmer")) {
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
                ArrayAdapter arrayAdapterFarmer = new ArrayAdapter(CropMonitoring.this, android.R.layout.simple_dropdown_item_1line, farmarArrayList);
                spn_farmer.setAdapter(arrayAdapterFarmer);

                farmerId = 0;
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
                SharedPrefHelper spf = new SharedPrefHelper(CropMonitoring.this);
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
                ArrayAdapter arrayAdapterCC = new ArrayAdapter(CropMonitoring.this, android.R.layout.simple_dropdown_item_1line, cropCategoryList);
                category_cropfilter.setAdapter(arrayAdapterCC);

                crop_ = "";
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

                final ArrayAdapter Adapter = new ArrayAdapter(CropMonitoring.this, R.layout.spinner_list, landArrayList);
                category_landfiltert.setAdapter(Adapter);

                land_ = "";
                category_landfiltert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!category_landfiltert.getSelectedItem().toString().equals(getString(R.string.select_land))) {
                            land_ = String.valueOf(landHM.get(category_landfiltert.getSelectedItem().toString().toString().trim()));
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
                        if (filter_List.size() > 0 && !filter_List.get(0).getTotal_plant().equals("0")) {
                            tv_no_data.setVisibility(View.GONE);
                            cropmonetoringAdapter = new CropMoneroringAdapter(CropMonitoring.this, filter_List,land_id_id,screen_type,selected_farmer);
                            int counter = filter_List.size();
                            PlantCount.setText(" " + getString(R.string.Crop) + ": " + counter);
                            rec_cropmonetoring.setHasFixedSize(true);
                            rec_cropmonetoring.setLayoutManager(new LinearLayoutManager(CropMonitoring.this));
                            rec_cropmonetoring.setAdapter(cropmonetoringAdapter);
                            alertDialog.dismiss();
                        } else {
                            filter_List.clear();
                            cropmonetoringAdapter = new CropMoneroringAdapter(CropMonitoring.this, filter_List,land_id_id,screen_type,selected_farmer);
                            int counter = filter_List.size();
                            PlantCount.setText(" " + getString(R.string.Crop) + ": " + counter);
                            rec_cropmonetoring.setHasFixedSize(true);
                            rec_cropmonetoring.setLayoutManager(new LinearLayoutManager(CropMonitoring.this));
                            rec_cropmonetoring.setAdapter(cropmonetoringAdapter);
                            tv_no_data.setVisibility(View.VISIBLE);
                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void setAdapter() {
        cropPlaningPojoArrayList = sqliteHelper.getPlantList(selected_farmer,land_id_id,screen_type);
        cropmonetoringAdapter = new CropMoneroringAdapter(this, cropPlaningPojoArrayList,land_id_id,screen_type,selected_farmer);
        int counter = cropPlaningPojoArrayList.size();
        PlantCount.setText(" " + getString(R.string.Crop) + ": " + counter);
        rec_cropmonetoring.setHasFixedSize(true);
        rec_cropmonetoring.setLayoutManager(new LinearLayoutManager(this));
        rec_cropmonetoring.setAdapter(cropmonetoringAdapter);
    }

    private void initViews() {
        sharedPrefHelper = new SharedPrefHelper(this);
        rec_cropmonetoring = findViewById(R.id.rec_cropmonetoring);
        sqliteHelper = new SqliteHelper(this);
        fab_visit_plant = findViewById(R.id.fab_visit_plant);
        cropPlaningPojoArrayList = new ArrayList<>();
        filter_List = new ArrayList<>();
        cropplaning_filter = findViewById(R.id.cropplaning_filter);
        farmarArrayList = new ArrayList<>();
        farmarNameHM = new HashMap<>();
        cropCategoryList = new ArrayList<>();
        cropCategoryHM = new HashMap<>();
        tv_no_data = findViewById(R.id.tv_no_data);
        PlantCount = findViewById(R.id.PlantCount);
        landArrayList = new ArrayList<>();
        landHM = new HashMap<>();
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

            Intent intent = new Intent(this, HomeAcivity.class);
            this.startActivity(intent);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
             if (!from_view_land.equals("")){
                Intent intent=new Intent(CropMonitoring.this,ViewLandActivity.class);
                intent.putExtra("land_id_id",land_id_id);
                intent.putExtra("farmer_id",selected_farmer);
                startActivity(intent);
                finish();
            }else
            if (screen_type.equals("crop_monitoring")){
                Intent intent=new Intent(CropMonitoring.this,LandHoldingActivity.class);
                intent.putExtra("fromMonitoring","1");
                startActivity(intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
          if (!from_view_land.equals("")){
            Intent intent=new Intent(CropMonitoring.this,ViewLandActivity.class);
              intent.putExtra("land_id_id",land_id_id);
              intent.putExtra("farmer_id",selected_farmer);
            startActivity(intent);
            finish();
        }else
        if (screen_type.equals("crop_monitoring")){
            Intent intent=new Intent(CropMonitoring.this,LandHoldingActivity.class);
            intent.putExtra("fromMonitoring","1");
            startActivity(intent);
            finish();
        }else{
            Intent intent=new Intent(CropMonitoring.this,LandHoldingActivity.class);
            startActivity(intent);
            finish();
        }
    }
}