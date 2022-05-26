package com.sanket.jubifarm.Activity;

import android.app.DatePickerDialog;
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
import android.text.TextWatcher;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sanket.jubifarm.Adapter.AddCropSalesAdapter;
import com.sanket.jubifarm.BuildConfig;
import com.sanket.jubifarm.Modal.PlantGrowthModal;
import com.sanket.jubifarm.Modal.PlantGrowthPojo;
import com.sanket.jubifarm.Modal.VisitPlantModel;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.cropImage.enums.ImagePickerEnum;
import com.sanket.jubifarm.cropImage.listeners.IImagePickerLister;
import com.sanket.jubifarm.cropImage.utils.FileUtils;
import com.sanket.jubifarm.cropImage.utils.UiHelper;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.sanket.jubifarm.utils.CommonClass;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPlantGrowthActivity extends AppCompatActivity implements IImagePickerLister {
    Button tvplantgrowth, btn_imageadd;
    EditText date, et_remarks,et_healthy,et_unhealthy,et_dead;
    Spinner spnCropStatus, grow_Crop_selection;
    CircleImageView img_addimage;
    LinearLayout ll_healthy;
    int CropStatus=0;
    int cropselection;
    PlantGrowthPojo plantGrowthPojo;
    SqliteHelper sqliteHelper;
    DatePickerDialog datePickerDialog;
    ArrayList<String> cropCategoryList = new ArrayList<>();
    ArrayList<String> cropstatusList = new ArrayList<>();
    ArrayList<PlantGrowthPojo> PlantGrowthModalList = new ArrayList<>();
    HashMap<String, Integer> cropCategoryHM;
    HashMap<String, Integer> croplistStatusHM;
    PlantGrowthModal plantGrowthModal;
    SharedPrefHelper sharedPrefHelper;
    private Context context = this;

    private static final int CAMERA_ACTION_PICK_REQUEST_CODE = 610;
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    public static final int CAMERA_STORAGE_REQUEST_CODE = 611;
    public static final int ONLY_CAMERA_REQUEST_CODE = 612;
    public static final int ONLY_STORAGE_REQUEST_CODE = 613;
    private String currentPhotoPath = "";
    private UiHelper uiHelper = new UiHelper();
    private String base64 = "";
    String plant_id;
    String farmer_id = "";
    String total_tree = "";
    String crop_planing_id = "";
    String crop_type_catagory_id = "";
    String crop_type_subcatagory_id = "";
    ProgressDialog dialog;
    public static android.app.Dialog submit_alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant_growth);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.Add_Plant_growth)  + "</font>"));

        tvplantgrowth = findViewById(R.id.tvplantgrowth);
        sharedPrefHelper=new SharedPrefHelper(this);
        grow_Crop_selection = findViewById(R.id.grow_Crop_selection);
        spnCropStatus = findViewById(R.id.spnCropStatus);
        btn_imageadd = findViewById(R.id.btn_imageadd);
        date = findViewById(R.id.date);
        ll_healthy = findViewById(R.id.ll_healthy);
        et_remarks = findViewById(R.id.et_remarks);
        et_healthy = findViewById(R.id.et_healthy);
        et_unhealthy = findViewById(R.id.et_unhealthy);
        et_dead = findViewById(R.id.et_dead);
        img_addimage = findViewById(R.id.img_addimage);
        sqliteHelper = new SqliteHelper(this);
        plantGrowthPojo = new PlantGrowthPojo();
        plantGrowthModal = new PlantGrowthModal();
        dialog=new ProgressDialog(this);
        getAllCategoryList();
        getgrowthStatus();

        /*get intent values here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            farmer_id = bundle.getString("farmer_id", "");
            crop_planing_id = bundle.getString("crop_planing_id", "");
            crop_type_catagory_id = bundle.getString("crop_type_catagory_id", "");
            crop_type_subcatagory_id = bundle.getString("crop_type_subcatagory_id", "");
            total_tree = bundle.getString("total_tree", "");
            et_healthy.setText(total_tree);
            if(crop_type_catagory_id.equals("4") || crop_type_catagory_id.equals("6")){
                ll_healthy.setVisibility(View.VISIBLE);
            }else {
                ll_healthy.setVisibility(View.GONE);
            }
        }


       /* et_unhealthy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!total_tree.equals("") && !et_unhealthy.getText().toString().equals("")) {
                    int num = Integer.parseInt(et_unhealthy.getText().toString());
                    String numw = String.valueOf(Integer.parseInt(total_tree) - num);
                    total_tree=numw;
                    et_healthy.setText(numw);
                }
            }
        });
        et_dead.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!total_tree.equals("") && !et_dead.getText().toString().equals("")) {
                    int num = Integer.parseInt(et_dead.getText().toString());
                    String numw = String.valueOf(Integer.parseInt(total_tree) - num);
                    total_tree=numw;
                    et_healthy.setText(numw);
                }
            }
        });
*/

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateToStr = format.format(today);
        date.setText(dateToStr);
        /*date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(AddPlantGrowthActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });*/


        findViewById(R.id.btn_imageadd).setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (uiHelper.checkSelfPermissions(this))
                    openCamera();
                  //  uiHelper.showImagePickerDialog(this, this);
        });

        tvplantgrowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int num=0;
                int num2=0;

                if(num==0 && num2==0){


                    setSendData(num, num2);

                }else{

                    num= Integer.parseInt(et_unhealthy.getText().toString());
                    num2= Integer.parseInt(et_dead.getText().toString());

                    if (et_unhealthy.getText().toString().equals("")){
                        et_unhealthy.setError("required");
                        return;
                    }
                    if (et_dead.getText().toString().equals("")){
                        et_dead.setError("required");
                        return;
                    }


                    setSendData(num, num2);

                }







                /*if (isInternetOn()) {
                    PlantGrowthModalList = sqliteHelper.getPlantgrwthListForSync();
                    if (PlantGrowthModalList.size()>0) {

                            Gson gsonn = new Gson();
                            String element = gsonn.toJson(
                                    PlantGrowthModalList,
                                    new TypeToken<ArrayList<VisitPlantModel>>() {
                                    }.getType());
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(element);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JSONObject jsonobj = new JSONObject();
                            try {
                                jsonobj.put("plant_growth", jsonArray);

                                Gson gson = new Gson();
                                String data = jsonobj.toString(); //gson.toJson(jsonobj);
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                RequestBody body = RequestBody.create(JSON, data);
                                send_growth_plantData(body);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                 else {*/

                /*}*/
            }
        });
    }

    private void setSendData(int num, int num2) {

        if (num+num2>Integer.parseInt(total_tree)){
            Toast.makeText(AddPlantGrowthActivity.this, "Dead and Unhealthy Plant total can not be greater than Total healthy plants", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Integer.parseInt(total_tree)-num+num2<0){
            Toast.makeText(AddPlantGrowthActivity.this, "Dead and Unhealthy Plant total can not be less than zero", Toast.LENGTH_SHORT).show();
            return;

        }

        plantGrowthModal.setGrowth_date(date.getText().toString());
        plantGrowthModal.setRemarks(et_remarks.getText().toString());
        plantGrowthModal.setHealthy_plants(String.valueOf(Integer.parseInt(total_tree)-num+num2));
        plantGrowthModal.setUnhealthy_plants(et_unhealthy.getText().toString());
        plantGrowthModal.setDead_plants(et_dead.getText().toString());
        plantGrowthModal.setCrop_planing_id(crop_planing_id);
        plantGrowthModal.setCrop_status_id(String.valueOf(CropStatus));
        plantGrowthModal.setUser_id(sharedPrefHelper.getString("user_id", ""));
        plantGrowthModal.setFarmer_id(farmer_id);
        plantGrowthModal.setPlant_image(base64);
        plantGrowthModal.setCrop_type_catagory_id(crop_type_catagory_id);
        plantGrowthModal.setCrop_type_subcatagory_id(crop_type_subcatagory_id);
        plantGrowthModal.setId(CommonClass.getUUID());
        sqliteHelper.getAddplantgrowth(plantGrowthModal);
        sqliteHelper.updateTotalTree("crop_planning", crop_planing_id,Integer.parseInt(total_tree)-num+num2);

//        Toast.makeText(AddPlantGrowthActivity.this, R.string.no_internet_data_saved_locally, Toast.LENGTH_LONG).show();
        showSubmitDialog(context,"Data has been saved in local database successfully.");
//        Intent homeIntent = new Intent(AddPlantGrowthActivity.this, PlantGrowthListActivity.class);
//        startActivity(homeIntent);
//        finish();

    }
    public static void showSubmitDialog(Context context, String message) {
        submit_alert = new android.app.Dialog(context);

        submit_alert.setContentView(R.layout.submitdialog);
        submit_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = submit_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        android.widget.TextView tvDescription = (TextView) submit_alert.findViewById(R.id.tv_description);
        Button btnOk = (Button) submit_alert.findViewById(R.id.btnOk);

        tvDescription.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO
                submit_alert.dismiss();
                Intent intent = new Intent(context, CropMonitoring.class);
                context.startActivity(intent);
            }
        });

        submit_alert.show();
        submit_alert.setCanceledOnTouchOutside(false);
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
        Intent intentHome = new Intent(AddPlantGrowthActivity.this, SubMonitoring.class);
        startActivity(intentHome);
        finish();
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

    private void getAllCategoryList() {
        cropCategoryList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(AddPlantGrowthActivity.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            cropCategoryHM = sqliteHelper.getAllCategoryType(2);
        }
        else
        {
            cropCategoryHM = sqliteHelper.getAllCategoryType(1);
        }

        for (int i = 0; i < cropCategoryHM.size(); i++) {
            cropCategoryList.add(cropCategoryHM.keySet().toArray()[i].toString().trim());
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, cropCategoryList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grow_Crop_selection.setAdapter(Adapter);
        grow_Crop_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!grow_Crop_selection.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_farmer))) {
                    if (grow_Crop_selection.getSelectedItem().toString().trim() != null) {
                        cropselection = cropCategoryHM.get(grow_Crop_selection.getSelectedItem().toString().trim());
                        Log.e("=============", "==========" + cropselection);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getgrowthStatus() {
        cropstatusList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(AddPlantGrowthActivity.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            croplistStatusHM = sqliteHelper.getPlantGrowth_Status(6, 2);
        }
        else
        {
            croplistStatusHM = sqliteHelper.getPlantGrowth_Status(6, 1);
        }
        for (int i = 0; i < croplistStatusHM.size(); i++) {
            cropstatusList.add(croplistStatusHM.keySet().toArray()[i].toString().trim());
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, cropstatusList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCropStatus.setAdapter(Adapter);
        spnCropStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spnCropStatus.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_farmer))) {
                    if (spnCropStatus.getSelectedItem().toString().trim() != null) {
                        CropStatus = croplistStatusHM.get(spnCropStatus.getSelectedItem().toString().trim());
                        Log.e("=============", "==========" + CropStatus);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    public void send_growth_plantData(RequestBody body) {
         dialog = ProgressDialog.show(this, "", getString(R.string.Please_wait), true);
         APIClient.getClient().create(JubiForm_API.class).plant_growth(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("subp", "plant_growth===" + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    if (status.equals("1")) {
                        sqliteHelper.updateVisitPlantFlag("plant_growth", 0, 1);
                       // String sub_id = jsonObject.optString("growth_id");
                       // sqliteHelper.updateServerid("plant_growth", Integer.parseInt(local_id), Integer.parseInt(sub_id));
                        Intent intent = new Intent(AddPlantGrowthActivity.this, PlantGrowthListActivity.class);
                        intent.putExtra("id",crop_planing_id);
                        startActivity(intent);
                        finish();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
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
                uiHelper.toast(this, getString(R.string.ImageCropper3));
                finish();
            }
        } else if (requestCode == ONLY_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else {
                uiHelper.toast(this, getString(R.string.ImageCropper1));
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
        startActivityForResult(Intent.createChooser(pictureIntent,getString(R.string.Select_Picture)), PICK_IMAGE_GALLERY_REQUEST_CODE);
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
            img_addimage.setImageBitmap(bitmap); //set to show image

            bitmap = getResizedBitmap(bitmap, 300); //get resize image
            base64 = getStringImage(bitmap); //get base 64 image file.
            Log.e("image", base64);
        } catch (Exception e) {
            uiHelper.toast(this, "Please select different profile picture.");
        }
    }
}