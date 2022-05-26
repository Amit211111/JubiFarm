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
import com.sanket.jubifarm.Adapter.PostPlantationAdapter;
import com.sanket.jubifarm.Modal.PostPlantationPojo;
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

public class PostPlantationAcivity extends AppCompatActivity {
    Button btn_Submit;
    SqliteHelper sqliteHelper;
    PostPlantationPojo postPlantationPojo;
    SharedPrefHelper sharedPrefHelper;
    ProgressDialog dialog;
    ArrayList<PostPlantationPojo> arrayList = new ArrayList<>();
    ArrayList<PostPlantationPojo> arrayList1 = new ArrayList<>();
    ArrayList<PostPlantationPojo> arrayListSync = new ArrayList<>();
    PostPlantationPojo postmonitoringmodal;
    RecyclerView post_plantation_view;
    PostPlantationAdapter PostplantAdapter;
    HashMap<Integer, String> selectedVlauesHM;
    String plant_id;
    String farmer_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_plantation_acivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + "POST PLANTATION" + "</font>"));

        sharedPrefHelper = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);
        postPlantationPojo = new PostPlantationPojo();
        dialog = new ProgressDialog(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            plant_id = bundle.getString("plant_id", "");
            farmer_id = bundle.getString("farmer_id", "");
        }

        InserId();
        String language = sharedPrefHelper.getString("languageID", "");
        if (language.equalsIgnoreCase("hin")) {
            arrayList = sqliteHelper.getPostPlant(1);
        } else {
            arrayList = sqliteHelper.getPostPlant(1);
        }
        arrayList1 = sqliteHelper.getPostPlantationList(plant_id,farmer_id);
        if (arrayList1.size()>0){
            btn_Submit.setVisibility(View.GONE);
        }
        PostplantAdapter = new PostPlantationAdapter(arrayList, this,arrayList1);
        post_plantation_view.setHasFixedSize(true);
        post_plantation_view.setLayoutManager(new LinearLayoutManager(this));
        post_plantation_view.setAdapter(PostplantAdapter);
        getData();
    }

    private void getData() {
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedVlauesHM = new HashMap<>();
                selectedVlauesHM = PostplantAdapter.getCheckedValues();
                if (selectedVlauesHM.size() > 0) {
                    ArrayList<Integer> values = new ArrayList<>();
                    ArrayList<String> ans = new ArrayList<>();
                    for (int i = 0; i < selectedVlauesHM.size(); i++) {
                        ans.add(selectedVlauesHM.values().toArray()[i].toString().trim());
                        values.add((Integer) selectedVlauesHM.keySet().toArray()[i]);
                    }
                    for (int i = 0; i < values.size(); i++) {
                        postPlantationPojo = new PostPlantationPojo();
                        postPlantationPojo.setPost_plantation_id(String.valueOf(values.get(i)));
                        postPlantationPojo.setValue(ans.get(i));
                        postPlantationPojo.setCrop_type_catagory_id(plant_id);
                        if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
                            postPlantationPojo.setFarmer_id(String.valueOf(farmer_id));
                        } else {
                            postPlantationPojo.setFarmer_id(sharedPrefHelper.getString("farmer_id", ""));
                        }
                        postPlantationPojo.setUser_id(sharedPrefHelper.getString("user_id", ""));
                        sqliteHelper.getPostPlantationData(postPlantationPojo);

                    }
                    if (isInternetOn()) {
                        arrayListSync = sqliteHelper.getPostPlantatationForSync();
                        if (arrayListSync.size() > 0) {
                            for (int i = 0; i < arrayListSync.size(); i++) {
                                arrayListSync.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));

                                Gson gson = new Gson();
                                String data = gson.toJson(arrayListSync.get(i));
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                RequestBody body = RequestBody.create(JSON, data);
                                sendPostPlantation(body, arrayListSync.get(i).getLocal_id());

                            }
                            Toast.makeText(PostPlantationAcivity.this, "You have done post plantation.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PostPlantationAcivity.this, CropDetailActivity.class);
                            intent.putExtra("plant_id",plant_id);
                            startActivity(intent);
                            finish();

                        }
                    } else {
                        Toast.makeText(PostPlantationAcivity.this, getString(R.string.no_internet_data_saved_locally), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PostPlantationAcivity.this, CropDetailActivity.class);
                        intent.putExtra("plant_id",plant_id);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(PostPlantationAcivity.this, "Please select radio buttons", Toast.LENGTH_SHORT).show();
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

    private void InserId() {
        btn_Submit = findViewById(R.id.btn_Submit);
        post_plantation_view = findViewById(R.id.post_plantation_view);

    }


    private void sendPostPlantation(RequestBody body, String local_id) {
        APIClient.getClient().create(JubiForm_API.class).add_post_plantation(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("subp", "Polstplaantation==" + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    if (status.equals("1")) {
                        sqliteHelper.updateAddPlantFlag("post_plantation", Integer.parseInt(local_id), 1);
                        String sub_id = jsonObject.optString("post_id");
                        sqliteHelper.updateServerid("post_plantation", Integer.parseInt(local_id), Integer.parseInt(sub_id));

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
        return super.onOptionsItemSelected(item);
    }
}

