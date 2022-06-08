package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.sanket.jubifarm.Livelihood.Model.CandidatePojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

public class CandidateProfileActivity extends AppCompatActivity {

    TextView skill_center,tv_name,tv_email,tv_mobileno,tv_qualification,tv_training_stream,tv_training_date;
    private String skill_centerr = "", name = "", email = "", id = "",mobile = "", qualification = "", training_stream = "", bulk_density = "", land_name,
            date_of_training = "", soil_texture = "", ph = "",image="";
    CandidatePojo candidatePojo;
    SqliteHelper sqliteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_tracking_list_view);
        getSupportActionBar().setTitle("View Data Trained Person");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        IntilizeAll();

        /*get intent values here*/
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





    }
    private void IntilizeAll()
    {
        sqliteHelper=new SqliteHelper(this);
        skill_center=findViewById(R.id.skill_center);
        tv_name=findViewById(R.id.tv_name);
        tv_email=findViewById(R.id.tv_email);
        tv_mobileno=findViewById(R.id.tv_mobileno);
        tv_qualification=findViewById(R.id.tv_qualification);
        tv_training_stream=findViewById(R.id.tv_training_stream);
        tv_training_date=findViewById(R.id.tv_training_date);

    }
    private void setTextValues()
    {
        skill_center.setText(skill_centerr);
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
                Intent intent = new Intent(CandidateProfileActivity.this, CandidateList.class);
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