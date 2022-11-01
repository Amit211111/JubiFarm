package com.sanket.jubifarm.Activity;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.BuildConfig;
import com.sanket.jubifarm.Modal.LandHoldingPojo;
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
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLandActivity extends AppCompatActivity implements IImagePickerLister {
    Button btn_submitDetls, btn_upload_land;
    private ImageView img_addland;
    private Spinner spnfarmerSelection, spn_soil_type, spn_soil_color,
            spn_soil_characteristics, spn_soil_chemical_composition;
    private EditText et_landto_be_added;
    private Spinner spn_Unit;
    private LinearLayout ll_farmer_selection;
    private ToggleButton toggle_button;
    private LinearLayout ll_land_soil_info;
    private TextView tv_Farmer_name;
    /*normal widgets*/
    private Context context = this;
    private String id = "";
    private String screen_type = "";
    private String land_area = "", farmer_name = "", unit = "", land_id = "", p = "", ca = "", ec = "", bulk_density = "", land_name,
            filtration_rate = "", soil_texture = "", ph = "", cation_exchange_capacity = "";
    private String soil_type_name = "", soil_color_name = "", soil_characteristics_name = "", soil_chemical_composition_name = "";
    private int unitId = 0, farmerId = 0, soilTypeId = 0, soilColorId = 0, soilCharId = 0, soilChemicalId = 0;
    private LandHoldingPojo landHoldingPojo;
    private ArrayList<LandHoldingPojo> landHoldingAL = new ArrayList<>();
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sharedPrefHelper;
    private ArrayList<String> UnitArrayList;
    private HashMap<String, Integer> UnitNameHM;

    private ArrayList<String> farmarArrayList;
    private HashMap<String, Integer> farmarNameHM;
    private ImageLoadingUtils utils;
    //    private String filtration_rate;
//    private String soil_texture;
//    private String ph;
//    private String bulk_density;
//    private String ec;
    // private String p;
    private String s;
    private String mg;
    private String k;
    private String n;
    //  private String ca;
    // private String land_name;
    private String cation_exchange;
    private String farmer_id;
    EditText et_filtration_rate, et_soil_texture, et_ph, et_bulk_density, et_cation_exchange_capacity, et_ec;
    EditText et_p, et_s, et_mg, et_k, et_n, et_ca, et_land_name;

    /*enable crop image*/
    private static final int CAMERA_ACTION_PICK_REQUEST_CODE = 610;
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    public static final int CAMERA_STORAGE_REQUEST_CODE = 611;
    public static final int ONLY_CAMERA_REQUEST_CODE = 612;
    public static final int ONLY_STORAGE_REQUEST_CODE = 613;
    private String currentPhotoPath = "";
    private UiHelper uiHelper = new UiHelper();
    private String base64 = "";
    boolean isEditable = false;
    private ArrayList<String> soilTypeAL;
    private HashMap<String, Integer> soilTypeHM;
    private ArrayList<String> soilColorAL;
    private HashMap<String, Integer> soilColorHM;
    private ArrayList<String> soilCharacteristicsAL;
    private HashMap<String, Integer> soilCharacteristicsHM;
    private ArrayList<String> soilChemicalCompositionAL;
    private HashMap<String, Integer> soilChemicalCompositionHM;
    public static android.app.Dialog submit_alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.ADD_LAND) + "</font>"));

        initView();

        setUnitSpinner();
        setFarmerSpinner();
        setSoilTypeSpinner();
        setSoilColorSpinner();
        setSoilCharacteristicsSpinner();
        setSoilChemicalCompositionSpinner();

        String role_id = sharedPrefHelper.getString("role_id", "");

        if (role_id.equals("5")) {
            ll_farmer_selection.setVisibility(View.GONE);
        } else {
            ll_farmer_selection.setVisibility(View.VISIBLE);
        }

        //get intent values here
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            screen_type = bundle.getString("screen_type", "");
            land_area = bundle.getString("land_area", "");
            farmer_name = bundle.getString("farmer_name", "");
            unit = bundle.getString("unit", "");
            base64 = bundle.getString("land_image", "");
            land_id = bundle.getString("land_id", "");
            farmerId = Integer.parseInt(bundle.getString("farmer_id", ""));
            farmer_id = bundle.getString("farmer_id", "");
            unitId = bundle.getInt("unit_id", 0);
            soil_type_name = bundle.getString("soil_type_name", "");
            soil_color_name = bundle.getString("soil_color_name", "");
            soil_characteristics_name = bundle.getString("soil_characteristics_name", "");
            soil_chemical_composition_name = bundle.getString("soil_chemical_composition_name", "");
            p = bundle.getString("p", "");
            ca = bundle.getString("ca", "");
            mg = bundle.getString("mg", "");
            n = bundle.getString("n", "");
            k = bundle.getString("k", "");
            s = bundle.getString("s", "");
            land_name = bundle.getString("land_name", "");
            bulk_density = bundle.getString("bulk_density", "");
            ph = bundle.getString("ph", "");
            ec = bundle.getString("ec", "");
            filtration_rate = bundle.getString("filtration_rate", "");
            soil_texture = bundle.getString("soil_texture", "");
            cation_exchange_capacity = bundle.getString("cation_exchange_capacity", "");
        }

        if (screen_type.equalsIgnoreCase("edit_land")) {
            setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.Edit_Land) + "</font>"));
            setEditValues();
        }

        if (!sharedPrefHelper.getString("selected_farmer", "").equals("")) {
            spnfarmerSelection.setVisibility(View.GONE);
            tv_Farmer_name.setVisibility(View.VISIBLE);
            tv_Farmer_name.setText(sqliteHelper.getFarmerName(sharedPrefHelper.getString("selected_farmer", "")));
        } else {
            spnfarmerSelection.setVisibility(View.VISIBLE);
            tv_Farmer_name.setVisibility(View.GONE);
        }

        findViewById(R.id.btn_upload_land).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (uiHelper.checkSelfPermissions(this)) ;
            openCamera();
            // uiHelper.showImagePickerDialog(this, this);
        });


