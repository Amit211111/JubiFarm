package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Livelihood.Model.ParyavaranSakhiRegistrationPojo;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.sanket.jubifarm.utils.CommonClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmerRegistrationForm extends AppCompatActivity {

    Spinner IdCard,State, District, Village, Block, religion, cast, Category, annualIncome,
            Agrozone, alternetLivehood;


    private static final int CAMERA_REQUEST=1888;
    String base64;
    ImageView img_selection_pencil;
    CardView imageView_profile;
    Button alldataSubmit;
    CheckBox term_condition;
    LinearLayout ll_aadhar,ll_other,ll_age,ll_dob,rl_profile_image;
    EditText householdNo,FarmerAge,Farmerdob,FarmerName,AadharNo,husbandFatherName,mobileNumber,Address,pincode,TotalLandHoldingArea,NoOfMembers;
    RadioGroup rg_IdCard,rg_age,rg_bpls,rg_PhysicalChallenges;
    RadioButton rb_AadharCard,rb_otherNationalIdCard,rb_age,rb_dob,rb_BPLYes,rb_BPLNo,rb_PhysicalChallengesNo,rb_PhysicalChallengesYes;
    SqliteHelper sqliteHelper;
    ParyavaranSakhiRegistrationPojo paryavaranSakhiRegistrationPojo;
    String str_PhysicalChallenges,str_bpl,str_IdCard;
    int mYear,mMonth,mDay,year,month,day;
    DatePickerDialog datePickerDialog;
    private Object FarmerRecycle;
    private ProgressDialog dialog;
    ArrayList<String> AnnualIncomeArrayList = new ArrayList<>();
    HashMap<String, Integer> AnnualIncomeNameHM = new HashMap<>();
    ArrayList<String> IdCardArrayList = new ArrayList<>();
    HashMap<String, Integer> IdCardNameHM = new HashMap<>();
    ArrayList<String> ReligionArrayList = new ArrayList<>();
    HashMap<String, Integer> ReligionNameHM = new HashMap<>();
    ArrayList<String> CasteArrayList = new ArrayList<>();
    HashMap<String, Integer> CasteNameHM = new HashMap<>();
    ArrayList<String> EducationArrayList = new ArrayList<>();
    HashMap<String, Integer> EducationNameHM = new HashMap<>();
    ArrayList<String> CategoryArrayList = new ArrayList<>();
    HashMap<String, Integer> CategoryNameHM = new HashMap<>();
    ArrayList<String> Agro_climaticZoneArrayList = new ArrayList<>();
    HashMap<String, Integer> Agro_climaticZoneNameHM = new HashMap<>();
    ArrayList<String> alternetLivehoodArrayList = new ArrayList<>();
    HashMap<String, Integer> alternetLivehoodNameHM = new HashMap<>();
    HashMap<String, Integer> stateNameHM, districtNameHM, blockNameHM, villageNameHM;
    ArrayList<String> stateArrayList, distrcitArrayList, blockArrayList, villageArrayList;
    int annualicme = 0,ID_Card = 0, hmReligion = 0, hmcaste = 0, hmEducation = 0, hmCategory = 0,
            AgroZone = 0;
    int state_id = 0, district_id = 0, block_id = 0, village_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_registration_form);
        getSupportActionBar().setTitle("Farmer Registration Form");

        intializeAll();
        sqliteHelper = new SqliteHelper(getApplicationContext());
        getGrampanchayatSpinner();

        //All Spinner Value
