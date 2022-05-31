package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.sanket.jubifarm.Livelihood.Model.SkillTrackingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

public class SkillTrackingListView extends AppCompatActivity {

    TextView skill_center,tv_name,tv_email,tv_mobileno,tv_qualification,tv_training_stream,tv_training_date;
    private String skill_centerr = "", name = "", email = "", id = "",mobile = "", qualification = "", training_stream = "", bulk_density = "", land_name,
            date_of_training = "", soil_texture = "", ph = "",image="";
    SkillTrackingPojo skillTrackingPojo;
    SqliteHelper sqliteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_tracking_list_view);
        getSupportActionBar().setTitle("View Data Trained Person");
        IntilizeAll();

        /*get intent values here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            id = bundle.getString("id", "");
            name = bundle.getString("namee", "");
            skillTrackingPojo = sqliteHelper.PSSkillTrackingDetail(id,name);
            if (id.equals("")) {
                id = skillTrackingPojo.getId();
            }
            skill_centerr = skillTrackingPojo.getSkill_center();
            email= skillTrackingPojo.getEmail();
            mobile=skillTrackingPojo.getMobileno();
            qualification=skillTrackingPojo.getQualification();
            training_stream=skillTrackingPojo.getTraining_stream();
            date_of_training=skillTrackingPojo.getDate_of_completion_of_training();
            name=skillTrackingPojo.getName();



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

}