package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.cropImage.utils.UiHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class PS_LandHolding extends AppCompatActivity {

    private static final int CAMERA_REQUEST=1888;
    String base64;
    String [] sp_farmer ={"Select Farmer"};
    String [] sp_landArea ={"Select Unit","Yard","Hectare","Bigha","Pucca Bigha","Acres","Biswansi"};
    Button btn_upload_land,btn_submitDetls;
    Spinner spnfarmerSelection,spn_Unit;
    EditText et_landto_be_added,et_land_name;
    SqliteHelper sqliteHelper;
    PSLandHoldingPojo psLandHoldingPojo;
    ImageView img_addland;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps_land_holding);
        getSupportActionBar().setTitle("Land Holding");


        img_addland=findViewById(R.id.img_addland);
        btn_upload_land=findViewById(R.id.btn_upload_land);
        btn_submitDetls=findViewById(R.id.btn_submitDetls);
        spnfarmerSelection=findViewById(R.id.spnfarmerSelection);
        spn_Unit=findViewById(R.id.spn_Unit);
        et_landto_be_added=findViewById(R.id.et_landto_be_added);
        et_land_name=findViewById(R.id.et_land_name);
        //All Spinner
        ArrayAdapter farmer_adapter=new ArrayAdapter(PS_LandHolding.this, R.layout.spinner_list,sp_farmer);
        farmer_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnfarmerSelection.setAdapter(farmer_adapter);

        ArrayAdapter land_adapter=new ArrayAdapter(PS_LandHolding.this,R.layout.spinner_list,sp_landArea);
        land_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_Unit.setAdapter(land_adapter);
        sqliteHelper = new SqliteHelper(getApplicationContext());


        btn_upload_land.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt, CAMERA_REQUEST);

            }
        });


        btn_submitDetls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                psLandHoldingPojo=new PSLandHoldingPojo();
                psLandHoldingPojo.setBtn_upload_land(base64);
                psLandHoldingPojo.setFarmer_Selection(spnfarmerSelection.getSelectedItem().toString().trim());
                psLandHoldingPojo.setLand_Area(et_landto_be_added.getText().toString().trim() +" "+spn_Unit.getSelectedItem().toString().trim());
                psLandHoldingPojo.setLand_Name(et_land_name.getText().toString().trim());
                //psLandHoldingPojo.setLand_id();


                long id = sqliteHelper.pslandholding1(psLandHoldingPojo);
                Intent intent = new Intent(PS_LandHolding.this, PS_LandHoldingList.class);
                intent .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

//
//                Intent intent1=new Intent(PS_LandHolding.this, PS_Neem_LandHolding_List.class);
//                startActivity(intent1);





            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytes = stream.toByteArray();
//
            base64 = encodeTobase64(photo);
            img_addland.setImageBitmap(photo);
        }

    }

    private String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOS = null;
        try {
            System.gc();
            byteArrayOS = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOS);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            image.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOS);
        }
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP);
    }
}