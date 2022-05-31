package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sanket.jubifarm.Activity.HomeAcivity;
import com.sanket.jubifarm.DataDownload;
import com.sanket.jubifarm.Modal.LoginPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MainMenu extends AppCompatActivity {

    CardView jubifarm,skillTracking,paryavaran;
    private ProgressDialog dialog;
    private Context context = this;
    SharedPrefHelper sharedPrefHelper;
    SqliteHelper sqliteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().setTitle("Main Menu");

        initi();

        paryavaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this,ParyavaranSakhiHome.class);
                startActivity(intent);
            }
        });


        jubifarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sharedPrefHelper.getString("jubifarm_done", "").equalsIgnoreCase("done")){
                    Intent intent = new Intent(MainMenu.this, HomeAcivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    callLoginApi();
                }
            }

        });

        skillTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, SkillTrackingMenuActivity.class);
                startActivity(intent);

            }

        });
    }

    private void initi() {
        jubifarm=findViewById(R.id.jubifarm);
        skillTracking=findViewById(R.id.skillTracking);
        paryavaran=findViewById(R.id.paryavaran);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        sqliteHelper = new SqliteHelper(getApplicationContext());
    }

    private void callLoginApi() {
        dialog = ProgressDialog.show(context, "", getString(R.string.plase), true);
        LoginPojo login = new LoginPojo();

        login.setEmail(sharedPrefHelper.getString("setEmail",""));
        login.setMobile(sharedPrefHelper.getString("setMobile",""));
        login.setLogin_type(sharedPrefHelper.getString("setLogin_type","")); //if login with email.
        login.setPassword(sharedPrefHelper.getString("setPassword", ""));
        login.setToken(sharedPrefHelper.getString("setToken",""));

        Gson gson = new Gson();
        String data = gson.toJson(login);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        APIClient.getClient().create(JubiForm_API.class).LoginApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().toString());
