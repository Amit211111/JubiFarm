package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sanket.jubifarm.Activity.FarmerRegistration;
import com.sanket.jubifarm.Activity.HomeAcivity;
import com.sanket.jubifarm.Activity.RegistrationListActivity;
import com.sanket.jubifarm.Adapter.RegistrationListAdapter;
import com.sanket.jubifarm.Livelihood.Adapter.Adapter_PS_LandHolding;
import com.sanket.jubifarm.Livelihood.Adapter.RegisterAdapter;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Livelihood.Model.ParyavaranSakhiRegistrationPojo;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class FarmerRecycle extends AppCompatActivity {
    ImageButton fab1;

    RecyclerView recyclerView;
    ArrayList<ParyavaranSakhiRegistrationPojo> arrayList = new ArrayList<>();
    SqliteHelper sqliteHelper;
    EditText etSearchBar;
    String village = "";
    SharedPrefHelper sharedPrefHelper;
    ArrayList<String> villageArrayList  = new ArrayList<>();
    android.app.Dialog change_language_alert;
    HashMap<String, Integer> villageNameHM;
    Context context = this;
    // private Object user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_recycle);
        getSupportActionBar().setTitle("Farmer List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setTitle(Html.fromHtml("<font color=\"#000000\">" +getString(R.string.FARMER_LIST)+ "</font>"));

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab1 = findViewById(R.id.fab1);

        sqliteHelper = new SqliteHelper(this);
        recyclerView = findViewById(R.id.recycle2);
        etSearchBar = findViewById(R.id.etSearchBar);
        sharedPrefHelper = new SharedPrefHelper(this);


        etSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String search = etSearchBar.getText().toString();
                arrayList = sqliteHelper.getPSFarmerList(search);
                RegisterAdapter registerAdapter = new RegisterAdapter(FarmerRecycle.this, arrayList);
                int counter = arrayList.size();
                //  FarmerCount.setText("Farmer 0"+counter);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(FarmerRecycle.this));
                recyclerView.setAdapter(registerAdapter);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        arrayList = sqliteHelper.PS_getRegistrationData1();
        RegisterAdapter registerAdapter = new RegisterAdapter(context, arrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(registerAdapter);


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(FarmerRecycle.this, FarmerRegistrationForm.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        menu.findItem(R.id.home_filter).setVisible(true);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home_menu) {

            Intent intent = new Intent(this, ParyavaranSakhiHome.class);
            this.startActivity(intent);
            return true;
        }


        if (id == R.id.home_filter) {
            change_language_alert = new android.app.Dialog(FarmerRecycle.this);
            change_language_alert.setContentView(R.layout.custom_village_filter);
            change_language_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = change_language_alert.getWindow().getAttributes();
            params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
            change_language_alert.getWindow().setLayout(700, 700); //Controlling width and height.

            Spinner btn_filter_farmerfiltert = (Spinner) change_language_alert.findViewById(R.id.btn_filter_farmer_filtert);
            TextView tv_search = (TextView) change_language_alert.findViewById(R.id.tv_search);
            villageArrayList.clear();
            String language = sharedPrefHelper.getString("languageID", "");
            if (language.equalsIgnoreCase("hin")) {
                villageNameHM = sqliteHelper.getAllVillageforfilter(2);
            } else {
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
                    if (!btn_filter_farmerfiltert.getSelectedItem().toString().equals(R.string.select_village)) {
                        village = String.valueOf(villageNameHM.get(btn_filter_farmerfiltert.getSelectedItem().toString().trim()));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            tv_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    arrayList = sqliteHelper.getPSFarmerListforfilter(village);
                    RegisterAdapter registrationListAdapter = new RegisterAdapter(FarmerRecycle.this, arrayList);
                    int counter = arrayList.size();
                    //  FarmerCount.setText("Farmer 0"+counter);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(FarmerRecycle.this));
                    recyclerView.setAdapter(registrationListAdapter);
                     change_language_alert.dismiss();

                }
            });
            change_language_alert.show();
        }
        return super.onOptionsItemSelected(item);
    }



}

//        if (item.getItemId() == android.R.id.home) {
//        Intent intent=new Intent(RegistrationListActivity.this,HomeAcivity.class);
//        startActivity(intent);
//        finish();
//   }
//        return super.onOptionsItemSelected(item);
//









