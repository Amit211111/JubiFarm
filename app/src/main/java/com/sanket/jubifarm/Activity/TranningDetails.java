package com.sanket.jubifarm.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Modal.AttendanceModal;
import com.sanket.jubifarm.Modal.TrainingAttandancePojo;
import com.sanket.jubifarm.Modal.TrainingPojo;
import com.sanket.jubifarm.R;
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

public class TranningDetails extends AppCompatActivity {
    TextView training_Header, training_Date, tranner_dest, training_mobileno, training_fromtime, training_totime, training_Location, training_toDate, training_fromdae, tv_trannername;
    TextView tv_iwillAttend, tv_view_attendance;
    AttendanceModal AtndModal;
    TrainingAttandancePojo tranningAttendancePojo;
    ArrayList<TrainingAttandancePojo> attandancePojos = new ArrayList<>();
    TrainingAttandancePojo trainingAttandancePojos;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    private ArrayList<TrainingPojo> trainingPojoArrayList;
    String id, userID;
    String training_attendance_id, trainingAttendee="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranning_details);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.training_details) + "</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        InizIDS();
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);
        trainingPojoArrayList = new ArrayList<>();
        AtndModal = new AttendanceModal("");
        tranningAttendancePojo = new TrainingAttandancePojo();
        trainingAttandancePojos = new TrainingAttandancePojo();
        String rol_id = sharedPrefHelper.getString("role_id", "");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = getIntent().getStringExtra("id");
            userID = getIntent().getStringExtra("userID");
            training_Header.setText(getIntent().getStringExtra("TRANNINGHEADER"));
            training_toDate.setText(getIntent().getStringExtra("TO_DATE"));
            training_fromdae.setText(getIntent().getStringExtra("FROM_DATE"));
            training_fromtime.setText(getIntent().getStringExtra("FROM_TIME"));
            training_totime.setText(getIntent().getStringExtra("TO_TIME"));
            training_Location.setText(" " + getIntent().getStringExtra("LOCATION"));
            training_mobileno.setText(getIntent().getStringExtra("MOBILENO"));

        }

        if (Integer.valueOf(rol_id) == 4) {
            tv_iwillAttend.setVisibility(View.GONE);
            tv_view_attendance.setVisibility(View.VISIBLE);
        } else if (Integer.valueOf(rol_id) == 5) {
            tv_view_attendance.setVisibility(View.GONE);

        }

        if (sharedPrefHelper.getString("user_type", "").equals("Farmer")) {
            trainingAttandancePojos = sqliteHelper.getTrainingAttendanceData(sharedPrefHelper.getString("farmer_id", ""), id);
            if (trainingAttandancePojos.getTraining_id()!= null) {
                if(trainingAttandancePojos.getFlag()!= null){
                    if (trainingAttandancePojos.getFlag().equals("1")) {
                    tv_iwillAttend.setBackgroundColor(Color.GRAY);
                    tv_iwillAttend.setEnabled(false);
                    }
                } else if ( trainingAttandancePojos.getStatus().equals("Approve") || trainingAttandancePojos.getStatus().equals("Pending")) {
                    tv_iwillAttend.setBackgroundColor(Color.GRAY);
                    tv_iwillAttend.setEnabled(false);
                }
            }
        }

        iWillAttend();

        OnlyCrishmitrVisiable();
    }

    private void iWillAttend() {
        tv_iwillAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // trainingPojoArrayList= sqliteHelper.getTrainingData();

                tranningAttendancePojo.setTraining_id(id);
                tranningAttendancePojo.setFarmer_id(sharedPrefHelper.getString("farmer_id", ""));
                tranningAttendancePojo.setUser_id(sharedPrefHelper.getString("user_id", ""));
                sqliteHelper.svaeTraning_Attendance(tranningAttendancePojo);

                if (isInternetOn()) {
                    attandancePojos = sqliteHelper.getTrainingAttendanceforsync();
                    for (int i = 0; i < attandancePojos.size(); i++) {
                        Gson gson = new Gson();
                        String data = gson.toJson(attandancePojos.get(i));
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(JSON, data);
                        Log.e("========", "" + data);
                        training_attendance(body, attandancePojos.get(i).getLocal_id());
                    }


                } else {
                    Intent vintent = new Intent(TranningDetails.this, TrainingActivity.class);
                    startActivity(vintent);
                    finish();
                }
            }
        });
    }

    private void OnlyCrishmitrVisiable() {
        tv_view_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vintent = new Intent(TranningDetails.this, View_Attendance.class);
                vintent.putExtra("userID", id);
                startActivity(vintent);
                finish();
            }
        });
    }

    private void InizIDS() {
        training_Header = findViewById(R.id.training_Header);
        training_toDate = findViewById(R.id.training_toDate);
        training_fromtime = findViewById(R.id.training_fromtime);
        training_totime = findViewById(R.id.training_totime);
        training_fromdae = findViewById(R.id.training_fromdae);
        tv_trannername = findViewById(R.id.tv_trannername);
        training_mobileno = findViewById(R.id.training_mobileno);
        training_Location = findViewById(R.id.training_Location);
        tranner_dest = findViewById(R.id.tranner_dest);
        tv_iwillAttend = findViewById(R.id.tv_iwillAttend);
        tv_view_attendance = findViewById(R.id.tv_view_attendance);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void training_attendance(RequestBody body, String local_id) {
        APIClient.getClient().create(JubiForm_API.class).training_attendance(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    Log.e("subp", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    if (Integer.valueOf(status) == 1) {

                        tv_iwillAttend.setBackgroundColor(Color.GRAY);
                        tv_iwillAttend.setClickable(false);



                        String message = jsonObject.optString("message");
                        training_attendance_id = jsonObject.optString("training_attendance_id");
                        sqliteHelper.updateServerid("training_attendance", Integer.parseInt(local_id), Integer.parseInt(training_attendance_id));
                        sqliteHelper.updateLocalFlag("training_attendance", Integer.parseInt(local_id), 1);
                        Intent vintent = new Intent(TranningDetails.this, TrainingActivity.class);

                        startActivity(vintent);
                        finish();
                        Toast.makeText(TranningDetails.this, ""+ message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TranningDetails.this, "Server error", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
               /* if (dialog.isShowing()) {
                    dialog.dismiss();
                }*/
            }
        });
    }

    private boolean isInternetOn() {
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

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
}