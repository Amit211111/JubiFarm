package com.sanket.jubifarm.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sanket.jubifarm.Activity.AddPlantAcivity;
import com.sanket.jubifarm.Activity.CropPlanning;
import com.sanket.jubifarm.Activity.InputOrdiringListActivity;
import com.sanket.jubifarm.Modal.CropVegitableDetails;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MyOperationLayoutAddCropDetails {
    static SharedPrefHelper sharedPrefHelper;
    Activity activity;
    Context context;
    SqliteHelper sqliteHelper = new SqliteHelper(activity);

    public static HashMap<String, ArrayList<String>> display(final Activity activity) {
        LinearLayout scrollViewlinerLayout = (LinearLayout) activity.findViewById(R.id.linearLayoutAddCrop);
        scrollViewlinerLayout.setVerticalGravity(View.TEXT_ALIGNMENT_CENTER);
        HashMap<String, ArrayList<String>> prescriptionHM = new HashMap<>();
        ArrayList<String> nameAl = new ArrayList<>();
        ArrayList<String> areaAl = new ArrayList<>();
        ArrayList<String> seasonAl = new ArrayList<>();
        ArrayList<String> unitAl = new ArrayList<>();
        ArrayList<String> unitsAl = new ArrayList<>();
        ArrayList<String> subAl = new ArrayList<>();
        ArrayList<String> qtyAl = new ArrayList<>();

        for (int i = 0; i < scrollViewlinerLayout.getChildCount(); i++) {
            LinearLayout innerLayout = (LinearLayout) scrollViewlinerLayout.getChildAt(i);
            final Spinner spn_crop = innerLayout.findViewById(R.id.spn_crop);
            final EditText et_Area = innerLayout.findViewById(R.id.et_Area);
            final EditText et_qty = innerLayout.findViewById(R.id.et_Quanty);
            final TextView family_remove = innerLayout.findViewById(R.id.family_remove);
            final Spinner spn_season = innerLayout.findViewById(R.id.spn_season);
            final Spinner spn_unit = innerLayout.findViewById(R.id.spn_unit);
            final Spinner spn_units = innerLayout.findViewById(R.id.spn_Units);
            final Spinner spn_sub_crop = innerLayout.findViewById(R.id.spn_sub_crop);
            sharedPrefHelper = new SharedPrefHelper(activity);
            nameAl.add(spn_crop.getSelectedItem().toString());
            subAl.add(spn_sub_crop.getSelectedItem().toString());
            areaAl.add(et_Area.getText().toString().trim());
            qtyAl.add(et_qty.getText().toString().trim());
            seasonAl.add(String.valueOf(spn_season.getSelectedItemId()));
            seasonAl.add(String.valueOf(spn_season.getSelectedItemId()));
            unitAl.add(spn_unit.getSelectedItem().toString());
            unitsAl.add(spn_units.getSelectedItem().toString());
        }

        prescriptionHM.put("name", nameAl);
        prescriptionHM.put("subname", subAl);
        prescriptionHM.put("area", areaAl);
        prescriptionHM.put("season", seasonAl);
        prescriptionHM.put("unit", unitAl);
        prescriptionHM.put("units", unitsAl);
        prescriptionHM.put("quantity", qtyAl);

        return prescriptionHM;

    }



    public static HashMap<String, String> add(final Activity activity, Button btn) {
        final HashMap<String, String> hashMap = new HashMap<>();
        SqliteHelper sqliteHelper = new SqliteHelper(activity);
        ArrayList<String> UnitArrayList = new ArrayList<>();
        ArrayList<String> monthlyList = new ArrayList<>();
        ArrayList<String> monthlyList1 = new ArrayList<>();
        monthlyList.add(0,"Kharif");
        monthlyList.add(1,"Rabi");
        monthlyList.add(2,"Zaid");
        monthlyList.add(3,"Annual");
        monthlyList1.add(0,"खरीफ");
        monthlyList1.add(1,"रबी");
        monthlyList1.add(2,"ज़ैद");
        monthlyList1.add(3,"सालाना");
        final HashMap<String, Integer>[] UnitNameHM = new HashMap[]{new HashMap<>()};
        final HashMap<String, Integer>[] unitsNameHM = new HashMap[]{new HashMap<>()};
        ArrayList<String> unitsArrayList = new ArrayList<>();
        final HashMap<String, Integer>[] FamilyEducationNameHM = new HashMap[]{new HashMap<>()};
        boolean isEditable = false;
        String unit = "";
        final int[] unitId = {0};
        final int[] cat_ID = {0};
        ArrayList<String> cropCategoryList=new ArrayList<>();
        ArrayList<String> subCategoryArrayList=new ArrayList<>();
        final HashMap<String, Integer>[] cropCategoryHM = new HashMap[]{new HashMap<>()};
        final HashMap<String, Integer>[] subCategoryHM = new HashMap[]{new HashMap<>()};


        final LinearLayout linearLayoutAddMedicine = (LinearLayout) activity.findViewById(R.id.linearLayoutAddCrop);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout newView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.add_crop_details, null);
                newView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                final Spinner spn_crop = newView.findViewById(R.id.spn_crop);
                final EditText et_Area = newView.findViewById(R.id.et_Area);
                final TextView family_remove = newView.findViewById(R.id.family_remove);
                final Spinner spn_season = newView.findViewById(R.id.spn_season);
                final Spinner spn_Unit = newView.findViewById(R.id.spn_unit);
                final Spinner spn_sub_crop = newView.findViewById(R.id.spn_sub_crop);
                final Spinner spn_Units = newView.findViewById(R.id.spn_Units);
                SharedPrefHelper spf = new SharedPrefHelper(activity);
                String languages = spf.getString("languageID","");
                if(languages.equalsIgnoreCase("hin")){
                    final ArrayAdapter Adapter1 = new ArrayAdapter(activity, R.layout.spinner_list, monthlyList1);
                    Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_season.setAdapter(Adapter1);
                }else {
                    final ArrayAdapter Adapter1 = new ArrayAdapter(activity, R.layout.spinner_list, monthlyList);
                    Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_season.setAdapter(Adapter1);
                }


                UnitArrayList.clear();
                String language = spf.getString("languageID","");
                if(language.equalsIgnoreCase("hin"))
                {
                    UnitNameHM[0] = sqliteHelper.getMasterSpinnerId(4, 2);
                }
                else
                {
                    UnitNameHM[0] = sqliteHelper.getMasterSpinnerId(4, 1);
                }

                for (int i = 0; i < UnitNameHM[0].size(); i++) {
                    UnitArrayList.add(UnitNameHM[0].keySet().toArray()[i].toString().trim());
                }
                if (isEditable) {
                    //UnitArrayList.add(0, unit);
                } else {
                    UnitArrayList.add(0, activity.getString(R.string.select_unit));
                }
                final ArrayAdapter Adapter = new ArrayAdapter(activity, R.layout.spinner_list, UnitArrayList);
                Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_Unit.setAdapter(Adapter);
                if (isEditable) {
                    int spinnerPosition = 0;
                    String strpos1 = unit;
                    if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                        strpos1 = unit;
                        spinnerPosition = Adapter.getPosition(strpos1);
                        spn_Unit.setSelection(spinnerPosition);
                        spinnerPosition = 0;
                    }
                }

                spn_Unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!spn_Unit.getSelectedItem().toString().trim().equalsIgnoreCase(activity.getString(R.string.select_unit))) {
                            if (spn_Unit.getSelectedItem().toString().trim() != null) {
                                unitId[0] = UnitNameHM[0].get(spn_Unit.getSelectedItem().toString().trim());
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                cropCategoryList.clear();
                if(language.equalsIgnoreCase("hin"))
                {
                    cropCategoryHM[0] = sqliteHelper.getAllCategoryType(2);

                }
                else
                {
                    cropCategoryHM[0] = sqliteHelper.getAllCategoryType(1);

                }
                for (int i = 0; i < cropCategoryHM[0].size(); i++) {
                    cropCategoryList.add(cropCategoryHM[0].keySet().toArray()[i].toString().trim());

                }
                cropCategoryList.add(0, activity.getString(R.string.select_crop_category));
                ArrayAdapter arrayAdapterCC = new ArrayAdapter(activity, R.layout.spinner_list, cropCategoryList);
                arrayAdapterCC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_crop.setAdapter(arrayAdapterCC);
                spn_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!spn_crop.getSelectedItem().toString().trim().equalsIgnoreCase(activity.getString(R.string.select_crop_category))) {
                            if (spn_crop.getSelectedItem().toString().trim() != null) {
                                cat_ID[0] = cropCategoryHM[0].get(spn_crop.getSelectedItem().toString().trim());
                                HashMap<String, Integer> subCategoryHM = new HashMap<>();
                                subCategoryArrayList.clear();
                                SharedPrefHelper spf = new SharedPrefHelper(activity);
                                String language = spf.getString("languageID","");
                                if(language.equalsIgnoreCase("hin"))
                                {
                                    subCategoryHM = sqliteHelper.getAllSubCategory(cat_ID[0], 2);
                                }
                                else
                                {
                                    subCategoryHM = sqliteHelper.getAllSubCategory(cat_ID[0], 1);
                                }
                                for (int i = 0; i < subCategoryHM.size(); i++) {
                                    subCategoryArrayList.add(subCategoryHM.keySet().toArray()[i].toString().trim());
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });




                    subCategoryArrayList.add(0, activity.getString(R.string.select_sub_crop_category));
                    final ArrayAdapter adapter = new ArrayAdapter(activity, R.layout.spinner_list, subCategoryArrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_sub_crop.setAdapter(adapter);

                     spn_sub_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (!spn_sub_crop.getSelectedItem().toString().trim().equalsIgnoreCase(activity.getString(R.string.select_sub_crop_category))) {
                                if (spn_sub_crop.getSelectedItem().toString().trim() != null) {

                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                unitsArrayList.clear();
                if(language.equalsIgnoreCase("hin"))
                {
                    unitsNameHM[0] = sqliteHelper.getMasterSpinnerId(3, 2);
                }
                else
                {
                    unitsNameHM[0] = sqliteHelper.getMasterSpinnerId(3, 1);
                }

                for (int i = 0; i < unitsNameHM[0].size(); i++) {
                    unitsArrayList.add(unitsNameHM[0].keySet().toArray()[i].toString().trim());
                }
                if (isEditable) {
                    //UnitArrayList.add(0, unit);
                } else {
                    unitsArrayList.add(0, activity.getString(R.string.select_Units));
                }
                final ArrayAdapter Adapterr = new ArrayAdapter(activity, R.layout.spinner_list, unitsArrayList);
                Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_Units.setAdapter(Adapterr);




                TextView btn_remove = (TextView) newView.findViewById(R.id.family_remove);
                btn_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearLayoutAddMedicine.removeView(newView);
                        final Spinner spn_crop = newView.findViewById(R.id.spn_crop);
                        final EditText et_Area = newView.findViewById(R.id.et_Area);
                        final TextView family_remove = newView.findViewById(R.id.family_remove);
                        final Spinner spn_season = newView.findViewById(R.id.spn_season);
                        final Spinner spn_unit = newView.findViewById(R.id.spn_unit);
                        String sssss = et_Area.getText().toString().trim();
                        hashMap.remove(sssss);

                    }
                });
                linearLayoutAddMedicine.addView(newView);
            }
        });


        return hashMap;
    }




    public static HashMap<String, String> addforEdit(final Activity activity, ArrayList<CropVegitableDetails> farmerfamilyPojoArrayList) {
        final HashMap<String, String> hashMap = new HashMap<>();
        SqliteHelper sqliteHelper = new SqliteHelper(activity);
        ArrayList<String> UnitArrayList = new ArrayList<>();
        ArrayList<String> unitsArrayList = new ArrayList<>();
        ArrayList<String> monthlyList = new ArrayList<>();
        ArrayList<String> monthlyList1 = new ArrayList<>();
        monthlyList.add(0,"Kharif");
        monthlyList.add(1,"Rabi");
        monthlyList.add(2,"Zaid");
        monthlyList.add(3,"Annual");
        monthlyList1.add(0,"खरीफ");
        monthlyList1.add(1,"रबी");
        monthlyList1.add(2,"ज़ैद");
        monthlyList1.add(3,"सालाना");
        final HashMap<String, Integer>[] UnitNameHM = new HashMap[]{new HashMap<>()};
        final HashMap<String, Integer>[] unitsNameHM = new HashMap[]{new HashMap<>()};
        final HashMap<String, Integer>[] FamilyEducationNameHM = new HashMap[]{new HashMap<>()};
        boolean isEditable = false;
        String unit = "";
        final int[] cat_ID = {0};
        final int[] unitId = {0};
        ArrayList<String> cropCategoryList=new ArrayList<>();
        ArrayList<String> subCategoryArrayList;
        final HashMap<String, Integer>[] cropCategoryHM = new HashMap[]{new HashMap<>()};
        final HashMap<String, Integer>[] subCategoryHM = new HashMap[]{new HashMap<>()};

        final LinearLayout linearLayoutAddMedicine = (LinearLayout) activity.findViewById(R.id.linearLayoutAddCrop);
        for (int k = 0; k < farmerfamilyPojoArrayList.size() ; k++) {
            final LinearLayout newView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.add_crop_details, null);
            newView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final Spinner spn_crop = newView.findViewById(R.id.spn_crop);
            final EditText et_Area = newView.findViewById(R.id.et_Area);
            final EditText et_qty = newView.findViewById(R.id.et_Quanty);
            final TextView family_remove = newView.findViewById(R.id.family_remove);
            final Spinner spn_season = newView.findViewById(R.id.spn_season);
            final Spinner spn_Unit = newView.findViewById(R.id.spn_unit);
            final Spinner spn_Units = newView.findViewById(R.id.spn_Units);
            final Spinner spn_sub_crop = newView.findViewById(R.id.spn_sub_crop);
            SharedPrefHelper spf = new SharedPrefHelper(activity);
            String language = spf.getString("languageID","");
            et_Area.setText(farmerfamilyPojoArrayList.get(k).getArea());
            et_qty.setText(farmerfamilyPojoArrayList.get(k).getQuantity());
            if(language.equalsIgnoreCase("hin")){
                final ArrayAdapter Adapter1 = new ArrayAdapter(activity, R.layout.spinner_list, monthlyList1);
                Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_season.setAdapter(Adapter1);
            }else {
                final ArrayAdapter Adapter1 = new ArrayAdapter(activity, R.layout.spinner_list, monthlyList);
                Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_season.setAdapter(Adapter1);
            }
            if (Integer.parseInt(farmerfamilyPojoArrayList.get(k).getSeason_id())>4){
               String season = sqliteHelper.getmasterName(Integer.parseInt(farmerfamilyPojoArrayList.get(k).getSeason_id()),language);
                spn_season.setSelection(monthlyList.indexOf(season));

            }else {
                spn_season.setSelection(Integer.parseInt(farmerfamilyPojoArrayList.get(k).getSeason_id()));

            }

            spn_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!spn_crop.getSelectedItem().toString().trim().equalsIgnoreCase(activity.getString(R.string.select_crop_category))) {
                        if (spn_crop.getSelectedItem().toString().trim() != null) {
                            cat_ID[0] = cropCategoryHM[0].get(spn_crop.getSelectedItem().toString().trim());
                           /* HashMap<String, Integer> subCategoryHM = new HashMap<>();
                            subCategoryArrayList.clear();
                            subCategoryHM = sqliteHelper.getAllSubCategory(cat_ID[0]);
                            for (int i = 0; i < subCategoryHM.size(); i++) {
                                subCategoryArrayList.add(subCategoryHM.keySet().toArray()[i].toString().trim());
                            }*/

                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            subCategoryArrayList=new ArrayList<>();

            if(language.equalsIgnoreCase("hin"))
            {
                subCategoryHM[0] = sqliteHelper.getAllSubCategory(Integer.parseInt(farmerfamilyPojoArrayList.get(k).getCrop_name()), 2);
            }
            else
            {
                subCategoryHM[0] = sqliteHelper.getAllSubCategory(Integer.parseInt(farmerfamilyPojoArrayList.get(k).getCrop_name()), 1);
            }
            for (int i = 0; i < subCategoryHM[0].size(); i++) {
                subCategoryArrayList.add(subCategoryHM[0].keySet().toArray()[i].toString().trim());
            }
            subCategoryArrayList.add(0, activity.getString(R.string.select_sub_crop_category));
            final ArrayAdapter adapter = new ArrayAdapter(activity, R.layout.spinner_list, subCategoryArrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_sub_crop.setAdapter(adapter);
            int spinnerPositionss = 0;
            if (farmerfamilyPojoArrayList.get(k).getCrop_type_subcatagory_id() != null && !farmerfamilyPojoArrayList.get(k).getCrop_type_subcatagory_id().equals("null") && !farmerfamilyPojoArrayList.get(k).getCrop_type_subcatagory_id().equals("")) {
                String strpos111 = sqliteHelper.getSubCategotyName(Integer.parseInt(farmerfamilyPojoArrayList.get(k).getCrop_type_subcatagory_id()),language);
               // String strpos111 = sqliteHelper.getSubCategotyName(farmerfamilyPojoArrayList.get(k).getCrop_type_subcatagory_id());
                if (strpos111 != null && !strpos111.equals(null) && !strpos111.equals("")) {
                    spinnerPositionss = adapter.getPosition(strpos111);
                    spn_sub_crop.setSelection(spinnerPositionss);
                    spinnerPositionss = 0;

                }
            }


            UnitArrayList.clear();
            if(language.equalsIgnoreCase("hin"))
            {
                UnitNameHM[0] = sqliteHelper.getMasterSpinnerId(4, 2);
            }
            else
            {
                UnitNameHM[0] = sqliteHelper.getMasterSpinnerId(4, 1);
            }

            for (int i = 0; i < UnitNameHM[0].size(); i++) {
                UnitArrayList.add(UnitNameHM[0].keySet().toArray()[i].toString().trim());
            }
            if (isEditable) {
                //UnitArrayList.add(0, unit);
            } else {
                UnitArrayList.add(0, activity.getString(R.string.select_unit));
            }
            final ArrayAdapter Adapter = new ArrayAdapter(activity, R.layout.spinner_list, UnitArrayList);
            Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_Unit.setAdapter(Adapter);
                int spinnerPosition = 0;
                String strpos1 = sqliteHelper.getmasterName(Integer.parseInt(farmerfamilyPojoArrayList.get(k).getUnit_id()),language);
                if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                    spinnerPosition = Adapter.getPosition(strpos1);
                    spn_Unit.setSelection(spinnerPosition);
                    spinnerPosition = 0;

            }

            unitsArrayList.clear();
            if(language.equalsIgnoreCase("hin"))
            {
                unitsNameHM[0] = sqliteHelper.getMasterSpinnerId(3, 2);
            }
            else
            {
                unitsNameHM[0] = sqliteHelper.getMasterSpinnerId(3, 1);
            }

            for (int i = 0; i < unitsNameHM[0].size(); i++) {
                unitsArrayList.add(unitsNameHM[0].keySet().toArray()[i].toString().trim());
            }
            if (isEditable) {
                //UnitArrayList.add(0, unit);
            } else {
                unitsArrayList.add(0, activity.getString(R.string.select_Units));
            }
            final ArrayAdapter Adapterr = new ArrayAdapter(activity, R.layout.spinner_list, unitsArrayList);
            Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_Units.setAdapter(Adapterr);
            int spinnerPositions = 0;
            String strposs1 = sqliteHelper.getmasterName(Integer.parseInt(farmerfamilyPojoArrayList.get(k).getUnits_id()),language);
            if (strpos1 != null || !strposs1.equals(null) || !strposs1.equals("")) {
                spinnerPositions = Adapterr.getPosition(strposs1);
                spn_Units.setSelection(spinnerPositions);
                spinnerPositions = 0;

            }


            cropCategoryList.clear();
            if(language.equalsIgnoreCase("hin"))
            {
                cropCategoryHM[0] = sqliteHelper.getAllCategoryType(2);
            }
            else
            {
                cropCategoryHM[0] = sqliteHelper.getAllCategoryType(1);
            }
            for (int i = 0; i < cropCategoryHM[0].size(); i++) {
                cropCategoryList.add(cropCategoryHM[0].keySet().toArray()[i].toString().trim());

            }
            cropCategoryList.add(0, activity.getString(R.string.select_crop_category));
            ArrayAdapter arrayAdapterCC = new ArrayAdapter(activity,R.layout.spinner_list, cropCategoryList);
            arrayAdapterCC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_crop.setAdapter(arrayAdapterCC);
            int spinnerPosi = 0;
            String strpos11 = sqliteHelper.getCategotyName(Integer.parseInt(farmerfamilyPojoArrayList.get(k).getCrop_name()),language);
            if (strpos11 != null || !strpos11.equals(null) || !strpos11.equals("")) {
                spinnerPosi = arrayAdapterCC.getPosition(strpos11);
                spn_crop.setSelection(spinnerPosi);
                spinnerPosi = 0;

            }








            TextView btn_remove = (TextView) newView.findViewById(R.id.family_remove);
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayoutAddMedicine.removeView(newView);
                    final EditText et_name = newView.findViewById(R.id.et_name);
                    final EditText et_Area = newView.findViewById(R.id.et_Area);
                    final TextView family_remove = newView.findViewById(R.id.family_remove);
                    final Spinner spn_season = newView.findViewById(R.id.spn_season);
                    final Spinner spn_unit = newView.findViewById(R.id.spn_unit);
                  //  String ssss = et_name.getText().toString().trim();
                    String sssss = et_Area.getText().toString().trim();
                    hashMap.remove(sssss);
                   // hashMap.remove(ssss);

                }
            });
            linearLayoutAddMedicine.addView(newView);

        }
        return hashMap;
    }

}
