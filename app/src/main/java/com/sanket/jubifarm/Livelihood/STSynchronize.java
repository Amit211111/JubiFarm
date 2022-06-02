package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Livelihood.Model.CandidatePojo;
import com.sanket.jubifarm.Livelihood.Model.MonitoringStatusPojo;
import com.sanket.jubifarm.Livelihood.Model.Neem_Monitoring_Pojo;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.Livelihood.Model.ParyavaranSakhiRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.sanket.jubifarm.utils.CommonClass;

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

public class STSynchronize extends AppCompatActivity {
    @BindView(R.id.btnFarmerRegistration)
    android.widget.TextView btnFarmerRegistration;
    @BindView(R.id.btnLandHolding)
    android.widget.TextView btnLandHolding;
    @BindView(R.id.llFarmerRegistration)
    LinearLayout llFarmerRegistration;
    @BindView(R.id.llLandHolding)
    LinearLayout llLandHolding;
    @BindView(R.id.tvFarmerRegistrationCount)
    android.widget.TextView tvFarmerRegistrationCount;
    @BindView(R.id.tvLandHoldingCount)
    android.widget.TextView tvLandHoldingCount;

    private Context context = this;
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sharedPrefHelper;
    private ArrayList<CandidatePojo> candidatePojoArrayList = new ArrayList<>();
    private ArrayList<MonitoringStatusPojo> monitoringStatusPojoArrayList = new ArrayList<>();
    private int countCandidate = 0, countMonitoring = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stsynchronize);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.SYNC_DATA) + "</font>"));

        initViews();
    }
        private void setDataCount () {
            candidatePojoArrayList = sqliteHelper.getSt_CandidateForSyn();
            countCandidate = candidatePojoArrayList.size();
            if (countCandidate > 0) {
                tvFarmerRegistrationCount.setText(countCandidate + "");
            }
            monitoringStatusPojoArrayList = sqliteHelper.getMonitoringTableDataToBeSync();
            countMonitoring = monitoringStatusPojoArrayList.size();
            if (countMonitoring > 0) {
                tvLandHoldingCount.setText(countMonitoring + "");
            }
        }

        private void initViews () {
            sqliteHelper = new SqliteHelper(this);
            sharedPrefHelper = new SharedPrefHelper(this);
        }


        @OnClick({R.id.llFarmerRegistration,
                R.id.llLandHolding})
        void onClick (View v){
            switch (v.getId()) {
                case R.id.llFarmerRegistration:
                    sendFarmerRegistrationDataOnServer();
                    break;
                case R.id.llLandHolding:
                    sendLandHoldingDataOnServer();
                    break;
//
            }
        }

        private void sendFarmerRegistrationDataOnServer () {
            try {
                //in users table last inserted id is user_id
                int ids = sqliteHelper.getLastInsertedLocalID();

                if (CommonClass.isInternetOn(context)) {
                    candidatePojoArrayList = sqliteHelper.getSt_CandidateForSyn();
                    countCandidate = candidatePojoArrayList.size();
                    if (countCandidate > 0) {
                        for (int i = 0; i < candidatePojoArrayList.size(); i++) {

                            Gson gson = new Gson();
                            String data = gson.toJson(candidatePojoArrayList.get(i));
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(JSON, data);
//                        if (farmerRegistrationPojoArrayList.get(i).getOffline_sync() == 1 && farmerRegistrationPojoArrayList.get(i).getFlag().equals("1")) {
//                            sendEditFramerRegistrationData(body, userId);
//                        } else {
                            sendFramerRegistrationData(body, candidatePojoArrayList.get(i).getLocal_id());
//                        }
                        }
                    } else {
                        Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(STSynchronize.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private void sendFramerRegistrationData (RequestBody body, String localId){
            ProgressDialog dialog = ProgressDialog.show(STSynchronize.this, "", getString(R.string.Please_wait), true);
            APIClient.getPsClient().create(JubiForm_API.class).st_candidate(body).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                        dialog.dismiss();
                        Log.e("bchjc", "vxghs " + jsonObject.toString());
                        String status = jsonObject.optString("status");
                        String message = jsonObject.optString("message");
                        String last_candidate_id = jsonObject.optString("last_candidate_id");

                        if (status.equalsIgnoreCase("1")) {
                            //update flag in tables

                            sqliteHelper.updateId("candidate_registration", "id", Integer.parseInt(last_candidate_id), Integer.parseInt(localId), "local_id");
                            sqliteHelper.updateId("monitoring_status", "candidate_id", Integer.parseInt(last_candidate_id), Integer.parseInt(localId), "local_id");
                            sqliteHelper.updatePSFlag("candidate_registration", Integer.parseInt(localId), 1, "local_id");

                            if (countCandidate > 0) {
                                countCandidate = countCandidate - 1;
                                tvFarmerRegistrationCount.setText(countCandidate + "");
                                dialog.dismiss();
                            }

                            Toast.makeText(STSynchronize.this, "" + message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(STSynchronize.this, "" + message, Toast.LENGTH_LONG).show();
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

        private void sendLandHoldingDataOnServer () {
            try {
                if (CommonClass.isInternetOn(context)) {
                    candidatePojoArrayList = sqliteHelper.getSt_CandidateForSyn();
                    if (candidatePojoArrayList.size() == 0) {
                        monitoringStatusPojoArrayList = sqliteHelper.getMonitoringTableDataToBeSync();
                        countMonitoring = monitoringStatusPojoArrayList.size();
                        if (countMonitoring > 0) {
                            for (int i = 0; i < monitoringStatusPojoArrayList.size(); i++) {
                                Gson gson = new Gson();
                                String data = gson.toJson(monitoringStatusPojoArrayList.get(i));
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                RequestBody body = RequestBody.create(JSON, data);

                                sendAddLandData(body, Integer.parseInt(monitoringStatusPojoArrayList.get(i).getLocal_id()));
                            }
                        } else {
                            Toast.makeText(context, R.string.no_data_pending, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, R.string.sync_farmer_registration_data, Toast.LENGTH_LONG).show();
                    }
//
                } else {
                    Toast.makeText(STSynchronize.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void sendAddLandData (RequestBody body,int local_id){
            ProgressDialog dialog = ProgressDialog.show(context, "", getString(R.string.Please_wait), true);
            APIClient.getPsClient().create(JubiForm_API.class).st_monitoring(body).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                        dialog.dismiss();
//                        Log.e("land_Data", "landData " + jsonObject.toString());
                        String status = jsonObject.optString("status");
                        String message = jsonObject.optString("message");
                        String last_motitor_id = jsonObject.optString("last_motitor_id");
                        if (status.equalsIgnoreCase("1")) {
                            Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                            sqliteHelper.updateId("monitoring_status", "id", Integer.parseInt(last_motitor_id), local_id, "local_id");
                            sqliteHelper.updatePSFlag("monitoring_status", local_id, 1, "local_id");
                            if (countMonitoring > 0) {
                                countMonitoring = countMonitoring - 1;
                                tvLandHoldingCount.setText(countMonitoring + "");
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
    @Override
    protected void onResume() {
        super.onResume();
        setDataCount();
    }
    }
