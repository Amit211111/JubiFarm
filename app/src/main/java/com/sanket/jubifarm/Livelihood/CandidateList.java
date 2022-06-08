package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.sanket.jubifarm.Activity.RegistrationListActivity;
import com.sanket.jubifarm.Adapter.RegistrationListAdapter;
import com.sanket.jubifarm.Livelihood.Adapter.AdapterSkillCenter;
import com.sanket.jubifarm.Livelihood.Model.CandidatePojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class CandidateList extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<CandidatePojo> arrayList = new ArrayList<>();
    SqliteHelper sqliteHelper;
    EditText etSearchBar;
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
        etSearchBar = findViewById(R.id.etSearchBar);


        arrayList = sqliteHelper.getPsSkillTrackingData();
        AdapterSkillCenter adapterSkillCenter = new AdapterSkillCenter(context, arrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterSkillCenter);


        etSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String search = etSearchBar.getText().toString();
                arrayList= sqliteHelper.getSkillCenter(search);
                AdapterSkillCenter adapterSkillCenter  = new AdapterSkillCenter(CandidateList.this, arrayList);
                int counter = arrayList.size();
                //  FarmerCount.setText("Farmer 0"+counter);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(CandidateList.this));
                recyclerView.setAdapter(adapterSkillCenter);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        addskill=findViewById(R.id.addskill);


        addskill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CandidateList.this, AddCandidateActivity.class);
                startActivity(intent);
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(CandidateList.this, SkillTrackingMenuActivity.class);
                startActivity(intent);
                return true;
            case R.id.home_menu:
                Intent intent1 = new Intent(this,ParyavaranSakhiHome.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}