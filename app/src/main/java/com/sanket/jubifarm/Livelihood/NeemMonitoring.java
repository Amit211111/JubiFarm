package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.sanket.jubifarm.Livelihood.Model.Neem_Monitoring_Pojo;
import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class NeemMonitoring extends AppCompatActivity {

    Button btnSubmit;
    ImageView img_setimage;
    Spinner spnLandSelection;
    TextView tv_click;
    EditText et_monitoring_date, remarks;
    private int land_id = 0;
    ArrayList<String> neemArrayList;
    HashMap<String, Integer> neemHM=new HashMap<>();
    EditText et_plant_date;
    SharedPrefHelper sharedPrefHelper;
    String base64;
    Spinner spnNeemSelection;
    int neemId=0;
    String landId="";

    boolean isEditable = false;
    private Context context = this;

    private static final int CAMERA_REQUEST = 1888;
    SqliteHelper sqliteHelper;
    Neem_Monitoring_Pojo neem_monitoring;
    int mYear, mMonth, mDay, year, month, day;
    DatePickerDialog datePickerDialog;
    String farmer_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neem_monitoring);
        getSupportActionBar().setTitle("Neem Monitoring");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        initliaze();

        setNeemSpinner();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            landId = bundle.getString("landId", "");
        }

        et_plant_date.setKeyListener(null);

        et_plant_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_plant_date.setError(null);
                et_plant_date.clearFocus();
                mYear = year;
                mMonth = month;
                mDay = day;

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR); // current year
                mMonth = c.get(Calendar.MONTH); // current month
                mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                DatePickerDialog datePickerDialog = new DatePickerDialog(NeemMonitoring.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                et_plant_date.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#ff173e6d"));
            }
        });


        tv_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                neem_monitoring = new Neem_Monitoring_Pojo();

                farmer_id= sqliteHelper.getCloumnName("farmer_id","ps_land_holding", "where land_id='"+landId+"'");

                neem_monitoring.setMonitoring_date(et_monitoring_date.getText().toString().trim());
                neem_monitoring.setNeem_monitoring_image(base64);
                neem_monitoring.setFarmer_id(farmer_id);
                neem_monitoring.setLand_id(landId);
                neem_monitoring.setUser_id(sharedPrefHelper.getString("user_id",""));
                neem_monitoring.setRemarks(remarks.getText().toString().trim());
                neem_monitoring.setNeem_id(String.valueOf(neemId));
                neem_monitoring.setLatitude(sharedPrefHelper.getString("LAT",""));
                neem_monitoring.setLongitude(sharedPrefHelper.getString("LONG",""));
                sqliteHelper.AddneemMonitoring(neem_monitoring);

                Intent intent = new Intent(NeemMonitoring.this, PS_LandHoldingList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initliaze() {
        img_setimage = findViewById(R.id.img_setimage);
        spnLandSelection = findViewById(R.id.spnLandSelection);
        et_monitoring_date = findViewById(R.id.et_monitoring_date);
        neemArrayList=new ArrayList<>();
        sharedPrefHelper=new SharedPrefHelper(this);
        remarks = findViewById(R.id.remarks);
        et_plant_date = findViewById(R.id.et_monitoring_date);
        tv_click =findViewById(R.id.tv_click);
        btnSubmit = findViewById(R.id.btnSubmit);
        spnNeemSelection= findViewById(R.id.spnNeemSelection);
        sqliteHelper = new SqliteHelper(getApplicationContext());
    }

    private void setNeemSpinner() {
        neemArrayList.clear();
        neemHM = sqliteHelper.getAllPSNeem();

        for (int i = 0; i < neemHM.size(); i++) {
            neemArrayList.add(neemHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            //farmarArrayList.add(0, farmer_name);
        } else {
            neemArrayList.add(0, "Select Neem");
        }
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_list, neemArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNeemSelection.setAdapter(arrayAdapter);
        if (isEditable) {
            int spinnerPosition = 0;
            Integer strpos1 = land_id;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = land_id;
                spinnerPosition = arrayAdapter.getPosition(strpos1);
                spnNeemSelection.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spnNeemSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spnNeemSelection.getSelectedItem().toString().trim().equalsIgnoreCase("Select Neem")) {
                    if (spnNeemSelection.getSelectedItem().toString().trim() != null) {
                        neemId = neemHM.get(spnNeemSelection.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytes = stream.toByteArray();
//
            base64 = encodeTobase64(photo);
            img_setimage.setImageBitmap(photo);
        }

    }

    private String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOS = null;
        try {
            System.gc();
            byteArrayOS = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOS);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            image.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOS);
        }
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP);
    }




}