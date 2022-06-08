package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Bundle;
        import android.text.Html;
        import android.util.Base64;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.TextView;

        import com.sanket.jubifarm.Activity.CropPlanning;
        import com.sanket.jubifarm.Activity.FarmerDeatilActivity;
        import com.sanket.jubifarm.Activity.FarmerRegistration;
        import com.sanket.jubifarm.Activity.HomeAcivity;
        import com.sanket.jubifarm.Activity.LandHoldingActivity;
        import com.sanket.jubifarm.Activity.ViewCropActivity;
        import com.sanket.jubifarm.Adapter.FarmerFamilyAdapter;
        import com.sanket.jubifarm.Livelihood.Model.ParyavaranSakhiRegistrationPojo;
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

public class PS_FarmerDetailsActivity extends AppCompatActivity {
    private TextView tv_Age, mobilenumber, tv_State, tv_District, tv_Block, tv_Village, tv_Pincode, tv_bpl,
            tv_AlternativeLiveHood, tv_Education, tv_EducationDetil, tv_EditProfile, tv_CropMonitoring,
            tv_AgroClimateZone, tv_totalincome, tv_totallandHolding, tv_physicalChallenges,
            tv_caste, tv_Fathername, tv_Ctegory, tv_no_of_migrated, tv_CropDetails, tv_multicroping,
            tv_fertilizer, tv_Irrigation, tv_Address, tv_Religion, tv_NameFarmer, tv_Aadhar_no, tv_Farmer,
            tv_householdNo, mobilenum, tv_date_of_birth, tv_ids_text,tv_cropDetail;
    private SqliteHelper sqliteHelper;
    //    private FarmerFamilyAdapter farmerFamilyAdapter;
    private ArrayList<ParyavaranSakhiRegistrationPojo> paryavaranSakhiRegistrationPojoArrayList;
    private ParyavaranSakhiRegistrationPojo paryavaranSakhiRegistrationPojo;
    private SharedPrefHelper sharedPrefHelper;
    private CircleImageView img_tree;
    private String user_id="",farmer_id, farmer_name="";
    private String farmerId="";
    private String local_id="";

