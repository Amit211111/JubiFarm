package com.sanket.jubifarm.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.DataDownload;
import com.sanket.jubifarm.Livelihood.MainMenu;
import com.sanket.jubifarm.Location.GpsUtils;
import com.sanket.jubifarm.Modal.LoginPojo;
import com.sanket.jubifarm.Modal.UsersPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.receiver.MyAlarmReceiver;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.sanket.jubifarm.service.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreenActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = LoginScreenActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Button btnLogIn;
    TextView ivLogLogo, tvSign, tvForgotPass, tv_sapyler;
    SqliteHelper sqliteHelper;
    UsersPojo usersPojo;
    EditText etPassword, etEmailLog;

    private Context context = this;
    private ProgressDialog dialog;

    SharedPrefHelper sharedPrefHelper;
    private String[] users = {"users"};
    private String[] farmer_registration = {"farmer_registration"};
    private String[] farmer_family = {"farmer_family"};

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Location mLastLocation;
    String altitude;
    private String latitude;
    private String longitude;
    private boolean isGPS;
    private PendingIntent alarmPendingIntent;
    private CheckBox cb_showPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        getSupportActionBar().hide();
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(context);
        usersPojo = new UsersPojo();
        dialog = new ProgressDialog(context);

        btnLogIn = findViewById(R.id.btnLogIn);
        ivLogLogo = findViewById(R.id.ivLogLogo);
        tvForgotPass = findViewById(R.id.tvForgotPass);
        etPassword = findViewById(R.id.etPassword);
        etEmailLog = findViewById(R.id.etEmailLog);
        tvSign = findViewById(R.id.tvSign);
        tv_sapyler = findViewById(R.id.tv_sapyler);
        cb_showPassword=findViewById(R.id.cb_showPassword);

        showPassword();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    callLoginApi();
                }
            }
        });
        tvSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Regintent = new Intent(LoginScreenActivity.this, FarmerRegistration.class);
                Regintent.putExtra("fromLogin", "1");
                startActivity(Regintent);

            }
        });
        tv_sapyler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Regintent = new Intent(LoginScreenActivity.this, VendorRegistration.class);
                startActivity(Regintent);

            }
        });
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Regintent = new Intent(LoginScreenActivity.this, ForgotPasswordActivity.class);
                startActivity(Regintent);

            }
        });

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });
        buildGoogleApiClient();

        /*fire-base token*/
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        alarmPendingIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 10 minute
        long firstMillis = System.currentTimeMillis(); // first run of alarm is immediate
        int intervalMillis = 1800000 ; // as of API 19, alarm manager will be forced up to 60000 to save battery
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // See https://developer.android.com/training/scheduling/alarms.html
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, alarmPendingIntent);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //  txtMessage.setText(message);
                }
            }
        };
        displayFirebaseRegId();
    }

    private void showPassword() {
        cb_showPassword.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // show password
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // hide password
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginScreenActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                sharedPrefHelper.setString("Token", mToken);
                Log.e("Token", mToken);
            }
        });
        String regId = pref.getString("regId", null);
        Log.e(TAG, "Firebase reg id: " + regId);
        if (!TextUtils.isEmpty(regId)) {
            //txtRegId.setText("Firebase Reg Id: " + regId);
        } else {
            //txtRegId.setText("Firebase Reg Id is not received yet!");
        }
    }

    private boolean checkValidation() {
        if (etEmailLog.getText().toString().trim().length() == 0) {
            Toast.makeText(context, R.string.pleae_enter_mobile_email, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPassword.getText().toString().trim().length() == 0) {
            Toast.makeText(context, R.string.please_enter_password, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void callLoginApi() {
        dialog = ProgressDialog.show(context, "", getString(R.string.plase), true);
        LoginPojo login = new LoginPojo();
        if (etEmailLog.getText().toString().trim().matches(Patterns.EMAIL_ADDRESS.pattern())) {
            login.setEmail(etEmailLog.getText().toString().trim());
            login.setMobile("");
            login.setLogin_type("2"); //if login with email.
            sharedPrefHelper.setString("setEmail",etEmailLog.getText().toString().trim());
            sharedPrefHelper.setString("setMobile","");
            sharedPrefHelper.setString("setLogin_type","2");
        } else {
            login.setMobile(etEmailLog.getText().toString().trim());
            login.setEmail("");
            login.setLogin_type("1"); //if login with mobile no.
            sharedPrefHelper.setString("setEmail","");
            sharedPrefHelper.setString("setMobile",etEmailLog.getText().toString().trim());
            sharedPrefHelper.setString("setLogin_type","1");
        }
        login.setPassword(etPassword.getText().toString().trim());
        login.setToken(sharedPrefHelper.getString("Token", ""));
        sharedPrefHelper.setString("setPassword", etPassword.getText().toString().trim());
        sharedPrefHelper.setString("setToken","");

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
//                        String first_name = jsonObjectProfile.optString("first_name");
//                        String last_name = jsonObjectProfile.optString("last_name");
//                        String mobile = jsonObjectProfile.optString("mobile");
//                        String email = jsonObjectProfile.optString("email");
//                        String role_id = jsonObjectProfile.optString("role_id");
//                        sharedPrefHelper.setString("role_id", role_id);
//                        String profile_photo = jsonObjectProfile.optString("profile_photo");
//                        sharedPrefHelper.setString("first_name", first_name);
//                        sharedPrefHelper.setString("last_name", last_name);
//                        sharedPrefHelper.setString("mobile", mobile);
//                        sharedPrefHelper.setString("photo", profile_photo);
//
//                        //save response data into tables
//                        sqliteHelper.dropTable("users");
//                        sqliteHelper.dropTable("farmer_registration");
//                        sqliteHelper.dropTable("farmer_family");
//                        sqliteHelper.dropTable("land_holding");
//                        sqliteHelper.dropTable("crop_planning");
//                        sqliteHelper.dropTable("training");
//                        sqliteHelper.dropTable("training_attendance");
//                        sqliteHelper.dropTable("post_plantation");
//                        sqliteHelper.dropTable("sub_plantation");
//                        sqliteHelper.dropTable("sale_details");
//                        sqliteHelper.dropTable("production_details");
//                        sqliteHelper.dropTable("help_line");
//                        sqliteHelper.dropTable("supplier_registration");
//                        sqliteHelper.dropTable("input_ordering");
//                        sqliteHelper.dropTable("input_ordering_vender");
//                        sqliteHelper.dropTable("crop_vegetable_details");
//
//                        if (jsonObject.has("users")) {
//                            JSONArray jsonArrayUsers = jsonObject.getJSONArray("users");
//                            Log.e("users", "onResponse: " + jsonArrayUsers.toString());
//                            if (jsonArrayUsers != null && jsonArrayUsers.length() > 0) {
//                                for (int i = 0; i < jsonArrayUsers.length(); i++) {
//                                    JSONObject object = new JSONObject(jsonArrayUsers.get(i).toString());
//                                    Iterator keys = object.keys();
//                                    ContentValues contentValues = new ContentValues();
//                                    while (keys.hasNext()) {
//                                        String currentDynamicKey = (String) keys.next();
//                                        contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                    }
//                                    sqliteHelper.saveMasterTable(contentValues, "users");
//                                }
//                            }
//                        }
//                        if (jsonObject.has("supplier_registration")) {
//                            JSONArray jsonArraySR = jsonObject.getJSONArray("supplier_registration");
//                            Log.e("supplier_registration", "onResponse: " + jsonArraySR.toString());
//                            if (jsonArraySR != null && jsonArraySR.length() > 0) {
//                                for (int i = 0; i < jsonArraySR.length(); i++) {
//                                    JSONObject object = new JSONObject(jsonArraySR.get(i).toString());
//                                    Iterator keys = object.keys();
//                                    ContentValues contentValues = new ContentValues();
//                                    while (keys.hasNext()) {
//                                        String currentDynamicKey = (String) keys.next();
//                                        contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                    }
//                                    sqliteHelper.saveMasterTable(contentValues, "supplier_registration");
//                                }
//                            }
//                        }
//                        if (user_type.equals("krishi_mitra") || user_type.equals("Farmer")) {
//                            if (jsonObject.has("farmer_registration")) {
//                                JSONArray jsonArrayFR = jsonObject.getJSONArray("farmer_registration");
//                                Log.e("farmer_registration", "onResponse: " + jsonArrayFR.toString());
//                                if (jsonArrayFR != null && jsonArrayFR.length() > 0) {
//                                    for (int i = 0; i < jsonArrayFR.length(); i++) {
//                                        JSONObject object = new JSONObject(jsonArrayFR.get(i).toString());
//                                        String farmerID = object.optString("id");
//                                        sharedPrefHelper.setString("farmer_id", farmerID);
//                                        Iterator keys = object.keys();
//                                        ContentValues contentValues = new ContentValues();
//                                        while (keys.hasNext()) {
//                                            String currentDynamicKey = (String) keys.next();
//                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                        }
//                                        sqliteHelper.saveMasterTable(contentValues, "farmer_registration");
//                                    }
//                                }
//                            }
//                            if (jsonObject.has("farmer_family")) {
//                                JSONArray jsonArrayFF = jsonObject.getJSONArray("farmer_family");
//                                Log.e("farmer_family", "onResponse: " + jsonArrayFF.toString());
//                                if (jsonArrayFF != null && jsonArrayFF.length() > 0) {
//                                    for (int i = 0; i < jsonArrayFF.length(); i++) {
//                                        JSONObject object = new JSONObject(jsonArrayFF.get(i).toString());
//                                        Iterator keys = object.keys();
//                                        ContentValues contentValues = new ContentValues();
//                                        while (keys.hasNext()) {
//                                            String currentDynamicKey = (String) keys.next();
//                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                        }
//                                        sqliteHelper.saveMasterTable(contentValues, "farmer_family");
//                                    }
//                                }
//                            }
//                        }
//                        if (user_type.equals("krishi_mitra") ||
//                                user_type.equals("Supplier")) {
//                            DataDownload dataDownload = new DataDownload();
//                            dataDownload.getTablesForKrishiMitra((Activity) context);
//                        }
//                        else {
//                            if (jsonObject.has("training")) {
//                                JSONArray jsonArraytraining = jsonObject.getJSONArray("training");
//                                Log.e("training", "onResponse: " + jsonArraytraining.toString());
//                                if (jsonArraytraining != null && jsonArraytraining.length() > 0) {
//                                    for (int i = 0; i < jsonArraytraining.length(); i++) {
//                                        JSONObject object = new JSONObject(jsonArraytraining.get(i).toString());
//                                        Iterator keys = object.keys();
//                                        ContentValues contentValues = new ContentValues();
//                                        while (keys.hasNext()) {
//                                            String currentDynamicKey = (String) keys.next();
//                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                        }
//                                        sqliteHelper.saveMasterTable(contentValues, "training");
//                                    }
//                                }
//                            }
//
//                            if (jsonObject.has("crop_vegetable_details")) {
//                                JSONArray jsonArraytraining = jsonObject.getJSONArray("crop_vegetable_details");
//                                Log.e("crop_vegetable_details", "onResponse: " + jsonArraytraining.toString());
//                                if (jsonArraytraining != null && jsonArraytraining.length() > 0) {
//                                    for (int i = 0; i < jsonArraytraining.length(); i++) {
//                                        JSONObject object = new JSONObject(jsonArraytraining.get(i).toString());
//                                        Iterator keys = object.keys();
//                                        ContentValues contentValues = new ContentValues();
//                                        while (keys.hasNext()) {
//                                            String currentDynamicKey = (String) keys.next();
//                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                        }
//                                        sqliteHelper.saveMasterTable(contentValues, "crop_vegetable_details");
//                                    }
//                                }
//                            }
//                            if (jsonObject.has("input_ordering")) {
//                                JSONArray jsonArraytraining_attendance = jsonObject.getJSONArray("input_ordering");
//                                Log.e("input_ordering", "onResponse: " + jsonArraytraining_attendance.toString());
//                                if (jsonArraytraining_attendance != null && jsonArraytraining_attendance.length() > 0) {
//                                    for (int i = 0; i < jsonArraytraining_attendance.length(); i++) {
//                                        JSONObject object = new JSONObject(jsonArraytraining_attendance.get(i).toString());
//                                        Iterator keys = object.keys();
//                                        ContentValues contentValues = new ContentValues();
//                                        while (keys.hasNext()) {
//                                            String currentDynamicKey = (String) keys.next();
//                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                        }
//                                        sqliteHelper.saveMasterTable(contentValues, "input_ordering");
//                                    }
//                                }
//                            }
//                            if (jsonObject.has("input_ordering_vender")) {
//                                JSONArray jsonArraytraining_attendance = jsonObject.getJSONArray("input_ordering_vender");
//                                Log.e("input_ordering_vender", "input_ordering_vender onResponse: " + jsonArraytraining_attendance.toString());
//                                if (jsonArraytraining_attendance != null && jsonArraytraining_attendance.length() > 0) {
//                                    for (int i = 0; i < jsonArraytraining_attendance.length(); i++) {
//                                        JSONObject object = new JSONObject(jsonArraytraining_attendance.get(i).toString());
//                                        Iterator keys = object.keys();
//                                        ContentValues contentValues = new ContentValues();
//                                        while (keys.hasNext()) {
//                                            String currentDynamicKey = (String) keys.next();
//                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                        }
//                                        sqliteHelper.saveMasterTable(contentValues, "input_ordering_vender");
//                                    }
//                                }
//                            }
//                            if (jsonObject.has("training_attendance")) {
//                                JSONArray jsonArraytraining_attendance = jsonObject.getJSONArray("training_attendance");
//                                Log.e("training_attendance", "onResponse: " + jsonArraytraining_attendance.toString());
//                                if (jsonArraytraining_attendance != null && jsonArraytraining_attendance.length() > 0) {
//                                    for (int i = 0; i < jsonArraytraining_attendance.length(); i++) {
//                                        JSONObject object = new JSONObject(jsonArraytraining_attendance.get(i).toString());
//                                        Iterator keys = object.keys();
//                                        ContentValues contentValues = new ContentValues();
//                                        while (keys.hasNext()) {
//                                            String currentDynamicKey = (String) keys.next();
//                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                        }
//                                        sqliteHelper.saveMasterTable(contentValues, "training_attendance");
//                                    }
//                                }
//                            }
//                            if (jsonObject.has("post_plantation")) {
//                                JSONArray jsonArraypost_plantation = jsonObject.getJSONArray("post_plantation");
//                                Log.e("post_plantation", "onResponse: " + jsonArraypost_plantation.toString());
//                                if (jsonArraypost_plantation != null && jsonArraypost_plantation.length() > 0) {
//                                    for (int i = 0; i < jsonArraypost_plantation.length(); i++) {
//                                        JSONObject object = new JSONObject(jsonArraypost_plantation.get(i).toString());
//                                        Iterator keys = object.keys();
//                                        ContentValues contentValues = new ContentValues();
//                                        while (keys.hasNext()) {
//                                            String currentDynamicKey = (String) keys.next();
//                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                        }
//                                        sqliteHelper.saveMasterTable(contentValues, "post_plantation");
//                                    }
//                                }
//                            }
//                            if (jsonObject.has("sub_plantation")) {
//                                JSONArray sub_plantation = jsonObject.getJSONArray("sub_plantation");
//                                Log.e("sub_plantation", "onResponse: " + sub_plantation.toString());
//                                if (sub_plantation != null && sub_plantation.length() > 0) {
//                                    for (int i = 0; i < sub_plantation.length(); i++) {
//                                        JSONObject object = new JSONObject(sub_plantation.get(i).toString());
//                                        Iterator keys = object.keys();
//                                        ContentValues contentValues = new ContentValues();
//                                        while (keys.hasNext()) {
//                                            String currentDynamicKey = (String) keys.next();
//                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                        }
//                                        sqliteHelper.saveMasterTable(contentValues, "sub_plantation");
//                                    }
//                                }
//                            }
//                            if (jsonObject.has("sale_details")) {
//                                JSONArray sale_details = jsonObject.getJSONArray("sale_details");
//                                Log.e("sale_details", "onResponse: " + sale_details.toString());
//                                if (sale_details != null && sale_details.length() > 0) {
//                                    for (int i = 0; i < sale_details.length(); i++) {
//                                        JSONObject object = new JSONObject(sale_details.get(i).toString());
//                                        Iterator keys = object.keys();
//                                        ContentValues contentValues = new ContentValues();
//                                        while (keys.hasNext()) {
//                                            String currentDynamicKey = (String) keys.next();
//                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                        }
//                                        sqliteHelper.saveMasterTable(contentValues, "sale_details");
//                                    }
//                                }
//                            }
//                            if (jsonObject.has("production_details")) {
//                                JSONArray production_details = jsonObject.getJSONArray("production_details");
//                                Log.e("production_details", "onResponse: " + production_details.toString());
//                                if (production_details != null && production_details.length() > 0) {
//                                    for (int i = 0; i < production_details.length(); i++) {
//                                        JSONObject object = new JSONObject(production_details.get(i).toString());
//                                        Iterator keys = object.keys();
//                                        ContentValues contentValues = new ContentValues();
//                                        while (keys.hasNext()) {
//                                            String currentDynamicKey = (String) keys.next();
//                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                        }
//                                        sqliteHelper.saveMasterTable(contentValues, "production_details");
//                                    }
//                                }
//                            }
//                            if (jsonObject.has("help_line")) {
//                                JSONArray help_line = jsonObject.getJSONArray("help_line");
//                                Log.e("help_line", "onResponse: " + help_line.toString());
//                                if (help_line != null && help_line.length() > 0) {
//                                    for (int i = 0; i < help_line.length(); i++) {
//                                        JSONObject object = new JSONObject(help_line.get(i).toString());
//                                        Iterator keys = object.keys();
//                                        ContentValues contentValues = new ContentValues();
//                                        while (keys.hasNext()) {
//                                            String currentDynamicKey = (String) keys.next();
//                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                        }
//                                        sqliteHelper.saveMasterTable(contentValues, "help_line");
//                                    }
//                                }
//                            }
//                            if (jsonObject.has("land_holding")) {
//                                JSONArray jsonArrayLH = jsonObject.getJSONArray("land_holding");
//                                Log.e("land_holding", "onResponse: " + jsonArrayLH.toString());
//                                if (jsonArrayLH != null && jsonArrayLH.length() > 0) {
//                                    for (int i = 0; i < jsonArrayLH.length(); i++) {
//                                        JSONObject object = new JSONObject(jsonArrayLH.get(i).toString());
//                                        Iterator keys = object.keys();
//                                        ContentValues contentValues = new ContentValues();
//                                        while (keys.hasNext()) {
//                                            String currentDynamicKey = (String) keys.next();
//                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                        }
//                                        sqliteHelper.saveMasterTable(contentValues, "land_holding");
//                                    }
//                                }
//                            }
//                            if (jsonObject.has("crop_planning")) {
//                                JSONArray jsonArrayCP = jsonObject.getJSONArray("crop_planning");
//                                Log.e("crop_planning", "onResponse: " + jsonArrayCP.toString());
//                                if (jsonArrayCP != null && jsonArrayCP.length() > 0) {
//                                    for (int i = 0; i < jsonArrayCP.length(); i++) {
//                                        JSONObject object = new JSONObject(jsonArrayCP.get(i).toString());
//                                        Iterator keys = object.keys();
//                                        ContentValues contentValues = new ContentValues();
//                                        while (keys.hasNext()) {
//                                            String currentDynamicKey = (String) keys.next();
//                                            contentValues.put(currentDynamicKey, object.get(currentDynamicKey).toString());
//                                        }
//                                        sqliteHelper.saveMasterTable(contentValues, "crop_planning");
//                                        dialog.dismiss();
//
//                                    }
//                                }
//                            }
//
//
//
//                        }

                        //
//                        Intent intent = new Intent(LoginScreenActivity.this,HomeAcivity.class);
                        sharedPrefHelper.setString("login","1");
                        Intent intent = new Intent(LoginScreenActivity.this,MainMenu.class);
                        startActivity(intent);
                        finish();



                    } else {
                        Toast.makeText(LoginScreenActivity.this, "" + message, Toast.LENGTH_LONG).show();
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



    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100); // Update location every second
        if (ActivityCompat.checkSelfPermission(LoginScreenActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = String.valueOf(mLastLocation.getLatitude());
            longitude = String.valueOf(mLastLocation.getLongitude());
            altitude = String.valueOf(mLastLocation.getAltitude());
            sharedPrefHelper.setString("LAT", latitude);
            sharedPrefHelper.setString("LONG", longitude);
            sharedPrefHelper.setString("ALTI", altitude);


        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());

        System.out.println("latitude>>>>" + latitude);
        altitude = String.valueOf(location.getAltitude());
        //String Address=cf.getAddress(Double.parseDouble(latitude), Double.parseDouble(longitude));
        SharedPreferences pref = getApplicationContext().getSharedPreferences("GCMSetting", MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("LATTITUDE", latitude);
        editor.putString("LONGITUDE", longitude);
        editor.putString("ALTITUDE", altitude);

        //editor.putString("Address", Address);

        editor.commit(); // commit changes

        /*GlobalVars.LATTITUDE = Double.parseDouble(latitude);
        GlobalVars.LONGITUDE = Double.parseDouble(longitude);
        GlobalVars.ALTITUDE = Double.parseDouble(altitude);
        new Thread(new Runnable() {
            @Override
            public void run() {
                GlobalVars.Address=cf.getAddress(GlobalVars.LATTITUDE, GlobalVars.LONGITUDE);
            }
        });*/

       /* Float thespeed = location.getSpeed();
        Double lat=location.getLatitude();
        Double lng=location.getLongitude();*/
        // tv.setText("Location -"+String.valueOf(lat)+String.valueOf(lng)+"\n Speed: "+String.valueOf(thespeed));

        //Log.v("speed", String.valueOf(thespeed));

    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
         mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}