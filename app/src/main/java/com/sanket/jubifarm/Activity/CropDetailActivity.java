package com.sanket.jubifarm.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Modal.PlantSubCategoryPojo;
import com.sanket.jubifarm.Modal.SubMonitoringModal;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CropDetailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_Postpantation, tv_subLantation, tvplantgrowth, tv_plant_id, tv_plant_name, tv_cordinates,tv_map,tv_crop,tv_land,tv_plant_visit,tv_fruited_date,tv_planted_date,tv_total_tree;
    private int click;
    private Context mcontext=this;
    SharedPrefHelper sharedPrefHelper;
    SqliteHelper sqliteHelper;
    String plant_id;
    String id;
    ImageView iv_plant;
    String crop_type_catagory_id="";
    String crop_type_subcatagory_id="";
    String farmer_id="";
    ArrayList<PlantSubCategoryPojo> plantSubCategoryPojos=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.VIEW_PLANT) + "</font>"));

        sharedPrefHelper = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);
        String language = sharedPrefHelper.getString("languageID","");
        tv_subLantation = findViewById(R.id.tv_subLantation);
        tv_subLantation.setOnClickListener(this); // calling onClick() method
        tv_Postpantation = findViewById(R.id.tv_Postpantation);
        tv_Postpantation.setOnClickListener(this);
        tvplantgrowth = findViewById(R.id.tvplantgrowth);
        tv_cordinates = findViewById(R.id.tv_cordinates);
        tv_map = findViewById(R.id.tv_map);
        tv_crop = findViewById(R.id.tv_crop);
        tv_land = findViewById(R.id.tv_land);
        tv_fruited_date = findViewById(R.id.tv_fruited_date);
        tv_planted_date = findViewById(R.id.tv_planted_date);
        tv_plant_name = findViewById(R.id.tv_plant_name);
        tv_plant_id = findViewById(R.id.tv_plant_id);
        tv_cordinates = findViewById(R.id.tv_cordinates);
        iv_plant = findViewById(R.id.iv_plant);
        tv_plant_visit = findViewById(R.id.tv_plant_visit);
        tv_total_tree = findViewById(R.id.tv_total_tree);
        tvplantgrowth.setOnClickListener(this);
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            plant_id=bundle.getString("plant_id","");
            sharedPrefHelper.setString("plant_id",plant_id);
            plantSubCategoryPojos=sqliteHelper.getPlantDeatails(plant_id);
            tv_plant_name.setText(plantSubCategoryPojos.get(0).getName());
            tv_plant_visit.setText(""+sqliteHelper.getvisitCount(plant_id));
            tv_plant_id.setText(plantSubCategoryPojos.get(0).getPlant_id());
            tv_cordinates.setText(plantSubCategoryPojos.get(0).getLatitude()+", "+plantSubCategoryPojos.get(0).getLongitude());
            tv_land.setText(sqliteHelper.getLandIDbyid(plantSubCategoryPojos.get(0).getLand_id()));
            tv_crop.setText(sqliteHelper.getCategotyName(Integer.parseInt(plantSubCategoryPojos.get(0).getCrop_type_catagory_id()),language));
            tv_fruited_date.setText(plantSubCategoryPojos.get(0).getFruited_date());
            tv_planted_date.setText(plantSubCategoryPojos.get(0).getPlanted_date());
            tv_total_tree.setText(plantSubCategoryPojos.get(0).getTotal_tree());
            crop_type_catagory_id = plantSubCategoryPojos.get(0).getCrop_type_catagory_id();
            crop_type_subcatagory_id = plantSubCategoryPojos.get(0).getCrop_type_subcatagory_id();
            farmer_id = plantSubCategoryPojos.get(0).getFarmer_id();
            id = String.valueOf(plantSubCategoryPojos.get(0).getId());
            String land_image=plantSubCategoryPojos.get(0).getPlant_image();
            if (land_image != null && land_image.length()>200) {
                byte[] decodedString = Base64.decode(land_image, Base64.NO_WRAP);
                InputStream inputStream = new ByteArrayInputStream(decodedString);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                iv_plant.setImageBitmap(bitmap);
            } else if (land_image.length() <=200) {
                try {
                    String url = APIClient.IMAGE_PLANTS_URL + land_image;
                    Picasso.with(this).load(url).placeholder(R.drawable.plant).into(iv_plant);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            tv_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri = "http://maps.google.com/maps?saddr=" + sharedPrefHelper.getString("LAT","") + ", " + sharedPrefHelper.getString("LONG","") + "&daddr=" + plantSubCategoryPojos.get(0).getLatitude() + "," + plantSubCategoryPojos.get(0).getLongitude();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);

                }
            });
        }

    }

    private void ViewGrowth() {
        Intent Regintent = new Intent(CropDetailActivity.this, PlantGrowthListActivity.class);
        Regintent.putExtra("id",id);
        Regintent.putExtra("farmer_id",farmer_id);
        Regintent.putExtra("crop_type_catagory_id",crop_type_catagory_id);
        Regintent.putExtra("crop_type_subcatagory_id",crop_type_subcatagory_id);
        Log.e("PlantDetalsList","======"+crop_type_catagory_id);
        startActivity(Regintent);
        finish();
    }

    private void Postplantation() {
        Intent Regintent = new Intent(CropDetailActivity.this, PostPlantationAcivity.class);
        Regintent.putExtra("plant_id",plant_id);
        Regintent.putExtra("farmer_id",farmer_id);
        startActivity(Regintent);
        finish();
    }

    private void Subplantation() {
        Intent Regintent = new Intent(CropDetailActivity.this, SubPlantationActivity.class);
        Regintent.putExtra("plant_id",plant_id);
        Regintent.putExtra("farmer_id",farmer_id);
        startActivity(Regintent);
        finish();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_subLantation:
               int count = sqliteHelper.getCountTables("sub_plantation",plant_id);
               if (count==0){
                   Subplantation();
               }else {
                   Subplantation();
               }
                break;
            case R.id.tv_Postpantation:
                int counts = sqliteHelper.getCountTables("sub_plantation",plant_id);
                int countss = sqliteHelper.getCountTables("post_plantation",plant_id);
                if (counts>0 && countss==0){
                    Postplantation();
                }else if(counts>0 && countss>0){
                    Postplantation();
                } else {
                    Toast.makeText(mcontext, "You have not done sub plantation, please do the sub plantation first.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tvplantgrowth:
//                int countsss = sqliteHelper.getCountTables("sub_plantation",plant_id);
//                int countssss = sqliteHelper.getCountTables("post_plantation",plant_id);
//                if (countsss==0 ){
                    ViewGrowth();
//                }else {
////                    Toast.makeText(mcontext, "You have not done sub plantation and post plantation, please do the sub plantation and post plantation first.", Toast.LENGTH_SHORT).show();
               // }
               // ViewGrowth();
                break;
            default:
                break;
        }
    }
}