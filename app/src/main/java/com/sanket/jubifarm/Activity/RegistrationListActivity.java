package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanket.jubifarm.Adapter.QueryListAdapter;
import com.sanket.jubifarm.Adapter.RegistrationListAdapter;
import com.sanket.jubifarm.Drawer.AppDrawer;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RegistrationListActivity extends AppCompatActivity {
    RecyclerView rv_farmer;
    ImageView search_farmer;
    RegistrationListAdapter registrationListAdapter;
    private ArrayList<FarmerRegistrationPojo> farmerRegistrationPojoArrayList;
    TextView FarmerCount;
    SqliteHelper sqliteHelper;
    ImageButton Button;
    EditText etSearchBar;
    android.app.Dialog change_language_alert;
    ArrayList<String> villageArrayList=new ArrayList<>();
    HashMap<String, Integer> villageNameHM;
    String village="";
    SharedPrefHelper sharedPrefHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.FARMER_LIST) + "</font>"));


        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper=new SharedPrefHelper(this);
        villageNameHM=new HashMap<>();
        Button = findViewById(R.id.fab);
        search_farmer= findViewById(R.id.search_farmer);
        etSearchBar= findViewById(R.id.etSearchBar);
        rv_farmer = (RecyclerView) findViewById(R.id.rv_farmer);
      //  FarmerCount = (TextView) findViewById(R.id.FarmerCount);

        etSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               String search = etSearchBar.getText().toString();
                farmerRegistrationPojoArrayList= sqliteHelper.getFarmerList(search);
                registrationListAdapter  = new RegistrationListAdapter(RegistrationListActivity.this, farmerRegistrationPojoArrayList);
                int counter = farmerRegistrationPojoArrayList.size();
                //  FarmerCount.setText("Farmer 0"+counter);
                rv_farmer.setHasFixedSize(true);
                rv_farmer.setLayoutManager(new LinearLayoutManager(RegistrationListActivity.this));
                rv_farmer.setAdapter(registrationListAdapter);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Regintent = new Intent(RegistrationListActivity.this, FarmerRegistration.class);
                Regintent.putExtra("fromLogin","2");
                startActivity(Regintent);
            }
        });


        farmerRegistrationPojoArrayList= sqliteHelper.getFarmerList("");
        registrationListAdapter  = new RegistrationListAdapter(this, farmerRegistrationPojoArrayList);
        int counter = farmerRegistrationPojoArrayList.size();
      //  FarmerCount.setText("Farmer 0"+counter);
        rv_farmer.setHasFixedSize(true);
        rv_farmer.setLayoutManager(new LinearLayoutManager(this));
        rv_farmer.setAdapter(registrationListAdapter);

       /* searchby_farmer.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(farmerRegistrationPojoArrayList.contains(query)){
                    registrationListAdapter.getFilter().filter(query);
                }else{
                    Toast.makeText(RegistrationListActivity.this, "No Match found", Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                registrationListAdapter.getFilter().filter(newText);
                return false;
            }
        });*/





    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        menu.findItem(R.id.home_filter).setVisible(true);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(RegistrationListActivity.this,HomeAcivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home_menu) {

            Intent intent = new Intent(this, HomeAcivity.class);
            this.startActivity(intent);
            return true;
        }

        if (id == R.id.home_filter) {
            change_language_alert = new android.app.Dialog(RegistrationListActivity.this);
            change_language_alert.setContentView(R.layout.custom_village_filter);
            change_language_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = change_language_alert.getWindow().getAttributes();
            params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
            change_language_alert.getWindow().setLayout(700, 700); //Controlling width and height.

            Spinner btn_filter_farmerfiltert = (Spinner) change_language_alert.findViewById(R.id.btn_filter_farmer_filtert);
            TextView tv_search = (TextView) change_language_alert.findViewById(R.id.tv_search);
            villageArrayList.clear();
            String language = sharedPrefHelper.getString("languageID","");
            if(language.equalsIgnoreCase("hin"))
            {
                villageNameHM = sqliteHelper.getAllVillageforfilter(2);
            }
            else
            {
                villageNameHM = sqliteHelper.getAllVillageforfilter(1);
            }
            for (int i = 0; i < villageNameHM.size(); i++) {
                villageArrayList.add(villageNameHM.keySet().toArray()[i].toString().trim());
            }

            Collections.sort(villageArrayList);
            villageArrayList.add(0, getString(R.string.select_village));
            final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, villageArrayList);
            Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            btn_filter_farmerfiltert.setAdapter(Adapter);
            btn_filter_farmerfiltert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!btn_filter_farmerfiltert.getSelectedItem().toString().equals(R.string.select_village)){
                        village= String.valueOf(villageNameHM.get(btn_filter_farmerfiltert.getSelectedItem().toString().trim()));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            tv_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    farmerRegistrationPojoArrayList= sqliteHelper.getFarmerListforfilter(village);
                    registrationListAdapter  = new RegistrationListAdapter(RegistrationListActivity.this, farmerRegistrationPojoArrayList);
                    int counter = farmerRegistrationPojoArrayList.size();
                    //  FarmerCount.setText("Farmer 0"+counter);
                    rv_farmer.setHasFixedSize(true);
                    rv_farmer.setLayoutManager(new LinearLayoutManager(RegistrationListActivity.this));
                    rv_farmer.setAdapter(registrationListAdapter);
                    change_language_alert.dismiss();

                }
            });
            change_language_alert.show();
        }

        if (item.getItemId() == android.R.id.home) {
            Intent intent=new Intent(RegistrationListActivity.this,HomeAcivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
