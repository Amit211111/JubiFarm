package com.sanket.jubifarm.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sanket.jubifarm.Activity.InputOrdiringListActivity;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MyOperationLayoutInputOrdering {
    Activity activity;

    SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(activity);
    SqliteHelper sqliteHelper = new SqliteHelper(activity);

    public static HashMap<String, ArrayList<String>> display(final Activity activity) {
        LinearLayout scrollViewlinerLayout = (LinearLayout) activity.findViewById(R.id.linearLayoutInputOrdering);
        scrollViewlinerLayout.setVerticalGravity(View.TEXT_ALIGNMENT_CENTER);
        HashMap<String, ArrayList<String>> inputOrderingHM = new HashMap<>();
        ArrayList<String> toDateAl = new ArrayList<>();
        ArrayList<String> fromDateAl = new ArrayList<>();
        ArrayList<String> quantityAl = new ArrayList<>();
        ArrayList<String> remarksAl = new ArrayList<>();
        ArrayList<String> unitAl = new ArrayList<>();
        ArrayList<String> seedsAl = new ArrayList<>();
        ArrayList<String> farmerAl = new ArrayList<>();

        for (int i = 0; i < scrollViewlinerLayout.getChildCount(); i++) {
            LinearLayout innerLayout = (LinearLayout) scrollViewlinerLayout.getChildAt(i);
            final EditText et_IOTodate = innerLayout.findViewById(R.id.et_IOTodate);
            final EditText et_IOfromdate = innerLayout.findViewById(R.id.et_IOfromdate);
            final EditText et_quantity = innerLayout.findViewById(R.id.et_quantity);
            final EditText et_remarks = innerLayout.findViewById(R.id.et_remarks);
            final Spinner spn_unit = innerLayout.findViewById(R.id.spn_unit);
            final Spinner spnSeeds = innerLayout.findViewById(R.id.spnSeeds);
            final Spinner spn_farmer = innerLayout.findViewById(R.id.spn_farmer);

            toDateAl.add(et_IOTodate.getText().toString().trim());
            fromDateAl.add(et_IOfromdate.getText().toString().trim());
            quantityAl.add(et_quantity.getText().toString().trim());
            remarksAl.add(et_remarks.getText().toString().trim());
            unitAl.add(spn_unit.getSelectedItem().toString().trim());
            seedsAl.add(spnSeeds.getSelectedItem().toString().trim());
            farmerAl.add(spn_farmer.getSelectedItem().toString().trim());
        }

        inputOrderingHM.put("toDate", toDateAl);
        inputOrderingHM.put("fromDate", fromDateAl);
        inputOrderingHM.put("quantity", quantityAl);
        inputOrderingHM.put("remarks", remarksAl);
        inputOrderingHM.put("unit", unitAl);
        inputOrderingHM.put("seeds", seedsAl);
        inputOrderingHM.put("farmer", farmerAl);

        return inputOrderingHM;
    }

    public static HashMap<String, String> add(final Activity activity, ImageButton btn) {
        final HashMap<String, String> hashMap = new HashMap<>();
        SqliteHelper sqliteHelper = new SqliteHelper(activity);
        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(activity);
        ArrayList<String> seedsList = new ArrayList<>();
        ArrayList<String> unitList = new ArrayList<>();
        ArrayList<String> farmerList = new ArrayList<>();
        final HashMap<String, Integer>[] unitHM = new HashMap[]{new HashMap<>()};
        final HashMap<String, Integer>[] seedsHM = new HashMap[]{new HashMap<>()};
        final HashMap<String, Integer>[] farmerHM = new HashMap[]{new HashMap<>()};
        final LinearLayout linearLayoutInputOrdering = (LinearLayout) activity.findViewById(R.id.linearLayoutInputOrdering);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout newView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.addmoreinputordering, null);
                newView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                final EditText et_IOTodate = newView.findViewById(R.id.et_IOTodate);
                final EditText et_IOfromdate = newView.findViewById(R.id.et_IOfromdate);
                final EditText et_quantity = newView.findViewById(R.id.et_quantity);
                final EditText et_remarks = newView.findViewById(R.id.et_remarks);
                final Spinner spnSeeds = newView.findViewById(R.id.spnSeeds);
                final Spinner spn_unit = newView.findViewById(R.id.spn_unit);
                final TextView remove = newView.findViewById(R.id.remove);
                final TextView tv_Farmer_name = newView.findViewById(R.id.tv_Farmer_name);
                final LinearLayout ll_farmer = newView.findViewById(R.id.ll_farmer);
                final Spinner spn_farmer = newView.findViewById(R.id.spn_farmer);

                if (sharedPrefHelper.getString("selected_farmer","").equals("")){
                    spn_farmer.setVisibility(View.VISIBLE);
                    tv_Farmer_name.setVisibility(View.GONE);
                }else {
                    spn_farmer.setVisibility(View.GONE);
                    tv_Farmer_name.setVisibility(View.VISIBLE);
                    tv_Farmer_name.setText(sqliteHelper.getFarmerName(sharedPrefHelper.getString("selected_farmer","")));
                }
                /*make condition for farmer*/
                if (sharedPrefHelper.getString("user_type", "").equals("Farmer")) {
                    ll_farmer.setVisibility(View.GONE);
                } else {
                    ll_farmer.setVisibility(View.VISIBLE);
                }

                //farmer spinner
                farmerList.clear();
                farmerHM[0] = sqliteHelper.getFarmerspinner();

                for (int i = 0; i < farmerHM[0].size(); i++) {
                    farmerList.add(farmerHM[0].keySet().toArray()[i].toString().trim());
                }

                farmerList.add(0, activity.getString(R.string.select_farmer));
                final ArrayAdapter xxx = new ArrayAdapter(activity, R.layout.spinner_list, farmerList);
                xxx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_farmer.setAdapter(xxx);

                //product unit spinner
                unitList.clear();
                SharedPrefHelper spf = new SharedPrefHelper(activity);
                String language = spf.getString("languageID","");
                if(language.equalsIgnoreCase("hin"))
                {
                    unitHM[0] = sqliteHelper.getMasterSpinnerId(3, 2);
                }
                else
                {
                    unitHM[0] = sqliteHelper.getMasterSpinnerId(3, 1);
                }

                for (int i = 0; i < unitHM[0].size(); i++) {
                    unitList.add(unitHM[0].keySet().toArray()[i].toString().trim());
                }

                unitList.add(0, activity.getString(R.string.select_unit));
                final ArrayAdapter xx = new ArrayAdapter(activity, R.layout.spinner_list, unitList);
                xx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_unit.setAdapter(xx);
                //seeds spinner
                seedsList.clear();
                if(language.equalsIgnoreCase("hin"))
                {
                    seedsHM[0] = sqliteHelper.getMasterSpinnerId(13, 2);
                }
                else
                {
                    seedsHM[0] = sqliteHelper.getMasterSpinnerId(13, 1);
                }

                for (int i = 0; i < seedsHM[0].size(); i++) {
                    seedsList.add(seedsHM[0].keySet().toArray()[i].toString().trim());
                }

                seedsList.add(0, activity.getString(R.string.select_items));
                final ArrayAdapter x = new ArrayAdapter(activity, R.layout.spinner_list, seedsList);
                x.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnSeeds.setAdapter(x);

                et_IOTodate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog mDatePickerDialog1 = null;
                        Calendar mCalendar1 = Calendar.getInstance();

                        Calendar c = Calendar.getInstance();
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int month = c.get(Calendar.MONTH);
                        int year = c.get(Calendar.YEAR);

                        if (mDatePickerDialog1 == null) {
                            mDatePickerDialog1 = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                                    et_IOTodate.setText(String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                                    mCalendar1.set(java.util.Calendar.MONTH, monthOfYear);
                                    mCalendar1.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
                                    mCalendar1.set(java.util.Calendar.YEAR, year);

                                }
                            }, year, month, day);

                            mDatePickerDialog1.setCanceledOnTouchOutside(true);
                        }
                        //Disable date before today
                        mDatePickerDialog1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        mDatePickerDialog1.show();
                    }
                });

                et_IOfromdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog mDatePickerDialog1 = null;
                        Calendar mCalendar1 = Calendar.getInstance();

                        Calendar c = Calendar.getInstance();
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int month = c.get(Calendar.MONTH);
                        int year = c.get(Calendar.YEAR);

                        if (mDatePickerDialog1 == null) {
                            mDatePickerDialog1 = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                                    et_IOfromdate.setText(String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                                    mCalendar1.set(java.util.Calendar.MONTH, monthOfYear);
                                    mCalendar1.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
                                    mCalendar1.set(java.util.Calendar.YEAR, year);

                                }
                            }, year, month, day);

                            mDatePickerDialog1.setCanceledOnTouchOutside(true);
                        }
                        //Disable date before today
                        mDatePickerDialog1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        mDatePickerDialog1.show();
                    }
                });

                TextView btn_remove = (TextView) newView.findViewById(R.id.remove);
                btn_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearLayoutInputOrdering.removeView(newView);
                        final EditText et_IOTodate = newView.findViewById(R.id.et_IOTodate);
                        final EditText et_IOfromdate = newView.findViewById(R.id.et_IOfromdate);
                        final EditText et_quantity = newView.findViewById(R.id.et_quantity);
                        final EditText et_remarks = newView.findViewById(R.id.et_remarks);
                        final Spinner spn_unit = newView.findViewById(R.id.spn_unit);
                        final Spinner spnSeeds = newView.findViewById(R.id.spnSeeds);
                        final Spinner spn_farmer = newView.findViewById(R.id.spn_farmer);
                        final TextView remove = newView.findViewById(R.id.remove);
                        String ssss = et_IOTodate.getText().toString().trim();
                        String sssss = et_IOfromdate.getText().toString().trim();
                        String ssssss = et_quantity.getText().toString().trim();
                        String sssssss = et_remarks.getText().toString().trim();
                        hashMap.remove(ssss);
                        hashMap.remove(sssss);
                        hashMap.remove(ssssss);
                        hashMap.remove(sssssss);
                    }
                });
                linearLayoutInputOrdering.addView(newView);
            }
        });
        return hashMap;
    }
}
