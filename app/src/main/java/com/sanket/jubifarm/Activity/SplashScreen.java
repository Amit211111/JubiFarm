   package com.sanket.jubifarm.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.DataDownload;
import com.sanket.jubifarm.Livelihood.MainMenu;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.cropImage.utils.UiHelper;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class    SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    private static String splashLoaded = "No";

    public static final String MainPP_SP = "MainPP_data";
    public static final int R_PERM = 2822;
    private static final int REQUEST = 112;
    private UiHelper uiHelper = new UiHelper();
    String lngTypt="en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash_screen);
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        //  mProgressDialog=new ProgressDialog(this);
        sqliteHelper.openDataBase();
        SharedPreferences settings = getSharedPreferences(MainPP_SP, 0);
        HashMap<String, String> map = (HashMap<String, String>) settings.getAll();

        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("TAG","@@@ IN IF Build.VERSION.SDK_INT >= 23");
            //if (uiHelper.checkSelfPermissions(this)); //make pic image permission on splash
            String[] PERMISSIONS = {
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA
            };
            if (!hasPermissions(this, PERMISSIONS)) {
                Log.d("TAG","@@@ IN IF hasPermissions");
                ActivityCompat.requestPermissions((Activity) this, PERMISSIONS, REQUEST );
            } else {
                Log.d("TAG","@@@ IN ELSE hasPermissions");
                callNextActivity();
            }
        } else {
            Log.d("TAG","@@@ IN ELSE  Build.VERSION.SDK_INT >= 23");
            callNextActivity();
        }
    }

    public void callNextActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                  // String hh=signingkey("4666b834096f2e0f7247b07fc6791de0","c//f4cO+KzRcYgWJUGfTaO4ubxTyfh2YaRadph9GIMJX7IoJsnQmo1GWlTuM7BDf","POST","50.62.6.159","{\"source_identifier\":\"23002\",\"patient_identifier_type\":\"uhid/ipd\",\"patient_identifier\":\"2134390\"}");
                  // Log.e("jj",hh);
                lngTypt = sharedPrefHelper.getString("languageID","en");

                if (lngTypt.equals("en")){
                    setLanguage("en");
                }else if (lngTypt.equals("hin")){
                    setLanguage("hin");
                }else if (lngTypt.equals("guj")){
                    setLanguage("guj");
                }else if (lngTypt.equals("kan")){
                    setLanguage("kan");
                }else {
                    //
                }

                splashLoaded = sharedPrefHelper.getString("isSplashLoaded", "No");
                String user_type = sharedPrefHelper.getString("user_type", "");

                if (splashLoaded.equals("No")) {
                    DataDownload dataDownload = new DataDownload();
                    dataDownload.getMasterTables(SplashScreen.this);
                    dataDownload.getTables(SplashScreen.this);
                } else if (user_type.equals("Farmer") || user_type.equals("krishi_mitra")
                        || user_type.equals("Supplier")) {
//                    Intent intent = new Intent(SplashScreen.this, HomeAcivity.class);
                    Intent intent = new Intent(SplashScreen.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreen.this, LoginScreenActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG", "@@@ PERMISSIONS grant");
                    callNextActivity();
                } else {
                    Log.d("TAG", "@@@ PERMISSIONS Denied");
                    Toast.makeText(this, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void setLanguage(String languageToLoad) {
        if (!languageToLoad.equals("")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = getBaseContext().getResources().getConfiguration();
                //config.locale = locale;
                config.setLocale(locale);
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

            } else {
                Resources resources = getBaseContext().getResources();
                Configuration configuration = resources.getConfiguration();
                //configuration.setLocale(new Locale(lang));
                configuration.locale = new Locale(languageToLoad);
                getBaseContext().getApplicationContext().createConfigurationContext(configuration);
            }
        }
    }

    public static String signingkey(String apiAccessKey, String hmacSecretKey, String apiMethod, String clientip, String jsonInput) {
        String retval = "";
        final String HMAC_SHA_512_ALGORITHM = "HmacSHA512";
        final String userKey = apiAccessKey + hmacSecretKey;
        try {
// get an hmac_sha512 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(userKey.getBytes(), HMAC_SHA_512_ALGORITHM);
// get an hmac_sha512 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA_512_ALGORITHM);
            mac.init(signingKey);
// compute the hmac on input data bytes
            String payload = (apiMethod.toUpperCase()).concat(clientip).concat(jsonInput);
            byte[] rawHmac = mac.doFinal(payload.getBytes());
// the value of hmac needs to placed in headers under the key api-hmac
            return bytesToHexString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String bytesToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02x", b&0xff));
        }
        return sb.toString();
    }
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
 }