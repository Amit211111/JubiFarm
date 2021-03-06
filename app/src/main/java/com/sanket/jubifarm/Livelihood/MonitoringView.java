package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.sanket.jubifarm.Livelihood.Model.MonitoringStatusPojo;
import com.sanket.jubifarm.Livelihood.Model.CandidatePojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.Calendar;

public class MonitoringView extends AppCompatActivity {
    String [] sp_current_work={"Select Current Work","Employed","Non Employed"};

    Spinner spn_current_work;
    EditText et_date_of_monitoring;
     RadioGroup rg_working;
     RadioButton rb_working,rb_notworking;
    TextView tv_name,tv_email,tv_mobileno,tv_qualification,tv_training_stream,tv_training_date,tv_submit;
    SharedPrefHelper sharedPrefHelper;

    EditText et_remark;
    private String skill_centerr = "", name = "", email = "", id = "",mobile = "", qualification = "", training_stream = "", bulk_density = "", land_name,
            date_of_training = "", soil_texture = "", ph = "",image="";
    CandidatePojo candidatePojo;
    SqliteHelper sqliteHelper;
   String rb_workingStatus;
    int mYear,mMonth,mDay,year,month,day;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_view);
        getSupportActionBar().setTitle("Monitoring");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sqliteHelper = new SqliteHelper(getApplicationContext());

        IntilizeAll();

        ArrayAdapter adapter=new ArrayAdapter(MonitoringView.this,R.layout.spinner_list,sp_current_work);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_current_work.setAdapter(adapter);

        /*get intent values here*/
        //View Data Other Page ....get Data
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id", "");
            name = bundle.getString("namee", "");
            candidatePojo = sqliteHelper.PSSkillTrackingDetail(id,name);
            if (id.equals("")) {
                id = candidatePojo.getId();
            }
            skill_centerr = candidatePojo.getSkill_center();
            email= candidatePojo.getEmail();
            mobile= candidatePojo.getMobileno();
            qualification= candidatePojo.getQualification();
            training_stream= candidatePojo.getTraining_stream();
            date_of_training= candidatePojo.getDate_of_completion_of_training();
            name= candidatePojo.getName();
        }
        //All Set Values
        setTextValues();

        rg_working.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_working:
                        rb_workingStatus = "Working";
                        break;
                    case R.id.rb_notworking:
                        rb_workingStatus = "Not Working";
                        break;
                }
            }
        });

        et_date_of_monitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_date_of_monitoring.setError(null);
                et_date_of_monitoring.clearFocus();
                mYear=year;
                mMonth=month;
                mDay=day;

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR); // current year
                mMonth = c.get(Calendar.MONTH); // current month
                mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(MonitoringView.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                et_date_of_monitoring.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#ff173e6d"));
            }
        });


        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonitoringStatusPojo monitoringStatusPojo = new MonitoringStatusPojo();

                monitoringStatusPojo.setWorking_status(rb_workingStatus);
                monitoringStatusPojo.setCurrent_work(spn_current_work.getSelectedItem().toString().trim());
                monitoringStatusPojo.setRemark(et_remark.getText().toString().trim());
                monitoringStatusPojo.setUser_id(sharedPrefHelper.getString("user_id",""));
                monitoringStatusPojo.setDate_monitoring(et_date_of_monitoring.getText().toString().trim());
                monitoringStatusPojo.setLatitude(sharedPrefHelper.getString("LAT","")+", "+sharedPrefHelper.getString("LONG",""));
                monitoringStatusPojo.setLongitude(sharedPrefHelper.getString("LAT","")+", "+sharedPrefHelper.getString("LONG",""));
                monitoringStatusPojo.setCandidate_id(String.valueOf(id));

                sqliteHelper.MonitoringStatus(monitoringStatusPojo);

                Intent intent = new Intent(MonitoringView.this, CandidateList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });




    }
    private void IntilizeAll()
    {
        sqliteHelper=new SqliteHelper(this);
        sharedPrefHelper=new SharedPrefHelper(this);
        tv_submit=findViewById(R.id.tv_submit);
        et_date_of_monitoring=findViewById(R.id.et_date_of_monitoring);
        spn_current_work=findViewById(R.id.spn_current_work);
        et_remark=findViewById(R.id.et_remark);
        rg_working=findViewById(R.id.rg_working);
        rb_working=findViewById(R.id.rb_working);
        rb_notworking=findViewById(R.id.rb_notworking);
        tv_name=findViewById(R.id.tv_name);
        tv_email=findViewById(R.id.tv_email);
        tv_mobileno=findViewById(R.id.tv_mobileno);
        tv_qualification=findViewById(R.id.tv_qualification);
        tv_training_stream=findViewById(R.id.tv_training_stream);
        tv_training_date=findViewById(R.id.tv_training_date);

    }
    //View Data Other Page ....Set Data
    private void setTextValues()
    {
        tv_name.setText(name);
        tv_email.setText(email);
        tv_mobileno.setText(mobile);
        tv_training_stream.setText(training_stream);
        tv_qualification.setText(qualification);
        tv_training_date.setText(date_of_training);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(MonitoringView.this, CandidateList.class);
                startActivity(intent);
                return true;
            case R.id.home_menu:
                Intent intent1 = new Intent(this,ParyavaranSakhiHome.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}