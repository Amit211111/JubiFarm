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

import com.sanket.jubifarm.Activity.InputOrdiringListActivity;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MyOperationLayoutAddMemeber {
    static SharedPrefHelper sharedPrefHelper;
    Activity activity;
    SqliteHelper sqliteHelper = new SqliteHelper(activity);

    public static HashMap<String, ArrayList<String>> display(final Activity activity) {
        LinearLayout scrollViewlinerLayout = (LinearLayout) activity.findViewById(R.id.linearLayoutAddMedicine);
        scrollViewlinerLayout.setVerticalGravity(View.TEXT_ALIGNMENT_CENTER);
        HashMap<String, ArrayList<String>> prescriptionHM = new HashMap<>();
        ArrayList<String> nameAl = new ArrayList<>();
        ArrayList<String> ageAl = new ArrayList<>();
        ArrayList<String> educationAl = new ArrayList<>();
        ArrayList<String> occupationAl = new ArrayList<>();
        ArrayList<String> genderAl = new ArrayList<>();
        ArrayList<String> monthlyAl = new ArrayList<>();
        ArrayList<String> relationAl = new ArrayList<>();
        for (int i = 0; i < scrollViewlinerLayout.getChildCount(); i++) {
            LinearLayout innerLayout = (LinearLayout) scrollViewlinerLayout.getChildAt(i);
            final EditText et_name = innerLayout.findViewById(R.id.et_name);
            final EditText et_Age = innerLayout.findViewById(R.id.et_Age);
            final Spinner spnr_Education = innerLayout.findViewById(R.id.spnr_Education);
            final Spinner spnr_Occupation = innerLayout.findViewById(R.id.spnr_Occupation);
            final Spinner spn_gender = innerLayout.findViewById(R.id.spn_gender);
            final Spinner spnr_monthly = innerLayout.findViewById(R.id.spnr_monthly);
            final Spinner spnr_relation = innerLayout.findViewById(R.id.spnr_relation);
            sharedPrefHelper = new SharedPrefHelper(activity);
            nameAl.add(et_name.getText().toString().trim());
            ageAl.add(et_Age.getText().toString().trim());
            educationAl.add(spnr_Education.getSelectedItem().toString().trim());
            occupationAl.add(spnr_Occupation.getSelectedItem().toString());
            genderAl.add(spn_gender.getSelectedItem().toString());
            monthlyAl.add(spnr_monthly.getSelectedItem().toString().trim());
            relationAl.add(spnr_relation.getSelectedItem().toString().trim());
        }

        prescriptionHM.put("name", nameAl);
        prescriptionHM.put("age", ageAl);
        prescriptionHM.put("education", educationAl);
        prescriptionHM.put("occupation", occupationAl);
        prescriptionHM.put("gender", genderAl);
        prescriptionHM.put("monthly", monthlyAl);
        prescriptionHM.put("relation", relationAl);

        return prescriptionHM;

    }



    public static HashMap<String, String> add(final Activity activity, Button btn) {
        final HashMap<String, String> hashMap = new HashMap<>();
        SqliteHelper sqliteHelper = new SqliteHelper(activity);
        ArrayList<String> educationList = new ArrayList<>();
        ArrayList<String> monthlyList = new ArrayList<>();
        final HashMap<String, Integer>[] FamiliymonthlyNameHM = new HashMap[]{new HashMap<>()};
        final HashMap<String, Integer>[] FamilyEducationNameHM = new HashMap[]{new HashMap<>()};
        String[] occuptionfamily_dtls = {"Select Occupation", "Occupation", "Doctor", "Engineer", "Business Man", "Student",
                "Other"};
        String[] genderAL = {"Select Gender", "Male", "Female", "Other"};
        String[] genderAk = {"लिंग का चयन करें", "पुरुष", "महिला", "अन्य"};
        String livehood_name = "";
        String relation_name = "";
        boolean isEditable = false;

        ArrayList<String> alternetLivehoodArrayList=new ArrayList<>();
        ArrayList<String> relationArrayList=new ArrayList<>();
        final HashMap<String, Integer>[] alternetLivehoodNameHM = new HashMap[]{new HashMap<>()};
        final HashMap<String, Integer>[] relationHM = new HashMap[]{new HashMap<>()};
        final LinearLayout linearLayoutAddMedicine = (LinearLayout) activity.findViewById(R.id.linearLayoutAddMedicine);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout newView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.addmorefamily, null);
                newView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                final EditText et_name = newView.findViewById(R.id.et_name);
                final EditText et_Age = newView.findViewById(R.id.et_Age);
                final TextView family_remove = newView.findViewById(R.id.family_remove);
                final Spinner spnr_Education = newView.findViewById(R.id.spnr_Education);
                final Spinner spnr_Occupation = newView.findViewById(R.id.spnr_Occupation);
                final Spinner spn_gender = newView.findViewById(R.id.spn_gender);
                final Spinner spnr_monthly = newView.findViewById(R.id.spnr_monthly);
                final Spinner spnr_relation = newView.findViewById(R.id.spnr_relation);


                alternetLivehoodArrayList.clear();
                SharedPrefHelper spf = new SharedPrefHelper(activity);
                String language = spf.getString("languageID","");
                if(language.equalsIgnoreCase("hin"))
                {
                    alternetLivehoodNameHM[0] = sqliteHelper.getMasterSpinnerId(12, 2);
                }
                else
                {
                    alternetLivehoodNameHM[0] = sqliteHelper.getMasterSpinnerId(12, 1);
                }

                for (int i = 0; i < alternetLivehoodNameHM[0].size(); i++) {
                    alternetLivehoodArrayList.add(alternetLivehoodNameHM[0].keySet().toArray()[i].toString().trim());
                }
                if (isEditable) {
                    //alternetLivehoodArrayList.add(0, Id_Card);
                } else {
                    alternetLivehoodArrayList.add(0, activity.getString(R.string.select_alternative_livelihood));
                }

                final ArrayAdapter ll = new ArrayAdapter(activity, R.layout.spinner_list, alternetLivehoodArrayList);
                ll.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnr_Occupation.setAdapter(ll);
                if (isEditable) {
                    int spinnerPosition = 0;
                    String strpos1 = livehood_name;
                    if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                        strpos1 = livehood_name;
                        spinnerPosition = ll.getPosition(strpos1);
                        spnr_Occupation.setSelection(spinnerPosition);
                        spinnerPosition = 0;
                    }
                }





                relationArrayList.clear();
                language = spf.getString("languageID","");
                if(language.equalsIgnoreCase("hin"))
                {
                    relationHM[0] = sqliteHelper.getMasterSpinnerId(21, 2);
                }
                else
                {
                    relationHM[0] = sqliteHelper.getMasterSpinnerId(21, 1);
                }

                for (int i = 0; i < relationHM[0].size(); i++) {
                    relationArrayList.add(relationHM[0].keySet().toArray()[i].toString().trim());
                }
                if (isEditable) {
                    //alternetLivehoodArrayList.add(0, Id_Card);
                } else {
                    relationArrayList.add(0, activity.getString(R.string.select_relation));
                }

                final ArrayAdapter lll = new ArrayAdapter(activity, R.layout.spinner_list, relationArrayList);
                lll.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnr_relation.setAdapter(lll);
                if (isEditable) {
                    int spinnerPosition = 0;
                    String strpos1 = relation_name;
                    if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                        strpos1 = relation_name;
                        spinnerPosition = lll.getPosition(strpos1);
                        spnr_relation.setSelection(spinnerPosition);
                        spinnerPosition = 0;
                    }
                }

                spnr_Occupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (!spnr_Occupation.getSelectedItem().toString().trim().equalsIgnoreCase(activity.getString(R.string.select_alternative_livelihood))) {
                            if (spnr_Occupation.getSelectedItem().toString().trim() != null) {
                                //stralternetLivehood = alternetLivehoodNameHM[0].get(spnr_Occupation.getSelectedItem().toString().trim());
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
               /* final ArrayAdapter occuptionfamily_dtl = new ArrayAdapter(activity, R.layout.spinner_list, occuptionfamily_dtls);
                occuptionfamily_dtl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnr_Occupation.setAdapter(occuptionfamily_dtl);
*/
               if (language.equals("hin")){
                   final ArrayAdapter arrayAdapter = new ArrayAdapter(activity, R.layout.spinner_list, genderAk);
                   arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                   spn_gender.setAdapter(arrayAdapter);
               }else {
                   final ArrayAdapter arrayAdapter = new ArrayAdapter(activity, R.layout.spinner_list, genderAL);
                   arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                   spn_gender.setAdapter(arrayAdapter);
               }
                monthlyList.clear();
                if(language.equalsIgnoreCase("hin"))
                {
                    FamiliymonthlyNameHM[0] = sqliteHelper.getMasterSpinnerId(1, 2);
                }
                else
                {
                    FamiliymonthlyNameHM[0] = sqliteHelper.getMasterSpinnerId(1, 1);
                }

                for (int i = 0; i < FamiliymonthlyNameHM[0].size(); i++) {
                    monthlyList.add(FamiliymonthlyNameHM[0].keySet().toArray()[i].toString().trim());
                }

                final ArrayAdapter xx = new ArrayAdapter(activity, R.layout.spinner_list, monthlyList);
                xx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnr_monthly.setAdapter(xx);


                educationList.clear();
                if(language.equalsIgnoreCase("hin"))
                {
                    FamilyEducationNameHM[0] = sqliteHelper.getMasterSpinnerId(9, 2);
                }
                else
                {
                    FamilyEducationNameHM[0] = sqliteHelper.getMasterSpinnerId(9, 1);
                }

                for (int i = 0; i < FamilyEducationNameHM[0].size(); i++) {
                    educationList.add(FamilyEducationNameHM[0].keySet().toArray()[i].toString().trim());
                }

                final ArrayAdapter x = new ArrayAdapter(activity, R.layout.spinner_list, educationList);
                x.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnr_Education.setAdapter(x);


                TextView btn_remove = (TextView) newView.findViewById(R.id.family_remove);
                btn_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearLayoutAddMedicine.removeView(newView);
                        final EditText et_name = newView.findViewById(R.id.et_name);
                        final EditText et_Age = newView.findViewById(R.id.et_Age);
                        final TextView family_remove = newView.findViewById(R.id.family_remove);
                        final Spinner spnr_Education = newView.findViewById(R.id.spnr_Education);
                        final Spinner spnr_Occupation = newView.findViewById(R.id.spnr_Occupation);
                        final Spinner spn_gender = newView.findViewById(R.id.spn_gender);
                        final Spinner spnr_monthly = newView.findViewById(R.id.spnr_monthly);
                        String ssss = et_name.getText().toString().trim();
                        String sssss = et_Age.getText().toString().trim();
                        hashMap.remove(sssss);
                        hashMap.remove(ssss);

                    }
                });
                linearLayoutAddMedicine.addView(newView);
            }
        });


        return hashMap;
    }

    public static HashMap<String, String> addforEdit(final Activity activity, ArrayList<FarmerRegistrationPojo> farmerfamilyPojoArrayList) {
        final HashMap<String, String> hashMap = new HashMap<>();
        SqliteHelper sqliteHelper = new SqliteHelper(activity);
        ArrayList<String> educationList = new ArrayList<>();
        ArrayList<String> monthlyList = new ArrayList<>();
        final HashMap<String, Integer>[] FamiliymonthlyNameHM = new HashMap[]{new HashMap<>()};
        final HashMap<String, Integer>[] FamilyEducationNameHM = new HashMap[]{new HashMap<>()};
        String[] occuptionfamily_dtls = {"Select Occupation", "Occupation", "Doctor", "Engineer", "Business Man", "Student",
                "Other"};
        String[] genderAL = {"Select Gender", "Male", "Female", "Other"};
        String[] genderAk = {"लिंग का चयन करें", "पुरुष", "महिला", "अन्य"};
        String livehood_name = "";
        boolean isEditable = false;
        ArrayList<String> alternetLivehoodArrayList=new ArrayList<>();
        ArrayList<String> relationArrayList=new ArrayList<>();
        final HashMap<String, Integer>[] alternetLivehoodNameHM = new HashMap[]{new HashMap<>()};
        final HashMap<String, Integer>[] relationHM = new HashMap[]{new HashMap<>()};

        final LinearLayout linearLayoutAddMedicine = (LinearLayout) activity.findViewById(R.id.linearLayoutAddMedicine);
        for (int k = 0; k < farmerfamilyPojoArrayList.size() ; k++) {

            final LinearLayout newView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.addmorefamily, null);
            newView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final EditText et_name = newView.findViewById(R.id.et_name);
            final EditText et_Age = newView.findViewById(R.id.et_Age);
            final TextView family_remove = newView.findViewById(R.id.family_remove);
            final Spinner spnr_Education = newView.findViewById(R.id.spnr_Education);
            final Spinner spnr_Occupation = newView.findViewById(R.id.spnr_Occupation);
            final Spinner spn_gender = newView.findViewById(R.id.spn_gender);
            final Spinner spnr_monthly = newView.findViewById(R.id.spnr_monthly);
            final Spinner spnr_relation = newView.findViewById(R.id.spnr_relation);
            et_name.setText(farmerfamilyPojoArrayList.get(k).getName());
            et_Age.setText(farmerfamilyPojoArrayList.get(k).getAge());
           /* final ArrayAdapter occuptionfamily_dtl = new ArrayAdapter(activity, R.layout.spinner_list, occuptionfamily_dtls);
            occuptionfamily_dtl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnr_Occupation.setAdapter(occuptionfamily_dtl);
            spnr_Occupation.setSelection(farmerfamilyPojoArrayList.get(k).getOccupation());
*/
            alternetLivehoodArrayList.clear();
            SharedPrefHelper spf = new SharedPrefHelper(activity);
            String language = spf.getString("languageID","");
            if(language.equalsIgnoreCase("hin"))
            {
                alternetLivehoodNameHM[0] = sqliteHelper.getMasterSpinnerId(12, 2);
            }
            else
            {
                alternetLivehoodNameHM[0] = sqliteHelper.getMasterSpinnerId(12, 1);
            }

            for (int i = 0; i < alternetLivehoodNameHM[0].size(); i++) {
                alternetLivehoodArrayList.add(alternetLivehoodNameHM[0].keySet().toArray()[i].toString().trim());
            }
            if (isEditable) {
                //alternetLivehoodArrayList.add(0, Id_Card);
            } else {
                alternetLivehoodArrayList.add(0, activity.getString(R.string.select_alternative_livelihood));
            }

            final ArrayAdapter ll = new ArrayAdapter(activity, R.layout.spinner_list, alternetLivehoodArrayList);
            ll.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnr_Occupation.setAdapter(ll);
            int spinnerPosition = 0;
            String livehood_namea=sqliteHelper.getmasterName(farmerfamilyPojoArrayList.get(k).getOccupation(),language);
            String strpos1 = livehood_namea;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = livehood_namea;
                spinnerPosition = ll.getPosition(strpos1);
                spnr_Occupation.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }


            relationArrayList.clear();
            language = spf.getString("languageID","");
            if(language.equalsIgnoreCase("hin"))
            {
                relationHM[0] = sqliteHelper.getMasterSpinnerId(21, 2);
            }
            else
            {
                relationHM[0] = sqliteHelper.getMasterSpinnerId(21, 1);
            }

            for (int i = 0; i < relationHM[0].size(); i++) {
                relationArrayList.add(relationHM[0].keySet().toArray()[i].toString().trim());
            }
            if (isEditable) {
                //alternetLivehoodArrayList.add(0, Id_Card);
            } else {
                relationArrayList.add(0, activity.getString(R.string.select_relation));
            }

            final ArrayAdapter lll = new ArrayAdapter(activity, R.layout.spinner_list, relationArrayList);
            lll.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnr_relation.setAdapter(lll);
            if (farmerfamilyPojoArrayList.get(k).getRelation_id() != 0 && !(farmerfamilyPojoArrayList.get(k).getRelation_id() ==0)) {
                String relation_name = sqliteHelper.getmasterName(Integer.parseInt(String.valueOf(farmerfamilyPojoArrayList.get(k).getRelation_id())), language);
                int spinnerPositions = 0;
                    String strpos111 = relation_name;
                    if (!strpos111.equals("")) {
                        strpos111 = relation_name;
                        spinnerPositions = lll.getPosition(strpos111);
                        spnr_relation.setSelection(spinnerPositions);
                        spinnerPositions = 0;
                    }

            }

            spnr_Occupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (!spnr_Occupation.getSelectedItem().toString().trim().equalsIgnoreCase(activity.getString(R.string.select_alternative_livelihood))) {
                        if (spnr_Occupation.getSelectedItem().toString().trim() != null) {
                            //stralternetLivehood = alternetLivehoodNameHM[0].get(spnr_Occupation.getSelectedItem().toString().trim());
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            if (language.equals("hin")){
                final ArrayAdapter arrayAdapter = new ArrayAdapter(activity, R.layout.spinner_list, genderAk);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_gender.setAdapter(arrayAdapter);
            }else {
                final ArrayAdapter arrayAdapter = new ArrayAdapter(activity, R.layout.spinner_list, genderAL);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_gender.setAdapter(arrayAdapter);
            }
            if ( farmerfamilyPojoArrayList.get(k).getGender()!=null) {
                if (farmerfamilyPojoArrayList.get(k).getGender().equals("Male")) {
                    spn_gender.setSelection(1);
                } else if (farmerfamilyPojoArrayList.get(k).getGender().equals("Female")) {
                    spn_gender.setSelection(2);
                } else if (farmerfamilyPojoArrayList.get(k).getGender().equals("Other")) {
                    spn_gender.setSelection(3);
                }else if (farmerfamilyPojoArrayList.get(k).getGender().equals("पुरुष")) {
                    spn_gender.setSelection(1);
                } else if (farmerfamilyPojoArrayList.get(k).getGender().equals("महिला")) {
                    spn_gender.setSelection(2);
                } else if (farmerfamilyPojoArrayList.get(k).getGender().equals("अन्य")) {
                    spn_gender.setSelection(3);
                }
            }

            monthlyList.clear();
            if(language.equalsIgnoreCase("hin"))
            {
                FamiliymonthlyNameHM[0] = sqliteHelper.getMasterSpinnerId(1, 2);
            }
            else
            {
                FamiliymonthlyNameHM[0] = sqliteHelper.getMasterSpinnerId(1, 1);
            }

            for (int i = 0; i < FamiliymonthlyNameHM[0].size(); i++) {
                monthlyList.add(FamiliymonthlyNameHM[0].keySet().toArray()[i].toString().trim());
            }

            final ArrayAdapter xx = new ArrayAdapter(activity, R.layout.spinner_list, monthlyList);
            xx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnr_monthly.setAdapter(xx);
            int spinnerPositiozns = 0;
            String livehood_naes=sqliteHelper.getmasterName(farmerfamilyPojoArrayList.get(k).getMonthly_income(),language);
            String strpos111 = livehood_naes;
            if (strpos111 != null || !strpos1.equals(null) || !strpos111.equals("")) {
                strpos111 = livehood_naes;
                spinnerPosition = xx.getPosition(strpos111);
                spnr_monthly.setSelection(spinnerPosition);
                spinnerPositiozns = 0;
            }
           // spnr_monthly.setSelection(farmerfamilyPojoArrayList.get(k).getMonthly_income());


            educationList.clear();
            if(language.equalsIgnoreCase("hin"))
            {
                FamilyEducationNameHM[0] = sqliteHelper.getMasterSpinnerId(9, 2);
            }
            else
            {
                FamilyEducationNameHM[0] = sqliteHelper.getMasterSpinnerId(9, 1);
            }

            for (int i = 0; i < FamilyEducationNameHM[0].size(); i++) {
                educationList.add(FamilyEducationNameHM[0].keySet().toArray()[i].toString().trim());
            }

            final ArrayAdapter x = new ArrayAdapter(activity, R.layout.spinner_list, educationList);
            x.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnr_Education.setAdapter(x);
            int spinnerPositions = 0;
            String livehood_names=sqliteHelper.getmasterName(Integer.parseInt(farmerfamilyPojoArrayList.get(k).getEducation_id()),language);
            String strpos11 = livehood_names;
            if (strpos11 != null || !strpos1.equals(null) || !strpos11.equals("")) {
                strpos11 = livehood_names;
                spinnerPosition = x.getPosition(strpos11);
                spnr_Education.setSelection(spinnerPosition);
                spinnerPositions = 0;
            }

            TextView btn_remove = (TextView) newView.findViewById(R.id.family_remove);
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayoutAddMedicine.removeView(newView);
                    final EditText et_name = newView.findViewById(R.id.et_name);
                    final EditText et_Age = newView.findViewById(R.id.et_Age);
                    final TextView family_remove = newView.findViewById(R.id.family_remove);
                    final Spinner spnr_Education = newView.findViewById(R.id.spnr_Education);
                    final Spinner spnr_Occupation = newView.findViewById(R.id.spnr_Occupation);
                    final Spinner spn_gender = newView.findViewById(R.id.spn_gender);
                    final Spinner spnr_monthly = newView.findViewById(R.id.spnr_monthly);
                    String ssss = et_name.getText().toString().trim();
                    String sssss = et_Age.getText().toString().trim();
                    hashMap.remove(sssss);
                    hashMap.remove(ssss);

                }
            });
            linearLayoutAddMedicine.addView(newView);


        }

        return hashMap;
    }





}

