package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

public class OtpScreen extends AppCompatActivity {
    @BindView(R.id.reg_phone)
    TextView reg_phone;
    @BindView(R.id.reg_otp)
    EditText reg_otp;
    @BindView(R.id.resend_otp)
    Button resend_otp;
    @BindView(R.id.chronometer)
    Chronometer chronometer;
    @BindView(R.id.verify_otp)
    Button verify_otp;
    ProgressDialog mprogressDialog;
    private Context context = this;
    private ProgressDialog dialog;
    private SharedPrefHelper sharedPrefHelper;
    private String mobile = "";
    private String mobile_for_show = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.Otp_Authentication) + "</font>"));
       // setTitle("Otp Authentication");
        initViews();

        /*get intent value here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mobile = bundle.getString("Mobile", "");
            mobile_for_show = bundle.getString("Mobile", "");
            mobile_for_show = mobile_for_show.replaceAll("\\d(?=(?:\\D*\\d){2})", "*");
            reg_phone.setText(getString(R.string.otp_number) + " +91 " + mobile_for_show);
        }


        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                chronometer.setText(new StringBuilder().append("00:").append(millisUntilFinished / 1000).toString());
                resend_otp.setVisibility(View.GONE);
            }

            public void onFinish() {
                resend_otp.setVisibility(View.VISIBLE);
                chronometer.setVisibility(View.GONE);
            }

        }.start();


        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id1 = sharedPrefHelper.getString("user_id", "");
                ResendOtp(mobile, user_id1);            }
        });

    }

    private void initViews() {
        dialog = new ProgressDialog(context);
        sharedPrefHelper = new SharedPrefHelper(context);
    }

    @OnClick(R.id.verify_otp)
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_otp:
                String otp = reg_otp.getText().toString().trim();
                String user_id = sharedPrefHelper.getString("user_id", "");
                if (otp.isEmpty()) {
                    Toast.makeText(context, "Please Enter Correct OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                OtpVerify(mobile, otp, user_id);
                break;


        }


    }


    private void ResendOtp(String mobile, String user_id1) {
        OtpVerifyPojo otpverifyp = new OtpVerifyPojo();
        otpverifyp.setMobile(mobile);
        otpverifyp.setUser_id(user_id1);
        Gson gson = new Gson();
        String data = gson.toJson(otpverifyp);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        mprogressDialog = ProgressDialog.show(context, "", getString(R.string.Resend_Otp), true);
        APIClient.getClient().create(JubiForm_API.class).resendOtp(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    String success = jsonObject.optString("success");
                    if (Integer.valueOf(success) == 1) {
                        String message = jsonObject.optString("message");
                        Intent intent = new Intent(OtpScreen.this, OtpScreen.class);
                        startActivity(intent);
                        finish();

                    } else {

                        mprogressDialog.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (mprogressDialog.isShowing()) {
                    mprogressDialog.dismiss();
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void OtpVerify(String mobile, String otp, String user_id) {
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
                    jsonObject = new JSONObject(response.body().toString());
                    String status_ = jsonObject.optString("status");
                    String message_ = jsonObject.optString("message");
                    String user_id_ = jsonObject.optString("user_id");
                    if (status_.equals("1")) {
                        if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
                            Intent intent = new Intent(context, RegistrationListActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(context, "Registration has been done successfully now you can login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginScreenActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        Log.e("OTPSCREEN====", "Data : ," + status_ + "," + message_ + "," + user_id_);
                        /*Intent intent = new Intent(context, ChangePassword.class);
                        intent.putExtra("role_id", user_id_);
                        intent.putExtra("fromOtp","1");
                        startActivity(intent);
                        finish();*/

                    } else {
                        Toast.makeText(context, "Invalid Otp !", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("OTP SCREEN ", "====" + t.getMessage());
            }
        });

    }
}