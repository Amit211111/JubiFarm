package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Adapter.VenderListAdapter;
import com.sanket.jubifarm.Modal.InputOrderingVendor;
import com.sanket.jubifarm.Modal.SupplierRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.interfaces.ClickListener;
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

public class VendorList extends AppCompatActivity {
    private RecyclerView rv_list_of_vendors;
    private TextView tv_no_data_found;
    private ExtendedFloatingActionButton fab;
    private CheckBox check_box_all;

    /*normal widgets*/
    private Context context = this;
    private VenderListAdapter venderListAdapter;
    private ArrayList<SupplierRegistrationPojo> supplierRegistrationAl;
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sharedPrefHelper;
    private String category_id = "";
    private String input_ordering_id = "";
    private String statuss = "";
    private String status_id = "";
    private String user_id = "";
    private String farmer_login = "";
    private long insertedId=0;
    private HashMap<Integer, String> selectedVlauesHM;
    private ArrayList<InputOrderingVendor> inputOrderingVendorAl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + "VENDOR LIST" + "</font>"));

        initViews();

        /*get intent values here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category_id = bundle.getString("category_id", "");
            user_id = sharedPrefHelper.getString("user_id", "");
            input_ordering_id = bundle.getString("input_ordering_id", "");
            statuss = bundle.getString("status", "");
            status_id = bundle.getString("status_id", "");
            farmer_login = bundle.getString("farmer_login", "");
        }

        if (farmer_login.equals("farmer_login")) {
            check_box_all.setVisibility(View.GONE);
        }

        setAdapter();
        clickAllCheckedButton();
        AllListSubmitData();
    }

    private void clickAllCheckedButton() {
        check_box_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (check_box_all.isChecked()) {
                    venderListAdapter.selectAll();
                    Toast.makeText(context, "Work in progress.", Toast.LENGTH_SHORT).show();
                }
                else {
                   venderListAdapter.unselectall();
                }

            }

        });
    }

    private void AllListSubmitData() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get list data here
                InputOrderingVendor inputOrderingVendor = new InputOrderingVendor();
                selectedVlauesHM.clear();
                selectedVlauesHM = venderListAdapter.getCheckedValues();
                if (selectedVlauesHM.size() > 0) {
                    ArrayList<Integer> values = new ArrayList<>();
                    ArrayList<String> ans = new ArrayList<>();
                    for (int i = 0; i < selectedVlauesHM.size(); i++) {
                        ans.add(selectedVlauesHM.values().toArray()[i].toString().trim());
                        values.add((Integer) selectedVlauesHM.keySet().toArray()[i]);
                    }
                    for (int i = 0; i < values.size(); i++) {
                        inputOrderingVendor.setInput_ordering_id(input_ordering_id);
                        inputOrderingVendor.setUser_id(user_id);
                        inputOrderingVendor.setVendor_id(String.valueOf(values.get(i)));

                        insertedId = sqliteHelper.saveInputOrderingVendorDataInTable(inputOrderingVendor);
                    }
                    if (insertedId > 0) {
                    if (isInternetOn()) {
                        inputOrderingVendorAl = sqliteHelper.getInputOrderingVendorDataToBeSync();
                        if (inputOrderingVendorAl.size() > 0) {
                            for (int i = 0; i < inputOrderingVendorAl.size(); i++) {
                                String localId=inputOrderingVendorAl.get(i).getLocal_id();

                                inputOrderingVendorAl.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));

                                Gson gson = new Gson();
                                String data = gson.toJson(inputOrderingVendorAl.get(i));
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                RequestBody body = RequestBody.create(JSON, data);

                                sendInputOrderingVendorData(body, localId);
                            }
                        }
                    }
                    } else {
                        Toast.makeText(context,
                                "Network Error, please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void sendInputOrderingVendorData(RequestBody body, String localId) {
        ProgressDialog dialog = ProgressDialog.show(context, "", "Please wait...", true);
        APIClient.getClient().create(JubiForm_API.class).sendInputOrderingVendorData(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("gcgcgcgc", "vxghshj " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String input_ordering_vender_id = jsonObject.optString("input_ordering_vender_id");
                    if (status.equalsIgnoreCase("1")) {
                        //update flag in tables here
                        sqliteHelper.updateId("input_ordering_vender", "id", Integer.parseInt(input_ordering_vender_id), Integer.parseInt(localId), "local_id");
                        sqliteHelper.updateLocalFlag("input_ordering_vender", Integer.parseInt(localId), 1);
                        sqliteHelper.updateInputOrderingStatus("input_ordering", Integer.parseInt(category_id), statuss);
                        sqliteHelper.updateInputOrderingStatusId("input_ordering", Integer.parseInt(category_id), status_id);

                        Toast.makeText(context, "Input order referred to vendor successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, InputOrderingHome.class);
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

    private void setAdapter() {
        supplierRegistrationAl = sqliteHelper.getVendorsList(category_id);
        if (supplierRegistrationAl.size() > 0) {
            venderListAdapter = new VenderListAdapter(context, supplierRegistrationAl, category_id, farmer_login);
            rv_list_of_vendors.setHasFixedSize(true);
            rv_list_of_vendors.setLayoutManager(new LinearLayoutManager(this));
            rv_list_of_vendors.setAdapter(venderListAdapter);

        } else {
            tv_no_data_found.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        sqliteHelper = new SqliteHelper(context);
        sharedPrefHelper = new SharedPrefHelper(context);
        supplierRegistrationAl = new ArrayList<>();
        rv_list_of_vendors = findViewById(R.id.rv_list_of_vendors);
        tv_no_data_found = findViewById(R.id.tv_no_data_found);
        fab = findViewById(R.id.fab);
        check_box_all = findViewById(R.id.check_box_all);
        selectedVlauesHM=new HashMap<>();
        inputOrderingVendorAl=new ArrayList<>();
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}