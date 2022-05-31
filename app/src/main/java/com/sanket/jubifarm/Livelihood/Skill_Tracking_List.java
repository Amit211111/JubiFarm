package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.sanket.jubifarm.Livelihood.Adapter.AdapterSkillCenter;
import com.sanket.jubifarm.Livelihood.Model.SkillTrackingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class Skill_Tracking_List extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<SkillTrackingPojo> arrayList = new ArrayList<>();
    SqliteHelper sqliteHelper;
    ImageView Landholding_filter;

    Context context = this;


    ImageButton addskill,addtraining;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_tracking_list);
        getSupportActionBar().setTitle("List of Trained Person");
        sqliteHelper = new SqliteHelper(this);
        recyclerView = findViewById(R.id.rv1);
        Landholding_filter = findViewById(R.id.Landholding_filter);


        arrayList = sqliteHelper.getPsSkillTrackingData();
        AdapterSkillCenter adapterSkillCenter = new AdapterSkillCenter(context, arrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterSkillCenter);




        addskill=findViewById(R.id.addskill);


        addskill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Skill_Tracking_List.this,SkillTracking.class);
                startActivity(intent);
            }
        });


    }
}