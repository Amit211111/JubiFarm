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
import com.sanket.jubifarm.Livelihood.Model.CandidatePojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class CandidateList extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<CandidatePojo> arrayList = new ArrayList<>();
    SqliteHelper sqliteHelper;
    ImageView Landholding_filter;

    Context context = this;


    ImageButton addskill,addtraining;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_tracking_list);
        getSupportActionBar().setTitle("List of Trained Person");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                Intent intent=new Intent(CandidateList.this, AddCandidateActivity.class);
                startActivity(intent);
            }
        });


    }
}