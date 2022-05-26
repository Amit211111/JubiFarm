package com.sanket.jubifarm.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Modal.ProductionDetailsPojo;
import com.sanket.jubifarm.Modal.SaleDetailsPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CropDetalsInPut extends AppCompatActivity {
    Spinner spin_season, spin_crop, spin_crop_category, spin_farmer,spin_quantity_unit;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    EditText et_price, et_quantity, et_year;
    Button btn_submit;
    LinearLayout ll_farmer;
    LinearLayout ll_quantity_unit;
    ArrayList<String> seasonArryList = new ArrayList<>();
    ArrayList<String> qunatityUnitArryList = new ArrayList<>();
    HashMap<String, Integer> quantityUnitHm;
    HashMap<String, Integer> seasonNameHm;
    int season_id = 0;
    int quantity_unit_id = 0;
    int cropCategory_id = 0;
    int subcropCategory_id = 0;
    int farmerId = 0;
    HashMap<String, Integer> cropCategoryHM;
    ArrayList<String> cropCategoryList = new ArrayList<>();
    ArrayList<String> subCategoryArrayList = new ArrayList<>();
    ArrayList<SaleDetailsPojo> saleDetailsPojos = new ArrayList<>();
    ArrayList<ProductionDetailsPojo> productionDetailsPojos = new ArrayList<>();
    private ArrayList<String> farmarArrayList = new ArrayList<>();
    private HashMap<String, Integer> farmarNameHM;
    HashMap<String, Integer> subCategoryHM;
    SaleDetailsPojo saleDetailsPojo;
    ProductionDetailsPojo productionDetailsPojo;
    boolean isEditable = false;
    private String Select_Farmer;
    String fromSalesDetials,selected_farmer,selected_plant;
    ProgressDialog dialog;
    LinearLayout ll_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_detals_in_put);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + "Add Details" + "</font>"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        spin_season = findViewById(R.id.spin_season);
        spin_crop = findViewById(R.id.spin_crop);
        spin_quantity_unit = findViewById(R.id.spin_quantity_unit);
        ll_farmer = findViewById(R.id.ll_farmer);
        ll_price = findViewById(R.id.ll_price);
        ll_quantity_unit = findViewById(R.id.ll_quantity_unit);
        spin_crop_category = findViewById(R.id.spin_crop_category);
        spin_farmer = findViewById(R.id.spin_farmer);
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        et_price = findViewById(R.id.et_price);
        et_quantity = findViewById(R.id.et_quantity);
        btn_submit = findViewById(R.id.btn_submit);
        et_year = findViewById(R.id.et_year);
        seasonNameHm = new HashMap<>();
        quantityUnitHm = new HashMap<>();
        cropCategoryHM = new HashMap<>();
        subCategoryHM = new HashMap<>();
        farmarNameHM = new HashMap<>();
        saleDetailsPojo = new SaleDetailsPojo();
        productionDetailsPojo=new ProductionDetailsPojo();
        dialog = new ProgressDialog(this);
        setSeasonSpinner();
        getAllCategoryList();
        setCategorySpinner();
        setSubcategorySpinner();

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            fromSalesDetials=bundle.getString("fromSalesDetials","");
            selected_plant=bundle.getString("selected_plant","");
            selected_farmer=bundle.getString("selected_farmer","");
            if (fromSalesDetials.equals("2")){
                ll_price.setVisibility(View.GONE);
                ll_quantity_unit.setVisibility(View.VISIBLE);
                setQuantityUnitSpinner();
            }else {
                ll_price.setVisibility(View.VISIBLE);
                ll_quantity_unit.setVisibility(View.GONE);

            }
        }
        if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
            ll_farmer.setVisibility(View.VISIBLE);
            setFarmerSpinner();
        } else {
            saleDetailsPojo.setFarmer_id(sharedPrefHelper.getString("farmer_id", ""));
            ll_farmer.setVisibility(View.GONE);
        }
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()){
                if (fromSalesDetials.equals("1")) {
                    saleDetailsPojo.setCrop_type_catagory_id(String.valueOf(cropCategory_id));
                    saleDetailsPojo.setCrop_type_subcatagory_id(String.valueOf(subcropCategory_id));
                    saleDetailsPojo.setCrop_type_price(et_price.getText().toString());
                    saleDetailsPojo.setYear(et_year.getText().toString());
                    saleDetailsPojo.setSeason_id(String.valueOf(season_id));
                    if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
                        saleDetailsPojo.setFarmer_id(String.valueOf(farmerId));
                    } else {
                        saleDetailsPojo.setFarmer_id(sharedPrefHelper.getString("farmer_id", ""));
                    }
                    saleDetailsPojo.setUser_id(sharedPrefHelper.getString("user_id", ""));
                    saleDetailsPojo.setQuantity(et_quantity.getText().toString());
                    sqliteHelper.getAddseleDetailData(saleDetailsPojo, fromSalesDetials);
                    if (isInternetOn()) {
                        saleDetailsPojos = sqliteHelper.getCropListForSync(fromSalesDetials);
                        if (saleDetailsPojos.size() > 0) {
                            for (int i = 0; i < saleDetailsPojos.size(); i++) {
                                saleDetailsPojos.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));

                                Gson gson = new Gson();
                                String data = gson.toJson(saleDetailsPojos.get(i));
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                RequestBody body = RequestBody.create(JSON, data);
                                sendCropDetailsData(body, Integer.parseInt(saleDetailsPojos.get(i).getLocal_id()));
                            }
                        }
                    } else {
                        Intent Regintent = new Intent(CropDetalsInPut.this, CropSaleDetails.class);
                        startActivity(Regintent);
                        finish();
                    }
                }else {
                    productionDetailsPojo.setCrop_type_catagory_id(String.valueOf(cropCategory_id));
                    productionDetailsPojo.setCrop_type_subcatagory_id(String.valueOf(subcropCategory_id));
                    productionDetailsPojo.setYear(et_year.getText().toString());
                    productionDetailsPojo.setSeason_id(String.valueOf(season_id));
                    if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
                        productionDetailsPojo.setFarmer_id(String.valueOf(farmerId));
                    } else {
                        productionDetailsPojo.setFarmer_id(sharedPrefHelper.getString("farmer_id", ""));
                    }
                    productionDetailsPojo.setUser_id(sharedPrefHelper.getString("user_id", ""));
                    productionDetailsPojo.setQuantity(et_quantity.getText().toString());
                    productionDetailsPojo.setQuanity_unit_id(String.valueOf(quantity_unit_id));
                    sqliteHelper.getAddProductionDetailData(productionDetailsPojo);
                    if (isInternetOn()) {
                        productionDetailsPojos = sqliteHelper.getProductionListForSync();
                        if (productionDetailsPojos.size() > 0) {
                            for (int i = 0; i < productionDetailsPojos.size(); i++) {
                                productionDetailsPojos.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));

                                Gson gson = new Gson();
                                String data = gson.toJson(productionDetailsPojos.get(i));
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                RequestBody body = RequestBody.create(JSON, data);
                                sendProductionDetailsData(body, Integer.parseInt(productionDetailsPojos.get(i).getLocal_id()));
                            }
                        }
                    } else {
                        Intent Regintent = new Intent(CropDetalsInPut.this, CropSaleDetails.class);
                        startActivity(Regintent);
                        finish();
                    }


                }

                }
            }
        });

    }

    private void sendProductionDetailsData(RequestBody body, int local_id) {
        dialog = ProgressDialog.show(this, "", getString(R.string.Please_wait), true);
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
                        sqliteHelper.updateAddPlantFlag("production_details", local_id, 1);
                        String plant_id = jsonObject.optString("production_id");
                        sqliteHelper.updateServerid("production_details", local_id, Integer.parseInt(plant_id));
                        Intent Regintent = new Intent(CropDetalsInPut.this, CropSaleDetails.class);
                        startActivity(Regintent);
                        finish();
                    } else {
                        Toast.makeText(CropDetalsInPut.this, "" + message, Toast.LENGTH_LONG).show();
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

    private void sendCropDetailsData(RequestBody body, int local_id) {
        dialog = ProgressDialog.show(this, "", getString(R.string.Please_wait), true);
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
                        sqliteHelper.updateAddPlantFlag("sale_details", local_id, 1);
                        String plant_id = jsonObject.optString("sale_details_id");
                        sqliteHelper.updateServerid("sale_details", local_id, Integer.parseInt(plant_id));
                        Intent Regintent = new Intent(CropDetalsInPut.this, CropSaleDetails.class);
                        startActivity(Regintent);
                        finish();
                    } else {
                        Toast.makeText(CropDetalsInPut.this, "" + message, Toast.LENGTH_LONG).show();
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

    private void setFarmerSpinner() {
        farmarArrayList.clear();
        farmarNameHM = sqliteHelper.getFarmerspinner();

        for (int i = 0; i < farmarNameHM.size(); i++) {
            farmarArrayList.add(farmarNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            farmarArrayList.add(0, Select_Farmer);
        } else {
            farmarArrayList.add(0, getString(R.string.select_farmer));
        }
        final ArrayAdapter Adapte = new ArrayAdapter(this, R.layout.spinner_list, farmarArrayList);
        Adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_farmer.setAdapter(Adapte);

        spin_farmer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spin_farmer.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_farmer))) {
                    if (spin_farmer.getSelectedItem().toString().trim() != null) {
                        farmerId = farmarNameHM.get(spin_farmer.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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


    private void getAllCategoryList() {
        cropCategoryList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(CropDetalsInPut.this);
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
        spin_crop_category.setAdapter(Adapter);


    }

    private void setCategorySpinner() {
        spin_crop_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spin_crop_category.getSelectedItem().toString().trim() != null) {
                    cropCategory_id = cropCategoryHM.get(spin_crop_category.getSelectedItem().toString().trim());

                    getAllDistrictFromTable(cropCategory_id);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void getAllDistrictFromTable(int state_id) {
        subCategoryArrayList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(CropDetalsInPut.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            subCategoryHM = sqliteHelper.getAllSubCategory(state_id, 2);
        }
        else
        {
            subCategoryHM = sqliteHelper.getAllSubCategory(state_id, 1);
        }
        for (int i = 0; i < subCategoryHM.size(); i++) {
            subCategoryArrayList.add(subCategoryHM.keySet().toArray()[i].toString().trim());
        }

        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.spinner_list, subCategoryArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_crop.setAdapter(Adapter);
    }

    private void setSubcategorySpinner() {
        spin_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spin_crop.getSelectedItem().toString().trim() != null) {
                    subcropCategory_id = subCategoryHM.get(spin_crop.getSelectedItem().toString().trim());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setSeasonSpinner() {
        seasonArryList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(CropDetalsInPut.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            seasonNameHm = sqliteHelper.getMasterSpinnerId(5, 2);
        }
        else
        {
            seasonNameHm = sqliteHelper.getMasterSpinnerId(5, 1);
        }

        for (int i = 0; i < seasonNameHm.size(); i++) {
            seasonArryList.add(seasonNameHm.keySet().toArray()[i].toString().trim());
        }

        final ArrayAdapter ff = new ArrayAdapter(this, R.layout.spinner_list, seasonArryList);
        ff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_season.setAdapter(ff);

        spin_season.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spin_season.getSelectedItem().toString().trim().equalsIgnoreCase("")) {
                    if (spin_season.getSelectedItem().toString().trim() != null) {
                        season_id = seasonNameHm.get(spin_season.getSelectedItem().toString().trim());

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setQuantityUnitSpinner() {
        qunatityUnitArryList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(CropDetalsInPut.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            quantityUnitHm = sqliteHelper.getMasterSpinnerId(3, 2);
        }
        else
        {
            quantityUnitHm = sqliteHelper.getMasterSpinnerId(3, 1);
        }


        for (int i = 0; i < quantityUnitHm.size(); i++) {
            qunatityUnitArryList.add(quantityUnitHm.keySet().toArray()[i].toString().trim());
        }

        final ArrayAdapter ff = new ArrayAdapter(this, R.layout.spinner_list, qunatityUnitArryList);
        ff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_quantity_unit.setAdapter(ff);

        spin_quantity_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spin_quantity_unit.getSelectedItem().toString().trim().equalsIgnoreCase("")) {
                    if (spin_quantity_unit.getSelectedItem().toString().trim() != null) {
                        quantity_unit_id = quantityUnitHm.get(spin_quantity_unit.getSelectedItem().toString().trim());

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private boolean checkValidation() {
        boolean ret = true;
        if (et_year.getText().toString().trim().equals("")) {
            EditText flagEditfield = et_year;
            String msg = "Please Enter Year !";
            et_year.setError(msg);
            et_year.requestFocus();
            return false;
        }

        if (et_quantity.getText().toString().trim().equals("")) {
            EditText flagEditfield = et_quantity;
            String msg = "Please Enter quantity!";
            et_quantity.setError(msg);
            et_quantity.requestFocus();
            return false;
        }
        if (fromSalesDetials.equals("2")){

        }else {
            if (et_price.getText().toString().trim().equals("")) {
                EditText flagEditfield = et_price;
                String msg = "Please Enter price!";
                et_price.setError(msg);
                et_price.requestFocus();
                return false;
            }
        }

        return ret;
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}