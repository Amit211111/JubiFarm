package com.sanket.jubifarm.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.sanket.jubifarm.Modal.SoilPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UpdatedSoilInFo extends AppCompatActivity {
    EditText et_filtration_rate, et_soil_texture, et_ph, et_bulk_density, et_cation_exchange_capacity, et_ec;
    EditText et_p, et_s, et_mg, et_k, et_n, et_ca, et_land_name, et_Date;
    private Spinner spn_soil_type, spn_soil_color;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    private ArrayList<String> soilTypeAL;
    private HashMap<String, Integer> soilTypeHM;
    private ArrayList<String> soilColorAL;
    private HashMap<String, Integer> soilColorHM;
    boolean isEditable = true;
    DatePickerDialog datePickerDialog;
    String land_id;
    private String soil_type_name = "", soil_color_name = "";
    Context context = this;
    String id="";
    SoilPojo soilPojo;
    int soilColorId;
    int soilTypeId;
    String soildate;
    private String k = "", mg = "", s = "", n = "", p = "", ca = "", ec = "", bulk_density = "", land_name,
            filtration_rate = "", soil_texture = "", ph = "", cation_exchange = "";
    Button btn_submitDetls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updated_soil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" +getString(R.string.update_soil)+ "</font>"));
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            land_id = bundle.getString("land_id", "");
        }
        soilPojo = new SoilPojo();
        soilTypeAL = new ArrayList<>();
        soilTypeHM = new HashMap<>();
        soilColorAL = new ArrayList<>();
        soilColorHM = new HashMap<>();

        et_filtration_rate = findViewById(R.id.et_filtration);
        et_bulk_density = findViewById(R.id.et_bulkdensity);
        et_ph = findViewById(R.id.et_ph);
        et_cation_exchange_capacity = findViewById(R.id.et_cation);
        et_soil_texture = findViewById(R.id.et_soil);
        et_ec = findViewById(R.id.et_ec);
        et_p = findViewById(R.id.et_p);
        et_s = findViewById(R.id.et_s);
        et_mg = findViewById(R.id.et_mg);
        et_k = findViewById(R.id.et_k);
        et_n = findViewById(R.id.et_n);
        et_ca = findViewById(R.id.et_ca);
        et_land_name = findViewById(R.id.et_land_name);
        spn_soil_type = findViewById(R.id.spn_soil_type);
        spn_soil_color = findViewById(R.id.spn_soil_color);
        et_Date = findViewById(R.id.et_Date);
        btn_submitDetls = findViewById(R.id.btn_submitDetls);
        localdata();
        setSoilTypeSpinner();
        setSoilColorSpinner();


        et_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                + "/" + String.valueOf(year);
                        et_Date.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });


        btn_submitDetls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtration_rate = et_filtration_rate.getText().toString();
                soil_texture = et_soil_texture.getText().toString();
                soildate = et_Date.getText().toString();
                ph = et_ph.getText().toString();
                bulk_density = et_bulk_density.getText().toString();
                cation_exchange = et_cation_exchange_capacity.getText().toString();
                ec = et_ec.getText().toString();
                p = et_p.getText().toString();
                s = et_s.getText().toString();
                mg = et_mg.getText().toString();
                k = et_k.getText().toString();
                n = et_n.getText().toString();
                ca = et_ca.getText().toString();
                // land_name = et_land_name.getText().toString();

                soilPojo.setFiltration_rate(filtration_rate);
                soilPojo.setSoil_texture(soil_texture);
                soilPojo.setPh(ph);
                soilPojo.setSoil_type_id(String.valueOf(soilTypeId));
                soilPojo.setSoil_color_id(String.valueOf(soilColorId));
                soilPojo.setBulk_density(bulk_density);
                soilPojo.setCation_exchange_capacity(cation_exchange);
                soilPojo.setEc(ec);
                soilPojo.setP(p);
                soilPojo.setS(s);
                soilPojo.setMg(mg);
                soilPojo.setK(k);
                soilPojo.setN(n);
                soilPojo.setCa(ca);
                soilPojo.setLand_id(land_id);
                // soilPojo.setLand_name(land_name);
                soilPojo.setSoil_updated_date(soildate);
                sqliteHelper.saveSoilInfoData(soilPojo,id);
                Intent intent = new Intent(UpdatedSoilInFo.this, SoilInFoListActivity.class);
                intent.putExtra("land_id",land_id);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setSoilColorSpinner() {
        soilColorAL.clear();
        SharedPrefHelper spf = new SharedPrefHelper(UpdatedSoilInFo.this);
        String language = spf.getString("languageID", "");
        if (language.equalsIgnoreCase("hin")) {
            soilColorHM = sqliteHelper.getMasterSpinnerId(18, 1);
        } else {
            soilColorHM = sqliteHelper.getMasterSpinnerId(18, 1);
        }


        for (int i = 0; i < soilColorHM.size(); i++) {
            soilColorAL.add(soilColorHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
        } else {
            soilColorAL.add(0, getString(R.string.select_soil_color));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, soilColorAL);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_soil_color.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = soil_color_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = soil_color_name;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_soil_color.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_soil_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_soil_color.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_soil_color))) {
                    if (spn_soil_color.getSelectedItem().toString().trim() != null) {
                        soilColorId = soilColorHM.get(spn_soil_color.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setSoilTypeSpinner() {
        soilTypeAL.clear();
        SharedPrefHelper spf = new SharedPrefHelper(UpdatedSoilInFo.this);
        String language = spf.getString("languageID", "");
        if (language.equalsIgnoreCase("hin")) {
            soilTypeHM = sqliteHelper.getMasterSpinnerId(17, 2);
        }else if (language.equalsIgnoreCase("kan")) {
            soilTypeHM = sqliteHelper.getMasterSpinnerId(17, 3);
        } else {
            soilTypeHM = sqliteHelper.getMasterSpinnerId(17, 1);
        }

        for (int i = 0; i < soilTypeHM.size(); i++) {
            soilTypeAL.add(soilTypeHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
        } else {
            soilTypeAL.add(0, getString(R.string.select_soil_type));
        }

        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, soilTypeAL);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_soil_type.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = soil_type_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = soil_type_name;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_soil_type.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_soil_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_soil_type.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_soil_type))) {
                    if (spn_soil_type.getSelectedItem().toString().trim() != null) {
                        soilTypeId = soilTypeHM.get(spn_soil_type.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void localdata() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id", "");

            if (!id.equals("")) {
                String language = sharedPrefHelper.getString("languageID","");
                soilPojo = sqliteHelper.getSoilDetail(id);
                et_bulk_density.setText(soilPojo.getBulk_density());
                et_ca.setText(soilPojo.getCa());
                et_cation_exchange_capacity.setText(soilPojo.getCation_exchange_capacity());
                et_Date.setText(soilPojo.getSoil_updated_date());
                et_ec.setText(soilPojo.getEc());
                et_filtration_rate.setText(soilPojo.getFiltration_rate());
                et_k.setText(soilPojo.getK());
                et_mg.setText(soilPojo.getMg());
                et_n.setText(soilPojo.getN());
                et_p.setText(soilPojo.getP());
                et_ph.setText(soilPojo.getPh());
                et_s.setText(soilPojo.getS());
                land_id=soilPojo.getLand_id();
                soil_type_name=sqliteHelper.getmasterName(Integer.parseInt(soilPojo.getSoil_type_id()),language);
                soil_color_name=sqliteHelper.getmasterName(Integer.parseInt(soilPojo.getSoil_color_id()),language);
                //spn_soil_type.setSelection(Integer.parseInt(soilPojo.getSoil_type_id()));
                //  state_id = agentPojo.getState();

            }

        }
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intentHome = new Intent(UpdatedSoilInFo.this, SoilInFoListActivity.class);
        intentHome.putExtra("land_id",land_id);
        startActivity(intentHome);
        finish();
    }
}