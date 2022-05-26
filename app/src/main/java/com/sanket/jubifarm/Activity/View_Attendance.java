package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sanket.jubifarm.Adapter.TrainingAdapter;
import com.sanket.jubifarm.Adapter.View_Attendance_Adapter;
import com.sanket.jubifarm.Modal.AttendanceModal;
import com.sanket.jubifarm.Modal.TrainingAttandancePojo;
import com.sanket.jubifarm.Modal.TrainingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.List;

public class View_Attendance extends AppCompatActivity {
 View_Attendance_Adapter attendance_adapter;
 RecyclerView rv_attendance;
 AttendanceModal attendmodal;
    SqliteHelper sqliteHelper;
    private ArrayList<TrainingAttandancePojo> attendArrayList;
    TrainingAttandancePojo TrainingAttandancePojo;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__attendance);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + "View Attendance" + "</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sqliteHelper=new SqliteHelper(this);
        rv_attendance = findViewById(R.id.rv_attendance);


        attendmodal = new AttendanceModal("");
        attendArrayList = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = getIntent().getStringExtra("userID");
        }
        attendArrayList= sqliteHelper.getTrainingAttendance(userID);
        attendance_adapter  = new View_Attendance_Adapter(this, attendArrayList,userID);
        rv_attendance.setHasFixedSize(true);
        rv_attendance.setLayoutManager(new LinearLayoutManager(this));
        rv_attendance.setAdapter(attendance_adapter);

    }
    public boolean onCreateOptionsMenu(Menu menu) {
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
            Intent trHome = new Intent(View_Attendance.this, TranningDetails.class);
            startActivity(trHome);
            finish();
            // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent trHome = new Intent(View_Attendance.this, TranningDetails.class);
        startActivity(trHome);
        finish();
    }
}