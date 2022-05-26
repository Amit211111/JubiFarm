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
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Adapter.SubPlantationAdapter;
import com.sanket.jubifarm.Modal.SubPlantationPojo;
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

public class SubPlantationActivity extends AppCompatActivity {
    Button Submit;
    SqliteHelper sqliteHelper;
    SubPlantationPojo subPlantationPojo;
    SharedPrefHelper sharedPrefHelper;
    ProgressDialog dialog;
    ArrayList<SubPlantationPojo> arrayList = new ArrayList<>();
    ArrayList<SubPlantationPojo> arrayList1 = new ArrayList<>();
    ArrayList<SubPlantationPojo> arrayListSync = new ArrayList<>();
    SubPlantationPojo submonitoringmodal;
    RecyclerView rv_subplant;
    SubPlantationAdapter subPlantationAdapter;
    HashMap<Integer, String> selectedVlauesHM;
    String plant_id;
    String farmer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_plantation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + "SUB PLANTATION" + "</font>"));

        sharedPrefHelper = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);
        dialog = new ProgressDialog(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            plant_id = bundle.getString("plant_id", "");
            farmer_id = bundle.getString("farmer_id", "");
        }

        inserId();
        String language = sharedPrefHelper.getString("languageID", "");
        if (language.equalsIgnoreCase("hin")) {
            arrayList = sqliteHelper.getSubPlantationMasterList(2);
        } else {
            arrayList = sqliteHelper.getSubPlantationMasterList(1);

        }


        arrayList1 = sqliteHelper.getSubPlantationList(plant_id, farmer_id);


        if (arrayList1.size() > 0) {
            Submit.setVisibility(View.GONE);
        }
        subPlantationAdapter = new SubPlantationAdapter(arrayList, this, arrayList1);
        rv_subplant.setHasFixedSize(true);
        rv_subplant.setLayoutManager(new LinearLayoutManager(this));
        rv_subplant.setAdapter(subPlantationAdapter);
        getData();

    }

    private void getData() {
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedVlauesHM = new HashMap<>();
                selectedVlauesHM = subPlantationAdapter.getCheckedValues();
                if (selectedVlauesHM.size() > 0) {
                    ArrayList<Integer> values = new ArrayList<>();
                    ArrayList<String> ans = new ArrayList<>();
                    for (int i = 0; i < selectedVlauesHM.size(); i++) {
                        ans.add(selectedVlauesHM.values().toArray()[i].toString().trim());
                        values.add((Integer) selectedVlauesHM.keySet().toArray()[i]);
                    }
                    for (int i = 0; i < values.size(); i++) {
                        subPlantationPojo = new SubPlantationPojo();
                        subPlantationPojo.setSub_plantation_id(String.valueOf(values.get(i)));
                        subPlantationPojo.setValue(ans.get(i));
                        subPlantationPojo.setCrop_type_catagory_id(plant_id);
                        if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
                            subPlantationPojo.setFarmer_id(String.valueOf(farmer_id));
                        } else {
                            subPlantationPojo.setFarmer_id(sharedPrefHelper.getString("farmer_id", ""));
                        }
                        subPlantationPojo.setUser_id(sharedPrefHelper.getString("user_id", ""));
                        sqliteHelper.getSubPlantationData(subPlantationPojo);

                    }
                    if (isInternetOn()) {
                        arrayListSync = sqliteHelper.getSubPlantatationForSync();
                        if (arrayListSync.size() > 0) {
                            for (int i = 0; i < arrayListSync.size(); i++) {
                                arrayListSync.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));

                                Gson gson = new Gson();
                                String data = gson.toJson(arrayListSync.get(i));
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                RequestBody body = RequestBody.create(JSON, data);
                                sendsubPlantation(body, arrayListSync.get(i).getLocal_id());

                            }
                            Toast.makeText(SubPlantationActivity.this, "You have done sub plantation, please do the post plantation.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SubPlantationActivity.this, CropDetailActivity.class);
                            intent.putExtra("plant_id", plant_id);
                            startActivity(intent);
                            finish();

                        }

                    } else {
                        Toast.makeText(SubPlantationActivity.this, getString(R.string.no_internet_data_saved_locally), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SubPlantationActivity.this, CropDetailActivity.class);
                        intent.putExtra("plant_id", plant_id);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(SubPlantationActivity.this, "Please select radio option.", Toast.LENGTH_SHORT).show();
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

    private void inserId() {
        Submit = findViewById(R.id.submit);
        rv_subplant = findViewById(R.id.rv_subplant);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
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

    private void sendsubPlantation(RequestBody body, String local_id) {
        APIClient.getClient().create(JubiForm_API.class).add_sub_plantation(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("subp", "Subplantation===" + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    if (status.equals("1")) {
                        sqliteHelper.updateAddPlantFlag("sub_plantation", Integer.parseInt(local_id), 1);
                        String sub_id = jsonObject.optString("sub_id");
                        sqliteHelper.updateServerid("sub_plantation", Integer.parseInt(local_id), Integer.parseInt(sub_id));

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


}