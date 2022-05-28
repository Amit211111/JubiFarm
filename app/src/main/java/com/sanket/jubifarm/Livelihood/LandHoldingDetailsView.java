package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.sanket.jubifarm.Activity.AddLandActivity;
import com.sanket.jubifarm.Activity.AddPlantAcivity;
import com.sanket.jubifarm.Activity.CropMonitoring;
import com.sanket.jubifarm.Activity.CropPlanning;
import com.sanket.jubifarm.Activity.HomeAcivity;
import com.sanket.jubifarm.Activity.SoilInFoListActivity;
import com.sanket.jubifarm.Activity.ViewLandActivity;
import com.sanket.jubifarm.Livelihood.Adapter.Adapter_PS_LandHolding;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Modal.LandHoldingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class LandHoldingDetailsView extends AppCompatActivity {
    ImageView land_imagee;
    Button tvlandedit;
    TextView farmer_selection,tv_total_neem_plat,land_area,tv_landid,land_name,geo_cordinate;
    ArrayList<PSLandHoldingPojo> arrayList = new ArrayList<>();
    String id = "";
    SqliteHelper sqliteHelper;
    PSLandHoldingPojo psLandHoldingPojo;
    RecyclerView rvLandHoldning;
    int unitId=0;
    private Context context = this;
    private String land_id = "", land_areaa = "",land_namee="", farmer_name = "", total_plant = "",
            unit = "", land_image="", latitude="", longitude="",farmer_id,base64="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_details_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            AllIntilize();

//
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            land_id = bundle.getString("land_id", "");
//        }

//        psLandHoldingPojo= new PSLandHoldingPojo();
//        arrayList = sqliteHelper.PSgetRegistrationData();
//        if (arrayList.size()>0) {
//
//            farmer_selection.setText(arrayList.get(0).getFarmer_id());
//            land_name.setText(arrayList.get(0).getLand_name());
//            tv_total_neem_plat.setText(arrayList.get(0).getLand_area());
//            tv_landid.setText(arrayList.get(0).getLand_id());
//        }else {
//            Toast.makeText(getApplicationContext(),"No data found", Toast.LENGTH_LONG).show();
//        }
//
//
//        tvlandedit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(LandHoldingDetailsView.this, PS_LandHolding.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("type", "edit");
//                intent.putExtra("id",arrayList.get(0).getLocal_id());
//                intent.putExtra("id", id);
//                startActivity(intent);
//
//
//            }
//        });
        /*get intent values here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            land_id = bundle.getString("land_Id", "");
            farmer_id = bundle.getString("farmerId", "");
            psLandHoldingPojo = sqliteHelper.PSLandDetail(farmer_id, land_id);
            if (land_id.equals("")) {
                land_id = psLandHoldingPojo.getLand_id();
            }
            land_areaa = psLandHoldingPojo.getLand_area();
            land_namee= psLandHoldingPojo.getLand_name();

            farmer_name = sqliteHelper.getFarmerName(farmer_id);
            farmer_name=psLandHoldingPojo.getFarmer_id();
//            total_plant = sqliteHelper.getTotalPlantbyid(psLandHoldingPojo.getFarmer_id());
            unit = sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(psLandHoldingPojo.getLand_unit()));
            land_image = psLandHoldingPojo.getLand_image();
        }

        //All Set Values
        setTextValues();



        tvlandedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addLandIntent=new Intent(context, PS_LandHolding.class);
                addLandIntent.putExtra("screen_type", "edit_land");
                addLandIntent.putExtra("land_area", land_areaa);
                addLandIntent.putExtra("farmer_id",farmer_id);
                addLandIntent.putExtra("farmer_name", sqliteHelper.getNameById("ps_land_holding", "farmer_name", "id", Integer.parseInt(farmer_id)));
                addLandIntent.putExtra("unit", sqliteHelper.getNameById("master", "master_name", "caption_id", unitId));
                addLandIntent.putExtra("unit_id", unitId);
                addLandIntent.putExtra("land_image", base64);
                addLandIntent.putExtra("type", "edit");
                addLandIntent.putExtra("land_id", land_id);

                addLandIntent.putExtra("land_name", land_namee);
                startActivity(addLandIntent);
                finish();
            }
        });

    }
    private void setTextValues()
    {

        if (land_image != null && land_image.length() > 200) {
            byte[] decodedString = Base64.decode(land_image, Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            land_imagee.setImageBitmap(bitmap);
        } else if (land_image != null && land_image.length() <= 200) {
            try {
                String url = APIClient.LAND_IMAGE_URL + land_image;
                Picasso.with(context).load(url).placeholder(R.drawable.apple).into(land_imagee);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            land_imagee.setImageResource(R.drawable.land);
        }
        farmer_selection.setText(farmer_name);
        tv_total_neem_plat.setText(total_plant);
        land_area.setText(land_areaa + "( " + unit+ ")");
        land_name.setText(land_namee);
       // land_imagee.setIland_image);
        tv_landid.setText(land_id);
    }
    private void AllIntilize()
    {
        sqliteHelper=new SqliteHelper(this);
        land_imagee=findViewById(R.id.land_imagee);
        tvlandedit=findViewById(R.id.tvlandedit);
        farmer_selection=findViewById(R.id.farmer_selection);
        tv_total_neem_plat=findViewById(R.id.tv_total_neem_plat);
        land_area=findViewById(R.id.land_area);
        tv_landid=findViewById(R.id.tv_landid);
        land_name=findViewById(R.id.land_name);
        geo_cordinate=findViewById(R.id.geo_cordinate);
    }
    }
