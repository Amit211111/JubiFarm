package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;

public class ParyavaranSakhiHome extends AppCompatActivity {
    TextView farmer,txt_LandHolding,txt_Monitoring,txt_Syncronize;
    LinearLayout ll_Helpline,ll_Sync,ll_neem_plantation,ll_neem_monitoring,ll_land_holding,ll_farmReg;
    SharedPrefHelper sharedPrefHelper;
    String screenType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paryavaran_sakhi_home);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        farmer =findViewById(R.id.farmer);
        sharedPrefHelper=new SharedPrefHelper(this);
        txt_LandHolding = findViewById(R.id.txt_LandHolding);
        txt_Monitoring=findViewById(R.id.txt_Monitoring);
        txt_Syncronize=findViewById(R.id.txt_Syncronize);
        ll_neem_plantation=findViewById(R.id.ll_neem_plantation);
        ll_neem_monitoring=findViewById(R.id.ll_neem_monitoring);
        ll_land_holding=findViewById(R.id.ll_land_holding);
        ll_Sync=findViewById(R.id.ll_Sync);
        ll_farmReg=findViewById(R.id.ll_farmReg);
        ll_Helpline=findViewById(R.id.ll_Helpline);

     /*   txt_LandHolding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ParyavaranSakhiHome.this,PS_LandHolding.class);
                startActivity(intent);
            }
        });*/

        ll_farmReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(ParyavaranSakhiHome.this,FarmerRecycle.class);
                startActivity(intent);
            }
        });
        ll_neem_plantation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefHelper.setString("plantation_screenType", "view");
                sharedPrefHelper.setString("prayawran_screenType", "land");
                sharedPrefHelper.setString("plantation_list", "plantation");
                Intent intent1=new Intent(ParyavaranSakhiHome.this,PS_NeemPlantationList.class);
                startActivity(intent1);
            }
        });
        ll_land_holding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(ParyavaranSakhiHome.this,PS_LandHoldingList.class);
                sharedPrefHelper.setString("prayawran_screenType", "land");
                startActivity(intent1);
            }
        });
        ll_neem_monitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(ParyavaranSakhiHome.this,PS_LandHoldingList.class);
                sharedPrefHelper.setString("prayawran_screenType", "monitoring");
                sharedPrefHelper.setString("plantation_list", "monitoring");
                startActivity(intent2);
            }
        });
        ll_Sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(ParyavaranSakhiHome.this,PS_Synchronize.class);
                startActivity(intent1);
            }
        });
        ll_Helpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParyavaranSakhiHome.this,PS_Helpline.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(ParyavaranSakhiHome.this, MainMenu.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}