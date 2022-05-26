package com.sanket.jubifarm.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sanket.jubifarm.Adapter.FarmerFamilyAdapter;
import com.sanket.jubifarm.Modal.Attendance_Approval;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class FarmerDeatilActivity extends AppCompatActivity {
    private TextView tv_Age, mobilenumber, tv_State, tv_District, tv_Block, tv_Village, tv_Pincode, tv_bpl,
            tv_AlternativeLiveHood, tv_Education, tv_EducationDetil, tv_EditProfile, tv_CropMonitoring,
            tv_LandDetail, tv_AgroClimateZone, tv_totalincome, tv_totallandHolding, tv_physicalChallenges,
            tv_caste, tv_Fathername, tv_Ctegory, tv_no_of_migrated, tv_CropDetails, tv_multicroping,
            tv_fertilizer, tv_Irrigation, tv_Address, tv_Religion, tv_NameFarmer, tv_Aadhar_no, tv_Farmer,
            tv_householdNo, mobilenum, tv_date_of_birth, tv_ids_text,tv_cropDetail;
    private SqliteHelper sqliteHelper;
    private FarmerFamilyAdapter farmerFamilyAdapter;
    private ArrayList<FarmerRegistrationPojo> farmerRegistrationPojoArrayList;
    private FarmerRegistrationPojo farmerRegistrationPojo;
    private SharedPrefHelper sharedPrefHelper;
    private RecyclerView rv_Familydetail;
    private CircleImageView img_tree;
    private String user_id="", farmer_id;
    private String farmerId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_deatil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle(Html.fromHtml("<font color=\"#000000\">" +getString(R.string.FARMER_DETAILS)+ "</font>"));

        initViews();
        /*get intent value here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user_id = bundle.getString("user_id", "");
        }
        //for farmer id
        Attendance_Approval attendance_approval=new Attendance_Approval();
        if (sharedPrefHelper.getString("user_type", "").equals("Farmer")) {
            attendance_approval = sqliteHelper.getFarmerDetails(sharedPrefHelper.getString("user_id", ""));
            farmerId = attendance_approval.getId();
            tv_CropMonitoring.setVisibility(View.GONE);
            tv_LandDetail.setVisibility(View.GONE);
        } else {
            attendance_approval = sqliteHelper.getFarmerDetails(user_id);
            farmerId = attendance_approval.getId();
            tv_CropMonitoring.setVisibility(View.VISIBLE);
            tv_LandDetail.setVisibility(View.VISIBLE);
        }

        setFramerDetailsData();
        setFarmerFamilyDetails();

        tv_EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rfd = new Intent(FarmerDeatilActivity.this, FarmerRegistration.class);
                rfd.putExtra("screen_type", "edit_profile");
                rfd.putExtra("user_id", user_id);
                rfd.putExtra("farmer_id", farmerId);
                rfd.putExtra("fromLogin","3");
                startActivity(rfd);
            }
        });
        tv_CropMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rfd = new Intent(FarmerDeatilActivity.this, CropPlanning.class);
                rfd.putExtra("farmer_id", farmerId);
                rfd.putExtra("screen_type","farmer_details");
                startActivity(rfd);
                finish();
            }
        });
        tv_LandDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rfd = new Intent(FarmerDeatilActivity.this, LandHoldingActivity.class);
                rfd.putExtra("farmer_id", farmerId);
                rfd.putExtra("screen_type","farmer_details");
                startActivity(rfd);
                finish();
            }
        });
        tv_cropDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rfd = new Intent(FarmerDeatilActivity.this, ViewCropActivity.class);
                rfd.putExtra("farmer_id", farmer_id);
                rfd.putExtra("screen_type", "farmer_details");
                startActivity(rfd);
                finish();
            }
        });
    }

    private void setFramerDetailsData() {
        if (sharedPrefHelper.getString("role_id","").equals("5")) { //role_id = 5 (farmer login)
            if (!sharedPrefHelper.getString("user_id", "").equals("")) {
                farmerRegistrationPojo = sqliteHelper.getFarmerDetailsForEdit(sharedPrefHelper.getString("user_id", ""));
                farmer_id= String.valueOf(farmerRegistrationPojo.getId());
                if (farmerRegistrationPojo.getProfile_photo() != null && farmerRegistrationPojo.getProfile_photo().length()>200) {
                    byte[] decodedString = Base64.decode(farmerRegistrationPojo.getProfile_photo(), Base64.NO_WRAP);
                    InputStream inputStream = new ByteArrayInputStream(decodedString);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    img_tree.setImageBitmap(bitmap);
                } else if (farmerRegistrationPojo.getProfile_photo().length() <=200) {
                    try {
                        String base64 = APIClient.IMAGE_PROFILE_URL + farmerRegistrationPojo.getProfile_photo();
                        Picasso.with(this).load(base64).placeholder(R.drawable.farmer_female).into(img_tree);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    img_tree.setImageResource(R.drawable.farmer_female);
                }


                tv_NameFarmer.setText(farmerRegistrationPojo.getFarmer_name());
                String otherIdCard=farmerRegistrationPojo.getId_other_name();
                if (otherIdCard!=null && otherIdCard.equals("Aadhar Card")) {
                    tv_ids_text.setText(getString(R.string.Aadhar_Card));
                    tv_Aadhar_no.setText(farmerRegistrationPojo.getId_no());
                }else  if (otherIdCard!=null && otherIdCard.equals("आधार कार्ड")) {
                    tv_ids_text.setText(getString(R.string.Aadhar_Card));
                    tv_Aadhar_no.setText(farmerRegistrationPojo.getId_no());
                } else if (otherIdCard!=null && otherIdCard.equals("Other")) {
                    String langID= sharedPrefHelper.getString("languageID","");
                    int ID_Card = Integer.parseInt(farmerRegistrationPojo.getId_type_id());
                    String other_ids_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", ID_Card,langID);
                    tv_ids_text.setText(other_ids_name);
                    tv_Aadhar_no.setText(farmerRegistrationPojo.getId_no());
                }else if (otherIdCard!=null && otherIdCard.equals("अन्य")) {
                    String langId= sharedPrefHelper.getString("languageID","");
                    int ID_Card = Integer.parseInt(farmerRegistrationPojo.getId_type_id());
                    String other_ids_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", ID_Card,langId);
                    tv_ids_text.setText(other_ids_name);
                    tv_Aadhar_no.setText(farmerRegistrationPojo.getId_no());
                }else if (otherIdCard!=null && otherIdCard.equals("ಇತರರು")) {
                    String langId= sharedPrefHelper.getString("languageID","");
                    int ID_Card = Integer.parseInt(farmerRegistrationPojo.getId_type_id());
                    String other_ids_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", ID_Card,langId);
                    tv_ids_text.setText(other_ids_name);
                    tv_Aadhar_no.setText(farmerRegistrationPojo.getId_no());
                }
                tv_householdNo.setText(farmerRegistrationPojo.getHousehold_no());
                tv_Fathername.setText(farmerRegistrationPojo.getFather_husband_name());
                //tv_EducationDetil.setText(farmerRegistrationPojo.getEducation_qualification());
                tv_physicalChallenges.setText(farmerRegistrationPojo.getPhysical_challenges());
           //     tv_handicapped.setText(farmerRegistrationPojo.getHandicapped());
                tv_totallandHolding.setText(farmerRegistrationPojo.getTotal_land_holding());
                tv_Farmer.setText(farmerRegistrationPojo.getFarmer_name());
                mobilenumber.setText(farmerRegistrationPojo.getMobile());
                mobilenum.setText("+91 " + farmerRegistrationPojo.getMobile());
                tv_Age.setText(farmerRegistrationPojo.getAge());
                String incomingDate = farmerRegistrationPojo.getDate_of_birth();
                if (incomingDate != null) {
                if (incomingDate.length() > 10) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date newDate = sdf.parse(incomingDate);
                            sdf = new SimpleDateFormat("dd-MM-yyyy");
                            String outputDate = sdf.format(newDate);
                            tv_date_of_birth.setText(outputDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                } else {
                    tv_date_of_birth.setText(incomingDate);
                }
                }
                tv_Pincode.setText(farmerRegistrationPojo.getPincode());
                tv_Address.setText(farmerRegistrationPojo.getAddress());
                tv_Irrigation.setText(farmerRegistrationPojo.getIrrigation_facility());
               // tv_CropDetails.setText(farmerRegistrationPojo.getCrop_vegetable_details());
                tv_multicroping.setText(farmerRegistrationPojo.getMulti_cropping());
                tv_fertilizer.setText(farmerRegistrationPojo.getFertilizer());
                tv_bpl.setText(farmerRegistrationPojo.getBpl());
                if (farmerRegistrationPojo.getNof_member_migrated() != null) {
                    tv_no_of_migrated.setText(farmerRegistrationPojo.getNof_member_migrated());
                }
                String language = sharedPrefHelper.getString("languageID","");

                int hmReligion = Integer.parseInt(farmerRegistrationPojo.getReligion_id());
                String religion_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", hmReligion,language);
                tv_Religion.setText(religion_name);
                String state_name = sqliteHelper.getNameByIdlocation("state_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getState_id()),language);
                tv_State.setText(state_name);
                String district_name = sqliteHelper.getNameByIdlocation("district_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getDistrict_id()),language);
                tv_District.setText(district_name);
                String block_name = sqliteHelper.getNameByIdlocation("block_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getBlock_id()),language);
                tv_Block.setText(block_name);
                String village_name = sqliteHelper.getNameByIdlocation("village_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getVillage_id()),language);
                tv_Village.setText(village_name);
                int AgroZone = Integer.parseInt(farmerRegistrationPojo.getAgro_climat_zone_id());
                String zone_name = sqliteHelper.getNameById("master", "master_name", "caption_id", AgroZone);
                tv_AgroClimateZone.setText(zone_name);
                int stralternetLivehood = Integer.parseInt(farmerRegistrationPojo.getAlternative_livelihood_id());
                String livehood_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", stralternetLivehood,language);
                tv_AlternativeLiveHood.setText(livehood_name);
                int hmcaste = Integer.parseInt(farmerRegistrationPojo.getCaste());
                String cast_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", hmcaste,language);
                tv_caste.setText(cast_name);
                int hmEducation = Integer.parseInt(farmerRegistrationPojo.getEducation_id());
                String education_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", hmEducation,language);
                tv_Education.setText(education_name);
                int hmCategory = Integer.parseInt(farmerRegistrationPojo.getCategory_id());
                String category_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", hmCategory,language);
                tv_Ctegory.setText(category_name);
                int annualicme = farmerRegistrationPojo.getAnnual_income();
                String income_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", annualicme,language);
                tv_totalincome.setText(income_name);
            }
        } else {
            if (!user_id.equals("")) {
                farmerRegistrationPojo = sqliteHelper.getFarmerDetailsForEdit(user_id);
                farmer_id= String.valueOf(farmerRegistrationPojo.getId());

                if (farmerRegistrationPojo.getProfile_photo() != null && farmerRegistrationPojo.getProfile_photo().length()>200) {
                    byte[] decodedString = Base64.decode(farmerRegistrationPojo.getProfile_photo(), Base64.NO_WRAP);
                    InputStream inputStream = new ByteArrayInputStream(decodedString);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    img_tree.setImageBitmap(bitmap);
                } else if (farmerRegistrationPojo.getProfile_photo().length() <=200) {
                    try {
                        String base64 = APIClient.IMAGE_PROFILE_URL + farmerRegistrationPojo.getProfile_photo();
                        Picasso.with(this).load(base64).placeholder(R.drawable.farmer_female).into(img_tree);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    img_tree.setImageResource(R.drawable.farmer_female);

                }
                tv_NameFarmer.setText(farmerRegistrationPojo.getFarmer_name());
                String otherIdCard=farmerRegistrationPojo.getId_other_name();
                if (otherIdCard.equals(getString(R.string.Aadhar_Card))) {
                    tv_ids_text.setText(getString(R.string.Aadhar_Card));
                    tv_Aadhar_no.setText(farmerRegistrationPojo.getId_no());
                } else if (otherIdCard.equals(getString(R.string.Other))) {
                    int ID_Card = Integer.parseInt(farmerRegistrationPojo.getId_type_id());
                    String other_ids_name = sqliteHelper.getNameById("master", "master_name", "caption_id", ID_Card);
                    tv_ids_text.setText(other_ids_name);
                    tv_Aadhar_no.setText(farmerRegistrationPojo.getId_no());
                }
                tv_Aadhar_no.setText(farmerRegistrationPojo.getId_no());
                tv_householdNo.setText(farmerRegistrationPojo.getHousehold_no());
                tv_Fathername.setText(farmerRegistrationPojo.getFather_husband_name());
                //tv_EducationDetil.setText(farmerRegistrationPojo.getEducation_qualification());
                tv_physicalChallenges.setText(farmerRegistrationPojo.getPhysical_challenges());
         //       tv_handicapped.setText(farmerRegistrationPojo.getHandicapped());
                tv_totallandHolding.setText(farmerRegistrationPojo.getTotal_land_holding());
                tv_Farmer.setText(farmerRegistrationPojo.getFarmer_name());
                mobilenumber.setText(farmerRegistrationPojo.getMobile());
                mobilenum.setText("+91 " + farmerRegistrationPojo.getMobile());
                   tv_Age.setText(farmerRegistrationPojo.getAge());
                String incomingDate = farmerRegistrationPojo.getDate_of_birth();
                if (incomingDate != null) {
                if (incomingDate.length() > 10) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date newDate = sdf.parse(incomingDate);
                            sdf = new SimpleDateFormat("dd-MM-yyyy");
                            String outputDate = sdf.format(newDate);
                            tv_date_of_birth.setText(outputDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                } else {
                    tv_date_of_birth.setText(incomingDate);
                }
                }
                tv_Pincode.setText(farmerRegistrationPojo.getPincode());
                tv_Address.setText(farmerRegistrationPojo.getAddress());
                tv_Irrigation.setText(farmerRegistrationPojo.getIrrigation_facility());
              //  tv_CropDetails.setText(farmerRegistrationPojo.getCrop_vegetable_details());
                tv_multicroping.setText(farmerRegistrationPojo.getMulti_cropping());
                tv_fertilizer.setText(farmerRegistrationPojo.getFertilizer());
                tv_bpl.setText(farmerRegistrationPojo.getBpl());
                if (farmerRegistrationPojo.getNof_member_migrated() != null) {
                    tv_no_of_migrated.setText(farmerRegistrationPojo.getNof_member_migrated());
                }
                String language = sharedPrefHelper.getString("languageID","");

                int hmReligion = Integer.parseInt(farmerRegistrationPojo.getReligion_id());
                String religion_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", hmReligion,language);
                tv_Religion.setText(religion_name);

                String state_name = sqliteHelper.getNameByIdlocation("state_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getState_id()),language);
                tv_State.setText(state_name);

                String district_name = sqliteHelper.getNameByIdlocation("district_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getDistrict_id()),language);
                tv_District.setText(district_name);

                String block_name = sqliteHelper.getNameByIdlocation("block_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getBlock_id()),language);
                tv_Block.setText(block_name);

                String village_name = sqliteHelper.getNameByIdlocation("village_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getVillage_id()),language);
                tv_Village.setText(village_name);

                int AgroZone = Integer.parseInt(farmerRegistrationPojo.getAgro_climat_zone_id());
                String zone_name = sqliteHelper.getNameById("master", "master_name", "caption_id", AgroZone);
                tv_AgroClimateZone.setText(zone_name);

                int stralternetLivehood = Integer.parseInt(farmerRegistrationPojo.getAlternative_livelihood_id());
                String livehood_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", stralternetLivehood,language);
                tv_AlternativeLiveHood.setText(livehood_name);

                int hmcaste = Integer.parseInt(farmerRegistrationPojo.getCaste());
                String cast_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", hmcaste,language);
                tv_caste.setText(cast_name);

                int hmEducation = Integer.parseInt(farmerRegistrationPojo.getEducation_id());
                String education_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", hmEducation,language);
                tv_Education.setText(education_name);

                int hmCategory = Integer.parseInt(farmerRegistrationPojo.getCategory_id());
                String category_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", hmCategory,language);
                tv_Ctegory.setText(category_name);

                int annualicme = farmerRegistrationPojo.getAnnual_income();
                String income_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", annualicme,language);
                tv_totalincome.setText(income_name);

                int relationShip = farmerRegistrationPojo.getRelation_id();
                String relation_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", relationShip,language);
            }
        }
    }

    private void setFarmerFamilyDetails() {
        if (sharedPrefHelper.getString("role_id","").equals("5")) {
            if (!sharedPrefHelper.getString("user_id", "").equals("")) {
                farmerRegistrationPojoArrayList = sqliteHelper.getFarmerFamily(sharedPrefHelper.getString("user_id", ""));
                farmerFamilyAdapter = new FarmerFamilyAdapter(this, farmerRegistrationPojoArrayList);
                rv_Familydetail.setHasFixedSize(true);
                rv_Familydetail.setLayoutManager(new LinearLayoutManager(this));
                rv_Familydetail.setAdapter(farmerFamilyAdapter);
            }
        } else {
            if (!user_id.equals("")) {
                farmerRegistrationPojoArrayList = sqliteHelper.getFarmerFamily(user_id);

                farmerFamilyAdapter = new FarmerFamilyAdapter(this, farmerRegistrationPojoArrayList);
                rv_Familydetail.setHasFixedSize(true);
                rv_Familydetail.setLayoutManager(new LinearLayoutManager(this));
                rv_Familydetail.setAdapter(farmerFamilyAdapter);
            }
        }
    }

    private void initViews() {
        sharedPrefHelper =new SharedPrefHelper(this);
        tv_EditProfile = findViewById(R.id.tv_EditProfile);
        img_tree = findViewById(R.id.img_tree);
        tv_CropMonitoring = findViewById(R.id.tv_CropMonitoring);
        tv_LandDetail = findViewById(R.id.tv_LandDetail);
        tv_Fathername = findViewById(R.id.tv_Fathername);
        tv_AgroClimateZone = findViewById(R.id.tv_AgroClimateZone);
        tv_totalincome = findViewById(R.id.tv_totalincome);
        tv_totallandHolding = findViewById(R.id.tv_totallandHolding);
        tv_physicalChallenges = findViewById(R.id.tv_physicalChallenges);
   //   tv_handicapped = findViewById(R.id.tv_handicapped);
        tv_Age = findViewById(R.id.tv_Age);
        tv_date_of_birth = findViewById(R.id.tv_date_of_birth);
        tv_ids_text = findViewById(R.id.tv_ids_text);
        tv_State = findViewById(R.id.tv_State);
        tv_District = findViewById(R.id.tv_District);
        tv_Block = findViewById(R.id.tv_Block);
        tv_Village = findViewById(R.id.tv_Village);
        tv_Pincode = findViewById(R.id.tv_Pincode);
        tv_bpl = findViewById(R.id.tv_bpl);
        tv_fertilizer = findViewById(R.id.tv_fertilizer);
        tv_CropDetails = findViewById(R.id.tv_CropDetails);
        tv_Irrigation = findViewById(R.id.tv_Irrigation);
        tv_Address = findViewById(R.id.tv_Address);
        tv_AlternativeLiveHood = findViewById(R.id.tv_AlternativeLiveHood);
        tv_no_of_migrated = findViewById(R.id.tv_no_of_migrated);
        tv_multicroping = findViewById(R.id.tv_multicroping);
        tv_Ctegory = findViewById(R.id.tv_Ctegory);
        tv_EducationDetil = findViewById(R.id.tv_EducationDetil);
        tv_Education = findViewById(R.id.tv_Education);
        tv_caste = findViewById(R.id.tv_caste);
        tv_Religion = findViewById(R.id.tv_Religion);
        tv_cropDetail = findViewById(R.id.tv_cropDetail);
        tv_NameFarmer = findViewById(R.id.tv_NameFarmer);
        tv_Aadhar_no = findViewById(R.id.tv_Aadhar_no);
        tv_Farmer = findViewById(R.id.tv_Farmer);
        tv_householdNo = findViewById(R.id.tv_householdNo);
        rv_Familydetail = findViewById(R.id.rv_Familydetail);

        mobilenumber = findViewById(R.id.mobilenumber);
        mobilenum = findViewById(R.id.mobilenum);

        sqliteHelper = new SqliteHelper(this);
        farmerRegistrationPojo = new FarmerRegistrationPojo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}