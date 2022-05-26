package com.sanket.jubifarm.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.BuildConfig;
import com.sanket.jubifarm.Modal.CropPlaningPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.cropImage.enums.ImagePickerEnum;
import com.sanket.jubifarm.cropImage.listeners.IImagePickerLister;
import com.sanket.jubifarm.cropImage.utils.FileUtils;
import com.sanket.jubifarm.cropImage.utils.UiHelper;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.sanket.jubifarm.utils.CommonClass;
import com.sanket.jubifarm.utils.ImageLoadingUtils;
import com.yalantis.ucrop.UCrop;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPlantAcivity extends AppCompatActivity implements IImagePickerLister {
    Button btn_submitDetls;
    Spinner spnCropselection, spnLandSelection, spnfarmerselection, spnsub_Cropselection,spnUnitSelection;
    String farmerselection;
    private int LandSelection=0;
    String crop;
    ImageView img_setimage;
    private Spinner spn_seasons;
    private String season = "";
    private int seasonId = 0;
    TextView clickImage , GeoCoodinate,tv_Farmer_name,tv_land_name;
    EditText et_area,et_total_tree;
    CropPlaningPojo cropPlaningPojo;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    LinearLayout ll_date,ll_total_tree,ll_areaRequirement, ll_fruitationDate;
    EditText et_planted_date,et_fruited_date;
    String unit_name;
    int Unit;
    ArrayList<String> seasonArryList;
    HashMap<String, Integer> seasonNameHm;
    private int season_id=0;
    /*enable crop image*/
    private static final int CAMERA_ACTION_PICK_REQUEST_CODE = 610;
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    public static final int CAMERA_STORAGE_REQUEST_CODE = 611;
    public static final int ONLY_CAMERA_REQUEST_CODE = 612;
    public static final int ONLY_STORAGE_REQUEST_CODE = 613;
    private String currentPhotoPath = "";
    private UiHelper uiHelper = new UiHelper();
    private String base64 = "";
    ArrayList<String> cropCategoryList = new ArrayList<>();
    ArrayList<CropPlaningPojo> cropPlanningsAL = new ArrayList<>();
    ArrayList<String> landArrayList;
    HashMap<String, Integer> landHM;
    HashMap<String, Integer> cropCategoryHM;
    ArrayList<String> subCategoryArrayList = new ArrayList<>();
    HashMap<String, Integer> subCategoryHM;
    int cropCategory_id;
    int subcropCategory_id;
    private ArrayList<String> farmarArrayList = new ArrayList<>();
    private HashMap<String, Integer> farmarNameHM;
    private ArrayList<String> UnitArryList = new ArrayList<>();
    private HashMap<String, Integer> UnitNameHM;
    private String Select_Farmer;
    private int unitId = 0, farmerId = 0;
    boolean isEditable = false;
    LinearLayout ll_spn_farmer;
    private ImageLoadingUtils utils;
    private Context context=this;
    String selected_farmer;
    String land_id="",farmer_id="",fromViewLand="",screen_type="";
    DatePickerDialog datePickerDialog;
    public static android.app.Dialog submit_alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant_acivity);
        setTitle(Html.fromHtml("<font color=\"#000000\">" +getString(R.string.Add_Plant)+ "</font>"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);


        btn_submitDetls = findViewById(R.id.btn_submitDetls);
        spnCropselection = findViewById(R.id.spnCropselection);
        tv_land_name = findViewById(R.id.tv_land_name);
        GeoCoodinate = findViewById(R.id.GeoCoodinate);
        spnLandSelection = findViewById(R.id.spnLandSelection);
        spnfarmerselection = findViewById(R.id.spnfarmerselection);
        spnsub_Cropselection = findViewById(R.id.spnsub_Cropselection);
        ll_spn_farmer = findViewById(R.id.ll_spn_farmer);
        tv_Farmer_name = findViewById(R.id.tv_Farmer_name);
        ll_date = findViewById(R.id.ll_date);
        ll_areaRequirement = findViewById(R.id.ll_areaRequirement);
        ll_fruitationDate = findViewById(R.id.ll_fruitationDate);
        ll_total_tree = findViewById(R.id.ll_total_tree);
        et_fruited_date = findViewById(R.id.et_fruit_date);
        et_planted_date = findViewById(R.id.et_plant_date);
        et_area = findViewById(R.id.et_area);
        et_total_tree = findViewById(R.id.et_total_tree);
        spnUnitSelection = findViewById(R.id.spnUnitSelection);
        farmarNameHM = new HashMap<>();

        seasonArryList = new ArrayList<>();
        seasonNameHm = new HashMap<>();
        cropCategoryHM = new HashMap<>();
        subCategoryHM = new HashMap<>();
        landArrayList=new ArrayList<>();
        landHM=new HashMap<>();
        UnitNameHM = new HashMap<>();
        UnitArryList = new ArrayList<>();
        img_setimage = findViewById(R.id.img_setimage);
        spn_seasons = findViewById(R.id.spn_seasons);

        et_fruited_date.setKeyListener(null);
        et_planted_date.setKeyListener(null);

        et_planted_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                                + "-" + String.valueOf(year);
                        et_planted_date.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });
