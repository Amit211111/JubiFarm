    package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sanket.jubifarm.R;

    public class ParyavaranSakhiHome extends AppCompatActivity {
    TextView farmer,tv_neem_plantation,txt_LandHolding,txt_Monitoring,txt_Syncronize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paryavaran_sakhi_home);
        getSupportActionBar().setTitle("Home");

        farmer =findViewById(R.id.farmer);
        tv_neem_plantation = findViewById(R.id.tv_neem_plantation);
        txt_LandHolding = findViewById(R.id.txt_LandHolding);
        txt_Monitoring=findViewById(R.id.txt_Monitoring);
        txt_Syncronize=findViewById(R.id.txt_Syncronize);
        tv_neem_plantation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParyavaranSakhiHome.this,NeemPlantation.class);
                startActivity(intent);
            }
        });

        txt_LandHolding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParyavaranSakhiHome.this,PS_LandHolding.class);
                startActivity(intent);
            }
        });

        farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(ParyavaranSakhiHome.this,FarmerRecycle.class);
                startActivity(intent);
            }
        });
        tv_neem_plantation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(ParyavaranSakhiHome.this,PS_NeemPlantationList.class);
                startActivity(intent1);
            }
        });
        txt_LandHolding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(ParyavaranSakhiHome.this,PS_LandHoldingList.class);
                startActivity(intent1);
            }
        });
        txt_Monitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(ParyavaranSakhiHome.this,PS_Neem_LandHolding_List.class);
                startActivity(intent2);
            }
        });
        txt_Syncronize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(ParyavaranSakhiHome.this,PS_Synchronize.class);
                startActivity(intent1);
            }
        });
    }
}