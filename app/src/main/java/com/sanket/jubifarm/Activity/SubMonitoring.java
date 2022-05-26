package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanket.jubifarm.Adapter.SubCropPlaningAdapter;
import com.sanket.jubifarm.Adapter.SubMonitoringAdapter;
import com.sanket.jubifarm.Modal.CropPlaningPojo;
import com.sanket.jubifarm.Modal.PlantSubCategoryPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class SubMonitoring extends AppCompatActivity {
    private RecyclerView sub_monitoring_recview;
    private ImageView cropplaning_filter;
    private TextView tv_no_data, PlantCount;

    /*normal widgets*/
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sharedPrefHelper;
    private ArrayList<PlantSubCategoryPojo> plantSubCategoryPojoArrayList;
    private String cropCategory_id;
    private SubMonitoringAdapter subCropPlaningAdapter;
    private ArrayList<PlantSubCategoryPojo> filter_List;
    private ArrayList<String> farmarArrayList;
    private HashMap<String, Integer> farmarNameHM;
    private int farmerId = 0;
    private ArrayList<String> cropCategoryList;
    private HashMap<String, Integer> cropCategoryHM;
    private ArrayList<String> landArrayList;
    private HashMap<String, Integer> landHM;
    private String land = "";
    private String subcropCategory="";
    private HashMap<String, Integer> subCategoryHM;
    private String cropCategory="";
    private ArrayList<String> subCategoryArrayList;
    private String selected_farmer;
    private String land_id_id;
    private String screen_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_monitoring);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.Sub_Monitoring_List) + "</font>"));

        initViews();
        /*get intent values here*/
       selected_farmer= sharedPrefHelper.getString("selected_farmer","");
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            cropCategory_id=bundle.getString("cropCategory_id","");
            selected_farmer = bundle.getString("farmer_id", "");
            land_id_id = bundle.getString("land_id_id", "");
            screen_type = bundle.getString("screen_type", "");

        }

        setAdapter();
        setSubMonitoringFilter();
    }

    private void setSubMonitoringFilter() {
        cropplaning_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(SubMonitoring.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.subcroping_filter, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();

                //farmer spinner
                LinearLayout ll_farmer = dialogView.findViewById(R.id.ll_farmer);
                if (sharedPrefHelper.getString("user_type", "").equals("Farmer")) {
                    ll_farmer.setVisibility(View.GONE);
                }
                MaterialSpinner spn_farmer = dialogView.findViewById(R.id.spn_farmer);
                farmarArrayList.clear();
                farmarNameHM = sqliteHelper.getFarmerspinner();
                for (int i = 0; i < farmarNameHM.size(); i++) {
                    farmarArrayList.add(farmarNameHM.keySet().toArray()[i].toString().trim());

                }
                farmarArrayList.add(0, getString(R.string.select_farmer));
                ArrayAdapter arrayAdapterFarmer = new ArrayAdapter(SubMonitoring.this, android.R.layout.simple_dropdown_item_1line, farmarArrayList);
                spn_farmer.setAdapter(arrayAdapterFarmer);

                farmerId=0;
                spn_farmer.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        if (!item.equals(getString(R.string.select_farmer))) {
                            farmerId = farmarNameHM.get(spn_farmer.getText().toString().trim());
                        }
                    }
                });

                MaterialSpinner btn_filter_Subcropfilter = (MaterialSpinner) dialogView.findViewById(R.id.btn_filter_Subcropfilter);

                subcropCategory="";
                btn_filter_Subcropfilter.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        subcropCategory = String.valueOf(subCategoryHM.get(item));
                    }
                });
                MaterialSpinner btn_filter_cropfilter = (MaterialSpinner) dialogView.findViewById(R.id.btn_filter_cropfilter);
                cropCategoryList.clear();
                SharedPrefHelper spf = new SharedPrefHelper(SubMonitoring.this);
                String language = spf.getString("languageID","");
                if(language.equalsIgnoreCase("hin"))
                {
                    cropCategoryHM = sqliteHelper.getAllCategoryType(2);
                }else if(language.equalsIgnoreCase("kan")) {
                    cropCategoryHM = sqliteHelper.getAllCategoryType(3);
                } else {
                    cropCategoryHM = sqliteHelper.getAllCategoryType(1);
                }
                for (int i = 0; i < cropCategoryHM.size(); i++) {
                    cropCategoryList.add(cropCategoryHM.keySet().toArray()[i].toString().trim());

                }
                cropCategoryList.add(0, getString(R.string.Select_Crop_Category));
                btn_filter_cropfilter.setItems(cropCategoryList);

                cropCategory="";
                btn_filter_cropfilter.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String SUBcrop) {
                        cropCategory = String.valueOf(cropCategoryHM.get(SUBcrop));
                        subCategoryArrayList.clear();
                        SharedPrefHelper spf = new SharedPrefHelper(SubMonitoring.this);
                        String language = spf.getString("languageID","");
                        if(language.equalsIgnoreCase("hin"))
                        {
                            subCategoryHM = sqliteHelper.getAllSubCategory(Integer.parseInt(cropCategory), 2);
                        } else if(language.equalsIgnoreCase("kan"))
                        {
                            subCategoryHM = sqliteHelper.getAllSubCategory(Integer.parseInt(cropCategory), 3);
                        }
                        else
                        {
                            subCategoryHM = sqliteHelper.getAllSubCategory(Integer.parseInt(cropCategory), 1);
                        }
                        for (int i = 0; i < subCategoryHM.size(); i++) {
                            subCategoryArrayList.add(subCategoryHM.keySet().toArray()[i].toString().trim());
                        }
                        subCategoryArrayList.add(0, getString(R.string.Select_Subcategory));
                        btn_filter_Subcropfilter.setItems(subCategoryArrayList);

                    }
                });
                MaterialSpinner btn_filter_landfiltert = (MaterialSpinner) dialogView.findViewById(R.id.btn_filter_landfiltert);

                landArrayList.clear();
                landHM = sqliteHelper.getLandHoldingList();

                for (int i = 0; i < landHM.size(); i++) {
                    landArrayList.add(landHM.keySet().toArray()[i].toString().trim());
                }
                landArrayList.add(0, getString(R.string.select_land));

                final ArrayAdapter Adapter = new ArrayAdapter(SubMonitoring.this, R.layout.spinner_list, landArrayList);
                Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                btn_filter_landfiltert.setAdapter(Adapter);

                land="";
                btn_filter_landfiltert.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        if (!item.equals(getString(R.string.select_land))) {
                            land = String.valueOf(landHM.get(btn_filter_landfiltert.getText().toString().trim()));
                        }
                    }
                });

                TextView search_filter = (TextView) dialogView.findViewById(R.id.tv_search);
                search_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filter_List.clear();
                        filter_List = sqliteHelper.getSubCategoryListFilter(cropCategory, subcropCategory, land, farmerId);
                        if (filter_List.size() > 0) {
                            tv_no_data.setVisibility(View.GONE);
                            subCropPlaningAdapter = new SubMonitoringAdapter(SubMonitoring.this, filter_List);
                            int counter = filter_List.size();
                            PlantCount.setText(" " +getString(R.string.Crop) + ": " + counter);
                            sub_monitoring_recview.setHasFixedSize(true);
                            sub_monitoring_recview.setLayoutManager(new LinearLayoutManager(SubMonitoring.this));
                            sub_monitoring_recview.setAdapter(subCropPlaningAdapter);
                            alertDialog.dismiss();
                        } else {
                            filter_List.clear();
                            subCropPlaningAdapter = new SubMonitoringAdapter(SubMonitoring.this, filter_List);
                            int counter = filter_List.size();
                            PlantCount.setText(" " +getString(R.string.Crop) + ": " + counter);
                            sub_monitoring_recview.setHasFixedSize(true);
                            sub_monitoring_recview.setLayoutManager(new LinearLayoutManager(SubMonitoring.this));
                            sub_monitoring_recview.setAdapter(subCropPlaningAdapter);
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
        plantSubCategoryPojoArrayList = sqliteHelper.getSubCategoryList(cropCategory_id,selected_farmer,land_id_id,screen_type);
        subCropPlaningAdapter = new SubMonitoringAdapter(this, plantSubCategoryPojoArrayList);
        int counter = plantSubCategoryPojoArrayList.size();
        PlantCount.setText(" " + getString(R.string.Crop) + ": " + counter);
        sub_monitoring_recview.setHasFixedSize(true);
        sub_monitoring_recview.setLayoutManager(new LinearLayoutManager(this));
        sub_monitoring_recview.setAdapter(subCropPlaningAdapter);
    }

    private void initViews() {
        sub_monitoring_recview = findViewById(R.id.sub_monitoring_recview);
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper =new SharedPrefHelper(this);
        cropplaning_filter=findViewById(R.id.cropplaning_filter);
        filter_List = new ArrayList<>();
        farmarArrayList = new ArrayList<>();
        farmarNameHM = new HashMap<>();
        cropCategoryList = new ArrayList<>();
        cropCategoryHM = new HashMap<>();
        tv_no_data = findViewById(R.id.tv_no_data);
        PlantCount = findViewById(R.id.PlantCount);
        landArrayList = new ArrayList<>();
        landHM = new HashMap<>();
        subCategoryHM = new HashMap<>();
        subCategoryArrayList = new ArrayList<>();
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