//        et_planted_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                int mYear = c.get(Calendar.YEAR); // current year
//                int mMonth = c.get(Calendar.MONTH); // current month
//                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
//                datePickerDialog = new DatePickerDialog(context,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                et_planted_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//
//                            }
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.show();
//            }
//        });
//        et_fruited_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                int mYear = c.get(Calendar.YEAR); // current year
//                int mMonth = c.get(Calendar.MONTH); // current month
//                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
//                datePickerDialog = new DatePickerDialog(context,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                et_fruited_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//                            }
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.show();
//            }
//
//        });


        et_fruited_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                                + "-" + String.valueOf(year);
                        et_fruited_date.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });


        GeoCoodinate.setText(sharedPrefHelper.getString("LAT","")+", "+sharedPrefHelper.getString("LONG",""));
        findViewById(R.id.CLICKIMAGE).setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (uiHelper.checkSelfPermissions(this));
            openCamera();
                    //uiHelper.showImagePickerDialog(this, this);
        });
       /* clickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    if (uiHelper.checkSelfPermissions(AddPlantAcivity.this))
                        uiHelper.showImagePickerDialog(AddPlantAcivity.this, this);
               // setImage();
            }
        });*/
       Bundle bundle=getIntent().getExtras();
       if (bundle!=null){
         land_id=  bundle.getString("land_id","");
          farmer_id= bundle.getString("farmer_id","0");
          fromViewLand= bundle.getString("fromViewLand","");
           screen_type= bundle.getString("screen_type","");
       }

        selected_farmer = sharedPrefHelper.getString("selected_farmer","");
        if (!selected_farmer.equals("")) {
            spnfarmerselection.setVisibility(View.GONE);
            tv_Farmer_name.setVisibility(View.VISIBLE);
            tv_Farmer_name.setText(sqliteHelper.getFarmerName(selected_farmer));
            getLandFromTable(Integer.parseInt(selected_farmer));
        } else {
            spnfarmerselection.setVisibility(View.VISIBLE);
            tv_Farmer_name.setVisibility(View.GONE);
        }

        if (!fromViewLand.equals("")){
           spnLandSelection.setVisibility(View.GONE);
           spnfarmerselection.setVisibility(View.GONE);
           tv_land_name.setVisibility(View.VISIBLE);
           tv_Farmer_name.setVisibility(View.VISIBLE);
           tv_land_name.setText(sqliteHelper.getLandName(land_id));
           tv_Farmer_name.setText(sqliteHelper.getFarmerName(farmer_id));

       }else {
           spnLandSelection.setVisibility(View.VISIBLE);
           spnfarmerselection.setVisibility(View.VISIBLE);
           tv_land_name.setVisibility(View.GONE);
           tv_Farmer_name.setVisibility(View.GONE);
       }


        btn_submitDetls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    cropPlaningPojo = new CropPlaningPojo();
                    cropPlaningPojo.setPlant_name(spnsub_Cropselection.getSelectedItem().toString());
                    cropPlaningPojo.setPlant_id(String.valueOf(subcropCategory_id));
                    cropPlaningPojo.setCrop_type_catagory_id(String.valueOf(cropCategory_id));
                    cropPlaningPojo.setCrop_type_subcatagory_id(String.valueOf(subcropCategory_id));
                    cropPlaningPojo.setLand_id(String.valueOf(LandSelection));
                    if (fromViewLand.equals("")) {
                        if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
                            if (selected_farmer.equals(""))
                                cropPlaningPojo.setFarmer_id(String.valueOf(farmerId));
                            else
                                cropPlaningPojo.setFarmer_id(selected_farmer);

                        } else {
                            cropPlaningPojo.setFarmer_id(sharedPrefHelper.getString("farmer_id", ""));
                        }
                    }else {
                        cropPlaningPojo.setFarmer_id(String.valueOf(farmer_id));
                        cropPlaningPojo.setLand_id(land_id);

                    }
                    cropPlaningPojo.setUser_id(sharedPrefHelper.getString("user_id", ""));
                    cropPlaningPojo.setLatitude(sharedPrefHelper.getString("LAT", ""));
                    cropPlaningPojo.setLongitude(sharedPrefHelper.getString("LONG", ""));
                    cropPlaningPojo.setPlant_image(base64);
                    cropPlaningPojo.setPlanted_date(et_planted_date.getText().toString());
                    cropPlaningPojo.setFruited_date(et_fruited_date.getText().toString());
                    cropPlaningPojo.setArea(et_area.getText().toString());
                    cropPlaningPojo.setTotal_tree(et_total_tree.getText().toString());
                    cropPlaningPojo.setSeason(String.valueOf(season_id));
                    cropPlaningPojo.setUnit(Unit);
                    cropPlaningPojo.setId(CommonClass.getUUID());

                    sqliteHelper.AddPlantData(cropPlaningPojo);


                    /*if (isInternetOn()) {
                        cropPlanningsAL = sqliteHelper.getAddPlantDataToBeSync();
                        if (cropPlanningsAL.size() > 0) {
                            for (int i = 0; i < cropPlanningsAL.size(); i++) {
                                cropPlanningsAL.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));

                                Gson gson = new Gson();
                                String data = gson.toJson(cropPlanningsAL.get(i));
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                RequestBody body = RequestBody.create(JSON, data);

                                sendAddPlantData(body, Integer.parseInt(cropPlanningsAL.get(i).getLocal_id()));
                            }
                        }
                    } else {*/
                    if (screen_type.equals("fromcropplanning")) {
//                        Toast.makeText(context, R.string.no_internet_data_saved_locally, Toast.LENGTH_LONG).show();
                        showSubmitDialog(context,"Data has been saved in local database successfully.");
//                        Intent intentHome = new Intent(AddPlantAcivity.this, CropPlanning.class);
//                        startActivity(intentHome);
//                        finish();
                    }else
                    if (fromViewLand.equals("")) {
                        Intent intentHome = new Intent(AddPlantAcivity.this, HomeAcivity.class);
                        startActivity(intentHome);
                        finish();
                    }else {
                        Intent intentHome = new Intent(AddPlantAcivity.this, ViewLandActivity.class);
                        intentHome.putExtra("land_id_id",land_id);
                        intentHome.putExtra("farmer_id",farmer_id);
                        startActivity(intentHome);
                        finish();
                    }


                        /*Toast.makeText(context, R.string.no_internet_data_saved_locally, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, CropPlanning.class);
                        startActivity(intent);
                        finish();
                    }*/
                }}
        });

        getAllCategoryList();
        setSeasonSpinner();
        setCategorySpinner();
        setSubcategorySpinner();
        setUnitSpinner();
        if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
            if (selected_farmer.equals("")) {
                ll_spn_farmer.setVisibility(View.VISIBLE);
                setFarmerSpinner();
            }


        } else {
            ll_spn_farmer.setVisibility(View.GONE);
            setLandSpinner();
        }


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
                Intent intent = new Intent(context, CropPlanning.class);
                context.startActivity(intent);
            }
        });

        submit_alert.show();
        submit_alert.setCanceledOnTouchOutside(false);
    }


    private boolean isInternetOn() {
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }


    private void setLandSpinner() {
        landArrayList.clear();
        landHM = sqliteHelper.getLandHoldingListforFarmer(sharedPrefHelper.getString("farmer_id", ""));

        for (int i = 0; i < landHM.size(); i++) {
            landArrayList.add(landHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
        } else {
            landArrayList.add(0, getString(R.string.Select_Land));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, landArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLandSelection.setAdapter(Adapter);

        spnLandSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spnLandSelection.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.Select_Land))) {
                    if (spnLandSelection.getSelectedItem().toString().trim() != null) {
                        LandSelection = landHM.get(spnLandSelection.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setFarmerSpinner() {
        farmarArrayList.clear();
        farmarNameHM = sqliteHelper.getFarmerspinner();

        for (int i = 0; i < farmarNameHM.size(); i++) {
            farmarArrayList.add(farmarNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            farmarArrayList.add(0, Select_Farmer);
        } else {
            farmarArrayList.add(0, getString(R.string.select_farmer));
        }
        final ArrayAdapter Adapte = new ArrayAdapter(this, R.layout.spinner_list, farmarArrayList);
        Adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnfarmerselection.setAdapter(Adapte);

        spnfarmerselection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spnfarmerselection.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_farmer))) {
                    if (spnfarmerselection.getSelectedItem().toString().trim() != null) {
                        farmerId = farmarNameHM.get(spnfarmerselection.getSelectedItem().toString().trim());
                        getLandFromTable(farmerId);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }







 private void setUnitSpinner() {
     UnitArryList.clear();
         SharedPrefHelper spf = new SharedPrefHelper(AddPlantAcivity.this);
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
             UnitArryList.add(UnitNameHM.keySet().toArray()[i].toString().trim());
         }
         Collections.sort(UnitArryList);
         if (isEditable) {
             //CasteArrayList.add(0, Id_Card);
         } else {
             UnitArryList.add(0, getString(R.string.select_unit));
         }

         final ArrayAdapter dd = new ArrayAdapter(this, R.layout.spinner_list, UnitArryList);
         dd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     spnUnitSelection.setAdapter(dd);
         if (isEditable) {
             int spinnerPosition = 0;
             String strpos1 = unit_name;
             if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                 strpos1 = unit_name;
                 spinnerPosition = dd.getPosition(strpos1);
                 spnUnitSelection.setSelection(spinnerPosition);
                 spinnerPosition = 0;
             }
         }

     spnUnitSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 if (!spnUnitSelection.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_unit))) {
                     if (spnUnitSelection.getSelectedItem().toString().trim() != null) {
                         Unit = UnitNameHM.get(spnUnitSelection.getSelectedItem().toString().trim());
                     }
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });
     }







