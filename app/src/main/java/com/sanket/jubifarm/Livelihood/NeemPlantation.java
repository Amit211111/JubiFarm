package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.Calendar;

public class NeemPlantation extends AppCompatActivity {
//    String [] sp_neemPlantation ={"Select Neem Plantation","Neem"};
    //  String [] sp_NeemCategory={"Select Neem Category"};
    String [] sp_land={"Select Land"};
    Button btn_submitDetls;
    TextView CLICKIMAGE,GeoCoodinate;
    Spinner spnNeemPlantation,spnLandSelection;
    EditText et_plant_date;
    SqliteHelper sqliteHelper;
    PSNeemPlantationPojo psNeemPlantationPojo;
    int mYear,mMonth,mDay,year,month,day;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neem_plantation);
        getSupportActionBar().setTitle("Neem Plantation");
        //Button
        btn_submitDetls=findViewById(R.id.btn_submitDetls);
        //All Text View
        CLICKIMAGE=findViewById(R.id.CLICKIMAGE);
        GeoCoodinate=findViewById(R.id.GeoCoodinate);
        //All Spinner
 //       spnNeemPlantation=findViewById(R.id.spnNeemPlantation);
//        spnsub_NeemCategory=findViewById(R.id.spnsub_NeemCategory);
        spnLandSelection=findViewById(R.id.spnLandSelection);
        //All Edit Text
        et_plant_date=findViewById(R.id.et_plant_date);

        sqliteHelper = new SqliteHelper(getApplicationContext());


        //All Spinner

//        ArrayAdapter Neem_adapter=new ArrayAdapter(NeemPlantation.this, R.layout.spinner_list,sp_neemPlantation);
//        Neem_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnNeemPlantation.setAdapter(Neem_adapter);

//        ArrayAdapter Category_adapter=new ArrayAdapter(NeemPlantation.this,R.layout.spinner_list,sp_NeemCategory);
//        Category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnsub_NeemCategory.setAdapter(Category_adapter);

        ArrayAdapter land_adapter=new ArrayAdapter(NeemPlantation.this, R.layout.spinner_list,sp_land);
        land_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLandSelection.setAdapter(land_adapter);
        //Date Picker
        et_plant_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_plant_date.setError(null);
                et_plant_date.clearFocus();
                mYear=year;
                mMonth=month;
                mDay=day;

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

        btn_submitDetls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                psNeemPlantationPojo=new PSNeemPlantationPojo();
                psNeemPlantationPojo.setNeemPlantation_Image(CLICKIMAGE.getText().toString().trim());
                psNeemPlantationPojo.setNeem_Plantation(spnNeemPlantation.getSelectedItem().toString().trim());
//                psNeemPlantationPojo.setSub_Neem_Category(spnsub_NeemCategory.getSelectedItem().toString().trim());
                psNeemPlantationPojo.setLand(spnLandSelection.getSelectedItem().toString().trim());
                psNeemPlantationPojo.setPlantation_Date(et_plant_date.getText().toString().trim());
                psNeemPlantationPojo.setGeo_Coordinates(GeoCoodinate.getText().toString().trim());
                sqliteHelper.PSsaveHousehold(psNeemPlantationPojo);

            }
        });





    }
}