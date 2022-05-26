package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.security.keystore.SecureKeyImportUnavailableException;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.sanket.jubifarm.Livelihood.Model.SkillTrackingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

public class SkillTracking extends AppCompatActivity {
    String [] sp_skill ={"Select Skill Center Name","Hyderabad","Delhi","Mumbai","Patna","Noida"};

    String [] sp_state ={"Select State","Bihar","Delhi","Mumbai","Telangana","Noida"};

    String [] sp_district={"Select District","Madhubani","Darbhanga","Saharsa","Patna","Muzaffarpur"};

    String [] sp_village ={"Select Village","Hulasspatti","Mahdeva","Khaira","Sijoul","Inrwa","Phulpras"};

    Spinner skill,state,district,village;
    EditText address,contact,mobileno,latitude,longitude,pincode;
    Button submit;
    SqliteHelper sqliteHelper;
    SkillTrackingPojo skillTrackingPojo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_tracking);
        getSupportActionBar().setTitle("Skill Center Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        skill =findViewById(R.id.skill);
        ArrayAdapter adapter3=new ArrayAdapter(SkillTracking.this, R.layout.spinner_list,sp_skill);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        skill.setAdapter(adapter3);

        state =findViewById(R.id.state);

        ArrayAdapter adapter=new ArrayAdapter(SkillTracking.this, R.layout.spinner_list,sp_state);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(adapter);

        district =findViewById(R.id.district);
        ArrayAdapter adapter1=new ArrayAdapter(SkillTracking.this, R.layout.spinner_list,sp_district);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district.setAdapter(adapter1);

        village =findViewById(R.id.village);
        ArrayAdapter adapter2=new ArrayAdapter(SkillTracking.this, R.layout.spinner_list,sp_village);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        village.setAdapter(adapter2);


        address =findViewById(R.id.address);
        contact =findViewById(R.id.contact);
        mobileno =findViewById(R.id.mobileno);
        latitude =findViewById(R.id.latitude);
        longitude =findViewById(R.id.longitude);
        pincode=findViewById(R.id.pincode);

        submit =findViewById(R.id.submit);
        sqliteHelper = new SqliteHelper(getApplicationContext());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                skillTrackingPojo = new SkillTrackingPojo();
                skillTrackingPojo.setSkill(skill.getSelectedItem().toString().trim());
                skillTrackingPojo.setAddress(address.getText().toString().trim());
                skillTrackingPojo.setContact(contact.getText().toString().trim());
                skillTrackingPojo.setMobileno(mobileno.getText().toString().trim());
                skillTrackingPojo.setLatitude(latitude.getText().toString().trim());
                skillTrackingPojo.setLongitude(longitude.getText().toString().trim());
                skillTrackingPojo.setState(state.getSelectedItem().toString().trim());
                skillTrackingPojo.setDistrict(district.getSelectedItem().toString().trim());
                skillTrackingPojo.setVillage(village.getSelectedItem().toString().trim());


                sqliteHelper.SkillTracking(skillTrackingPojo);

                Intent intent=new Intent(SkillTracking.this, Skill_Tracking_List.class);
                startActivity(intent);
            }
        });

    }
}