package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Activity.FarmerRegistration;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Livelihood.Model.ParyavaranSakhiRegistrationPojo;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmerRegistrationForm extends AppCompatActivity {

    Spinner spnIdCard,State, District, Village, Block, religion, cast, Category, annualIncome,
            Agrozone, alternetLivehood,education;


    private static final int CAMERA_REQUEST=1888;
    String base64;
    ImageView img_selection_pencil, IV_profile;
   // CardView imageView_profile;
    Button alldataSubmit;
    CheckBox term_condition;
    ArrayList<ParyavaranSakhiRegistrationPojo> paryavaranSakhiRegistrationPojoArrayList = new ArrayList<>();
    LinearLayout ll_aadhar,ll_other,ll_age,ll_dob,rl_profile_image,ll_other_enter;
    EditText et_other_card_value,householdNo,FarmerAge,Farmerdob,FarmerName,AadharNo,husbandFatherName,mobileNumber,Address,pincode,TotalLandHoldingArea,NoOfMembers;
    RadioGroup rg_IdCard,rg_age,rg_bpls,rg_PhysicalChallenges;
    RadioButton rb_AadharCard,rb_otherNationalIdCard,rb_age,rb_dob,rb_BPLYes,rb_BPLNo,rb_PhysicalChallengesNo,rb_PhysicalChallengesYes;
    SqliteHelper sqliteHelper;
    ParyavaranSakhiRegistrationPojo paryavaranSakhiRegistrationPojo;
    ParyavaranSakhiRegistrationPojo editparyavaranSakhiRegistrationPojo;
    String str_PhysicalChallenges,str_bpl,str_IdCard;
    int mYear,mMonth,mDay,year,month,day;
    DatePickerDialog datePickerDialog;
    private String type="",screen_type="";
    ImageView img_addland;

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
    private String local_id;
    boolean isEditable = false;
    private String st_caste = "",mobile_no="",farmer_name="", st_state = "",categoryy="",st_religion="",st_annual_income="",st_district="", st_block = "",st_date_of_birth="", st_village = "",st_age="",father_husbend_name="",st_aadhar_no="",house_hold_no="",st_id_spn_other_name="",
            st_pincode = "", st_bpl="", st_alternative_livelihood="", education_details="",st_martial_category="",st_physical_challenged="",st_total_landholding="",st_agro_climatic_zone="",st_no_of_member="",st_address="";
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_registration_form);
        getSupportActionBar().setTitle("Farmer Registration Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


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

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            local_id = bundle.getString("farmerId", "");
            screen_type = bundle.getString("screen_type", "");
            house_hold_no= bundle.getString("household_no", "");
            st_age = bundle.getString("age", "");
            mobile_no=bundle.getString("mobile","");
            farmer_name=bundle.getString("farmerId","");
            st_aadhar_no = bundle.getString("adhar_no", "");
            father_husbend_name = bundle.getString("father_husband_name", "");
            base64 = bundle.getString("farmer_image", "");
            st_date_of_birth = bundle.getString("date_of_birth", "");
            type = bundle.getString("type", "");
            st_religion = bundle.getString("religion_id", "");
            st_caste = bundle.getString("caste", "");
            st_state = bundle.getString("state_id", "");
            st_block = bundle.getString("block_id", "");
            st_district = bundle.getString("district_id", "");
            st_village = bundle.getString("village_id", "");
            st_pincode = bundle.getString("pincode", "");
            st_bpl = bundle.getString("bpl", "");
            st_alternative_livelihood = bundle.getString("alternative_livelihood_id", "");
            education_details = bundle.getString("education_id", "");
            st_martial_category = bundle.getString("martial_category", "");
            st_physical_challenged = bundle.getString("physical_challenges", "");
            st_total_landholding = bundle.getString("total_land_holding", "");
            st_agro_climatic_zone = bundle.getString("agro_climat_zone_id", "");
            st_no_of_member = bundle.getString("no_of_member_migrated", "");
            st_address = bundle.getString("address", "");
            st_annual_income=bundle.getString("annual_income","");
            st_id_spn_other_name=bundle.getString("id_other_name","");



        }
       //setTextValues();
