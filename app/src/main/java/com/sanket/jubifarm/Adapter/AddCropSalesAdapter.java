package com.sanket.jubifarm.Adapter;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Activity.AddTranner;
import com.sanket.jubifarm.Activity.FarmerRegistration;
import com.sanket.jubifarm.Activity.InputOrdiringListActivity;
import com.sanket.jubifarm.Modal.SaleDetailsPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.sanket.jubifarm.utils.CommonClass;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCropSalesAdapter  extends RecyclerView.Adapter<AddCropSalesAdapter.ViewHolder> {
    List<SaleDetailsPojo> arrayList;
    Context context;
    ArrayList<String> seasonArryList;
    ArrayList<String> qunatityUnitArryList;
    HashMap<String, Integer> quantityUnitHm;
    HashMap<String, Integer> seasonNameHm;
    SqliteHelper sqliteHelper;
    int season_id = 0;
    int quantity_unit_id = 0;
    String fromSalesDetials="";
    SharedPrefHelper sharedPrefHelper;
    int farmer_id=0;
    int saved=1;
    SaleDetailsPojo saleDetailsPojo;
    ArrayList<SaleDetailsPojo> saleDetailsPojosAL;
    String unique_id="";
    DatePickerDialog datePickerDialog;

    /*HashMap<String, String> allDataHM=new HashMap<>();

    public HashMap<String, String> getAllDataHM() {
        return allDataHM;
    }*/

    public AddCropSalesAdapter(List<SaleDetailsPojo> saleDetailsPojos, Context context,
                               String fromSalesDetials, int farmer_id,String unique_id) {
        this.arrayList = saleDetailsPojos;
        this.context = context;
        this.fromSalesDetials = fromSalesDetials;
        quantityUnitHm=new HashMap<>();
        seasonNameHm=new HashMap<>();
        sqliteHelper=new SqliteHelper(context);
        sharedPrefHelper=new SharedPrefHelper(context);
        this.farmer_id=farmer_id;
        this.unique_id=unique_id;
        saleDetailsPojo=new SaleDetailsPojo();
        seasonArryList = new ArrayList<>();
        qunatityUnitArryList=new ArrayList<>();
        saleDetailsPojosAL = new ArrayList<>();


    }

    @NonNull
    @Override
    public AddCropSalesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_crop_sales, parent, false);
        AddCropSalesAdapter.ViewHolder viewholder = new AddCropSalesAdapter.ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddCropSalesAdapter.ViewHolder holder, int position) {
        String languages = sharedPrefHelper.getString("languageID","");
        int plant_id = Integer.parseInt(arrayList.get(position).getCrop_type_subcatagory_id());
        holder.et_plant_name.setText(sqliteHelper.getNameById("crop_planning", "plant_name", "crop_type_subcatagory_id", plant_id));
        if (sharedPrefHelper.getString("user_type", "").equals("Farmer")) {
            holder.et_farmer_name.setText(sharedPrefHelper.getString("first_name", "")+ " "+
                    sharedPrefHelper.getString("last_name", ""));
        } else {
            holder.et_farmer_name.setText(sqliteHelper.getNameById("farmer_registration", "farmer_name", "id", farmer_id));
        }

        if (fromSalesDetials.equals("1")){
            holder.ll_price.setVisibility(View.GONE);
        }else {
            holder.ll_price.setVisibility(View.VISIBLE);
        }
        if (farmer_id==0){
         farmer_id= Integer.parseInt(sharedPrefHelper.getString("farmer_id",""));
        }
        int plants=sqliteHelper.getPlantId(plant_id,farmer_id);
       String date = sqliteHelper.getDatesfromCrop(plants);
        if (!date.equals("")){
            String[] dats=date.split(",");
            //quantity spinner
            if(dats[1]!=null){
                holder.et_fruit_date.setText(dats[1]);
            }

            holder.et_plant_date.setText(dats[0]);
            holder.tv_season.setText(sqliteHelper.getmasterName(Integer.parseInt(dats[2]),languages));
            season_id = Integer.parseInt(dats[2]);
        }

        holder.et_fruit_date.setEnabled(false);
        holder.et_plant_date.setEnabled(false);
        holder.et_plant_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                holder.et_plant_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        holder.et_fruit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                holder.et_fruit_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        qunatityUnitArryList.clear();
        SharedPrefHelper spf = new SharedPrefHelper(context);
        String language = spf.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            quantityUnitHm = sqliteHelper.getMasterSpinnerId(3, 2);
        } else if(language.equalsIgnoreCase("kan"))
        {
            quantityUnitHm = sqliteHelper.getMasterSpinnerId(3, 3);
        }
        else
        {
            quantityUnitHm = sqliteHelper.getMasterSpinnerId(3, 1);
        }

        for (int i = 0; i < quantityUnitHm.size(); i++) {
            qunatityUnitArryList.add(quantityUnitHm.keySet().toArray()[i].toString().trim());
        }

        qunatityUnitArryList.add(0, context.getString(R.string.select_unit));



        final ArrayAdapter ff = new ArrayAdapter(context, R.layout.spinner_list, qunatityUnitArryList);
        ff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spn_quantity_unit.setAdapter(ff);

        holder.spn_quantity_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!holder.spn_quantity_unit.getSelectedItem().toString().trim().equalsIgnoreCase(context.getString(R.string.select_unit))) {
                    quantity_unit_id =  quantityUnitHm.get(holder.spn_quantity_unit.getSelectedItem().toString().trim());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //season spinner
        seasonArryList.clear();
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

        seasonArryList.add(0, context.getString(R.string.select_season));

        final ArrayAdapter ffs = new ArrayAdapter(context, R.layout.spinner_list, seasonArryList);
        ffs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spn_season.setAdapter(ffs);

        holder.spn_season.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!holder.spn_season.getSelectedItem().toString().trim().equalsIgnoreCase(context.getString(R.string.select_season))) {
                    season_id = seasonNameHm.get(holder.spn_season.getSelectedItem().toString().trim());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.spn_quantity_unit.setSelection(2);

        /*if (!holder.et_farmer_name.getText().toString().trim().equals("")) {
        allDataHM.put("farmerName", holder.et_farmer_name.getText().toString().trim());
        }
        allDataHM.put("cropTypeCategoryId", arrayList.get(position).getCrop_type_catagory_id());
        allDataHM.put("cropTypeSubCategoryId", arrayList.get(position).getCrop_type_subcatagory_id());
        if (!holder.et_price.getText().toString().trim().equals("")) {
             allDataHM.put("price", holder.et_price.getText().toString().trim());
        }
        if (!holder.et_year.getText().toString().trim().equals("")) {
            allDataHM.put("year", holder.et_year.getText().toString().trim());
        }
        allDataHM.put("seasonId", String.valueOf(season_id));
        if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
            allDataHM.put("farmerId", arrayList.get(position).getFarmer_id());
        } else {
            allDataHM.put("farmerId", sharedPrefHelper.getString("user_id", ""));
        }
        allDataHM.put("userId", sharedPrefHelper.getString("user_id", ""));
        if (!holder.et_quantity.getText().toString().trim().equals("")) {
            allDataHM.put("qty", holder.et_quantity.getText().toString().trim());
        }*/

        holder.btn_save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefHelper.setInt("clicked",saved);
                saved++;
                saleDetailsPojo.setCrop_type_catagory_id(arrayList.get(position).getCrop_type_catagory_id());
                saleDetailsPojo.setCrop_type_subcatagory_id(arrayList.get(position).getCrop_type_subcatagory_id());
                saleDetailsPojo.setYear(holder.et_year.getText().toString().trim());
                saleDetailsPojo.setUnique_id(unique_id);
                if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
                    saleDetailsPojo.setFarmer_id(String.valueOf(farmer_id));
                } else {
                    saleDetailsPojo.setFarmer_id(sharedPrefHelper.getString("farmer_id", ""));
                }
                saleDetailsPojo.setUser_id(sharedPrefHelper.getString("user_id", ""));
                saleDetailsPojo.setQuantity(holder.et_quantity.getText().toString().trim());
                saleDetailsPojo.setSeason_id(String.valueOf(season_id));
                saleDetailsPojo.setPlanted_date(holder.et_plant_date.getText().toString());
                saleDetailsPojo.setFruited_date(holder.et_fruit_date.getText().toString());
                if (fromSalesDetials.equals("2")) {
                    saleDetailsPojo.setCrop_type_price(holder.et_price.getText().toString().trim());
                }
                saleDetailsPojo.setQuanity_unit_id(String.valueOf(quantity_unit_id));
                if (fromSalesDetials.equals("2")) {
                    sqliteHelper.dropTableSale("sale_details",saleDetailsPojo.getUnique_id(),saleDetailsPojo.getCrop_type_subcatagory_id());
                }else {
                    sqliteHelper.dropTableSale("production_details",saleDetailsPojo.getUnique_id(),saleDetailsPojo.getCrop_type_subcatagory_id());

                }
                sqliteHelper.getAddseleDetailData(saleDetailsPojo, fromSalesDetials);
                Toast.makeText(context, "data saved successfully.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void sendCropSalesData(RequestBody body, int local_id) {
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
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                        sqliteHelper.updateAddPlantFlag("sale_details", local_id, 1);
                        String plant_id = jsonObject.optString("sale_details_id");
                        sqliteHelper.updateServerid("sale_details", local_id, Integer.parseInt(plant_id));

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

    private void sendCropDetailsData(RequestBody body, int local_id) {
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
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                        sqliteHelper.updateAddPlantFlag("production_details", local_id, 1);
                        String plant_id = jsonObject.optString("production_id");
                        sqliteHelper.updateServerid("production_details", local_id, Integer.parseInt(plant_id));
                    } else {
                        Toast.makeText(context, ""+message, Toast.LENGTH_LONG).show();
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

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public EditText et_year,et_farmer_name,et_quantity,et_price, et_plant_name,et_fruit_date,et_plant_date;
        Spinner spn_quantity_unit,spn_season;
        LinearLayout ll_price;
        TextView tv_season;
        Button btn_save_data;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            et_farmer_name=itemView.findViewById(R.id.et_farmer_name);
            et_year=itemView.findViewById(R.id.et_year);
            et_fruit_date=itemView.findViewById(R.id.et_fruit_date);
            et_plant_date=itemView.findViewById(R.id.et_plant_date);
            et_quantity=itemView.findViewById(R.id.et_quantity);
            et_price=itemView.findViewById(R.id.et_price);
            spn_quantity_unit=itemView.findViewById(R.id.spn_quantity_unit);
            spn_season=itemView.findViewById(R.id.spn_season);
            tv_season=itemView.findViewById(R.id.tv_season);
            ll_price=itemView.findViewById(R.id.ll_price);
            et_plant_name=itemView.findViewById(R.id.et_plant_name);
            btn_save_data=itemView.findViewById(R.id.btn_save_data);
        }
    }
}
