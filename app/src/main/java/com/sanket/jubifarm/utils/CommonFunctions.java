package com.sanket.jubifarm.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Modal.CropPlaningPojo;
import com.sanket.jubifarm.Modal.LandHoldingPojo;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonFunctions {

    Context context;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    public CommonFunctions(Context context) {
        this.context = context;
        sqliteHelper = new SqliteHelper(context);
        sharedPrefHelper=new SharedPrefHelper(context);
    }

    public boolean isInternetOn() {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

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

    public  void sendLandData() {
        ArrayList<LandHoldingPojo> landHoldingAL = new ArrayList<>();
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
                sendAddLandData(body, Integer.parseInt(landHoldingAL.get(i).getLocal_id()));

            }
        }
    }
    private void sendAddLandData(RequestBody body, int local_id) {
        APIClient.getClient().create(JubiForm_API.class).AddLand(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String land_id_primarykey = jsonObject.optString("land_id_primarykey");
                    String land_id = jsonObject.optString("land_id");
                    if (status.equalsIgnoreCase("1")) {
                      //  Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                        sqliteHelper.updateServerid("land_holding", local_id, Integer.parseInt(land_id_primarykey));
                        sqliteHelper.updateLandId("land_holding", "land_id", land_id, local_id, "local_id");
                        sqliteHelper.updateLocalFlag("land_holding", local_id, 1);

                    }
                    else {
                      //  Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    public  void addPlantData(){
        ArrayList<CropPlaningPojo> cropPlanningsAL = new ArrayList<>();
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

    }
    private void sendAddPlantData(RequestBody body, int local_id) {
        APIClient.getClient().create(JubiForm_API.class).add_plant(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    if (status.equalsIgnoreCase("1")) {
                        sqliteHelper.updateAddPlantFlag("crop_planning",local_id,1);
                        String plant_id = jsonObject.optString("plant_id_AI");
                        String plant_ids = jsonObject.optString("plant_id");
                        sqliteHelper.updateAddPlantID("crop_planning",local_id, plant_ids);
                        sqliteHelper.updateServerid("crop_planning",local_id, Integer.parseInt(plant_id));

                    }
                    else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


}