    private String st_caste = "",mobile_no="", state = "",categoryy="",religion="",st_annual_income="",district="", block = "",st_date_of_birth="", village = "",st_agee="",father_husbend_name="",aadhar_noo="",house_hold_no="",st_id_other_name="",
           st_pincode = "", st_bpl="", alternative_livelihood="", education_details="",st_martial_category="",st_physical_challenged="",st_total_landholding="",st_agro_climatic_zone="",st_no_of_member="",addresss="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps_farmer_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(Html.fromHtml("<font color=\"#000000\">" +getString(R.string.FARMER_DETAILS)+ "</font>"));

        initViews();
        /*get intent value here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            farmerId = bundle.getString("farmerId", "");
        }
        //for farmer id
//        Attendance_Approval attendance_approval=new Attendance_Approval();
//        if (sharedPrefHelper.getString("user_type", "").equals("Farmer")) {
//            attendance_approval = sqliteHelper.getFarmerDetails(sharedPrefHelper.getString("user_id", ""));
//            farmerId = attendance_approval.getId();
//
//        } else {
//            attendance_approval = sqliteHelper.getFarmerDetails(user_id);
//            farmerId = attendance_approval.getId();
//
//        }

        setFramerDetailsData();
        //for farmer id
        tv_EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rfd = new Intent(PS_FarmerDetailsActivity.this, FarmerRegistrationForm.class);
                rfd.putExtra("screen_type", "edit_profile");
               // rfd.putExtra("user_id", user_id);
               //rfd.putExtra("farmer_name", user_id);
                rfd.putExtra("farmerId",farmerId);

//                rfd.putExtra("farmer_name",farmer_name);

//                rfd.putExtra("aadhar_no", aadhar_noo);
//                rfd.putExtra("household_no", house_hold_no);
//                rfd.putExtra("id_other_name", st_id_other_name);
//                rfd.putExtra("father_husband_name", father_husbend_name);
//                rfd.putExtra("age", st_agee);
//                rfd.putExtra("date_of_birth", st_date_of_birth);
//                rfd.putExtra("bpl", st_bpl);
//                rfd.putExtra("physical_challenges", st_physical_challenged);
//                rfd.putExtra("annual_income", st_annual_income);
//                rfd.putExtra("alternative_livelihood_id", alternative_livelihood);
//                rfd.putExtra("religion_id", religion);
//                rfd.putExtra("category_id", categoryy);
//                rfd.putExtra("state_id", state);
//                rfd.putExtra("district_id", district);
//                rfd.putExtra("village_id", village);
//                rfd.putExtra("block_id", block);
//                rfd.putExtra("address", addresss);
//                rfd.putExtra("education_id", education_details);
//                rfd.putExtra("pincode", st_pincode);
//                rfd.putExtra("caste", st_caste);
//                rfd.putExtra("martial_category", st_martial_category);
//                rfd.putExtra("total_land_holding", st_total_landholding);
//                rfd.putExtra("agro_climat_zone_id", st_agro_climatic_zone);
//                rfd.putExtra("no_of_member_migrated", st_no_of_member);
//                rfd.putExtra("mobile", mobile_no);
////                rfd.putExtra("farmer_id", farmerId);
////               // rfd.putExtra("fromLogin", "3");
                startActivity(rfd);
            }
        });
    }


    private void setFramerDetailsData() {
//            if (!sharedPrefHelper.getString("user_id", "").equals("")) {
        paryavaranSakhiRegistrationPojo = sqliteHelper.getPSEdit(farmerId);
        farmer_id = String.valueOf(paryavaranSakhiRegistrationPojo.getId());
        if (paryavaranSakhiRegistrationPojo.getFarmer_image() != null && paryavaranSakhiRegistrationPojo.getFarmer_image().length() > 200) {
            byte[] decodedString = Base64.decode(paryavaranSakhiRegistrationPojo.getFarmer_image(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            img_tree.setImageBitmap(bitmap);
        } else if (paryavaranSakhiRegistrationPojo.getFarmer_image() != null && paryavaranSakhiRegistrationPojo.getFarmer_image().length() <= 200) {
            try {
                String base64 = APIClient.IMAGE_PROFILE_URL + paryavaranSakhiRegistrationPojo.getFarmer_image();
                Picasso.with(this).load(base64).placeholder(R.drawable.farmer_female).into(img_tree);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            img_tree.setImageResource(R.drawable.farmer_female);
        }


        tv_NameFarmer.setText(paryavaranSakhiRegistrationPojo.getFarmer_name());
        String otherIdCard = paryavaranSakhiRegistrationPojo.getId_other_name();
        if (otherIdCard != null && otherIdCard.equals("Aadhar Card")) {
            tv_ids_text.setText(getString(R.string.Aadhar_Card));
            tv_Aadhar_no.setText(paryavaranSakhiRegistrationPojo.getAadhar_no());
            //              } else if (otherIdCard != null && otherIdCard.equals("आधार कार्ड")) {
//                    tv_ids_text.setText(getString(R.string.Aadhar_Card));
            tv_Aadhar_no.setText(paryavaranSakhiRegistrationPojo.getAadhar_no());
        } else if (otherIdCard != null && otherIdCard.equals("Other")) {
            String langID = sharedPrefHelper.getString("languageID", "");
            int ID_Card = Integer.parseInt(paryavaranSakhiRegistrationPojo.getId_type_id());
            String other_ids_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", ID_Card, langID);
            tv_ids_text.setText(other_ids_name);
            tv_Aadhar_no.setText(paryavaranSakhiRegistrationPojo.getAadhar_no());
        }

//                else if (otherIdCard != null && otherIdCard.equals("अन्य")) {
//                    String langId = sharedPrefHelper.getString("languageID", "");
//                    int ID_Card = Integer.parseInt(paryavaranSakhiRegistrationPojo.getId_other_name());
//                    String other_ids_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", ID_Card, langId);
//                    tv_ids_text.setText(other_ids_name);
//                    tv_Aadhar_no.setText(paryavaranSakhiRegistrationPojo.getAadhar_no());
//                } else if (otherIdCard != null && otherIdCard.equals("ಇತರರು")) {
//                    String langId = sharedPrefHelper.getString("languageID", "");
//                    int ID_Card = Integer.parseInt(paryavaranSakhiRegistrationPojo.getId_other_name());
//                    String other_ids_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", ID_Card, langId);
//                    tv_ids_text.setText(other_ids_name);
//                    tv_Aadhar_no.setText(paryavaranSakhiRegistrationPojo.getAadhar_no());
//                }
        tv_householdNo.setText(paryavaranSakhiRegistrationPojo.getHousehold_no());
        tv_Fathername.setText(paryavaranSakhiRegistrationPojo.getFather_husband_name());
        tv_Farmer.setText(paryavaranSakhiRegistrationPojo.getFarmer_name());
        mobilenumber.setText(paryavaranSakhiRegistrationPojo.getMobile());
        mobilenum.setText("+91 " + paryavaranSakhiRegistrationPojo.getMobile());
        tv_Age.setText(paryavaranSakhiRegistrationPojo.getAge());
        String incomingDate = paryavaranSakhiRegistrationPojo.getDate_of_birth();
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
        tv_Pincode.setText(paryavaranSakhiRegistrationPojo.getPincode());
        tv_Address.setText(paryavaranSakhiRegistrationPojo.getAddress());
        tv_Religion.setText(paryavaranSakhiRegistrationPojo.getReligion_id());
        tv_totallandHolding.setText(paryavaranSakhiRegistrationPojo.getTotal_land_holding());
        tv_physicalChallenges.setText(paryavaranSakhiRegistrationPojo.getPhysical_challenges());
//                tv_Irrigation.setText(farmerRegistrationPojo.getIrrigation_facility());
//                // tv_CropDetails.setText(farmerRegistrationPojo.getCrop_vegetable_details());
//                tv_multicroping.setText(farmerRegistrationPojo.getMulti_cropping());
//                tv_fertilizer.setText(farmerRegistrationPojo.getFertilizer());
        tv_bpl.setText(paryavaranSakhiRegistrationPojo.getBpl());
        if (paryavaranSakhiRegistrationPojo.getNo_of_member_migrated() != null) {
            tv_no_of_migrated.setText(paryavaranSakhiRegistrationPojo.getNo_of_member_migrated());
//                }
            String language = sharedPrefHelper.getString("languageID", "");

//                    int hmReligion = Integer.parseInt(ParyavaranSakhiRegistrationPojo.getRe());
//                    String religion_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", hmReligion, language);
//                    tv_Religion.setText(religion_name);
            String state_name = sqliteHelper.getNameByIdlocation("state_language", "name", "id", Integer.parseInt(paryavaranSakhiRegistrationPojo.getState_id()), language);
            tv_State.setText(state_name);
            String district_name = sqliteHelper.getNameByIdlocation("district_language", "name", "id", Integer.parseInt(paryavaranSakhiRegistrationPojo.getDistrict_id()), language);
            tv_District.setText(district_name);
            String block_name = sqliteHelper.getNameByIdlocation("block_language", "name", "id", Integer.parseInt(paryavaranSakhiRegistrationPojo.getBlock_id()), language);
            tv_Block.setText(block_name);
            String village_name = sqliteHelper.getNameByIdlocation("village_language", "name", "id", Integer.parseInt(paryavaranSakhiRegistrationPojo.getVillage_id()), language);
            tv_Village.setText(village_name);
            int AgroZone = Integer.parseInt(paryavaranSakhiRegistrationPojo.getAgro_climat_zone_id());
            String zone_name = sqliteHelper.getNameById("master", "master_name", "caption_id", AgroZone);
            tv_AgroClimateZone.setText(zone_name);
            int stralternetLivehood = Integer.parseInt(paryavaranSakhiRegistrationPojo.getAlternative_livelihood_id());
            String livehood_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", stralternetLivehood, language);
            tv_AlternativeLiveHood.setText(livehood_name);
            int hmcaste = Integer.parseInt(paryavaranSakhiRegistrationPojo.getCaste());
            String cast_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", hmcaste, language);
            tv_caste.setText(cast_name);
            int hmEducation = Integer.parseInt(paryavaranSakhiRegistrationPojo.getEducation_id());
            String education_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", hmEducation, language);
            tv_Education.setText(education_name);
            int hmCategory = Integer.parseInt(paryavaranSakhiRegistrationPojo.getMartial_category());
            String category_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", hmCategory, language);
            tv_Ctegory.setText(category_name);
//                     int annualicme = paryavaranSakhiRegistrationPojo.getAnnual_income();
//                    String income_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", annualicme, language);
//                    tv_totalincome.setText(income_name);
        }

//            } else {
//                if (!farmerId.equals("")) {
//                    paryavaranSakhiRegistrationPojo = sqliteHelper.getPSEdit(farmerId);
//                    farmer_id = String.valueOf(paryavaranSakhiRegistrationPojo.getId());
//
//                    if (paryavaranSakhiRegistrationPojo.getFarmer_image() != null && paryavaranSakhiRegistrationPojo.getFarmer_image().length() > 200) {
//                        byte[] decodedString = Base64.decode(paryavaranSakhiRegistrationPojo.getFarmer_image(), Base64.NO_WRAP);
//                        InputStream inputStream = new ByteArrayInputStream(decodedString);
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        img_tree.setImageBitmap(bitmap);
//                    } else if (paryavaranSakhiRegistrationPojo.getFarmer_image() != null && paryavaranSakhiRegistrationPojo.getFarmer_image().length() <= 200) {
//                        try {
//                            String base64 = APIClient.IMAGE_PROFILE_URL + paryavaranSakhiRegistrationPojo.getFarmer_image();
//                            Picasso.with(this).load(base64).placeholder(R.drawable.farmer_female).into(img_tree);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        img_tree.setImageResource(R.drawable.farmer_female);
//
//                    }
//                    tv_NameFarmer.setText(paryavaranSakhiRegistrationPojo.getFarmer_name());
//                    String otherIdCard = paryavaranSakhiRegistrationPojo.getId_other_name();
////                    if (otherIdCard.equals(getString(R.string.Aadhar_Card))) {
////                        tv_ids_text.setText(getString(R.string.Aadhar_Card));
////                        tv_Aadhar_no.setText(paryavaranSakhiRegistrationPojo.getAadhar_no());
////                    } else if (otherIdCard.equals(getString(R.string.Other))) {
////                        int ID_Card = Integer.parseInt(paryavaranSakhiRegistrationPojo.getId_other_name());
////                        String other_ids_name = sqliteHelper.getNameById("master", "master_name", "caption_id", ID_Card);
//////                    tv_ids_text.setText(other_ids_name);
////                        tv_Aadhar_no.setText(paryavaranSakhiRegistrationPojo.getAadhar_no());
////                 }
//                    tv_Aadhar_no.setText(paryavaranSakhiRegistrationPojo.getAadhar_no());
//                    tv_householdNo.setText(paryavaranSakhiRegistrationPojo.getHousehold_no());
//                    tv_Fathername.setText(paryavaranSakhiRegistrationPojo.getFather_husband_name());
//                    tv_EducationDetil.setText(paryavaranSakhiRegistrationPojo.getEducation_id());
//                    tv_physicalChallenges.setText(paryavaranSakhiRegistrationPojo.getPhysical_challenges());
//                    //       tv_handicapped.setText(farmerRegistrationPojo.getHandicapped());
//                    tv_totallandHolding.setText(paryavaranSakhiRegistrationPojo.getTotal_land_holding());
//                    tv_Farmer.setText(paryavaranSakhiRegistrationPojo.getFarmer_name());
//                    mobilenumber.setText(paryavaranSakhiRegistrationPojo.getMobile());
//                    mobilenum.setText("+91 " + paryavaranSakhiRegistrationPojo.getMobile());
//                    tv_Age.setText(paryavaranSakhiRegistrationPojo.getAge());
//                    String incomingDate = paryavaranSakhiRegistrationPojo.getDate_of_birth();
//                    if (incomingDate != null) {
//                        if (incomingDate.length() > 10) {
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            try {
//                                Date newDate = sdf.parse(incomingDate);
//                                sdf = new SimpleDateFormat("dd-MM-yyyy");
//                                String outputDate = sdf.format(newDate);
//                                tv_date_of_birth.setText(outputDate);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            tv_date_of_birth.setText(incomingDate);
//                        }
//                    }
//
//                    String language = sharedPrefHelper.getString("languageID", "");
//
//                    int hmReligion = Integer.parseInt(paryavaranSakhiRegistrationPojo.getReligion_id());
//                    String religion_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", hmReligion, language);
//                    tv_Religion.setText(religion_name);
//
//                    String state_name = sqliteHelper.getNameByIdlocation("state_language", "name", "id", Integer.parseInt(paryavaranSakhiRegistrationPojo.getState_id()), language);
//                    tv_State.setText(state_name);
//
//                    String district_name = sqliteHelper.getNameByIdlocation("district_language", "name", "id", Integer.parseInt(paryavaranSakhiRegistrationPojo.getDistrict_id()), language);
//                    tv_District.setText(district_name);
//
//                    String block_name = sqliteHelper.getNameByIdlocation("block_language", "name", "id", Integer.parseInt(paryavaranSakhiRegistrationPojo.getBlock_id()), language);
//                    tv_Block.setText(block_name);
//
//                    String village_name = sqliteHelper.getNameByIdlocation("village_language", "name", "id", Integer.parseInt(paryavaranSakhiRegistrationPojo.getVillage_id()), language);
//                    tv_Village.setText(village_name);
//
//                    int AgroZone = Integer.parseInt(paryavaranSakhiRegistrationPojo.getAgro_climat_zone_id());
//                    String zone_name = sqliteHelper.getNameById("master", "master_name", "caption_id", AgroZone);
//                    tv_AgroClimateZone.setText(zone_name);
//
//                    int stralternetLivehood = Integer.parseInt(paryavaranSakhiRegistrationPojo.getAlternative_livelihood_id());
//                    String livehood_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", stralternetLivehood, language);
//                    tv_AlternativeLiveHood.setText(livehood_name);
//
//                    int hmcaste = Integer.parseInt(paryavaranSakhiRegistrationPojo.getCaste());
//                    String cast_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", hmcaste, language);
//                    tv_caste.setText(cast_name);
//
////                    int relationShip = paryavaranSakhiRegistrationPojo.getRelation_id();
////                    String relation_name = sqliteHelper.getNameByIdlocation("master", "master_name", "caption_id", relationShip, language);
//                }
//            }

    }

    private void initViews() {
        sharedPrefHelper =new SharedPrefHelper(this);
        tv_EditProfile = findViewById(R.id.tv_EditProfile);
        img_tree = findViewById(R.id.img_tree);
        tv_Fathername = findViewById(R.id.tv_Fathername);
        tv_EducationDetil = findViewById(R.id.tv_EducationDetil);
        tv_AgroClimateZone = findViewById(R.id.tv_AgroClimateZone);
        tv_totalincome = findViewById(R.id.tv_totalincome);
        // tv_totalincome = findViewById(R.id.tv_totalincome);
        tv_totallandHolding = findViewById(R.id.tv_totallandHolding);
        tv_physicalChallenges = findViewById(R.id.tv_physicalChallenges);
        tv_Religion = findViewById(R.id.tv_Religion);
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
        tv_Address = findViewById(R.id.tv_Address);
        tv_AlternativeLiveHood = findViewById(R.id.tv_AlternativeLiveHood);
        tv_no_of_migrated = findViewById(R.id.tv_no_of_migrated);
        tv_Ctegory = findViewById(R.id.tv_Ctegory);
        tv_caste = findViewById(R.id.tv_caste);
        tv_Education = findViewById(R.id.tv_Education);
        tv_NameFarmer = findViewById(R.id.tv_NameFarmer);
        tv_Aadhar_no = findViewById(R.id.tv_Aadhar_no);
        tv_Farmer = findViewById(R.id.tv_Farmer);
        tv_householdNo = findViewById(R.id.tv_householdNo);

        mobilenumber = findViewById(R.id.mobilenumber);
        mobilenum = findViewById(R.id.mobilenum);

        sqliteHelper = new SqliteHelper(this);
        paryavaranSakhiRegistrationPojo = new ParyavaranSakhiRegistrationPojo();

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
                Intent intent = new Intent(PS_FarmerDetailsActivity.this, FarmerRecycle.class);
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