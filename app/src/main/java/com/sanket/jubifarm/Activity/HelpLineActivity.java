package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;

public class HelpLineActivity extends AppCompatActivity {
TextView click;
SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_line);

        setTitle(Html.fromHtml("<font color=\"#000000\">" +getString(R.string.HELPLINE)+ "</font>"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        click=findViewById( R.id.click);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Regintent = new Intent(HelpLineActivity.this, QueryListActivity.class);
                startActivity(Regintent);
                finish();

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeAcivity.class);
        this.startActivity(intent);
        finish();
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
            Intent intent = new Intent(this, HomeAcivity.class);
            this.startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}