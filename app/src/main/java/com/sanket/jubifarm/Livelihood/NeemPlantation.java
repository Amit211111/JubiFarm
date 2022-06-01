package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class NeemPlantation extends AppCompatActivity {

    Button btn_submitDetls;
    TextView CLICKIMAGE,GeoCoodinate, Neem_Id;
    boolean isEditable = false;
    private Context context = this;
    SharedPrefHelper sharedPrefHelper;
    Spinner spnLandSelection;
    private int land_id = 0;
    ArrayList<String> landArrayList;
    HashMap<String, Integer> landName;
    EditText et_plant_date;
    String base64;


    ImageView img_setimage;
    private static final int CAMERA_REQUEST = 1888;
    SqliteHelper sqliteHelper;
    PSNeemPlantationPojo psNeemPlantationPojo;
    int mYear,mMonth,mDay,year,month,day;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neem_plantation);
        getSupportActionBar().setTitle("Neem Plantation");
        sharedPrefHelper = new SharedPrefHelper(this);


        //Button
        btn_submitDetls = findViewById(R.id.btn_submitDetls);
        //All Text View
        CLICKIMAGE = findViewById(R.id.CLICKIMAGE);
        landArrayList = new ArrayList<>();
        landName = new HashMap<>();
        img_setimage = findViewById(R.id.img_setimage);
        sharedPrefHelper = new SharedPrefHelper(this);
        GeoCoodinate = findViewById(R.id.GeoCoodinate);


        //All Spinner
        //       spnNeemPlantation=findViewById(R.id.spnNeemPlantation);
//        spnsub_NeemCategory=findViewById(R.id.spnsub_NeemCategory);
        spnLandSelection = findViewById(R.id.spnLandSelection);
        //All Edit Text
        et_plant_date = findViewById(R.id.et_plant_date);

        sqliteHelper = new SqliteHelper(getApplicationContext());

        setLandSpinner();


        //Date Picker
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
                datePickerDialog = new DatePickerDialog(NeemPlantation.this,
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

        CLICKIMAGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        btn_submitDetls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                psNeemPlantationPojo = new PSNeemPlantationPojo();
                psNeemPlantationPojo.setNeem_plantation_image(base64);
                psNeemPlantationPojo.setLand_id(spnLandSelection.getSelectedItem().toString().trim());
                psNeemPlantationPojo.setPlantation_Date(et_plant_date.getText().toString().trim());
                GeoCoodinate.setText(sharedPrefHelper.getString("LAT","")+", "+sharedPrefHelper.getString("LONG",""));
                sqliteHelper.PSsaveHousehold(psNeemPlantationPojo);

                Intent intent = new Intent(NeemPlantation.this, PS_NeemPlantationList.class);
                sharedPrefHelper.setString("neemPlantation", "view");
                startActivity(intent);

            }
        });
    }

    private void setLandSpinner() {
        landArrayList.clear();
        landName = sqliteHelper.getAllPSLAND();

        for (int i = 0; i < landName.size(); i++) {
            landArrayList.add(landName.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            //farmarArrayList.add(0, farmer_name);
        } else {
            landArrayList.add(0, getString(R.string.select_farmer));
        }
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_list, landArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLandSelection.setAdapter(arrayAdapter);
        if (isEditable) {
            int spinnerPosition = 0;
            Integer strpos1 = land_id;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = land_id;
                spinnerPosition = arrayAdapter.getPosition(strpos1);
                spnLandSelection.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spnLandSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spnLandSelection.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_farmer))) {
                    if (spnLandSelection.getSelectedItem().toString().trim() != null) {
                        land_id = landName.get(spnLandSelection.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

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




