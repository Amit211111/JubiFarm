package com.sanket.jubifarm.Activity;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanket.jubifarm.Adapter.SubCropPlaningAdapter;
import com.sanket.jubifarm.Modal.PlantSubCategoryPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class SubCropPlaninigActivity extends AppCompatActivity {
    RecyclerView rv_subcropplanning;
    ImageView cropplaning_filter;
    SubCropPlaningAdapter subCropPlaningAdapter;
    private ArrayList<PlantSubCategoryPojo> plantSubCategoryPojoArrayList;
    private ArrayList<PlantSubCategoryPojo> filter_List;

    TextView subcropCount, tv_no_data;
    SqliteHelper sqliteHelper;
    FloatingActionButton Button;
    String cropCategory="";
    String land="";
    String subcropCategory="";
    String cropCategory_id,farmer_id="";
    ArrayList<String> cropCategoryList;
    ArrayList<String> subCategoryArrayList;
    HashMap<String, Integer> cropCategoryHM;
    HashMap<String, Integer> subCategoryHM;
    SharedPrefHelper sharedPrefHelper;
    ArrayList<String> landArrayList;
    HashMap<String, Integer> landHM;
    private ArrayList<String> farmarArrayList;
    private HashMap<String, Integer> farmarNameHM;
    private int farmerId=0;
    private String selected_farmer;
    private String land_id_id;
    private String screen_type;
    private String fromcropPlaniong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_crop_planinig);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.SUB_CROP_LIST) + "</font>"));

        initViews();

        selected_farmer=sharedPrefHelper.getString("selected_farmer","");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cropCategory_id = bundle.getString("cropCategory_id", "");
            farmer_id = bundle.getString("farmer_id", "");
            land_id_id = bundle.getString("land_id_id", "");
            screen_type = bundle.getString("screen_type", "");
            fromcropPlaniong = bundle.getString("fromcropPlaniong", "");

        }
        if (farmer_id.equals("")){
          farmer_id=selected_farmer;
        }
        if(screen_type.equals("view_land")){
            Button.setVisibility(View.GONE);
        }else {
            Button.setVisibility(View.VISIBLE);
        }
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Regintent = new Intent(SubCropPlaninigActivity.this, AddPlantAcivity.class);
                Regintent.putExtra("screen_type","fromcropplanning");
                startActivity(Regintent);
                finish();
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });


        cropplaning_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(SubCropPlaninigActivity.this);
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
                ArrayAdapter arrayAdapterFarmer = new ArrayAdapter(SubCropPlaninigActivity.this, android.R.layout.simple_dropdown_item_1line, farmarArrayList);
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
                SharedPrefHelper spf = new SharedPrefHelper(SubCropPlaninigActivity.this);
                String language = spf.getString("languageID","");
                if(language.equalsIgnoreCase("hin"))
                {
                    cropCategoryHM = sqliteHelper.getAllCategoryType(2);
                }
                else
                {
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
                        SharedPrefHelper spf = new SharedPrefHelper(SubCropPlaninigActivity.this);
                        String language = spf.getString("languageID","");
                        if(language.equalsIgnoreCase("hin"))
                        {
                            subCategoryHM = sqliteHelper.getAllSubCategory(Integer.parseInt(cropCategory), 2);
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

                final ArrayAdapter Adapter = new ArrayAdapter(SubCropPlaninigActivity.this, R.layout.spinner_list, landArrayList);
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
                            subCropPlaningAdapter = new SubCropPlaningAdapter(SubCropPlaninigActivity.this, filter_List);
                            int counter = filter_List.size();
                            subcropCount.setText(" " + getString(R.string.Crop) + ": " + counter);
                            rv_subcropplanning.setHasFixedSize(true);
                            rv_subcropplanning.setLayoutManager(new LinearLayoutManager(SubCropPlaninigActivity.this));
                            rv_subcropplanning.setAdapter(subCropPlaningAdapter);
                            alertDialog.dismiss();
                        } else {
                            filter_List.clear();
                            subCropPlaningAdapter = new SubCropPlaningAdapter(SubCropPlaninigActivity.this, filter_List);
                            int counter = filter_List.size();
                            subcropCount.setText(" " + getString(R.string.Crop) + ": " + counter);
                            rv_subcropplanning.setHasFixedSize(true);
                            rv_subcropplanning.setLayoutManager(new LinearLayoutManager(SubCropPlaninigActivity.this));
                            rv_subcropplanning.setAdapter(subCropPlaningAdapter);
                            tv_no_data.setVisibility(View.VISIBLE);
                            alertDialog.dismiss();
                        }
                    }


                });


                alertDialog.show();


            }
        });

        plantSubCategoryPojoArrayList = sqliteHelper.getSubCategoryList(cropCategory_id,farmer_id,land_id_id,screen_type);
        subCropPlaningAdapter = new SubCropPlaningAdapter(this, plantSubCategoryPojoArrayList);
        int counter = plantSubCategoryPojoArrayList.size();
        subcropCount.setText(" " + getString(R.string.Crop) + ": "+ counter);
        rv_subcropplanning.setHasFixedSize(true);
        rv_subcropplanning.setLayoutManager(new LinearLayoutManager(this));
        rv_subcropplanning.setAdapter(subCropPlaningAdapter);

    }

    private void initViews() {
        sqliteHelper = new SqliteHelper(this);
        Button = findViewById(R.id.fab);
        subcropCount = findViewById(R.id.subcropCount);
        tv_no_data = findViewById(R.id.tv_no_data);
        cropplaning_filter = findViewById(R.id.cropplaning_filter);
        cropCategoryHM = new HashMap<>();
        subCategoryHM = new HashMap<>();
        sharedPrefHelper = new SharedPrefHelper(this);
        rv_subcropplanning = (RecyclerView) findViewById(R.id.rv_cropplanning);
        landArrayList=new ArrayList<>();
        landHM=new HashMap<>();
        filter_List=new ArrayList<>();
        farmarArrayList=new ArrayList<>();
        farmarNameHM=new HashMap<>();
        cropCategoryList = new ArrayList<>();
        subCategoryArrayList = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.home_menu) {
//
//            Intent intent = new Intent(this, HomeAcivity.class);
//            this.startActivity(intent);
//            return true;
//        }
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.home_menu) {

            Intent intent = new Intent(this, HomeAcivity.class);
            this.startActivity(intent);
            return true;
        }
        String data1 = getIntent().getExtras().getString("fromcropPlaniong");
        if (data1 != null) {
            if (item.getItemId() == android.R.id.home) {
                Intent intent = new Intent(SubCropPlaninigActivity.this, CropPlanning.class);
                startActivity(intent);
                finish();
            }
        } else {
            if (item.getItemId() == android.R.id.home) {
                Intent intent = new Intent(SubCropPlaninigActivity.this, CropPlanning.class);
                startActivity(intent);
                finish();
            }

        }


        return super.onOptionsItemSelected(item);
    }

}
