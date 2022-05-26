package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sanket.jubifarm.Adapter.RegistrationListAdapter;
import com.sanket.jubifarm.Adapter.SoilInfoAdapter;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.Modal.SoilPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class SoilInFoListActivity extends AppCompatActivity {
    RecyclerView rv_SolilInfo;
    SoilInfoAdapter soilInfoAdapter;
    private ArrayList<SoilPojo> soilPojoArrayList;
    SqliteHelper sqliteHelper;
    ImageButton Button;
    Context context=this;
    String land_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_in_fo);
        sqliteHelper =new SqliteHelper(this);
        rv_SolilInfo=findViewById(R.id.rv_SolilInfo);
        Button = findViewById(R.id.fab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" +getString(R.string.update_soil_list)+ "</font>"));

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            land_id = bundle.getString("land_id","");
        }
       soilPojoArrayList= sqliteHelper.getDateSoil(land_id);
        soilInfoAdapter  = new SoilInfoAdapter(this, soilPojoArrayList);
        rv_SolilInfo.setHasFixedSize(true);
        rv_SolilInfo.setLayoutManager(new LinearLayoutManager(this));
        rv_SolilInfo.setAdapter(soilInfoAdapter);

        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SoilInFoListActivity.this, UpdatedSoilInFo.class);
                intent.putExtra("land_id",land_id);
                startActivity(intent);
                finish();
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