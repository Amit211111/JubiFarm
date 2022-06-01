package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.sanket.jubifarm.Activity.AddLandActivity;
import com.sanket.jubifarm.Activity.LandHoldingActivity;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.cropImage.utils.UiHelper;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.utils.CommonClass;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PS_LandHolding extends AppCompatActivity {

    private static final int CAMERA_REQUEST=1888;
    String base64;
//    String [] sp_farmer ={"Select Farmer"};
//    String [] sp_landArea ={"Select Unit","Yard","Hectare","Bigha","Pucca Bigha","Acres","Biswansi"};
    Button btn_upload_land,btn_submitDetls;
    Spinner spnfarmerSelection,spn_Unit;
    EditText et_landto_be_added,et_land_name;
    SqliteHelper sqliteHelper;
    PSLandHoldingPojo psLandHoldingPojo;
    SharedPrefHelper sharedPrefHelper;
    PSLandHoldingPojo psLHPojo;
    ArrayList<PSLandHoldingPojo> arrayList = new ArrayList<>();
    public static android.app.Dialog submit_alert;

    ImageView img_addland;
    private String land_area = "", farmer_name = "", unit = "", land_id = "", p = "", ca = "", ec = "", bulk_density = "", land_name,
            filtrationr_rate = "", soil_texture = "", ph = "",image="";
    private String land_unit="";
    private String local_id;
    private String type="";
    ArrayList<String> farmarArrayList;
    HashMap<String, Integer> farmarNameHM;
    boolean isEditable = false;
    private Context context = this;

    private String screen_type = "";
    ArrayList<String>landArrayList = new ArrayList<>();
    HashMap<String, Integer> landNameHM = new HashMap<>();
    private ArrayList<String> UnitArrayList;
    private HashMap<String, Integer> UnitNameHM;
    String farmer_id="";
    int unit_id=0;
    private int unitId = 0, farmerId = 0, soilTypeId = 0, soilColorId = 0, soilCharId = 0, soilChemicalId = 0;

    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps_land_holding);
        getSupportActionBar().setTitle("Land Holding");

        sharedPrefHelper = new SharedPrefHelper(this);


        sqliteHelper = new SqliteHelper(getApplicationContext());
        //Intilize All
        IntilizeAll();
        setFarmerSpinner();
        setUnitSpinner();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            local_id = bundle.getString("local_id", "");
            land_area = bundle.getString("land_area", "");
            farmer_name = bundle.getString("farmer_name", "");
           unit = bundle.getString("land_unit", "");

            base64 = bundle.getString("land_image", "");
            land_id = bundle.getString("land_id", "");
            type = bundle.getString("type", "");
            farmer_id = bundle.getString("farmerId", "");

        }

        
            if (type.equals("edit")) {

                isEditable=true;
                getSupportActionBar().setTitle("Update Land Holding Details");
                psLHPojo=new PSLandHoldingPojo();

                et_land_name.setText(psLHPojo.getLand_name());
                et_landto_be_added.setText(psLHPojo.getLand_area());

                //spn_Unit.setSelection(Integer.parseInt(psLHPojo.getLand_unit()));
                psLHPojo= sqliteHelper.PSLandDetail("",land_id);
                base64 = psLHPojo.getLand_image();
                 spnfarmerSelection.setSelection(Integer.parseInt(psLHPojo.getFarmer_id()));

                if (base64 != null && base64.length() > 200) {
                    byte[] decodedString = Base64.decode(base64, Base64.NO_WRAP);
                    InputStream inputStream = new ByteArrayInputStream(decodedString);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    img_addland.setImageBitmap(bitmap);
                }
//                else if (base64.length() <= 200) {
//                    try {
//                        String url = APIClient.LAND_IMAGE_URL + base64;
//                        Picasso.with(context).load(url).placeholder(R.drawable.land).into(img_addland);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
                else {
                    img_addland.setImageResource(R.drawable.land);
                }

                unit=psLHPojo.getLand_unit();
                setFarmerSpinner();
                setUnitSpinner();
                et_landto_be_added.setText(psLHPojo.getLand_area());
                et_land_name.setText(psLHPojo.getLand_name());
            }



        btn_upload_land.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt, CAMERA_REQUEST);

            }
        });
