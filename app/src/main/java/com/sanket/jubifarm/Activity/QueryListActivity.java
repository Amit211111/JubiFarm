package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanket.jubifarm.Adapter.LandHoldingAdapter;
import com.sanket.jubifarm.Adapter.QueryListAdapter;
import com.sanket.jubifarm.Modal.HelplinePojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.interfaces.ClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class QueryListActivity extends AppCompatActivity {
    RecyclerView rv_Query;
    DatePickerDialog datePickerDialog;
    EditText et_IOTodate, et_IOfromdate;
    ImageView cropplaning_filter;
    QueryListAdapter queryListAdapter;
    AdapterView adapterView;
    private ArrayList<HelplinePojo> helplinePojoArrayList;
    HelplinePojo helplinePojo;
    TextView FarmerCount;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    ImageButton Button;
    private ArrayList<String> farmarArrayList = new ArrayList<>();
    private HashMap<String, Integer> farmarNameHM;
    private String farmer_name="";
    private String fromAnswer="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.LIST_OF_QUERIES) + "</font>"));

        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper=new SharedPrefHelper(this);
        et_IOTodate = findViewById(R.id.et_IOTodate);
        et_IOfromdate = findViewById(R.id.et_IOfromdate);
        FarmerCount = findViewById(R.id.FarmerCount);
        Button = findViewById(R.id.fab);
        rv_Query = (RecyclerView) findViewById(R.id.rv_Query);
        cropplaning_filter = findViewById(R.id.cropplaning_filter);

        if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
            Button.setVisibility(View.GONE);
        } else {
            Button.setVisibility(View.VISIBLE);
        }

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            fromAnswer=bundle.getString("fromAnswer","");
        }

        cropplaning_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(QueryListActivity.this);
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
                ArrayAdapter arrayAdapterFarmer = new ArrayAdapter(QueryListActivity.this, android.R.layout.simple_dropdown_item_1line, farmarArrayList);
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
                        queryListAdapter  = new QueryListAdapter(QueryListActivity.this, helplinePojoArrayList);
                        int counter = helplinePojoArrayList.size();
                        FarmerCount.setText(" "+getString(R.string.Query) +": "+counter);
                        rv_Query.setHasFixedSize(true);
                        rv_Query.setLayoutManager(new LinearLayoutManager(QueryListActivity.this));
                        rv_Query.setAdapter(queryListAdapter);
                    }
                });
                alertDialog.show();
            }
        });

//        et_IOfromdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                int mYear = c.get(Calendar.YEAR); // current year
//                int mMonth = c.get(Calendar.MONTH); // current month
//                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
//                datePickerDialog = new DatePickerDialog(QueryListActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                et_IOfromdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                            }
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.show();
//            }
//        });

//        et_IOTodate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                int mYear = c.get(Calendar.YEAR); // current year
//                int mMonth = c.get(Calendar.MONTH); // current month
//                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
//                datePickerDialog = new DatePickerDialog(QueryListActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                et_IOTodate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                            }
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.show();
//            }
//        });



        if (fromAnswer.equals("1")){
            helplinePojoArrayList.clear();
            helplinePojoArrayList = sqliteHelper.getQueryListData(sharedPrefHelper.getString("selected_farmer", ""),fromAnswer);
            queryListAdapter = new QueryListAdapter(this, helplinePojoArrayList);
            int counter = helplinePojoArrayList.size();
            FarmerCount.setText(" "+getString(R.string.Query) +": "+ counter);
            rv_Query.setHasFixedSize(true);
            rv_Query.setLayoutManager(new LinearLayoutManager(this));
            rv_Query.setAdapter(queryListAdapter);

        }else if (fromAnswer.equals("0")){
            helplinePojoArrayList.clear();
            helplinePojoArrayList = sqliteHelper.getQueryListData(sharedPrefHelper.getString("selected_farmer", ""),fromAnswer);
            queryListAdapter = new QueryListAdapter(this, helplinePojoArrayList);
            int counter = helplinePojoArrayList.size();
            FarmerCount.setText(" "+getString(R.string.Query) +": "+ counter);
            rv_Query.setHasFixedSize(true);
            rv_Query.setLayoutManager(new LinearLayoutManager(this));
            rv_Query.setAdapter(queryListAdapter);

        }else {
            helplinePojoArrayList = sqliteHelper.getQueryListData(sharedPrefHelper.getString("selected_farmer", ""),"");
            queryListAdapter = new QueryListAdapter(this, helplinePojoArrayList);
            int counter = helplinePojoArrayList.size();
            FarmerCount.setText(" "+getString(R.string.Query) +": "+ counter);
            rv_Query.setHasFixedSize(true);
            rv_Query.setLayoutManager(new LinearLayoutManager(this));
            rv_Query.setAdapter(queryListAdapter);
        }

        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Regintent = new Intent(QueryListActivity.this, AddQueryActivity.class);
                startActivity(Regintent);
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

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
//            Intent intent = new Intent(this, HelplineMenu.class);
//            this.startActivity(intent);
//            finish();
//            return true;
//        }
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(QueryListActivity.this, HelpLineActivity.class);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(QueryListActivity.this, HelpLineActivity.class);
        startActivity(intent);
        finish();
    }
}