//

        if (screen_type.equals("edit_profile")) {

            isEditable=true;
            getSupportActionBar().setTitle("Update Farmer Registration Details");
            editparyavaranSakhiRegistrationPojo=new ParyavaranSakhiRegistrationPojo();
            editparyavaranSakhiRegistrationPojo=sqliteHelper.getPSEdit(local_id);

            householdNo.setText(editparyavaranSakhiRegistrationPojo.getHousehold_no());
            FarmerAge.setText(editparyavaranSakhiRegistrationPojo.getAge());
            mobileNumber.setText(editparyavaranSakhiRegistrationPojo.getMobile());
            FarmerName.setText(editparyavaranSakhiRegistrationPojo.getFarmer_name());
            AadharNo.setText(editparyavaranSakhiRegistrationPojo.getAadhar_no());
            husbandFatherName.setText(editparyavaranSakhiRegistrationPojo.getFather_husband_name());
            Farmerdob.setText(editparyavaranSakhiRegistrationPojo.getDate_of_birth());
          //  rb_BPLYes=(editparyavaranSakhiRegistrationPojo.getBpl());
            //Bpl
            String bpl = editparyavaranSakhiRegistrationPojo.getBpl();
            if (bpl.equalsIgnoreCase("Yes")){
                rb_BPLYes.setChecked(true);
                str_bpl="Yes";
            }else {
                rb_BPLNo.setChecked(true);
                str_bpl="No";
            }
            //Physical Challenge
            String physical_challenge=editparyavaranSakhiRegistrationPojo.getPhysical_challenges();
            if (physical_challenge.equalsIgnoreCase("Yes")){
                rb_PhysicalChallengesYes.setChecked(true);
                str_PhysicalChallenges="Yes";
            }else {
                rb_PhysicalChallengesNo.setChecked(true);
                str_PhysicalChallenges="No";
            }
           //ID Card
            String aadhar_card = editparyavaranSakhiRegistrationPojo.getAadhar_no();

            if (!aadhar_card.equalsIgnoreCase("")){
                if(editparyavaranSakhiRegistrationPojo.getId_type_id().equals("0"))
                {
                    rb_AadharCard.setChecked(true);
                    ID_Card = "Aadhar Card";
                    ll_aadhar.setVisibility(View.VISIBLE);
                }
                else {
                    rb_otherNationalIdCard.setChecked(true);
                    ll_other.setVisibility(View.VISIBLE);
                    ll_aadhar.setVisibility(View.VISIBLE);
                    ID_Card = "Other";

                }

            }
            //Age and Dob

            String age= editparyavaranSakhiRegistrationPojo.getAge();

            if (!age.equalsIgnoreCase("")){
                rb_age.setChecked(true);
                ll_age.setVisibility(View.VISIBLE);
                ll_dob.setVisibility(View.GONE);

            }else {
                rb_dob.setChecked(true);
                ll_dob.setVisibility(View.VISIBLE);
                ll_age.setVisibility(View.GONE);


            }

//            String id_card=editparyavaranSakhiRegistrationPojo.getId_other_name();
//            if (id_card.equalsIgnoreCase("Yes")){
//                rb_AadharCard.setChecked(true);
//            }else {
//                rb_otherNationalIdCard.setChecked(true);
//            }

            pincode.setText(editparyavaranSakhiRegistrationPojo.getPincode());
            TotalLandHoldingArea.setText(editparyavaranSakhiRegistrationPojo.getTotal_land_holding());
            NoOfMembers.setText(editparyavaranSakhiRegistrationPojo.getNo_of_member_migrated());
            Address.setText(editparyavaranSakhiRegistrationPojo.getAddress());
            base64 = editparyavaranSakhiRegistrationPojo.getFarmer_image();
            if (base64 != null && base64.length() > 200) {
                byte[] decodedString = Base64.decode(base64, Base64.NO_WRAP);
                InputStream inputStream = new ByteArrayInputStream(decodedString);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                IV_profile.setImageBitmap(bitmap);
            }
//                else if (base64.length() <= 200) {
//                    try {
//                        String url = APIClient.LAND_IMAGE_URL + base64;
//                        Picasso.with(context).load(url).placeholder(R.drawable.land).into(img_addland);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            else {
                IV_profile.setImageResource(R.drawable.land);
            }

            state_id = Integer.parseInt(editparyavaranSakhiRegistrationPojo.getState_id());
            district_id = Integer.parseInt(editparyavaranSakhiRegistrationPojo.getDistrict_id());
            block_id = Integer.parseInt(editparyavaranSakhiRegistrationPojo.getBlock_id());
            village_id = Integer.parseInt(editparyavaranSakhiRegistrationPojo.getVillage_id());
            hmReligion= Integer.parseInt(editparyavaranSakhiRegistrationPojo.getReligion_id());
            hmcaste=Integer.parseInt(editparyavaranSakhiRegistrationPojo.getCaste());
            hmEducation=Integer.parseInt(editparyavaranSakhiRegistrationPojo.getEducation_id());
            hmCategory=Integer.parseInt(editparyavaranSakhiRegistrationPojo.getMartial_category());
            //AgroZone=Integer.parseInt(editparyavaranSakhiRegistrationPojo.getAgro_climat_zone_id());
            alternativeLivelihood=Integer.parseInt(editparyavaranSakhiRegistrationPojo.getAlternative_livelihood_id());
            //spnIdCard.setSelection(Integer.parseInt(editparyavaranSakhiRegistrationPojo.getAddress()));

            getIdCardSpinner();
            annualicme=Integer.parseInt(editparyavaranSakhiRegistrationPojo.getAnnual_income());
            getAnnualIncomeSpinner();

            getStateSpinner();
            getDistrictSpinner();
           getReligionSpinner();
            getCasteSpinner();
            getEducationSpinner();
            getMartialSpinner();
            getAgroClimateSpinner();
            getAlternativeLivelihoodSpinner();



        }

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
                        ll_other_enter.setVisibility(View.GONE);
                        AadharNo.getText().clear();
                        ID_Card = "Aadhar Card";
                        break;
                    case R.id.rb_otherNationalIdCard:
                        ll_aadhar.setVisibility(View.GONE);
                        ll_other.setVisibility(View.VISIBLE);
                        ll_other_enter.setVisibility(View.VISIBLE);
                        ID_Card = "Other";
                        et_other_card_value.getText().clear();
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
                        ll_dob.setVisibility(View.GONE);

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
                if (checkValidation()) {
                    Random random = new Random();
                    int value = random.nextInt(1000);

                    paryavaranSakhiRegistrationPojo = new ParyavaranSakhiRegistrationPojo();

                    paryavaranSakhiRegistrationPojo.setHousehold_no(householdNo.getText().toString().trim());
                    paryavaranSakhiRegistrationPojo.setFarmer_image(base64);
                    paryavaranSakhiRegistrationPojo.setUser_id(sharedPrefHelper.getString("user_id",""));
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
                    paryavaranSakhiRegistrationPojo.setAadhar_no(AadharNo.getText().toString().trim()+et_other_card_value.getText().toString().trim());
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

                    if (screen_type.equals("edit_profile")) {
//                        paryavaranSakhiRegistrationPojo.setFarmer_name(local_id);
                        sqliteHelper.updatePSFarmerRegistrationData(paryavaranSakhiRegistrationPojo, local_id);
                        Intent intent=new Intent(FarmerRegistrationForm.this,FarmerRecycle.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        long id = sqliteHelper.getPSFarmerRegistrationData(paryavaranSakhiRegistrationPojo);


                        Intent intent = new Intent(FarmerRegistrationForm.this, FarmerRecycle.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
//                    Gson gson = new Gson();
//                    String data = gson.toJson(paryavaranSakhiRegistrationPojo);
//                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//                    RequestBody body = RequestBody.create(JSON, data);
//                    sendPS_FarmerRegistrationdata(body, String.valueOf(id));
//                    }
                }else{
                    Toast.makeText(FarmerRegistrationForm.this, "jhdgjhd", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }


        private void sendPS_FarmerRegistrationdata(RequestBody body, String localId) {
            ProgressDialog dialog = ProgressDialog.show(FarmerRegistrationForm.this, "", getString(R.string.Please_wait), true);
            APIClient.getClient().create(JubiForm_API.class).sendPSFarmerRegistrationdata(body).enqueue(new Callback<JsonObject>() {

                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        Log.e("TAG", "onResponse: " + jsonObject.toString());
                        String success = jsonObject.optString("status");
                        String message = jsonObject.optString("message");
                        String lat_insert_id = jsonObject.optString("let_inssert_id");
                        if (Integer.valueOf(success) == 1) {

                            Intent intent = new Intent(FarmerRegistrationForm.this, FarmerRecycle.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), "Registration Success" + message, Toast.LENGTH_SHORT).show();
                            //                      sqliteHelper.update("school_list", "id='" + id + "'", lat_insert_id, "id");

                        } else {
                            Toast.makeText(FarmerRegistrationForm.this, "Farmer Registration Not Register", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(FarmerRegistrationForm.this, "Fail", Toast.LENGTH_SHORT).show();
                    Log.e("FarmerRegistration", "Failure" + t + "," + call);
                    dialog.dismiss();
                }
            });
        }


    private void intializeAll()
    {
        //All Linear

        sharedPrefHelper=new SharedPrefHelper(this);
        ll_other_enter=findViewById(R.id.ll_other_enter);
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
        et_other_card_value=findViewById(R.id.et_other_card_value);
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
        spnIdCard =findViewById(R.id.spnIdCard);
        State =findViewById(R.id.State);
        District =findViewById(R.id.District);
        Block =findViewById(R.id.Block);
        Village =findViewById(R.id.Village);
        religion =findViewById(R.id.religion);
        education =findViewById(R.id.education);
        cast =findViewById(R.id.cast);
        //       education =findViewById(R.id.education);
        Category =findViewById(R.id.Category);
        //imageView_profile=findViewById(R.id.imageView_profile);
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
        IV_profile=findViewById(R.id.imageView_profile);
//        imageView_profile=findViewById(R.id.imageView_profile);
        stateArrayList=new ArrayList<>();
        blockArrayList=new ArrayList<>();
        distrcitArrayList=new ArrayList<>();
        villageArrayList=new ArrayList<>();
        Agro_climaticZoneArrayList=new ArrayList<>();
        alternetLivehoodArrayList=new ArrayList<>();
        EducationArrayList=new ArrayList<>();
        ReligionArrayList=new ArrayList<>();
    }
//    private void setTextValues()
//    {
//        householdNo.setText(house_hold_no);
//        //ll_age.setText(st_age);
//        mobileNumber.setText(mobile_no);
//        AadharNo.setText(st_aadhar_no);
////        tv_training_stream.setText(training_stream);
////        tv_qualification.setText(qualification);
////        tv_training_date.setText(date_of_training);
//
//    }

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
            IV_profile.setImageBitmap(photo);
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
        ArrayAdapter Idcard_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, IdCardArrayList);
        Idcard_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIdCard.setAdapter(Idcard_adapter);

        if (screen_type.equals("edit_profile")) {
            if (!editparyavaranSakhiRegistrationPojo.getId_type_id().equalsIgnoreCase("0")) {
                st_id_spn_other_name = sqliteHelper.getPSIDCardOther(editparyavaranSakhiRegistrationPojo.getId_type_id());
                int pos = Idcard_adapter.getPosition(st_id_spn_other_name.trim());
                spnIdCard.setSelection(pos);
            }
        }


        spnIdCard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                IDCard=0;
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
        ArrayAdapter annual_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, AnnualIncomeArrayList);
        annual_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        annualIncome.setAdapter(annual_adapter);

        if (screen_type.equals("edit_profile")) {
            st_annual_income = sqliteHelper.getPSAnnualincome(editparyavaranSakhiRegistrationPojo.getAnnual_income());
            int pos = annual_adapter.getPosition(st_annual_income.trim());
            annualIncome.setSelection(pos);
        }

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

        if (screen_type.equals("edit_profile")) {
            st_state = sqliteHelper.getPSState(editparyavaranSakhiRegistrationPojo.getState_id());
            int pos = state_adapter.getPosition(st_state);
            State.setSelection(pos);
        }

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

        if (screen_type.equals("edit_profile")) {
            st_district = sqliteHelper.getPSDistrict(editparyavaranSakhiRegistrationPojo.getDistrict_id());
            int pos = state_adapter.getPosition(st_district);
            District.setSelection(pos);
        }
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

        if (screen_type.equals("edit_profile")) {
            st_block = sqliteHelper.getPSBlock(editparyavaranSakhiRegistrationPojo.getBlock_id());
            int pos = block_adapter.getPosition(st_block.trim());
            Block.setSelection(pos);
        }

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
        villageNameHM = sqliteHelper.getAllVillage2(block_id,1);
        for (int i = 0; i < villageNameHM.size(); i++) {
            villageArrayList.add(villageNameHM.keySet().toArray()[i].toString().trim());
        }
        Collections.sort(villageArrayList, String.CASE_INSENSITIVE_ORDER);

//        Collections.sort(blockArrayList);
        villageArrayList.add(0, "Select Village");
        //state spinner choose
        ArrayAdapter villade_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, villageArrayList);
        villade_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Village.setAdapter(villade_adapter);
        if (screen_type.equals("edit_profile")) {
            st_village = sqliteHelper.getPSVillage(editparyavaranSakhiRegistrationPojo.getVillage_id());
            int pos = villade_adapter.getPosition(st_village.trim());
            Village.setSelection(pos);
        }

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
        ArrayAdapter religion_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ReligionArrayList);
        religion_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        religion.setAdapter(religion_adapter);
        if (screen_type.equals("edit_profile")) {
            st_religion = sqliteHelper.getPSReligion(editparyavaranSakhiRegistrationPojo.getReligion_id());
            int pos = religion_adapter.getPosition(st_religion.trim());
            religion.setSelection(pos);
        }

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
        ArrayAdapter caste_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CasteArrayList);
        caste_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cast.setAdapter(caste_adapter);

        if (screen_type.equals("edit_profile")) {
            st_caste = sqliteHelper.getPSCaste(editparyavaranSakhiRegistrationPojo.getCaste());
            int pos = caste_adapter.getPosition(st_caste.trim());
            cast.setSelection(pos);
        }

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
        ArrayAdapter category_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CategoryArrayList);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Category.setAdapter(category_adapter);
        if (screen_type.equals("edit_profile")) {
            st_martial_category = sqliteHelper.getPSMartialCateogry(editparyavaranSakhiRegistrationPojo.getMartial_category());
            int pos = category_adapter.getPosition(st_martial_category.trim());
            Category.setSelection(pos);
        }

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
        if (screen_type.equals("edit_profile")) {
            st_agro_climatic_zone = sqliteHelper.getPSAgrozone(editparyavaranSakhiRegistrationPojo.getAgro_climat_zone_id());
            int pos = block_category.getPosition(st_agro_climatic_zone.trim());
            Agrozone.setSelection(pos);
        }


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
        ArrayAdapter alternative_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, alternetLivehoodArrayList);
        alternative_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alternetLivehood.setAdapter(alternative_adapter);

        if (screen_type.equals("edit_profile")) {
            st_alternative_livelihood = sqliteHelper.getPSAlternativeLivelihood(editparyavaranSakhiRegistrationPojo.getAlternative_livelihood_id());
            int pos = alternative_adapter.getPosition(st_alternative_livelihood.trim());
            alternetLivehood.setSelection(pos);
        }


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

        if (screen_type.equals("edit_profile")) {
            education_details = sqliteHelper.getPSEducation(editparyavaranSakhiRegistrationPojo.getEducation_id());
            int pos = education_adapter.getPosition(education_details.trim());
            education.setSelection(pos);
        }

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


    private boolean checkValidation() {
        boolean ret = true;
        if (!FarmerName.getText().toString().trim().matches("[a-zA-Z ]+")) {
            EditText flagEditfield = FarmerName;
            String msg = getString(R.string.Please_Enter_Farmer_Name);
            FarmerName.setError(msg);
            FarmerName.requestFocus();
            return false;
        }
        if (rb_AadharCard.isChecked() || rb_otherNationalIdCard.isChecked()) {
            if(rb_AadharCard.isChecked()){
                if (AadharNo.getText().toString().trim().length() < 12 ) {
                    EditText flagEditfield = husbandFatherName;
                    String msg = getString(R.string.Please_enter_valid_id);
                    AadharNo.setError(msg);
                    AadharNo.requestFocus();
                    return false;
                }

            }

            else if(rb_otherNationalIdCard.isChecked()){

                if (spnIdCard.getSelectedItemPosition() > 0) {
                    String itemvalue = String.valueOf(spnIdCard.getSelectedItem());
                } else {
                    TextView errorTextview = (TextView) spnIdCard.getSelectedView();
                    errorTextview.setError("Error");
                    errorTextview.requestFocus();
                    return false;
                }

                if (!et_other_card_value.getText().toString().trim().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}") ) {
                    EditText flagEditfield = husbandFatherName;
                    String msg = getString(R.string.Please_enter_valid_id);
                    et_other_card_value.setError(msg);
                    et_other_card_value.requestFocus();

                    return false;
                }


            }

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.Please_select_Id_Number), Toast.LENGTH_SHORT).show();
            return false;
        }



        if (!husbandFatherName.getText().toString().trim().matches("[a-zA-Z ]+")){
            EditText flagEditfield = husbandFatherName;
            String msg = getString(R.string.Please_Enter_Fatherhusband_Name);
            husbandFatherName.setError(msg);
            husbandFatherName.requestFocus();
            return false;
        }

        if (householdNo.getText().toString().trim().length() == 0) {
            EditText flagEditfield = householdNo;
            String msg = getString(R.string.Please_Enter_HouseholdNo);
            householdNo.setError(msg);
            householdNo.requestFocus();
            return false;
        }
        if (mobileNumber.getText().toString().trim().length() < 10) {
            EditText flagEditfield = mobileNumber;
            String msg = getString(R.string.Please_Enter_Valid_Contact_Number);
            mobileNumber.setError(msg);
            mobileNumber.requestFocus();
            return false;
        }
        if (rb_age.isChecked() || rb_dob.isChecked()) {
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.Please_select_DOB_Or_Age), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!FarmerAge.getText().toString().equalsIgnoreCase("") && Double.parseDouble(FarmerAge.getText().toString().trim()) > 120) {
            FarmerAge.setError(" Age Should be between 18  to 120 ");
            FarmerAge.requestFocus();
            ret = false;
        } else if (!FarmerAge.getText().toString().equalsIgnoreCase("") && Double.parseDouble(FarmerAge.getText().toString().trim()) < 18) {
            FarmerAge.setError(" Age Should be between 18  to 120 ");
            FarmerAge.requestFocus();
            ret = false;
        }
        if (rb_age.isChecked()) {
            if (FarmerAge.getText().toString().trim().length() < 0) {
                EditText flagEditfield = FarmerAge;
                String msg = getString(R.string.Please_Enter_ValidAge);
                FarmerAge.setError(msg);
                FarmerAge.requestFocus();
                return false;
            }
        } else {
            if (Farmerdob.getText().toString().trim().length() < 0) {
                EditText flagEditfield = Farmerdob;
                String msg = getString(R.string.Please_Enter_Valid_Date_of_Birth);
                Farmerdob.setError(msg);
                Farmerdob.requestFocus();
                return false;
            }
        }
        if (Address.getText().toString().trim().equalsIgnoreCase("")) {
            EditText flagEditfield = Address;
            String msg = getString(R.string.please_enter_address);
            Address.setError(msg);
            Address.requestFocus();
            return false;
        }
