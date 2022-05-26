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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Activity.CropSaleDetails;
import com.sanket.jubifarm.Activity.CropSaleSubActivity;
import com.sanket.jubifarm.Activity.PostPlantationAcivity;
import com.sanket.jubifarm.Activity.SubCropPlaninigActivity;
import com.sanket.jubifarm.Modal.CropPlaningPojo;
import com.sanket.jubifarm.Modal.CropPlanningModal;
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

public class CropSaleDetailsAdapter extends RecyclerView.Adapter<CropSaleDetailsAdapter.ViewHolder> {
        private Context context;
        private ArrayList<SaleDetailsPojo> arrayList;
        SqliteHelper sqliteHelper;
        ProgressDialog dialog;
        SharedPrefHelper sharedPrefHelper;
        public CropSaleDetailsAdapter(Context context, ArrayList<SaleDetailsPojo> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
            sqliteHelper=new SqliteHelper(context);
            dialog=new ProgressDialog(context);
            sharedPrefHelper=new SharedPrefHelper(context);
        }

        @NonNull

        @Override
        public CropSaleDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.cropsaledetails, parent, false);
            CropSaleDetailsAdapter.ViewHolder viewholder = new CropSaleDetailsAdapter.ViewHolder(view);
            return viewholder;
        }




        @Override
        public void onBindViewHolder(@NonNull CropSaleDetailsAdapter.ViewHolder holder, final int position) {
            holder.tv_saledtl_years.setText(arrayList.get(position).getYear());
            int plant_id = Integer.parseInt(arrayList.get(position).getCrop_type_subcatagory_id());
            holder.tv_saledtl_crop.setText(sqliteHelper.getNameById("crop_planning", "plant_name", "crop_type_subcatagory_id", plant_id));
            holder.tv_saledtl_price.setText(arrayList.get(position).getCrop_type_price());
            holder.tv_saledtl_quantity.setText(arrayList.get(position).getQuantity() + " "+ sqliteHelper.getNameById("master", "master_name" , "caption_id", Integer.parseInt(arrayList.get(position).getQuanity_unit_id())));

            if(!arrayList.get(position).getFruited_date().equals("")) {
                holder.tv_fruited_date.setText("" + arrayList.get(position).getFruited_date());
            }else{
                holder.tv_fruited_date.setText(" N/A");
            }

            holder.tv_planted_date.setText(""+arrayList.get(position).getPlanted_date());

            if (arrayList.get(position).getSeason_id()!=null && !arrayList.get(position).getSeason_id().equals("")) {
                holder.tv_saledtl_seassion.setText(sqliteHelper.getNameById("master", "master_name" , "caption_id", Integer.parseInt(arrayList.get(position).getSeason_id())));
            }

            holder.img_crop_dele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SaleDetailsPojo saleDetailsPojo=new SaleDetailsPojo();
                    saleDetailsPojo.setTable_name("sale_details");
                    saleDetailsPojo.setId(arrayList.get(position).getId());
                    saleDetailsPojo.setRole_id(sharedPrefHelper.getString("role_id",""));
                    Gson gson = new Gson();
                    String data = gson.toJson(saleDetailsPojo);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);
                    deletesale(body);
                }
            });

            holder.ll_cropsale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, CropSaleSubActivity.class);
                    intent.putExtra("crop_sub_id",arrayList.get(position).getCrop_type_subcatagory_id());
                    context.startActivity(intent);
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
                    dialog.dismiss();
                    if (Integer.valueOf(status) == 1) {
                        String message = jsonObject.optString("message");
                        String training_attendance_id = jsonObject.optString("id");
                        sqliteHelper.deleteTableSale("sale_details", training_attendance_id);
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

        public TextView tv_saledtl_years,tv_saledtl_price,tv_saledtl_seassion,tv_saledtl_quantity,tv_fruited_date,tv_planted_date, tv_saledtl_crop;
       // ImageView saledtl_img_crop;
        LinearLayout ll_cropsale;
        ImageView img_crop_dele;
        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_saledtl_years = (TextView) itemView.findViewById(R.id.tv_saledtl_years);
            this.tv_saledtl_price = (TextView) itemView.findViewById(R.id.tv_saledtl_price);
            this.tv_saledtl_seassion = (TextView) itemView.findViewById(R.id.tv_saledtl_seassion);
            this.tv_saledtl_quantity = (TextView) itemView.findViewById(R.id.tv_saledtl_quantity);
            this.ll_cropsale = (LinearLayout) itemView.findViewById(R.id.ll_cropsale);
            this.img_crop_dele = (ImageView) itemView.findViewById(R.id.img_crop_dele);
            this.tv_planted_date = (TextView) itemView.findViewById(R.id.tv_planted_date);
            this.tv_fruited_date = (TextView) itemView.findViewById(R.id.tv_fruited_date);
            this.tv_saledtl_crop = (TextView) itemView.findViewById(R.id.tv_saledtl_crop);

        }
    }

}