//        ArrayAdapter state_adapter = new ArrayAdapter(FarmerRegistrationForm.this, R.layout.spinner_list, sp_state);
//        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        State.setAdapter(state_adapter);
//
//        ArrayAdapter district_adapter = new ArrayAdapter(FarmerRegistrationForm.this, R.layout.spinner_list, sp_district);
//        district_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        District.setAdapter(district_adapter);
//
//        ArrayAdapter block_adapter = new ArrayAdapter(FarmerRegistrationForm.this, R.layout.spinner_list, sp_block);
//        block_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Block.setAdapter(block_adapter);
//
//        ArrayAdapter village_adapter = new ArrayAdapter(FarmerRegistrationForm.this, R.layout.spinner_list, sp_village);
//        village_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Village.setAdapter(village_adapter);
//
//        ArrayAdapter religion_adapter = new ArrayAdapter(FarmerRegistrationForm.this, R.layout.spinner_list, sp_religion);
//        religion_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        religion.setAdapter(religion_adapter);
//
//        ArrayAdapter cast_adapter = new ArrayAdapter(FarmerRegistrationForm.this, R.layout.spinner_list, sp_cast);
//        cast_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        cast.setAdapter(cast_adapter);
//
//        ArrayAdapter education_adapter = new ArrayAdapter(FarmerRegistrationForm.this, R.layout.spinner_list, sp_education);
//        education_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        education.setAdapter(education_adapter);
//
//        ArrayAdapter category_adapter = new ArrayAdapter(FarmerRegistrationForm.this, R.layout.spinner_list, sp_category);
//        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Category.setAdapter(category_adapter);
//
//        ArrayAdapter annual_adapter = new ArrayAdapter(FarmerRegistrationForm.this, R.layout.spinner_list, sp_income);
//        annual_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        annualIncome.setAdapter(annual_adapter);
//
//        ArrayAdapter zone_adapter = new ArrayAdapter(FarmerRegistrationForm.this, R.layout.spinner_list, sp_zone);
//        zone_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Agrozone.setAdapter(zone_adapter);
//        ArrayAdapter livelihood_adapter = new ArrayAdapter(FarmerRegistrationForm.this, R.layout.spinner_list, sp_livelihood);
//        livelihood_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        alternetLivehood.setAdapter(livelihood_adapter);
//        ArrayAdapter otherid_adapter = new ArrayAdapter(FarmerRegistrationForm.this, R.layout.spinner_list, sp_otherid);
//        otherid_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        IdCard.setAdapter(otherid_adapter);



        //Image View
        rl_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt, CAMERA_REQUEST);
            }
        });

        //Physical Chalenges Radio Group

        rg_PhysicalChallenges.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.rb_PhysicalChallengesYes:
                        str_PhysicalChallenges = "Yes";
                        break;
                    case R.id.rb_PhysicalChallengesNo:
                        str_PhysicalChallenges = "No";
                        break;
                }
            }
        });
        rg_bpls.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_BPLYes:
                        str_bpl = "Yes";
                        break;
                    case R.id.rb_BPLNo:
                        str_bpl = "No";
                        break;
                }

            }
        });
        rg_IdCard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_AadharCard:
                        ll_aadhar.setVisibility(View.VISIBLE);
                        ll_other.setVisibility(View.GONE);
                        break;
                    case R.id.rb_otherNationalIdCard:
                        ll_aadhar.setVisibility(View.GONE);
                        ll_other.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        rg_age.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_age:
                        ll_age.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_dob:
                        ll_age.setVisibility(View.GONE);
                        ll_dob.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });
//
        Farmerdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Farmerdob.setError(null);
                Farmerdob.clearFocus();
                mYear = year;
                mMonth = month;
                mDay = day;

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR); // current year
                mMonth = c.get(Calendar.MONTH); // current month
                mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(FarmerRegistrationForm.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Farmerdob.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#ff173e6d"));
            }

        });


        alldataSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paryavaranSakhiRegistrationPojo = new ParyavaranSakhiRegistrationPojo();

//                paryavaranSakhiRegistrationPojo.setHousehold_no(householdNo.getText().toString().trim());
//                paryavaranSakhiRegistrationPojo.setFarmer_image(base64);
                paryavaranSakhiRegistrationPojo.setFarmer_name(FarmerName.getText().toString().trim());
                paryavaranSakhiRegistrationPojo.setFather_husband_name(husbandFatherName.getText().toString().trim());
                paryavaranSakhiRegistrationPojo.setAddress(Address.getText().toString().trim());
                paryavaranSakhiRegistrationPojo.setState_id(State.getSelectedItem().toString().trim());
                paryavaranSakhiRegistrationPojo.setDistrict_id(District.getSelectedItem().toString().trim());
                paryavaranSakhiRegistrationPojo.setBlock_id(Block.getSelectedItem().toString().trim());
                paryavaranSakhiRegistrationPojo.setPincode(pincode.getText().toString().trim());
                paryavaranSakhiRegistrationPojo.setVillage_id(Village.getSelectedItem().toString().trim());
                paryavaranSakhiRegistrationPojo.setReligion_id(religion.getSelectedItem().toString().trim());
                paryavaranSakhiRegistrationPojo.setCaste(cast.getSelectedItem().toString().trim());
