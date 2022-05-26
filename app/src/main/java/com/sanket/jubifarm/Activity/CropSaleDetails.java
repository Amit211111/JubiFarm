package com.sanket.jubifarm.Activity;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanket.jubifarm.Adapter.CropPlanningAdapter;
import com.sanket.jubifarm.Adapter.CropSaleAdapter;
import com.sanket.jubifarm.Adapter.CropSaleDetailsAdapter;
import com.sanket.jubifarm.Modal.ProductionDetailsPojo;
import com.sanket.jubifarm.Modal.SaleDetailsPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class CropSaleDetails extends AppCompatActivity {
    RecyclerView CropsealList;
    ImageView search_farmer;
    CropSaleDetailsAdapter cropSaleDetailsAdapter;
    CropSaleAdapter CropSaleAdapter;
    private ArrayList<SaleDetailsPojo> saleDetailsPojoArrayList;
    private ArrayList<ProductionDetailsPojo> productionDetailsPojos;
    TextView CropCount, tab_productionsale, tab_sale_detailspro;
    ImageView cropplaning_filter;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;

    ImageButton Button;
    private Context context=this;
    private ArrayList<String> farmarArrayList;
    private HashMap<String, Integer> farmarNameHM;
    private int farmerId=0;
    private ArrayList<String> yearArrayList;
    private HashMap<String, Integer> yearNameHM;
    private String yearId;
    private String selected_farmer="";
    ArrayList<String> seasonArryList;
    HashMap<String, Integer> seasonNameHm;
    private int season_id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_sale_details);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.crop_sale) + "</font>"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper=new SharedPrefHelper(this);
        seasonArryList=new ArrayList<>();
        seasonNameHm=new HashMap<>();
        farmarArrayList=new ArrayList<>();
        farmarNameHM=new HashMap<>();
        yearArrayList=new ArrayList<>();
        yearNameHM=new HashMap<>();

        tab_productionsale = findViewById(R.id.tab_productionsale);
        tab_sale_detailspro = findViewById(R.id.tab_sale_detailspro);
        CropCount = findViewById(R.id.CropCount);
        Button = findViewById(R.id.fab);
        sharedPrefHelper.setString("fromSalesDetials","1");
        selected_farmer=sharedPrefHelper.getString("selected_farmer","");

        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Regintent = new Intent(CropSaleDetails.this, SelectCropActivity.class);
                Regintent.putExtra("fromSalesDetials",sharedPrefHelper.getString("fromSalesDetials", ""));
                startActivity(Regintent);
                finish();
            }
        });


        tab_sale_detailspro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefHelper.setString("fromSalesDetials","2");
                if (sharedPrefHelper.getString("fromSalesDetials","").equals("2")){
                    tab_productionsale.setBackgroundColor(getResources().getColor(R.color.colornotselected));
                    tab_sale_detailspro.setBackgroundColor(getResources().getColor(R.color.colorselected));

                }

               /*saleDetailsPojoArrayList = sqliteHelper.getCropList();
                cropSaleDetailsAdapter = new CropSaleDetailsAdapter(CropSaleDetails.this, saleDetailsPojoArrayList);
                int counter = saleDetailsPojoArrayList.size();
                CropCount.setText("Crop : " + counter);
                CropsealList.setHasFixedSize(true);
                CropsealList.setLayoutManager(new LinearLayoutManager(CropSaleDetails.this));
                CropsealList.setAdapter(cropSaleDetailsAdapter);*/
                saleDetailsPojoArrayList = sqliteHelper.getCropList(selected_farmer);
                cropSaleDetailsAdapter = new CropSaleDetailsAdapter(CropSaleDetails.this, saleDetailsPojoArrayList);
                int counter = saleDetailsPojoArrayList.size();
                CropCount.setText(" " +getString(R.string.Crop) + ": " + counter);
                CropsealList.setHasFixedSize(true);
                CropsealList.setLayoutManager(new LinearLayoutManager(CropSaleDetails.this));
                CropsealList.setAdapter(cropSaleDetailsAdapter);
            }
        });

        tab_productionsale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefHelper.setString("fromSalesDetials","1");
                if (sharedPrefHelper.getString("fromSalesDetials","").equals("1")){
                    tab_sale_detailspro.setBackgroundColor(getResources().getColor(R.color.colornotselected));
                    tab_productionsale.setBackgroundColor(getResources().getColor(R.color.colorselected));
                }

                productionDetailsPojos = sqliteHelper.getProductionList(selected_farmer);
                CropSaleAdapter = new CropSaleAdapter(CropSaleDetails.this, productionDetailsPojos);
                int counter = productionDetailsPojos.size();
                CropCount.setText(" " +getString(R.string.Crop) + ": "  + counter);
                CropsealList.setHasFixedSize(true);
                CropsealList.setLayoutManager(new LinearLayoutManager(CropSaleDetails.this));
                CropsealList.setAdapter(CropSaleAdapter);

                /*productionDetailsPojos = sqliteHelper.getProductionList();
                CropSaleAdapter = new CropSaleAdapter(CropSaleDetails.this, productionDetailsPojos);
                int counter = productionDetailsPojos.size();
                CropCount.setText("Crop : " + counter);
                CropsealList.setHasFixedSize(true);
                CropsealList.setLayoutManager(new LinearLayoutManager(CropSaleDetails.this));
                CropsealList.setAdapter(CropSaleAdapter);*/

            }
        });


        CropsealList = findViewById(R.id.CrsealList);
        cropplaning_filter = findViewById(R.id.cropplaning_filter);
        cropplaning_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CropSaleDetails.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_filter, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();

                //farmer spinner
                LinearLayout ll_farmer = dialogView.findViewById(R.id.ll_farmer);
                if (sharedPrefHelper.getString("user_type", "").equals("Farmer") || !sharedPrefHelper.getString("selected_farmer","").equals("")) {
                    ll_farmer.setVisibility(View.GONE);
                }
                Spinner spn_farmer = dialogView.findViewById(R.id.spn_farmer);
                farmarArrayList.clear();
                farmarNameHM = sqliteHelper.getFarmerspinner();
                for (int i = 0; i < farmarNameHM.size(); i++) {
                    farmarArrayList.add(farmarNameHM.keySet().toArray()[i].toString().trim());

                }
                farmarArrayList.add(0, getString(R.string.select_farmer));
                ArrayAdapter arrayAdapterFarmer = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, farmarArrayList);
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


                //years spinners
                Spinner btn_filter_years = (Spinner) dialogView.findViewById(R.id.btn_filter_years);
                String[] yearAL = {getString(R.string.select_year), "2020-21"};

                ArrayAdapter arrayAdapterYear = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, yearAL);
                btn_filter_years.setAdapter(arrayAdapterYear);

                yearId="";
                btn_filter_years.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!btn_filter_years.getSelectedItem().toString().equals(getString(R.string.select_year))) {
                            yearId = btn_filter_years.getSelectedItem().toString().trim();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                //crop spinners

                //season spinner
                Spinner btn_Seassion = (Spinner) dialogView.findViewById(R.id.btn_Seassion);
                seasonArryList.clear();
                SharedPrefHelper spf = new SharedPrefHelper(CropSaleDetails.this);
                String language = spf.getString("languageID","");
                if(language.equalsIgnoreCase("hin"))
                {
                    seasonNameHm = sqliteHelper.getMasterSpinnerId(5, 2);
                }else if(language.equalsIgnoreCase("kan"))
                {
                    seasonNameHm = sqliteHelper.getMasterSpinnerId(5, 3);
                }
                else
                {
                    seasonNameHm = sqliteHelper.getMasterSpinnerId(5, 1);
                }

                for (int i = 0; i < seasonNameHm.size(); i++) {
                    seasonArryList.add(seasonNameHm.keySet().toArray()[i].toString().trim());
                }
                seasonArryList.add(0,getString(R.string.select_season));
                ArrayAdapter arrayAdapterSeason = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, seasonArryList);
                btn_Seassion.setAdapter(arrayAdapterSeason);

                season_id=0;
                btn_Seassion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!btn_Seassion.getSelectedItem().toString().equals(getString(R.string.select_season))) {
                            season_id = seasonNameHm.get(btn_Seassion.getSelectedItem().toString().trim());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                TextView search_filter = (TextView) dialogView.findViewById(R.id.btn_search);
                search_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fromSalesDetials=  sharedPrefHelper.getString("fromSalesDetials","");

                        if (fromSalesDetials.equals("1")){
                            productionDetailsPojos.clear();
                            tab_productionsale.setBackgroundColor(getResources().getColor(R.color.colorselected));
                            tab_sale_detailspro.setBackgroundColor(getResources().getColor(R.color.colornotselected));
                            /*saleDetailsPojoArrayList = sqliteHelper.getCropListFilter(season_id, String.valueOf(yearId), farmerId);
                            cropSaleDetailsAdapter = new CropSaleDetailsAdapter(CropSaleDetails.this, saleDetailsPojoArrayList);
                            int counter = saleDetailsPojoArrayList.size();
                            CropCount.setText("Crop : " + counter);
                            CropsealList.setHasFixedSize(true);
                            CropsealList.setLayoutManager(new LinearLayoutManager(CropSaleDetails.this));
                            CropsealList.setAdapter(cropSaleDetailsAdapter);*/
                            productionDetailsPojos = sqliteHelper.getProductionListFilter(season_id, String.valueOf(yearId), farmerId);
                            CropSaleAdapter = new CropSaleAdapter(CropSaleDetails.this, productionDetailsPojos);
                            int counter = productionDetailsPojos.size();
                            CropCount.setText(" " +getString(R.string.Crop) + ": "  + counter);
                            CropsealList.setHasFixedSize(true);
                            CropsealList.setLayoutManager(new LinearLayoutManager(CropSaleDetails.this));
                            CropsealList.setAdapter(CropSaleAdapter);
                            alertDialog.dismiss();

                        }else {
                            saleDetailsPojoArrayList.clear();
                            tab_sale_detailspro.setBackgroundColor(getResources().getColor(R.color.colorselected));
                            tab_productionsale.setBackgroundColor(getResources().getColor(R.color.colornotselected));
                            /*productionDetailsPojos = sqliteHelper.getProductionListFilter(season_id, String.valueOf(yearId), farmerId);
                            CropSaleAdapter = new CropSaleAdapter(CropSaleDetails.this, productionDetailsPojos);
                            int counter = productionDetailsPojos.size();
                            CropCount.setText("Crop : " + counter);
                            CropsealList.setHasFixedSize(true);
                            CropsealList.setLayoutManager(new LinearLayoutManager(CropSaleDetails.this));
                            CropsealList.setAdapter(CropSaleAdapter);*/
                            saleDetailsPojoArrayList = sqliteHelper.getCropListFilter(season_id, String.valueOf(yearId), farmerId);
                            cropSaleDetailsAdapter = new CropSaleDetailsAdapter(CropSaleDetails.this, saleDetailsPojoArrayList);
                            int counter = saleDetailsPojoArrayList.size();
                            CropCount.setText(" " +getString(R.string.Crop) + ": "  + counter);

                            CropsealList.setHasFixedSize(true);
                            CropsealList.setLayoutManager(new LinearLayoutManager(CropSaleDetails.this));
                            CropsealList.setAdapter(cropSaleDetailsAdapter);
                            alertDialog.dismiss();
                        }
                     }
                });
                alertDialog.show();
            }
        });

        //set production details adapter
        productionDetailsPojos = sqliteHelper.getProductionList(selected_farmer);
        CropSaleAdapter = new CropSaleAdapter(CropSaleDetails.this, productionDetailsPojos);
        int counter = productionDetailsPojos.size();
        CropCount.setText(" " + getString(R.string.Crop) + ": " + counter);
        CropsealList.setHasFixedSize(true);
        CropsealList.setLayoutManager(new LinearLayoutManager(CropSaleDetails.this));
        CropsealList.setAdapter(CropSaleAdapter);
        /*saleDetailsPojoArrayList = sqliteHelper.getCropList();
        cropSaleDetailsAdapter = new CropSaleDetailsAdapter(this, saleDetailsPojoArrayList);
        int counter = saleDetailsPojoArrayList.size();
        CropCount.setText("Crop : " + counter);
        CropsealList.setHasFixedSize(true);
        CropsealList.setLayoutManager(new LinearLayoutManager(this));
        CropsealList.setAdapter(cropSaleDetailsAdapter);*/
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}