//        Bundle bundl = getIntent().getExtras();
//        if (bundl != null) {
//            id = bundl.getString("id", "");
//
//            if (!id.equals("")) {
//
//
//
//
//
//            }
        btn_submitDetls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    Random random = new Random();
                    int value = random.nextInt(1000);
                    if (role_id.equalsIgnoreCase("5")) { //role_id=5(farmer)
                        landHoldingPojo.setFarmer_id(sharedPrefHelper.getString("farmer_id", ""));

                    } else {
                        if (sharedPrefHelper.getString("selected_farmer", "").equals("")) {
                            landHoldingPojo.setFarmer_id(String.valueOf(farmerId));

                        } else {

                            landHoldingPojo.setFarmer_id(sharedPrefHelper.getString("selected_farmer", ""));
                        }
                    }
                    filtration_rate = et_filtration_rate.getText().toString();
                    soil_texture = et_soil_texture.getText().toString();
                    ph = et_ph.getText().toString();
                    bulk_density = et_bulk_density.getText().toString();
                    cation_exchange = et_cation_exchange_capacity.getText().toString();
                    ec = et_ec.getText().toString();
                    p = et_p.getText().toString();
                    s = et_s.getText().toString();
                    mg = et_mg.getText().toString();
                    k = et_k.getText().toString();
                    n = et_n.getText().toString();
                    ca = et_ca.getText().toString();
                    land_name = et_land_name.getText().toString();
                    landHoldingPojo.setLand_id(String.valueOf(value));
                    landHoldingPojo.setArea(et_landto_be_added.getText().toString().trim());
                    landHoldingPojo.setLand_unit(String.valueOf(unitId));
                    landHoldingPojo.setImage(base64);
                    landHoldingPojo.setUser_id(sharedPrefHelper.getString("user_id", ""));
                    landHoldingPojo.setTotal_plant("0");
                    landHoldingPojo.setSoil_type_id(String.valueOf(soilTypeId));
                    landHoldingPojo.setSoil_color_id(String.valueOf(soilColorId));
                    landHoldingPojo.setSoil_characteristics_id(String.valueOf(soilCharId));
                    landHoldingPojo.setSoil_chemical_composition_id(String.valueOf(soilChemicalId));
                    landHoldingPojo.setFiltration_rate(filtration_rate);
                    landHoldingPojo.setSoil_texture(soil_texture);
                    landHoldingPojo.setPh(ph);
                    landHoldingPojo.setBulk_density(bulk_density);
                    landHoldingPojo.setCation_exchange_capacity(cation_exchange);
                    landHoldingPojo.setEc(ec);
                    landHoldingPojo.setP(p);
                    landHoldingPojo.setS(s);
                    landHoldingPojo.setMg(mg);
                    landHoldingPojo.setK(k);
                    landHoldingPojo.setN(n);
                    landHoldingPojo.setCa(ca);
                    landHoldingPojo.setLand_name(land_name);


                    if (screen_type.equals("edit_land")) {
                        landHoldingPojo.setLand_id(land_id);
                        sqliteHelper.updateLandData(landHoldingPojo, land_id);
                    } else {
                        landHoldingPojo.setLand_id(String.valueOf(value));
                        landHoldingPojo.setId(CommonClass.getUUID());
                        long inserted_id = sqliteHelper.AddLandData(landHoldingPojo);
                    }

                    try {
                        /*if (isInternetOn()) {
                            landHoldingAL = sqliteHelper.getAddLandDataToBeSync();
                            if (landHoldingAL.size() > 0) {
                                for (int i = 0; i < landHoldingAL.size(); i++) {
                                    landHoldingAL.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));
                                    //send land_holding server-id while editing land
                                    landHoldingAL.get(i).setLand_id_AI(landHoldingAL.get(i).getId());

                                    Gson gson = new Gson();
                                    String data = gson.toJson(landHoldingAL.get(i));
                                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                    RequestBody body = RequestBody.create(JSON, data);
                                    if (screen_type.equals("edit_land")) {
                                        sendEditLandData(body, Integer.parseInt(landHoldingAL.get(i).getLocal_id()));
                                    } else {
                                        sendAddLandData(body, Integer.parseInt(landHoldingAL.get(i).getLocal_id()));
                                    }
                                }
                            }
                        } else {*/
//                            Toast.makeText(context, R.string.no_internet_data_saved_locally, Toast.LENGTH_LONG).show();
                        showSubmitDialog(context,"Data has been saved in local database successfully.");
//                        Intent addLandIntent = new Intent(context, LandHoldingActivity.class);
//                            startActivity(addLandIntent);
//                            finish();
                        /*}*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        setToggleButtonClick();
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
                Intent intent = new Intent(context, LandHoldingActivity.class);
                context.startActivity(intent);
            }
        });

        submit_alert.show();
        submit_alert.setCanceledOnTouchOutside(false);
    }


    private void setSoilChemicalCompositionSpinner() {
        soilChemicalCompositionAL.clear();
        SharedPrefHelper spf = new SharedPrefHelper(AddLandActivity.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            soilChemicalCompositionHM = sqliteHelper.getMasterSpinnerId(20, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            soilChemicalCompositionHM = sqliteHelper.getMasterSpinnerId(20, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            soilChemicalCompositionHM = sqliteHelper.getMasterSpinnerId(20, 4);
        }
        else
        {
            soilChemicalCompositionHM = sqliteHelper.getMasterSpinnerId(20, 1);
        }

        for (int i = 0; i < soilChemicalCompositionHM.size(); i++) {
            soilChemicalCompositionAL.add(soilChemicalCompositionHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
        } else {
            soilChemicalCompositionAL.add(0, getString(R.string.select_soil_chemical_composition));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, soilChemicalCompositionAL);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_soil_chemical_composition.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = soil_chemical_composition_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = soil_chemical_composition_name;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_soil_chemical_composition.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_soil_chemical_composition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_soil_chemical_composition.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_soil_chemical_composition))) {
                    if (spn_soil_chemical_composition.getSelectedItem().toString().trim() != null) {
                        soilChemicalId = soilChemicalCompositionHM.get(spn_soil_chemical_composition.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSoilCharacteristicsSpinner() {
        soilCharacteristicsAL.clear();
        SharedPrefHelper spf = new SharedPrefHelper(AddLandActivity.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            soilCharacteristicsHM = sqliteHelper.getMasterSpinnerId(19, 2);
        }
        else  if(language.equalsIgnoreCase("kan"))
        {
            soilCharacteristicsHM = sqliteHelper.getMasterSpinnerId(19, 3);
        }
        else  if(language.equalsIgnoreCase("guj"))
        {
            soilCharacteristicsHM = sqliteHelper.getMasterSpinnerId(19, 4);
        }
        else
        {
            soilCharacteristicsHM = sqliteHelper.getMasterSpinnerId(19, 1);
        }

        for (int i = 0; i < soilCharacteristicsHM.size(); i++) {
            soilCharacteristicsAL.add(soilCharacteristicsHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
        } else {
            soilCharacteristicsAL.add(0, getString(R.string.select_soil_characteristics));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, soilCharacteristicsAL);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_soil_characteristics.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = soil_characteristics_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = soil_characteristics_name;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_soil_characteristics.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_soil_characteristics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_soil_characteristics.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_soil_characteristics))) {
                    if (spn_soil_characteristics.getSelectedItem().toString().trim() != null) {
                        soilCharId = soilCharacteristicsHM.get(spn_soil_characteristics.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSoilColorSpinner() {
        soilColorAL.clear();
        SharedPrefHelper spf = new SharedPrefHelper(AddLandActivity.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            soilColorHM = sqliteHelper.getMasterSpinnerId(18, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            soilColorHM = sqliteHelper.getMasterSpinnerId(18, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            soilColorHM = sqliteHelper.getMasterSpinnerId(18, 4);
        }
        else
        {
            soilColorHM = sqliteHelper.getMasterSpinnerId(18, 1);
        }


        for (int i = 0; i < soilColorHM.size(); i++) {
            soilColorAL.add(soilColorHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
        } else {
            soilColorAL.add(0, getString(R.string.select_soil_color));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, soilColorAL);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_soil_color.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = soil_color_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = soil_color_name;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_soil_color.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_soil_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_soil_color.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_soil_color))) {
                    if (spn_soil_color.getSelectedItem().toString().trim() != null) {
                        soilColorId = soilColorHM.get(spn_soil_color.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setSoilTypeSpinner() {
        soilTypeAL.clear();
        SharedPrefHelper spf = new SharedPrefHelper(AddLandActivity.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            soilTypeHM = sqliteHelper.getMasterSpinnerId(17, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            soilTypeHM = sqliteHelper.getMasterSpinnerId(17, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            soilTypeHM = sqliteHelper.getMasterSpinnerId(17, 4);
        }
        else
        {
            soilTypeHM = sqliteHelper.getMasterSpinnerId(17, 1);
        }

        for (int i = 0; i < soilTypeHM.size(); i++) {
            soilTypeAL.add(soilTypeHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
        } else {
            soilTypeAL.add(0, getString(R.string.select_soil_type));
        }

        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, soilTypeAL);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_soil_type.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = soil_type_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = soil_type_name;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_soil_type.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_soil_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_soil_type.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_soil_type))) {
                    if (spn_soil_type.getSelectedItem().toString().trim() != null) {
                        soilTypeId = soilTypeHM.get(spn_soil_type.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setToggleButtonClick() {
        toggle_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    ll_land_soil_info.setVisibility(View.VISIBLE);
                } else {
                    // The toggle is disabled
                    ll_land_soil_info.setVisibility(View.GONE);
                }
            }
        });
    }

    private void sendEditLandData(RequestBody body, int local_id) {
        ProgressDialog dialog = ProgressDialog.show(context, "", getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).callEditLandApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String land_id = jsonObject.optString("land_id");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                        sqliteHelper.updateLocalFlag("land_holding", local_id, 1);
                        Intent addLandIntent = new Intent(context, LandHoldingActivity.class);
                        startActivity(addLandIntent);
                        finish();
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
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

    private void sendAddLandData(RequestBody body, int local_id) {
        ProgressDialog dialog = ProgressDialog.show(context, "", getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).AddLand(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String land_id_primarykey = jsonObject.optString("land_id_primarykey");
                    String land_id = jsonObject.optString("land_id");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                        sqliteHelper.updateServerid("land_holding", local_id, Integer.parseInt(land_id_primarykey));
                        sqliteHelper.updateLandId("land_holding", "land_id", land_id, local_id, "local_id");
                        sqliteHelper.updateLocalFlag("land_holding", local_id, 1);
                        Intent addLandIntent = new Intent(context, LandHoldingActivity.class);
                        startActivity(addLandIntent);
                        finish();
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.dismiss();

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

    private void setEditValues() {
        isEditable = true;
        if (base64 != null && base64.length() > 200) {
            byte[] decodedString = Base64.decode(base64, Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            img_addland.setImageBitmap(bitmap);
        } else if (base64.length() <= 200) {
            try {
                String url = APIClient.LAND_IMAGE_URL + base64;
                Picasso.with(context).load(url).placeholder(R.drawable.land).into(img_addland);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            img_addland.setImageResource(R.drawable.land);
        }
        et_landto_be_added.setText(land_area);
        et_p.setText(p);
        et_ca.setText(ca);
        et_s.setText(s);
        et_k.setText(k);
        et_mg.setText(mg);
        et_n.setText(n);
        et_ec.setText(ec);
        et_ph.setText(ph);
        et_bulk_density.setText(bulk_density);
        et_soil_texture.setText(soil_texture);
        et_cation_exchange_capacity.setText(cation_exchange_capacity);
        et_filtration_rate.setText(filtration_rate);
        et_land_name.setText(land_name);

        setFarmerSpinner();
        setUnitSpinner();
        setSoilTypeSpinner();
        setSoilColorSpinner();
        setSoilCharacteristicsSpinner();
        setSoilChemicalCompositionSpinner();
    }


    private void setFarmerSpinner() {
        farmarArrayList.clear();
        farmarNameHM = sqliteHelper.getFarmerspinner();

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
        SharedPrefHelper spf = new SharedPrefHelper(AddLandActivity.this);
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
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = unit;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = unit;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_Unit.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }else{
            spn_Unit.setSelection(3);
        }

        spn_Unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_Unit.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_unit))) {
                    if (spn_Unit.getSelectedItem().toString().trim() != null) {
                        unitId = UnitNameHM.get(spn_Unit.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView() {
        sharedPrefHelper = new SharedPrefHelper(this);
        UnitArrayList = new ArrayList<>();
        UnitNameHM = new HashMap<>();
        farmarArrayList = new ArrayList<>();
        farmarNameHM = new HashMap<>();
        btn_submitDetls = findViewById(R.id.btn_submitDetls);
        btn_upload_land = findViewById(R.id.btn_upload_land);
        img_addland = findViewById(R.id.img_addland);
        tv_Farmer_name = findViewById(R.id.tv_Farmer_name);
        et_landto_be_added = findViewById(R.id.et_landto_be_added);
        spnfarmerSelection = findViewById(R.id.spnfarmerSelection);
        ll_farmer_selection = findViewById(R.id.ll_farmer_selection);
        spn_Unit = findViewById(R.id.spn_Unit);
        landHoldingPojo = new LandHoldingPojo();
        sqliteHelper = new SqliteHelper(this);
        toggle_button = findViewById(R.id.toggle_button);
        ll_land_soil_info = findViewById(R.id.ll_land_soil_info);
        spn_soil_type = findViewById(R.id.spn_soil_type);
        spn_soil_color = findViewById(R.id.spn_soil_color);
        spn_soil_characteristics = findViewById(R.id.spn_soil_characteristics);
        spn_soil_chemical_composition = findViewById(R.id.spn_soil_chemical_composition);
        soilTypeAL = new ArrayList<>();
        soilTypeHM = new HashMap<>();
        soilColorAL = new ArrayList<>();
        soilColorHM = new HashMap<>();
        soilCharacteristicsAL = new ArrayList<>();
        soilCharacteristicsHM = new HashMap<>();
        soilChemicalCompositionAL = new ArrayList<>();
        soilChemicalCompositionHM = new HashMap<>();
        et_filtration_rate = findViewById(R.id.et_filtration);
        et_bulk_density = findViewById(R.id.et_bulkdensity);
        et_ph = findViewById(R.id.et_ph);
        et_cation_exchange_capacity = findViewById(R.id.et_cation);
        et_soil_texture = findViewById(R.id.et_soil);
        et_ec = findViewById(R.id.et_ec);
        et_p = findViewById(R.id.et_p);
        et_s = findViewById(R.id.et_s);
        et_mg = findViewById(R.id.et_mg);
        et_k = findViewById(R.id.et_k);
        et_n = findViewById(R.id.et_n);
        et_ca = findViewById(R.id.et_ca);
        et_land_name = findViewById(R.id.et_land_name);
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
            if (screen_type != null && !screen_type.equals("")) {
                super.onBackPressed();
                Intent intentLandHoldingActivity = new Intent(context, ViewLandActivity.class);
                intentLandHoldingActivity.putExtra("land_id_id",land_id);
                intentLandHoldingActivity.putExtra("farmer_id",farmer_id);
                startActivity(intentLandHoldingActivity);
                finish();
            } else {
                super.onBackPressed();
                Intent intentLandHoldingActivity = new Intent(context, LandHoldingActivity.class);
                startActivity(intentLandHoldingActivity);
                finish();
            }
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
            uiHelper.toast(this, getString(R.string.Please_select_another_image));
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
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
 //       String imagesFolder="images";
//        File storageDir = new File(
//                Environment.getExternalStorageDirectory(), imagesFolder
//        );
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
                uiHelper.toast(this, getString(R.string.ImageCropper3));
                finish();
            }
        } else if (requestCode == ONLY_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else {
                uiHelper.toast(this, getString(R.string.ImageCropper1));
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


    public Bitmap compressImage(String filePath) {

        //String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

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
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;


        Matrix scaleMatrix = new Matrix();
//        Matrix scaleMatrix = {0.9846154, 0.0, 4.4307556; 0.0, 0.9846154, 7.8769226][0.0, 0.0, 1.0]};

        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        String lat = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system
        try {

            lat = "Lat.: " + sharedPrefHelper.getString("LAT", "");
            lat += " Long.:" + sharedPrefHelper.getString("LONG", "");
        } catch (Exception e) {
            e.printStackTrace();
        }


        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        Paint tPaint = new Paint();
        tPaint.setTextSize(7);
        tPaint.setColor(Color.parseColor("#E51900"));
        tPaint.setStyle(Paint.Style.FILL);

        Paint mpaint = new Paint();
        mpaint.setColor(Color.parseColor("#40FFFFFF"));
        mpaint.setStyle(Paint.Style.FILL);
        if (ratioX < 1) {
            canvas.drawRect(0f, scaledBitmap.getHeight() - 65f, scaledBitmap.getWidth(), scaledBitmap.getHeight(), mpaint);
            canvas.drawText(dateTime, 25f, scaledBitmap.getHeight() - 35f, tPaint);
            canvas.drawText(lat, 25f, scaledBitmap.getHeight() - 10f, tPaint);
        } else {
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
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
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


    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.colorAccent));
        UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(500, 500)
                .withAspectRatio(5f, 5f)
                .start(this);
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
            bitmap = compressImage(imageUri.getPath());
            img_addland.setImageBitmap(bitmap); //set to show image

            bitmap = getResizedBitmap(bitmap, 300); //get resize image
            base64 = getStringImage(bitmap); //get base 64 image file.
            Log.e("image", base64);
        } catch (Exception e) {
            uiHelper.toast(this, getString(R.string.profile_picture));
        }
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


    @Override
    public void onBackPressed() {



            if (screen_type != null && !screen_type.equals("")) {
                super.onBackPressed();
                Intent intentLandHoldingActivity = new Intent(context, ViewLandActivity.class);
                intentLandHoldingActivity.putExtra("land_id_id",land_id);
                intentLandHoldingActivity.putExtra("farmer_id",farmer_id);
                startActivity(intentLandHoldingActivity);
                finish();
            } else {
                super.onBackPressed();
                Intent intentLandHoldingActivity = new Intent(context, LandHoldingActivity.class);
                startActivity(intentLandHoldingActivity);
                finish();
            }

    }
}




