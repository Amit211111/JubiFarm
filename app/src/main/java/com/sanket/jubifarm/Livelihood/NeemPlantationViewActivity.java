package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class NeemPlantationViewActivity extends AppCompatActivity {

    TextView Neem_Id, Land_id, et_plant_date, GeoCoodinate;
    SqliteHelper sqliteHelper;
    ArrayList<PSNeemPlantationPojo> psNeemPlantationPojos;
    PSNeemPlantationPojo ps_neem_plantation;

    String id = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neem_plantation_view);

        Neem_Id = findViewById(R.id.Neem_Id);
        Land_id = findViewById(R.id.Land_id);
        et_plant_date = findViewById(R.id.et_plant_date);
        GeoCoodinate = findViewById(R.id.GeoCoodinate);
        sqliteHelper = new SqliteHelper(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            id = bundle.getString("id", "0");
        }


        ps_neem_plantation = new PSNeemPlantationPojo();
        psNeemPlantationPojos = sqliteHelper.getViewNeemPlantation(id);
        Land_id.setText(psNeemPlantationPojos.get(0).getLand_id());
        Neem_Id.setText(psNeemPlantationPojos.get(0).getLocal_id());
        et_plant_date.setText(psNeemPlantationPojos.get(0).getPlantation_Date());

    }
}