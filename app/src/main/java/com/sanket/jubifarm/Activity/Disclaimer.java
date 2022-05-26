package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.sanket.jubifarm.Modal.DisclaimerPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.HashMap;

public class Disclaimer extends AppCompatActivity {

    HashMap<String,Integer> hashMap;
    SqliteHelper db;
    TextView tv_disclaimer;

    DisclaimerPojo pojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.disclaimer_title) + "</font>"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_disclaimer = findViewById(R.id.tv_disclaimer);
        pojo = new DisclaimerPojo();
        db=new SqliteHelper(this);
        hashMap = new HashMap<>();
        SharedPrefHelper spf = new SharedPrefHelper(Disclaimer.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            pojo = db.getdisclaimer(2, 2);
        }else if(language.equalsIgnoreCase("kan"))
        {
            pojo = db.getdisclaimer(3, 3);
        }
        else
        {
            pojo = db.getdisclaimer(1, 1);
        }
        String disc = pojo.getDescription();
        tv_disclaimer.setText(" "+disc);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home_menu) {

            Intent intent = new Intent(this, HomeAcivity.class);
            this.startActivity(intent);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            Intent intentHome = new Intent(Disclaimer.this, HomeAcivity.class);
            startActivity(intentHome);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}