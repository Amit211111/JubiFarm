package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.sanket.jubifarm.R;
import com.shamanland.fab.FloatingActionButton;

public class PS_NeemPlantationList extends AppCompatActivity {

  ImageButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps_neem_plantation_list);
        getSupportActionBar().setTitle("Neem Plantation List");
        fab=findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(PS_NeemPlantationList.this,com.sanket.jubifarm.Livelihood.NeemPlantation.class);
                startActivity(intent);
            }
        });
    }
}