//                paryavaranSakhiRegistrationPojo.setEducation_id(education.getSelectedItem().toString().trim());
                paryavaranSakhiRegistrationPojo.setCategory_id(Category.getSelectedItem().toString().trim());
                //paryavaranSakhiRegistrationPojo.setId_other_name(IdCard.getSelectedItem().toString().trim());
                //paryavaranSakhiRegistrationPojo.setAadhar_no(AadharNo.getText().toString().trim());
                paryavaranSakhiRegistrationPojo.setAge(FarmerAge.getText().toString().trim());
                //paryavaranSakhiRegistrationPojo.setDate_of_birth(Farmerdob.getText().toString().trim());

                paryavaranSakhiRegistrationPojo.setPhysical_challenges(str_PhysicalChallenges);
                paryavaranSakhiRegistrationPojo.setBpl(str_bpl);

                paryavaranSakhiRegistrationPojo.setTotal_land_holding(TotalLandHoldingArea.getText().toString().trim());
            //    paryavaranSakhiRegistrationPojo.setAnnual_income(annualIncome.getSelectedItem().toString().trim());
                paryavaranSakhiRegistrationPojo.setAgro_climat_zone_id(Agrozone.getSelectedItem().toString().trim());
                paryavaranSakhiRegistrationPojo.setAlternative_livelihood_id(alternetLivehood.getSelectedItem().toString().trim());
                paryavaranSakhiRegistrationPojo.setNo_of_member_migrated(NoOfMembers.getText().toString().trim());

                sqliteHelper.getPSFarmerRegistrationData(paryavaranSakhiRegistrationPojo);

                Intent intent = new Intent(FarmerRegistrationForm.this, FarmerRecycle.class);
                intent .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

//                Gson gson = new Gson();
//                String data = gson.toJson(FarmerRecycle);
//                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//                RequestBody body = RequestBody.create(JSON, data);
//                callFarmerRegistrationAPI(body);
//                Log.e("FarmerRegistration", "registration: " + data);


            }

        });
    }
        