//                    dialog.dismiss();
                    Log.e("jbchjbch", "onResponse: " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    if (status.equalsIgnoreCase("1")) {
                        String user_type = jsonObject.optString("user_type");
                        sharedPrefHelper.setString("user_type", user_type);
                        JSONObject jsonObjectProfile = jsonObject.getJSONObject("profile");
                        String id = jsonObjectProfile.optString("id");
                        sharedPrefHelper.setString("user_id", id);
                        String first_name = jsonObjectProfile.optString("first_name");
                        String last_name = jsonObjectProfile.optString("last_name");
                        String mobile = jsonObjectProfile.optString("mobile");
                        String email = jsonObjectProfile.optString("email");
                        String role_id = jsonObjectProfile.optString("role_id");
                        sharedPrefHelper.setString("role_id", role_id);
                        String profile_photo = jsonObjectProfile.optString("profile_photo");
                        sharedPrefHelper.setString("first_name", first_name);
                        sharedPrefHelper.setString("last_name", last_name);
                        sharedPrefHelper.setString("mobile", mobile);
                        sharedPrefHelper.setString("photo", profile_photo);

                        //save response data into tables
                        sqliteHelper.dropTable("users");
                        sqliteHelper.dropTable("farmer_registration");
                        sqliteHelper.dropTable("farmer_family");
                        sqliteHelper.dropTable("land_holding");
                        sqliteHelper.dropTable("crop_planning");
                        sqliteHelper.dropTable("training");
                        sqliteHelper.dropTable("training_attendance");
                        sqliteHelper.dropTable("post_plantation");
                        sqliteHelper.dropTable("sub_plantation");
                        sqliteHelper.dropTable("sale_details");
                        sqliteHelper.dropTable("production_details");
                        sqliteHelper.dropTable("help_line");
                        sqliteHelper.dropTable("supplier_registration");
                        sqliteHelper.dropTable("input_ordering");
                        sqliteHelper.dropTable("input_ordering_vender");
                        sqliteHelper.dropTable("crop_vegetable_details");

                        if (jsonObject.has("users")) {
                            JSONArray jsonArrayUsers = jsonObject.getJSONArray("users");
                            Log.e("users", "onResponse: " + jsonArrayUsers.toString());
                            if (jsonArrayUsers != null && jsonArrayUsers.length() > 0) {
                                for (int i = 0; i < jsonArrayUsers.length(); i++) {
                                    JSONObject object = new JSONObject(jsonArrayUsers.get(i).toString());
                                    Iterator keys = object.keys();
                                    ContentValues contentValues = new ContentValues();
                                    while (keys.hasNext()) {
                                        String currentDynamicKey = (String) keys.next();
                                        contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                    }
                                    sqliteHelper.saveMasterTable(contentValues, "users");
                                }
                            }
                        }
                        if (jsonObject.has("supplier_registration")) {
                            JSONArray jsonArraySR = jsonObject.getJSONArray("supplier_registration");
                            Log.e("supplier_registration", "onResponse: " + jsonArraySR.toString());
                            if (jsonArraySR != null && jsonArraySR.length() > 0) {
                                for (int i = 0; i < jsonArraySR.length(); i++) {
                                    JSONObject object = new JSONObject(jsonArraySR.get(i).toString());
                                    Iterator keys = object.keys();
                                    ContentValues contentValues = new ContentValues();
                                    while (keys.hasNext()) {
                                        String currentDynamicKey = (String) keys.next();
                                        contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                    }
                                    sqliteHelper.saveMasterTable(contentValues, "supplier_registration");
                                }
                            }
                        }
                        if (user_type.equals("krishi_mitra") || user_type.equals("Farmer")) {
                            if (jsonObject.has("farmer_registration")) {
                                JSONArray jsonArrayFR = jsonObject.getJSONArray("farmer_registration");
                                Log.e("farmer_registration", "onResponse: " + jsonArrayFR.toString());
                                if (jsonArrayFR != null && jsonArrayFR.length() > 0) {
                                    for (int i = 0; i < jsonArrayFR.length(); i++) {
                                        JSONObject object = new JSONObject(jsonArrayFR.get(i).toString());
                                        String farmerID = object.optString("id");
                                        sharedPrefHelper.setString("farmer_id", farmerID);
                                        Iterator keys = object.keys();
                                        ContentValues contentValues = new ContentValues();
                                        while (keys.hasNext()) {
                                            String currentDynamicKey = (String) keys.next();
                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                        }
                                        sqliteHelper.saveMasterTable(contentValues, "farmer_registration");
                                    }
                                }
                            }
                            if (jsonObject.has("farmer_family")) {
                                JSONArray jsonArrayFF = jsonObject.getJSONArray("farmer_family");
                                Log.e("farmer_family", "onResponse: " + jsonArrayFF.toString());
                                if (jsonArrayFF != null && jsonArrayFF.length() > 0) {
                                    for (int i = 0; i < jsonArrayFF.length(); i++) {
                                        JSONObject object = new JSONObject(jsonArrayFF.get(i).toString());
                                        Iterator keys = object.keys();
                                        ContentValues contentValues = new ContentValues();
                                        while (keys.hasNext()) {
                                            String currentDynamicKey = (String) keys.next();
                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                        }
                                        sqliteHelper.saveMasterTable(contentValues, "farmer_family");
                                    }
                                }
                            }
                        }
                        if (user_type.equals("krishi_mitra") ||
                                user_type.equals("Supplier")) {
                            DataDownload dataDownload = new DataDownload();
                            dataDownload.getTablesForKrishiMitra((Activity) context);
                        }
                        else {
                            if (jsonObject.has("training")) {
                                JSONArray jsonArraytraining = jsonObject.getJSONArray("training");
                                Log.e("training", "onResponse: " + jsonArraytraining.toString());
                                if (jsonArraytraining != null && jsonArraytraining.length() > 0) {
                                    for (int i = 0; i < jsonArraytraining.length(); i++) {
                                        JSONObject object = new JSONObject(jsonArraytraining.get(i).toString());
                                        Iterator keys = object.keys();
                                        ContentValues contentValues = new ContentValues();
                                        while (keys.hasNext()) {
                                            String currentDynamicKey = (String) keys.next();
                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                        }
                                        sqliteHelper.saveMasterTable(contentValues, "training");
                                    }
                                }
                            }

                            if (jsonObject.has("crop_vegetable_details")) {
                                JSONArray jsonArraytraining = jsonObject.getJSONArray("crop_vegetable_details");
                                Log.e("crop_vegetable_details", "onResponse: " + jsonArraytraining.toString());
                                if (jsonArraytraining != null && jsonArraytraining.length() > 0) {
                                    for (int i = 0; i < jsonArraytraining.length(); i++) {
                                        JSONObject object = new JSONObject(jsonArraytraining.get(i).toString());
                                        Iterator keys = object.keys();
                                        ContentValues contentValues = new ContentValues();
                                        while (keys.hasNext()) {
                                            String currentDynamicKey = (String) keys.next();
                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                        }
                                        sqliteHelper.saveMasterTable(contentValues, "crop_vegetable_details");
                                    }
                                }
                            }
                            if (jsonObject.has("input_ordering")) {
                                JSONArray jsonArraytraining_attendance = jsonObject.getJSONArray("input_ordering");
                                Log.e("input_ordering", "onResponse: " + jsonArraytraining_attendance.toString());
                                if (jsonArraytraining_attendance != null && jsonArraytraining_attendance.length() > 0) {
                                    for (int i = 0; i < jsonArraytraining_attendance.length(); i++) {
                                        JSONObject object = new JSONObject(jsonArraytraining_attendance.get(i).toString());
                                        Iterator keys = object.keys();
                                        ContentValues contentValues = new ContentValues();
                                        while (keys.hasNext()) {
                                            String currentDynamicKey = (String) keys.next();
                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                        }
                                        sqliteHelper.saveMasterTable(contentValues, "input_ordering");
                                    }
                                }
                            }
                            if (jsonObject.has("input_ordering_vender")) {
                                JSONArray jsonArraytraining_attendance = jsonObject.getJSONArray("input_ordering_vender");
                                Log.e("input_ordering_vender", "input_ordering_vender onResponse: " + jsonArraytraining_attendance.toString());
                                if (jsonArraytraining_attendance != null && jsonArraytraining_attendance.length() > 0) {
                                    for (int i = 0; i < jsonArraytraining_attendance.length(); i++) {
                                        JSONObject object = new JSONObject(jsonArraytraining_attendance.get(i).toString());
                                        Iterator keys = object.keys();
                                        ContentValues contentValues = new ContentValues();
                                        while (keys.hasNext()) {
                                            String currentDynamicKey = (String) keys.next();
                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                        }
                                        sqliteHelper.saveMasterTable(contentValues, "input_ordering_vender");
                                    }
                                }
                            }
                            if (jsonObject.has("training_attendance")) {
                                JSONArray jsonArraytraining_attendance = jsonObject.getJSONArray("training_attendance");
                                Log.e("training_attendance", "onResponse: " + jsonArraytraining_attendance.toString());
                                if (jsonArraytraining_attendance != null && jsonArraytraining_attendance.length() > 0) {
                                    for (int i = 0; i < jsonArraytraining_attendance.length(); i++) {
                                        JSONObject object = new JSONObject(jsonArraytraining_attendance.get(i).toString());
                                        Iterator keys = object.keys();
                                        ContentValues contentValues = new ContentValues();
                                        while (keys.hasNext()) {
                                            String currentDynamicKey = (String) keys.next();
                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                        }
                                        sqliteHelper.saveMasterTable(contentValues, "training_attendance");
                                    }
                                }
                            }
                            if (jsonObject.has("post_plantation")) {
                                JSONArray jsonArraypost_plantation = jsonObject.getJSONArray("post_plantation");
                                Log.e("post_plantation", "onResponse: " + jsonArraypost_plantation.toString());
                                if (jsonArraypost_plantation != null && jsonArraypost_plantation.length() > 0) {
                                    for (int i = 0; i < jsonArraypost_plantation.length(); i++) {
                                        JSONObject object = new JSONObject(jsonArraypost_plantation.get(i).toString());
                                        Iterator keys = object.keys();
                                        ContentValues contentValues = new ContentValues();
                                        while (keys.hasNext()) {
                                            String currentDynamicKey = (String) keys.next();
                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                        }
                                        sqliteHelper.saveMasterTable(contentValues, "post_plantation");
                                    }
                                }
                            }
                            if (jsonObject.has("sub_plantation")) {
                                JSONArray sub_plantation = jsonObject.getJSONArray("sub_plantation");
                                Log.e("sub_plantation", "onResponse: " + sub_plantation.toString());
                                if (sub_plantation != null && sub_plantation.length() > 0) {
                                    for (int i = 0; i < sub_plantation.length(); i++) {
                                        JSONObject object = new JSONObject(sub_plantation.get(i).toString());
                                        Iterator keys = object.keys();
                                        ContentValues contentValues = new ContentValues();
                                        while (keys.hasNext()) {
                                            String currentDynamicKey = (String) keys.next();
                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                        }
                                        sqliteHelper.saveMasterTable(contentValues, "sub_plantation");
                                    }
                                }
                            }
                            if (jsonObject.has("sale_details")) {
                                JSONArray sale_details = jsonObject.getJSONArray("sale_details");
                                Log.e("sale_details", "onResponse: " + sale_details.toString());
                                if (sale_details != null && sale_details.length() > 0) {
                                    for (int i = 0; i < sale_details.length(); i++) {
                                        JSONObject object = new JSONObject(sale_details.get(i).toString());
                                        Iterator keys = object.keys();
                                        ContentValues contentValues = new ContentValues();
                                        while (keys.hasNext()) {
                                            String currentDynamicKey = (String) keys.next();
                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                        }
                                        sqliteHelper.saveMasterTable(contentValues, "sale_details");
                                    }
                                }
                            }
                            if (jsonObject.has("production_details")) {
                                JSONArray production_details = jsonObject.getJSONArray("production_details");
                                Log.e("production_details", "onResponse: " + production_details.toString());
                                if (production_details != null && production_details.length() > 0) {
                                    for (int i = 0; i < production_details.length(); i++) {
                                        JSONObject object = new JSONObject(production_details.get(i).toString());
                                        Iterator keys = object.keys();
                                        ContentValues contentValues = new ContentValues();
                                        while (keys.hasNext()) {
                                            String currentDynamicKey = (String) keys.next();
                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                        }
                                        sqliteHelper.saveMasterTable(contentValues, "production_details");
                                    }
                                }
                            }
                            if (jsonObject.has("help_line")) {
                                JSONArray help_line = jsonObject.getJSONArray("help_line");
                                Log.e("help_line", "onResponse: " + help_line.toString());
                                if (help_line != null && help_line.length() > 0) {
                                    for (int i = 0; i < help_line.length(); i++) {
                                        JSONObject object = new JSONObject(help_line.get(i).toString());
                                        Iterator keys = object.keys();
                                        ContentValues contentValues = new ContentValues();
                                        while (keys.hasNext()) {
                                            String currentDynamicKey = (String) keys.next();
                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                        }
                                        sqliteHelper.saveMasterTable(contentValues, "help_line");
                                    }
                                }
                            }
                            if (jsonObject.has("land_holding")) {
                                JSONArray jsonArrayLH = jsonObject.getJSONArray("land_holding");
                                Log.e("land_holding", "onResponse: " + jsonArrayLH.toString());
                                if (jsonArrayLH != null && jsonArrayLH.length() > 0) {
                                    for (int i = 0; i < jsonArrayLH.length(); i++) {
                                        JSONObject object = new JSONObject(jsonArrayLH.get(i).toString());
                                        Iterator keys = object.keys();
                                        ContentValues contentValues = new ContentValues();
                                        while (keys.hasNext()) {
                                            String currentDynamicKey = (String) keys.next();
                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                        }
                                        sqliteHelper.saveMasterTable(contentValues, "land_holding");
                                    }
                                }
                            }
                            if (jsonObject.has("crop_planning")) {
                                JSONArray jsonArrayCP = jsonObject.getJSONArray("crop_planning");
                                Log.e("crop_planning", "onResponse: " + jsonArrayCP.toString());
                                if (jsonArrayCP != null && jsonArrayCP.length() > 0) {
                                    for (int i = 0; i < jsonArrayCP.length(); i++) {
                                        JSONObject object = new JSONObject(jsonArrayCP.get(i).toString());
                                        Iterator keys = object.keys();
                                        ContentValues contentValues = new ContentValues();
                                        while (keys.hasNext()) {
                                            String currentDynamicKey = (String) keys.next();
                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
                                        }
                                        sqliteHelper.saveMasterTable(contentValues, "crop_planning");
                                        dialog.dismiss();

                                    }
                                }
                            }



                        }

                        //
//                        Intent intent = new Intent(LoginScreenActivity.this,HomeAcivity.class);
                        Intent intent = new Intent(MainMenu.this, HomeAcivity.class);
                        startActivity(intent);
                        finish();
                        sharedPrefHelper.setString("jubifarm_done", "done");



                    } else {
                        Toast.makeText(MainMenu.this, "" + message, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("LOGIN SCREEN ", "====" + t.getMessage());
            }
        });

    }

}