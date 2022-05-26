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

    Context context = this;
   // private Object user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_recycle);
        getSupportActionBar().setTitle("Farmer List");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab1=findViewById(R.id.fab1);

        sqliteHelper = new SqliteHelper(this);
        recyclerView = findViewById(R.id.recycle2);


        arrayList = sqliteHelper.PS_getRegistrationData1();
        RegisterAdapter registerAdapter = new RegisterAdapter(context, arrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(registerAdapter);



        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1=new Intent(FarmerRecycle.this,FarmerRegistrationForm.class);
                startActivity(intent1);
            }
        });

    }
}