//        UnitArryList.clear();
//        UnitNameHM = sqliteHelper.getUnitspinner();
//
//        for (int i = 0; i < UnitArryList.size(); i++) {
//            UnitArryList.add(UnitNameHM.keySet().toArray()[i].toString().trim());
//        }
//        if (isEditable) {
//            UnitArryList.add(0, Select_Unit);
//        } else {
//            UnitArryList.add(0, getString(R.string.select_unit));
//        }
//        final ArrayAdapter Adapte = new ArrayAdapter(this, R.layout.spinner_list, UnitArryList);
//        Adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnfarmerselection.setAdapter(Adapte);
//
//        spnfarmerselection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (!spnfarmerselection.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_unit))) {
//                    if (spnfarmerselection.getSelectedItem().toString().trim() != null) {
//                        Unit = UnitNameHM.get(spnfarmerselection.getSelectedItem().toString().trim());
//                       // getLandFromTable(farmerId);
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }

    private void setSeasonSpinner() {
        seasonArryList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(AddPlantAcivity.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            seasonNameHm = sqliteHelper.getMasterSpinnerId(5, 2);
        }
        else  if(language.equalsIgnoreCase("kan"))
        {
            seasonNameHm = sqliteHelper.getMasterSpinnerId(5, 3);
        }
        else  if(language.equalsIgnoreCase("guj"))
        {
            seasonNameHm = sqliteHelper.getMasterSpinnerId(5, 4);
        }
        else
        {
            seasonNameHm = sqliteHelper.getMasterSpinnerId(5, 1);
        }


        for (int i = 0; i < seasonNameHm.size(); i++) {
            seasonArryList.add(seasonNameHm.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
        } else {
            seasonArryList.add(0, getString(R.string.select_season));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, seasonArryList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_seasons.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = season;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = season;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_seasons.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_seasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_seasons.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_season))) {
                    if (spn_seasons.getSelectedItem().toString().trim() != null) {
                        season_id = seasonNameHm.get(spn_seasons.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAllCategoryList() {
        cropCategoryList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(AddPlantAcivity.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            cropCategoryHM = sqliteHelper.getAllCategoryType(2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            cropCategoryHM = sqliteHelper.getAllCategoryType(3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            cropCategoryHM = sqliteHelper.getAllCategoryType(4);
        }
        else
        {
            cropCategoryHM = sqliteHelper.getAllCategoryType(1);
        }
        for (int i = 0; i < cropCategoryHM.size(); i++) {
            cropCategoryList.add(cropCategoryHM.keySet().toArray()[i].toString().trim());
        }

        cropCategoryList.add(0, getString(R.string.select_crop_category));
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, cropCategoryList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCropselection.setAdapter(Adapter);
    }

    private void setCategorySpinner() {
        spnCropselection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!spnCropselection.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_crop_category))) {
                    if (spnCropselection.getSelectedItem().toString().trim() != null) {
                        cropCategory_id = cropCategoryHM.get(spnCropselection.getSelectedItem().toString().trim());
                        if (cropCategory_id==6 ){
                            ll_date.setVisibility(View.VISIBLE);
                        }else {
                            ll_date.setVisibility(View.VISIBLE);
                        }
                        if (cropCategory_id==4 || cropCategory_id==6){
                            ll_total_tree.setVisibility(View.VISIBLE);
                        }else {
                            ll_total_tree.setVisibility(View.GONE);
                        }
//                        if (cropCategory_id==6 || cropCategory_id==4){
//                            ll_areaRequirement.setVisibility(View.GONE);
//                        }else {
//                            ll_areaRequirement.setVisibility(View.VISIBLE);
//                        }


                        if(cropCategory_id ==4 || cropCategory_id==6){
                            ll_fruitationDate.setVisibility(View.GONE);
                        }else{
                            ll_fruitationDate.setVisibility(View.VISIBLE);

                        }

                        getAllDistrictFromTable(cropCategory_id);
                    }
                }else{
                    cropCategory_id=0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void getLandFromTable(int farmerId) {
        landArrayList.clear();
        landHM = sqliteHelper.getLandHoldingListforFarmer(String.valueOf(farmerId));

        for (int i = 0; i < landHM.size(); i++) {
            landArrayList.add(landHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
        } else {
            landArrayList.add(0, getString(R.string.Select_Land));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, landArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLandSelection.setAdapter(Adapter);

        spnLandSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spnLandSelection.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.Select_Land))) {
                    if (spnLandSelection.getSelectedItem().toString().trim() != null) {
                        LandSelection = landHM.get(spnLandSelection.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAllDistrictFromTable(int state_id) {
        subCategoryArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(AddPlantAcivity.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            subCategoryHM = sqliteHelper.getAllSubCategory(state_id, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            subCategoryHM = sqliteHelper.getAllSubCategory(state_id, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            subCategoryHM = sqliteHelper.getAllSubCategory(state_id, 4);
        }
        else
        {
            subCategoryHM = sqliteHelper.getAllSubCategory(state_id, 1);
        }
        for (int i = 0; i < subCategoryHM.size(); i++) {
            subCategoryArrayList.add(subCategoryHM.keySet().toArray()[i].toString().trim());
        }

        subCategoryArrayList.add(0, getString(R.string.select_sub_crop_category));
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, subCategoryArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnsub_Cropselection.setAdapter(Adapter);
    }

    private void setSubcategorySpinner() {
        spnsub_Cropselection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spnsub_Cropselection.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_sub_crop_category))) {
                    if (spnsub_Cropselection.getSelectedItem().toString().trim() != null) {
                        subcropCategory_id = subCategoryHM.get(spnsub_Cropselection.getSelectedItem().toString().trim());
                    }
                }else{
                    subcropCategory_id=0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sendAddPlantData(RequestBody body, int local_id) {
        ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).add_plant(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    if (status.equalsIgnoreCase("1")) {
                        sqliteHelper.updateAddPlantFlag("crop_planning",local_id,1);
                        String plant_id = jsonObject.optString("plant_id_AI");
                        String plant_ids = jsonObject.optString("plant_id");
                        sqliteHelper.updateAddPlantID("crop_planning",local_id, plant_ids);
                        sqliteHelper.updateServerid("crop_planning",local_id, Integer.parseInt(plant_id));
                        if (screen_type.equals("fromcropplanning")) {
                            Intent intentHome = new Intent(AddPlantAcivity.this, CropPlanning.class);
                            startActivity(intentHome);
                            finish();
                        }else
                        if (fromViewLand.equals("")) {
                            Intent intentHome = new Intent(AddPlantAcivity.this, HomeAcivity.class);
                            startActivity(intentHome);
                            finish();
                        }else {
                            Intent intentHome = new Intent(AddPlantAcivity.this, ViewLandActivity.class);
                            intentHome.putExtra("land_id_id",land_id);
                            intentHome.putExtra("farmer_id",farmer_id);
                            startActivity(intentHome);
                            finish();
                        }
                    }
                    else {
                        Toast.makeText(AddPlantAcivity.this, "" + message, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    private boolean checkValidation() {
        boolean ret = true;

//        if (base64.equals("")){
//            Toast.makeText(this, getString(R.string.Please_take_image_of_plant), Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if(cropCategory_id==0){
            Toast.makeText(this, getString(R.string.Please_select_crop), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(subcropCategory_id==0){
            Toast.makeText(this, getString(R.string.Please_select_sub_crop), Toast.LENGTH_SHORT).show();
            return false;
        }
        /*if (spnsub_Cropselection.getSelectedItem().toString().equals("") || spnsub_Cropselection.getSelectedItem().toString() ==null){
            Toast.makeText(this, getString(R.string.Please_select_crop), Toast.LENGTH_SHORT).show();
            return false;
        }*/
        if(cropCategory_id ==4 || cropCategory_id==6) {

        }else{
            if (et_fruited_date.getText().toString().equals("")) {
                Toast.makeText(this, getString(R.string.please_select_fruited_date), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (et_planted_date.getText().toString().equals("")){
            Toast.makeText(this, getString(R.string.please_select_planted_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        return ret;
    }

    @Override
    public void onBackPressed() {
         if (screen_type.equals("fromcropplanning")) {
            Intent intentHome = new Intent(AddPlantAcivity.this, CropPlanning.class);
            startActivity(intentHome);
            finish();
        }else
        if (fromViewLand.equals("")) {
            Intent intentHome = new Intent(AddPlantAcivity.this, HomeAcivity.class);
            startActivity(intentHome);
            finish();
        }else {
            Intent intentHome = new Intent(AddPlantAcivity.this, ViewLandActivity.class);
            intentHome.putExtra("land_id_id",land_id);
            intentHome.putExtra("farmer_id",farmer_id);
            startActivity(intentHome);
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
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
            if (screen_type.equals("fromcropplanning")) {
                Intent intentHome = new Intent(AddPlantAcivity.this, CropPlanning.class);
                startActivity(intentHome);
                finish();
            }else
            if (fromViewLand.equals("")) {
                Intent intentHome = new Intent(AddPlantAcivity.this, HomeAcivity.class);
                startActivity(intentHome);
                finish();
            }else {
                Intent intentHome = new Intent(AddPlantAcivity.this, ViewLandActivity.class);
                intentHome.putExtra("land_id_id",land_id);
                intentHome.putExtra("farmer_id",farmer_id);
                startActivity(intentHome);
                finish();
            }
            // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionSelected(ImagePickerEnum imagePickerEnum) {

        if (imagePickerEnum == ImagePickerEnum.FROM_CAMERA)
            openCamera();
        else if (imagePickerEnum == ImagePickerEnum.FROM_GALLERY)
            openImagesDocument();

    }

    private void openCamera() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file;
        try {
            file = getImageFile(); // 1
        } catch (Exception e) {
            e.printStackTrace();
            uiHelper.toast(this, getString(R.string.Please_take_another_image));
            return;
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID.concat(".provider"), file);
        else
            uri = Uri.fromFile(file); // 3
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
        startActivityForResult(pictureIntent, CAMERA_ACTION_PICK_REQUEST_CODE);
    }

    private File getImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        /*File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        ); */
        String imagesFolder="images";
        File storageDir = new File(
                Environment.getExternalStorageDirectory(), imagesFolder
        );
        System.out.println(storageDir.getAbsolutePath());
        if (!storageDir.exists()){
            storageDir.mkdirs();
            System.out.println("File not exists"); }
        else
            System.out.println("File exists");
        //File file = File.createTempFile(imageFileName, ".jpg", storageDir);
        File file = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                uiHelper.toast(this, getString(R.string.ImageCropper1));
                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                uiHelper.toast(this, getString(R.string.ImageCropper2));
                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                uiHelper.toast(this, getString(R.string.ImageCropper3));
                finish();
            }
        } else if (requestCode == ONLY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else {
                uiHelper.toast(this, getString(R.string.ImageCropper2));
                finish();
            }
        } else if (requestCode == ONLY_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else {
                uiHelper.toast(this, getString(R.string.ImageCropper3));
                finish();
            }
        }
    }

    private void openImagesDocument() {
        Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pictureIntent.setType("image/*");
        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
            pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(pictureIntent, getString(R.string.Select_Picture)), PICK_IMAGE_GALLERY_REQUEST_CODE);
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String getStringImage(Bitmap image) {
        ByteArrayOutputStream byteArrayOS = null;
        try {
            System.gc();
            byteArrayOS = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOS);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            image.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOS);
        }
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //profile crop-image intent
            if (requestCode == CAMERA_ACTION_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
                Uri uri = Uri.parse(currentPhotoPath);
                openCropActivity(uri, uri);
            } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = UCrop.getOutput(data);
                    showImage(uri);
                }
            } else if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                try {
                    Uri sourceUri = data.getData();
                    File file = getImageFile();
                    Uri destinationUri = Uri.fromFile(file);
                    openCropActivity(sourceUri, destinationUri);
                } catch (Exception e) {
                    uiHelper.toast(this, getString(R.string.Please_select_another_image));
                }
            }
        }
    }

    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.colorAccent));
        UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(500, 500)
                .withAspectRatio(5f, 5f)
                .start(this);
    }
    public Bitmap compressImage(String filePath) {

        //String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath,options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 4024.0f;
        float maxWidth = 4024.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }
        utils = new ImageLoadingUtils(this);
        options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16*1024];

        try{
            bmp = BitmapFactory.decodeFile(filePath,options);
        }
        catch(OutOfMemoryError exception){
            exception.printStackTrace();

        }
        try{
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        }
        catch(OutOfMemoryError exception){
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float)options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;







        Matrix scaleMatrix = new Matrix();
//        Matrix scaleMatrix = {0.9846154, 0.0, 4.4307556; 0.0, 0.9846154, 7.8769226][0.0, 0.0, 1.0]};

        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        String lat = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system
        try {

            lat = "Lat.: " + sharedPrefHelper.getString("LAT","");
            lat += " Long.:" + sharedPrefHelper.getString("LONG","");
        } catch (Exception e) {
            e.printStackTrace();
        }




        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        Paint tPaint = new Paint();
        tPaint.setTextSize(7);
        tPaint.setColor(Color.parseColor("#E51900"));
        tPaint.setStyle(Paint.Style.FILL);

        Paint mpaint= new Paint();
        mpaint.setColor(Color.parseColor("#40FFFFFF"));
        mpaint.setStyle(Paint.Style.FILL);
        if(ratioX<1) {
            canvas.drawRect(0f, scaledBitmap.getHeight() - 65f, scaledBitmap.getWidth(), scaledBitmap.getHeight(), mpaint);
            canvas.drawText(dateTime, 25f, scaledBitmap.getHeight() - 35f, tPaint);
            canvas.drawText(lat, 25f, scaledBitmap.getHeight() - 10f, tPaint);
        }
        else
        {
            canvas.drawRect(0f, scaledBitmap.getHeight() - 120f, scaledBitmap.getWidth(), scaledBitmap.getHeight(), mpaint);
            canvas.drawText(dateTime, 10f, scaledBitmap.getHeight() - 25f, tPaint);
            canvas.drawText(lat, 10f, scaledBitmap.getHeight() - 12f, tPaint);
        }
//        canvas.drawRect(middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2,middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2,tPaint);

        ExifInterface exif;
        try {

            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        FileOutputStream out = null;
//        String filename = getFilename();
//        try {
//            out = new FileOutputStream(filename);
//            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        return scaledBitmap;

    }

    private void showImage(Uri imageUri) {
        try {
            File file;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                file = FileUtils.getFile(this, imageUri);
            } else {
                file = new File(currentPhotoPath);
            }
            InputStream inputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            bitmap=compressImage(imageUri.getPath());
            img_setimage.setImageBitmap(bitmap); //set to show image

            bitmap = getResizedBitmap(bitmap, 300); //get resize image
            base64 = getStringImage(bitmap); //get base 64 image file.
            Log.e("image", base64);
        } catch (Exception e) {
            uiHelper.toast(this, getString(R.string.profile_picture));
        }
    }
}