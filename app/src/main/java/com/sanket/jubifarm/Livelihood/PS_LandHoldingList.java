package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

import com.sanket.jubifarm.Activity.LandHoldingActivity;
import com.sanket.jubifarm.Adapter.LandHoldingAdapter;
import com.sanket.jubifarm.Livelihood.Adapter.Adapter_PS_LandHolding;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Modal.LandHoldingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class PS_LandHoldingList extends AppCompatActivity {

    ImageButton fab;
    RecyclerView recyclerView;
    private ImageView Landholding_filter;
    private LandHoldingAdapter landHoldingAdapter;
    private ArrayList<String> farmarArrayList = new ArrayList<>();
    private HashMap<String, Integer> farmarNameHM;
    private String farmer_id = "";
    private TextView landCount;
    ArrayList<PSLandHoldingPojo> arrayList = new ArrayList<>();
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    String screenType = "";
    String screen_type = "";

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps_land_holding_list);
        getSupportActionBar().setTitle("Land Holding List");
        fab = findViewById(R.id.fab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        farmarNameHM = new HashMap<>();
        Landholding_filter = findViewById(R.id.Landholding_filter);
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        recyclerView = findViewById(R.id.rvLandHoldning);
        landCount = findViewById(R.id.landCount);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            screenType = bundle.getString("land", "");
        }

        screen_type = sharedPrefHelper.getString("prayawran_screenType", "");

        if (screen_type.equals("land")) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }


        setLandList();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(PS_LandHoldingList.this, PS_LandHolding.class);
                startActivity(intent1);
            }
        });

        Landholding_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PS_LandHoldingList.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_farmer_filter, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                Spinner btn_filter_farmerfiltert = (Spinner) dialogView.findViewById(R.id.btn_filter_farmer_filtert);
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
                btn_filter_farmerfiltert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!btn_filter_farmerfiltert.getSelectedItem().toString().equals(getString(R.string.select_farmer))) {
                            farmer_id = String.valueOf(farmarNameHM.get(btn_filter_farmerfiltert.getSelectedItem().toString().trim()));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                alertDialog.show();
                tv_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        arrayList.clear();
                        arrayList = sqliteHelper.PSgetRegistrationData(farmer_id);
                        Adapter_PS_LandHolding adapter_ps_landHolding = new Adapter_PS_LandHolding(context, arrayList, screenType);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(PS_LandHoldingList.this));
                        recyclerView.setAdapter(adapter_ps_landHolding);
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
                Intent intent = new Intent(PS_LandHoldingList.this, ParyavaranSakhiHome.class);
                startActivity(intent);
                return true;
            case R.id.home_menu:
                Intent intent1 = new Intent(this, ParyavaranSakhiHome.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setLandList() {
        arrayList = sqliteHelper.PSgetRegistrationData("");
        Adapter_PS_LandHolding adapter_ps_landHolding = new Adapter_PS_LandHolding(context, arrayList, screenType);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter_ps_landHolding);
    }

    private void Filter_LandHolding() {
    }

    @Override
    public void onBackPressed() {

        Intent intentLandHoldingActivity = new Intent(context, ParyavaranSakhiHome.class);
        startActivity(intentLandHoldingActivity);

    }
}





