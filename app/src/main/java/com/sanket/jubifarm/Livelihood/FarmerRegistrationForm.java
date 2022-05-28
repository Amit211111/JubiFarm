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

import com.google.android.material.button.MaterialButton;
import com.sanket.jubifarm.Livelihood.Model.ParyavaranSakhiRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class FarmerRegistrationForm extends AppCompatActivity {

    Spinner spnIdCard,State, District, Village, Block, religion, cast, Category, annualIncome,
            Agrozone, alternetLivehood,education;


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
    int annualicme = 0, hmReligion = 0, hmcaste = 0, hmEducation = 0, hmCategory = 0,
            AgroZone = 0, IDCard = 0,alternativeLivelihood = 0;
    int state_id = 0, district_id = 0, block_id = 0, village_id = 0;
    String ID_Card = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_registration_form);
        getSupportActionBar().setTitle("Farmer Registration Form");

        intializeAll();
        sqliteHelper = new SqliteHelper(getApplicationContext());
        getAnnualIncomeSpinner();
        getIdCardSpinner();
        getStateSpinner();
        getDistrictSpinner();
        getBlockSpinner();
        getVillageSpinner();
        getReligionSpinner();
        getCasteSpinner();
        getMartialSpinner();
        getAgroClimateSpinner();
        getAlternativeLivelihoodSpinner();
        getEducationSpinner();
        //  getStateSpinner()



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
                        ID_Card="Aadhar Card";

                        break;
                    case R.id.rb_otherNationalIdCard:
                        ll_aadhar.setVisibility(View.VISIBLE);
                        ll_other.setVisibility(View.VISIBLE);
                        ID_Card="Other";
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

                paryavaranSakhiRegistrationPojo.setHousehold_no(householdNo.getText().toString().trim());
                paryavaranSakhiRegistrationPojo.setFarmer_image(base64);
                paryavaranSakhiRegistrationPojo.setFarmer_name(FarmerName.getText().toString().trim());
                paryavaranSakhiRegistrationPojo.setMobile(mobileNumber.getText().toString().trim());

                paryavaranSakhiRegistrationPojo.setFather_husband_name(husbandFatherName.getText().toString().trim());
                paryavaranSakhiRegistrationPojo.setAddress(Address.getText().toString().trim());
                paryavaranSakhiRegistrationPojo.setState_id(String.valueOf(state_id));
                paryavaranSakhiRegistrationPojo.setDistrict_id(String.valueOf(district_id));
                paryavaranSakhiRegistrationPojo.setBlock_id(String.valueOf(block_id));
                paryavaranSakhiRegistrationPojo.setPincode(pincode.getText().toString().trim());
                paryavaranSakhiRegistrationPojo.setVillage_id(String.valueOf(village_id));
                paryavaranSakhiRegistrationPojo.setReligion_id(String.valueOf(hmReligion));
                paryavaranSakhiRegistrationPojo.setCaste(String.valueOf(hmcaste));
                paryavaranSakhiRegistrationPojo.setEducation_id(String.valueOf(hmEducation));
                paryavaranSakhiRegistrationPojo.setMartial_category(String.valueOf(hmCategory));
                paryavaranSakhiRegistrationPojo.setId_other_name(ID_Card);
                paryavaranSakhiRegistrationPojo.setId_type_id(String.valueOf(IDCard));
                paryavaranSakhiRegistrationPojo.setAadhar_no(AadharNo.getText().toString().trim());
                paryavaranSakhiRegistrationPojo.setAge(FarmerAge.getText().toString().trim());
                paryavaranSakhiRegistrationPojo.setDate_of_birth(Farmerdob.getText().toString().trim());

                paryavaranSakhiRegistrationPojo.setPhysical_challenges(str_PhysicalChallenges);
                paryavaranSakhiRegistrationPojo.setBpl(str_bpl);

                paryavaranSakhiRegistrationPojo.setTotal_land_holding(TotalLandHoldingArea.getText().toString().trim());
                paryavaranSakhiRegistrationPojo.setAnnual_income(String.valueOf(annualicme));
                paryavaranSakhiRegistrationPojo.setAgro_climat_zone_id(String.valueOf(AgroZone));
                paryavaranSakhiRegistrationPojo.setAlternative_livelihood_id(String.valueOf(alternativeLivelihood));
                paryavaranSakhiRegistrationPojo.setNo_of_member_migrated(NoOfMembers.getText().toString().trim());
              //  paryavaranSakhiRegistrationPojo.setMartial_category(Category.getText().toString().trim());

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
        spnIdCard =findViewById(R.id.IdCard);
        State =findViewById(R.id.State);
        District =findViewById(R.id.District);
        Block =findViewById(R.id.Block);
        Village =findViewById(R.id.Village);
        religion =findViewById(R.id.religion);
        education =findViewById(R.id.education);
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
        stateArrayList=new ArrayList<>();
        blockArrayList=new ArrayList<>();
        distrcitArrayList=new ArrayList<>();
        villageArrayList=new ArrayList<>();
        Agro_climaticZoneArrayList=new ArrayList<>();
        alternetLivehoodArrayList=new ArrayList<>();
        EducationArrayList=new ArrayList<>();
        ReligionArrayList=new ArrayList<>();
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

    private void getIdCardSpinner() {

        IdCardArrayList.clear();
        IdCardNameHM = sqliteHelper.getMasterSpinnerId(2,1);
        for (int i = 0; i < IdCardNameHM.size(); i++) {
            IdCardArrayList.add(IdCardNameHM.keySet().toArray()[i].toString().trim());
        }
//        Collections.sort(blockArrayList);
        IdCardArrayList.add(0, "Select Id Card");
        //state spinner choose
        ArrayAdapter panchayat_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, IdCardArrayList);
        panchayat_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIdCard.setAdapter(panchayat_adapter);


        spnIdCard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!spnIdCard.getSelectedItem().toString().trim().equalsIgnoreCase("Select Id Card")) {
                    if (spnIdCard.getSelectedItem().toString().trim() != null) {
                        IDCard = IdCardNameHM.get(spnIdCard.getSelectedItem().toString().trim());
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getAnnualIncomeSpinner() {

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
                        annualicme = AnnualIncomeNameHM.get(annualIncome.getSelectedItem().toString().trim());
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getStateSpinner() {

        stateArrayList.clear();
        stateNameHM = sqliteHelper.getAllState(1);
        for (int i = 0; i < stateNameHM.size(); i++) {
            stateArrayList.add(stateNameHM.keySet().toArray()[i].toString().trim());
        }
//        Collections.sort(blockArrayList);
        stateArrayList.add(0, "Select State");
        //state spinner choose
        ArrayAdapter state_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stateArrayList);
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        State.setAdapter(state_adapter);


        State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                state_id = 0;
                if (!State.getSelectedItem().toString().trim().equalsIgnoreCase("Select State")) {
                    if (State.getSelectedItem().toString().trim() != null) {
                        state_id = stateNameHM.get(State.getSelectedItem().toString().trim());
                    }
                    getDistrictSpinner();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getDistrictSpinner() {

        distrcitArrayList.clear();
        districtNameHM = sqliteHelper.getAllDistrict(state_id,1);
        for (int i = 0; i < districtNameHM.size(); i++) {
            distrcitArrayList.add(districtNameHM.keySet().toArray()[i].toString().trim());
        }
//        Collections.sort(blockArrayList);
        distrcitArrayList.add(0, "Select District");
        //state spinner choose
        ArrayAdapter state_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, distrcitArrayList);
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        District.setAdapter(state_adapter);


        District.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                district_id = 0;
                if (!District.getSelectedItem().toString().trim().equalsIgnoreCase("Select District")) {
                    if (District.getSelectedItem().toString().trim() != null) {
                        district_id = districtNameHM.get(District.getSelectedItem().toString().trim());
                    }
                    getBlockSpinner();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getBlockSpinner() {

        blockArrayList.clear();
        blockNameHM = sqliteHelper.getAllBlock(district_id,1);
        for (int i = 0; i < blockNameHM.size(); i++) {
            blockArrayList.add(blockNameHM.keySet().toArray()[i].toString().trim());
        }
//        Collections.sort(blockArrayList);
        blockArrayList.add(0, "Select Block");
        //state spinner choose
        ArrayAdapter block_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, blockArrayList);
        block_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Block.setAdapter(block_adapter);


        Block.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!Block.getSelectedItem().toString().trim().equalsIgnoreCase("Select Block")) {
                    if (Block.getSelectedItem().toString().trim() != null) {
                        block_id = blockNameHM.get(Block.getSelectedItem().toString().trim());
                    }
                    getVillageSpinner();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getVillageSpinner() {

        villageArrayList.clear();
        villageNameHM = sqliteHelper.getAllBlock(block_id,1);
        for (int i = 0; i < villageNameHM.size(); i++) {
            villageArrayList.add(villageNameHM.keySet().toArray()[i].toString().trim());
        }
//        Collections.sort(blockArrayList);
        villageArrayList.add(0, "Select Village");
        //state spinner choose
        ArrayAdapter villade_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, villageArrayList);
        villade_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Village.setAdapter(villade_adapter);


        Village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                village_id = 0;
                if (!Village.getSelectedItem().toString().trim().equalsIgnoreCase("Select Village")) {
                    if (Village.getSelectedItem().toString().trim() != null) {
                        village_id = villageNameHM.get(Village.getSelectedItem().toString().trim());
                    }

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void getReligionSpinner() {

        ReligionArrayList.clear();
        ReligionNameHM = sqliteHelper.getMasterSpinnerId(7,1);
        for (int i = 0; i < ReligionNameHM.size(); i++) {
            ReligionArrayList.add(ReligionNameHM.keySet().toArray()[i].toString().trim());
        }
//        Collections.sort(blockArrayList);
        ReligionArrayList.add(0, "Select Religion");
        //state spinner choose
        ArrayAdapter block_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ReligionArrayList);
        block_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        religion.setAdapter(block_adapter);


        religion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hmReligion = 0;
                if (!religion.getSelectedItem().toString().trim().equalsIgnoreCase("Select Religion")) {
                    if (religion.getSelectedItem().toString().trim() != null) {
                        hmReligion = ReligionNameHM.get(religion.getSelectedItem().toString().trim());
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void getCasteSpinner() {

        CasteArrayList.clear();
        CasteNameHM = sqliteHelper.getMasterSpinnerId(8,1);
        for (int i = 0; i < CasteNameHM.size(); i++) {
            CasteArrayList.add(CasteNameHM.keySet().toArray()[i].toString().trim());
        }
//        Collections.sort(blockArrayList);
        CasteArrayList.add(0, "Select Caste");
        //state spinner choose
        ArrayAdapter block_caste = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CasteArrayList);
        block_caste.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cast.setAdapter(block_caste);


        cast.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hmcaste = 0;
                if (!cast.getSelectedItem().toString().trim().equalsIgnoreCase("Select Caste")) {
                    if (cast.getSelectedItem().toString().trim() != null) {
                        hmcaste = CasteNameHM.get(cast.getSelectedItem().toString().trim());
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void getMartialSpinner() {

        CategoryArrayList.clear();
        CategoryNameHM = sqliteHelper.getMasterSpinnerId(10,1);
        for (int i = 0; i < CategoryNameHM.size(); i++) {
            CategoryArrayList.add(CategoryNameHM.keySet().toArray()[i].toString().trim());
        }
//        Collections.sort(blockArrayList);
        CategoryArrayList.add(0, "Select Martial Category");
        //state spinner choose
        ArrayAdapter block_category = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CategoryArrayList);
        block_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Category.setAdapter(block_category);


        Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hmCategory = 0;
                if (!Category.getSelectedItem().toString().trim().equalsIgnoreCase("Select Martial Category")) {
                    if (Category.getSelectedItem().toString().trim() != null) {
                        hmCategory = CategoryNameHM.get(Category.getSelectedItem().toString().trim());
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void getAgroClimateSpinner() {

        Agro_climaticZoneArrayList.clear();
        Agro_climaticZoneNameHM = sqliteHelper.getMasterSpinnerId(11,1);
        for (int i = 0; i < Agro_climaticZoneNameHM.size(); i++) {
            Agro_climaticZoneArrayList.add(Agro_climaticZoneNameHM.keySet().toArray()[i].toString().trim());
        }
//        Collections.sort(blockArrayList);
        Agro_climaticZoneArrayList.add(0, "Select Agro Climate Zone");
        //state spinner choose
        ArrayAdapter block_category = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Agro_climaticZoneArrayList);
        block_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Agrozone.setAdapter(block_category);


        Agrozone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AgroZone = 0;
                if (!Agrozone.getSelectedItem().toString().trim().equalsIgnoreCase("Select Agro Climate Zone")) {
                    if (Agrozone.getSelectedItem().toString().trim() != null) {
                        AgroZone = Agro_climaticZoneNameHM.get(Agrozone.getSelectedItem().toString().trim());
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getAlternativeLivelihoodSpinner() {

        alternetLivehoodArrayList.clear();
        alternetLivehoodNameHM = sqliteHelper.getMasterSpinnerId(12,1);
        for (int i = 0; i < alternetLivehoodNameHM.size(); i++) {
            alternetLivehoodArrayList.add(alternetLivehoodNameHM.keySet().toArray()[i].toString().trim());
        }
//        Collections.sort(blockArrayList);
        alternetLivehoodArrayList.add(0, "Select Alternative Livelihood");
        //state spinner choose
        ArrayAdapter block_category = new ArrayAdapter(this, android.R.layout.simple_spinner_item, alternetLivehoodArrayList);
        block_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alternetLivehood.setAdapter(block_category);


        alternetLivehood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                alternativeLivelihood = 0;
                if (!alternetLivehood.getSelectedItem().toString().trim().equalsIgnoreCase("Select Alternative Livelihood")) {
                    if (alternetLivehood.getSelectedItem().toString().trim() != null) {
                        alternativeLivelihood = alternetLivehoodNameHM.get(alternetLivehood.getSelectedItem().toString().trim());
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getEducationSpinner() {

        EducationArrayList.clear();
        EducationNameHM = sqliteHelper.getMasterSpinnerId(9,1);
        for (int i = 0; i < EducationNameHM.size(); i++) {
            EducationArrayList.add(EducationNameHM.keySet().toArray()[i].toString().trim());
        }
//        Collections.sort(blockArrayList);
        EducationArrayList.add(0, "Select Education");
        //state spinner choose
        ArrayAdapter education_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, EducationArrayList);
        education_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        education.setAdapter(education_adapter);


        education.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hmEducation = 0;
                if (!education.getSelectedItem().toString().trim().equalsIgnoreCase("Select Education")) {
                    if (education.getSelectedItem().toString().trim() != null) {
                        hmEducation = EducationNameHM.get(education.getSelectedItem().toString().trim());
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



}