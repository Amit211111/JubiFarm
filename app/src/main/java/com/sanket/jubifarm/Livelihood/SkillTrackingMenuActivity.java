package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sanket.jubifarm.R;

public class SkillTrackingMenuActivity extends AppCompatActivity {

    CardView skill_Tracking_menu, cv_Synchronize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_tracking_menu);
        skill_Tracking_menu = findViewById(R.id.skill_Tracking_menu);
        cv_Synchronize = findViewById(R.id.cv_Synchronize);

        skill_Tracking_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SkillTrackingMenuActivity.this, Skill_Tracking_List.class);
                startActivity(intent);
            }
        });

        cv_Synchronize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SkillTrackingMenuActivity.this, STSynchronize.class);
                startActivity(intent);
            }
        });

    }
}