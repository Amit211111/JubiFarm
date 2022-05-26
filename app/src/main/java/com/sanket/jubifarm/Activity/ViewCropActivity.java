package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.sanket.jubifarm.Adapter.CropDetailsAdapter;
import com.sanket.jubifarm.Adapter.FarmerFamilyAdapter;
import com.sanket.jubifarm.Modal.CropVegitableDetails;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class ViewCropActivity extends AppCompatActivity {
    RecyclerView rv_crop_detail;
    TextView tv_no_data;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    CropDetailsAdapter cropDetailsAdapter;
    ArrayList<CropVegitableDetails> arrayList=new ArrayList<>();
    String user_id="";
    String screen_type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_crop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.crop_details) + "</font>"));
        sqliteHelper=new SqliteHelper(this);
        sharedPrefHelper=new SharedPrefHelper(this);
        rv_crop_detail=findViewById(R.id.rv_crop_detail);
        tv_no_data=findViewById(R.id.tv_no_data);
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
           user_id= bundle.getString("farmer_id","");
           screen_type= bundle.getString("screen_type","");
        }
        setFarmerFamilyDetails();
    }

    private void setFarmerFamilyDetails() {
        if (sharedPrefHelper.getString("role_id","").equals("5")) {
            if (!sharedPrefHelper.getString("user_id", "").equals("")) {
                arrayList = sqliteHelper.getCropDetailsData(sharedPrefHelper.getString("user_id", ""));
                cropDetailsAdapter = new CropDetailsAdapter( arrayList,this);
                rv_crop_detail.setHasFixedSize(true);
                rv_crop_detail.setLayoutManager(new LinearLayoutManager(this));
                rv_crop_detail.setAdapter(cropDetailsAdapter);
            }
        } else {
            if (!user_id.equals("")) {
                arrayList = sqliteHelper.getCropDetailsData(user_id);
                cropDetailsAdapter = new CropDetailsAdapter(arrayList,this);
                rv_crop_detail.setHasFixedSize(true);
                rv_crop_detail.setLayoutManager(new LinearLayoutManager(this));
                rv_crop_detail.setAdapter(cropDetailsAdapter);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
                Intent intentCropMonitoring = new Intent(ViewCropActivity.this, FarmerDeatilActivity.class);
                intentCropMonitoring.putExtra("user_id", user_id);
                startActivity(intentCropMonitoring);
                finish();

            // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intentCropMonitoring = new Intent(ViewCropActivity.this, FarmerDeatilActivity.class);
        intentCropMonitoring.putExtra("user_id", user_id);
        startActivity(intentCropMonitoring);
        finish();
    }
}
