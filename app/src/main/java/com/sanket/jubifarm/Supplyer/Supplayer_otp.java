package com.sanket.jubifarm.Supplyer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Activity.ChangePassword;
import com.sanket.jubifarm.Activity.LoginScreenActivity;
import com.sanket.jubifarm.Modal.OtpVerifyPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Supplayer_otp extends AppCompatActivity {
    @BindView(R.id.supplyer_phone)
    EditText supplyer_phone;
    @BindView(R.id.supplyer_otp)
    EditText supplyer_otp;
    @BindView(R.id.supplyer_resend_otp)
    Button supplyer_resend_otp;
    @BindView(R.id.supplyer_chronometer)
    Chronometer supplyer_chronometer;
    @BindView(R.id.supplyer_verify_otp)
    Button supplyer_verify_otp;

    private Context context = this;
    private ProgressDialog dialog;
    SharedPrefHelper sharedPrefHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplayer_otp);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + "Otp Authentication" + "</font>"));

        dialog = new ProgressDialog(context);
        sharedPrefHelper = new SharedPrefHelper(context);

        Intent intent = getIntent();
        String Mobile = intent.getStringExtra("sup_Mobile");
        supplyer_phone.setText(Mobile);

        supplyer_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String mobileNo = supplyer_phone.getText().toString().trim();
               // String user_id = sharedPrefHelper.getString("user_id", "");
               // OtpVerify(mobileNo,"",user_id);

            }
        });
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                supplyer_chronometer.setText(new StringBuilder().append("00:").append(millisUntilFinished / 1000).toString());
                supplyer_resend_otp.setVisibility(View.GONE);
            }

            public void onFinish() {
                supplyer_resend_otp.setVisibility(View.VISIBLE);
                supplyer_chronometer.setVisibility(View.GONE);
            }

        }.start();
    }
    @OnClick(R.id.supplyer_verify_otp)
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.supplyer_verify_otp:
                String mobileNo = supplyer_phone.getText().toString().trim();
                String otp = supplyer_otp.getText().toString().trim();
                String user_id = sharedPrefHelper.getString("user_id", "");
                if (otp.isEmpty()){
                    Toast.makeText(context, "Please Enter Correct OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                OtpVerify(mobileNo, otp,user_id);
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(context, LoginScreenActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void OtpVerify (String mobile, String otp, String user_id) {
        dialog = ProgressDialog.show(context, "", "Please Wait...", true);
        OtpVerifyPojo otpverifyp = new OtpVerifyPojo();
        otpverifyp.setMobile(mobile);
        otpverifyp.setOtp(otp);
        otpverifyp.setUser_id(user_id);
        otpverifyp.setRole_id(sharedPrefHelper.getString("role_id", ""));
        Gson mGson = new Gson();
        String data = mGson.toJson(otpverifyp);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        APIClient.getClient().create(JubiForm_API.class).otp_verification(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JSONObject jsonObject = null;
                dialog.dismiss();
                try {
                    Log.e("Response=====","======="+response.toString());
                    jsonObject = new JSONObject(response.body().toString());
                    String status_ = jsonObject.optString("status");
                    if (Integer.valueOf(status_)==1){
                        String message_ = jsonObject.optString("message");
                        String user_id_ = jsonObject.optString("user_id");
                        Intent intent = new Intent(context, ChangePassword.class);
                        intent.putExtra("role_id", user_id_);
                        startActivity(intent);
                        finish();
                        Log.e("OTPSCREEN====", "Data : ," + status_ + "," + message_ + "," + user_id_);
                        
                    }else {
                        Toast.makeText(Supplayer_otp.this, "Invalid OTP...", Toast.LENGTH_SHORT).show();
                    }
                 
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("OTP SCREEN ","===="+t.getMessage());
            }
        });

    }
}