//
        btn_submitDetls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (checkValidation()) {
                   Random random = new Random();
                   int value = random.nextInt(1000);

                   psLandHoldingPojo = new PSLandHoldingPojo();
                   psLandHoldingPojo.setLand_image(base64);
                   psLandHoldingPojo.setFarmer_id(String.valueOf(farmerId));
                   psLandHoldingPojo.setLand_area(et_landto_be_added.getText().toString().trim());
                   psLandHoldingPojo.setLand_unit(String.valueOf(unit_id));
                   psLandHoldingPojo.setLand_name(et_land_name.getText().toString().trim());
                   psLandHoldingPojo.setLatitude(sharedPrefHelper.getString("LAT","")+", "+sharedPrefHelper.getString("LONG",""));
                   psLandHoldingPojo.setLongitude(sharedPrefHelper.getString("LAT","")+", "+sharedPrefHelper.getString("LONG",""));

                   if (type.equals("edit")) {
                       psLandHoldingPojo.setLand_id(land_id);
                       sqliteHelper.updatePsLandData(psLandHoldingPojo, land_id);

                   } else {

                       psLandHoldingPojo.setLand_id(String.valueOf(value));
                       long inserted_id = sqliteHelper.pslandholding1(psLandHoldingPojo);


                   }

                   try {

                       showSubmitDialog(context, "Data has been saved in local database successfully.");

                       Intent intent=new Intent(PS_LandHolding.this,PS_LandHoldingList.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       context.startActivity(intent);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }

               }
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
    private void setFarmerSpinner() {
        farmarArrayList.clear();
        farmarNameHM = sqliteHelper.getAllPSFARMER();

        for (int i = 0; i < farmarNameHM.size(); i++) {
            farmarArrayList.add(farmarNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            //farmarArrayList.add(0, farmer_name);
        } else {
            farmarArrayList.add(0, getString(R.string.select_farmer));
        }



        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_list, farmarArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnfarmerSelection.setAdapter(arrayAdapter);

        if (type.equals("edit")) {
            farmer_name = sqliteHelper.getPSFarmerName(psLHPojo.getFarmer_id());
            int pos = arrayAdapter.getPosition(farmer_name);
            spnfarmerSelection.setSelection(pos);
        }

        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = farmer_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = farmer_name;
                spinnerPosition = arrayAdapter.getPosition(strpos1);
                spnfarmerSelection.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spnfarmerSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spnfarmerSelection.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_farmer))) {
                    if (spnfarmerSelection.getSelectedItem().toString().trim() != null) {
                        farmerId = farmarNameHM.get(spnfarmerSelection.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setUnitSpinner() {
        UnitArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(PS_LandHolding.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            UnitNameHM = sqliteHelper.getMasterSpinnerId(4, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            UnitNameHM = sqliteHelper.getMasterSpinnerId(4, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            UnitNameHM = sqliteHelper.getMasterSpinnerId(4, 4);
        }
        else
        {
            UnitNameHM = sqliteHelper.getMasterSpinnerId(4, 1);
        }


        for (int i = 0; i < UnitNameHM.size(); i++) {
            UnitArrayList.add(UnitNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            //UnitArrayList.add(0, unit);
        } else {
            UnitArrayList.add(0, getString(R.string.select_unit));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, UnitArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_Unit.setAdapter(Adapter);

        if (type.equals("edit")) {
            land_unit = sqliteHelper.getPSLandUnit(psLHPojo.getLand_unit());
            int pos = Adapter.getPosition(land_unit);
            spn_Unit.setSelection(pos);
        }

//        if (isEditable) {
//            int spinnerPosition = 0;
//            String strpos1 = unit;
//            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
//                strpos1 = unit;
//                spinnerPosition = Adapter.getPosition(strpos1);
//                spn_Unit.setSelection(spinnerPosition);
//                spinnerPosition = 0;
//            }
//        }
//        else{
//            spn_Unit.setSelection(0);
//        }

        spn_Unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_Unit.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_unit))) {
                    if (spn_Unit.getSelectedItem().toString().trim() != null) {
                        unit_id = UnitNameHM.get(spn_Unit.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public static void showSubmitDialog(Context context,String message) {
        submit_alert = new android.app.Dialog(context);

        submit_alert.setContentView(R.layout.submitdialog);
        submit_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = submit_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        android.widget.TextView tvDescription = (TextView) submit_alert.findViewById(R.id.tv_description);
        Button btnOk = (Button) submit_alert.findViewById(R.id.btnOk);

        tvDescription.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO
                submit_alert.dismiss();
                Intent intent = new Intent(context, PS_LandHoldingList.class);
                context.startActivity(intent);
            }
        });

        submit_alert.show();
        submit_alert.setCanceledOnTouchOutside(false);
    }
    private void IntilizeAll()
    {
        UnitArrayList=new ArrayList<>();
        UnitNameHM=new HashMap<>();
        farmarArrayList = new ArrayList<>();
        farmarNameHM = new HashMap<>();
        img_addland=findViewById(R.id.img_addland);
        btn_upload_land=findViewById(R.id.btn_upload_land);
        btn_submitDetls=findViewById(R.id.btn_submitDetls);
        spnfarmerSelection=findViewById(R.id.spnfarmerSelection);
        spn_Unit=findViewById(R.id.spn_Unit);
        et_landto_be_added=findViewById(R.id.et_landto_be_added);
        et_land_name=findViewById(R.id.et_land_name);
        sharedPrefHelper = new SharedPrefHelper(this);
    }
        private boolean checkValidation() {
            boolean ret = true;
            if (et_landto_be_added.getText().toString().equals("")) {
                et_landto_be_added.setError(getString(R.string.Please_enter_land_quantity));
                return false;
            }
            if (spn_Unit.getSelectedItem().toString().equals(getString(R.string.select_unit))) {
                Toast.makeText(context, getString(R.string.Please_select_unit), Toast.LENGTH_SHORT).show();
                return false;
            }

//        if (base64.equals("")) {
//            Toast.makeText(context, getString(R.string.Please_take_image_of_land), Toast.LENGTH_SHORT).show();
//            return false;
//        }
            return ret;
        }

}