package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanket.jubifarm.Adapter.LandHoldingAdapter;
import com.sanket.jubifarm.Adapter.VisitPlantAdapter;
import com.sanket.jubifarm.Modal.LandHoldingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.interfaces.ClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class LandHoldingActivity extends AppCompatActivity {
    private RecyclerView rvLandHoldning;
    private ImageView Landholding_filter;
    private LinearLayout ll_filter_LHtxt;
    private TextView landCount;
    private FloatingActionButton fab;

    /*normal widgets*/
    private Context context=this;
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sharedPrefHelper;
    private LandHoldingAdapter landHoldingAdapter;
    private ArrayList<LandHoldingPojo> landHoldingPojoArrayList;

    private ArrayList<String> farmarArrayList = new ArrayList<>();
    private HashMap<String, Integer> farmarNameHM;
    private String farmerId="";
    private String selected_farmer="";
    private String screen_type="";
    private String fromMonitoring="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_holding);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.LAND_HOLDING) + "</font>"));

        initViews();

        /*get intent values here*/
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            selected_farmer=bundle.getString("farmer_id","");
            screen_type=bundle.getString("screen_type","");
            fromMonitoring=bundle.getString("fromMonitoring","");
        }
        if (selected_farmer.equals("")){
            selected_farmer=sharedPrefHelper.getString("selected_farmer","");
        }

        setAdapter();
        if (!fromMonitoring.equals("")){
            setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.LAND_HOLDING) + "</font>"));
            fab.setVisibility(View.GONE);
        }else {
            fab.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addLandIntent = new Intent(context, AddLandActivity.class);
                startActivity(addLandIntent);
                finish();
            }
        });

        if (sharedPrefHelper.getString("role_id", "").equals("5") || !selected_farmer.equals("")) {
            ll_filter_LHtxt.setVisibility(View.GONE);
        } else {
            ll_filter_LHtxt.setVisibility(View.VISIBLE);
            Filter_LandHolding();
        }
    }

    private void setAdapter() {
        landHoldingPojoArrayList = sqliteHelper.getLandList(selected_farmer);
        landHoldingAdapter = new LandHoldingAdapter(this, landHoldingPojoArrayList);
        int counter = landHoldingPojoArrayList.size();
        landCount.setText(" "+getString(R.string.Land) +": "+ counter);
        rvLandHoldning.setHasFixedSize(true);
        rvLandHoldning.setLayoutManager(new LinearLayoutManager(this));
        rvLandHoldning.setAdapter(landHoldingAdapter);

        landHoldingAdapter.onViewLandClick(new ClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onViewLandClick(int position) {
                if (fromMonitoring.equals("")) {
                    Intent viewLandIntent = new Intent(context, ViewLandActivity.class);
                    viewLandIntent.putExtra("land_id", landHoldingPojoArrayList.get(position).getLand_id());
                    viewLandIntent.putExtra("land_area", landHoldingPojoArrayList.get(position).getArea());
                    viewLandIntent.putExtra("farmer_id", landHoldingPojoArrayList.get(position).getFarmer_id());
                    viewLandIntent.putExtra("farmer_name", sqliteHelper.getNameById("farmer_registration", "farmer_name", "id", Integer.parseInt(landHoldingPojoArrayList.get(position).getFarmer_id())));
                    viewLandIntent.putExtra("total_plant", landHoldingPojoArrayList.get(position).getTotal_plant());
                    viewLandIntent.putExtra("unit", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getLand_unit())));
                    viewLandIntent.putExtra("land_image", landHoldingPojoArrayList.get(position).getImage());
                    viewLandIntent.putExtra("latitude", landHoldingPojoArrayList.get(position).getLatitude());
                    viewLandIntent.putExtra("longitude", landHoldingPojoArrayList.get(position).getLongitude());
                    viewLandIntent.putExtra("p", landHoldingPojoArrayList.get(position).getP());
                    viewLandIntent.putExtra("s", landHoldingPojoArrayList.get(position).getS());
                    viewLandIntent.putExtra("mg", landHoldingPojoArrayList.get(position).getMg());
                    viewLandIntent.putExtra("k", landHoldingPojoArrayList.get(position).getK());
                    viewLandIntent.putExtra("n", landHoldingPojoArrayList.get(position).getN());
                    viewLandIntent.putExtra("ca", landHoldingPojoArrayList.get(position).getCa());
                    viewLandIntent.putExtra("bulk_density", landHoldingPojoArrayList.get(position).getBulk_density());
                    viewLandIntent.putExtra("filtration_rate", landHoldingPojoArrayList.get(position).getFiltration_rate());
                    viewLandIntent.putExtra("soil_texture", landHoldingPojoArrayList.get(position).getSoil_texture());
                    viewLandIntent.putExtra("cation_exchange_capacity", landHoldingPojoArrayList.get(position).getCation_exchange_capacity());
                    viewLandIntent.putExtra("ph", landHoldingPojoArrayList.get(position).getPh());
                    viewLandIntent.putExtra("ec", landHoldingPojoArrayList.get(position).getEc());
                    viewLandIntent.putExtra("land_name", landHoldingPojoArrayList.get(position).getLand_name());
                    viewLandIntent.putExtra("soil_type_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_type_id())));
                    viewLandIntent.putExtra("soil_color_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_color_id())));
                    //viewLandIntent.putExtra("soil_characteristics_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_characteristics_id())));
                    //viewLandIntent.putExtra("soil_chemical_composition_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_chemical_composition_id())));
                    viewLandIntent.putExtra("land_id_id", landHoldingPojoArrayList.get(position).getId());
                    viewLandIntent.putExtra("unit_id", landHoldingPojoArrayList.get(position).getLand_unit());
                    viewLandIntent.putExtra("land_image", landHoldingPojoArrayList.get(position).getImage());

                    startActivity(viewLandIntent);
                }else {
                    Intent intent = new Intent(LandHoldingActivity.this, CropMonitoring.class);
                    intent.putExtra("land_id_id", landHoldingPojoArrayList.get(position).getId()); //land_id for view list according to land id.
                    intent.putExtra("screen_type", "crop_monitoring");
                    intent.putExtra("farmer_id", landHoldingPojoArrayList.get(position).getFarmer_id());
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onEditLandClick(int position) {
                Intent addLandIntent=new Intent(context, AddLandActivity.class);
                addLandIntent.putExtra("screen_type", "edit_land");
                addLandIntent.putExtra("land_area", landHoldingPojoArrayList.get(position).getArea());
                addLandIntent.putExtra("farmer_id", landHoldingPojoArrayList.get(position).getFarmer_id());
                addLandIntent.putExtra("farmer_name", sqliteHelper.getNameById("farmer_registration", "farmer_name", "id", Integer.parseInt(landHoldingPojoArrayList.get(position).getFarmer_id())));
                addLandIntent.putExtra("unit", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getLand_unit())));
                addLandIntent.putExtra("unit_id", landHoldingPojoArrayList.get(position).getLand_unit());
                addLandIntent.putExtra("P", landHoldingPojoArrayList.get(position).getP());
                addLandIntent.putExtra("s", landHoldingPojoArrayList.get(position).getS());
                addLandIntent.putExtra("mg", landHoldingPojoArrayList.get(position).getMg());
                addLandIntent.putExtra("k", landHoldingPojoArrayList.get(position).getK());
                addLandIntent.putExtra("n", landHoldingPojoArrayList.get(position).getN());
                addLandIntent.putExtra("ca", landHoldingPojoArrayList.get(position).getCa());
                addLandIntent.putExtra("land_image", landHoldingPojoArrayList.get(position).getImage());
                addLandIntent.putExtra("land_id", landHoldingPojoArrayList.get(position).getLand_id());
                addLandIntent.putExtra("soil_type_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_type_id())));
                addLandIntent.putExtra("soil_color_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_color_id())));
    //            addLandIntent.putExtra("soil_characteristics_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_characteristics_id())));
    //            addLandIntent.putExtra("soil_chemical_composition_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_chemical_composition_id())));
                startActivity(addLandIntent);
            }

            @Override
            public void onCheckBoxClick(int position) {

            }


        });
    }

    private void initViews() {
        landCount = findViewById(R.id.landCount);
        rvLandHoldning = findViewById(R.id.rvLandHoldning);
        sharedPrefHelper = new SharedPrefHelper(this);
        Landholding_filter = findViewById(R.id.Landholding_filter);
        fab = findViewById(R.id.fab);
        ll_filter_LHtxt = findViewById(R.id.ll_filter_LHtxt);
        sqliteHelper = new SqliteHelper(this);
        farmarNameHM=new HashMap<>();
    }

    private void Filter_LandHolding() {
        Landholding_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(LandHoldingActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_farmer_filter, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                Spinner btn_filter_farmerfiltert = (Spinner) dialogView.findViewById(R.id.btn_filter_farmer_filtert);
                TextView tv_search = (TextView) dialogView.findViewById(R.id.tv_search);

                farmarArrayList.clear();
                farmarNameHM = sqliteHelper.getFarmerspinner();

                for (int i = 0; i < farmarNameHM.size(); i++) {
                    farmarArrayList.add(farmarNameHM.keySet().toArray()[i].toString().trim());
                }
                farmarArrayList.add(0, getString(R.string.select_farmer));
                ArrayAdapter arrayAdapterFarmer = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, farmarArrayList);
                btn_filter_farmerfiltert.setAdapter(arrayAdapterFarmer);

                farmerId="";
                btn_filter_farmerfiltert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!btn_filter_farmerfiltert.getSelectedItem().toString().equals(getString(R.string.select_farmer))){
                            farmerId = String.valueOf(farmarNameHM.get(btn_filter_farmerfiltert.getSelectedItem().toString().trim()));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                tv_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        landHoldingPojoArrayList.clear();
                        landHoldingPojoArrayList = sqliteHelper.getLandList(farmerId);
                        landHoldingAdapter = new LandHoldingAdapter(LandHoldingActivity.this, landHoldingPojoArrayList);
                        int counter = landHoldingPojoArrayList.size();
                        landCount.setText(" "+getString(R.string.Land) +": "+ counter);
                        rvLandHoldning.setHasFixedSize(true);
                        rvLandHoldning.setLayoutManager(new LinearLayoutManager(LandHoldingActivity.this));
                        rvLandHoldning.setAdapter(landHoldingAdapter);
                        alertDialog.dismiss();

                        landHoldingAdapter.onViewLandClick(new ClickListener() {
                            @Override
                            public void onItemClick(int position) {

                            }

                            @Override
                            public void onViewLandClick(int position) {
                                Intent viewLandIntent=new Intent(context, ViewLandActivity.class);
                                viewLandIntent.putExtra("land_id", landHoldingPojoArrayList.get(position).getLand_id());
                                viewLandIntent.putExtra("land_area", landHoldingPojoArrayList.get(position).getArea());
                                viewLandIntent.putExtra("farmer_id", landHoldingPojoArrayList.get(position).getFarmer_id());
                                viewLandIntent.putExtra("farmer_name", sqliteHelper.getNameById("farmer_registration", "farmer_name", "id", Integer.parseInt(landHoldingPojoArrayList.get(position).getFarmer_id())));
                                viewLandIntent.putExtra("total_plant", landHoldingPojoArrayList.get(position).getTotal_plant());
                                viewLandIntent.putExtra("unit", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getLand_unit())));
                                viewLandIntent.putExtra("land_image", landHoldingPojoArrayList.get(position).getImage());
                                viewLandIntent.putExtra("latitude", landHoldingPojoArrayList.get(position).getLatitude());
                                viewLandIntent.putExtra("longitude", landHoldingPojoArrayList.get(position).getLongitude());
                                viewLandIntent.putExtra("p", landHoldingPojoArrayList.get(position).getP());
                                viewLandIntent.putExtra("s", landHoldingPojoArrayList.get(position).getS());
                                viewLandIntent.putExtra("mg", landHoldingPojoArrayList.get(position).getMg());
                                viewLandIntent.putExtra("k", landHoldingPojoArrayList.get(position).getK());
                                viewLandIntent.putExtra("n", landHoldingPojoArrayList.get(position).getN());
                                viewLandIntent.putExtra("ca", landHoldingPojoArrayList.get(position).getCa());
                                viewLandIntent.putExtra("bulk_density", landHoldingPojoArrayList.get(position).getBulk_density());
                                viewLandIntent.putExtra("filtration_rate", landHoldingPojoArrayList.get(position).getFiltration_rate());
                                viewLandIntent.putExtra("soil_texture", landHoldingPojoArrayList.get(position).getSoil_texture());
                                viewLandIntent.putExtra("cation_exchange_capacity", landHoldingPojoArrayList.get(position).getCation_exchange_capacity());
                                viewLandIntent.putExtra("ph", landHoldingPojoArrayList.get(position).getPh());
                                viewLandIntent.putExtra("ec", landHoldingPojoArrayList.get(position).getEc());
                                viewLandIntent.putExtra("land_name", landHoldingPojoArrayList.get(position).getLand_name());
                                viewLandIntent.putExtra("soil_type_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_type_id())));
                                viewLandIntent.putExtra("soil_color_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_color_id())));
                                //             viewLandIntent.putExtra("soil_characteristics_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_characteristics_id())));
                                //              viewLandIntent.putExtra("soil_chemical_composition_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_chemical_composition_id())));
                                viewLandIntent.putExtra("land_id_id", landHoldingPojoArrayList.get(position).getId());
                                viewLandIntent.putExtra("unit_id", landHoldingPojoArrayList.get(position).getLand_unit());
                                viewLandIntent.putExtra("land_image", landHoldingPojoArrayList.get(position).getImage());

                                startActivity(viewLandIntent);
                            }

                            @Override
                            public void onEditLandClick(int position) {
                                Intent addLandIntent=new Intent(context, AddLandActivity.class);
                                addLandIntent.putExtra("screen_type", "edit_land");
                                addLandIntent.putExtra("land_area", landHoldingPojoArrayList.get(position).getArea());
                                addLandIntent.putExtra("farmer_id", landHoldingPojoArrayList.get(position).getFarmer_id());
                                addLandIntent.putExtra("farmer_name", sqliteHelper.getNameById("farmer_registration", "farmer_name", "id", Integer.parseInt(landHoldingPojoArrayList.get(position).getFarmer_id())));
                                addLandIntent.putExtra("unit", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getLand_unit())));
                                addLandIntent.putExtra("unit_id", landHoldingPojoArrayList.get(position).getLand_unit());
                                addLandIntent.putExtra("P", landHoldingPojoArrayList.get(position).getP());
                                addLandIntent.putExtra("s", landHoldingPojoArrayList.get(position).getS());
                                addLandIntent.putExtra("mg", landHoldingPojoArrayList.get(position).getMg());
                                addLandIntent.putExtra("k", landHoldingPojoArrayList.get(position).getK());
                                addLandIntent.putExtra("n", landHoldingPojoArrayList.get(position).getN());
                                addLandIntent.putExtra("ca", landHoldingPojoArrayList.get(position).getCa());
                                addLandIntent.putExtra("land_image", landHoldingPojoArrayList.get(position).getImage());
                                addLandIntent.putExtra("land_id", landHoldingPojoArrayList.get(position).getLand_id());
                                addLandIntent.putExtra("soil_type_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_type_id())));
                                addLandIntent.putExtra("soil_color_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_color_id())));
                                //            addLandIntent.putExtra("soil_characteristics_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_characteristics_id())));
                                //            addLandIntent.putExtra("soil_chemical_composition_name", sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(landHoldingPojoArrayList.get(position).getSoil_chemical_composition_id())));
                                startActivity(addLandIntent);
                            }

                            @Override
                            public void onCheckBoxClick(int position) {

                            }


                        });

                    }
                });

                alertDialog.show();
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
    public void onBackPressed() {
        if (screen_type.equals("farmer_details")){
            Intent intentLandHoldingActivity = new Intent(LandHoldingActivity.this, FarmerDeatilActivity.class);
            String user_id=sqliteHelper.getUserID(Integer.parseInt(selected_farmer));
            intentLandHoldingActivity.putExtra("user_id",user_id);
            startActivity(intentLandHoldingActivity);
            finish();
        } else  {
            Intent intentLandHoldingActivity = new Intent(LandHoldingActivity.this, HomeAcivity.class);
            startActivity(intentLandHoldingActivity);
            finish();
        }
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
            if (screen_type.equals("farmer_details")){
                Intent intentLandHoldingActivity = new Intent(LandHoldingActivity.this, FarmerDeatilActivity.class);
                String user_id=sqliteHelper.getUserID(Integer.parseInt(selected_farmer));
                intentLandHoldingActivity.putExtra("user_id",user_id);
                startActivity(intentLandHoldingActivity);
                finish();
            } else  {
                Intent intentLandHoldingActivity = new Intent(LandHoldingActivity.this, HomeAcivity.class);
                startActivity(intentLandHoldingActivity);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
