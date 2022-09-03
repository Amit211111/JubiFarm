package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class NeemPlantationViewActivity extends AppCompatActivity {

    TextView Neem_Id, Land_id, et_plant_date, GeoCoodinate;
    ImageView img_tree;
    SqliteHelper sqliteHelper;
    ArrayList<PSNeemPlantationPojo> psNeemPlantationPojos;
    ArrayList<PSNeemPlantationPojo> psNeemPlantationPojoArrayList;
    SharedPrefHelper sharedPrefHelper;
    String id = "";
    String land_name="";
    String neem_id="";
    String st_image="";
    String land_id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neem_plantation_view);
        setTitle(" View Neem Plantation ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Neem_Id = findViewById(R.id.Neem_Id);
        Land_id = findViewById(R.id.Land_id);
        et_plant_date = findViewById(R.id.et_plant_date);
        GeoCoodinate = findViewById(R.id.GeoCoodinate);
        img_tree=findViewById(R.id.img_tree);
        psNeemPlantationPojoArrayList=new ArrayList<>();
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        GeoCoodinate.setText(sharedPrefHelper.getString("LAT","")+", "+sharedPrefHelper.getString("LONG",""));

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            id = bundle.getString("id", "");
//            land_name = bundle.getString("land_name", "");
//            neem_id = bundle.getString("neem_id", "");
//            cordinates_dates = bundle.getString("date_cordinates", "");


        }
//        Land_id.setText(land_name);
//        Neem_Id.setText(neem_id);
//        et_plant_date.setText(cordinates_dates);


        psNeemPlantationPojoArrayList = sqliteHelper.getViewDataNeem(id);
        land_id=psNeemPlantationPojoArrayList.get(0).getLand_id();
        land_name= sqliteHelper.getCloumnNameLand("land_name","ps_land_holding","where land_id='" +land_id+"'");
         Land_id.setText(land_name);

        Neem_Id.setText(psNeemPlantationPojoArrayList.get(0).getNeem_id());
        et_plant_date.setText(psNeemPlantationPojoArrayList.get(0).getPlantation_Date());
        st_image = String.valueOf(psNeemPlantationPojoArrayList.get(0).getNeem_plantation_image());
        if (st_image != null && st_image.length() > 200) {
            byte[] decodedString = Base64.decode(st_image, Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            img_tree.setImageBitmap(bitmap);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(NeemPlantationViewActivity.this, PS_NeemPlantationList.class);
                startActivity(intent);
                return true;
            case R.id.home_menu:
                Intent intent1 = new Intent(this,ParyavaranSakhiHome.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}