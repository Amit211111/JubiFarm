package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanket.jubifarm.Adapter.InputOrdiringListAdapter;
import com.sanket.jubifarm.Adapter.VisitPlantAdapter;
import com.sanket.jubifarm.Modal.InputOrderingPojo;
import com.sanket.jubifarm.Modal.InputOrderingVendor;
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

public class InputOrdiringListActivity extends AppCompatActivity {
    private RecyclerView rv_training;
    private ImageView searchfilter_INO;
    private FloatingActionButton fab;
    private TextView ChildCount;
    private TextView tv_no_data_found;

    /*normal widgets*/
    private Context context = this;
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sharedPrefHelper;
    private InputOrdiringListAdapter inputOrdiringListAdapter;
    private ArrayList<InputOrderingPojo> inputOrderingPojoAL;
    android.app.Dialog referred_to_vendor_alert;
    android.app.Dialog accept_reject_alert;
    android.app.Dialog vendor_list_alert;
    android.app.Dialog farmer_accept_reject_alert;
    private InputOrderingVendor inputOrderingVendor;
    private ArrayList<String> farmarArrayList;
    private HashMap<String, Integer> farmarNameHM;
    private int farmerId=0;
    private ArrayList<String> seedsAl;
    private HashMap<String, Integer> seedsHM;
    private int seedsId=0;
    String selected_farmer;
    private String tab_type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_ordiring_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.INPUT_ORDERING_LIST) + "</font>"));

        initViews();
        selected_farmer=sharedPrefHelper.getString("selected_farmer","");
        /*get intent values here*/
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null) {
            tab_type=bundle.getString("tab_type", "");
        }

        setAdapter();

        /*show and hide floating button*/
        showHideFloatingButton();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inputOrdering = new Intent(context, InPutOrdiringActivity.class);
                startActivity(inputOrdering);
                finish();
            }
        });

        searchfilter_INO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(InputOrdiringListActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.inputording_filter, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                //farmer spinner
                LinearLayout ll_farmer = dialogView.findViewById(R.id.ll_farmer);
                if (sharedPrefHelper.getString("user_type", "").equals("Farmer")) {
                    ll_farmer.setVisibility(View.GONE);
                }
                MaterialSpinner spn_farmer = dialogView.findViewById(R.id.spn_farmer);
                farmarArrayList.clear();
                farmarNameHM = sqliteHelper.getFarmerspinner();
                for (int i = 0; i < farmarNameHM.size(); i++) {
                    farmarArrayList.add(farmarNameHM.keySet().toArray()[i].toString().trim());

                }
                farmarArrayList.add(0, getString(R.string.select_farmer));
                ArrayAdapter arrayAdapterFarmer = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, farmarArrayList);
                spn_farmer.setAdapter(arrayAdapterFarmer);

                farmerId=0;
                spn_farmer.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        if (!item.equals(getString(R.string.select_farmer))) {
                            farmerId = farmarNameHM.get(spn_farmer.getText().toString().trim());
                        }
                    }
                });
                //input-type spinner
                MaterialSpinner spn_input_type = dialogView.findViewById(R.id.spn_input_type);
                seedsAl.clear();
                SharedPrefHelper spf = new SharedPrefHelper(InputOrdiringListActivity.this);
                String language = spf.getString("languageID","");
                if(language.equalsIgnoreCase("hin"))
                {
                    seedsHM = sqliteHelper.getMasterSpinnerId(13, 2);
                }else if(language.equalsIgnoreCase("kan"))
                {
                    seedsHM = sqliteHelper.getMasterSpinnerId(13, 3);
                }
                else
                {
                    seedsHM = sqliteHelper.getMasterSpinnerId(13, 1);
                }
                ;
                for (int i = 0; i < seedsHM.size(); i++) {
                    seedsAl.add(seedsHM.keySet().toArray()[i].toString().trim());

                }
                seedsAl.add(0, getString(R.string.select_input_type));
                ArrayAdapter arrayAdapterSeeds = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, seedsAl);
                spn_input_type.setAdapter(arrayAdapterSeeds);

                seedsId=0;
                spn_input_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        if (!item.equals(getString(R.string.select_input_type))) {
                            seedsId = seedsHM.get(spn_input_type.getText().toString().trim());
                        }
                    }
                });

                //search click
                TextView tv_search = dialogView.findViewById(R.id.tv_search);
                tv_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputOrderingPojoAL.clear();
                        inputOrderingPojoAL = sqliteHelper.getFilterInputOrderingList(seedsId, farmerId);
                        if (inputOrderingPojoAL.size() > 0) {
                            tv_no_data_found.setVisibility(View.GONE);
                            inputOrdiringListAdapter = new InputOrdiringListAdapter(InputOrdiringListActivity.this, inputOrderingPojoAL);
                            int counter = inputOrderingPojoAL.size();
                            ChildCount.setText(": " + counter);
                            rv_training.setHasFixedSize(true);
                            rv_training.setLayoutManager(new LinearLayoutManager(InputOrdiringListActivity.this));
                            rv_training.setAdapter(inputOrdiringListAdapter);

                            inputOrdiringListAdapter.onItemClick(new ClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")
                                            && inputOrderingPojoAL.get(position).getStatus_id().equals("1")) {
                                        showPopupForVendorList(inputOrderingPojoAL, position);
                                    } else if (sharedPrefHelper.getString("user_type", "").equals("Supplier")
                                            && inputOrderingPojoAL.get(position).getStatus_id().equals("5")) {
                                        showPopupForAcceptReject(inputOrderingPojoAL, position);
                                    } else if (sharedPrefHelper.getString("user_type", "").equals("Farmer")
                                            && inputOrderingPojoAL.get(position).getStatus_id().equals("3")) {
                                        showPopupForFarmerAcceptReject(inputOrderingPojoAL, position);
                                    } else {
                                        //showPopupForViewVendorList(inputOrderingPojoAL, position);
                                    }
                                }

                                @Override
                                public void onViewLandClick(int position) {

                                }

                                @Override
                                public void onEditLandClick(int position) {

                                }

                                @Override
                                public void onCheckBoxClick(int position) {

                                }

                              });
                            alertDialog.dismiss();
                        } else {
                            inputOrderingPojoAL.clear();
                            inputOrdiringListAdapter = new InputOrdiringListAdapter(InputOrdiringListActivity.this, inputOrderingPojoAL);
                            int counter = inputOrderingPojoAL.size();
                            ChildCount.setText(": " + counter);
                            rv_training.setHasFixedSize(true);
                            rv_training.setLayoutManager(new LinearLayoutManager(InputOrdiringListActivity.this));
                            rv_training.setAdapter(inputOrdiringListAdapter);
                            alertDialog.dismiss();
                            tv_no_data_found.setVisibility(View.VISIBLE);
                        }
                    }
                });

                alertDialog.show();
            }
        });

    }

    //Approved=3
    //Pending=1
    //Rejected=7
    //Vendor Referred=3
    private void showHideFloatingButton() {

        if (tab_type.equals("3") || tab_type.equals("1") ||
                tab_type.equals("7") || tab_type.equals("2")) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
        if (sharedPrefHelper.getString("user_type", "").equals("Supplier")){
            fab.setVisibility(View.GONE);
        }
    }

    private void setAdapter() {
        inputOrderingPojoAL = sqliteHelper.getInputOrderingList(selected_farmer, tab_type);
        if (inputOrderingPojoAL.size() > 0) {
            inputOrdiringListAdapter = new InputOrdiringListAdapter(this, inputOrderingPojoAL);
            int counter = inputOrderingPojoAL.size();
            ChildCount.setText(": " + counter);
            rv_training.setHasFixedSize(true);
            rv_training.setLayoutManager(new LinearLayoutManager(this));
            rv_training.setAdapter(inputOrdiringListAdapter);

            inputOrdiringListAdapter.onItemClick(new ClickListener() {
                @Override
                public void onItemClick(int position) {
                    Log.e("user_type", "onItemClick: "+sharedPrefHelper.getString("user_type", ""));
                    Log.e("status_id", "onItemClick: "+inputOrderingPojoAL.get(position).getStatus_id());
                    if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")
                            && inputOrderingPojoAL.get(position).getStatus_id().equals("1")) {
                        showPopupForVendorList(inputOrderingPojoAL, position);
                    } else if (sharedPrefHelper.getString("user_type", "").equals("Supplier")
                            && inputOrderingPojoAL.get(position).getStatus_id().equals("5")) {
                        showPopupForAcceptReject(inputOrderingPojoAL, position);
                    } else if (sharedPrefHelper.getString("user_type", "").equals("Farmer")
                            && inputOrderingPojoAL.get(position).getStatus_id().equals("3")) {
                        showPopupForFarmerAcceptReject(inputOrderingPojoAL, position);
                    } else {
                        //showPopupForViewVendorList(inputOrderingPojoAL, position);
                    }
                }

                @Override
                public void onViewLandClick(int position) {

                }

                @Override
                public void onEditLandClick(int position) {

                }

                @Override
                public void onCheckBoxClick(int position) {

                }


            });
        } else {
            tv_no_data_found.setVisibility(View.VISIBLE);
        }
    }

    private void showPopupForFarmerAcceptReject(ArrayList<InputOrderingPojo> inputOrderingPojoAL, int position) {
        farmer_accept_reject_alert = new android.app.Dialog(context);

        farmer_accept_reject_alert.setContentView(R.layout.farmer_accept_reject_dialog);
        farmer_accept_reject_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = farmer_accept_reject_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        Button btn_accept = farmer_accept_reject_alert.findViewById(R.id.btn_accept);
        Button btn_cancel = farmer_accept_reject_alert.findViewById(R.id.btn_cancel);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetOn()) {
                    callAcceptRejectFarmerApi(inputOrderingPojoAL.get(position).getId());
                } else {
                    Toast.makeText(context,
                            getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
                }
                farmer_accept_reject_alert.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farmer_accept_reject_alert.dismiss();
            }
        });

        farmer_accept_reject_alert.show();
        farmer_accept_reject_alert.setCanceledOnTouchOutside(false);
    }

    private void callAcceptRejectFarmerApi(String id) {
        int venderID=sqliteHelper.getSupplierLastInsertedLocalID();
        String inputOrderingVenderId=sqliteHelper.getInputOrderingVenderId(id, String.valueOf(venderID));
        inputOrderingVendor.setInput_ordering_vender_id(inputOrderingVenderId);//auto increment id of input_ordering_vender
        inputOrderingVendor.setInput_ordering_id(id);
        inputOrderingVendor.setIs_approved_by_farmer("1");
        inputOrderingVendor.setRole_id(sharedPrefHelper.getString("role_id", ""));

        Gson gson = new Gson();
        String data = gson.toJson(inputOrderingVendor);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        sendAcceptRejectFarmerData(body, id, inputOrderingVenderId);
    }

    private void sendAcceptRejectFarmerData(RequestBody body, String id, String inputOrderingVenderId) {
        ProgressDialog mProgressDialog = ProgressDialog.show(context, "", "Please wait...", true);
        APIClient.getClient().create(JubiForm_API.class).callApprovedByFarmerApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    mProgressDialog.dismiss();
                    Log.e("Accept-Reject", "vxghshj " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        try {
                            sqliteHelper.updateInputOrderingStatus("input_ordering", Integer.parseInt(id), "Approved by Farmer");
                            sqliteHelper.updateInputOrderingStatusId("input_ordering", Integer.parseInt(id), "4");

                            sqliteHelper.updateId("input_ordering_vender", "is_approved_by_farmer",
                                    Integer.parseInt("1"), Integer.parseInt(inputOrderingVenderId), "id");

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(context, InputOrderingHome.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgressDialog.dismiss();
            }
        });
    }

    private void showPopupForViewVendorList(ArrayList<InputOrderingPojo> inputOrderingPojoAL, int position) {
        vendor_list_alert = new android.app.Dialog(context);

        vendor_list_alert.setContentView(R.layout.inputording_dialog);
        vendor_list_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = vendor_list_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        TextView tv_category = vendor_list_alert.findViewById(R.id.tv_category);
        TextView tv_date = vendor_list_alert.findViewById(R.id.tv_date);
        TextView tv_quantity = vendor_list_alert.findViewById(R.id.tv_quantity);
        TextView tv_status = vendor_list_alert.findViewById(R.id.tv_status);
        Button btn_referred_to_vendor = vendor_list_alert.findViewById(R.id.btn_referred_to_vendor);
        Button btn_view_vendor_list = vendor_list_alert.findViewById(R.id.btn_view_vendor_list);
        Button btn_cancel = vendor_list_alert.findViewById(R.id.btn_cancel);

        if (sharedPrefHelper.getString("user_type", "").equals("Farmer") ||
                sharedPrefHelper.getString("user_type", "").equals("Supplier")) {
            btn_referred_to_vendor.setVisibility(View.GONE);
        }
        if (sqliteHelper.getSupplierID()!=0) {
            btn_view_vendor_list.setVisibility(View.VISIBLE);
        }

        //set text values
        int itemsId = Integer.parseInt(inputOrderingPojoAL.get(position).getInput_type_id());
        String items_name = sqliteHelper.getNameById("master", "master_name", "caption_id", itemsId);
        tv_category.setText(getString(R.string.Items)+" : "+items_name);
        tv_date.setText(getString(R.string.Date)+" : "+inputOrderingPojoAL.get(position).getRequest_date_to()+
                " - "+inputOrderingPojoAL.get(position).getRequest_date_from());
        tv_quantity.setText(getString(R.string.Date)+" : "+inputOrderingPojoAL.get(position).getQuantity());
        String status = inputOrderingPojoAL.get(position).getStatus();
        if (status!=null) {
                tv_status.setText(getString(R.string.Status)+" : "+ status);
        }

        btn_referred_to_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vendorList = new Intent(context, VendorList.class);
                vendorList.putExtra("category_id", inputOrderingPojoAL.get(position).getInput_type_id());
                vendorList.putExtra("farmer_login", "farmer_login");
                startActivity(vendorList);
                vendor_list_alert.dismiss();
            }
        });
        btn_view_vendor_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vendorList = new Intent(context, VendorList.class);
                vendorList.putExtra("category_id", inputOrderingPojoAL.get(position).getInput_type_id());
                vendorList.putExtra("farmer_login", "farmer_login");
                startActivity(vendorList);
                vendor_list_alert.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendor_list_alert.dismiss();
            }
        });

        vendor_list_alert.show();
        vendor_list_alert.setCanceledOnTouchOutside(false);
    }

    private void showPopupForAcceptReject(ArrayList<InputOrderingPojo> inputOrderingPojoAL, int position) {
        accept_reject_alert = new android.app.Dialog(context);

        accept_reject_alert.setContentView(R.layout.accept_reject_dialog);
        accept_reject_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = accept_reject_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        EditText et_excepted_price_per_unit = accept_reject_alert.findViewById(R.id.et_excepted_price_per_unit);
        EditText et_quantity = accept_reject_alert.findViewById(R.id.et_quantity);
        Button btn_accept = accept_reject_alert.findViewById(R.id.btn_accept);
        Button btn_reject = accept_reject_alert.findViewById(R.id.btn_reject);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetOn()) {//inout_ordering_id send
                    callAcceptRejectApi(et_excepted_price_per_unit.getText().toString().trim(),
                            et_quantity.getText().toString().trim(), "Approve",
                            inputOrderingPojoAL.get(position).getId());
                } else {
                    Toast.makeText(context,getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
                }
                accept_reject_alert.dismiss();
            }
        });
        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetOn()) {
                    callAcceptRejectApi(et_excepted_price_per_unit.getText().toString().trim(),
                            et_quantity.getText().toString().trim(), "Reject",
                            inputOrderingPojoAL.get(position).getId());
                } else {
                    Toast.makeText(context,
                            getString(R.string.please_chekc_network), Toast.LENGTH_SHORT).show();
                }
                accept_reject_alert.dismiss();
            }
        });

        accept_reject_alert.show();
        accept_reject_alert.setCanceledOnTouchOutside(false);
    }

    private void callAcceptRejectApi(String price, String quantity, String acceptReject, String id) {
        int venderID=sqliteHelper.getSupplierLastInsertedLocalID();
        String inputOrderingVenderId=sqliteHelper.getInputOrderingVenderId(id, String.valueOf(venderID));
        String inputOrderingVenderVendorId=sqliteHelper.getInputOrderingVenderVendorId(id, String.valueOf(venderID));
        inputOrderingVendor.setInput_ordering_vender_id(inputOrderingVenderId);//auto increment id of input_ordering_vender
        inputOrderingVendor.setInput_ordering_id(id); //auto increment id of input_ordering
        inputOrderingVendor.setExpected_price_per_unit(price);
        inputOrderingVendor.setQuantity(quantity);
        inputOrderingVendor.setStatus(acceptReject);
        inputOrderingVendor.setRole_id(sharedPrefHelper.getString("role_id", ""));
        inputOrderingVendor.setVender_id(inputOrderingVenderVendorId);

        Gson gson = new Gson();
        String data = gson.toJson(inputOrderingVendor);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        sendAcceptRejectData(body, quantity, price, acceptReject, id, inputOrderingVenderId);
    }

    private void sendAcceptRejectData(RequestBody body, String quantity, String price, String acceptReject,
                                      String id, String inputOrderingVenderId) {
        ProgressDialog dialog = ProgressDialog.show(context, "", "Please wait...", true);
        APIClient.getClient().create(JubiForm_API.class).callInputOrderingApprovalStatusApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("Accept-Reject", "vxghshj " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String input_ordering_vender_id = jsonObject.optString("input_ordering_vender_id");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        try {
                            if (acceptReject.equals("Approve")) {
                                sqliteHelper.updateInputOrderingStatus("input_ordering", Integer.parseInt(id), "Approved by Vendor");
                                sqliteHelper.updateInputOrderingStatusId("input_ordering", Integer.parseInt(id), "3");
                            } else {
                                sqliteHelper.updateInputOrderingStatus("input_ordering", Integer.parseInt(id), "Reject by Vendor");
                                sqliteHelper.updateInputOrderingStatusId("input_ordering", Integer.parseInt(id), "7");
                            }
                            sqliteHelper.updateId("input_ordering_vender", "status",
                                    Integer.parseInt(acceptReject), Integer.parseInt(inputOrderingVenderId), "id");
                            sqliteHelper.updateId("input_ordering_vender", "quantity",
                                    Integer.parseInt(quantity), Integer.parseInt(inputOrderingVenderId), "id");
                            sqliteHelper.updateId("input_ordering_vender", "expected_price_per_unit",
                                    Integer.parseInt(price), Integer.parseInt(inputOrderingVenderId), "id");
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
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

    private void showPopupForVendorList(ArrayList<InputOrderingPojo> inputOrderingPojoAL, int position) {
        referred_to_vendor_alert = new android.app.Dialog(context);

        referred_to_vendor_alert.setContentView(R.layout.inputording_dialog);
        referred_to_vendor_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = referred_to_vendor_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        TextView tv_category = referred_to_vendor_alert.findViewById(R.id.tv_category);
        TextView tv_date = referred_to_vendor_alert.findViewById(R.id.tv_date);
        TextView tv_quantity = referred_to_vendor_alert.findViewById(R.id.tv_quantity);
        TextView tv_status = referred_to_vendor_alert.findViewById(R.id.tv_status);
        Button btn_referred_to_vendor = referred_to_vendor_alert.findViewById(R.id.btn_referred_to_vendor);
        Button btn_cancel = referred_to_vendor_alert.findViewById(R.id.btn_cancel);

        //set text values
        int itemsId = Integer.parseInt(inputOrderingPojoAL.get(position).getInput_type_id());
        String items_name = sqliteHelper.getNameById("master", "master_name", "caption_id", itemsId);
        tv_category.setText(getString(R.string.Items) +" : "+items_name);
        tv_date.setText(getString(R.string.Date) +" : "+inputOrderingPojoAL.get(position).getRequest_date_to()+
                " - "+inputOrderingPojoAL.get(position).getRequest_date_from());
        tv_quantity.setText(getString(R.string.Quantity) +" : "+inputOrderingPojoAL.get(position).getQuantity());
        String status = inputOrderingPojoAL.get(position).getStatus();
        if (status!=null) {
            tv_status.setText(getString(R.string.Status)+" : "+ status);
        }

        btn_referred_to_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vendorList = new Intent(context, VendorList.class);
                vendorList.putExtra("category_id", inputOrderingPojoAL.get(position).getInput_type_id());
                vendorList.putExtra("input_ordering_id", inputOrderingPojoAL.get(position).getId());
                vendorList.putExtra("status", "Request Shared with Vendor");
                vendorList.putExtra("status_id", "2");
                startActivity(vendorList);
                finish();
                referred_to_vendor_alert.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referred_to_vendor_alert.dismiss();
            }
        });

        referred_to_vendor_alert.show();
        referred_to_vendor_alert.setCanceledOnTouchOutside(false);
    }

    private void initViews() {
        fab = findViewById(R.id.fab);
        rv_training = findViewById(R.id.rv_cropplanning);
        tv_no_data_found = findViewById(R.id.tv_no_data_found);
        searchfilter_INO = findViewById(R.id.searchfilter_INO);
        sqliteHelper = new SqliteHelper(context);
        sharedPrefHelper = new SharedPrefHelper(context);
        ChildCount = findViewById(R.id.ChildCount);
        inputOrderingVendor = new InputOrderingVendor();
        farmarArrayList = new ArrayList<>();
        farmarNameHM = new HashMap<>();
        seedsAl = new ArrayList<>();
        seedsHM = new HashMap<>();
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
