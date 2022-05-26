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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sanket.jubifarm.Adapter.CropSaleAdapter;
import com.sanket.jubifarm.Adapter.CropSaleDetailsAdapter;
import com.sanket.jubifarm.Adapter.LandHoldingAdapter;
import com.sanket.jubifarm.Adapter.QueryListAdapter;
import com.sanket.jubifarm.Modal.HelplinePojo;
import com.sanket.jubifarm.Modal.SaleDetailsPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.interfaces.ClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HelplineMenu extends AppCompatActivity {

    TextView tv_answered,tv_pending;
    private ArrayList<HelplinePojo> helplinePojoArrayList=new ArrayList();
    private SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    private  RecyclerView rv_Query;
    private ImageView cropplaning_filter;
    QueryListAdapter queryListAdapter;
    private ArrayList<HelplinePojo> helplinePojo;
    ImageButton Button;
    TextView ChildCoun,FarmerCount;
    private ArrayList<String> farmarArrayList = new ArrayList<>();
    private HashMap<String, Integer> farmarNameHM;
    private String farmer_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hepline_menu);
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        rv_Query = findViewById(R.id.rv_Query);
        ChildCoun = findViewById(R.id.ChildCoun);
        FarmerCount = findViewById(R.id.FarmerCount);
        tv_answered = findViewById(R.id.tv_answered);
        Button = findViewById(R.id.fab);
        tv_pending = findViewById(R.id.tv_pending);
        cropplaning_filter = findViewById(R.id.cropplaning_filter);
        farmarNameHM=new HashMap<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.helplineMenu) + "</font>"));

        // Answered
        helplinePojoArrayList = sqliteHelper.getQueryListData(sharedPrefHelper.getString("selected_farmer", ""), "1");
        queryListAdapter = new QueryListAdapter(this, helplinePojoArrayList);
        ChildCoun.setText( getString(R.string.Total_Query) + ": " + helplinePojoArrayList.size());
        rv_Query.setLayoutManager(new LinearLayoutManager(this));
        rv_Query.setAdapter(queryListAdapter);

        tv_answered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Answered
                tv_answered.setBackgroundColor(getResources().getColor(R.color.colorselected));
                tv_pending.setBackgroundColor(getResources().getColor(R.color.colornotselected));
                helplinePojoArrayList = sqliteHelper.getQueryListData(sharedPrefHelper.getString("selected_farmer", ""),"1");
                ChildCoun.setText(getString(R.string.Total_Query) + ": " +helplinePojoArrayList.size());
                queryListAdapter.updateData(helplinePojoArrayList);
            }
        });

        cropplaning_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(HelplineMenu.this);
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
                ArrayAdapter arrayAdapterFarmer = new ArrayAdapter(HelplineMenu.this, android.R.layout.simple_dropdown_item_1line, farmarArrayList);
                btn_filter_farmerfiltert.setAdapter(arrayAdapterFarmer);

                farmer_name="";
                btn_filter_farmerfiltert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!btn_filter_farmerfiltert.getSelectedItem().toString().equals(getString(R.string.select_farmer))){
                            farmer_name = String.valueOf(farmarNameHM.get(btn_filter_farmerfiltert.getSelectedItem().toString().trim()));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                tv_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        helplinePojoArrayList= sqliteHelper.getQueryListData(farmer_name,"");
                        queryListAdapter  = new QueryListAdapter(HelplineMenu.this, helplinePojoArrayList);
                        int counter = helplinePojoArrayList.size();
                        FarmerCount.setText(" Query  "+counter);
                        rv_Query.setHasFixedSize(true);
                        rv_Query.setLayoutManager(new LinearLayoutManager(HelplineMenu.this));
                        rv_Query.setAdapter(queryListAdapter);

                    }
                });
                alertDialog.show();
            }
        });

        tv_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Not Answered
                tv_answered.setBackgroundColor(getResources().getColor(R.color.colornotselected));
                tv_pending.setBackgroundColor(getResources().getColor(R.color.colorselected));
                helplinePojoArrayList = sqliteHelper.getQueryListData(sharedPrefHelper.getString("selected_farmer", ""),"0");
                ChildCoun.setText(getString(R.string.Total_Query) + ": " +helplinePojoArrayList.size());
                queryListAdapter.updateData(helplinePojoArrayList);
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
