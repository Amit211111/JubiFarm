package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sanket.jubifarm.Activity.SyncDataActivity;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.Livelihood.Model.ParyavaranSakhiRegistrationPojo;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PS_Synchronize extends AppCompatActivity {

    @BindView(R.id.btnFarmerRegistration)
    android.widget.TextView btnFarmerRegistration;
    @BindView(R.id.btnLandHolding)
    android.widget.TextView btnLandHolding;
    @BindView(R.id.btnNeemPlantation)
    android.widget.TextView btnNeemPlantation;
    @BindView(R.id.btnNeemMonitoring)
    android.widget.TextView btnNeemMonitoring;
    @BindView(R.id.llFarmerRegistration)
    LinearLayout llFarmerRegistration;
    @BindView(R.id.llLandHolding)
    LinearLayout llLandHolding;
    @BindView(R.id.llNeemPlanning)
    LinearLayout llNeemPlanning;
    @BindView(R.id.llNeemMonitoring)
    LinearLayout llNeemMonitoring;
    @BindView(R.id.tvFarmerRegistrationCount)
    android.widget.TextView tvFarmerRegistrationCount;
    @BindView(R.id.tvLandHoldingCount)
    android.widget.TextView tvLandHoldingCount;
    @BindView(R.id.tvNeemPlanningCount)
    android.widget.TextView tvNeemPlanningCount;
    @BindView(R.id.tvNeemMonitoringCount)
    TextView tvNeemMonitoringCount;

    private Context context=this;
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sharedPrefHelper;
    private ArrayList<ParyavaranSakhiRegistrationPojo> paryavaranSakhiRegistrationPojoArrayList=new ArrayList<>();
    private ArrayList<PSLandHoldingPojo> landHoldingAL=new ArrayList<>();
    private ArrayList<PSNeemPlantationPojo> psNeemPlantationPojoArrayList=new ArrayList<>();
    //private ArrayList<PlantGrowthPojo> PlantGrowthModalList=new ArrayList<>();
    private String farmer_id="";
    private int countRegistration=0, countLandHolding=0,
            countProductDetails=0,
            countNeemPlant=0, cropTotalPlantPlant=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps_synchronize);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.SYNC_DATA) + "</font>"));

        initViews();
    }

    private void setDataCount() {
        paryavaranSakhiRegistrationPojoArrayList=sqliteHelper.getPSFarmerForSyn();
        countRegistration=paryavaranSakhiRegistrationPojoArrayList.size();
        if (countRegistration>0) {
            tvFarmerRegistrationCount.setText(countRegistration+"");
        }
//        landHoldingAL=sqliteHelper.getPSLandSyn();
//        countLandHolding=landHoldingAL.size();
//        if (countLandHolding>0) {
//            tvLandHoldingCount.setText(countLandHolding+"");
//        }

//        cropPlanningsAL=sqliteHelper.getAddPlantDataToBeSync();
//        countCropPlant=cropPlanningsAL.size();
//        if (countCropPlant>0) {
//            tvCropPlanningCount.setText(countCropPlant+"");
//        }
//        PlantGrowthModalList = sqliteHelper.getPlantgrwthListForSync();
//        countPlantGrowth=PlantGrowthModalList.size();
//        if (countPlantGrowth>0) {
//            tvCropMonitoringCount.setText(countPlantGrowth+"");
//        }
    }
    private void initViews() {
        sqliteHelper=new SqliteHelper(this);
        sharedPrefHelper=new SharedPrefHelper(this);
    }

    @OnClick({R.id.llFarmerRegistration,
            R.id.llLandHolding,
            R.id.llNeemPlanning,
            R.id.llNeemMonitoring})
    void onClick(View v){
        switch (v.getId()) {
            case R.id.llFarmerRegistration:
                sendFarmerRegistrationDataOnServer();
                break;
//            case R.id.llLandHolding:
//                sendLandHoldingDataOnServer();
//                break;
//            case R.id.llNeemPlanning:
//                sendNeemPlanningDataOnServer();
//                break;
//            case R.id.llNeemMonitoring:
//                sendNeemMonitoringDataOnServer();
//                break;
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
                paryavaranSakhiRegistrationPojoArrayList=sqliteHelper.getPSFarmerForSyn();
                countRegistration=paryavaranSakhiRegistrationPojoArrayList.size();
                if (countRegistration > 0) {
                    for (int i = 0; i < paryavaranSakhiRegistrationPojoArrayList.size(); i++) {

                        Gson gson = new Gson();
                        String data = gson.toJson(paryavaranSakhiRegistrationPojoArrayList.get(i));
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(JSON, data);
//                        if (farmerRegistrationPojoArrayList.get(i).getOffline_sync() == 1 && farmerRegistrationPojoArrayList.get(i).getFlag().equals("1")) {
//                            sendEditFramerRegistrationData(body, userId);
//                        } else {
                            sendFramerRegistrationData(body, paryavaranSakhiRegistrationPojoArrayList.get(i).getLocal_id());
//                        }
                    }
                }
                else {
                    Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(PS_Synchronize.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void sendEditFramerRegistrationData(RequestBody body, String userId) {
//        ProgressDialog dialog = ProgressDialog.show(SyncDataActivity.this, "", getString(R.string.Please_wait), true);
//        APIClient.getClient().create(JubiForm_API.class).callEditFarmerRegistrationApi(body).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
//                    dialog.dismiss();
//                    Log.e("bchjc", "vxghs " + jsonObject.toString());
//                    String status = jsonObject.optString("status");
//                    String message = jsonObject.optString("message");
//                    String user_id = jsonObject.optString("user_id");
//                    if (status.equalsIgnoreCase("1")) {
//                        /*sqliteHelper.updateFlag("users", Integer.parseInt(userId), 1);
//                        sqliteHelper.updateFlag("farmer_registration", Integer.parseInt(userId), 1);
//                        sqliteHelper.updateFlag("farmer_family", Integer.parseInt(userId), 1);
//                        sqliteHelper.updateFlag("crop_vegetable_details", Integer.parseInt(userId), 1);*/
//                        sqliteHelper.updateEditFlagInTable("users");
//                        sqliteHelper.updateEditFlagInTable("farmer_registration");
//                        sqliteHelper.updateEditFlagInTable("farmer_family");
//                        sqliteHelper.updateEditFlagInTable("crop_vegetable_details");
//
//                        if(countRegistration>0){
//                            countRegistration=countRegistration-1;
//                            tvFarmerRegistrationCount.setText(countRegistration+"");
//                            dialog.dismiss();
//                        }
//                        Toast.makeText(SyncDataActivity.this, "" + message, Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(SyncDataActivity.this, "" + message, Toast.LENGTH_LONG).show();
//                        dialog.dismiss();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    dialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                dialog.dismiss();
//            }
//        });
//    }

    private void sendFramerRegistrationData(RequestBody body, String localId) {
        ProgressDialog dialog = ProgressDialog.show(PS_Synchronize.this, "", getString(R.string.Please_wait), true);
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
                    Log.e("TAG", "StatusFarmer: "+status);
                    if (status.equalsIgnoreCase("1")) {
                        //update flag in tables
//                        sqliteHelper.updateId("users", "user_id", Integer.parseInt(user_id), Integer.parseInt(localId), "id");
//                        sqliteHelper.updateId("users", "id", Integer.parseInt(user_id), Integer.parseInt(user_id), "user_id");
//                        sqliteHelper.updateId("farmer_registration", "user_id", Integer.parseInt(user_id), Integer.parseInt(localId), "user_id");
//                        sqliteHelper.updateId("farmer_registration", "id", Integer.parseInt(farmer_id), Integer.parseInt(localId), "id");
//                        sqliteHelper.updateId("farmer_registration", "household_no", Integer.parseInt(household_no), Integer.parseInt(user_id), "user_id");
//                        sqliteHelper.updateId("farmer_family", "user_id", Integer.parseInt(user_id), Integer.parseInt(localId), "user_id");
//                        sqliteHelper.updateId("crop_vegetable_details", "user_id", Integer.parseInt(user_id), Integer.parseInt(localId), "user_id");
//                        sqliteHelper.updateId("land_holding", "farmer_id", Integer.parseInt(farmer_id), Integer.parseInt(localId), "farmer_id");
//                        sqliteHelper.updateId("crop_planning", "farmer_id", Integer.parseInt(farmer_id), Integer.parseInt(localId), "farmer_id");
//                        sqliteHelper.updateId("sale_details", "farmer_id", Integer.parseInt(farmer_id), Integer.parseInt(localId), "farmer_id");
//                        sqliteHelper.updateId("production_details", "farmer_id", Integer.parseInt(farmer_id), Integer.parseInt(localId), "farmer_id");
//                        sqliteHelper.updateId("plant_growth", "farmer_id", Integer.parseInt(farmer_id), Integer.parseInt(localId), "farmer_id");
//                        //int idss = sqliteHelper.getLastInsertedLocalID();
//                        sqliteHelper.updateFlag("users", Integer.parseInt(user_id), 1);
//                        sqliteHelper.updateFlag("farmer_registration", Integer.parseInt(user_id), 1);
//                       // sqliteHelper.updateFlag("farmer_family", Integer.parseInt(user_id), 1);
                        //sqliteHelper.updateFlag("crop_vegetable_details", Integer.parseInt(user_id), 1);

                        if(countRegistration>0){
                            countRegistration=countRegistration-1;
                            tvFarmerRegistrationCount.setText(countRegistration+"");
                            dialog.dismiss();
                        }

                        Toast.makeText(PS_Synchronize.this, "" + message, Toast.LENGTH_SHORT).show();
                    }else if(status.equals("2")){
                        //int idss = sqliteHelper.getLastInsertedLocalID();
                        /*sqliteHelper.dropTableFamily("users", user_id);
                        sqliteHelper.dropTableFamily("farmer_registration", user_id);
                        sqliteHelper.dropTableFamily("farmer_family", user_id);
                        sqliteHelper.dropTableFamily("crop_vegetable_details", user_id);*/
//                        sqliteHelper.updateEditFlagInTable("users");
//                        sqliteHelper.updateEditFlagInTable("farmer_registration");
//                        sqliteHelper.updateEditFlagInTable("farmer_family");
//                        sqliteHelper.updateEditFlagInTable("crop_vegetable_details");
                        if(countRegistration>0){
                            countRegistration=countRegistration-1;
                            tvFarmerRegistrationCount.setText(countRegistration+"");
                            dialog.dismiss();
                        }
                        //Toast.makeText(SyncDataActivity.this, R.string.your_number_already_exist, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PS_Synchronize.this, "" + message, Toast.LENGTH_LONG).show();
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

//    private void sendLandHoldingDataOnServer() {
//        try {
//            if (CommonClass.isInternetOn(context)) {
//                paryavaranSakhiRegistrationPojoArrayList=sqliteHelper.getPSFarmerForSyn();
//                if (paryavaranSakhiRegistrationPojoArrayList.size()==0) {
//                    landHoldingAL = sqliteHelper.getPSLandSyn();
//                    countLandHolding = landHoldingAL.size();
//                    if (countLandHolding > 0) {
//                        for (int i = 0; i < landHoldingAL.size(); i++) {
////                            landHoldingAL.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));
////                            //send land_holding server-id while editing land
////                            landHoldingAL.get(i).setLand_id_AI(landHoldingAL.get(i).getId());
//////                            cropTotalPlantPlant= Integer.parseInt(sqliteHelper.getTotalPlantbyid(landHoldingAL.get(i).getId()));;
//////
////                            landHoldingAL.get(i).setTotal_plant(String.valueOf(cropTotalPlantPlant));
//
//
//                            Gson gson = new Gson();
//                            String data = gson.toJson(landHoldingAL.get(i));
//                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//                            RequestBody body = RequestBody.create(JSON, data);
//
////                            if (landHoldingAL.get(i).getOffline_sync() == 1) {
////                                sendEditLandData(body, Integer.parseInt(landHoldingAL.get(i).getLocal_id()));
////                            } else {
////                                sendAddLandData(body, Integer.parseInt(landHoldingAL.get(i).getLocal_id()),
////                                        landHoldingAL.get(i).getId());
////                            }
//                        }
//                    }
//                    else {
//                        Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
//                    }
//                }
//                else {
//                    Toast.makeText(context, R.string.sync_farmer_registration_data, Toast.LENGTH_LONG).show();
//                }
//            }
//            else {
//                Toast.makeText(PS_Synchronize.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void sendEditLandData(RequestBody body, int local_id) {
//        ProgressDialog dialog = ProgressDialog.show(context, "", getString(R.string.Please_wait), true);
//        APIClient.getClient().create(JubiForm_API.class).callEditLandApi(body).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
//                    dialog.dismiss();
//                    Log.e("bchjc", "vxghs " + jsonObject.toString());
//                    String status = jsonObject.optString("status");
//                    String message = jsonObject.optString("message");
//                    String land_id = jsonObject.optString("land_id");
//                    if (status.equalsIgnoreCase("1")) {
//                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
//                        sqliteHelper.updateLocalFlag("land_holding", local_id, 1);
//                        if (countLandHolding>0){
//                            countLandHolding=countLandHolding-1;
//                            tvLandHoldingCount.setText(countLandHolding+"");
//                        }
//
//                    } else {
//                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
//                        dialog.dismiss();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//            }
//        });
//    }
//
//    private void sendAddLandData(RequestBody body, int local_id, String id) {
//        ProgressDialog dialog = ProgressDialog.show(context, "", getString(R.string.Please_wait), true);
//        APIClient.getClient().create(JubiForm_API.class).AddLand(body).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
//                    dialog.dismiss();
//                    Log.e("bchjc", "vxghs " + jsonObject.toString());
//                    String status = jsonObject.optString("status");
//                    String message = jsonObject.optString("message");
//                    String land_id_primarykey = jsonObject.optString("land_id_primarykey");
//                    String land_id = jsonObject.optString("land_id");
//                    if (status.equalsIgnoreCase("1")) {
//                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
//                        sqliteHelper.updateServerid("land_holding", local_id, Integer.parseInt(land_id_primarykey));
//                        sqliteHelper.updateLandId("land_holding", "land_id", land_id, local_id, "local_id");
//                        sqliteHelper.updateLocalFlag("land_holding", local_id, 1);
//                        sqliteHelper.updateLandServeridInCropPlanning("crop_planning", id, Integer.parseInt(land_id_primarykey));
//                        if (countLandHolding>0){
//                            countLandHolding=countLandHolding-1;
//                            tvLandHoldingCount.setText(countLandHolding+"");
//                        }
//                    } else {
//                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
//                        dialog.dismiss();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    dialog.dismiss();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//            }
//        });
//    }
//    private void sendNeemPlanningDataOnServer() {
//        try {
//            if (CommonClass.isInternetOn(context)) {
//                landHoldingAL=sqliteHelper.getAddLandDataToBeSync();
//                if (landHoldingAL.size()==0) {
//                    cropPlanningsAL = sqliteHelper.getAddPlantDataToBeSync();
//                    countCropPlant = cropPlanningsAL.size();
//                    if (countCropPlant > 0) {
//                        for (int i = 0; i < cropPlanningsAL.size(); i++) {
//                            cropPlanningsAL.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));
//
//                            Gson gson = new Gson();
//                            String data = gson.toJson(cropPlanningsAL.get(i));
//                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//                            RequestBody body = RequestBody.create(JSON, data);
//
//                            sendAddPlantData(body, Integer.parseInt(cropPlanningsAL.get(i).getLocal_id()),
//                                    cropPlanningsAL.get(i).getId());
//                        }
//                    } else {
//                        Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
//                    }
//                }
//                else {
//                    Toast.makeText(context, R.string.sync_land_holding, Toast.LENGTH_LONG).show();
//                }
//            }
//            else {
//                Toast.makeText(PS_Synchronize.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private void sendAddPlantData(RequestBody body, int local_id, String id) {
//        ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.Please_wait), true);
//        APIClient.getClient().create(JubiForm_API.class).add_plant(body).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
//                    dialog.dismiss();
//                    Log.e("bchjc", "vxghs " + jsonObject.toString());
//                    String status = jsonObject.optString("status");
//                    String message = jsonObject.optString("message");
//                    if (status.equalsIgnoreCase("1")) {
//                        sqliteHelper.updateAddPlantFlag("crop_planning",local_id,1);
//                        String plant_id = jsonObject.optString("plant_id_AI");
//                        String plant_ids = jsonObject.optString("plant_id");
//                        sqliteHelper.updateAddPlantID("crop_planning",local_id, plant_ids);
//                        sqliteHelper.updateServerid("crop_planning",local_id, Integer.parseInt(plant_id));
//                        sqliteHelper.updateLandServeridInPlantGrowth("plant_growth", id, Integer.parseInt(plant_id));
//                        if (countCropPlant>0){
//                            countCropPlant=countCropPlant-1;
//                            tvCropPlanningCount.setText(countCropPlant+"");
//                        }
//                    }
//                    else {
//                        Toast.makeText(PS_Synchronize.this, "" + message, Toast.LENGTH_LONG).show();
//                        dialog.dismiss();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//            }
//        });
//    }
//
//    private void sendNeemMonitoringDataOnServer() {
//        try {
//            if (CommonClass.isInternetOn(context)) {
//                cropPlanningsAL = sqliteHelper.getAddPlantDataToBeSync();
//                if (cropPlanningsAL.size()==0) {
//                    PlantGrowthModalList = sqliteHelper.getPlantgrwthListForSync();
//                    if (PlantGrowthModalList.size() > 0) {
//
//                        Gson gsonn = new Gson();
//                        String element = gsonn.toJson(
//                                PlantGrowthModalList,
//                                new TypeToken<ArrayList<VisitPlantModel>>() {
//                                }.getType());
//                        JSONArray jsonArray = null;
//                        try {
//                            jsonArray = new JSONArray(element);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        JSONObject jsonobj = new JSONObject();
//                        try {
//                            jsonobj.put("plant_growth", jsonArray);
//
//                            Gson gson = new Gson();
//                            String data = jsonobj.toString(); //gson.toJson(jsonobj);
//                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//                            RequestBody body = RequestBody.create(JSON, data);
//                            send_growth_plantData(body);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
//                    }
//                }
//                else {
//                    Toast.makeText(context, R.string.sync_crop_planning_data, Toast.LENGTH_LONG).show();
//                }
//            }
//            else {
//                Toast.makeText(PS_Synchronize.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
    @Override
    protected void onResume() {
        super.onResume();
        setDataCount();
    }


}