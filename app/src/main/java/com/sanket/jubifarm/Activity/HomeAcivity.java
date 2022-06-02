package com.sanket.jubifarm.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;
import com.sanket.jubifarm.Drawer.AppDrawer;
import com.sanket.jubifarm.Livelihood.MainMenu;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.Locale;

public class HomeAcivity extends AppCompatActivity {
    LinearLayout ll_crop_farmeReg,ll_LnadHolding,ll_crop_planning,
            ll_crop_inputOrd,ll_crop_sale,ll_Helpline,ll_knowledge,
            ll_crop_monitoring,ll_Sync,ll_select_farmer;
    private LinearLayout ll_row1,ll_row2,ll_row3,ll_row4;
    private TextView tv_farmer_reg,tv_farmer,tv_change,tv_remove;
    SharedPrefHelper sharedPrefHelper;
    String selected_farmer;
    SqliteHelper sqliteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_acivity);

        sharedPrefHelper = new SharedPrefHelper(this);
        sqliteHelper=new SqliteHelper(this);
        String role_id = sharedPrefHelper.getString("role_id","");
         selected_farmer = sharedPrefHelper.getString("selected_farmer","");

        ll_crop_farmeReg = findViewById(R.id.ll_crop_farmeReg);
        tv_farmer_reg = findViewById(R.id.tv_farmer_reg);
        tv_farmer = findViewById(R.id.tv_farmer);
        tv_change = findViewById(R.id.tv_change);
        tv_remove = findViewById(R.id.tv_remove);

        ll_crop_planning = findViewById(R.id.ll_crop_planning);
        ll_select_farmer = findViewById(R.id.ll_select_farmer);
        ll_crop_monitoring = findViewById(R.id.ll_crop_monitoring);
        ll_crop_inputOrd = findViewById(R.id.ll_crop_inputOrd);
        ll_crop_sale = findViewById(R.id.ll_crop_sale);
        ll_Helpline = findViewById(R.id.ll_Helpline);
        ll_knowledge = findViewById(R.id.ll_knowledge);
        ll_Sync = findViewById(R.id.ll_Sync);
        ll_LnadHolding = findViewById(R.id.ll_LnadHolding);
        inializeDS();


        ll_row1 = findViewById(R.id.ll_row1);
        ll_row2 = findViewById(R.id.ll_row2);
        ll_row3 = findViewById(R.id.ll_row3);
        ll_row4 = findViewById(R.id.ll_row4);

        if (role_id.equals("5")){
            tv_farmer_reg.setText(getResources().getString(R.string.Farmer_detals));
        }else if (role_id.equals("4")){
            tv_farmer_reg.setText(getResources().getString(R.string.farmer_reg));
        }else if (role_id.equals("3")){
            ll_row1.setVisibility(View.GONE);
            ll_row3.setVisibility(View.GONE);
            ll_row4.setVisibility(View.GONE);
            ll_Helpline.setVisibility(View.GONE);
            ll_row2.setGravity(Gravity.CENTER);
        }else {
        }


        if (!selected_farmer.equals("")){
            ll_select_farmer.setVisibility(View.VISIBLE);
            tv_farmer.setText(sqliteHelper.getFarmerName(selected_farmer));

        }else {
            ll_select_farmer.setVisibility(View.GONE);

        }
        hideUnhideFields();
        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeAcivity.this,RegistrationListActivity.class);
                startActivity(intent);
            }
        });
        tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefHelper.setString("selected_farmer","");
                selected_farmer="";
                ll_select_farmer.setVisibility(View.GONE);

            }
        });
        ll_crop_farmeReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (role_id.equals("5")){
                    Intent Regintent = new Intent(HomeAcivity.this, FarmerDeatilActivity.class);
                    Regintent.putExtra("user_id",sharedPrefHelper.getString("user_id",""));
                    startActivity(Regintent);
                }else if (!selected_farmer.equals("")){
                    Intent Regintent = new Intent(HomeAcivity.this, FarmerDeatilActivity.class);
                    String user_id=sqliteHelper.getUserID(Integer.parseInt(selected_farmer));
                    Regintent.putExtra("user_id",user_id);
                    startActivity(Regintent);
                }else {
                    Intent Regintent = new Intent(HomeAcivity.this, RegistrationListActivity.class);
                    startActivity(Regintent);
                    finish();
                }
            }
        });
        ll_crop_planning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regintent = new Intent(HomeAcivity.this, CropPlanning.class);
                //regintent.putExtra("","");
                startActivity(regintent);
            }
        });
        ll_crop_inputOrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Regintent = new Intent(HomeAcivity.this, InputOrderingHome.class);
                startActivity(Regintent);
            }
        });
        ll_crop_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Regintent = new Intent(HomeAcivity.this, CropSaleDetails.class);
                startActivity(Regintent);
            }
        });
        ll_Helpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPrefHelper.getString("user_type","").equals("krishi_mitra")){
                    Intent Regintent = new Intent(HomeAcivity.this, HelplineMenu.class);
                    startActivity(Regintent);
                }else {
                    Intent Regintent = new Intent(HomeAcivity.this, HelpLineActivity.class);
                    startActivity(Regintent);
                    finish();
                }

            }
        });

        ll_knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Regintent = new Intent(HomeAcivity.this, KnowledgeActivity.class);
                startActivity(Regintent);
            }
        });

        ll_crop_monitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regintent = new Intent(HomeAcivity.this, LandHoldingActivity.class);
                regintent.putExtra("fromMonitoring","1");
                startActivity(regintent);
            }
        });
        ll_Sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Regintent = new Intent(HomeAcivity.this, SyncDataActivity.class);
                startActivity(Regintent);
            }
        });

        ll_LnadHolding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (role_id.equals("5")){
                    Intent intentLandHolding = new Intent(HomeAcivity.this, LandHoldingActivity.class);
                    startActivity(intentLandHolding);
                }else{
                    Intent Regintent = new Intent(HomeAcivity.this, LandHoldingActivity.class);
                    startActivity(Regintent);
                }
            }
        });
    }



    private void inializeDS() {
        ll_crop_farmeReg = findViewById(R.id.ll_crop_farmeReg);
        tv_farmer_reg = findViewById(R.id.tv_farmer_reg);
        ll_crop_planning = findViewById(R.id.ll_crop_planning);
        ll_crop_monitoring = findViewById(R.id.ll_crop_monitoring);
        ll_crop_inputOrd = findViewById(R.id.ll_crop_inputOrd);
        ll_crop_sale = findViewById(R.id.ll_crop_sale);
        ll_Helpline = findViewById(R.id.ll_Helpline);
        ll_knowledge = findViewById(R.id.ll_knowledge);
        ll_Sync = findViewById(R.id.ll_Sync);
        ll_LnadHolding = findViewById(R.id.ll_LnadHolding);
    }

    private void hideUnhideFields() {
        if (sharedPrefHelper.getString("user_type", "").equals("Farmer")) {
            ll_row4.setVisibility(View.GONE);
        }
    }

    //on back press exit alert dialog
    @Override
    public void onBackPressed() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
//        builder.setIcon(R.drawable.dialog_alert);
//        builder.setTitle("Alert!");
//        builder.setMessage(R.string.are_you_sure_to_want_to_exit_application);
//        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
//                finishAffinity();
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(intent);
//
//            }
//        });
//        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //if user select "No", just cancel this dialog and continue with app
//                dialog.cancel();
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
    }


}