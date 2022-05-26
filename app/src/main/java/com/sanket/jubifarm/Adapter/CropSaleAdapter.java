package com.sanket.jubifarm.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Activity.AddPlantGrowthActivity;
import com.sanket.jubifarm.Activity.CropSaleDetails;
import com.sanket.jubifarm.Activity.PlantGrowthListActivity;
import com.sanket.jubifarm.Modal.CropPlanningModal;
import com.sanket.jubifarm.Modal.ProductionDetailsPojo;
import com.sanket.jubifarm.Modal.SaleDetailsPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CropSaleAdapter extends RecyclerView.Adapter<CropSaleAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ProductionDetailsPojo> arrayList;
    SqliteHelper sqliteHelper;
    ProgressDialog dialog;
    SharedPrefHelper sharedPrefHelper;




    public CropSaleAdapter(Context context, ArrayList<ProductionDetailsPojo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sqliteHelper=new SqliteHelper(context);
        dialog=new ProgressDialog(context);
        sharedPrefHelper=new SharedPrefHelper(context);
    }

    @NonNull

    @Override
    public CropSaleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cropsalelist, parent, false);
        CropSaleAdapter.ViewHolder viewholder = new com.sanket.jubifarm.Adapter.CropSaleAdapter.ViewHolder(view);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull com.sanket.jubifarm.Adapter.CropSaleAdapter.ViewHolder holder, final int position) {
        String language = sharedPrefHelper.getString("languageID","");

        holder.tv_CSyears.setText(""+arrayList.get(position).getYear());
        int plant_id = Integer.parseInt(arrayList.get(position).getCrop_type_subcatagory_id());
        holder.tv_cropname.setText(sqliteHelper.getNameById("crop_planning", "plant_name", "crop_type_subcatagory_id", plant_id));
        holder.crop_quantity.setText(""+arrayList.get(position).getQuantity());
        holder.tv_fruited_date.setText(""+arrayList.get(position).getFruited_date());
        holder.tv_planted_date.setText(""+arrayList.get(position).getPlanted_date());
        holder.crop_seasion.setText(sqliteHelper.getmasterName(Integer.parseInt(arrayList.get(position).getSeason_id()),language));

        holder.img_crop_dele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaleDetailsPojo saleDetailsPojo=new SaleDetailsPojo();
                saleDetailsPojo.setTable_name("production_details");
                saleDetailsPojo.setId(arrayList.get(position).getId());
                saleDetailsPojo.setRole_id(sharedPrefHelper.getString("role_id",""));
                Gson gson = new Gson();
                String data = gson.toJson(saleDetailsPojo);
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, data);
                deletesale(body);
            }
        });


    }

    public void deletesale(RequestBody body) {
        dialog = ProgressDialog.show(context, "", context.getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).deletecrop(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    Log.e("subp", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    if (Integer.valueOf(status) == 1) {
                        dialog.dismiss();
                        String message = jsonObject.optString("message");
                        String training_attendance_id = jsonObject.optString("id");
                        sqliteHelper.deleteTableSale("production_details", training_attendance_id);
                        notifyDataSetChanged();
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, CropSaleDetails.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                        Log.e("subp", "vxghs " + status + "," + message + "," + training_attendance_id);
                    } else {
                        Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
               /* if (dialog.isShowing()) {
                    dialog.dismiss();
                }*/
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_CSyears, tv_cropname, crop_seasion, crop_quantity,tv_planted_date,tv_fruited_date;
        ImageView img_crop_dtls,img_crop_dele;
        CardView card_participant;


        public ViewHolder(View itemView) {
            super(itemView);
            img_crop_dele = itemView.findViewById(R.id.img_crop_dele);
            tv_CSyears = (TextView) itemView.findViewById(R.id.tv_years);
            tv_cropname = (TextView) itemView.findViewById(R.id.tv_cropname);
            crop_seasion = (TextView) itemView.findViewById(R.id.crop_seasion);
            crop_quantity = (TextView) itemView.findViewById(R.id.crop_quantity);
            card_participant = (CardView) itemView.findViewById(R.id.card_participant);
            this.tv_planted_date = (TextView) itemView.findViewById(R.id.tv_planted_date);
            this.tv_fruited_date = (TextView) itemView.findViewById(R.id.tv_fruited_date);

            // this.img_crop_dtls = (ImageView) itemView.findViewById(R.id.img_crop_dtls);
        }
    }






}
