package com.sanket.jubifarm.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Adapter.AddCropSalesAdapter;
import com.sanket.jubifarm.Modal.SaleDetailsPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.sanket.jubifarm.utils.CommonClass;
import com.sayantan.advancedspinner.MultiSpinner;
import com.sayantan.advancedspinner.MultiSpinnerListener;
import com.sayantan.advancedspinner.SingleSpinner;
import com.sayantan.advancedspinner.SpinnerListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCropActivity extends AppCompatActivity {
    MultiSpinner spn_plant;
    SingleSpinner spn_farmer;
    Button btn_submit;
    Button btn_submit_all;
    RecyclerView rv_crop_sales;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    LinearLayout ll_farmer, ll_farmer_selection;
    private ArrayList<String> farmarArrayList = new ArrayList<>();
    private ArrayList<String> plantArrayList = new ArrayList<>();
    ArrayList<SaleDetailsPojo> saleDetailsPojosAL = new ArrayList<>();
    private HashMap<String, Integer> farmarNameHM;
    private HashMap<String, Integer> plantNameHM;
    boolean isEditable = false;
    ProgressDialog dialog;
    private String Select_Farmer;
    private String Select_Plant;
    private String fromSalesDetials;
    SaleDetailsPojo saleDetailsPojo;
    SaleDetailsPojo saleDetailfromAdapter;
    AddCropSalesAdapter addCropSalesAdapter;
    private HashMap<String, String> selectedVlauesHM;
    private ArrayList<SaleDetailsPojo> saleDetailsPojoAl;
    private long insertedId = 0;
    int farmer_id = 0;
    String selected_farmer;
    List<ContentValues> listValues;
    private Context context = this;
    TextView tv_farmer;
    String unique_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_crop);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.select_crop) + "</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        spn_farmer = findViewById(R.id.spn_farmer);
        spn_plant = findViewById(R.id.spn_plant);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit_all = findViewById(R.id.btn_submit_all);
        rv_crop_sales = findViewById(R.id.rv_crop_sales);
        tv_farmer = findViewById(R.id.tv_farmer);
        ll_farmer = findViewById(R.id.ll_farmer);
        ll_farmer_selection = findViewById(R.id.ll_farmer_selection);
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        dialog = new ProgressDialog(this);
        farmarNameHM = new HashMap<>();
        plantNameHM = new HashMap<>();
        saleDetailsPojo = new SaleDetailsPojo();
        selectedVlauesHM = new HashMap<>();
        saleDetailsPojoAl = new ArrayList<>();
        listValues = new ArrayList<>();
        Long tsLong = System.currentTimeMillis()/1000;
        unique_id = tsLong.toString();

        selected_farmer = sharedPrefHelper.getString("selected_farmer", "");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fromSalesDetials = bundle.getString("fromSalesDetials", "");
            if (fromSalesDetials.equals("1")) {
                setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.crop_production_selected) + "</font>"));

            } else {
                setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.crop_sale_selected)  + "</font>"));


            }
        }
        if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
            if (selected_farmer.equals("")) {
                ll_farmer.setVisibility(View.VISIBLE);
                ll_farmer_selection.setVisibility(View.GONE);

                setFarmerSpinner();
            } else {
                ll_farmer_selection.setVisibility(View.VISIBLE);
                tv_farmer.setText(sqliteHelper.getFarmerName(selected_farmer));
                ll_farmer.setVisibility(View.GONE);
                getPlantFromTable(Integer.parseInt(selected_farmer));
            }
        } else {
            setPlantSpinner();
            ll_farmer.setVisibility(View.INVISIBLE);
        }

        btn_submit_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonClass.isInternetOn(context)) {
                    int count=0;
                    if (fromSalesDetials.equals("2")){
                        count=sqliteHelper.getSalesCount("sale_details",unique_id);

                    }else {
                        count=sqliteHelper.getSalesCount("production_details",unique_id);

                    }
                    if (count!=addCropSalesAdapter.getItemCount()){
                        Toast.makeText(context, "Please save data first", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    saleDetailsPojosAL = sqliteHelper.getCropListForSync(fromSalesDetials);
                        for (int i = 0; i < saleDetailsPojosAL.size(); i++) {
                            saleDetailsPojosAL.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));
                            Gson gson = new Gson();
                            String data = gson.toJson(saleDetailsPojosAL.get(i));
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(JSON, data);
                            if (fromSalesDetials.equals("2")) {
                                sendCropSalesData(body, Integer.parseInt(saleDetailsPojosAL.get(i).getLocal_id()), i);
                            } else {
                                sendCropDetailsData(body, Integer.parseInt(saleDetailsPojosAL.get(i).getLocal_id()), i);
                            }
                        }
                } else {
                    Toast.makeText(SelectCropActivity.this, R.string.no_internet_data_saved_locally, Toast.LENGTH_LONG).show();
                    Intent homeIntent = new Intent(SelectCropActivity.this, CropSaleDetails.class);
                    startActivity(homeIntent);
                    finish();                }
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get list data here
                SaleDetailsPojo saleDetailsPojo = new SaleDetailsPojo();
                /*selectedVlauesHM.clear();
                selectedVlauesHM = addCropSalesAdapter.getAllDataHM();*/
                /*if (selectedVlauesHM.size() > 0) {
                    ArrayList<String> cropTypeCategoryIdValues = new ArrayList<>();
                    ArrayList<String> cropTypeSubCategoryIdValues = new ArrayList<>();
                    ArrayList<String> priceValues = new ArrayList<>();
                    ArrayList<String> yearValues = new ArrayList<>();
                    ArrayList<String> farmerIdValues = new ArrayList<>();
                    ArrayList<String> userIdValues = new ArrayList<>();
                    ArrayList<String> qtyValues = new ArrayList<>();
                    ArrayList<String> seasonIdValues = new ArrayList<>();
                    for (int i = 0; i < selectedVlauesHM.size(); i++) {
                        cropTypeCategoryIdValues.add(selectedVlauesHM.values().toArray()[i].toString().trim());
                        cropTypeSubCategoryIdValues.add(selectedVlauesHM.values().toArray()[i].toString().trim());
                        priceValues.add(selectedVlauesHM.values().toArray()[i].toString().trim());
                        yearValues.add(selectedVlauesHM.values().toArray()[i].toString().trim());
                        farmerIdValues.add(selectedVlauesHM.values().toArray()[i].toString().trim());
                        userIdValues.add(selectedVlauesHM.values().toArray()[i].toString().trim());
                        qtyValues.add(selectedVlauesHM.values().toArray()[i].toString().trim());
                        seasonIdValues.add(selectedVlauesHM.values().toArray()[i].toString().trim());
                    }
                    for (int i = 0; i < seasonIdValues.size(); i++) {
                        saleDetailsPojo.setCrop_type_catagory_id(String.valueOf(cropTypeCategoryIdValues.get(i)));
                        saleDetailsPojo.setCrop_type_subcatagory_id(String.valueOf(cropTypeSubCategoryIdValues.get(i)));
                        saleDetailsPojo.setCrop_type_price(String.valueOf(priceValues.get(i)));
                        saleDetailsPojo.setYear(String.valueOf(yearValues.get(i)));
                        saleDetailsPojo.setFarmer_id(String.valueOf(farmerIdValues.get(i)));
                        saleDetailsPojo.setUser_id(String.valueOf(userIdValues.get(i)));
                        saleDetailsPojo.setQuantity(String.valueOf(qtyValues.get(i)));
                        saleDetailsPojo.setSeason_id(String.valueOf(seasonIdValues.get(i)));
                    }
                }*/
                /*for (int i = 0; i < listValues.size(); i++) {
                    saleDetailsPojo.setCrop_type_catagory_id("");
                    saleDetailsPojo.setCrop_type_subcatagory_id("");
                    saleDetailsPojo.setCrop_type_price(listValues.get(i).get("price").toString());
                    saleDetailsPojo.setYear(listValues.get(i).get("current_year").toString());
                    saleDetailsPojo.setFarmer_id("");
                    saleDetailsPojo.setUser_id(sharedPrefHelper.getString("user_id", ""));
                    saleDetailsPojo.setQuantity(listValues.get(i).get("quantity").toString());
                    saleDetailsPojo.setSeason_id("");

                    sqliteHelper.getAddseleDetailData(saleDetailsPojo);
                    Toast.makeText(SelectCropActivity.this, "Data saved", Toast.LENGTH_SHORT).show();
                }*/

                /*if (isInternetOn()) {
                    saleDetailsPojosAL = sqliteHelper.getCropListForSync();
                    if (saleDetailsPojosAL.size() > 0) {
                        for (int i = 0; i < saleDetailsPojosAL.size(); i++) {
                            saleDetailsPojosAL.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));

                            Gson gson = new Gson();
                            String data = gson.toJson(saleDetailsPojosAL.get(i));
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(JSON, data);
                            sendCropDetailsData(body, Integer.parseInt(saleDetailsPojosAL.get(i).getLocal_id()));
                        }
                    }
                } else {
                    Intent Regintent = new Intent(SelectCropActivity.this, CropSaleDetails.class);
                    startActivity(Regintent);
                    finish();
                }*/

       /* Intent intent=new Intent(SelectCropActivity.this,CropDetalsInPut.class);
                intent.putExtra("fromSalesDetials",fromSalesDetials);
                intent.putExtra("selected_plant",Select_Plant);
                intent.putExtra("selected_farmer",Select_Farmer);
                startActivity(intent);
                finish();*/
            }
        });

    }

    private void sendCropSalesData(RequestBody body, int local_id, int i) {
        ProgressDialog dialog = ProgressDialog.show(context, "", context.getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).add_sale_details(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        sqliteHelper.updateAddPlantFlag("sale_details", local_id, 1);
                        String plant_id = jsonObject.optString("sale_details_id");
                        sqliteHelper.updateServerid("sale_details", local_id, Integer.parseInt(plant_id));
                        if (i + 1 == saleDetailsPojosAL.size()) {
                            Intent intent = new Intent(SelectCropActivity.this, CropSaleDetails.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
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

    private void sendCropDetailsData(RequestBody body, int local_id, int i) {
        ProgressDialog dialog = ProgressDialog.show(context, "", context.getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).add_production_details(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        sqliteHelper.updateAddPlantFlag("production_details", local_id, 1);
                        String plant_id = jsonObject.optString("production_id");
                        sqliteHelper.updateServerid("production_details", local_id, Integer.parseInt(plant_id));
                        if (i + 1 == saleDetailsPojosAL.size()) {
                            Intent intent = new Intent(SelectCropActivity.this, CropSaleDetails.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
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

    private void setFarmerSpinner() {
        farmarArrayList.clear();
        farmarNameHM = sqliteHelper.getFarmerspinner();

        for (int i = 0; i < farmarNameHM.size(); i++) {
            farmarArrayList.add(farmarNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            farmarArrayList.add(0, Select_Farmer);
        } else {
            //farmarArrayList.add(0, getString(R.string.select_farmer));
        }
        spn_farmer.setSpinnerList(farmarArrayList);
        spn_farmer.addOnItemChoosenListener(new SpinnerListener() {
            @Override
            public void onItemChoosen(String s, int i) {
                farmer_id = farmarNameHM.get(s);
                Select_Farmer = String.valueOf(farmer_id);
                saleDetailsPojo.setFarmer_id(Select_Farmer);
                getPlantFromTable(farmer_id);
            }
        });
    }

    private void setPlantSpinner() {
        plantArrayList.clear();
        plantNameHM = sqliteHelper.getPlantSpinner("");

        for (int i = 0; i < plantNameHM.size(); i++) {
            plantArrayList.add(plantNameHM.keySet().toArray()[i].toString().trim());
        }

        spn_plant.setSpinnerList(plantArrayList);
        spn_plant.addOnItemsSelectedListener(new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<String> list, boolean[] booleans) {
                List<SaleDetailsPojo> detailsPojosAL = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    SaleDetailsPojo saleDetailsPojo = new SaleDetailsPojo();

                    Select_Plant = String.valueOf(plantNameHM.get(list.get(i)));
                    saleDetailsPojo.setCrop_type_subcatagory_id(Select_Plant);
                    saleDetailsPojo.setCrop_type_catagory_id(sqliteHelper.getCategotyTypeId(Select_Plant));
                    detailsPojosAL.add(saleDetailsPojo);
                    addCropSalesAdapter = new AddCropSalesAdapter(detailsPojosAL, SelectCropActivity.this, fromSalesDetials, farmer_id,unique_id);
                    rv_crop_sales.setHasFixedSize(true);
                    rv_crop_sales.setLayoutManager(new LinearLayoutManager(SelectCropActivity.this));
                    rv_crop_sales.setAdapter(addCropSalesAdapter);

                    /*rv_crop_sales.setItemViewCacheSize(arrayList.size());

                    for (int j = 0; j < arrayList.size(); j++) {
                        View view = rv_crop_sales.getChildAt(i);
                        EditText farmerNameET = (EditText) view.findViewById(R.id.et_farmer_name);
                        EditText currentYearET = (EditText) view.findViewById(R.id.et_year);
                        EditText qtyET = (EditText) view.findViewById(R.id.et_quantity);
                        EditText priseET = (EditText) view.findViewById(R.id.et_price);

                        String currentYear = currentYearET.getText().toString().trim();
                        String farmerName = farmerNameET.getText().toString().trim();
                        String qty = qtyET.getText().toString().trim();
                        String prise = priseET.getText().toString().trim();

                        listValues.clear();
                        ContentValues cv = new ContentValues();
                        cv.put("current_year", currentYear);
                        cv.put("farmer_name", farmerName);
                        cv.put("quantity", qty);
                        cv.put("price", prise);
                        listValues.add(cv);
                    }*/

                }
            }
        });
    }


    private void getPlantFromTable(int farmerId) {
        plantArrayList.clear();
        plantNameHM = sqliteHelper.getPlantSpinner(String.valueOf(farmerId));

        for (int i = 0; i < plantNameHM.size(); i++) {
            plantArrayList.add(plantNameHM.keySet().toArray()[i].toString().trim());
        }

        spn_plant.setSpinnerList(plantArrayList);
        spn_plant.addOnItemsSelectedListener(new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<String> list, boolean[] booleans) {
                List<SaleDetailsPojo> detailsPojosAL = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    SaleDetailsPojo saleDetailsPojo = new SaleDetailsPojo();

                    Select_Plant = String.valueOf(plantNameHM.get(list.get(i)));
                    saleDetailsPojo.setCrop_type_subcatagory_id(Select_Plant);
                    saleDetailsPojo.setCrop_type_catagory_id(sqliteHelper.getCategotyTypeId(Select_Plant));
                    detailsPojosAL.add(saleDetailsPojo);
                    addCropSalesAdapter = new AddCropSalesAdapter(detailsPojosAL, SelectCropActivity.this, fromSalesDetials, farmerId,unique_id);
                    rv_crop_sales.setHasFixedSize(true);
                    rv_crop_sales.setLayoutManager(new LinearLayoutManager(SelectCropActivity.this));
                    rv_crop_sales.setAdapter(addCropSalesAdapter);

                }
            }
        });
    }

    @Override
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
            Intent intentCropSaleDetails = new Intent(context, CropSaleDetails.class);
            startActivity(intentCropSaleDetails);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentCropSaleDetails = new Intent(context, CropSaleDetails.class);
        startActivity(intentCropSaleDetails);
        finish();
    }
}
