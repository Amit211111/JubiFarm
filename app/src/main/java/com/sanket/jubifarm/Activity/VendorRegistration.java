package com.sanket.jubifarm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

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
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;

import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.BuildConfig;
import com.sanket.jubifarm.Modal.SupplierRegistrationPojo;
import com.sanket.jubifarm.Modal.VendorRegModal;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.Supplyer.Supplayer_otp;
import com.sanket.jubifarm.cropImage.enums.ImagePickerEnum;
import com.sanket.jubifarm.cropImage.listeners.IImagePickerLister;
import com.sanket.jubifarm.cropImage.utils.FileUtils;
import com.sanket.jubifarm.cropImage.utils.UiHelper;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.yalantis.ucrop.UCrop;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorRegistration extends AppCompatActivity implements IImagePickerLister {
    MultiSpinnerSearch spn_vendor;
    ImageView img_aadhar, img_pan, img_gst;
    Button btn_submit;
    EditText et_companyName, et_contactNo, et_propiretor, address, et_gstn, et_pancard, adharNo, et_email;
    private Spinner spn_State, spn_District;

    /*enable crop image*/
    private Context context = this;
    private static final int CAMERA_ACTION_PICK_REQUEST_CODE = 610;
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    public static final int CAMERA_STORAGE_REQUEST_CODE = 611;
    public static final int ONLY_CAMERA_REQUEST_CODE = 612;
    public static final int ONLY_STORAGE_REQUEST_CODE = 613;
    private String currentPhotoPath = "";
    private String currentPhotoPathPanCard = "";
    private String currentPhotoPathAadharCard = "";
    private UiHelper uiHelper = new UiHelper();
    private String base64 = "";
    private String base64PanCard = "";
    private String base64AadharCard = "";
    private boolean isGSTImageCondition = false;
    private boolean isPanCardImageCondition = false;
    private boolean isAadharCardImageCondition = false;

    private int vendorCategoryId=0;
    private SharedPrefHelper sharedPrefHelper;
    private SqliteHelper sqliteHelper;
    private SupplierRegistrationPojo registrationPojo;
    private VendorRegModal vendorRegModal;
    private HashMap<String, Integer> categoryHM;
    private ArrayList<String> categoryAl;
    private ArrayList<String> categoryValueAl;
    private ArrayList<VendorRegModal> vendorRegModals;
    private int state_id=0, district_id=0;
    private HashMap<String, Integer> stateNameHM, districtNameHM;
    private ArrayList<String> stateArrayList, distrcitArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_registration);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.Vendor_Registration) + "</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initViews();
        getAllStateFromTable();
        setStateSpinner();
        setDistrictSpinner();
        setVendorCategorySpinner();
        ButtonListener();
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
        /*if (isEditable) {
            stateArrayList.add(0, state_name);
            getAllDistrictFromTable(0);
            getAllBlockFromTable(0);
            getAllVillageFromTable(0);
        } else {*/
            stateArrayList.add(0, getString(R.string.select_state));
        //}
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, stateArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_State.setAdapter(Adapter);


    }

    private void setStateSpinner() {
        spn_State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!spn_State.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_state))) {
                    if (spn_State.getSelectedItem().toString().trim() != null) {
                        state_id = stateNameHM.get(spn_State.getSelectedItem().toString().trim());
                        Log.e("StateID : ", "===" + state_id);
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
        }        for (int i = 0; i < districtNameHM.size(); i++) {
            distrcitArrayList.add(districtNameHM.keySet().toArray()[i].toString().trim());
        }
        /*if (isEditable) {
            distrcitArrayList.add(0, district_name);
        } else {*/
            distrcitArrayList.add(0, getString(R.string.select_district));
        //}

        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, distrcitArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_District.setAdapter(Adapter);
    }

    private void setDistrictSpinner() {
        spn_District.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_District.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_district))) {
                    if (spn_District.getSelectedItem().toString().trim() != null) {
                        district_id = districtNameHM.get(spn_District.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setVendorCategorySpinner() {
        SharedPrefHelper spf = new SharedPrefHelper(VendorRegistration.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            categoryHM = sqliteHelper.getMasterSpinnerId(13, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            categoryHM = sqliteHelper.getMasterSpinnerId(13, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            categoryHM = sqliteHelper.getMasterSpinnerId(13, 4);
        }
        else
        {
            categoryHM = sqliteHelper.getMasterSpinnerId(13, 1);
        }

        HashMap<String, Integer> sortedMapAsc = sortCategoryByComparator(categoryHM);

        categoryAl.clear();
        categoryValueAl.clear();
        for (int i = 0; i < sortedMapAsc.size(); i++) {
            categoryAl.add(sortedMapAsc.keySet().toArray()[i].toString().trim());
            categoryValueAl.add(String.valueOf(sortedMapAsc.get(categoryAl.get(i))));
        }

        final List<KeyPairBoolData> listArray0 = new ArrayList<>();
        for (int i = 0; i < categoryAl.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(categoryAl.get(i));
            h.setSelected(false);
            listArray0.add(h);
        }

        spn_vendor.setItems(listArray0, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        categoryAl.add(sortedMapAsc.keySet().toArray()[i].toString().trim());
                        categoryValueAl.add(String.valueOf(sortedMapAsc.get(categoryAl.get(i))));
                        Gson gson = new Gson();
                        String data = gson.toJson(categoryValueAl);

                        Log.i("xnnc", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                        //Toast.makeText(context, ""+data, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private HashMap<String, Integer> sortCategoryByComparator(HashMap<String, Integer> categoryHM) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(categoryHM.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {

                return o1.getValue().compareTo(o2.getValue());

            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    private void initViews() {
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        registrationPojo = new SupplierRegistrationPojo();
        vendorRegModal = new VendorRegModal();
        spn_vendor = findViewById(R.id.spn_vendor);
        et_companyName = findViewById(R.id.et_companyName);
        et_contactNo = findViewById(R.id.et_contactNo);
        et_propiretor = findViewById(R.id.et_propiretor);
        address = findViewById(R.id.address);
        et_gstn = findViewById(R.id.et_gstn);
        et_pancard = findViewById(R.id.et_pancard);
        adharNo = findViewById(R.id.adharNo);
        et_email = findViewById(R.id.et_email);
        img_aadhar = findViewById(R.id.img_aadhar);
        img_gst = findViewById(R.id.img_gst);
        img_pan = findViewById(R.id.img_pan);
        btn_submit = findViewById(R.id.btn_submit);
        spn_State = findViewById(R.id.spn_State);
        spn_District = findViewById(R.id.spn_District);
        categoryHM=new HashMap<>();
        categoryAl=new ArrayList<>();
        categoryValueAl=new ArrayList<>();
        vendorRegModals=new ArrayList<>();
        stateNameHM=new HashMap<>();
        districtNameHM=new HashMap<>();
        stateArrayList=new ArrayList<>();
        distrcitArrayList=new ArrayList<>();
    }

    private void ButtonListener() {
        findViewById(R.id.gstn_upload_img).setOnClickListener(v -> {
            isGSTImageCondition = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (uiHelper.checkSelfPermissions(this))
                    uiHelper.showImagePickerDialog(this, this);
        });
        findViewById(R.id.pan_upload_img).setOnClickListener(v -> {
            isPanCardImageCondition = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (uiHelper.checkSelfPermissions(this))
                    uiHelper.showImagePickerDialog(this, this);
        });
        findViewById(R.id.aadhar_upload_img).setOnClickListener(v -> {
            isAadharCardImageCondition = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (uiHelper.checkSelfPermissions(this))
                    uiHelper.showImagePickerDialog(this, this);
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    registrationPojo.setName(et_companyName.getText().toString().trim());
                    registrationPojo.setMobile(et_contactNo.getText().toString().trim());
                    registrationPojo.setProprietor_no(et_propiretor.getText().toString().trim());
                    registrationPojo.setAddress(address.getText().toString().trim());
                    registrationPojo.setGstn_no(et_gstn.getText().toString().trim());
                    registrationPojo.setPan_no(et_pancard.getText().toString().trim());
                    registrationPojo.setAadhar_no(adharNo.getText().toString().trim());

                    String ids = "";
                    List<KeyPairBoolData> dds3 = spn_vendor.getSelectedItems();
                    for (int i = 0; i < dds3.size(); i++) {
                        String name = dds3.get(i).getName();
                        if (i == 0) {
                            ids = String.valueOf(categoryHM.get(name));
                        } else if (ids != null) {
                            ids = ids + "," + String.valueOf(categoryHM.get(name));
                        }
                    }
                    registrationPojo.setVender_category(String.valueOf(ids));
                    registrationPojo.setGstn_image(base64);
                    registrationPojo.setPan_image(base64PanCard);
                    registrationPojo.setAadhar_image(base64AadharCard);
                    registrationPojo.setState_id(String.valueOf(state_id));
                    registrationPojo.setDistrict_id(String.valueOf(district_id));
                    registrationPojo.setEmail(et_email.getText().toString().trim());

                    //save in db
                    long inserted_id = sqliteHelper.saveVendorRegistrationData(registrationPojo);
                    //get table last inserted id
                    int idss = sqliteHelper.getLastInsertedLocalIdForVendor();

                    try {
                        if (isInternetOn() && idss > 0) {
                            vendorRegModals = sqliteHelper.getVendorRegistrationDataToBeSync();
                            if (vendorRegModals.size() > 0) {
                                for (int i = 0; i < vendorRegModals.size(); i++) {
                                    vendorRegModals.get(i).setFirst_name("");
                                    vendorRegModals.get(i).setLast_name("");
                                    vendorRegModals.get(i).setProfile_photo("");

                                    Gson gson = new Gson();
                                    String data = gson.toJson(vendorRegModals.get(i));
                                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                    RequestBody body = RequestBody.create(JSON, data);

                                    vendorRegistration(body, idss);
                                }
                            }
                        } else {
                            Toast.makeText(VendorRegistration.this,
                                    "Network Error, please check your network connection.", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }



    private boolean checkValidation() {
        boolean ret = true;
        if (!et_companyName.getText().toString().trim().matches("[a-zA-Z ]+")) {
            EditText flagEditfield = et_companyName;
            String msg = getString(R.string.Please_Enter_Company_Name);
            et_companyName.setError(msg);
            et_companyName.requestFocus();
            return false;
        }

        if (et_contactNo.getText().toString().trim().length() < 10) {
            EditText flagEditfield = et_contactNo;
            String msg = getString(R.string.Please_Enter_Valid_Contact_Number);
            et_contactNo.setError(msg);
            et_contactNo.requestFocus();
            return false;
        }
        if (et_propiretor.getText().toString().trim().length() < 4) {
            EditText flagEditfield = et_propiretor;
            String msg = getString(R.string.Please_Enter_Valid_Proprietor);
            et_propiretor.setError(msg);
            et_propiretor.requestFocus();
            return false;
        }

        if (address.getText().toString().trim().length() < 4) {
            EditText flagEditfield = address;
            String msg = getString(R.string.Please_Enter_Valid_Address_Number);
            address.setError(msg);
            address.requestFocus();
            return false;
        }
        if (et_email.getText().toString().trim().equals("")) {
            EditText flagEditfield = et_email;
            String msg = getString(R.string.please_enter_email);
            et_email.setError(msg);
            et_email.requestFocus();
            return false;
        }

        if (spn_State.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(spn_State.getSelectedItem());
        } else {
            android.widget.TextView errorTextview = (android.widget.TextView) spn_State.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }

        if (spn_District.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(spn_District.getSelectedItem());
        } else {
            android.widget.TextView errorTextview = (android.widget.TextView) spn_District.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }

        if (spn_vendor.getSelectedIds().size() == 0) {
            android.widget.TextView errorTextview = (android.widget.TextView) spn_vendor.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }
        if (et_gstn.getText().toString().trim().length() < 10) {
            EditText flagEditfield = et_gstn;
            String msg = getString(R.string.Please_Enter_Valid_GSTN_Number);
            et_gstn.setError(msg);
            et_gstn.requestFocus();
            return false;
        }
        if (et_pancard.getText().toString().trim().length() < 10) {
            EditText flagEditfield = et_pancard;
            String msg = getString(R.string.Please_Enter_Valid_Pan_card_Number);
            et_pancard.setError(msg);
            et_pancard.requestFocus();
            return false;
        }
        if (adharNo.getText().toString().trim().length() < 12) {
            EditText flagEditfield = adharNo;
            String msg = getString(R.string.Please_Enter_Valid_Aadhar_Number);
            adharNo.setError(msg);
            adharNo.requestFocus();
            return false;
        }
        return ret;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
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
            Intent intentHome = new Intent(context, LoginScreenActivity.class);
            startActivity(intentHome);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionSelected(ImagePickerEnum imagePickerEnum) {
        if (imagePickerEnum == ImagePickerEnum.FROM_CAMERA)
            openCamera();
        else if (imagePickerEnum == ImagePickerEnum.FROM_GALLERY)
            openImagesDocument();
    }

    private void openCamera() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file;
        try {
            file = getImageFile(); // 1
        } catch (Exception e) {
            e.printStackTrace();
            uiHelper.toast(this, "Please take another image");
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
                uiHelper.toast(this, "ImageCropper needs Storage access in order to store your profile picture.");
                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                uiHelper.toast(this, "ImageCropper needs Camera access in order to take profile picture.");
                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                uiHelper.toast(this, "ImageCropper needs Camera and Storage access in order to take profile picture.");
                finish();
            }
        } else if (requestCode == ONLY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else {
                uiHelper.toast(this, "ImageCropper needs Camera access in order to take profile picture.");
                finish();
            }
        } else if (requestCode == ONLY_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else {
                uiHelper.toast(this, "ImageCropper needs Storage access in order to store your profile picture.");
                finish();
            }
        }
    }

    private void showImage(Uri imageUri) {
        try {
            if (isGSTImageCondition) {
                File file;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    file = FileUtils.getFile(this, imageUri);
                } else {
                    file = new File(currentPhotoPath);
                }
                InputStream inputStream = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img_gst.setImageBitmap(bitmap); //set to show image

                bitmap = getResizedBitmap(bitmap, 300); //get resize image
                base64 = getStringImage(bitmap); //get base 64 image file.
                Log.e("image", base64);
                isGSTImageCondition = false;
            }
            if (isPanCardImageCondition) {
                File file;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    file = FileUtils.getFile(this, imageUri);
                } else {
                    file = new File(currentPhotoPathPanCard);
                }
                InputStream inputStream = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img_pan.setImageBitmap(bitmap); //set to show image

                bitmap = getResizedBitmap(bitmap, 300); //get resize image
                base64PanCard = getStringImage(bitmap); //get base 64 image file.
                Log.e("base64PanCard", base64PanCard);
                isPanCardImageCondition = false;
            }
            if (isAadharCardImageCondition) {
                File file;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    file = FileUtils.getFile(this, imageUri);
                } else {
                    file = new File(currentPhotoPathAadharCard);
                }
                InputStream inputStream = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img_aadhar.setImageBitmap(bitmap); //set to show image

                bitmap = getResizedBitmap(bitmap, 300); //get resize image
                base64AadharCard = getStringImage(bitmap); //get base 64 image file.
                Log.e("base64AadharCard", base64AadharCard);
                isAadharCardImageCondition = false;
            }
        } catch (Exception e) {
            uiHelper.toast(this, "Please select different profile picture.");
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
        startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), PICK_IMAGE_GALLERY_REQUEST_CODE);
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
            image.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOS);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            image.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOS);
        }
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                    uiHelper.toast(this, "Please select another image");
                }
            }
        }
    }

    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.colorAccent));
        UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(500, 500)
                .withAspectRatio(5f, 5f)
                .start(this);
    }

    private void vendorRegistration(RequestBody body, int local_id) {
        ProgressDialog dialog = ProgressDialog.show(VendorRegistration.this, "", "Please wait...", true);

        APIClient.getClient().create(JubiForm_API.class).supplier_registration(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("jbcjh", "mcjcjcd " + jsonObject.toString());
                    String message = jsonObject.optString("message");
                    String user_id = jsonObject.optString("user_id");
                    String supplier_id = jsonObject.optString("supplier_id");
                    sharedPrefHelper.setString("user_id", user_id);
                    String status = jsonObject.optString("status");
                    if (status.equals("1")) {
//                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                        //update flag in tables
                        sqliteHelper.updateId("supplier_registration", "user_id", Integer.parseInt(user_id), local_id, "id");
                        sqliteHelper.updateId("supplier_registration", "id", Integer.parseInt(supplier_id), Integer.parseInt(user_id), "user_id");
                        int idsss = sqliteHelper.getLastInsertedLocalIdForVendor();
                        sqliteHelper.updateFlag("farmer_registration", idsss, 1);

                        Intent intentSupplierOtp = new Intent(VendorRegistration.this, OtpScreen.class);
                        intentSupplierOtp.putExtra("Mobile", registrationPojo.getMobile());
                        startActivity(intentSupplierOtp);
                        finish();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
            }
        });
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
}