package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanket.jubifarm.Modal.CropTypePojo;
import com.sanket.jubifarm.Modal.PlantGrowthPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ViewGrowthActivity extends AppCompatActivity {
    Button tvplantgrowth;
    ImageView img_tree;
    TextView tvCropselection, tvCropStaus, date, tvremarks;
    String id, localId="";
    SqliteHelper sqliteHelper;
    PlantGrowthPojo plantGrowthPojo;
    CropTypePojo statePojo;
    private Context context=this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_growth);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" +  getString(R.string.plant_growth_details)+ "</font>"));
     //   setTitle(" PLANT GROWTH DETAIL  ");
        sqliteHelper = new SqliteHelper(this);
        plantGrowthPojo = new PlantGrowthPojo();
        statePojo = new CropTypePojo();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id", "");
            localId = bundle.getString("localId", "");
        }


        initViews();


        setAllFieldsData();

        Log.e("=======",""+plantGrowthPojo.getPlant_id()+","+plantGrowthPojo.getCrop_status());

        tvplantgrowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewGrowthActivity.this, HomeAcivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initViews() {
        tvplantgrowth = findViewById(R.id.tvplantgrowth);
        tvCropselection = findViewById(R.id.tvCropselection);
        tvCropStaus = findViewById(R.id.tvCropStaus);
        date = findViewById(R.id.date);
        tvremarks = findViewById(R.id.tvremarks);
        img_tree = findViewById(R.id.img_tree);
    }

    private void setAllFieldsData() {
        plantGrowthPojo=sqliteHelper.getPlantgrwthDetail(localId);
        //String cropname = sqliteHelper.getgrowthSpndata(plantGrowthPojo.getPlant_id());
        //String cropstatus_ = sqliteHelper.getgrowthStatus_Spn(plantGrowthPojo.getCrop_status());
        tvCropselection.setText(sqliteHelper.getNameById("crop_type_sub_catagory_language", "name", "id", Integer.parseInt(plantGrowthPojo.getCrop_type_subcategory_id())));
        tvCropStaus.setText(sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(plantGrowthPojo.getCrop_status())));
        date.setText(plantGrowthPojo.getGrowth_date());
        if (plantGrowthPojo.getPlant_image() != null && plantGrowthPojo.getPlant_image().length()>200) {
            byte[] decodedString = Base64.decode(plantGrowthPojo.getPlant_image(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            img_tree.setImageBitmap(bitmap);
        } else if (plantGrowthPojo.getPlant_image().length() <=200) {
            try {
                String url = APIClient.IMAGE_PLANTS_URL + plantGrowthPojo.getPlant_image();
                Picasso.with(context).load(url).placeholder(R.drawable.plant).into(img_tree);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            img_tree.setImageResource(R.drawable.plant);
        }
    }
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
        return super.onOptionsItemSelected(item);
    }
}