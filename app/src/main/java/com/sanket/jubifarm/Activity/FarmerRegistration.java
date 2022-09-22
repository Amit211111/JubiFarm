 package com.sanket.jubifarm.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sanket.jubifarm.BuildConfig;
import com.sanket.jubifarm.Modal.CropVegitableDetails;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.Modal.UsersPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.cropImage.enums.ImagePickerEnum;
import com.sanket.jubifarm.cropImage.listeners.IImagePickerLister;
import com.sanket.jubifarm.cropImage.utils.FileUtils;
import com.sanket.jubifarm.cropImage.utils.UiHelper;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.sanket.jubifarm.utils.DataAttributes;
import com.sanket.jubifarm.utils.MyOperationLayoutAddCropDetails;
import com.sanket.jubifarm.utils.MyOperationLayoutAddMemeber;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmerRegistration extends AppCompatActivity implements IImagePickerLister {
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    FarmerRegistrationPojo farmerRegistrationPojo;
    public static android.app.Dialog submit_alert;
    private Context context=this;

    UsersPojo usersPojo;
    String IdCard, bpl, PhysicalChallenges, Multicropping, IrrigationFacility, Fertilizer, AnnualIncome;
    String[] occuptionfamily_dtls = {"Select Occupation", "Occupation", "Doctor", "Engineer",
            "Business Man", "Student", "Other"};
    String[] genderAL = {"Select Gender" , "Male", "Female", "Others"};
    String[] genderAk = {"लिंग का चयन करें", "पुरुष", "महिला", "अन्य"};
    String[] genderAM = {"ಲಿಂಗ ಆಯ್ಕೆ", "ಪುರುಷ", "ಹೆಣ್ಣು", "ಇತರೆ"};
    int annualicme = 0, ID_Card = 0, hmReligion = 0, hmcaste = 0, hmEducation = 0, hmCategory = 0,
            AgroZone = 0, stralternetLivehood = 0, FamiliyEducation = 0, Familiymonthly = 0;
    int state_id = 0, district_id = 0, block_id = 0, village_id = 0;
    ArrayList<FarmerRegistrationPojo> farmerRegistrationPojoArrayList = new ArrayList<>();
    ArrayList<FarmerRegistrationPojo> farmerfamilyPojoArrayList = new ArrayList<>();
    ArrayList<CropVegitableDetails> cropVegitableDetailsArrayList = new ArrayList<>();

    ArrayList<String> AnnualIncomeArrayList;
    HashMap<String, Integer> AnnualIncomeNameHM;
    ArrayList<String> IdCardArrayList;
    HashMap<String, Integer> IdCardNameHM;
    ArrayList<String> ReligionArrayList;
    HashMap<String, Integer> ReligionNameHM;
    ArrayList<String> CasteArrayList;
    HashMap<String, Integer> CasteNameHM;
    ArrayList<String> EducationArrayList;
    HashMap<String, Integer> EducationNameHM;
    ArrayList<String> CategoryArrayList;
    HashMap<String, Integer> CategoryNameHM;
    ArrayList<String> Agro_climaticZoneArrayList;
    HashMap<String, Integer> Agro_climaticZoneNameHM;
    ArrayList<String> alternetLivehoodArrayList;
    HashMap<String, Integer> alternetLivehoodNameHM;
    HashMap<String, Integer> relationHM;
    ArrayList<String> relationArrayList;
    ArrayList<String> FamilyEducationArrayList;
    HashMap<String, Integer> FamilyEducationNameHM;
    ArrayList<String> FamiliymonthlyArrayList;
    HashMap<String, Integer> FamiliymonthlyNameHM;
    HashMap<String, Integer> stateNameHM, districtNameHM, blockNameHM, villageNameHM, unitHM,cropCategoryHM,subCategoryHM, unitshm, seasonhm;
    ArrayList<String> stateArrayList, distrcitArrayList, blockArrayList, villageArrayList,cropCategoryList,subCategoryArrayList, unitArrayList;

    boolean isEditable = false;
    Button btn_addFamily, alldataSubmit;
    TextView et_SCan;
    ImageView img_selection_pencil;
    HashMap<String, ArrayList<String>> familyHM;
    HashMap<String, ArrayList<String>> cropDetailHM;
    LinearLayout ll_age, ll_dob, ll_family_details,ll_crop_details;
    RadioGroup rg_age;
    CheckBox ch_term_condition;
    RadioButton rb_age, rb_dob;
    Button btn_addCrop;
    /*enable crop image*/
    private static final int CAMERA_ACTION_PICK_REQUEST_CODE = 610;
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    public static final int CAMERA_STORAGE_REQUEST_CODE = 611;
    public static final int ONLY_CAMERA_REQUEST_CODE = 612;
    public static final int ONLY_STORAGE_REQUEST_CODE = 613;
    private String currentPhotoPath = "";
    private UiHelper uiHelper = new UiHelper();
    private String base64 = "";
    String flag="";

    Spinner spn_IdCard, spn_religion, spn_village, spnFamiliy_monthly, spn_State, spn_district, spn_block, spn_cast,
            spn_annualIncome, spnF_Education, spn_Agrozone, spn_category, spn_alternetLivehood, spn_education,spn_crop,spn_sub_crop, spn_Units,spn_relation;
    EditText et_householdNo, et_address, et_FarmerName, et_Aadhar, et_husbandFatherName, et_mobileNumber,
            et_Educationdetails, et_TotalLandHoldingArea, et_pincode, et_NoOfMembers, et_CropDetails,
            et_name, et_Age;
    RadioGroup rg_IdCard, rg_BPL, rg_PhysicalChallenges, rg_Handicapped, rg_Multicropping, rg_Fertilizer, rg_IrrigationFacility;
    RadioButton rb_AadharCard, rb_otherNationalIdCard, rb_BPLNo, rb_BPLYes, rb_PhysicalChallengesYes,
            rb_PhysicalChallengesNo, rg_HandicappedYes, rg_HandicappedNo ,rb_MulticroppingYes, rb_MulticroppingNo, rb_FertilizerYes, rb_FertilizerNo,
            rb_IrrigationFacilityYes, rb_IrrigationFacilityNo;
    LinearLayout ll_aadhar, ll_other;
    TextView tv_householdNo, tv_disclaimer;
    LinearLayout ll_household;
    String fromLogin = "";
    String pincode = "";

    static EditText et_Farmerdob;
    static EditText et_FarmerAge;
    private Spinner spin_Occupation, spn_gender;
    private CircleImageView imageView_profile;
    private String screen_type = "";
    private String user_id = "";
    private String farmer_id = "";
    private String state_name = "";
    private String district_name = "";
    private String block_name = "";
    private String village_name = "";
    private String religion_name = "";
    private String cast_name = "";
    private String education_name = "";
    private String category_name = "";
    private String income_name = "";
    private String zone_name = "";
    private String livehood_name = "";
    private String other_ids_name = "";
    DatePickerDialog datePickerDialog;
    public static String age_in_month;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String what_you_know = "";
    EditText et_Area, et_quantity;
    Spinner spn_season;
    Spinner spn_Unit;
    String unit = "";
    String season = "";
    String units = "";
    String relation = "";
    String category = "";
    int units_id = 0;
    int unitId = 0;
    int unitsId = 0;
     int relation_id = 0;
     int seasonId = 0;
     int cat_ID = 0;
     int subcropCategory_id = 0;
    ArrayList<String> UnitArrayList = new ArrayList<>();
    ArrayList<String> unitsArrayList = new ArrayList<>();
    ArrayList<String> seasonArrayList = new ArrayList<>();
    ArrayList<String> monthlyList = new ArrayList<>();
    CropVegitableDetails cropVegitableDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initViews();
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR); // current year
        mMonth = c.get(Calendar.MONTH); // current month
        mDay = c.get(Calendar.DAY_OF_MONTH); //current Day.

        AllRadioButtons();
        idcard();
        getAllStateFromTable();
        setStateSpinner();
        setDistrictSpinner();
        setBlockSpinner();
        setVillageSpinner();
        setReligionSpinner();
        setCastSpinner();
        setEducationSpinner();
        setCategorySpinner();
        setAnnualIncomeSpinner();
        setAgroClimaticZoneSpinner();
        setAlternativeLivehoodSpinner();
        setAlternativeLivehoodSpinnerinFamily();
        setOtherIdSpinner();
        setUnitSpinner();
        setCropCategorySpinner();
        setUnitsSpinner();
        setRelationSpinner();
        setSeasonsSpinner();
        setSubcategorySpinner();
        submitAllData();

        /*get intent values here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            screen_type = bundle.getString("screen_type", "");
            user_id = bundle.getString("user_id", "");
            farmer_id = bundle.getString("farmer_id", "");
        }

        if (screen_type.equalsIgnoreCase("edit_profile")) {
            setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.edit_profile) + "</font>"));
            setValuesForEdit();
            setEditableFalse();
            ll_family_details.setVisibility(View.GONE);
            ll_crop_details.setVisibility(View.GONE);
        } else {
            setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.farmer_registration) + "</font>"));
            ll_family_details.setVisibility(View.VISIBLE);
            ll_crop_details.setVisibility(View.VISIBLE);
        }

        et_SCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(FarmerRegistration.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt(getString(R.string.Scan_a_Aadharcard_QR_Code));
                integrator.setResultDisplayDuration(500);
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.initiateScan();
            }
        });

        et_Farmerdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });
    }

    private void setEditableFalse() {
        et_householdNo.setEnabled(false);
        et_householdNo.setTextColor(getResources().getColor(R.color.gray));
    }

    private void setFamilyDetails() {

    }

    private void setOtherIdSpinner() {
        IdCardArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            IdCardNameHM = sqliteHelper.getMasterSpinnerId(2, 2);
        }else if(language.equalsIgnoreCase("kan"))
        {
            IdCardNameHM = sqliteHelper.getMasterSpinnerId(2, 3);
        }else if(language.equalsIgnoreCase("guj"))
        {
            IdCardNameHM = sqliteHelper.getMasterSpinnerId(2, 4);
        }
        else
        {
            IdCardNameHM = sqliteHelper.getMasterSpinnerId(2, 1);
        }

        for (int i = 0; i < IdCardNameHM.size(); i++) {
            IdCardArrayList.add(IdCardNameHM.keySet().toArray()[i].toString().trim());
        }
        Collections.sort(IdCardArrayList);
        if (isEditable) {
            //IdCardArrayList.add(0, Id_Card);
        } else {
            IdCardArrayList.add(0, getString(R.string.select_other_ids));
        }
        final ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_list, IdCardArrayList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_IdCard.setAdapter(aa);

        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = other_ids_name;
            if (!strpos1.equals("")) {
                strpos1 = other_ids_name;
                spinnerPosition = aa.getPosition(strpos1);
                spn_IdCard.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_IdCard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spn_IdCard.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_other_ids))) {
                    if (spn_IdCard.getSelectedItem().toString().trim() != null) {
                        ID_Card = IdCardNameHM.get(spn_IdCard.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setUnitSpinner() {
        UnitArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            unitHM = sqliteHelper.getMasterSpinnerId(4, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            unitHM = sqliteHelper.getMasterSpinnerId(4, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            unitHM = sqliteHelper.getMasterSpinnerId(4, 4);
        }
        else
        {
            unitHM = sqliteHelper.getMasterSpinnerId(4, 1);
        }

        for (int i = 0; i < unitHM.size(); i++) {
            UnitArrayList.add(unitHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            //UnitArrayList.add(0, unit);
        } else {
            UnitArrayList.add(0, getString(R.string.select_unit));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, UnitArrayList);
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
                if (!spn_Unit.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_unit))) {
                    if (spn_Unit.getSelectedItem().toString().trim() != null) {
                        unitId = unitHM.get(spn_Unit.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setUnitsSpinner() {
        unitsArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            unitshm = sqliteHelper.getMasterSpinnerId(3, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            unitshm = sqliteHelper.getMasterSpinnerId(3, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            unitshm = sqliteHelper.getMasterSpinnerId(3, 4);
        }
        else
        {
            unitshm = sqliteHelper.getMasterSpinnerId(3, 1);
        }

        for (int i = 0; i < unitshm.size(); i++) {
            unitsArrayList.add(unitshm.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            //UnitArrayList.add(0, unit);
        } else {
            unitsArrayList.add(0, getString(R.string.select_Units));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, unitsArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_Units.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = units;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = units;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_Units.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_Units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_Units.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_Units))) {
                    if (spn_Units.getSelectedItem().toString().trim() != null) {
                        unitsId = unitshm.get(spn_Units.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setRelationSpinner() {
        relationArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            relationHM = sqliteHelper.getMasterSpinnerId(21, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            relationHM = sqliteHelper.getMasterSpinnerId(21, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            relationHM = sqliteHelper.getMasterSpinnerId(21, 4);
        }
        else
        {
            relationHM = sqliteHelper.getMasterSpinnerId(21, 1);
        }

        for (int i = 0; i < relationHM.size(); i++) {
            relationArrayList.add(relationHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            //UnitArrayList.add(0, unit);
        } else {
            relationArrayList.add(0, getString(R.string.select_relation));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, relationArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_relation.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = relation;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = relation;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_relation.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_relation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_relation.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_relation))) {
                    if (spn_relation.getSelectedItem().toString().trim() != null) {
                        relation_id = relationHM.get(spn_relation.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    private void setSeasonsSpinner() {
        seasonArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            seasonhm = sqliteHelper.getMasterSpinnerId(5, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            seasonhm = sqliteHelper.getMasterSpinnerId(5, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            seasonhm = sqliteHelper.getMasterSpinnerId(5, 4);
        }
        else
        {
            seasonhm = sqliteHelper.getMasterSpinnerId(5, 1);
        }

        for (int i = 0; i < seasonhm.size(); i++) {
            seasonArrayList.add(seasonhm.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            //UnitArrayList.add(0, unit);
        } else {
            seasonArrayList.add(0, getString(R.string.select_season));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, seasonArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_season.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = season;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = season;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_season.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_season.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_season.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_season))) {
                    if (spn_Units.getSelectedItem().toString().trim() != null) {
                        seasonId = seasonhm.get(spn_season.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setCropCategorySpinner() {
        cropCategoryList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            cropCategoryHM = sqliteHelper.getAllCategoryType(2);
        } else  if(language.equalsIgnoreCase("kan"))
        {
            cropCategoryHM = sqliteHelper.getAllCategoryType(3);
        }
        else
        {
            cropCategoryHM = sqliteHelper.getAllCategoryType(1);
        }

        for (int i = 0; i < cropCategoryHM.size(); i++) {
            cropCategoryList.add(cropCategoryHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            //UnitArrayList.add(0, unit);
        } else {
            cropCategoryList.add(0, getString(R.string.select_category));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, cropCategoryList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_crop.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = category;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = category;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_crop.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_crop.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_category))) {
                    if (spn_crop.getSelectedItem().toString().trim() != null) {
                        cat_ID = cropCategoryHM.get(spn_crop.getSelectedItem().toString().trim());
                        getsubcategoey(cat_ID);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getsubcategoey(int state_id) {
        subCategoryArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            subCategoryHM = sqliteHelper.getAllSubCategory(state_id, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            subCategoryHM = sqliteHelper.getAllSubCategory(state_id, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            subCategoryHM = sqliteHelper.getAllSubCategory(state_id, 4);
        }
        else
        {
            subCategoryHM = sqliteHelper.getAllSubCategory(state_id, 1);
        }
        for (int i = 0; i < subCategoryHM.size(); i++) {
            subCategoryArrayList.add(subCategoryHM.keySet().toArray()[i].toString().trim());
        }

        subCategoryArrayList.add(0, getString(R.string.select_sub_crop_category));
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, subCategoryArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_sub_crop.setAdapter(Adapter);
    }

    private void setSubcategorySpinner() {
        spn_sub_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_sub_crop.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_sub_crop_category))) {
                    if (spn_sub_crop.getSelectedItem().toString().trim() != null) {
                        subcropCategory_id = subCategoryHM.get(spn_sub_crop.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    private void setAlternativeLivehoodSpinner() {
        alternetLivehoodArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            alternetLivehoodNameHM = sqliteHelper.getMasterSpinnerId(12, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            alternetLivehoodNameHM = sqliteHelper.getMasterSpinnerId(12, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            alternetLivehoodNameHM = sqliteHelper.getMasterSpinnerId(12, 4);
        }
        else
        {
            alternetLivehoodNameHM = sqliteHelper.getMasterSpinnerId(12, 1);
        }

        for (int i = 0; i < alternetLivehoodNameHM.size(); i++) {
            alternetLivehoodArrayList.add(alternetLivehoodNameHM.keySet().toArray()[i].toString().trim());
        }
        Collections.sort(alternetLivehoodArrayList);
        if (isEditable) {
            //alternetLivehoodArrayList.add(0, Id_Card);
        } else {
            alternetLivehoodArrayList.add(0, getString(R.string.select_alternative_livelihood));
        }

        final ArrayAdapter ll = new ArrayAdapter(this, R.layout.spinner_list, alternetLivehoodArrayList);
        ll.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_alternetLivehood.setAdapter(ll);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = livehood_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = livehood_name;
                spinnerPosition = ll.getPosition(strpos1);
                spn_alternetLivehood.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_alternetLivehood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spn_alternetLivehood.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_alternative_livelihood))) {
                    if (spn_alternetLivehood.getSelectedItem().toString().trim() != null) {
                        stralternetLivehood = alternetLivehoodNameHM.get(spn_alternetLivehood.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setAlternativeLivehoodSpinnerinFamily() {
        alternetLivehoodArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            alternetLivehoodNameHM = sqliteHelper.getMasterSpinnerId(12, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            alternetLivehoodNameHM = sqliteHelper.getMasterSpinnerId(12, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            alternetLivehoodNameHM = sqliteHelper.getMasterSpinnerId(12, 4);
        }
        else
        {
            alternetLivehoodNameHM = sqliteHelper.getMasterSpinnerId(12, 1);
        }

        for (int i = 0; i < alternetLivehoodNameHM.size(); i++) {
            alternetLivehoodArrayList.add(alternetLivehoodNameHM.keySet().toArray()[i].toString().trim());
        }
        Collections.sort(alternetLivehoodArrayList);
        if (isEditable) {
            //alternetLivehoodArrayList.add(0, Id_Card);
        } else {
            alternetLivehoodArrayList.add(0, getString(R.string.select_alternative_livelihood));
        }

        final ArrayAdapter ll = new ArrayAdapter(this, R.layout.spinner_list, alternetLivehoodArrayList);
        ll.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_Occupation.setAdapter(ll);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = livehood_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = livehood_name;
                spinnerPosition = ll.getPosition(strpos1);
                spin_Occupation.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spin_Occupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spin_Occupation.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_alternative_livelihood))) {
                    if (spin_Occupation.getSelectedItem().toString().trim() != null) {
                        stralternetLivehood = alternetLivehoodNameHM.get(spin_Occupation.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void selectDate() {
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mYear = i;
                mMonth = i1;
                mDay = i2;
                Calendar c = Calendar.getInstance();
                c.set(i, i1, i2);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                String dob = sdf1.format(c.getTime());
                String formattedDate = sdf.format(c.getTime());
                et_Farmerdob.setText(formattedDate);
                et_FarmerAge.setText(getAge(dob));
            }

        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    @SuppressLint("NewApi")
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            String dt = day + "-" + month + "-" + year;
            Calendar c = Calendar.getInstance();
            c.set(year, month, day, 0, 0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            String dob = sdf1.format(c.getTime());
            String formattedDate = sdf.format(c.getTime());
            et_Farmerdob.setText(formattedDate);
            et_FarmerAge.setText(getAge(dob));

            /*get age in month for dob*/
            // Using Calendar - calculating number of months between two dates
            Calendar birthDay = new GregorianCalendar(year, month, day);
            Calendar today = new GregorianCalendar();
            today.setTime(new Date());

            int yearsInBetween = today.get(Calendar.YEAR)
                    - birthDay.get(Calendar.YEAR);
            int monthsDiff = today.get(Calendar.MONTH)
                    - birthDay.get(Calendar.MONTH);
            long ageInMonths = yearsInBetween * 12 + monthsDiff;
            long age = yearsInBetween;

            Log.e("ageInMonths", "xnxkjnc: " + ageInMonths);
            Log.e("age", "nkjsans: " + age);
        }
    }

    public static String getAge(String date) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date1 = format.parse(date);
            dob.setTime(date1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        long d = dob.getTimeInMillis();
        int year = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        int month = 0, totalDaysDifference = 0;
        if (today.get(Calendar.MONTH) >= dob.get(Calendar.MONTH)) {
            month = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
        } else {
            month = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
            month = 12 + month;
            year--;
        }

        if (today.get(Calendar.DAY_OF_MONTH) >= dob.get(Calendar.DAY_OF_MONTH)) {
            totalDaysDifference = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);
        } else {
            totalDaysDifference = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);
            totalDaysDifference = 30 + totalDaysDifference;
            month--;
        }
        double age = (year * 12) + month;
        Integer ageInt = (int) age;

        age_in_month = ageInt.toString(); //for months return this.
        int calAge = (Integer.parseInt(age_in_month) / 12); //for years return this.
        return String.valueOf(calAge);
    }

    private void setAgroClimaticZoneSpinner() {
        Agro_climaticZoneArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            Agro_climaticZoneNameHM = sqliteHelper.getMasterSpinnerId(11, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            Agro_climaticZoneNameHM = sqliteHelper.getMasterSpinnerId(11, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            Agro_climaticZoneNameHM = sqliteHelper.getMasterSpinnerId(11, 4);
        }
        else
        {
            Agro_climaticZoneNameHM = sqliteHelper.getMasterSpinnerId(11, 1);
        }

        for (int i = 0; i < Agro_climaticZoneNameHM.size(); i++) {
            Agro_climaticZoneArrayList.add(Agro_climaticZoneNameHM.keySet().toArray()[i].toString().trim());
        }
        Collections.sort(Agro_climaticZoneArrayList);
        if (isEditable) {
            //Agro_climaticZoneArrayList.add(0, Id_Card);
        } else {
            Agro_climaticZoneArrayList.add(0, getString(R.string.select_zone));
        }

        final ArrayAdapter pp = new ArrayAdapter(this, R.layout.spinner_list, Agro_climaticZoneArrayList);
        pp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_Agrozone.setAdapter(pp);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = zone_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = zone_name;
                spinnerPosition = pp.getPosition(strpos1);
                spn_Agrozone.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_Agrozone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spn_Agrozone.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_zone))) {
                    if (spn_Agrozone.getSelectedItem().toString().trim() != null) {
                        AgroZone = Agro_climaticZoneNameHM.get(spn_Agrozone.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setAnnualIncomeSpinner() {
        AnnualIncomeArrayList.clear();

        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            AnnualIncomeNameHM = sqliteHelper.getMasterSpinnerId(1, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            AnnualIncomeNameHM = sqliteHelper.getMasterSpinnerId(1, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            AnnualIncomeNameHM = sqliteHelper.getMasterSpinnerId(1, 4);
        }
        else
        {
            AnnualIncomeNameHM = sqliteHelper.getMasterSpinnerId(1, 1);
        }

        for (int i = 0; i < AnnualIncomeNameHM.size(); i++) {
            AnnualIncomeArrayList.add(AnnualIncomeNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            //AnnualIncomeArrayList.add(0, AnnualIncome);
            AnnualIncomeArrayList.add(0, getString(R.string.select_annual_income));

        } else {
            AnnualIncomeArrayList.add(0, getString(R.string.select_annual_income));
        }

        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, AnnualIncomeArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_annualIncome.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = income_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = income_name;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_annualIncome.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_annualIncome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spn_annualIncome.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_annual_income))) {
                    if (spn_annualIncome.getSelectedItem().toString().trim() != null) {
                        annualicme = AnnualIncomeNameHM.get(spn_annualIncome.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setCategorySpinner() {
        CategoryArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            CategoryNameHM = sqliteHelper.getMasterSpinnerId(10, 2);
        }
        else  if(language.equalsIgnoreCase("kan"))
        {
            CategoryNameHM = sqliteHelper.getMasterSpinnerId(10, 3);
        }
        else  if(language.equalsIgnoreCase("guj"))
        {
            CategoryNameHM = sqliteHelper.getMasterSpinnerId(10, 4);
        }
        else
        {
            CategoryNameHM = sqliteHelper.getMasterSpinnerId(10, 1);
        }

        for (int i = 0; i < CategoryNameHM.size(); i++) {
            CategoryArrayList.add(CategoryNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            //CategoryArrayList.add(0, Id_Card);
            CategoryArrayList.add(0, getString(R.string.select_category));

        } else {
            CategoryArrayList.add(0, getString(R.string.select_category));
        }

        final ArrayAdapter gg = new ArrayAdapter(this, R.layout.spinner_list, CategoryArrayList);
        gg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_category.setAdapter(gg);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = category_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = category_name;
                spinnerPosition = gg.getPosition(strpos1);
                spn_category.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spn_category.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_category))) {
                    if (spn_category.getSelectedItem().toString().trim() != null) {
                        hmCategory = CategoryNameHM.get(spn_category.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setEducationSpinner() {
        EducationArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            EducationNameHM = sqliteHelper.getMasterSpinnerId(9, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            EducationNameHM = sqliteHelper.getMasterSpinnerId(9, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            EducationNameHM = sqliteHelper.getMasterSpinnerId(9, 4);
        }
        else
        {
            EducationNameHM = sqliteHelper.getMasterSpinnerId(9, 1);
        }

        for (int i = 0; i < EducationNameHM.size(); i++) {
            EducationArrayList.add(EducationNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            //EducationArrayList.add(0, Id_Card);
            EducationArrayList.add(0, getString(R.string.select_education));

        } else {
            EducationArrayList.add(0, getString(R.string.select_education));
        }

        final ArrayAdapter vv = new ArrayAdapter(this, R.layout.spinner_list, EducationArrayList);
        vv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_education.setAdapter(vv);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = education_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = education_name;
                spinnerPosition = vv.getPosition(strpos1);
                spn_education.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_education.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spn_education.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_education))) {
                    if (spn_education.getSelectedItem().toString().trim() != null) {
                        hmEducation = EducationNameHM.get(spn_education.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setCastSpinner() {
        CasteArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            CasteNameHM = sqliteHelper.getMasterSpinnerId(8, 2);
        }
        else  if(language.equalsIgnoreCase("kan"))
        {
            CasteNameHM = sqliteHelper.getMasterSpinnerId(8, 3);
        }
        else  if(language.equalsIgnoreCase("guj"))
        {
            CasteNameHM = sqliteHelper.getMasterSpinnerId(8, 4);
        }
        else
        {
            CasteNameHM = sqliteHelper.getMasterSpinnerId(8, 1);
        }

        for (int i = 0; i < CasteNameHM.size(); i++) {
            CasteArrayList.add(CasteNameHM.keySet().toArray()[i].toString().trim());
        }
        Collections.sort(CasteArrayList);
        if (isEditable) {
            //CasteArrayList.add(0, Id_Card);
            CasteArrayList.add(0, getString(R.string.select_cast));
        } else {
            CasteArrayList.add(0, getString(R.string.select_cast));
        }

        final ArrayAdapter dd = new ArrayAdapter(this, R.layout.spinner_list, CasteArrayList);
        dd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_cast.setAdapter(dd);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = cast_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = cast_name;
                spinnerPosition = dd.getPosition(strpos1);
                spn_cast.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_cast.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spn_cast.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_cast))) {
                    if (spn_cast.getSelectedItem().toString().trim() != null) {
                        hmcaste = CasteNameHM.get(spn_cast.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setReligionSpinner() {
        ReligionArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            ReligionNameHM = sqliteHelper.getMasterSpinnerId(7, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            ReligionNameHM = sqliteHelper.getMasterSpinnerId(7, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            ReligionNameHM = sqliteHelper.getMasterSpinnerId(7, 4);
        }
        else
        {
            ReligionNameHM = sqliteHelper.getMasterSpinnerId(7, 1);
        }

        for (int i = 0; i < ReligionNameHM.size(); i++) {
            ReligionArrayList.add(ReligionNameHM.keySet().toArray()[i].toString().trim());
        }
        Collections.sort(ReligionArrayList);
        if (isEditable) {
            //ReligionArrayList.add(0, Id_Card);
            ReligionArrayList.add(0, getString(R.string.select_religion));
        } else {
            ReligionArrayList.add(0, getString(R.string.select_religion));
        }

        final ArrayAdapter bb = new ArrayAdapter(this, R.layout.spinner_list, ReligionArrayList);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_religion.setAdapter(bb);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = religion_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = religion_name;
                spinnerPosition = bb.getPosition(strpos1);
                spn_religion.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        spn_religion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spn_religion.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_religion))) {
                    if (spn_religion.getSelectedItem().toString().trim() != null) {
                        hmReligion = ReligionNameHM.get(spn_religion.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rg_age.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_age:
                        ll_age.setVisibility(View.VISIBLE);
                        ll_dob.setVisibility(View.GONE);
                        getDateOfBirthFromAge();
                        what_you_know = "Age";
                        break;
                    case R.id.rb_dob:
                        ll_age.setVisibility(View.GONE);
                        ll_dob.setVisibility(View.VISIBLE);
                        what_you_know = "Date of Birth";
                        break;
                }
            }
        });
    }

    private void getDateOfBirthFromAge() {
        et_FarmerAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                float aa = 0;
                int year = 0;
                int month = 0;
                String ageee = et_FarmerAge.getText().toString().trim();
                if (ageee.length() > 0) {
                    if (ageee.contains(".")) {
                        String[] dobDate = ageee.split("\\.");
                        year = Integer.parseInt(dobDate[0]);
                        if (dobDate.length > 1) {
                            month = Integer.parseInt(dobDate[1]);
                        }
                    } else {
                        year = Integer.parseInt(ageee);
                    }
                    if (et_FarmerAge.getText().toString().trim().equalsIgnoreCase("")) {
                        year = 0;
                    } else {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - year);
                        calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) + 1 - month);
                        calendar.set(Calendar.DAY_OF_MONTH, 0);
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd", Locale.getDefault());
                        String formatted = dateFormat.format(calendar.getTime());
                        et_Farmerdob.setText(formatted);
                    }
                } else {
                    et_Farmerdob.setText("");
                }
            }
        });
    }

    private void initViews() {
        et_Area = findViewById(R.id.et_Area);
        et_quantity = findViewById(R.id.et_Quanty);
        spn_season = findViewById(R.id.spn_season);
        spn_Unit = findViewById(R.id.spn_unit);
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        farmerRegistrationPojo = new FarmerRegistrationPojo();
        cropVegitableDetails =new CropVegitableDetails();
        usersPojo = new UsersPojo();
        AnnualIncomeArrayList = new ArrayList<>();
        AnnualIncomeNameHM = new HashMap<>();
        IdCardArrayList = new ArrayList<>();
        IdCardNameHM = new HashMap<>();
        ReligionArrayList = new ArrayList<>();
        ReligionNameHM = new HashMap<>();
        CasteArrayList = new ArrayList<>();
        CasteNameHM = new HashMap<>();
        EducationArrayList = new ArrayList<>();
        EducationNameHM = new HashMap<>();
        CategoryArrayList = new ArrayList<>();
        CategoryNameHM = new HashMap<>();
        Agro_climaticZoneArrayList = new ArrayList<>();
        Agro_climaticZoneNameHM = new HashMap<>();
        alternetLivehoodArrayList = new ArrayList<>();
        alternetLivehoodNameHM = new HashMap<>();
        relationHM = new HashMap<>();
        FamilyEducationArrayList = new ArrayList<>();
        relationArrayList = new ArrayList<>();
        FamilyEducationNameHM = new HashMap<>();
        FamiliymonthlyArrayList = new ArrayList<>();
        FamiliymonthlyNameHM = new HashMap<>();
        unitHM = new HashMap<>();
        cropCategoryHM = new HashMap<>();
        subCategoryHM = new HashMap<>();
        unitHM = sqliteHelper.getMasterSpinnerId(4, 1);
        cropCategoryHM = sqliteHelper.getAllCategoryType(1);
        stateNameHM = new HashMap<>();
        districtNameHM = new HashMap<>();
        blockNameHM = new HashMap<>();
        villageNameHM = new HashMap<>();
        cropDetailHM = new HashMap<>();

        stateArrayList = new ArrayList<>();
        subCategoryArrayList = new ArrayList<>();
        distrcitArrayList = new ArrayList<>();
        blockArrayList = new ArrayList<>();
        villageArrayList = new ArrayList<>();
        cropCategoryList = new ArrayList<>();
        monthlyList.add(0,"Kharif");
        monthlyList.add(1,"Rabi");
        monthlyList.add(2,"Zaid");
        monthlyList.add(3,"Annual");
        final ArrayAdapter Adapter1 = new ArrayAdapter(this, R.layout.spinner_list, monthlyList);
        Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_season.setAdapter(Adapter1);
        familyHM = new HashMap<>();
        imageView_profile = findViewById(R.id.imageView_profile);
        img_selection_pencil = findViewById(R.id.img_selection_pencil);
        ll_age = findViewById(R.id.ll_age);
        ll_dob = findViewById(R.id.ll_dob);
        ll_family_details = findViewById(R.id.ll_family_details);
        ll_crop_details = findViewById(R.id.ll_crop_details);
        rg_age = findViewById(R.id.rg_age);
        rb_age = findViewById(R.id.rb_age);
        ch_term_condition= findViewById(R.id.ch_term_condition);
        rb_dob = findViewById(R.id.rb_dob);
        et_householdNo = findViewById(R.id.et_householdNo);
        btn_addCrop = findViewById(R.id.btn_addCrop);
        et_FarmerName = findViewById(R.id.et_FarmerName);
        et_FarmerAge = findViewById(R.id.et_FarmerAge);
        et_Aadhar = findViewById(R.id.et_Aadhar);
        et_husbandFatherName = findViewById(R.id.et_husbandFatherName);
        et_mobileNumber = findViewById(R.id.et_mobileNumber);
        et_Educationdetails = findViewById(R.id.et_Educationdetails);
        et_TotalLandHoldingArea = findViewById(R.id.et_TotalLandHoldingArea);
        et_NoOfMembers = findViewById(R.id.et_NoOfMembers);
        et_CropDetails = findViewById(R.id.et_CropDetails);
        et_name = findViewById(R.id.et_name);
        et_pincode = findViewById(R.id.et_pincode);
        et_Age = findViewById(R.id.et_Age);
        tv_householdNo = findViewById(R.id.tv_householdNo);
        tv_disclaimer= findViewById(R.id.tv_disclaimer);
        ll_household = findViewById(R.id.ll_household);
        spnFamiliy_monthly = findViewById(R.id.spnFamiliy_monthly);
        et_address = findViewById(R.id.et_address);
        ll_aadhar = findViewById(R.id.ll_aadhar);
        et_SCan = findViewById(R.id.et_SCan);
        ll_other = findViewById(R.id.ll_other);

        //all Radio Group ides
        rg_IdCard = findViewById(R.id.rg_IdCard);
        rg_BPL = findViewById(R.id.rg_BPL);
        rg_PhysicalChallenges = findViewById(R.id.rg_PhysicalChallenges);
        rg_Handicapped = findViewById(R.id.rg_Handicapped);
        rg_Multicropping = findViewById(R.id.rg_Multicropping);
        rg_Fertilizer = findViewById(R.id.rg_Fertilizer);
        rg_IrrigationFacility = findViewById(R.id.rg_IrrigationFacility);

        //all Radio Buttons ides
        rb_AadharCard = findViewById(R.id.rb_AadharCard);
        rb_otherNationalIdCard = findViewById(R.id.rb_otherNationalIdCard);
        rb_BPLNo = findViewById(R.id.rb_BPLNo);
        rb_BPLYes = findViewById(R.id.rb_BPLYes);
        rb_PhysicalChallengesYes = findViewById(R.id.rb_PhysicalChallengesYes);
        rb_PhysicalChallengesNo = findViewById(R.id.rb_PhysicalChallengesNo);
        rg_HandicappedYes = findViewById(R.id.rg_HandicappedYes);
        rg_HandicappedNo = findViewById(R.id.rg_HandicappedNo);
        rb_MulticroppingYes = findViewById(R.id.rb_MulticroppingYes);
        rb_MulticroppingNo = findViewById(R.id.rb_MulticroppingNo);
        rb_FertilizerYes = findViewById(R.id.rb_FertilizerYes);
        rb_FertilizerNo = findViewById(R.id.rb_FertilizerNo);
        rb_IrrigationFacilityYes = findViewById(R.id.rb_IrrigationFacilityYes);
        rb_IrrigationFacilityNo = findViewById(R.id.rb_IrrigationFacilityNo);

        btn_addFamily = findViewById(R.id.btn_addFamily);
        alldataSubmit = findViewById(R.id.alldataSubmit);

        //all Spinners ides
        spn_religion = findViewById(R.id.spn_religion);
        et_Farmerdob = findViewById(R.id.et_Farmerdob);
        spn_IdCard = findViewById(R.id.spn_IdCard);
        spn_cast = findViewById(R.id.spn_cast);
        spn_village = findViewById(R.id.spn_village);
        spn_crop = findViewById(R.id.spn_crop);
        spn_Units = findViewById(R.id.spn_Units);
        spn_relation = findViewById(R.id.spn_relation);
        spn_sub_crop = findViewById(R.id.spn_sub_crop);
        spn_annualIncome = findViewById(R.id.spn_annualIncome);
        spn_Agrozone = findViewById(R.id.spn_Agrozone);
        spn_education = findViewById(R.id.spn_education);
        spnF_Education = findViewById(R.id.spnF_Education);
        spn_category = findViewById(R.id.spn_Category);
        spn_State = findViewById(R.id.spn_State);
        spn_district = findViewById(R.id.spn_District);
        spn_block = findViewById(R.id.spn_block);
        spn_alternetLivehood = findViewById(R.id.spn_alternetLivehood);

        spin_Occupation = findViewById(R.id.spin_Occupation);
        spn_gender = findViewById(R.id.spn_gender);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fromLogin = bundle.getString("fromLogin", "");
            if (fromLogin.equals("1")) {
                et_householdNo.setVisibility(View.GONE);
                ll_household.setVisibility(View.GONE);
            }else if (fromLogin.equals("2")){
                et_householdNo.setVisibility(View.GONE);
                ll_household.setVisibility(View.GONE);
            }
        }



        /////////////////////////////////////////////////////////Camera



        findViewById(R.id.imageView_profile).setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (uiHelper.checkSelfPermissions(this))
                    openCamera();
            //uiHelper.showImagePickerDialog(this, this);
        });

        btn_addFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyOperationLayoutAddMemeber.add(FarmerRegistration.this, btn_addFamily);
            }
        });
        btn_addCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyOperationLayoutAddCropDetails.add(FarmerRegistration.this, btn_addCrop);

            }
        });

    }

    private void setValuesForEdit() {
        if (sharedPrefHelper.getString("role_id", "").equals("5")) {
            if (!sharedPrefHelper.getString("user_id", "").equals("")) {
                isEditable = true;
                farmerRegistrationPojo = sqliteHelper.getFarmerDetailsForEdit(sharedPrefHelper.getString("user_id", ""));
                try {
                    base64 = farmerRegistrationPojo.getProfile_photo();
                    // Picasso.with(this).load(APIClient.IMAGE_PROFILE_URL + base64).placeholder(R.drawable.noimage).into(imageView_profile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (farmerRegistrationPojo.getProfile_photo() != null && farmerRegistrationPojo.getProfile_photo().length()>200) {
                    byte[] decodedString = Base64.decode(farmerRegistrationPojo.getProfile_photo(), Base64.NO_WRAP);
                    InputStream inputStream = new ByteArrayInputStream(decodedString);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView_profile.setImageBitmap(bitmap);
                } else if (farmerRegistrationPojo.getProfile_photo().length() <=200) {
                    try {
                        String base64 = APIClient.IMAGE_PROFILE_URL + farmerRegistrationPojo.getProfile_photo();
                        Picasso.with(this).load(base64).placeholder(R.drawable.farmer_female).into(imageView_profile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    imageView_profile.setImageResource(R.drawable.farmer_female);

                }

                et_householdNo.setText(farmerRegistrationPojo.getHousehold_no());
                et_householdNo.setEnabled(false);
                et_FarmerName.setText(farmerRegistrationPojo.getFarmer_name());
                IdCard = farmerRegistrationPojo.getId_other_name();
                if (IdCard.equals("Aadhar Card")) {
                    rb_AadharCard.setChecked(true);
                    et_SCan.setVisibility(View.VISIBLE);
                    ll_other.setVisibility(View.GONE);
                    et_Aadhar.setInputType(InputType.TYPE_CLASS_NUMBER);
                } if (IdCard.equals("आधार कार्ड")) {
                    rb_AadharCard.setChecked(true);
                    et_SCan.setVisibility(View.VISIBLE);
                    ll_other.setVisibility(View.GONE);
                    et_Aadhar.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else if (IdCard.equals(getString(R.string.Other))) {
                    rb_otherNationalIdCard.setChecked(true);
                    et_SCan.setVisibility(View.GONE);
                    ll_other.setVisibility(View.VISIBLE);
                }
                ID_Card = Integer.parseInt(farmerRegistrationPojo.getId_type_id());
                other_ids_name = sqliteHelper.getNameById("master", "master_name", "caption_id", ID_Card);
                setOtherIdSpinner();
                et_Aadhar.setText(farmerRegistrationPojo.getId_no());
                et_husbandFatherName.setText(farmerRegistrationPojo.getFather_husband_name());
                et_mobileNumber.setText(farmerRegistrationPojo.getMobile());
                what_you_know = farmerRegistrationPojo.getWhat_you_know();
                if (what_you_know != null) {
                    if (what_you_know.equals("Age")) {
                        rb_age.setChecked(true);
                    } else if (what_you_know.equals("Date of Birth")) {
                        rb_dob.setChecked(true);
                    }else if (what_you_know.equals("आयु")) {
                        rb_age.setChecked(true);
                    } else if (what_you_know.equals("जन्म की तारीख")) {
                        rb_dob.setChecked(true);
                    }
                }
                et_FarmerAge.setText(farmerRegistrationPojo.getAge());
                String incomingDate = farmerRegistrationPojo.getDate_of_birth();
                if (incomingDate != null) {
                    if (incomingDate.length() > 10) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date newDate = sdf.parse(incomingDate);
                            sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String outputDate = sdf.format(newDate);
                            et_Farmerdob.setText(outputDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        et_Farmerdob.setText(incomingDate);
                    }
                }
                et_address.setText(farmerRegistrationPojo.getAddress());
                String language = sharedPrefHelper.getString("languageID","");

                state_id = Integer.parseInt(farmerRegistrationPojo.getState_id());
                state_name = sqliteHelper.getNameByIdlocation("state_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getState_id()),language);
                getAllStateFromTable();
                district_id = Integer.parseInt(farmerRegistrationPojo.getDistrict_id());
                district_name = sqliteHelper.getNameByIdlocation("district_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getDistrict_id()),language);
                block_id = Integer.parseInt(farmerRegistrationPojo.getBlock_id());
                block_name = sqliteHelper.getNameByIdlocation("block_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getBlock_id()),language);
                village_id = Integer.parseInt(farmerRegistrationPojo.getVillage_id());
                village_name = sqliteHelper.getNameByIdlocation("village_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getVillage_id()),language);

                et_pincode.setText(farmerRegistrationPojo.getPincode());
                bpl = farmerRegistrationPojo.getBpl();
                if (bpl.equals("Yes")) {
                    rb_BPLYes.setChecked(true);
                } else if (bpl.equals("No")) {
                    rb_BPLNo.setChecked(true);
                }else  if (bpl.equals("हाँ")) {
                    rb_BPLYes.setChecked(true);
                } else if (bpl.equals("नहीं")) {
                    rb_BPLNo.setChecked(true);
                }
                hmReligion = Integer.parseInt(farmerRegistrationPojo.getReligion_id());
                religion_name = sqliteHelper.getNameById("master", "master_name", "caption_id", hmReligion);
                setReligionSpinner();
                hmcaste = Integer.parseInt(farmerRegistrationPojo.getCaste());
                cast_name = sqliteHelper.getNameById("master", "master_name", "caption_id", hmcaste);
                setCastSpinner();
                hmEducation = Integer.parseInt(farmerRegistrationPojo.getEducation_id());
                education_name = sqliteHelper.getNameById("master", "master_name", "caption_id", hmEducation);
                setEducationSpinner();
                //et_Educationdetails.setText(farmerRegistrationPojo.getEducation_qualification());
                PhysicalChallenges = farmerRegistrationPojo.getPhysical_challenges();
                if (PhysicalChallenges.equals("Yes")) {
                    rb_PhysicalChallengesYes.setChecked(true);
                } else if (PhysicalChallenges.equals("No")) {
                    rb_PhysicalChallengesNo.setChecked(true);
                }else  if (PhysicalChallenges.equals("हाँ")) {
                    rb_PhysicalChallengesYes.setChecked(true);
                } else if (PhysicalChallenges.equals("नहीं")) {
                    rb_PhysicalChallengesNo.setChecked(true);
                }


           /*     Handicapped = farmerRegistrationPojo.getHandicapped();
                if (Handicapped.equals(getString(R.string.yes))) {
                    rg_HandicappedYes.setChecked(true);
                } else if (Handicapped.equals(getString(R.string.no))) {
                    rg_HandicappedNo.setChecked(true);
                }   */

                hmCategory = Integer.parseInt(farmerRegistrationPojo.getCategory_id());
                category_name = sqliteHelper.getNameById("master", "master_name", "caption_id", hmCategory);
                setCategorySpinner();
                et_TotalLandHoldingArea.setText(farmerRegistrationPojo.getTotal_land_holding());
                annualicme = farmerRegistrationPojo.getAnnual_income();
                income_name = sqliteHelper.getNameById("master", "master_name", "caption_id", annualicme);
                setAnnualIncomeSpinner();
                AgroZone = Integer.parseInt(farmerRegistrationPojo.getAgro_climat_zone_id());
                zone_name = sqliteHelper.getNameById("master", "master_name", "caption_id", AgroZone);
                setAgroClimaticZoneSpinner();
                stralternetLivehood = Integer.parseInt(farmerRegistrationPojo.getAlternative_livelihood_id());
                livehood_name = sqliteHelper.getNameById("master", "master_name", "caption_id", stralternetLivehood);
                setAlternativeLivehoodSpinner();
                et_NoOfMembers.setText(farmerRegistrationPojo.getNof_member_migrated());
                //et_CropDetails.setText(farmerRegistrationPojo.getCrop_vegetable_details());
                Multicropping = farmerRegistrationPojo.getMulti_cropping();
                if (Multicropping.equals("Yes")) {
                    rb_MulticroppingYes.setChecked(true);
                } else if (Multicropping.equals("No")) {
                    rb_MulticroppingNo.setChecked(true);
                }else if (Multicropping.equals("हाँ")) {
                    rb_MulticroppingYes.setChecked(true);
                } else if (Multicropping.equals("नहीं")) {
                    rb_MulticroppingNo.setChecked(true);
                }
                Fertilizer = farmerRegistrationPojo.getFertilizer();
                if (Fertilizer.equals("Yes")) {
                    rb_FertilizerYes.setChecked(true);
                } else if (Fertilizer.equals("No")) {
                    rb_FertilizerNo.setChecked(true);
                }else if (Fertilizer.equals("हाँ")) {
                    rb_FertilizerYes.setChecked(true);
                } else if (Fertilizer.equals("नहीं")) {
                    rb_FertilizerNo.setChecked(true);
                }
                IrrigationFacility = farmerRegistrationPojo.getIrrigation_facility();
                if (IrrigationFacility.equals("Yes")) {
                    rb_IrrigationFacilityYes.setChecked(true);
                } else if (IrrigationFacility.equals("No")) {
                    rb_IrrigationFacilityNo.setChecked(true);
                }else  if (IrrigationFacility.equals("हाँ")) {
                    rb_IrrigationFacilityYes.setChecked(true);
                } else if (IrrigationFacility.equals("नहीं")) {
                    rb_IrrigationFacilityNo.setChecked(true);
                }

                farmerfamilyPojoArrayList.clear();
                farmerfamilyPojoArrayList = sqliteHelper.getFarmerFamily(String.valueOf(farmerRegistrationPojo.getId()));
                MyOperationLayoutAddMemeber.addforEdit(FarmerRegistration.this, farmerfamilyPojoArrayList);
                cropVegitableDetailsArrayList.clear();
                cropVegitableDetailsArrayList = sqliteHelper.getCropDetails(String.valueOf(farmerRegistrationPojo.getId()));
                MyOperationLayoutAddCropDetails.addforEdit(FarmerRegistration.this, cropVegitableDetailsArrayList);

                /*for farmer-family set values*/
                //TO DO with Rachit Tanwer
            }
        } else {
            if (!user_id.equals("")) {
                isEditable = true;
                farmerRegistrationPojo = sqliteHelper.getFarmerDetailsForEdit(user_id);

                try {
                    base64 = farmerRegistrationPojo.getProfile_photo();
                   // Picasso.with(this).load(APIClient.IMAGE_PROFILE_URL + base64).placeholder(R.drawable.noimage).into(imageView_profile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (farmerRegistrationPojo.getProfile_photo() != null && farmerRegistrationPojo.getProfile_photo().length()>200) {
                    byte[] decodedString = Base64.decode(farmerRegistrationPojo.getProfile_photo(), Base64.NO_WRAP);
                    InputStream inputStream = new ByteArrayInputStream(decodedString);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView_profile.setImageBitmap(bitmap);
                } else if (farmerRegistrationPojo.getProfile_photo().length() <=200) {
                    try {
                        String base64 = APIClient.IMAGE_PROFILE_URL + farmerRegistrationPojo.getProfile_photo();
                        Picasso.with(this).load(base64).placeholder(R.drawable.farmer_female).into(imageView_profile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    imageView_profile.setImageResource(R.drawable.farmer_female);

                }


                et_householdNo.setText(farmerRegistrationPojo.getHousehold_no());
                et_FarmerName.setText(farmerRegistrationPojo.getFarmer_name());
                IdCard = farmerRegistrationPojo.getId_other_name();
                if (IdCard.equals("Aadhar Card")) {
                    rb_AadharCard.setChecked(true);
                    et_SCan.setVisibility(View.VISIBLE);
                    ll_other.setVisibility(View.GONE);
                }else if (IdCard.equals("आधार कार्ड")) {
                    rb_AadharCard.setChecked(true);
                    et_SCan.setVisibility(View.VISIBLE);
                    ll_other.setVisibility(View.GONE);
                } else if (IdCard.equals(getString(R.string.Other))) {
                    rb_otherNationalIdCard.setChecked(true);
                    et_SCan.setVisibility(View.GONE);
                    ll_other.setVisibility(View.VISIBLE);
                } else if (IdCard.equals("Other")) {
                    rb_otherNationalIdCard.setChecked(true);
                    et_SCan.setVisibility(View.GONE);
                    ll_other.setVisibility(View.VISIBLE);
                }
                ID_Card = Integer.parseInt(farmerRegistrationPojo.getId_type_id());
                other_ids_name = sqliteHelper.getNameById("master", "master_name", "caption_id", ID_Card);
                setOtherIdSpinner();
                et_Aadhar.setText(farmerRegistrationPojo.getId_no());
                et_husbandFatherName.setText(farmerRegistrationPojo.getFather_husband_name());
                et_mobileNumber.setText(farmerRegistrationPojo.getMobile());
                what_you_know = farmerRegistrationPojo.getWhat_you_know();
                if (what_you_know != null) {
                    if (what_you_know.equals("Age")) {
                        rb_age.setChecked(true);
                    } else if (what_you_know.equals("Date of Birth")) {
                        rb_dob.setChecked(true);
                    } else if (what_you_know.equals("आयु")) {
                        rb_age.setChecked(true);
                    } else if (what_you_know.equals("जन्म की तारीख")) {
                        rb_dob.setChecked(true);
                    }
                }
                et_FarmerAge.setText(farmerRegistrationPojo.getAge());
                String incomingDate = farmerRegistrationPojo.getDate_of_birth();
                if (incomingDate != null) {
                    if (incomingDate.length() > 10) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date newDate = sdf.parse(incomingDate);
                            sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String outputDate = sdf.format(newDate);
                            et_Farmerdob.setText(outputDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        et_Farmerdob.setText(incomingDate);
                    }
                }
                et_address.setText(farmerRegistrationPojo.getAddress());
                String language = sharedPrefHelper.getString("languageID","");

                state_id = Integer.parseInt(farmerRegistrationPojo.getState_id());
                state_name = sqliteHelper.getNameByIdlocation("state_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getState_id()),language);
                getAllStateFromTable();
                district_id = Integer.parseInt(farmerRegistrationPojo.getDistrict_id());
                district_name = sqliteHelper.getNameByIdlocation("district_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getDistrict_id()),language);
                block_id = Integer.parseInt(farmerRegistrationPojo.getBlock_id());
                block_name = sqliteHelper.getNameByIdlocation("block_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getBlock_id()),language);
                village_id = Integer.parseInt(farmerRegistrationPojo.getVillage_id());
                village_name = sqliteHelper.getNameByIdlocation("village_language", "name", "id", Integer.parseInt(farmerRegistrationPojo.getVillage_id()),language);


                et_pincode.setText(farmerRegistrationPojo.getPincode());
                bpl = farmerRegistrationPojo.getBpl();
                if (bpl.equals("Yes")) {
                    rb_BPLYes.setChecked(true);
                } else if (bpl.equals("No")) {
                    rb_BPLNo.setChecked(true);
                }else if (bpl.equals("हाँ")) {
                    rb_BPLYes.setChecked(true);
                } else if (bpl.equals("नहीं")) {
                    rb_BPLNo.setChecked(true);
                }
                hmReligion = Integer.parseInt(farmerRegistrationPojo.getReligion_id());
                religion_name = sqliteHelper.getNameById("master", "master_name", "caption_id", hmReligion);
                setReligionSpinner();
                hmcaste = Integer.parseInt(farmerRegistrationPojo.getCaste());
                cast_name = sqliteHelper.getNameById("master", "master_name", "caption_id", hmcaste);
                setCastSpinner();
                hmEducation = Integer.parseInt(farmerRegistrationPojo.getEducation_id());
                education_name = sqliteHelper.getNameById("master", "master_name", "caption_id", hmEducation);
                setEducationSpinner();
                //et_Educationdetails.setText(farmerRegistrationPojo.getEducation_qualification());
                PhysicalChallenges = farmerRegistrationPojo.getPhysical_challenges();
                if (PhysicalChallenges.equals("Yes")) {
                    rb_PhysicalChallengesYes.setChecked(true);
                } else if (PhysicalChallenges.equals("No")) {
                    rb_PhysicalChallengesNo.setChecked(true);
                }else if (PhysicalChallenges.equals("हाँ")) {
                    rb_PhysicalChallengesYes.setChecked(true);
                } else if (PhysicalChallenges.equals("नहीं")) {
                    rb_PhysicalChallengesNo.setChecked(true);
                }


          /*      Handicapped = farmerRegistrationPojo.getHandicapped();
                if (Handicapped != null){
                if (Handicapped.equals(getString(R.string.yes))) {
                    rg_HandicappedYes.setChecked(true);
                } else if (Handicapped.equals(getString(R.string.no))) {
                    rg_HandicappedNo.setChecked(true);
                }
                }  */

                hmCategory = Integer.parseInt(farmerRegistrationPojo.getCategory_id());
                category_name = sqliteHelper.getNameById("master", "master_name", "caption_id", hmCategory);
                setCategorySpinner();
                et_TotalLandHoldingArea.setText(farmerRegistrationPojo.getTotal_land_holding());
                annualicme = farmerRegistrationPojo.getAnnual_income();
                income_name = sqliteHelper.getNameById("master", "master_name", "caption_id", annualicme);
                setAnnualIncomeSpinner();
                AgroZone = Integer.parseInt(farmerRegistrationPojo.getAgro_climat_zone_id());
                zone_name = sqliteHelper.getNameById("master", "master_name", "caption_id", AgroZone);
                setAgroClimaticZoneSpinner();
                stralternetLivehood = Integer.parseInt(farmerRegistrationPojo.getAlternative_livelihood_id());
                livehood_name = sqliteHelper.getNameById("master", "master_name", "caption_id", stralternetLivehood);
                setAlternativeLivehoodSpinner();
                et_NoOfMembers.setText(farmerRegistrationPojo.getNof_member_migrated());
              //  et_CropDetails.setText(farmerRegistrationPojo.getCrop_vegetable_details());
                Multicropping = farmerRegistrationPojo.getMulti_cropping();
                if (Multicropping.equals("Yes")) {
                    rb_MulticroppingYes.setChecked(true);
                } else if (Multicropping.equals("No")) {
                    rb_MulticroppingNo.setChecked(true);
                }else if (Multicropping.equals("हाँ")) {
                    rb_MulticroppingYes.setChecked(true);
                } else if (Multicropping.equals("नहीं")) {
                    rb_MulticroppingNo.setChecked(true);
                }
                Fertilizer = farmerRegistrationPojo.getFertilizer();
                if (Fertilizer.equals("Yes")) {
                    rb_FertilizerYes.setChecked(true);
                } else if (Fertilizer.equals("No")) {
                    rb_FertilizerNo.setChecked(true);
                }else  if (Fertilizer.equals("हाँ")) {
                    rb_FertilizerYes.setChecked(true);
                } else if (Fertilizer.equals("नहीं")) {
                    rb_FertilizerNo.setChecked(true);
                }
                IrrigationFacility = farmerRegistrationPojo.getIrrigation_facility();
                if (IrrigationFacility.equals("Yes")) {
                    rb_IrrigationFacilityYes.setChecked(true);
                } else if (IrrigationFacility.equals("No")) {
                    rb_IrrigationFacilityNo.setChecked(true);
                }else  if (IrrigationFacility.equals("हाँ")) {
                    rb_IrrigationFacilityYes.setChecked(true);
                } else if (IrrigationFacility.equals("नहीं")) {
                    rb_IrrigationFacilityNo.setChecked(true);
                }

                farmerfamilyPojoArrayList.clear();
                farmerfamilyPojoArrayList = sqliteHelper.getFarmerFamily(String.valueOf(farmerRegistrationPojo.getId()));
                MyOperationLayoutAddMemeber.addforEdit(FarmerRegistration.this, farmerfamilyPojoArrayList);
                cropVegitableDetailsArrayList.clear();
                cropVegitableDetailsArrayList = sqliteHelper.getCropDetails(String.valueOf(farmerRegistrationPojo.getId()));
                MyOperationLayoutAddCropDetails.addforEdit(FarmerRegistration.this, cropVegitableDetailsArrayList);

                /*for farmer-family set values*/
                //TO DO with Rachit Tanwer
            }
        }
    }

    private void submitAllData() {
        alldataSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (screen_type.equals("edit_profile")) {
                    if (checkValidation()) {
                        Random random = new Random();
                        int value = random.nextInt(1000);
                        farmerRegistrationPojo.setHousehold_no(String.valueOf(value));
                        farmerRegistrationPojo.setFarmer_name(et_FarmerName.getText().toString().trim());
                        farmerRegistrationPojo.setId_no(et_Aadhar.getText().toString().trim());
                        farmerRegistrationPojo.setFather_husband_name(et_husbandFatherName.getText().toString().trim());
                        farmerRegistrationPojo.setMobile((et_mobileNumber.getText().toString().trim()));
                        /*here age condition for month*/
                        if (et_FarmerAge.getText().toString().trim().equalsIgnoreCase("0")) {
                            if (age_in_month.length() == 1) {
                                age_in_month = "0.0" + age_in_month;
                            }
                            if (age_in_month.length() == 2) {
                                age_in_month = "0." + age_in_month;
                            }
                            farmerRegistrationPojo.setAge(age_in_month);
                        } else {
                            farmerRegistrationPojo.setAge(et_FarmerAge.getText().toString().trim());
                        }
                        farmerRegistrationPojo.setDate_of_birth(et_Farmerdob.getText().toString().trim());
                        farmerRegistrationPojo.setWhat_you_know(what_you_know);

                        //farmerRegistrationPojo.setEducation_qualification(et_Educationdetails.getText().toString());
                        farmerRegistrationPojo.setTotal_land_holding((et_TotalLandHoldingArea.getText().toString().trim()));
                        farmerRegistrationPojo.setNof_member_migrated(et_NoOfMembers.getText().toString().trim());
                       // farmerRegistrationPojo.setCrop_vegetable_details(et_CropDetails.getText().toString().trim());
                        farmerRegistrationPojo.setAddress(et_address.getText().toString().trim());
                        farmerRegistrationPojo.setPincode(et_pincode.getText().toString().trim());
                        farmerRegistrationPojo.setId_type_id(String.valueOf(ID_Card));
                        farmerRegistrationPojo.setId_other_name(IdCard);
                        farmerRegistrationPojo.setBpl(bpl);
                        farmerRegistrationPojo.setPhysical_challenges(PhysicalChallenges);
                      //  farmerRegistrationPojo.setHandicapped(Handicapped);
                        farmerRegistrationPojo.setMulti_cropping(Multicropping);
                        farmerRegistrationPojo.setFertilizer(Fertilizer);
                        farmerRegistrationPojo.setIrrigation_facility(IrrigationFacility);
                        farmerRegistrationPojo.setReligion_id(String.valueOf(hmReligion));
                        farmerRegistrationPojo.setCaste(String.valueOf(hmcaste));
                        farmerRegistrationPojo.setAgro_climat_zone_id(String.valueOf(AgroZone));
                        farmerRegistrationPojo.setAlternative_livelihood_id(String.valueOf(stralternetLivehood));
                        farmerRegistrationPojo.setEducation_id(String.valueOf(hmEducation));
                        farmerRegistrationPojo.setAnnual_income(annualicme);
                        farmerRegistrationPojo.setCategory_id(String.valueOf(hmCategory));
                        farmerRegistrationPojo.setState_id(String.valueOf(state_id));
                        farmerRegistrationPojo.setDistrict_id(String.valueOf(district_id));
                        farmerRegistrationPojo.setBlock_id(String.valueOf(block_id));
                        farmerRegistrationPojo.setVillage_id(String.valueOf(village_id));
                        farmerRegistrationPojo.setGender("Female");
                        usersPojo.setFirst_name(et_FarmerName.getText().toString().trim());
                        usersPojo.setLast_name(et_FarmerName.getText().toString().trim());
                        farmerRegistrationPojo.setGender(getString(R.string.Female));
                        usersPojo.setFirst_name(et_FarmerName.getText().toString().trim());
                        usersPojo.setLast_name(et_FarmerName.getText().toString().trim());
                        usersPojo.setMobile(farmerRegistrationPojo.getMobile());
                        usersPojo.setEmail(et_FarmerName.getText().toString().trim());
                        usersPojo.setProfile_photo(base64);

                        if (sharedPrefHelper.getString("user_type", "").equals("Farmer")) {
                            sqliteHelper.updateUsersData(usersPojo, sharedPrefHelper.getString("user_id", ""));
                            sqliteHelper.updateFarmerRegistrationData(farmerRegistrationPojo, farmer_id, sharedPrefHelper.getString("user_id", ""));
                        } else {
                            sqliteHelper.updateUsersData(usersPojo, user_id);
                            sqliteHelper.updateFarmerRegistrationData(farmerRegistrationPojo, farmer_id, user_id);
                        }

                        cropDetailHM.clear();
                        sqliteHelper.dropTableFamily("crop_vegetable_details", user_id);

                        cropDetailHM = MyOperationLayoutAddCropDetails.display(FarmerRegistration.this);
                        ArrayList<String> cropnameAl = new ArrayList<>();
                        ArrayList<String> areaAl = new ArrayList<>();
                        ArrayList<String> quantityAl = new ArrayList<>();
                        ArrayList<String> seasonAl = new ArrayList<>();
                        ArrayList<String> unitAl = new ArrayList<>();
                        ArrayList<String> unitsAl = new ArrayList<>();
                        ArrayList<String> subAl = new ArrayList<>();
                        cropnameAl=cropDetailHM.get("name");
                        areaAl=cropDetailHM.get("area");
                        quantityAl=cropDetailHM.get("quantity");
                        seasonAl=cropDetailHM.get("season");
                        unitAl=cropDetailHM.get("unit");
                        unitsAl=cropDetailHM.get("units");
                        subAl=cropDetailHM.get("subname");

                        if (cropnameAl.size()>0 && areaAl.size()>0 && quantityAl.size()>0 &&  seasonAl.size() >0 && unitAl.size()>0 && unitsAl.size()>0 ){
                            for (int i = 0; i < cropnameAl.size() ; i++) {
                                CropVegitableDetails vegitableDetails=new CropVegitableDetails();
                                vegitableDetails.setCrop_name(String.valueOf(cropCategoryHM.get(cropnameAl.get(i))));
                                getsubcategoey(Integer.parseInt(vegitableDetails.getCrop_name()));
                                vegitableDetails.setCrop_type_subcatagory_id(String.valueOf(subCategoryHM.get(subAl.get(i))));
                                vegitableDetails.setArea(areaAl.get(i));
                                vegitableDetails.setQuantity(quantityAl.get(i));
                                vegitableDetails.setSeason_id(seasonAl.get(i));
                                vegitableDetails.setUnit_id(String.valueOf(unitHM.get(unitAl.get(i).trim())));
                                vegitableDetails.setUnits_id(String.valueOf(unitshm.get(unitsAl.get(i).trim())));
                                sqliteHelper.setCropDetailsData(vegitableDetails,user_id);
                            }
                        }

                        // for family table
                        familyHM.clear();
                        sqliteHelper.dropTableFamily("farmer_family", user_id);
                        familyHM = MyOperationLayoutAddMemeber.display(FarmerRegistration.this);
                        ArrayList<String> nameAl = new ArrayList<>();
                        ArrayList<String> ageAl = new ArrayList<>();
                        ArrayList<String> educationAl = new ArrayList<>();
                        ArrayList<String> occupationAl = new ArrayList<>();
                        ArrayList<String> genderAl = new ArrayList<>();
                        ArrayList<String> monthlyAl = new ArrayList<>();
                        ArrayList<String> relationAl = new ArrayList<>();
                        nameAl = familyHM.get("name");
                        ageAl = familyHM.get("age");
                        educationAl = familyHM.get("education");
                        occupationAl = familyHM.get("occupation");
                        genderAl = familyHM.get("gender");
                        monthlyAl = familyHM.get("monthly");
                        relationAl = familyHM.get("relation");
                        if (nameAl.size() > 0 && ageAl.size() > 0 && educationAl.size() > 0 && occupationAl.size() > 0
                                && genderAl.size() > 0 && monthlyAl.size() > 0) {
                            for (int i = 0; i < nameAl.size(); i++) {
                                FarmerRegistrationPojo registrationPojo = new FarmerRegistrationPojo();
                                registrationPojo.setName(nameAl.get(i));
                                registrationPojo.setEducation_id(String.valueOf(FamilyEducationNameHM.get(educationAl.get(i))));
                                registrationPojo.setAge(ageAl.get(i));
                                registrationPojo.setOccupation(alternetLivehoodNameHM.get(occupationAl.get(i)));
                                registrationPojo.setGender(genderAl.get(i));
                                registrationPojo.setMonthly_income(FamiliymonthlyNameHM.get(monthlyAl.get(i)));
                                registrationPojo.setRelation_id(relationHM.get(relationAl.get(i)));
                                sqliteHelper.getFarmerFamilyData(registrationPojo, user_id);
                            }
                        }

                        try {
                            if (isInternetOn()) {
                                farmerRegistrationPojoArrayList = sqliteHelper.getTableDataToBeSync();
                                if (farmerRegistrationPojoArrayList.size() > 0) {
                                    for (int i = 0; i < farmerRegistrationPojoArrayList.size(); i++) {
                                        farmerRegistrationPojoArrayList.get(i).setFarmer_family(sqliteHelper.getFarmerFamilyTableDataToBeSync(0));
                                        farmerRegistrationPojoArrayList.get(i).setCrop_vegetable_details(sqliteHelper.getCropVegetableDataToBeSync(0));
                                        farmerRegistrationPojoArrayList.get(i).setUser_id(sharedPrefHelper.getString("user_id", ""));
                                        farmerRegistrationPojoArrayList.get(i).setFarmer_id(farmer_id);

                                        flag= farmerRegistrationPojo.getFlag();
                                        farmerfamilyPojoArrayList.get(i).setRole_id(sharedPrefHelper.getString("role_id",""));
                                        String userId = "";
                                        if (sharedPrefHelper.getString("user_type", "").equals("Farmer")) {
                                            userId = sharedPrefHelper.getString("user_id", "");
                                        } else {
                                            userId = user_id;
                                        }

                                        Gson gson = new Gson();
                                        String data = gson.toJson(farmerRegistrationPojoArrayList.get(i));
                                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                        RequestBody body = RequestBody.create(JSON, data);

                                        if(flag.equals("0")) {
                                            showSubmitDialog(context,"Data has been saved in local database successfully.");
                                        }else {
                                            sendEditFramerRegistrationData(body, userId);
                                        }
                                    }
                                }
                            } else {
//                                Toast.makeText(FarmerRegistration.this, getString(R.string.no_internet_data_saved_locally), Toast.LENGTH_SHORT).show();


                                showSubmitDialog(context,"Data has been saved in local database successfully.");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    if (checkValidation()) {
                        Random random = new Random();
                        int value = random.nextInt(1000);
//                        farmerRegistrationPojo.setHousehold_no(et_householdNo.getText().toString().trim());
                        farmerRegistrationPojo.setFarmer_name(et_FarmerName.getText().toString().trim());
                        farmerRegistrationPojo.setId_no(et_Aadhar.getText().toString().trim());
                        farmerRegistrationPojo.setFather_husband_name(et_husbandFatherName.getText().toString().trim());
                        farmerRegistrationPojo.setMobile((et_mobileNumber.getText().toString().trim()));
                        farmerRegistrationPojo.setHousehold_no(String.valueOf(value));
                        /*here age condition for month*/
                        if (et_FarmerAge.getText().toString().trim().equalsIgnoreCase("0")) {
                            if (age_in_month.length() == 1) {
                                age_in_month = "0.0" + age_in_month;
                            }
                            if (age_in_month.length() == 2) {
                                age_in_month = "0." + age_in_month;
                            }
                            farmerRegistrationPojo.setAge(age_in_month);
                        } else {
                            farmerRegistrationPojo.setAge(et_FarmerAge.getText().toString().trim());
                        }
                        farmerRegistrationPojo.setDate_of_birth(et_Farmerdob.getText().toString().trim());
                        farmerRegistrationPojo.setWhat_you_know(what_you_know);

                        //farmerRegistrationPojo.setEducation_qualification(et_Educationdetails.getText().toString());
                        farmerRegistrationPojo.setTotal_land_holding((et_TotalLandHoldingArea.getText().toString().trim()));
                        farmerRegistrationPojo.setNof_member_migrated(et_NoOfMembers.getText().toString().trim());
                        //farmerRegistrationPojo.setCrop_vegetable_details(et_CropDetails.getText().toString().trim());
                        farmerRegistrationPojo.setAddress(et_address.getText().toString().trim());
                        farmerRegistrationPojo.setPincode(et_pincode.getText().toString().trim());
                        farmerRegistrationPojo.setId_type_id(String.valueOf(ID_Card));
                        farmerRegistrationPojo.setId_other_name(IdCard);
                        farmerRegistrationPojo.setBpl(bpl);
                        farmerRegistrationPojo.setPhysical_challenges(PhysicalChallenges);
                //        farmerRegistrationPojo.setHandicapped(Handicapped);
                        farmerRegistrationPojo.setMulti_cropping(Multicropping);
                        farmerRegistrationPojo.setFertilizer(Fertilizer);
                        farmerRegistrationPojo.setIrrigation_facility(IrrigationFacility);
                        farmerRegistrationPojo.setReligion_id(String.valueOf(hmReligion));
                        farmerRegistrationPojo.setCaste(String.valueOf(hmcaste));
                        farmerRegistrationPojo.setAgro_climat_zone_id(String.valueOf(AgroZone));
                        farmerRegistrationPojo.setAlternative_livelihood_id(String.valueOf(stralternetLivehood));
                        farmerRegistrationPojo.setEducation_id(String.valueOf(hmEducation));
                        farmerRegistrationPojo.setAnnual_income(annualicme);
                        farmerRegistrationPojo.setCategory_id(String.valueOf(hmCategory));
                        farmerRegistrationPojo.setState_id(String.valueOf(state_id));
                        farmerRegistrationPojo.setDistrict_id(String.valueOf(district_id));
                        farmerRegistrationPojo.setBlock_id(String.valueOf(block_id));
                        farmerRegistrationPojo.setVillage_id(String.valueOf(village_id));
                        farmerRegistrationPojo.setGender("Female");
                        usersPojo.setFirst_name(et_FarmerName.getText().toString().trim());
                        usersPojo.setLast_name(et_FarmerName.getText().toString().trim());
                        farmerRegistrationPojo.setGender(getString(R.string.Female));
                        usersPojo.setFirst_name(et_FarmerName.getText().toString().trim());
                        usersPojo.setLast_name(et_FarmerName.getText().toString().trim());
                        usersPojo.setMobile(farmerRegistrationPojo.getMobile());
                        usersPojo.setEmail(et_FarmerName.getText().toString().trim());
                        usersPojo.setProfile_photo(base64);
                        Long aLong = System.currentTimeMillis()/1000;
                        int uuid = Integer.parseInt(aLong.toString());
                        usersPojo.setId(uuid);

                        sqliteHelper.saveUsersData(usersPojo);
                        //in users table last inserted id is user_id
                        int ids = sqliteHelper.getLastInsertedLocalID();

                        sqliteHelper.getFarmerRegistrationData(farmerRegistrationPojo, String.valueOf(ids));

                        //for crop details
                        cropDetailHM.clear();
                        cropDetailHM = MyOperationLayoutAddCropDetails.display(FarmerRegistration.this);
                        ArrayList<String> cropnameAl = new ArrayList<>();
                        ArrayList<String> areaAl = new ArrayList<>();
                        ArrayList<String> quantityAl = new ArrayList<>();
                        ArrayList<String> seasonAl = new ArrayList<>();
                        ArrayList<String> unitAl = new ArrayList<>();
                        ArrayList<String> unitsAl = new ArrayList<>();
                        ArrayList<String> subAl = new ArrayList<>();
                        cropnameAl=cropDetailHM.get("name");
                        areaAl=cropDetailHM.get("area");
                        seasonAl=cropDetailHM.get("season");
                        unitAl=cropDetailHM.get("unit");
                        unitsAl=cropDetailHM.get("units");
                        subAl=cropDetailHM.get("subname");
                        quantityAl=cropDetailHM.get("quantity");

                        if (cropnameAl.size()>0 && areaAl.size()>0 && seasonAl.size() >0 && unitAl.size()>0 && unitsAl.size()>0 ){
                            for (int i = 0; i < cropnameAl.size() ; i++) {
                                CropVegitableDetails vegitableDetails=new CropVegitableDetails();
                                vegitableDetails.setCrop_name(String.valueOf(cropCategoryHM.get(cropnameAl.get(i))));
                                getsubcategoey(Integer.parseInt(vegitableDetails.getCrop_name()));
                                vegitableDetails.setCrop_type_subcatagory_id(String.valueOf(subCategoryHM.get(subAl.get(i))));
                                vegitableDetails.setArea(areaAl.get(i));
                                vegitableDetails.setQuantity(quantityAl.get(i));
                                vegitableDetails.setSeason_id(seasonAl.get(i));
                                vegitableDetails.setUnit_id(String.valueOf(unitHM.get(unitAl.get(i).trim())));
                                vegitableDetails.setUnits_id(String.valueOf(unitshm.get(unitsAl.get(i).trim())));
                                sqliteHelper.setCropDetailsData(vegitableDetails,String.valueOf(ids));
                            }
                        }

                        cropVegitableDetails.setCrop_name(String.valueOf(cat_ID));
                        cropVegitableDetails.setCrop_type_subcatagory_id(String.valueOf(subcropCategory_id));
                        cropVegitableDetails.setArea(et_Area.getText().toString().trim());
                        cropVegitableDetails.setQuantity(et_quantity.getText().toString().trim());
                        cropVegitableDetails.setUnit_id(String.valueOf(unitId));
                        cropVegitableDetails.setUnits_id(String.valueOf(unitsId));
                        cropVegitableDetails.setSeason_id(String.valueOf(seasonId));
                        sqliteHelper.setCropDetailsData(cropVegitableDetails,String.valueOf(ids));
                        // for family table
                        familyHM.clear();
                        familyHM = MyOperationLayoutAddMemeber.display(FarmerRegistration.this);
                        ArrayList<String> nameAl = new ArrayList<>();
                        ArrayList<String> ageAl = new ArrayList<>();
                        ArrayList<String> educationAl = new ArrayList<>();
                        ArrayList<String> occupationAl = new ArrayList<>();
                        ArrayList<String> genderAl = new ArrayList<>();
                        ArrayList<String> monthlyAl = new ArrayList<>();
                        ArrayList<String> relationAl = new ArrayList<>();
                        nameAl = familyHM.get("name");
                        ageAl = familyHM.get("age");
                        educationAl = familyHM.get("education");
                        occupationAl = familyHM.get("occupation");
                        genderAl = familyHM.get("gender");
                        monthlyAl = familyHM.get("monthly");
                        relationAl = familyHM.get("relation");
                        if (nameAl.size() > 0 && ageAl.size() > 0 && educationAl.size() > 0 && occupationAl.size() > 0
                                && genderAl.size() > 0 && monthlyAl.size() > 0) {
                            for (int i = 0; i < nameAl.size(); i++) {
                                FarmerRegistrationPojo registrationPojo = new FarmerRegistrationPojo();
                                registrationPojo.setName(nameAl.get(i));
                                registrationPojo.setEducation_id(String.valueOf(FamilyEducationNameHM.get(educationAl.get(i))));
                                registrationPojo.setAge(ageAl.get(i));
                                registrationPojo.setOccupation(alternetLivehoodNameHM.get(occupationAl.get(i)));
                                registrationPojo.setGender(genderAl.get(i));
                                registrationPojo.setMonthly_income(FamiliymonthlyNameHM.get(monthlyAl.get(i)));
                                registrationPojo.setRelation_id((relationHM.get(relationAl.get(i))));
                                sqliteHelper.getFarmerFamilyData(registrationPojo, String.valueOf(ids));
                            }
                        }
                        farmerRegistrationPojo.setName(et_name.getText().toString().trim());
                        farmerRegistrationPojo.setEducation_id(String.valueOf(FamiliyEducation));
                        farmerRegistrationPojo.setAge((et_Age.getText().toString().trim()));
                        farmerRegistrationPojo.setGender(spn_gender.getSelectedItem().toString().trim());
                        farmerRegistrationPojo.setMonthly_income(Familiymonthly);
                        farmerRegistrationPojo.setOccupation((stralternetLivehood));
                        farmerRegistrationPojo.setRelation_id((relation_id));
                        sqliteHelper.getFarmerFamilyData(farmerRegistrationPojo, String.valueOf(ids));

                        try {
                            if (isInternetOn() && fromLogin.equals("1")) {
                                farmerRegistrationPojoArrayList = sqliteHelper.getTableDataToBeSync();
                                if (farmerRegistrationPojoArrayList.size() > 0) {
                                    for (int i = 0; i < farmerRegistrationPojoArrayList.size(); i++) {
                                        farmerRegistrationPojoArrayList.get(i).setFarmer_family(sqliteHelper.getFarmerFamilyTableDataToBeSync(0));
                                        farmerRegistrationPojoArrayList.get(i).setCrop_vegetable_details(sqliteHelper.getCropVegetableDataToBeSync(0));

                                        Gson gson = new Gson();
                                        String data = gson.toJson(farmerRegistrationPojoArrayList.get(i));
                                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                        RequestBody body = RequestBody.create(JSON, data);
                                        sendFramerRegistrationData(body, farmerRegistrationPojoArrayList.get(i).getUid());
                                    }
                                }
                            }else if(fromLogin.equals("2")){



//                                Toast.makeText(FarmerRegistration.this, R.string.no_internet_data_saved_locally, Toast.LENGTH_LONG).show();
                                showSubmitDialog(context,"Data has been saved in local database successfully.");

                            }
                            else {
                                Toast.makeText(FarmerRegistration.this, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void sendEditFramerRegistrationData(RequestBody body, String userId) {
        ProgressDialog dialog = ProgressDialog.show(FarmerRegistration.this, "", getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).callEditFarmerRegistrationApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String user_id = jsonObject.optString("user_id");
                    if (status.equalsIgnoreCase("1")) {
                        sqliteHelper.updateFlag("users", Integer.parseInt(userId), 1);
                        sqliteHelper.updateFlag("farmer_registration", Integer.parseInt(userId), 1);
                        sqliteHelper.updateFlag("farmer_family", Integer.parseInt(userId), 1);
                        sqliteHelper.updateFlag("crop_vegetable_details", Integer.parseInt(userId), 1);

                        Toast.makeText(FarmerRegistration.this, "" + message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FarmerRegistration.this, HomeAcivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(FarmerRegistration.this, "" + message, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void sendFramerRegistrationData(RequestBody body, String localId) {
        ProgressDialog dialog = ProgressDialog.show(FarmerRegistration.this, "", getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).sendFormerRegistrationData(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String user_id = jsonObject.optString("user_id");
                    /*sharedPrefHelper.setString("user_id", user_id);*/

                    if (status.equalsIgnoreCase("1")) {
                        //update flag in tables
                        sqliteHelper.updateId("users", "user_id", Integer.parseInt(user_id), Integer.parseInt(localId), "id");
                        sqliteHelper.updateId("users", "id", Integer.parseInt(user_id), Integer.parseInt(user_id), "user_id");
                        sqliteHelper.updateId("farmer_registration", "user_id", Integer.parseInt(user_id), Integer.parseInt(localId), "user_id");
                        sqliteHelper.updateId("farmer_family", "user_id", Integer.parseInt(user_id), Integer.parseInt(localId), "user_id");
                        sqliteHelper.updateId("crop_vegetable_details", "user_id", Integer.parseInt(user_id), Integer.parseInt(localId), "user_id");
                        //int idss = sqliteHelper.getLastInsertedLocalID();
                        sqliteHelper.updateFlag("users", Integer.parseInt(user_id), 1);
                        sqliteHelper.updateFlag("farmer_registration", Integer.parseInt(user_id), 1);
                        sqliteHelper.updateFlag("farmer_family", Integer.parseInt(user_id), 1);
                        sqliteHelper.updateFlag("crop_vegetable_details", Integer.parseInt(user_id), 1);

                        if(fromLogin.equals("2")){
                            Toast.makeText(FarmerRegistration.this, "" + message, Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(FarmerRegistration.this, HomeAcivity.class);
                            startActivity(homeIntent);
                            finish();
                        }else {
                            Toast.makeText(FarmerRegistration.this, "Registration has been done successfully now you can login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FarmerRegistration.this, LoginScreenActivity.class);
                            intent.putExtra("Mobile", farmerRegistrationPojo.getMobile());
                            startActivity(intent);
                            finish();
                        }
                    }else if(status.equals("2")){
                        int idss = sqliteHelper.getLastInsertedLocalID();
                        sqliteHelper.dropTableFamily("users", String.valueOf(idss));
                        sqliteHelper.dropTableFamily("farmer_registration", String.valueOf(idss));
                        sqliteHelper.dropTableFamily("farmer_family", String.valueOf(idss));
                        sqliteHelper.dropTableFamily("crop_vegetable_details", String.valueOf(idss));
                           Toast.makeText(FarmerRegistration.this, R.string.your_number_already_exist, Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(FarmerRegistration.this, "" + message, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private boolean checkValidation() {
        boolean ret = true;
//        if (base64.equals("")) {
//            Toast.makeText(this, R.string.photo, Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (!et_FarmerName.getText().toString().trim().matches("[a-zA-Z ]+")) {
            EditText flagEditfield = et_FarmerName;
            String msg = getString(R.string.Please_Enter_Farmer_Name);
            et_FarmerName.setError(msg);
            et_FarmerName.requestFocus();
            return false;
        }
        if (rb_AadharCard.isChecked() || rb_otherNationalIdCard.isChecked()) {
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.Please_select_Id_Number), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (et_Aadhar.getText().toString().trim().length() < 12 ) {
            EditText flagEditfield = et_husbandFatherName;
            String msg = getString(R.string.Please_enter_valid_id);
            et_Aadhar.setError(msg);
            et_Aadhar.requestFocus();
            return false;
        }

        if (!et_husbandFatherName.getText().toString().trim().matches("[a-zA-Z ]+")){
            EditText flagEditfield = et_husbandFatherName;
            String msg = getString(R.string.Please_Enter_Fatherhusband_Name);
            et_husbandFatherName.setError(msg);
            et_husbandFatherName.requestFocus();
            return false;
        }
        if (et_mobileNumber.getText().toString().trim().length() < 10) {
            EditText flagEditfield = et_mobileNumber;
            String msg = getString(R.string.Please_Enter_Valid_Contact_Number);
            et_mobileNumber.setError(msg);
            et_mobileNumber.requestFocus();
            return false;
        }
        if (rb_age.isChecked() || rb_dob.isChecked()) {
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.Please_select_DOB_Or_Age), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!et_FarmerAge.getText().toString().equalsIgnoreCase("") && Double.parseDouble(et_FarmerAge.getText().toString().trim()) > 120) {
            et_FarmerAge.setError(" Age Should be between 18  to 120 ");
            et_FarmerAge.requestFocus();
            ret = false;
        } else if (!et_FarmerAge.getText().toString().equalsIgnoreCase("") && Double.parseDouble(et_FarmerAge.getText().toString().trim()) < 18) {
            et_FarmerAge.setError(" Age Should be between 18  to 120 ");
            et_FarmerAge.requestFocus();
            ret = false;
        }
        if (rb_age.isChecked()) {
            if (et_FarmerAge.getText().toString().trim().length() < 0) {
                EditText flagEditfield = et_FarmerAge;
                String msg = getString(R.string.Please_Enter_ValidAge);
                et_FarmerAge.setError(msg);
                et_FarmerAge.requestFocus();
                return false;
            }
        } else {
            if (et_Farmerdob.getText().toString().trim().length() < 0) {
                EditText flagEditfield = et_Farmerdob;
                String msg = getString(R.string.Please_Enter_Valid_Date_of_Birth);
                et_Farmerdob.setError(msg);
                et_Farmerdob.requestFocus();
                return false;
            }
        }
        if (et_address.getText().toString().trim().equalsIgnoreCase("")) {
            EditText flagEditfield = et_address;
            String msg = getString(R.string.please_enter_address);
            et_address.setError(msg);
            et_address.requestFocus();
            return false;
        }
//        if (!et_FarmerName.getText().toString().trim().matches("[a-zA-Z ]+")) {
//            EditText flagEditfield = et_FarmerName;
//            String msg = getString(R.string.Please_Enter_Farmer_Name);
//            et_FarmerName.setError(msg);
//            et_FarmerName.requestFocus();
//            return false;
//        }



        if (spn_State.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(spn_State.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) spn_State.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }

        if (spn_district.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(spn_district.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) spn_district.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }


        if (spn_block.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(spn_block.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) spn_block.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }

        if (spn_village.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(spn_village.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) spn_village.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }
        if (et_pincode.getText().toString().trim().length() < 6) {
            EditText flagEditfield = et_pincode;
            String msg = getString(R.string.Please_Enter_Valid_Number);
            et_pincode.setError(msg);
            et_pincode.requestFocus();
            return false;
        }
        if (rb_BPLYes.isChecked() || rb_BPLNo.isChecked()) {
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.Please_select_BPL), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spn_religion.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(spn_religion.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) spn_religion.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }

        if (spn_cast.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(spn_cast.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) spn_cast.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }


        if (spn_education.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(spn_education.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) spn_education.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }

        if (rb_PhysicalChallengesYes.isChecked() || rb_PhysicalChallengesNo.isChecked()) {
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.Please_select_Physical_Challenges), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spn_category.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(spn_category.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) spn_category.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }

        if (rg_HandicappedYes.isChecked() || rg_HandicappedNo.isChecked()) {
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.Please_select_Handicapped), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_TotalLandHoldingArea.getText().toString().trim().length() < 1) {
            EditText flagEditfield = et_TotalLandHoldingArea;
            String msg = getString(R.string.Please_Enter_Valid_Number);
            et_TotalLandHoldingArea.setError(msg);
            et_TotalLandHoldingArea.requestFocus();
            return false;
        }
        if (spn_annualIncome.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(spn_annualIncome.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) spn_annualIncome.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }


        if (spn_alternetLivehood.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(spn_alternetLivehood.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) spn_alternetLivehood.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }


        if (et_NoOfMembers.getText().toString().trim().length() < 1) {
            EditText flagEditfield = et_NoOfMembers;
            String msg = getString(R.string.Please_Enter_Valid_Number);
            et_NoOfMembers.setError(msg);
            et_NoOfMembers.requestFocus();
            return false;
        }

        if (rb_MulticroppingYes.isChecked() || rb_MulticroppingNo.isChecked()) {
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.Please_select_Multicropping), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rb_FertilizerYes.isChecked() || rb_FertilizerNo.isChecked()) {
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.Please_select_Fertilizer), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rb_IrrigationFacilityYes.isChecked() || rb_IrrigationFacilityNo.isChecked()) {
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.Please_select_IrrigationFacility), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(ch_term_condition.isChecked()){
            ret=true;
        }
        else{
            ret=false;
            Toast.makeText(FarmerRegistration.this, R.string.terms_and_conmdition, Toast.LENGTH_SHORT).show();
        }


//        if (spn_religion.getSelectedItem().toString().trim().equals("Pick one")) {
//            Toast.makeText(FarmerRegistration.this, getString(R.string.select), Toast.LENGTH_SHORT).show();
//        }
        return ret;
    }
    private void AllRadioButtons() {
        rg_IdCard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_AadharCard:
                        IdCard = getString(R.string.Aadhar_Card);
                        et_SCan.setVisibility(View.VISIBLE);
                        ll_other.setVisibility(View.GONE);
                        rb_AadharCard.requestFocus();
                        et_Aadhar.setInputType(InputType.TYPE_CLASS_NUMBER);

                        break;

                    case R.id.rb_otherNationalIdCard:
                        IdCard = getString(R.string.Other);
                        ll_other.setVisibility(View.VISIBLE);
                        et_SCan.setVisibility(View.GONE);
                        et_Aadhar.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                }
            }
        });
        rg_BPL.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_BPLYes:
                        bpl = getString(R.string.yes);
                        break;

                    case R.id.rb_BPLNo:
                        bpl = getString(R.string.no);
                        break;
                }
            }
        });

     /*   rg_Handicapped.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rg_HandicappedYes:
                        Handicapped = getString(R.string.yes);
                        break;

                    case R.id.rg_HandicappedNo:
                        Handicapped = getString(R.string.no);
                        break;
                }
            }
        });     */

        rg_PhysicalChallenges.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_PhysicalChallengesYes:
                        PhysicalChallenges = getString(R.string.yes);
                        break;

                    case R.id.rb_PhysicalChallengesNo:
                        PhysicalChallenges = getString(R.string.no);
                        break;
                }
            }
        });

        rg_Multicropping.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_MulticroppingYes:
                        Multicropping = getString(R.string.yes);
                        break;

                    case R.id.rb_MulticroppingNo:
                        Multicropping = getString(R.string.no);
                        break;
                }
            }
        });

        rg_Fertilizer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_FertilizerYes:
                        Fertilizer = getString(R.string.yes);
                        break;

                    case R.id.rb_FertilizerNo:
                        Fertilizer = getString(R.string.no);
                        break;
                }
            }
        });
        rg_IrrigationFacility.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_IrrigationFacilityYes:
                        IrrigationFacility = getString(R.string.yes);
                        break;

                    case R.id.rb_IrrigationFacilityNo:
                        IrrigationFacility = getString(R.string.no);
                        break;
                }
            }
        });


    }

    private void getAllStateFromTable() {
        stateArrayList.clear();
        String language = sharedPrefHelper.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            stateNameHM = sqliteHelper.getAllState(2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            stateNameHM = sqliteHelper.getAllState(3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            stateNameHM = sqliteHelper.getAllState(4);
        }
        else
        {
            stateNameHM = sqliteHelper.getAllState(1);
        }
        for (int i = 0; i < stateNameHM.size(); i++) {
            stateArrayList.add(stateNameHM.keySet().toArray()[i].toString().trim());
        }
        Collections.sort(stateArrayList);
        if (isEditable) {
            /*stateArrayList.add(0, state_name);
            getAllDistrictFromTable(0);
            getAllBlockFromTable(0);
            getAllVillageFromTable(0);*/
            stateArrayList.add(0, getString(R.string.select_state));
        } else {
            stateArrayList.add(0, getString(R.string.select_state));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, stateArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_State.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = state_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = state_name;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_State.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }
    }

    private void setStateSpinner() {
        spn_State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!spn_State.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_state))) {
                    if (spn_State.getSelectedItem().toString().trim() != null) {
                        state_id = stateNameHM.get(spn_State.getSelectedItem().toString().trim());
                        getAllDistrictFromTable(state_id);
                    }

                } else {
                    getAllDistrictFromTable(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    }

    private void getAllDistrictFromTable(int state_id) {
        distrcitArrayList.clear();
        String language = sharedPrefHelper.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            districtNameHM = sqliteHelper.getAllDistrict(state_id,2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            districtNameHM = sqliteHelper.getAllDistrict(state_id,3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            districtNameHM = sqliteHelper.getAllDistrict(state_id,4);
        }
        else
        {
            districtNameHM = sqliteHelper.getAllDistrict(state_id,1);
        }
        for (int i = 0; i < districtNameHM.size(); i++) {
            distrcitArrayList.add(districtNameHM.keySet().toArray()[i].toString().trim());
        }
        Collections.sort(distrcitArrayList);
        if (isEditable) {
            //distrcitArrayList.add(0, district_name);
            distrcitArrayList.add(0, getString(R.string.select_district));

        } else {
            distrcitArrayList.add(0, getString(R.string.select_district));
        }

        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, distrcitArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_district.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = district_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = district_name;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_district.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }
    }

    private void setDistrictSpinner() {
        spn_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_district.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_district))) {
                    if (spn_district.getSelectedItem().toString().trim() != null) {
                        district_id = districtNameHM.get(spn_district.getSelectedItem().toString().trim());
                        getAllBlockFromTable(district_id);
                    }
                } else {
                    getAllBlockFromTable(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAllBlockFromTable(int district_id) {
        blockArrayList.clear();
        String language = sharedPrefHelper.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            blockNameHM = sqliteHelper.getAllBlock(district_id,2);
        }
        else  if(language.equalsIgnoreCase("kan"))
        {
            blockNameHM = sqliteHelper.getAllBlock(district_id,3);
        }
        else  if(language.equalsIgnoreCase("guj"))
        {
            blockNameHM = sqliteHelper.getAllBlock(district_id,4);
        }
        else
        {
            blockNameHM = sqliteHelper.getAllBlock(district_id,1);
        }
        for (int i = 0; i < blockNameHM.size(); i++) {
            blockArrayList.add(blockNameHM.keySet().toArray()[i].toString().trim());
        }
        Collections.sort(blockArrayList);
        if (isEditable) {
            //blockArrayList.add(0, block_name);
            blockArrayList.add(0, getString(R.string.select_block));

        } else {
            blockArrayList.add(0, getString(R.string.select_block));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, blockArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_block.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = block_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = block_name;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_block.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }
    }

    public static void showSubmitDialog(Context context,String message) {
        submit_alert = new android.app.Dialog(context);

        submit_alert.setContentView(R.layout.submitdialog);
        submit_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = submit_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        TextView tvDescription = (TextView) submit_alert.findViewById(R.id.tv_description);
        Button btnOk = (Button) submit_alert.findViewById(R.id.btnOk);

        tvDescription.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO
                submit_alert.dismiss();
                Intent intent = new Intent(context, HomeAcivity.class);
                context.startActivity(intent);
            }
        });

        submit_alert.show();
        submit_alert.setCanceledOnTouchOutside(false);
    }


    private void setBlockSpinner() {
        spn_block.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_block.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_block))) {
                    if (spn_block.getSelectedItem().toString().trim() != null) {
                        block_id = blockNameHM.get(spn_block.getSelectedItem().toString().trim());
                        getAllVillageFromTable(block_id);
                    }
                } else {
                    getAllVillageFromTable(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAllVillageFromTable(int block_id) {
        villageArrayList.clear();
        String language = sharedPrefHelper.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            villageNameHM = sqliteHelper.getAllVillage(block_id,2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            villageNameHM = sqliteHelper.getAllVillage(block_id,3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            villageNameHM = sqliteHelper.getAllVillage(block_id,4);
        }
        else
        {
            villageNameHM = sqliteHelper.getAllVillage(block_id,1);
        }
        for (int i = 0; i < villageNameHM.size(); i++) {
            villageArrayList.add(villageNameHM.keySet().toArray()[i].toString().trim());
        }
        Collections.sort(villageArrayList);
        if (isEditable) {
            //villageArrayList.add(0, village_name);
            villageArrayList.add(0, getString(R.string.select_village));

        } else {
            villageArrayList.add(0, getString(R.string.select_village));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, villageArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_village.setAdapter(Adapter);
        if (isEditable) {
            int spinnerPosition = 0;
            String strpos1 = village_name;
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                strpos1 = village_name;
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_village.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }
    }

    private void setVillageSpinner() {
        spn_village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_village.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_village))) {
                    village_id = villageNameHM.get(spn_village.getSelectedItem().toString().trim());
                    pincode = sqliteHelper.getVillageName(village_id);
                    et_pincode.setText(pincode);
                    et_pincode.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void idcard() {
        FamilyEducationArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(FarmerRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            FamilyEducationNameHM = sqliteHelper.getMasterSpinnerId(9, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            FamilyEducationNameHM = sqliteHelper.getMasterSpinnerId(9, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            FamilyEducationNameHM = sqliteHelper.getMasterSpinnerId(9, 4);
        }
        else
        {
            FamilyEducationNameHM = sqliteHelper.getMasterSpinnerId(9, 1);
        }

        for (int i = 0; i < FamilyEducationNameHM.size(); i++) {
            FamilyEducationArrayList.add(FamilyEducationNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            //FamilyEducationArrayList.add(0, Id_Card);
            FamilyEducationArrayList.add(0, getString(R.string.select_education));
        } else {
            FamilyEducationArrayList.add(0, getString(R.string.select_education));
        }

        final ArrayAdapter ff = new ArrayAdapter(this, R.layout.spinner_list, FamilyEducationArrayList);
        ff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnF_Education.setAdapter(ff);

        spnF_Education.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spnF_Education.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_education))) {
                    if (spnF_Education.getSelectedItem().toString().trim() != null) {
                        FamiliyEducation = FamilyEducationNameHM.get(spnF_Education.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FamiliymonthlyArrayList.clear();
        if(language.equalsIgnoreCase("hin"))
        {
            FamiliymonthlyNameHM = sqliteHelper.getMasterSpinnerId(1, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            FamiliymonthlyNameHM = sqliteHelper.getMasterSpinnerId(1, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            FamiliymonthlyNameHM = sqliteHelper.getMasterSpinnerId(1, 4);
        }
        else
        {
            FamiliymonthlyNameHM = sqliteHelper.getMasterSpinnerId(1, 1);
        }

        for (int i = 0; i < FamiliymonthlyNameHM.size(); i++) {
            FamiliymonthlyArrayList.add(FamiliymonthlyNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            FamiliymonthlyArrayList.add(0, AnnualIncome);
        } else {
        }
        final ArrayAdapter xx = new ArrayAdapter(this, R.layout.spinner_list, FamiliymonthlyArrayList);
        xx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFamiliy_monthly.setAdapter(xx);

        spnFamiliy_monthly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spnFamiliy_monthly.getSelectedItem().toString().trim().equalsIgnoreCase("")) {
                    if (spnFamiliy_monthly.getSelectedItem().toString().trim() != null) {
                        Familiymonthly = FamiliymonthlyNameHM.get(spnFamiliy_monthly.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      /*  final ArrayAdapter occuptionfamily_dtl = new ArrayAdapter(this, R.layout.spinner_list, occuptionfamily_dtls);
        occuptionfamily_dtl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_Occupation.setAdapter(occuptionfamily_dtl);
*/


        if(language.equalsIgnoreCase("hin"))
        {

            String text = getString(R.string.agree_our_terms_and_condition);
            SpannableString spannableString = new SpannableString(text);
            ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent=new Intent(FarmerRegistration.this, Disclaimer.class);
                    startActivity(intent);
                }
            };
            spannableString.setSpan(clickableSpan1, 54,64, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_disclaimer.setText(spannableString);
            tv_disclaimer.setMovementMethod(LinkMovementMethod.getInstance());
        } else if(language.equalsIgnoreCase("kan"))
        {

            String text = getString(R.string.agree_our_terms_and_condition);
            SpannableString spannableString = new SpannableString(text);
            ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent=new Intent(FarmerRegistration.this, Disclaimer.class);
                    startActivity(intent);
                }
            };
            spannableString.setSpan(clickableSpan1, 54,64, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_disclaimer.setText(spannableString);
            tv_disclaimer.setMovementMethod(LinkMovementMethod.getInstance());
        }
        else
        {

            String text = getString(R.string.agree_our_terms_and_condition);
            SpannableString spannableString = new SpannableString(text);
            ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent=new Intent(FarmerRegistration.this, Disclaimer.class);
                    startActivity(intent);
                }
            };
            spannableString.setSpan(clickableSpan1, 54,64, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_disclaimer.setText(spannableString);
            tv_disclaimer.setMovementMethod(LinkMovementMethod.getInstance());
        }

        SharedPrefHelper spfn = new SharedPrefHelper(FarmerRegistration.this);
        String language1 = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            final ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_list, genderAk);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_gender.setAdapter(arrayAdapter);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            final ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_list, genderAM);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_gender.setAdapter(arrayAdapter);
        }
        else
        {
            final ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_list, genderAL);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_gender.setAdapter(arrayAdapter);
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (fromLogin.equals("1")) {
                Intent intentHome = new Intent(FarmerRegistration.this, LoginScreenActivity.class);
                startActivity(intentHome);
                finish();
            } else {
                Intent intentHome = new Intent(FarmerRegistration.this, RegistrationListActivity.class);
                startActivity(intentHome);
                finish();
            }

            // finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }


    public void onRadioButtonClicked(View view) {

    }

    @Override
    public void onOptionSelected(ImagePickerEnum imagePickerEnum) {

        /*if (imagePickerEnum == ImagePickerEnum.FROM_CAMERA)
            openCamera();
        else if (imagePickerEnum == ImagePickerEnum.FROM_GALLERY)
            openImagesDocument();*/

    }

    private void openCamera() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file;
        try {
            file = getImageFile(); // 1
        } catch (Exception e) {
            e.printStackTrace();
            uiHelper.toast(this, getString(R.string.Please_take_another_image));
            return;
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID.concat(".provider"), file);
        else
            uri = Uri.fromFile(file); // 3
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
        startActivityForResult(pictureIntent, CAMERA_ACTION_PICK_REQUEST_CODE);
    }

    private File getImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        /*File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        ); */
        String imagesFolder="images";
        File storageDir = new File(
                Environment.getExternalStorageDirectory(), imagesFolder
        );
        System.out.println(storageDir.getAbsolutePath());
        if (!storageDir.exists()){
            storageDir.mkdirs();
            System.out.println("File not exists"); }
        else
            System.out.println("File exists");
        //File file = File.createTempFile(imageFileName, ".jpg", storageDir);
        File file = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                uiHelper.toast(this, getString(R.string.ImageCropper1));
                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                uiHelper.toast(this, getString(R.string.ImageCropper2));
                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                uiHelper.toast(this, getString(R.string.ImageCropper3));
                finish();
            }
        } else if (requestCode == ONLY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else {
                uiHelper.toast(this, getString(R.string.ImageCropper2));
                finish();
            }
        } else if (requestCode == ONLY_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else {
                uiHelper.toast(this, getString(R.string.ImageCropper3));
                finish();
            }
        }
    }

    private void openImagesDocument() {
        Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pictureIntent.setType("image/*");
        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
            pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(pictureIntent, getString(R.string.Select_Picture)), PICK_IMAGE_GALLERY_REQUEST_CODE);
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String getStringImage(Bitmap image) {
        ByteArrayOutputStream byteArrayOS = null;
        try {
            System.gc();
            byteArrayOS = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOS);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOS);
        }
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //profile crop-image intent
            if (requestCode == CAMERA_ACTION_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
                Uri uri = Uri.parse(currentPhotoPath);
                openCropActivity(uri, uri);
            } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = UCrop.getOutput(data);
                    showImage(uri);
                }
            } else if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                try {
                    Uri sourceUri = data.getData();
                    File file = getImageFile();
                    Uri destinationUri = Uri.fromFile(file);
                    openCropActivity(sourceUri, destinationUri);
                } catch (Exception e) {
                    uiHelper.toast(this, getString(R.string.Please_select_another_image));
                }

            }
            else if (scanningResult != null) {
                //we have a result
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();

                // process received data
                if (scanContent != null && !scanContent.isEmpty()) {
                    processScannedData(scanContent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.Scan_Cancelled), Toast.LENGTH_SHORT);
                    toast.show();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.Noscan_data_received), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    protected void processScannedData(String scanData) {
        Log.d("Rajdeol", scanData);
        XmlPullParserFactory pullParserFactory;

        try {
            // init the parserfactory
            pullParserFactory = XmlPullParserFactory.newInstance();
            // get the parser
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(scanData));

            // parse the XML
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("Rajdeol", "Start document");
                } else if (eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
                    // extract data from tag
                    //uid
                    String uid = parser.getAttributeValue(null, DataAttributes.AADHAR_UID_ATTR);
                    et_Aadhar.setText(uid);

                } else if (eventType == XmlPullParser.END_TAG) {
                    Log.d("Rajdeol", "End tag " + parser.getName());

                } else if (eventType == XmlPullParser.TEXT) {
                    Log.d("Rajdeol", "Text " + parser.getText());

                }
                // update eventType
                eventType = parser.next();
            }

            // display the data on screen

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }// EO function

    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        /*options.setCircleDimmedLayer(true);
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.colorAccent));*/
        options.withAspectRatio(5f, 5f);
        options.setCompressionQuality(100);
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this);
    }

    private void showImage(Uri imageUri) {
        try {
            File file;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                file = FileUtils.getFile(this, imageUri);
            } else {
                file = new File(currentPhotoPath);
            }
            InputStream inputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView_profile.setImageBitmap(bitmap); //set to show image

            bitmap = getResizedBitmap(bitmap, 600); //get resize image
            base64 = getStringImage(bitmap); //get base 64 image file.
            Log.e("image", base64);
        } catch (Exception e) {
            uiHelper.toast(this, getString(R.string.profile_picture));
        }
    }
}