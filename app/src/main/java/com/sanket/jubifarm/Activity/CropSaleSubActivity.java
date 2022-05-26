package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.sanket.jubifarm.Adapter.CropSaleDetailsAdapter;
import com.sanket.jubifarm.Adapter.CropSaleSubAdapter;
import com.sanket.jubifarm.Modal.SaleDetailsPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class CropSaleSubActivity extends AppCompatActivity {
    RecyclerView rv_sub_crop_sale;
    SharedPrefHelper sharedPrefHelper;
    SqliteHelper sqliteHelper;
    String crop_sub_id="";
    ArrayList<SaleDetailsPojo> saleDetailsPojoArrayList=new ArrayList<>();
    CropSaleSubAdapter saleSubAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_sale_sub);
        sqliteHelper =new SqliteHelper(this);
        sharedPrefHelper=new SharedPrefHelper(this);
        rv_sub_crop_sale=findViewById(R.id.rv_sub_crop_sale);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.crop_sale) + "</font>"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            crop_sub_id=bundle.getString("crop_sub_id","");
        }
        saleDetailsPojoArrayList = sqliteHelper.getSubCropList(crop_sub_id);
        saleSubAdapter = new CropSaleSubAdapter(this, saleDetailsPojoArrayList,"0");
        rv_sub_crop_sale.setHasFixedSize(true);
        rv_sub_crop_sale.setLayoutManager(new LinearLayoutManager(this));
        rv_sub_crop_sale.setAdapter(saleSubAdapter);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        menu.findItem(R.id.home_arch).setVisible(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home_menu) {
            Intent intent = new Intent(this, HomeAcivity.class);
            this.startActivity(intent);
            return true;
        }if (id == R.id.home_arch) {
            setTitle(Html.fromHtml("<font color=\"#000000\">" + "TOTAL ARCHIVED SALE" + "</font>"));
            saleDetailsPojoArrayList = sqliteHelper.getSubCropListArchived();
            saleSubAdapter = new CropSaleSubAdapter(CropSaleSubActivity.this, saleDetailsPojoArrayList,"1");
            rv_sub_crop_sale.setHasFixedSize(true);
            rv_sub_crop_sale.setLayoutManager(new LinearLayoutManager(CropSaleSubActivity.this));
            rv_sub_crop_sale.setAdapter(saleSubAdapter);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
