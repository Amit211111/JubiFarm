package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanket.jubifarm.Adapter.CropSaleAdapter;
import com.sanket.jubifarm.Modal.ProductionDetailsPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class CropDetails extends AppCompatActivity {
    RecyclerView rv_croppsale;
    ImageView search_farmer;
    CropSaleAdapter cropSaleAdapter;
    private ArrayList<ProductionDetailsPojo> productionDetailsPojoArrayList;
    TextView CropCount,tab_productionsale,tab_sale_details;
    ImageView  searchfilter;
    SqliteHelper sqliteHelper;
    ImageButton Button;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_sale);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + "CROP SALE" + "</font>"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rv_croppsale = (RecyclerView) findViewById(R.id.rv_croppsale);
        searchfilter = findViewById(R.id.searchfilter);
        tab_sale_details = (TextView) findViewById(R.id.tab_sale_details);
        sqliteHelper=new SqliteHelper(this);
        Button = findViewById(R.id.fab);
        CropCount = findViewById(R.id.CropCount);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Regintent = new Intent(CropDetails.this, CropDetalsInPut.class);
                startActivity(Regintent);

            }
        });
        searchfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CropDetails.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_filter, viewGroup, false);
                builder.setView(dialogView);
                MaterialSpinner checkInProviders = (MaterialSpinner) dialogView .findViewById(R.id.btn_filter_years);
                checkInProviders.setItems("--Select--","2017-2018", "2018-2019", "2019-2020");

                MaterialSpinner btn_filter_Crop = (MaterialSpinner) dialogView .findViewById(R.id.btn_filter_Crop);
                btn_filter_Crop.setItems("--Select--","Mango(M)", "Apple(A)", "Neem(N)");

                MaterialSpinner btn_Seassion = (MaterialSpinner) dialogView .findViewById(R.id.btn_Seassion);
                btn_Seassion.setItems("--Select--","Spring", "Summer", "autumn","Winter");
                AlertDialog alertDialog = builder.create();

                alertDialog.show();
            }
        });
        tab_sale_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Regintent = new Intent(CropDetails.this, CropSaleDetails.class);
                startActivity(Regintent);
                finish();
            }
        });

       // productionDetailsPojoArrayList= sqliteHelper.getCropProductionList();
        cropSaleAdapter = new CropSaleAdapter(this, productionDetailsPojoArrayList);
        int counter = productionDetailsPojoArrayList.size();
        CropCount.setText(" Crop :" + counter);
        rv_croppsale.setHasFixedSize(true);
        rv_croppsale.setLayoutManager(new LinearLayoutManager(this));
        rv_croppsale.setAdapter(cropSaleAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
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