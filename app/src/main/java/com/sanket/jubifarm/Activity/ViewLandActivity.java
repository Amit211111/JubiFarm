package com.sanket.jubifarm.Activity;

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
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.sanket.jubifarm.Modal.LandHoldingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ViewLandActivity extends AppCompatActivity {
    private Button tvplantgrowth,tvplantadd,tvplantvisit;
    private TextView tv_Farmer_name, tv_total_plat,
            tv_land_area, tv_landid, geo_cordinate,
            tv_map, tv_soil_type, tv_soil_color,tvlandedit,
            tv_soil_characteristics, tv_soil_chemical_composition,tv_soil_P,tv_soil_K,
            tv_soil_S,soil_N,tv_soil_mg,tv_soil_ca,tv_soil_bulk_density,tv_soil_ec,tv_landnmae,
            soil_filtration_rate,tv_soil_texture,tv_soil_Ph,tv_soil_cation_exchange_capacity,tv_visit_count;
    private ImageView img_tree;

    /*normal widgets*/
    private Context context = this;
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sharedPrefHelper;
    private String id;
Button tvUpdateSoilInfo;
    private String land_id = "", land_area = "", farmer_name = "", total_plant = "",
                   unit = "", land_image="", latitude="", longitude="",farmer_id;
    private String soil_type_name="",soil_color_name="",soil_characteristics_name="",
            soil_chemical_composition_name="", land_id_id="",base64="",p="",mg="",k="",s="",n="",ca="",ec=""
            ,bulk_density="",land_name,
            filtration_rate="",soil_texture="",ph="",cation_exchange_capacity="";
    int unitId=0;
    private ToggleButton togglebutton;
    private android.widget.LinearLayout my_linear_layout;
    private android.widget.RelativeLayout rl_soilifo;

    LandHoldingPojo holdingPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_land);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.land_details) + "</font>"));

        initViews();

        /*get intent values here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id", "");
            land_id = bundle.getString("land_id", "");
            farmer_id = bundle.getString("farmer_id", "");
            land_id_id = bundle.getString("land_id_id", "");
            holdingPojo = sqliteHelper.LandDetail(farmer_id,land_id_id);
            if(land_id.equals("")){
                land_id=holdingPojo.getLand_id();
            }
            land_area = holdingPojo.getArea();
            farmer_name = sqliteHelper.getFarmerName(farmer_id);
            total_plant = sqliteHelper.getTotalPlantbyid(holdingPojo.getId());
            unit = sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(holdingPojo.getLand_unit()));
            land_image = holdingPojo.getImage();
            latitude = holdingPojo.getLatitude();
            longitude = holdingPojo.getLongitude();
            soil_type_name = sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(holdingPojo.getSoil_type_id()));
            soil_color_name = sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(holdingPojo.getSoil_color_id()));
                    soil_characteristics_name = bundle.getString("soil_characteristics_name", "");
                            soil_chemical_composition_name = bundle.getString("soil_chemical_composition_name", "");

            base64 = holdingPojo.getImage();
            p = holdingPojo.getP();
            s = holdingPojo.getS();
            n = holdingPojo.getN();
            ca = holdingPojo.getCa();
            mg = holdingPojo.getMg();
            k = holdingPojo.getK();
            land_name = holdingPojo.getLand_name();
            bulk_density = holdingPojo.getBulk_density();
            ph = holdingPojo.getPh();
            ec = holdingPojo.getEc();
            filtration_rate = holdingPojo.getFiltration_rate();
            soil_texture = holdingPojo.getSoil_texture();
            cation_exchange_capacity =holdingPojo.getCation_exchange_capacity();
            tv_visit_count.setText(""+sqliteHelper.getlandvisitCount(land_id_id));
         //   unitId = Integer.parseInt(holdingPojo.getLand_unit());

        }

        setTextValues();
        if (soil_type_name != ""){
            rl_soilifo.setVisibility(View.VISIBLE);

        }else {
            rl_soilifo.setVisibility(View.GONE);

        }


        tvplantgrowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewLandActivity.this, CropPlanning.class);
                intent.putExtra("land_id_id", land_id_id); //land_id for view list according to land id.
                intent.putExtra("screen_type", "view_land");
                intent.putExtra("farmer_id", farmer_id);
                startActivity(intent);
                finish();
            }
        });

        tvlandedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addLandIntent=new Intent(context, AddLandActivity.class);
                addLandIntent.putExtra("screen_type", "edit_land");
                addLandIntent.putExtra("land_area", land_area);
                addLandIntent.putExtra("farmer_id",farmer_id);
                addLandIntent.putExtra("farmer_name", sqliteHelper.getNameById("farmer_registration", "farmer_name", "id", Integer.parseInt(farmer_id)));
                addLandIntent.putExtra("unit", sqliteHelper.getNameById("master", "master_name", "caption_id", unitId));
                addLandIntent.putExtra("unit_id", unitId);
                addLandIntent.putExtra("land_image", base64);
                addLandIntent.putExtra("land_id", land_id_id);
                addLandIntent.putExtra("soil_type_name", soil_type_name);
                addLandIntent.putExtra("soil_color_name", soil_color_name);
                addLandIntent.putExtra("soil_characteristics_name", soil_characteristics_name);
                addLandIntent.putExtra("soil_chemical_composition_name", soil_chemical_composition_name);
                addLandIntent.putExtra("p", p);
                addLandIntent.putExtra("n", n);
                addLandIntent.putExtra("mg", mg);
                addLandIntent.putExtra("k", k);
                addLandIntent.putExtra("ca", ca);
                addLandIntent.putExtra("s", s);
                addLandIntent.putExtra("land_name", land_name);
                addLandIntent.putExtra("filtration_rate", filtration_rate);
                addLandIntent.putExtra("soil_texture", soil_texture);
                addLandIntent.putExtra("ph", ph);
                addLandIntent.putExtra("ec", ec);
                addLandIntent.putExtra("cation_exchange_capacity", cation_exchange_capacity);
                addLandIntent.putExtra("bulk_density", bulk_density);
                startActivity(addLandIntent);
                finish();
            }
        });

        tvplantadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ViewLandActivity.this,AddPlantAcivity.class);
                intent.putExtra("land_id",land_id_id);
                intent.putExtra("farmer_id",farmer_id);
                intent.putExtra("fromViewLand","fromViewLand");

                startActivity(intent);
                finish();
            }
        });

        tvUpdateSoilInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewLandActivity.this, SoilInFoListActivity.class);
                intent.putExtra("land_id",land_id_id);
                startActivity(intent);
                finish();
            }
        });

        if (sharedPrefHelper.getString("user_type", "").equals("Farmer")) {
            tvplantvisit.setVisibility(View.INVISIBLE);
        } else {
            tvplantvisit.setVisibility(View.VISIBLE);
        }
        tvplantvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ViewLandActivity.this, CropMonitoring.class);
                intent.putExtra("land_id_id", land_id_id); //land_id for view list according to land id.
                intent.putExtra("screen_type", "crop_monitoring");
                intent.putExtra("from_view_land", "from_view_land");
                intent.putExtra("farmer_id", farmer_id);
                intent.putExtra("land_id_id", land_id_id); //land_id for view list according to land id.
                startActivity(intent);
                finish();

            }
        });
    }

    private void setTextValues() {
        tv_Farmer_name.setText(farmer_name);
        tv_total_plat.setText(total_plant);
        tv_land_area.setText(land_area + ", " + unit);
        tv_landid.setText(land_id);
        geo_cordinate.setText(sharedPrefHelper.getString("LAT", "") + ", " +
                sharedPrefHelper.getString("LONG", ""));
        tv_soil_type.setText(soil_type_name);
        tv_soil_color.setText(soil_color_name);
//        tv_soil_characteristics.setText(soil_characteristics_name);
//        tv_soil_chemical_composition.setText(soil_chemical_composition_name);
        tv_soil_ca.setText(ca);
        tv_soil_bulk_density.setText(bulk_density);
        tv_soil_cation_exchange_capacity.setText(cation_exchange_capacity);
        tv_soil_K.setText(k);
        tv_soil_S.setText(s);
        tv_soil_mg.setText(mg);
        tv_landnmae.setText(land_name);
        tv_soil_P.setText(p);
        tv_soil_Ph.setText(ph);
        tv_soil_texture.setText(soil_texture);
       soil_N.setText(n);
        soil_filtration_rate.setText(filtration_rate);
        tv_soil_ec.setText(ec);

        tv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?saddr=" + sharedPrefHelper.getString("LAT", "") + "," + sharedPrefHelper.getString("LONG", "") + "&daddr=" + latitude + "," + longitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                finish();
            }
        });
        if (land_image != null && land_image.length() > 200) {
            byte[] decodedString = Base64.decode(land_image, Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            img_tree.setImageBitmap(bitmap);
        } else if (land_image.length() <= 200) {
            try {
                String url = APIClient.LAND_IMAGE_URL + land_image;
                Picasso.with(context).load(url).placeholder(R.drawable.land).into(img_tree);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            img_tree.setImageResource(R.drawable.land);
        }
    }

    private void initViews() {
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        holdingPojo=new LandHoldingPojo();
        togglebutton = findViewById(R.id.togglebutton);
        my_linear_layout = findViewById(R.id.my_linear_layout);
        rl_soilifo = findViewById(R.id.rl_soilifo);
        tvplantgrowth = findViewById(R.id.tvplantgrowth);
        tv_map = findViewById(R.id.tv_map);
        tv_soil_texture = findViewById(R.id.tv_soil_texture);
        tv_soil_P = findViewById(R.id.tv_soil_P);
        tv_visit_count = findViewById(R.id.tv_visit_count);
        tv_soil_mg = findViewById(R.id.tv_soil_mg);
        tv_soil_S = findViewById(R.id.tv_soil_S);
        tv_landnmae = findViewById(R.id.tv_landnmae);
        tvUpdateSoilInfo = findViewById(R.id.tvUpdateSoilInfo);
        tv_soil_cation_exchange_capacity = findViewById(R.id.tv_soil_cation_exchange_capacity);
        tv_soil_ca = findViewById(R.id.tv_soil_ca);
        tv_soil_bulk_density = findViewById(R.id.tv_soil_bulk_density);
        tv_soil_Ph = findViewById(R.id.tv_soil_Ph);
        tv_soil_K = findViewById(R.id.tv_soil_K);
        tv_soil_ec = findViewById(R.id.tv_soil_ec);
        soil_filtration_rate = findViewById(R.id.soil_filtration_rate);
        soil_N = findViewById(R.id.soil_N);

        tv_Farmer_name = findViewById(R.id.tv_Farmer_name);
        tv_total_plat = findViewById(R.id.tv_total_plat);
        tv_land_area = findViewById(R.id.tv_land_area);
        tv_landid = findViewById(R.id.tv_landid);
        tvlandedit = findViewById(R.id.tvlandedit);
        tvplantadd = findViewById(R.id.tvplantadd);
        tvplantvisit = findViewById(R.id.tvplantvisit);
        geo_cordinate = findViewById(R.id.geo_cordinate);
        img_tree = findViewById(R.id.img_tree);

        tv_soil_type = findViewById(R.id.tv_soil_type);
                tv_soil_color = findViewById(R.id.tv_soil_color);
//        tv_soil_characteristics = findViewById(R.id.tv_soil_characteristics);
//         setToggleButtonClick()
//         tv_soil_chemical_composition = findViewById(R.id.tv_soil_chemical_composition);



        setToggleButtonClick();
    }


    private void setToggleButtonClick() {
        togglebutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    my_linear_layout.setVisibility(View.VISIBLE);
                } else {
                    // The toggle is disabled
                    my_linear_layout.setVisibility(View.GONE);
                }
            }
        });
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
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

}