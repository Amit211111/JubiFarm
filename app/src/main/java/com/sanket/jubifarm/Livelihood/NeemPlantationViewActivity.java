package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class NeemPlantationViewActivity extends AppCompatActivity {

    TextView Neem_Id, Land_id, et_plant_date, GeoCoodinate;
    SqliteHelper sqliteHelper;
    ArrayList<PSNeemPlantationPojo> psNeemPlantationPojos;
    PSNeemPlantationPojo ps_neem_plantation;
    SharedPrefHelper sharedPrefHelper;
    String id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neem_plantation_view);

        Neem_Id = findViewById(R.id.Neem_Id);
        Land_id = findViewById(R.id.Land_id);
        et_plant_date = findViewById(R.id.et_plant_date);
        GeoCoodinate = findViewById(R.id.GeoCoodinate);
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);

        GeoCoodinate.setText(sharedPrefHelper.getString("LAT","")+", "+sharedPrefHelper.getString("LONG",""));

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            id = bundle.getString("id", "");
        }


        ps_neem_plantation = new PSNeemPlantationPojo();

        ps_neem_plantation = sqliteHelper.getViewNeemPlantation(id);
        Land_id.setText(ps_neem_plantation.getLand_id());
        Neem_Id.setText(ps_neem_plantation.getNeem_id());
        et_plant_date.setText(ps_neem_plantation.getPlantation_Date());

    }
}