package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sanket.jubifarm.Livelihood.Model.CandidatePojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCandidateActivity extends AppCompatActivity {


    //    String [] skill_center={"Select Center","Delhi","Noida","Hyderabad","Mumbai","Kolkata","Munirka"};
    // String [] training_stream={"Select Training Stream","Java","Python","C#.Net","Php","Other"};
    Spinner spn_skill_center;
    EditText et_training_stream, et_name, et_email, et_mobileno, et_qualification, et_date_completation;
    Button submit;
    SqliteHelper sqliteHelper;
    CandidatePojo candidatePojo;
    int mYear, mMonth, mDay, year, month, day;
    DatePickerDialog datePickerDialog;
    SharedPrefHelper sharedPrefHelper;
    ArrayList<String> SkillCenterArrayList = new ArrayList<>();
    HashMap<String, Integer> SkillCenterHM = new HashMap<>();
    int skill_centerHM = 0;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_tracking);
        getSupportActionBar().setTitle("Add Candidate");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // IntilizeAll
        IntilizeAll();
//        ArrayAdapter adapter = new ArrayAdapter(AddCandidateActivity.this, R.layout.spinner_list, skill_center);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spn_skill_center.setAdapter(adapter);


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
        getSkillCenterSpinner();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    Random random = new Random();
                    int value = random.nextInt(1000);

                    candidatePojo = new CandidatePojo();
                    candidatePojo.setName(et_name.getText().toString().trim());
                    candidatePojo.setEmail(et_email.getText().toString().trim());
                    candidatePojo.setQualification(et_qualification.getText().toString().trim());
                    candidatePojo.setMobileno(et_mobileno.getText().toString().trim());
                    candidatePojo.setUser_id(sharedPrefHelper.getString("user_id", ""));
                    candidatePojo.setDate_of_completion_of_training(et_date_completation.getText().toString().trim());
                    candidatePojo.setTraining_stream(et_training_stream.getText().toString().trim());
                    candidatePojo.setSkill_center(spn_skill_center.getSelectedItem().toString().trim());
                    candidatePojo.setLatitude(sharedPrefHelper.getString("LAT", ""));
                    candidatePojo.setLongitude(sharedPrefHelper.getString("LONG", ""));


                    sqliteHelper.SkillTracking(candidatePojo);

                    Intent intent = new Intent(AddCandidateActivity.this, CandidateList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

        });
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!et_name.getText().toString().trim().matches("[a-zA-Z ]+")) {
            EditText flagEditfield = et_name;
            String msg = getString(R.string.Please_Enter_Name);
            et_name.setError(msg);
            et_name.requestFocus();
            return false;
        }
        if (et_mobileno.getText().toString().trim().length() > 10) {
            EditText flagEditfield = et_mobileno;
            String msg = getString(R.string.Please_Enter_Valid_Contact_Number);
            et_mobileno.setError(msg);
            et_mobileno.requestFocus();
            return false;
        }
        if (!et_qualification.getText().toString().trim().matches("[a-zA-Z ]+")) {
            EditText flagEditfield = et_qualification;
            String msg = getString(R.string.Please_Enter_Qualification);
            et_qualification.setError(msg);
            et_qualification.requestFocus();
            return false;
        }
        if (!et_training_stream.getText().toString().trim().matches("[a-zA-Z ]+")) {
            EditText flagEditfield = et_training_stream;
            String msg = getString(R.string.Please_Enter_Training_Name);
            et_training_stream.setError(msg);
            et_training_stream.requestFocus();
            return false;
        }

//        if (!et_date_completation.getText().toString().trim().matches("[a-zA-Z ]+")) {
//            EditText flagEditfield = et_date_completation;
//            String msg = getString(R.string.Please_Enter_Complitation_Date);
//            et_date_completation.setError(msg);
//            et_date_completation.requestFocus();
//            return false;
//        }

        if (et_email.getText().toString().trim().length() == 0) {
            Toast.makeText(context, R.string.Please_Enter_Valid_email_id, Toast.LENGTH_SHORT).show();
            return false;
        }
        return ret;
    }



    private void IntilizeAll()
    {
        et_training_stream =findViewById(R.id.et_training_stream);
        spn_skill_center =findViewById(R.id.spn_skill_center);
        et_name =findViewById(R.id.et_name);
        et_email =findViewById(R.id.et_email);
        et_mobileno =findViewById(R.id.et_mobileno);
        et_qualification=findViewById(R.id.et_qualification);
        et_date_completation=findViewById(R.id.et_date_completation);
        submit =findViewById(R.id.submit);
        sharedPrefHelper = new SharedPrefHelper(this);

    }
    private void getSkillCenterSpinner() {

        SkillCenterArrayList.clear();
        SkillCenterHM = sqliteHelper.getSkillCenter();
        for (int i = 0; i < SkillCenterHM.size(); i++) {
            SkillCenterArrayList.add(SkillCenterHM.keySet().toArray()[i].toString().trim());
        }
//        Collections.sort(blockArrayList);
        SkillCenterArrayList.add(0, "Select Skill Center");
        //state spinner choose
        ArrayAdapter skill_center_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, SkillCenterArrayList);
        skill_center_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_skill_center.setAdapter(skill_center_adapter);
//        if (screen_type.equals("edit_profile")) {
//            st_religion = sqliteHelper.getPSReligion(editparyavaranSakhiRegistrationPojo.getReligion_id());
//            int pos = religion_adapter.getPosition(st_religion.trim());
//            religion.setSelection(pos);
//        }

        spn_skill_center.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                skill_centerHM = 0;
                if (!spn_skill_center.getSelectedItem().toString().trim().equalsIgnoreCase("Select Skill Center")) {
                    if (spn_skill_center.getSelectedItem().toString().trim() != null) {
                        skill_centerHM = SkillCenterHM.get(spn_skill_center.getSelectedItem().toString().trim());
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                Intent intent = new Intent(AddCandidateActivity.this, CandidateList.class);
                startActivity(intent);
                return true;
            case R.id.home_menu:
                Intent intent1 = new Intent(this,SkillTrackingMenuActivity.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}