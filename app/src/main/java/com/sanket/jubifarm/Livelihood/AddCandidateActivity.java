package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.sanket.jubifarm.Livelihood.Model.CandidatePojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.Calendar;

public class AddCandidateActivity extends AppCompatActivity {


    String [] skill_center={"Select Center","Delhi","Noida","Hyderabad","Mumbai","Kolkata","Munirka"};
    String [] training_stream={"Select Training Stream","Java","Python","C#.Net","Php","Other"};
    Spinner spn_training_stream,spn_skill_center;
    EditText et_name,et_email,et_mobileno,et_qualification,et_date_completation;
    Button submit;
    SqliteHelper sqliteHelper;
    CandidatePojo candidatePojo;
    int mYear,mMonth,mDay,year,month,day;
    DatePickerDialog datePickerDialog;
    SharedPrefHelper sharedPrefHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_tracking);
        getSupportActionBar().setTitle("Add Candidate");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // IntilizeAll
        IntilizeAll();

        ArrayAdapter adapter = new ArrayAdapter(AddCandidateActivity.this, R.layout.spinner_list, skill_center);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_skill_center.setAdapter(adapter);


        ArrayAdapter adapter1 = new ArrayAdapter(AddCandidateActivity.this, R.layout.spinner_list, training_stream);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spn_training_stream.setAdapter(adapter1);

        et_date_completation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_date_completation.setError(null);
                et_date_completation.clearFocus();
                mYear = year;
                mMonth = month;
                mDay = day;

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR); // current year
                mMonth = c.get(Calendar.MONTH); // current month
                mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(AddCandidateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                et_date_completation.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#ff173e6d"));
            }

        });


        sqliteHelper = new SqliteHelper(getApplicationContext());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                candidatePojo = new CandidatePojo();
                candidatePojo.setName(et_name.getText().toString().trim());
                candidatePojo.setEmail(et_email.getText().toString().trim());
                candidatePojo.setQualification(et_qualification.getText().toString().trim());
                candidatePojo.setMobileno(et_mobileno.getText().toString().trim());
                candidatePojo.setDate_of_completion_of_training(et_date_completation.getText().toString().trim());
                candidatePojo.setTraining_stream(spn_training_stream.getSelectedItem().toString().trim());
                candidatePojo.setSkill_center(spn_skill_center.getSelectedItem().toString().trim());
                candidatePojo.setLatitude(sharedPrefHelper.getString("LAT","")+", "+sharedPrefHelper.getString("LONG",""));
                candidatePojo.setLongitude(sharedPrefHelper.getString("LAT","")+", "+sharedPrefHelper.getString("LONG",""));


                sqliteHelper.SkillTracking(candidatePojo);

                Intent intent = new Intent(AddCandidateActivity.this, CandidateList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
    private void IntilizeAll()
    {
        spn_training_stream =findViewById(R.id.spn_training_stream);
        spn_skill_center =findViewById(R.id.spn_skill_center);
        et_name =findViewById(R.id.et_name);
        et_email =findViewById(R.id.et_email);
        et_mobileno =findViewById(R.id.et_mobileno);
        et_qualification=findViewById(R.id.et_qualification);
        et_date_completation=findViewById(R.id.et_date_completation);
        submit =findViewById(R.id.submit);
        sharedPrefHelper = new SharedPrefHelper(this);

    }
}