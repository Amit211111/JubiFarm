package com.sanket.jubifarm.Activity;

import static android.os.Build.VERSION.SDK_INT;
import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;

import static com.sanket.jubifarm.data_base.SqliteHelper.DATABASE_NAME;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sanket.jubifarm.Modal.Attendance_Approval;
import com.sanket.jubifarm.Modal.CropPlaningPojo;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.Modal.LandHoldingPojo;
import com.sanket.jubifarm.Modal.PlantGrowthPojo;
import com.sanket.jubifarm.Modal.SaleDetailsPojo;
import com.sanket.jubifarm.Modal.VisitPlantModel;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.sanket.jubifarm.utils.CommonClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncDataActivity extends AppCompatActivity {
    @BindView(R.id.btnFarmerRegistration)
    TextView btnFarmerRegistration;
    @BindView(R.id.btnLandHolding)
    TextView btnLandHolding;
    @BindView(R.id.btnCropSaleProductionDetails)
    TextView btnCropSaleProductionDetails;
    @BindView(R.id.btnCropSaleSaleDetails)
    TextView btnCropSaleSaleDetails;
    @BindView(R.id.btnCropPlanning)
    TextView btnCropPlanning;
    @BindView(R.id.btnCropMonitoring)
    TextView btnCropMonitoring;
    @BindView(R.id.llFarmerRegistration)
    LinearLayout llFarmerRegistration;
    @BindView(R.id.llLandHolding)
    LinearLayout llLandHolding;
    @BindView(R.id.llCropSalePD)
    LinearLayout llCropSalePD;
    @BindView(R.id.llCropSaleSD)
    LinearLayout llCropSaleSD;
    @BindView(R.id.llCropPlanning)
    LinearLayout llCropPlanning;
    @BindView(R.id.llCropMonitoring)
    LinearLayout llCropMonitoring;
    @BindView(R.id.tvFarmerRegistrationCount)
    TextView tvFarmerRegistrationCount;
    @BindView(R.id.tvLandHoldingCount)
    TextView tvLandHoldingCount;
    @BindView(R.id.tvCropSaleCountPD)
    TextView tvCropSaleCountPD;
    @BindView(R.id.tvCropSaleCountSD)
    TextView tvCropSaleCountSD;
    @BindView(R.id.tvCropPlanningCount)
    TextView tvCropPlanningCount;
    @BindView(R.id.tvCropMonitoringCount)
    TextView tvCropMonitoringCount;

    /*normal views*/
    private Context context=this;
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sharedPrefHelper;
    private ArrayList<FarmerRegistrationPojo> farmerRegistrationPojoArrayList=new ArrayList<>();
    private ArrayList<LandHoldingPojo> landHoldingAL=new ArrayList<>();
    private ArrayList<CropPlaningPojo> cropPlaningPojoArrayList=new ArrayList<>();
    private ArrayList<CropPlaningPojo> cropPlanningsAL=new ArrayList<>();
    private ArrayList<PlantGrowthPojo> PlantGrowthModalList=new ArrayList<>();
    private ArrayList<SaleDetailsPojo> saleDetailsPojosAL=new ArrayList<>();
    private ArrayList<SaleDetailsPojo> saleDetailsPojosAL2=new ArrayList<>();
    private String farmer_id="";
    SQLiteToExcel sqliteToExcel;
    int count=1;
    String Currentdate = "";
    private int countRegistration=0, countLandHolding=0,
            countProductDetails=0, countSaleDetails=0,
            countCropPlant=0, countPlantGrowth=0, cropTotalPlantPlant=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_data);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.SYNC_DATA) + "</font>"));

        Date dt = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Currentdate = dateFormat.format(dt);
        initViews();
    }

    private void setDataCount() {
        farmerRegistrationPojoArrayList=sqliteHelper.getTableDataToBeSync();
        countRegistration=farmerRegistrationPojoArrayList.size();
        if (countRegistration>0) {
            tvFarmerRegistrationCount.setText(countRegistration+"");
        }
        landHoldingAL=sqliteHelper.getAddLandDataToBeSync();
        countLandHolding=landHoldingAL.size();
        if (countLandHolding>0) {
            tvLandHoldingCount.setText(countLandHolding+"");
        }
        saleDetailsPojosAL=sqliteHelper.getCropListForSync("1");
        countProductDetails=saleDetailsPojosAL.size();
        if (countProductDetails>0) {
            tvCropSaleCountPD.setText(countProductDetails+"");
        }
        saleDetailsPojosAL2=sqliteHelper.getCropListForSync("2");
        countSaleDetails=saleDetailsPojosAL2.size();
        if (countSaleDetails>0) {
            tvCropSaleCountSD.setText(countSaleDetails+"");
        }


        cropPlanningsAL=sqliteHelper.getAddPlantDataToBeSync();
        countCropPlant=cropPlanningsAL.size();
        if (countCropPlant>0) {
            tvCropPlanningCount.setText(countCropPlant+"");
        }
        PlantGrowthModalList = sqliteHelper.getPlantgrwthListForSync();
        countPlantGrowth=PlantGrowthModalList.size();
        if (countPlantGrowth>0) {
            tvCropMonitoringCount.setText(countPlantGrowth+"");
        }
    }

    private void initViews() {
        sqliteHelper=new SqliteHelper(this);
        sharedPrefHelper=new SharedPrefHelper(this);
    }

    @OnClick({R.id.llFarmerRegistration,
            R.id.llLandHolding,
            R.id.llCropSalePD,
            R.id.llCropSaleSD,
            R.id.llCropPlanning,
            R.id.llCropMonitoring})
    void onClick(View v){
        switch (v.getId()) {
            case R.id.llFarmerRegistration:
                sendFarmerRegistrationDataOnServer();
                break;
            case R.id.llLandHolding:
                sendLandHoldingDataOnServer();
                break;
            case R.id.llCropSalePD:
                sendCropSaleProductionDetailsDataOnServer();
                break;
            case R.id.llCropSaleSD:
                sendCropSaleSaleDetailsDataOnServer();
                break;
            case R.id.llCropPlanning:
                sendCropPlanningDataOnServer();
                break;
            case R.id.llCropMonitoring:
                sendCropMonitoringDataOnServer();
                break;
        }
    }

    private void sendFarmerRegistrationDataOnServer() {
        try {
            //in users table last inserted id is user_id
            int ids = sqliteHelper.getLastInsertedLocalID();
            //for farmer id
            Attendance_Approval attendance_approval=new Attendance_Approval();
            /*if (sharedPrefHelper.getString("user_type", "").equals("Farmer")) {
                attendance_approval = sqliteHelper.getFarmerDetails(sharedPrefHelper.getString("user_id", ""));
                farmer_id = attendance_approval.getId();
            } else {
                attendance_approval = sqliteHelper.getFarmerDetails(attendance_approval.getUser_id());
                farmer_id = attendance_approval.getId();
            }*/
            if (CommonClass.isInternetOn(context)) {
                farmerRegistrationPojoArrayList=sqliteHelper.getTableDataToBeSync();
                countRegistration=farmerRegistrationPojoArrayList.size();
                if (countRegistration > 0) {
                    for (int i = 0; i < farmerRegistrationPojoArrayList.size(); i++) {
                        farmerRegistrationPojoArrayList.get(i).setFarmer_family(sqliteHelper.getFarmerFamilyTableDataToBeSync(farmerRegistrationPojoArrayList.get(i).getId()));
                        farmerRegistrationPojoArrayList.get(i).setCrop_vegetable_details(sqliteHelper.getCropVegetableDataToBeSync(farmerRegistrationPojoArrayList.get(i).getId()));

                        String userId = "";
                        if (sharedPrefHelper.getString("user_type", "").equals("Farmer")) {
                            userId = sharedPrefHelper.getString("user_id", "");
                        } else {
                            userId = farmerRegistrationPojoArrayList.get(i).getUser_id();
                        }

                        farmerRegistrationPojoArrayList.get(i).setUser_id(sharedPrefHelper.getString("user_id", ""));
                        if (sharedPrefHelper.getString("user_type", "").equals("Farmer")){
                            attendance_approval = sqliteHelper.getFarmerDetails(sharedPrefHelper.getString("user_id", ""));
                            farmer_id = attendance_approval.getId();
                            farmerRegistrationPojoArrayList.get(i).setFarmer_id(farmer_id);
                        }else {
                            farmerRegistrationPojoArrayList.get(i).setFarmer_id(String.valueOf(farmerRegistrationPojoArrayList.get(i).getId()));
                        }
                        farmerRegistrationPojoArrayList.get(i).setRole_id(sharedPrefHelper.getString("role_id",""));

                        Gson gson = new Gson();
                        String data = gson.toJson(farmerRegistrationPojoArrayList.get(i));
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(JSON, data);
                        if (farmerRegistrationPojoArrayList.get(i).getOffline_sync() == 1 && farmerRegistrationPojoArrayList.get(i).getFlag().equals("1")) {
                            sendEditFramerRegistrationData(body, userId);
                        } else {
                            sendFramerRegistrationData(body, farmerRegistrationPojoArrayList.get(i).getUid());
                        }
                    }
                }
                else {
                    Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(SyncDataActivity.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEditFramerRegistrationData(RequestBody body, String userId) {
        ProgressDialog dialog = ProgressDialog.show(SyncDataActivity.this, "", getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).callEditFarmerRegistrationApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String user_id = jsonObject.optString("user_id");
                    if (status.equalsIgnoreCase("1")) {
                        /*sqliteHelper.updateFlag("users", Integer.parseInt(userId), 1);
                        sqliteHelper.updateFlag("farmer_registration", Integer.parseInt(userId), 1);
                        sqliteHelper.updateFlag("farmer_family", Integer.parseInt(userId), 1);
                        sqliteHelper.updateFlag("crop_vegetable_details", Integer.parseInt(userId), 1);*/
                        sqliteHelper.updateEditFlagInTable("users");
                        sqliteHelper.updateEditFlagInTable("farmer_registration");
                        sqliteHelper.updateEditFlagInTable("farmer_family");
                        sqliteHelper.updateEditFlagInTable("crop_vegetable_details");

                        if(countRegistration>0){
                            countRegistration=countRegistration-1;
                            tvFarmerRegistrationCount.setText(countRegistration+"");
                            dialog.dismiss();
                        }
                        Toast.makeText(SyncDataActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SyncDataActivity.this, "" + message, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void sendFramerRegistrationData(RequestBody body, String localId) {
        ProgressDialog dialog = ProgressDialog.show(SyncDataActivity.this, "", getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).sendFormerRegistrationData(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String user_id = jsonObject.optString("user_id");
                    String farmer_id = jsonObject.optString("farmer_id");
                    String household_no = jsonObject.optString("household_no");
                    sharedPrefHelper.setString("selected_farmer","");
                    //sharedPrefHelper.setString("user_id", user_id);

                    if (status.equalsIgnoreCase("1")) {
                        //update flag in tables
                        sqliteHelper.updateId("users", "user_id", Integer.parseInt(user_id), Integer.parseInt(localId), "id");
                        sqliteHelper.updateId("users", "id", Integer.parseInt(user_id), Integer.parseInt(user_id), "user_id");
                        sqliteHelper.updateId("farmer_registration", "user_id", Integer.parseInt(user_id), Integer.parseInt(localId), "user_id");
                        sqliteHelper.updateId("farmer_registration", "id", Integer.parseInt(farmer_id), Integer.parseInt(localId), "id");
                        sqliteHelper.updateId("farmer_registration", "household_no", Integer.parseInt(household_no), Integer.parseInt(user_id), "user_id");
                        sqliteHelper.updateId("farmer_family", "user_id", Integer.parseInt(user_id), Integer.parseInt(localId), "user_id");
                        sqliteHelper.updateId("farmer_family", "farmer_registration_id", Integer.parseInt(farmer_id), Integer.parseInt(localId), "farmer_registration_id");
                        sqliteHelper.updateId("crop_vegetable_details", "user_id", Integer.parseInt(user_id), Integer.parseInt(localId), "user_id");
                        sqliteHelper.updateId("crop_vegetable_details", "farmer_id", Integer.parseInt(farmer_id), Integer.parseInt(localId), "farmer_id");
                        sqliteHelper.updateId("land_holding", "farmer_id", Integer.parseInt(farmer_id), Integer.parseInt(localId), "farmer_id");
                        sqliteHelper.updateId("crop_planning", "farmer_id", Integer.parseInt(farmer_id), Integer.parseInt(localId), "farmer_id");
                        sqliteHelper.updateId("sale_details", "farmer_id", Integer.parseInt(farmer_id), Integer.parseInt(localId), "farmer_id");
                        sqliteHelper.updateId("production_details", "farmer_id", Integer.parseInt(farmer_id), Integer.parseInt(localId), "farmer_id");
                        sqliteHelper.updateId("plant_growth", "farmer_id", Integer.parseInt(farmer_id), Integer.parseInt(localId), "farmer_id");
                        //int idss = sqliteHelper.getLastInsertedLocalID();
                        sqliteHelper.updateFlag("users", Integer.parseInt(user_id), 1);
                        sqliteHelper.updateFlag("farmer_registration", Integer.parseInt(user_id), 1);
                        sqliteHelper.updateFlag("farmer_family", Integer.parseInt(user_id), 1);
                        sqliteHelper.updateFlag("crop_vegetable_details", Integer.parseInt(user_id), 1);

                        if(countRegistration>0){
                            countRegistration=countRegistration-1;
                            tvFarmerRegistrationCount.setText(countRegistration+"");
                            dialog.dismiss();
                        }

                        Toast.makeText(SyncDataActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }else if(status.equals("2")){
                        //int idss = sqliteHelper.getLastInsertedLocalID();
                        /*sqliteHelper.dropTableFamily("users", user_id);
                        sqliteHelper.dropTableFamily("farmer_registration", user_id);
                        sqliteHelper.dropTableFamily("farmer_family", user_id);
                        sqliteHelper.dropTableFamily("crop_vegetable_details", user_id);*/
                        sqliteHelper.updateEditFlagInTable("users");
                        sqliteHelper.updateEditFlagInTable("farmer_registration");
                        sqliteHelper.updateEditFlagInTable("farmer_family");
                        sqliteHelper.updateEditFlagInTable("crop_vegetable_details");
                        if(countRegistration>0){
                            countRegistration=countRegistration-1;
                            tvFarmerRegistrationCount.setText(countRegistration+"");
                            dialog.dismiss();
                        }
                        //Toast.makeText(SyncDataActivity.this, R.string.your_number_already_exist, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SyncDataActivity.this, "" + message, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void sendLandHoldingDataOnServer() {
        try {
            if (CommonClass.isInternetOn(context)) {
                farmerRegistrationPojoArrayList=sqliteHelper.getTableDataToBeSync();
                if (farmerRegistrationPojoArrayList.size()==0) {
                    landHoldingAL = sqliteHelper.getAddLandDataToBeSync();
                    countLandHolding = landHoldingAL.size();
                    if (countLandHolding > 0) {
                        for (int i = 0; i < landHoldingAL.size(); i++) {
                            landHoldingAL.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));
                            //send land_holding server-id while editing land
                            landHoldingAL.get(i).setLand_id_AI(landHoldingAL.get(i).getId());
//                            cropTotalPlantPlant= Integer.parseInt(sqliteHelper.getTotalPlantbyid(landHoldingAL.get(i).getId()));;
//
//                            landHoldingAL.get(i).setTotal_plant(String.valueOf(cropTotalPlantPlant));


                            Gson gson = new Gson();
                            String data = gson.toJson(landHoldingAL.get(i));
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(JSON, data);

                            if (landHoldingAL.get(i).getOffline_sync() == 1) {
                                sendEditLandData(body, Integer.parseInt(landHoldingAL.get(i).getLocal_id()));
                            } else {
                                sendAddLandData(body, Integer.parseInt(landHoldingAL.get(i).getLocal_id()),
                                        landHoldingAL.get(i).getId());
                            }
                        }
                    }
                    else {
                        Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(context, R.string.sync_farmer_registration_data, Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(SyncDataActivity.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        if (countLandHolding>0){
                            countLandHolding=countLandHolding-1;
                            tvLandHoldingCount.setText(countLandHolding+"");
                        }

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

    private void sendAddLandData(RequestBody body, int local_id, String id) {
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
                        sqliteHelper.updateLandServeridInCropPlanning("crop_planning", id, Integer.parseInt(land_id_primarykey));
                        if (countLandHolding>0){
                            countLandHolding=countLandHolding-1;
                            tvLandHoldingCount.setText(countLandHolding+"");
                        }
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

    private void sendCropSaleProductionDetailsDataOnServer() {
        try {
            if (CommonClass.isInternetOn(context)){
                /*landHoldingAL = sqliteHelper.getAddLandDataToBeSync();
                if (landHoldingAL.size()==0) {*/
                    String fromSalesDetials = "1";
                    saleDetailsPojosAL = sqliteHelper.getCropListForSync(fromSalesDetials);
                    countProductDetails = saleDetailsPojosAL.size();
                    if (countProductDetails > 0) {
                        for (int i = 0; i < saleDetailsPojosAL.size(); i++) {
                            saleDetailsPojosAL.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));
                            Gson gson = new Gson();
                            String data = gson.toJson(saleDetailsPojosAL.get(i));
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(JSON, data);

                            if (fromSalesDetials.equals("1")) {
                                sendCropDetailsData(body, Integer.parseInt(saleDetailsPojosAL.get(i).getLocal_id()), i);
                            }
                        }
                    }
                    else {
                        Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
                    }
                /*}
                else {
                    Toast.makeText(context, R.string.sync_land_holding, Toast.LENGTH_LONG).show();
                }*/
            }else {
                Toast.makeText(SyncDataActivity.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendCropDetailsData(RequestBody body, int local_id, int i) {
        ProgressDialog dialog = ProgressDialog.show(context, "", context.getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).add_production_details(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        sqliteHelper.updateAddPlantFlag("production_details", local_id, 1);
                        String plant_id = jsonObject.optString("production_id");
                        sqliteHelper.updateServerid("production_details", local_id, Integer.parseInt(plant_id));
                        if (i + 1 == saleDetailsPojosAL.size()) {
                        }
                        if (countProductDetails>0){
                            countProductDetails=countProductDetails-1;
                            tvCropSaleCountPD.setText(countProductDetails+"");
                        }
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

    private void sendCropSaleSaleDetailsDataOnServer() {
        try {
            if (CommonClass.isInternetOn(context)){
                /*landHoldingAL=sqliteHelper.getAddLandDataToBeSync();
                if (landHoldingAL.size()==0) {*/
                    String fromSalesDetials = "2";
                    saleDetailsPojosAL = sqliteHelper.getCropListForSync(fromSalesDetials);
                    countSaleDetails = saleDetailsPojosAL.size();
                    if (countSaleDetails > 0) {
                        for (int i = 0; i < saleDetailsPojosAL.size(); i++) {
                            saleDetailsPojosAL.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));
                            Gson gson = new Gson();
                            String data = gson.toJson(saleDetailsPojosAL.get(i));
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(JSON, data);
                            if (fromSalesDetials.equals("2")) {
                                sendCropSalesData(body, Integer.parseInt(saleDetailsPojosAL.get(i).getLocal_id()), i);
                            }
                        }
                    } else {
                        Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
                    }
                /*}
                else {
                    Toast.makeText(context, R.string.sync_land_holding, Toast.LENGTH_LONG).show();
                }*/
            }
            else {
                Toast.makeText(SyncDataActivity.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendCropSalesData(RequestBody body, int local_id, int i) {
        ProgressDialog dialog = ProgressDialog.show(context, "", context.getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).add_sale_details(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        sqliteHelper.updateAddPlantFlag("sale_details", local_id, 1);
                        String plant_id = jsonObject.optString("sale_details_id");
                        sqliteHelper.updateServerid("sale_details", local_id, Integer.parseInt(plant_id));
                        if (i + 1 == saleDetailsPojosAL.size()) {
                        }
                        if (countSaleDetails>0){
                            countSaleDetails=countSaleDetails-1;
                            tvCropSaleCountSD.setText(countSaleDetails+"");
                        }

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

    private void sendCropPlanningDataOnServer() {
        try {
            if (CommonClass.isInternetOn(context)) {
                landHoldingAL=sqliteHelper.getAddLandDataToBeSync();
                if (landHoldingAL.size()==0) {
                    cropPlanningsAL = sqliteHelper.getAddPlantDataToBeSync();
                    countCropPlant = cropPlanningsAL.size();
                    if (countCropPlant > 0) {
                        for (int i = 0; i < cropPlanningsAL.size(); i++) {
                            cropPlanningsAL.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));

                            Gson gson = new Gson();
                            String data = gson.toJson(cropPlanningsAL.get(i));
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(JSON, data);

                            sendAddPlantData(body, Integer.parseInt(cropPlanningsAL.get(i).getLocal_id()),
                                    cropPlanningsAL.get(i).getId());
                        }
                    } else {
                        Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(context, R.string.sync_land_holding, Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(SyncDataActivity.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendAddPlantData(RequestBody body, int local_id, String id) {
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
                        sqliteHelper.updateLandServeridInPlantGrowth("plant_growth", id, Integer.parseInt(plant_id));
                        if (countCropPlant>0){
                            countCropPlant=countCropPlant-1;
                            tvCropPlanningCount.setText(countCropPlant+"");
                        }
                    }
                    else {
                        Toast.makeText(SyncDataActivity.this, "" + message, Toast.LENGTH_LONG).show();
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

    private void sendCropMonitoringDataOnServer() {
        try {
            if (CommonClass.isInternetOn(context)) {
                cropPlanningsAL = sqliteHelper.getAddPlantDataToBeSync();
                if (cropPlanningsAL.size()==0) {
                    PlantGrowthModalList = sqliteHelper.getPlantgrwthListForSync();
                    if (PlantGrowthModalList.size() > 0) {

                        Gson gsonn = new Gson();
                        String element = gsonn.toJson(
                                PlantGrowthModalList,
                                new TypeToken<ArrayList<VisitPlantModel>>() {
                                }.getType());
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(element);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject jsonobj = new JSONObject();
                        try {
                            jsonobj.put("plant_growth", jsonArray);

                            Gson gson = new Gson();
                            String data = jsonobj.toString(); //gson.toJson(jsonobj);
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(JSON, data);
                            send_growth_plantData(body);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(context, R.string.sync_crop_planning_data, Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(SyncDataActivity.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void send_growth_plantData(RequestBody body) {
        ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).plant_growth(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("subp", "plant_growth===" + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    if (status.equals("1")) {
                        sqliteHelper.updateVisitPlantFlag("plant_growth", 0, 1);

                        if (countPlantGrowth>0){
                            countPlantGrowth=countPlantGrowth-1;
                            tvCropMonitoringCount.setText(countCropPlant+"");
                        }

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

    public static File getDBFile() {
        String DB_PATH_ALT="data/data/com.sanket.jubifarm/databases/"+DATABASE_NAME;
        File dbFile = new File(DB_PATH_ALT);

        return dbFile;
    }

    public void exportDb(View view) {
        if (hasManageExternalStoragePermission()){
            final File file = getDBFile();
            try {
                copyFileUsingStream(file, copyToSDcard(DATABASE_NAME));
                Toast.makeText(this, "DB Exported", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void AllFileExport() {
        ArrayList<String> columnsToExclude = new ArrayList<String>();
        columnsToExclude.add("local_id");
        //columnsToExclude.add("created_at");
        sqliteToExcel.setExcludeColumns(columnsToExclude);

        HashMap<String, String> prettyNameMapping = new HashMap<String, String>();
        prettyNameMapping.put("income_date", "Date");
        sqliteToExcel.setPrettyNameMapping(prettyNameMapping);
        String file_name = "farmer_registration"+ CommonClass.getUUID() + ".xls";
        sqliteToExcel.exportSingleTable("farmer_registration", file_name, new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }
            @Override
            public void onCompleted(String filePath) {
                sqliteHelper.dropTable("farmer_registration");
                count++;
                AllFileExport();
                Toast.makeText(getApplicationContext(),"Successfully Exported", Toast.LENGTH_LONG).show();

            }
            @Override
            public void onError(Exception e) {

            }
        });


    }

    public void exportExcel(View view) {
        String directory_path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // Export SQLite DB as EXCEL FILE
        sqliteToExcel = new SQLiteToExcel(getApplicationContext(), SqliteHelper.DATABASE_NAME, directory_path);
        if (count==1) {
            AllFileExport();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1201) {
            final File file = getDBFile();
            try {
                copyFileUsingStream(file, copyToSDcard(DATABASE_NAME));
                Toast.makeText(this, "DB Exported", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean hasManageExternalStoragePermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                return true;
            } else {
                if (Environment.isExternalStorageLegacy()) {
                    return true;
                }
                try {
                    Intent intent = new Intent();
                    intent.setAction(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:com.sanket.jubifarm"));
                    startActivityForResult(intent, 1201); //result code is just an int
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        if (SDK_INT >= Build.VERSION_CODES.Q) {
            if (Environment.isExternalStorageLegacy()) {
                return true;
            } else {
                try {
                    Intent intent = new Intent();
                    intent.setAction(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:com.sanket.jubifarm"));
                    startActivityForResult(intent, 1201); //result code is just an int
                    return false;
                } catch (Exception e) {
                    return true; //if anything needs adjusting it would be this
                }
            }
        }
        return true; // assumed storage permissions granted
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {

                os.write(buffer, 0, length);
            }
        } finally {
            if (is!=null)
                is.close();
            if (os!=null)
                os.close();
        }
    }
    public File copyToSDcard(String sFileName) {
        File root = new File(Environment.getExternalStorageDirectory(), "Db_Backup");
        if (!root.exists()) {
            root.mkdirs();
        } else {
            root.delete();
            File root2 = new File(Environment.getExternalStorageDirectory(), "Db_Backup");
            root2.mkdirs();
            Toast.makeText(this, "File Replaced", Toast.LENGTH_LONG).show();
        }
        File gpxfile = new File(root, sFileName);
        return gpxfile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDataCount();
    }
}
