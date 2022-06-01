package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

public class TrainningSurveyForm extends AppCompatActivity {

    String [] state1 ={"Select State","Bihar","Delhi","Mumbai","Telangana","Noida"};
    Spinner state;
    EditText name,address,mobileno,fathername,accupation;
    Button submit;
    SqliteHelper sqliteHelper;
    //AddTraineeSurveyPojo addTraineeSurveyPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainning_survey_form);

        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        mobileno=findViewById(R.id.mobileno);
        fathername=findViewById(R.id.fathername);
        accupation=findViewById(R.id.accupation);
        state =findViewById(R.id.state);

        ArrayAdapter adapter=new ArrayAdapter(TrainningSurveyForm.this, R.layout.spinner_list,state1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(adapter);

        submit=findViewById(R.id.submit);
       // sqliteDatabase = new SqliteDatabase(getApplicationContext());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                addTraineeSurveyPojo = new AddTraineeSurveyPojo();
//                addTraineeSurveyPojo.setName(name.getText().toString().trim());
//                addTraineeSurveyPojo.setMobileno(mobileno.getText().toString().trim());
//                addTraineeSurveyPojo.setAddress(address.getText().toString().trim());
//                addTraineeSurveyPojo.setState(state.getSelectedItem().toString().trim());
//                addTraineeSurveyPojo.setFather(father.getText().toString().trim());
//                addTraineeSurveyPojo.setOccupation(occupation.getText().toString().trim());
//
//                sqliteDatabase.saveHousehold2(addTraineeSurveyPojo);

                Intent intent=new Intent(TrainningSurveyForm.this, CandidateList.class);
                startActivity(intent);

            }
        });
    }
}