//    private void callFarmerRegistrationAPI(RequestBody body){
//            dialog = ProgressDialog.show(this, "", "Please wait...", true);
//            APIClient.getPsClient().create(JubiForm_API.class).getPsClient(body).enqueue(new Callback<JsonObject>() {
//
//                @Override
//                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().toString());
//                        Log.e("TAG", "onResponse: " + jsonObject.toString());
//                        String success = jsonObject.optString("status");
//                        String message = jsonObject.optString("message");
//                        String lat_insert_id = jsonObject.optString("let_inssert_id");
//                        if (Integer.valueOf(success) == 1)
//                        {
//
//                            Intent intent = new Intent(FarmerRegistrationForm.this, FarmerRecycle.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//
//                            Toast.makeText(getApplicationContext(), "Registration Success" + message, Toast.LENGTH_SHORT).show();
//      //                      sqliteHelper.update("school_list", "id='" + id + "'", lat_insert_id, "id");
//
//                        } else {
//                            Toast.makeText(FarmerRegistrationForm.this, "Farmer Registration Not Register", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        dialog.dismiss();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<JsonObject> call, Throwable t) {
//                    Toast.makeText(FarmerRegistrationForm.this, "Fail", Toast.LENGTH_SHORT).show();
//                    Log.e("FarmerRegistration", "Failure" + t + "," + call);
//                    dialog.dismiss();
//                }
//            });
       // }

    private void intializeAll()
    {
        //All Linear Layout
        ll_aadhar=findViewById(R.id.ll_aadhar);
        ll_other=findViewById(R.id.ll_other);
        ll_age=findViewById(R.id.ll_age);
        ll_dob=findViewById(R.id.ll_dob);
        rl_profile_image=findViewById(R.id.rl_profile_image);
        //Card View
       // imageView_profile=findViewById(R.id.imageView_profile);
        //Submit All Button
        alldataSubmit =findViewById(R.id.alldataSubmit);
        //All Edit Text
        FarmerAge=findViewById(R.id.FarmerAge);
        Farmerdob=findViewById(R.id.Farmerdob);
        householdNo =findViewById(R.id.householdNo);
        FarmerName =findViewById(R.id.FarmerName);
        AadharNo =findViewById(R.id.AadharNo);
        husbandFatherName =findViewById(R.id.husbandFatherName);
        mobileNumber =findViewById(R.id.mobileNumber);
        Address =findViewById(R.id.Address);
        pincode =findViewById(R.id.pincode);
        TotalLandHoldingArea =findViewById(R.id.TotalLandHoldingArea);
        NoOfMembers =findViewById(R.id.NoOfMembers);
        //All Spinner Find
        IdCard=findViewById(R.id.IdCard);
        State =findViewById(R.id.State);
        District =findViewById(R.id.District);
        Block =findViewById(R.id.Block);
        Village =findViewById(R.id.Village);
        religion =findViewById(R.id.religion);
        cast =findViewById(R.id.cast);
 //       education =findViewById(R.id.education);
        Category =findViewById(R.id.Category);
        annualIncome =findViewById(R.id.annualIncome);
        Agrozone =findViewById(R.id.Agrozone);
        alternetLivehood =findViewById(R.id.alternetLivehood);
        //All Radio Group
        rg_bpls=findViewById(R.id.rg_bpls);
        rg_PhysicalChallenges=findViewById(R.id.rg_PhysicalChallenge);
        rg_IdCard=findViewById(R.id.rg_IdCard);
        rg_age=findViewById(R.id.rg_age);

        //All Radio Button
        rb_age=findViewById(R.id.rb_age);
        rb_dob=findViewById(R.id.rb_dob);

        rb_BPLYes=findViewById(R.id.rb_BPLYes);
        rb_BPLNo=findViewById(R.id.rb_BPLNo);
        rb_PhysicalChallengesYes=findViewById(R.id.rb_PhysicalChallengesYes);
        rb_PhysicalChallengesNo=findViewById(R.id.rb_PhysicalChallengesNo);
        rb_AadharCard=findViewById(R.id.rb_AadharCard);
        rb_otherNationalIdCard=findViewById(R.id.rb_otherNationalIdCard);
        //Term Condition
        term_condition=findViewById(R.id.term_condition);
        //IMage view
        img_selection_pencil=findViewById(R.id.img_selection_pencil);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytes = stream.toByteArray();
//
            base64 = encodeTobase64(photo);
            img_selection_pencil.setImageBitmap(photo);
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

    private void getGrampanchayatSpinner() {

        AnnualIncomeArrayList.clear();
        AnnualIncomeNameHM = sqliteHelper.getMasterSpinnerId(1,1);
        for (int i = 0; i < AnnualIncomeNameHM.size(); i++) {
            AnnualIncomeArrayList.add(AnnualIncomeNameHM.keySet().toArray()[i].toString().trim());
        }
//        Collections.sort(blockArrayList);
        AnnualIncomeArrayList.add(0, "Select Annual Income");
        //state spinner choose
        ArrayAdapter panchayat_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, AnnualIncomeArrayList);
        panchayat_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        annualIncome.setAdapter(panchayat_adapter);


        annualIncome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                annualicme = 0;
                if (!annualIncome.getSelectedItem().toString().trim().equalsIgnoreCase("Select Annual Income")) {
                    if (annualIncome.getSelectedItem().toString().trim() != null) {
                        annualicme = Integer.parseInt(String.valueOf(AnnualIncomeNameHM.get(annualIncome.getSelectedItem().toString().trim())));
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}