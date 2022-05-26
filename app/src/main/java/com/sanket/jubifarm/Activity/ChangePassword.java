package com.sanket.jubifarm.Activity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Modal.ChangePasswordPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sanket.jubifarm.R.string.Please_Check_Password;
import static com.sanket.jubifarm.R.string.Please_enter_correct_Confirm_password;
import static com.sanket.jubifarm.R.string.Please_enter_correct_password;

public class ChangePassword extends AppCompatActivity {
    private Context context = this;
    private ProgressDialog dialog;

    @BindView(R.id.reg_password)
    EditText reg_password;
    @BindView(R.id.reg_conf_password)
    EditText reg_conf_password;
    @BindView(R.id.submit_password)
    Button submit_password;
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        dialog = new ProgressDialog(context);
        sharedPrefHelper = new SharedPrefHelper(context);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.change_password)  + "</font>"));

        Intent intent = getIntent();
        String userid_ = sharedPrefHelper.getString("user_id", "");

        submit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 =  reg_password.getText().toString();
                if (password1.isEmpty()){
                    Toast.makeText(context, Please_enter_correct_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                String password2 =  reg_conf_password.getText().toString();
                if (password2.isEmpty()){
                    Toast.makeText(context, Please_enter_correct_Confirm_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password1.equals(password2)){
                    ChangePassword(password2, userid_);
                }else{
                    Toast.makeText(context, Please_Check_Password, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater= getMenuInflater();
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
            Intent intentHome = new Intent(ChangePassword.this, HomeAcivity.class);
            startActivity(intentHome);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void ChangePassword(String password, String user_id) {
        dialog = ProgressDialog.show(context, "", getString(R.string.Please_Wait), true);

        ChangePasswordPojo changpass = new ChangePasswordPojo();
        changpass.setPassword(password);
        changpass.setUser_id(user_id);
        changpass.setRole_id(sharedPrefHelper.getString("role_id", ""));

        Gson mGson = new Gson();
        String data = mGson.toJson(changpass);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        APIClient.getClient().create(JubiForm_API.class).changePassword(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().toString());
                    dialog.dismiss();
                    String status_ = jsonObject.optString("status");
                    String message_ = jsonObject.optString("message");
                    if (status_.equals("1")) {
                        if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
                            Intent intent = new Intent(context, RegistrationListActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(context, LoginScreenActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("OTP SCREEN ","===="+t.getMessage());
                dialog.dismiss();
            }
        });

    }
}