package com.sanket.jubifarm.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sanket.jubifarm.Adapter.VisitPlantAdapter;
import com.sanket.jubifarm.Modal.VisitPlantModel;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.interfaces.ClickListener;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.sanket.jubifarm.utils.CommonClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitPlant extends AppCompatActivity {
    private RecyclerView rv_visit_plant;
    private TextView tv_no_data, PlantCount;
    private CheckBox selectAll;
    private ImageView cropplaning_filter;
    private ExtendedFloatingActionButton fab;
    private ImageView imageView;
    /*normal widgets*/
    private Context context = this;
    private SharedPrefHelper sharedPrefHelper;
    private SqliteHelper sqliteHelper;
    private ArrayList<VisitPlantModel> visitPlantModelAL;
    private VisitPlantAdapter visitPlantAdapter;
    private long insertedId = 0;
    private HashMap<Integer, String> selectedVlauesHM;
    private ArrayList<VisitPlantModel> visitPlantModelAl;
    String selected_farmer;
    String land_id = "", farmer_id = "", fromViewLand = "";
    android.app.Dialog alertResult;
    private static final int REQUEST_TAKE_PHOTO = 101;
    String image64="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_plant);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.visit_plant) + "</font>"));
        initViews();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            land_id = bundle.getString("land_id", "");
            farmer_id = bundle.getString("farmer_id", "0");
            fromViewLand = bundle.getString("fromViewLand", "");

        }
        selected_farmer = sharedPrefHelper.getString("selected_farmer", "");
        setVisitPlantAdapter();

        getCurrentDate();
        submitAllListData();

        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(selectAll.isChecked()){
                    visitPlantAdapter.selectAll();
                }
                else {
                    visitPlantAdapter.unselectall();
                }

            }
        });

    }

    private void submitAllListData() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertResult.setContentView(R.layout.visit_remarks);
                alertResult.setCancelable(false);
                alertResult.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
                WindowManager.LayoutParams params = alertResult.getWindow().getAttributes();
                params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
                EditText et_remarks = alertResult.findViewById(R.id.et_remarks);
                imageView = alertResult.findViewById(R.id.image);
                ImageView ivCancelImage = alertResult.findViewById(R.id.ivCancelImage);
                Button btn_submit = alertResult.findViewById(R.id.btn_submit);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dispatchTakePictureIntent();
                    }
                });
                ivCancelImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertResult.dismiss();
                    }
                });
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (CommonClass.isInternetOn(context)) {
                            ArrayList<VisitPlantModel> visitPlantModelA=new ArrayList<>();

                            visitPlantModelA = sqliteHelper.getPlantGrowthData();
                            if (visitPlantModelA.size()>0){

                                for (int i = 0; i < visitPlantModelA.size() ; i++) {
                                    sqliteHelper.updateImageRemarks("plant_growth", Integer.parseInt(visitPlantModelA.get(i).getLocal_id()),et_remarks.getText().toString(),image64);

                                }

                            }


                            visitPlantModelAL = sqliteHelper.getPlantGrowthData();

                            if (visitPlantModelAL.size() > 0) {
                                //JSONArray jsonArray = new JSONArray();
                                Gson gsonn = new Gson();
                                String element = gsonn.toJson(
                                        visitPlantModelAL,
                                        new TypeToken<ArrayList<VisitPlantModel>>() {
                                        }.getType());
                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(element);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                        /*for (int i = 0; i < visitPlantModelAL.size(); i++) {
                            visitPlantModelAL.get(i).setPlant_growth(visitPlantModelAL);
                            Gson gsonn = new Gson();
                            String dataN = gsonn.toJson(visitPlantModelAL.get(i));
                            JSONObject jsonObj=null;
                            try {
                                jsonObj = new JSONObject(dataN);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(jsonObj);

                            String localId=visitPlantModelAL.get(i).getLocal_id();

                        }*/
                                JSONObject jsonobj = new JSONObject();
                                try {
                                    jsonobj.put("plant_growth", jsonArray);

                                    Gson gson = new Gson();
                                    String data = jsonobj.toString(); //gson.toJson(jsonobj);
                                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                    RequestBody body = RequestBody.create(JSON, data);

                                    sendVisitPlantListData(body);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Toast.makeText(context, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertResult.show();

            }
        });
    }

    private void sendVisitPlantListData(RequestBody body) {
        ProgressDialog dialog = ProgressDialog.show(context, "", "Please wait...", true);
        APIClient.getClient().create(JubiForm_API.class).plant_growth(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("hvxh", "hhxhx " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    if (status.equalsIgnoreCase("1")) {
                        //update flag in tables here
                        sqliteHelper.updateVisitPlantFlag("plant_growth", 0, 1);
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, CropMonitoring.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

    }
    String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Camera::>>> ");
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                File destination = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_" + timeStamp + ".jpg");
                image64=bitmapToBase64(bitmap);
                imageView.setImageBitmap(bitmap);
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String imagePath = destination.getAbsolutePath();
                String[] file1 = imagePath.split(""+"/storage/emulated/0/Android/data/com.indev.enterprisemonitoring/files/Pictures/");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void getCurrentDate() {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateToStr = format.format(today);
        sharedPrefHelper.setString("current_date", dateToStr);
    }

    private void setVisitPlantAdapter() {
        visitPlantModelAL = sqliteHelper.getVisitPlantList(selected_farmer,land_id);
        visitPlantAdapter = new VisitPlantAdapter(this, visitPlantModelAL);
        int counter = visitPlantModelAL.size();
        PlantCount.setText(" " + getString(R.string.Crop) + " : " + counter);
        rv_visit_plant.setHasFixedSize(true);
        rv_visit_plant.setLayoutManager(new LinearLayoutManager(this));
        rv_visit_plant.setAdapter(visitPlantAdapter);

        visitPlantAdapter.onCheckBoxClick(new ClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onViewLandClick(int position) {

            }

            @Override
            public void onEditLandClick(int position) {

            }

            @Override
            public void onCheckBoxClick(int position) {
                //for (int i = 0; i < visitPlantModelAL.size(); i++) {
                VisitPlantModel visitPlantModel = new VisitPlantModel();
                visitPlantModel.setCrop_planing_id(visitPlantModelAL.get(position).getId());
                visitPlantModel.setCrop_status_id("24"); //for live plant id
                visitPlantModel.setGrowth_date(sharedPrefHelper.getString("current_date", ""));
                visitPlantModel.setRemarks("");
                visitPlantModel.setFarmer_id(visitPlantModelAL.get(position).getFarmer_id());
                visitPlantModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
                visitPlantModel.setCrop_type_category_id(visitPlantModelAL.get(position).getCrop_type_catagory_id());
                visitPlantModel.setCrop_type_subcategory_id(visitPlantModelAL.get(position).getCrop_type_subcatagory_id());
                visitPlantModel.setPlant_image(visitPlantModelAL.get(position).getPlant_image());
                visitPlantModel.setPlant_id(visitPlantModelAL.get(position).getPlant_id());

                insertedId = sqliteHelper.sendVisitPlantDataInDB(visitPlantModel);
                if (insertedId > 0) {
                    //Toast.makeText(context, "Data inserted successfully.", Toast.LENGTH_SHORT).show();
                }
                //}
            }


        });
    }

    private void initViews() {
        rv_visit_plant = findViewById(R.id.rv_visit_plant);
        tv_no_data = findViewById(R.id.tv_no_data);
        selectAll = findViewById(R.id.selectAll);
        PlantCount = findViewById(R.id.PlantCount);
        cropplaning_filter = findViewById(R.id.cropplaning_filter);
        fab = findViewById(R.id.fab);
        alertResult = new Dialog(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);
        visitPlantModelAL = new ArrayList<>();
        selectedVlauesHM = new HashMap<>();
        visitPlantModelAl = new ArrayList<>();

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
            if (fromViewLand.equals("")) {
                Intent intentCropMonitoring = new Intent(context, CropMonitoring.class);
                startActivity(intentCropMonitoring);
                finish();
            }else {
                Intent intentCropMonitoring = new Intent(context, ViewLandActivity.class);
                intentCropMonitoring.putExtra("land_id_id",land_id);
                intentCropMonitoring.putExtra("farmer_id",farmer_id);
                startActivity(intentCropMonitoring);
                finish();
            }
            // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (fromViewLand.equals("")) {
            Intent intentCropMonitoring = new Intent(context, CropMonitoring.class);
            startActivity(intentCropMonitoring);
            finish();
        }else {
            Intent intentCropMonitoring = new Intent(context, ViewLandActivity.class);
            intentCropMonitoring.putExtra("land_id_id",land_id);
            intentCropMonitoring.putExtra("farmer_id",farmer_id);
            startActivity(intentCropMonitoring);
            finish();
        }
    }
}
