package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.sanket.jubifarm.Livelihood.Adapter.AdapterSkillCenter;
import com.sanket.jubifarm.Livelihood.Adapter.Adapter_PS_LandHolding;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Livelihood.Model.SkillTrackingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class Skill_Tracking_List extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<SkillTrackingPojo> arrayList = new ArrayList<>();
    SqliteHelper sqliteHelper;

    Context context = this;


    ImageButton addskill,addtraining;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_tracking_list);

        sqliteHelper = new SqliteHelper(this);
        recyclerView = findViewById(R.id.rv1);


        arrayList = sqliteHelper.getRegistrationData1();
        AdapterSkillCenter adapterSkillCenter = new AdapterSkillCenter(context, arrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterSkillCenter);




        addskill=findViewById(R.id.addskill);
        addtraining=findViewById(R.id.addtraining);

        addskill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Skill_Tracking_List.this,SkillTracking.class);
                startActivity(intent);
            }
        });

        addtraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Skill_Tracking_List.this,TrainningSurveyForm.class);
                startActivity(intent);
            }
        });
    }
}