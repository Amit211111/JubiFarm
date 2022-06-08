package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sanket.jubifarm.Activity.SyncDataActivity;
import com.sanket.jubifarm.Livelihood.Model.Neem_Monitoring_Pojo;
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
    private ArrayList<PSNeemPlantationPojo> psNeemAL=new ArrayList<>();
    private ArrayList<Neem_Monitoring_Pojo> neem_monitoring_pojoArrayList=new ArrayList<>();
    private String farmer_id="";
    private int countRegistration=0, countLandHolding=0,
            countNeemMonitoring=0,
            countNeemPlant=0, cropTotalPlantPlant=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps_synchronize);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.SYNC_DATA) + "</font>"));

        initViews();
    }

    private void setDataCount() {
        paryavaranSakhiRegistrationPojoArrayList=sqliteHelper.getPSFarmerForSyn();
        countRegistration=paryavaranSakhiRegistrationPojoArrayList.size();
        if (countRegistration>0) {
            tvFarmerRegistrationCount.setText(countRegistration+"");
        }
        landHoldingAL=sqliteHelper.getPSLandSyn();
        countLandHolding=landHoldingAL.size();
        if (countLandHolding>0) {
            tvLandHoldingCount.setText(countLandHolding+"");
        }

        psNeemAL=sqliteHelper.getPSPlantSyn();
        countNeemPlant=psNeemAL.size();
        if (countNeemPlant>0) {
            tvNeemPlanningCount.setText(countNeemPlant+"");
        }
        neem_monitoring_pojoArrayList = sqliteHelper.getPSNeemMonitoringForSync();
        countNeemMonitoring=neem_monitoring_pojoArrayList.size();
        if (countNeemMonitoring>0) {
            tvNeemMonitoringCount.setText(countNeemMonitoring+"");
        }
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
            case R.id.llLandHolding:
                sendLandHoldingDataOnServer();
                break;
            case R.id.llNeemPlanning:
                sendNeemDataOnServer();
                break;
            case R.id.llNeemMonitoring:
                sendNeemMonitoringDataOnServer();
                break;
        }
    }

    private void sendFarmerRegistrationDataOnServer() {
        try {
            //in users table last inserted id is user_id
            int ids = sqliteHelper.getLastInsertedLocalID();

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
        APIClient.getPsClient().create(JubiForm_API.class).sendPSFarmerRegistrationdata(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
//                    String user_id = jsonObject.optString("user_id");
//                    String household_no = jsonObject.optString("household_no");
                     sharedPrefHelper.setString("selected_farmer","");
                    //sharedPrefHelper.setString("user_id", user_id);
                    Log.e("TAG", "StatusFarmer: "+status);
                    if (status.equalsIgnoreCase("1") ) {
                        //update flag in tables
                        String last_farmer_id = jsonObject.optString("last_farmer_id");

                        sqliteHelper.updateId("ps_farmer_registration", "id", Integer.parseInt(last_farmer_id), Integer.parseInt(localId), "local_id");
                        sqliteHelper.updateId("ps_land_holding", "farmer_id", Integer.parseInt(last_farmer_id), Integer.parseInt(localId), "local_id");
                        sqliteHelper.updatePSFlag("ps_farmer_registration", Integer.parseInt(localId), 1,"local_id");
//                       // sqliteHelper.updateFlag("farmer_family", Integer.parseInt(user_id), 1);
                        //sqliteHelper.updateFlag("crop_vegetable_details", Integer.parseInt(user_id), 1);

                        if(countRegistration>0){
                            countRegistration=countRegistration-1;
                            tvFarmerRegistrationCount.setText(countRegistration+"");
                            dialog.dismiss();
                        }

                        Toast.makeText(PS_Synchronize.this, "" + message, Toast.LENGTH_SHORT).show();
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
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    private void sendLandHoldingDataOnServer() {
        try {
            if (CommonClass.isInternetOn(context)) {
                paryavaranSakhiRegistrationPojoArrayList = sqliteHelper.getPSFarmerForSyn();
                if (paryavaranSakhiRegistrationPojoArrayList.size() == 0) {
                    landHoldingAL = sqliteHelper.getPSLandSyn();
                    countLandHolding = landHoldingAL.size();
                    if (countLandHolding > 0) {
                        for (int i = 0; i < landHoldingAL.size(); i++) {
                            Gson gson = new Gson();
                            String data = gson.toJson(landHoldingAL.get(i));
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(JSON, data);

                            sendAddLandData(body, Integer.parseInt(landHoldingAL.get(i).getLocal_id()));
                    }
                } else {
                    Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, R.string.sync_farmer_registration_data, Toast.LENGTH_LONG).show();
            }
//
        }else {
                Toast.makeText(PS_Synchronize.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show(); }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


private void sendNeemDataOnServer() {
        try {
            if (CommonClass.isInternetOn(context)) {
                landHoldingAL = sqliteHelper.getPSLandSyn();
                if (landHoldingAL.size() == 0) {
                    psNeemAL = sqliteHelper.getPSPlantSyn();
                    countNeemPlant = psNeemAL.size();
                    if (countNeemPlant > 0) {
                        for (int i = 0; i < psNeemAL.size(); i++) {
                            Gson gson = new Gson();
                            String data = gson.toJson(psNeemAL.get(i));
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(JSON, data);

                            sendAddPlantData(body, Integer.parseInt(psNeemAL.get(i).getLocal_id()));
                        }
                    } else {
                        Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, R.string.sync_land_holding, Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(PS_Synchronize.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
}

    private void sendAddLandData(RequestBody body, int local_id) {
        ProgressDialog dialog = ProgressDialog.show(context, "", getString(R.string.Please_wait), true);
        APIClient.getPsClient().create(JubiForm_API.class).PSAddLand(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("land_Data", "landData " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String last_land_id = jsonObject.optString("last_land_id");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                        sqliteHelper.updateId("ps_land_holding", "id", Integer.parseInt(last_land_id), local_id, "local_id");
                        sqliteHelper.updatePSFlag("ps_land_holding",local_id, 1,"local_id");
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

    private void sendAddPlantData(RequestBody body, int local_id) {
        ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.Please_wait), true);
        APIClient.getPsClient().create(JubiForm_API.class).add_neem_plant(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("Neem_Plant", "NeemPlant " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String last_neem_id = jsonObject.optString("last_neem_id");
                    if (status.equalsIgnoreCase("1")) {
                        sqliteHelper.updateId("add_neem_plant", "id", Integer.parseInt(last_neem_id), local_id, "local_id");
                        sqliteHelper.updateId("neem_monitoring", "neem_id", Integer.parseInt(last_neem_id), local_id, "local_id");
                        sqliteHelper.updatePSFlag("add_neem_plant",local_id, 1,"local_id");
                        if (countNeemPlant>0) {
                            countNeemPlant=countNeemPlant-1;
                            tvNeemPlanningCount.setText(countNeemPlant+"");
                        }
                    }
                    else {
                        Toast.makeText(PS_Synchronize.this, "" + message, Toast.LENGTH_LONG).show();
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
//
private void sendNeemMonitoringDataOnServer() {
    try {
        if (CommonClass.isInternetOn(context)) {
            psNeemAL = sqliteHelper.getPSNeemPlantationDataToBeSync();
            if (psNeemAL.size() == 0) {
                neem_monitoring_pojoArrayList = sqliteHelper.getPSNeemMonitoringForSync();
                countNeemMonitoring = neem_monitoring_pojoArrayList.size();
                if (countNeemMonitoring > 0) {
                    for (int i = 0; i < neem_monitoring_pojoArrayList.size(); i++) {
                        // psNeemPlantationPojoArrayList.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));

                        Gson gson = new Gson();
                        String data = gson.toJson(neem_monitoring_pojoArrayList.get(i));
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(JSON, data);

                        sendNeemMonitoringData(body, Integer.parseInt(neem_monitoring_pojoArrayList.get(i).getLocal_id()));
                    }
                } else {
                    Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, R.string.syn_neem_monitoring_data, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(PS_Synchronize.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
        }
    } catch (Exception e){
        e.printStackTrace();
    }
}

    public void sendNeemMonitoringData(RequestBody body, int local_id) {
        ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.Please_wait), true);
        APIClient.getPsClient().create(JubiForm_API.class).ps_neem_monitoring(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("subp", "neem_monitoring===" + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String last_monitro_id = jsonObject.optString("last_monitro_id");
                    if (status.equals("1")) {
                        sqliteHelper.updateId("neem_monitoring", "id", Integer.parseInt(last_monitro_id), local_id, "local_id");
                        sqliteHelper.updatePSFlag("neem_monitoring",local_id, 1,"local_id");

                        Toast.makeText(context, "" +message, Toast.LENGTH_SHORT).show();
                        if (countNeemMonitoring>0){
                            countNeemMonitoring=countNeemMonitoring-1;
                            tvNeemMonitoringCount.setText(countNeemPlant+"");
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

    @Override
    protected void onResume() {
        super.onResume();
        setDataCount();
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
                Intent intent = new Intent(PS_Synchronize.this, ParyavaranSakhiHome.class);
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