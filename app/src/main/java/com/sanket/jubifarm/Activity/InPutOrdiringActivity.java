package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Modal.InputOrderingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.sanket.jubifarm.utils.MyOperationLayoutAddMemeber;
import com.sanket.jubifarm.utils.MyOperationLayoutInputOrdering;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InPutOrdiringActivity extends AppCompatActivity {
    private FloatingActionButton addMore;
    private LinearLayout linearLayoutInputOrdering;
    private Button btn_submit_plantdtl;
    private EditText et_IOTodate, et_IOfromdate, et_quantity, et_remarks;
    private Spinner spnSeeds, spn_unit, spn_farmer;
    private TextView tv_Farmer_name;
    /*normal widgets*/
    private Context context = this;
    private SharedPrefHelper sharedPrefHelper;
    private SqliteHelper sqliteHelper;
    private HashMap<String, ArrayList<String>> inputOrderingHM;
    private HashMap<String, Integer> unitHM;
    private ArrayList<String> unitAl;
    private HashMap<String, Integer> seedsHM;
    private ArrayList<String> seedsAl;
    private InputOrderingPojo inputOrderingPojo;
    private ArrayList<InputOrderingPojo> inputOrderingAl;
    private int unitId = 0, seedsId = 0;
    private String user_id="";
    private LinearLayout ll_farmer;
    private ArrayList<String> farmarArrayList;
    private HashMap<String, Integer> farmarNameHM;
    boolean isEditable = false;
    private int farmerId = 0;
    String selected_farmer;
    public static android.app.Dialog submit_alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_put_ordiring);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.INPUT_ORDERING) + "</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        selected_farmer=sharedPrefHelper.getString("selected_farmer","");
        /*make condition for farmer*/
        if (selected_farmer.equals("")){
            spn_farmer.setVisibility(View.VISIBLE);
            tv_Farmer_name.setVisibility(View.GONE);

        }else {
            spn_farmer.setVisibility(View.GONE);
            tv_Farmer_name.setVisibility(View.VISIBLE);
            tv_Farmer_name.setText(sqliteHelper.getFarmerName(selected_farmer));
        }
        if (sharedPrefHelper.getString("user_type", "").equals("Farmer")) {
            ll_farmer.setVisibility(View.GONE);
        } else {
            ll_farmer.setVisibility(View.VISIBLE);
            setFarmerSpinner();
        }
        getPreferencesData();
        setToFromDate();
        setUnitSpinner();
        setSeedsSpinner();

        setAddMoreClick();
        btnSubmitClick();
    }

    private void setFarmerSpinner() {
        farmarArrayList.clear();
        farmarNameHM = sqliteHelper.getFarmerspinner();

        for (int i = 0; i < farmarNameHM.size(); i++) {
            farmarArrayList.add(farmarNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            //farmarArrayList.add(0, farmer_name);
        } else {
            farmarArrayList.add(0, getString(R.string.select_farmer));
        }
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_list, farmarArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_farmer.setAdapter(arrayAdapter);

        spn_farmer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_farmer.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_farmer))) {
                    if (spn_farmer.getSelectedItem().toString().trim() != null) {
                        farmerId = farmarNameHM.get(spn_farmer.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getPreferencesData() {
        user_id = sharedPrefHelper.getString("user_id", "");
    }

    private void setToFromDate() {
        et_IOTodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDatePickerDialog1 = null;
                Calendar mCalendar1 = Calendar.getInstance();

                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                if (mDatePickerDialog1 == null) {
                    mDatePickerDialog1 = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                            et_IOTodate.setText(String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                            et_IOTodate.setError(null);
                            mCalendar1.set(java.util.Calendar.MONTH, monthOfYear);
                            mCalendar1.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
                            mCalendar1.set(java.util.Calendar.YEAR, year);
                        }
                    }, year, month, day);

                    mDatePickerDialog1.setCanceledOnTouchOutside(true);
                }
                //Disable date before today
                mDatePickerDialog1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mDatePickerDialog1.show();
            }
        });

        et_IOfromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDatePickerDialog1 = null;
                Calendar mCalendar1 = Calendar.getInstance();

                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                if (mDatePickerDialog1 == null) {
                    mDatePickerDialog1 = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                            et_IOfromdate.setText(String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                            et_IOfromdate.setError(null);
                            mCalendar1.set(java.util.Calendar.MONTH, monthOfYear);
                            mCalendar1.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
                            mCalendar1.set(java.util.Calendar.YEAR, year);
                        }
                    }, year, month, day);

                    mDatePickerDialog1.setCanceledOnTouchOutside(true);
                }
                //Disable date before today
                mDatePickerDialog1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mDatePickerDialog1.show();
            }
        });
    }

    private void setSeedsSpinner() {
        seedsAl.clear();
        //seeds spinner
        SharedPrefHelper spf = new SharedPrefHelper(InPutOrdiringActivity.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            seedsHM = sqliteHelper.getMasterSpinnerId(13, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            seedsHM = sqliteHelper.getMasterSpinnerId(13, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            seedsHM = sqliteHelper.getMasterSpinnerId(13, 4);
        }
        else
        {
            seedsHM = sqliteHelper.getMasterSpinnerId(13, 1);
        }

        for (int i = 0; i < seedsHM.size(); i++) {
            seedsAl.add(seedsHM.keySet().toArray()[i].toString().trim());
        }

        seedsAl.add(0, getString(R.string.select_items));
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_list, seedsAl);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSeeds.setAdapter(arrayAdapter);

        spnSeeds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spnSeeds.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_items))) {
                    if (spnSeeds.getSelectedItem().toString().trim() != null) {
                        seedsId = seedsHM.get(spnSeeds.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUnitSpinner() {
        unitAl.clear();
        //product unit spinner
        SharedPrefHelper spf = new SharedPrefHelper(InPutOrdiringActivity.this);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            unitHM = sqliteHelper.getMasterSpinnerId(3, 2);
        }
        else if(language.equalsIgnoreCase("kan"))
        {
            unitHM = sqliteHelper.getMasterSpinnerId(3, 3);
        }
        else if(language.equalsIgnoreCase("guj"))
        {
            unitHM = sqliteHelper.getMasterSpinnerId(3, 4);
        }
        else
        {
            unitHM = sqliteHelper.getMasterSpinnerId(3, 1);
        }

        for (int i = 0; i < unitHM.size(); i++) {
            unitAl.add(unitHM.keySet().toArray()[i].toString().trim());
        }
        unitAl.add(0, getString(R.string.select_unit));

        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_list, unitAl);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_unit.setAdapter(arrayAdapter);

        spn_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_unit.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_unit))) {
                    if (spn_unit.getSelectedItem().toString().trim() != null) {
                        unitId = unitHM.get(spn_unit.getSelectedItem().toString().trim());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void btnSubmitClick() {
        btn_submit_plantdtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOrderingHM.clear();
                inputOrderingHM = MyOperationLayoutInputOrdering.display(InPutOrdiringActivity.this);
                ArrayList<String> toDateAl = new ArrayList<>();
                ArrayList<String> fromDateAl = new ArrayList<>();
                ArrayList<String> quantityAl = new ArrayList<>();
                ArrayList<String> remarksAl = new ArrayList<>();
                ArrayList<String> unitAl = new ArrayList<>();
                ArrayList<String> seedsAl = new ArrayList<>();

                toDateAl = inputOrderingHM.get("toDate");
                fromDateAl = inputOrderingHM.get("fromDate");
                quantityAl = inputOrderingHM.get("quantity");
                remarksAl = inputOrderingHM.get("remarks");
                unitAl = inputOrderingHM.get("unit");
                seedsAl = inputOrderingHM.get("seeds");

                if (toDateAl.size() > 0 && fromDateAl.size() > 0 && seedsAl.size() > 0
                        && quantityAl.size() > 0 && remarksAl.size() > 0 && unitAl.size() > 0) {
                    for (int i = 0; i < toDateAl.size(); i++) {
                        InputOrderingPojo inputOrderingPojo = new InputOrderingPojo();
                        inputOrderingPojo.setRequest_date_to(toDateAl.get(i));
                        inputOrderingPojo.setRequest_date_from(fromDateAl.get(i));
                        inputOrderingPojo.setQuantity(quantityAl.get(i));
                        inputOrderingPojo.setQuantity_units(String.valueOf(unitHM.get(unitAl.get(i))));
                        inputOrderingPojo.setInput_type_id(String.valueOf(seedsHM.get(seedsAl.get(i))));
                        inputOrderingPojo.setStatus(getString(R.string.Request_Raised_by_Farmer));
                        inputOrderingPojo.setStatus_id("1");
                        inputOrderingPojo.setRemarks(remarksAl.get(i));

                        if (selected_farmer.equals("")) {
                            inputOrderingPojo.setFarmer_id(sharedPrefHelper.getString("farmer_id", ""));
                        }
                        else {
                            inputOrderingPojo.setFarmer_id(selected_farmer);

                        }
                        sqliteHelper.saveInputOrderingDataInTable(inputOrderingPojo);
                    }
                }
                //for first index
                if (checkValidation()) {
                    inputOrderingPojo.setRequest_date_to(et_IOTodate.getText().toString().trim());
                    inputOrderingPojo.setRequest_date_from(et_IOfromdate.getText().toString().trim());
                    inputOrderingPojo.setQuantity(et_quantity.getText().toString().trim());
                    inputOrderingPojo.setQuantity_units(String.valueOf(unitId));
                    inputOrderingPojo.setInput_type_id(String.valueOf(seedsId));
                    inputOrderingPojo.setStatus(getString(R.string.Request_Raised_by_Farmer));

                    inputOrderingPojo.setStatus_id("1");
                    inputOrderingPojo.setRemarks(et_remarks.getText().toString().trim());
                    if (sharedPrefHelper.getString("role_id","").equals("5")) {
                        inputOrderingPojo.setFarmer_id(sharedPrefHelper.getString("farmer_id", ""));
                    } else {
                        if(selected_farmer.equals("")) {
                            inputOrderingPojo.setFarmer_id(String.valueOf(farmerId));
                        }else{
                            inputOrderingPojo.setFarmer_id(selected_farmer);
                        }
                    }

                    long insertedId = sqliteHelper.saveInputOrderingDataInTable(inputOrderingPojo);
                    if (insertedId > 0) {
                        if (isInternetOn()) {
                            inputOrderingAl = sqliteHelper.getInputOrderingDataToBeSync();
                            if (inputOrderingAl.size() > 0) {
                                for (int i = 0; i < inputOrderingAl.size(); i++) {
                                    String localId = inputOrderingAl.get(i).getLocal_id();
                                    inputOrderingAl.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));

                                    Gson gson = new Gson();
                                    String data = gson.toJson(inputOrderingAl.get(i));
                                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                    RequestBody body = RequestBody.create(JSON, data);

                                    sendInputOrderingData(body, localId);
                                }
                            }
                        }
                        else {
//                        Toast.makeText(context, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
                            showSubmitDialog(context,"Data has been saved in local database successfully.");

                        }
                    }
                    else {
                        Toast.makeText(context, getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
//                        showSubmitDialog(context,"Data save in local database successfully.");

                    }
                }
            }
        });
    }

    public static void showSubmitDialog(Context context,String message) {
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
                Intent intent = new Intent(context, InputOrdiringListActivity.class);
                context.startActivity(intent);
            }
        });

        submit_alert.show();
        submit_alert.setCanceledOnTouchOutside(false);
    }

    private void sendInputOrderingData(RequestBody body, String localId) {
        ProgressDialog dialog = ProgressDialog.show(InPutOrdiringActivity.this, "", getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).sendInputOrderingData(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String input_ordering_id = jsonObject.optString("input_ordering_id");
                    if (status.equalsIgnoreCase("1")) {
                        //update flag in tables here
                        sqliteHelper.updateId("input_ordering", "id", Integer.parseInt(input_ordering_id), Integer.parseInt(localId), "local_id");
                        sqliteHelper.updateLocalFlag("input_ordering", Integer.parseInt(localId), 1);

                        Intent intent = new Intent(context, InputOrderingHome.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(InPutOrdiringActivity.this, "" + message, Toast.LENGTH_LONG).show();
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

    private void setAddMoreClick() {
        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOperationLayoutInputOrdering.add(InPutOrdiringActivity.this, addMore);
            }
        });
    }

    private void initViews() {
        addMore = findViewById(R.id.addMore);
        linearLayoutInputOrdering = findViewById(R.id.linearLayoutInputOrdering);
        btn_submit_plantdtl = findViewById(R.id.btn_submit_plantdtl);
        inputOrderingHM = new HashMap<>();
        seedsHM = new HashMap<>();
        unitHM = new HashMap<>();
        sharedPrefHelper = new SharedPrefHelper(context);
        sqliteHelper = new SqliteHelper(context);
        et_IOTodate = findViewById(R.id.et_IOTodate);
        et_IOfromdate = findViewById(R.id.et_IOfromdate);
        et_quantity = findViewById(R.id.et_quantity);
        et_remarks = findViewById(R.id.et_remarks);

        spnSeeds = findViewById(R.id.spnSeeds);
        spn_unit = findViewById(R.id.spn_unit);
        tv_Farmer_name = findViewById(R.id.tv_Farmer_name);
        spn_farmer = findViewById(R.id.spn_farmer);
        inputOrderingPojo = new InputOrderingPojo();
        seedsAl = new ArrayList<>();
        unitAl = new ArrayList<>();
        inputOrderingAl = new ArrayList<>();
        ll_farmer = findViewById(R.id.ll_farmer);
        farmarArrayList=new ArrayList<>();
        farmarNameHM=new HashMap<>();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (et_IOfromdate.getText().toString().trim().length()==0) {
            EditText flagEditfield = et_IOfromdate;
            String msg = getString(R.string.select_from_date);
            et_IOfromdate.setError(msg);
            et_IOfromdate.requestFocus();
            return false;
        }
        if (et_IOTodate.getText().toString().trim().length()==0) {
            EditText flagEditfield = et_IOTodate;
            String msg = getString(R.string.select_to_date);
            et_IOTodate.setError(msg);
            et_IOTodate.requestFocus();
            return false;
        }
//        if (spn_farmer.getSelectedItemPosition() > 0) {
//            String itemvalue = String.valueOf(spn_farmer.getSelectedItem());
//        } else {
//            TextView errorTextview = (TextView) spn_farmer.getSelectedView();
//            errorTextview.setError("Error");
//            errorTextview.requestFocus();
//            return false;
//        }

        if(sharedPrefHelper.getString("role_id", "").equals("4")) {
            if(selected_farmer.equals("")){
                if (spn_farmer.getSelectedItem().toString().trim().equals("Select Farmer")) {
                    Toast.makeText(InPutOrdiringActivity.this, getString(R.string.select_farmer), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

        }


        if (spnSeeds.getSelectedItem().toString().trim().equals("Select Items")) {
            Toast.makeText(InPutOrdiringActivity.this, getString(R.string.select_items), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_quantity.getText().toString().trim().length()==0) {
            EditText flagEditfield = et_quantity;
            String msg = getString(R.string.enter_quantity);
            et_quantity.setError(msg);
            et_quantity.requestFocus();
            return false;
        }
        if (spn_unit.getSelectedItem().toString().trim().equals("Select Unit")) {
            Toast.makeText(InPutOrdiringActivity.this, getString(R.string.select_unit), Toast.LENGTH_SHORT).show();
            return false;
        }
        return ret;
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
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }



}


