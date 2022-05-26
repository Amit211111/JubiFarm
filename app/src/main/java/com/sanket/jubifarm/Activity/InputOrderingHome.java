package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;

public class InputOrderingHome extends AppCompatActivity {
    private TextView tv_approved, tv_pending, tv_rejected,
            tv_vendor_referred, tv_new_input_ordering;

    /*normal widgets*/
    private Context context = this;
    private SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_ordering_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.INPUT_ORDERING_HOME) + "</font>"));

        initViews();
        setAllButtonClick();

        hideUnhideFields();
    }

    private void hideUnhideFields() {
        if (sharedPrefHelper.getString("user_type", "").equals("Farmer") ||
                sharedPrefHelper.getString("user_type", "").equals("Supplier")) {
            tv_vendor_referred.setVisibility(View.GONE);

        } if (sharedPrefHelper.getString("user_type", "").equals("Supplier")){
            tv_pending.setVisibility(View.GONE);
            tv_new_input_ordering.setText("Pending");
            tv_new_input_ordering.setBackgroundColor(getResources().getColor(R.color.color_yellow_google));

        }if (sharedPrefHelper.getString("user_type", "").equals("Farmer")){
            tv_vendor_referred.setVisibility(View.GONE);
        }
    }

    private void setAllButtonClick() {
        if (tv_approved != null) {
            tv_approved.setOnClickListener((View View) -> {
                        Intent intentInputOrdiringListActivity = new Intent(context, InputOrdiringListActivity.class);
                        intentInputOrdiringListActivity.putExtra("tab_type", "3"); //approved by vendor
                        startActivity(intentInputOrdiringListActivity);
                    }
            );
        }
        if (tv_pending != null) {
            tv_pending.setOnClickListener((View view) -> {
                        Intent intentInputOrdiringListActivity = new Intent(context, InputOrdiringListActivity.class);
                        intentInputOrdiringListActivity.putExtra("tab_type", "1"); //pending
                        startActivity(intentInputOrdiringListActivity);
                    }
            );
        }
        if (tv_rejected != null) {
            tv_rejected.setOnClickListener((View view) -> {
                        Intent intentInputOrdiringListActivity = new Intent(context, InputOrdiringListActivity.class);
                intentInputOrdiringListActivity.putExtra("tab_type", "7"); //rejected by vendor
                        startActivity(intentInputOrdiringListActivity);
                    }
            );
        }
        if (tv_vendor_referred != null) {
            tv_vendor_referred.setOnClickListener((View view) -> {
                        Intent intentInputOrdiringListActivity = new Intent(context, InputOrdiringListActivity.class);
                intentInputOrdiringListActivity.putExtra("tab_type", "2"); //vendor referred
                        startActivity(intentInputOrdiringListActivity);
                    }
            );
        }
        if (tv_new_input_ordering != null) {
            tv_new_input_ordering.setOnClickListener((View view) -> {
                        Intent intentInputOrdiringListActivity = new Intent(context, InputOrdiringListActivity.class);
                intentInputOrdiringListActivity.putExtra("tab_type", ""); //new_input_ordering
                startActivity(intentInputOrdiringListActivity);
                    }
            );
        }
    }

    private void initViews() {
        tv_approved = findViewById(R.id.tv_approved);
        tv_pending = findViewById(R.id.tv_pending);
        tv_rejected = findViewById(R.id.tv_rejected);
        tv_vendor_referred = findViewById(R.id.tv_vendor_referred);
        tv_new_input_ordering = findViewById(R.id.tv_new_input_ordering);

        sharedPrefHelper = new SharedPrefHelper(context);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home_menu) {

            super.onBackPressed();
            Intent intentLandHoldingActivity = new Intent(InputOrderingHome.this, HomeAcivity.class);
            startActivity(intentLandHoldingActivity);
            finish();
        }
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            Intent intentLandHoldingActivity = new Intent(InputOrderingHome.this, HomeAcivity.class);
            startActivity(intentLandHoldingActivity);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {

            super.onBackPressed();
            Intent intentLandHoldingActivity = new Intent(InputOrderingHome.this, HomeAcivity.class);
            startActivity(intentLandHoldingActivity);
            finish();
    }
}