//        if (!et_FarmerName.getText().toString().trim().matches("[a-zA-Z ]+")) {
//            EditText flagEditfield = et_FarmerName;
//            String msg = getString(R.string.Please_Enter_Farmer_Name);
//            et_FarmerName.setError(msg);
//            et_FarmerName.requestFocus();
//            return false;
//        }



        if (State.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(State.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) State.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }

        if (District.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(District.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) District.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }


        if (Block.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(Block.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) Block.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }

        if (Village.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(Village.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) Village.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }
        if (pincode.getText().toString().trim().length() < 6) {
            EditText flagEditfield = pincode;
            String msg = getString(R.string.Please_Enter_Valid_Number);
            pincode.setError(msg);
            pincode.requestFocus();
            return false;
        }
        if (rb_BPLYes.isChecked() || rb_BPLNo.isChecked()) {
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.Please_select_BPL), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (religion.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(religion.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) religion.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }

        if (cast.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(cast.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) cast.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }


        if (education.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(education.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) education.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }

        if (rb_PhysicalChallengesYes.isChecked() || rb_PhysicalChallengesNo.isChecked()) {
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.Please_select_Physical_Challenges), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Category.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(Category.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) Category.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }

        if (TotalLandHoldingArea.getText().toString().trim().length() < 1) {
            EditText flagEditfield = TotalLandHoldingArea;
            String msg = getString(R.string.Please_Enter_Valid_Number);
            TotalLandHoldingArea.setError(msg);
            TotalLandHoldingArea.requestFocus();
            return false;
        }
        if (annualIncome.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(annualIncome.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) annualIncome.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }


        if (alternetLivehood.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(alternetLivehood.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) alternetLivehood.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }


        if (NoOfMembers.getText().toString().trim().length() < 1) {
            EditText flagEditfield = NoOfMembers;
            String msg = getString(R.string.Please_Enter_Valid_Number);
            NoOfMembers.setError(msg);
            NoOfMembers.requestFocus();
            return false;
        }
        if(term_condition.isChecked()){
            ret=true;
        } else{
            ret=false;
            Toast.makeText(FarmerRegistrationForm.this, R.string.terms_and_conmdition, Toast.LENGTH_SHORT).show();
        }
        return ret;
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
                Intent intent = new Intent(FarmerRegistrationForm.this, FarmerRecycle.class);
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