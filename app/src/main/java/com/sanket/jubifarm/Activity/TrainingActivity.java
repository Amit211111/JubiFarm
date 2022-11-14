package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sanket.jubifarm.Adapter.TrainingAdapter;
import com.sanket.jubifarm.Modal.TrainingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TrainingActivity extends AppCompatActivity {
    RecyclerView rv_training;
    TrainingAdapter trainingAdapter;
    String format ;
    private ArrayList<TrainingPojo> trainingPojoArrayList;
    TextView traiCount,search,cancel;
    SqliteHelper sqliteHelper;
    ImageButton Button;
    TextView tra_tab_knowledge,tra_tab_Tranning;
    private TextView et_todate,et_fromdate;
    private TextView et_totime,et_fromtime;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timepickerdialog;
    SharedPrefHelper sharedPrefHelper;
    int yy;
    int mm;
    int dd;
    String from_date="",to_date="";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.Tranning) + "</font>"));

        Button = findViewById(R.id.fab);
        tra_tab_knowledge = findViewById(R.id.tra_tab_knowledge);
        tra_tab_Tranning = findViewById(R.id.tra_tab_Tranning);
        et_todate = findViewById(R.id.et_todate);
       et_fromdate = findViewById(R.id.et_fromdates);
        search = findViewById(R.id.search);
        cancel = findViewById(R.id.cancel);
     //   et_totime = findViewById(R.id.et_totim);
     //   et_fromtime = findViewById(R.id.et_fromtime);
        traiCount = findViewById(R.id.traiCount);
        sqliteHelper=new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        String rol_id = sharedPrefHelper.getString("role_id", "");

    if (Integer.valueOf(rol_id)==5){
            Button.setVisibility(View.GONE);
        }

        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent Regintent = new Intent(TrainingActivity.this, AddTranner.class);
                startActivity(Regintent);
                finish();
            }
        });
        tra_tab_knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Regintent = new Intent(TrainingActivity.this, KnowledgeActivity.class);
                startActivity(Regintent);
                finish();

            }
        });
        et_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(TrainingActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if (monthOfYear<=8 && dayOfMonth<=9){
                                    to_date = "" + year  + "-0" + (monthOfYear + 1) + "-0" +dayOfMonth;
                                }else if (monthOfYear<=8){
                                    to_date = "" + year  + "-0" + (monthOfYear + 1) + "-" +dayOfMonth;
                                }else if (dayOfMonth<=9){
                                    to_date = "" + year  + "-" + (monthOfYear + 1) + "-0" +dayOfMonth;
                                }else {
                                    to_date = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                et_todate.setText(to_date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });




        et_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(TrainingActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                if (monthOfYear<=8 && dayOfMonth<=9){
                                    from_date = "" + year  + "-0" + (monthOfYear + 1) + "-0" +dayOfMonth;
                                }else if (monthOfYear<=8){
                                    from_date = "" + year  + "-0" + (monthOfYear + 1) + "-" +dayOfMonth;
                                }else if (dayOfMonth<=9){
                                    from_date = "" + year  + "-" + (monthOfYear + 1) + "-0" +dayOfMonth;
                                }else {
                                    from_date = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                et_fromdate.setText(from_date);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });



    /*    et_fromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final   Calendar c = Calendar.getInstance();
                int  CalendarHour = c.get(Calendar.HOUR_OF_DAY);
                int CalendarMinute = c.get(Calendar.MINUTE);
                timepickerdialog = new TimePickerDialog(TrainingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                               et_fromtime.setText(hourOfDay + ":" + minute +" "+ format);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();

            }
        });

        et_totime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final   Calendar c = Calendar.getInstance();
                int  CalendarHour = c.get(Calendar.HOUR_OF_DAY);
                int CalendarMinute = c.get(Calendar.MINUTE);
                timepickerdialog = new TimePickerDialog(TrainingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                et_totime.setText(hourOfDay + ":" + minute +" "+ format);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();

            }
        });  */

        rv_training = findViewById(R.id.rv_training);
        trainingPojoArrayList= sqliteHelper.getTrainingData();
        trainingAdapter  = new TrainingAdapter(this, trainingPojoArrayList);
        int counter = trainingPojoArrayList.size();
        traiCount.setText(getString(R.string.Total_Training) +": "+counter);
        rv_training.setHasFixedSize(true);
        rv_training.setLayoutManager(new LinearLayoutManager(this));
        rv_training.setAdapter(trainingAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_fromdate.setText("");
                et_todate.setText("");
                trainingPojoArrayList= sqliteHelper.getTrainingData();
                trainingAdapter  = new TrainingAdapter(TrainingActivity.this, trainingPojoArrayList);
                int counter = trainingPojoArrayList.size();
                traiCount.setText(getString(R.string.Total_Training) +": "+counter);
                rv_training.setHasFixedSize(true);
                rv_training.setLayoutManager(new LinearLayoutManager(TrainingActivity.this));
                rv_training.setAdapter(trainingAdapter);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                trainingPojoArrayList= sqliteHelper.getTrainingDataSearch(from_date,to_date);
                trainingAdapter  = new TrainingAdapter(TrainingActivity.this, trainingPojoArrayList);
                rv_training.setHasFixedSize(true);
                rv_training.setLayoutManager(new LinearLayoutManager(TrainingActivity.this));
                rv_training.setAdapter(trainingAdapter);
            }
        });



    }

    @Override
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
