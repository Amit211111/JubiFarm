package com.sanket.jubifarm.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sanket.jubifarm.Activity.CropSaleSubActivity;
import com.sanket.jubifarm.Modal.SaleDetailsPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CropSaleSubAdapter extends RecyclerView.Adapter<CropSaleSubAdapter.ViewHolder>{
    private Context context;
    private ArrayList<SaleDetailsPojo> arrayList;
    SqliteHelper sqliteHelper;
    String archived;

    public CropSaleSubAdapter(Context context, ArrayList<SaleDetailsPojo> arrayList,String archived) {
        this.archived = archived;
        this.context = context;
        this.arrayList = arrayList;
        sqliteHelper=new SqliteHelper(context);
    }

    @NonNull
    @Override
    public CropSaleSubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cropsaledetails, parent, false);
        CropSaleSubAdapter.ViewHolder viewholder = new CropSaleSubAdapter.ViewHolder(view);
        return viewholder;    }

    @Override
    public void onBindViewHolder(@NonNull CropSaleSubAdapter.ViewHolder holder, int position) {
        holder.tv_saledtl_years.setText(arrayList.get(position).getYear());
        holder.tv_saledtl_price.setText(arrayList.get(position).getCrop_type_price());
        holder.tv_saledtl_quantity.setText(arrayList.get(position).getQuantity());
        holder.tv_fruited_date.setText(""+arrayList.get(position).getFruited_date());
        holder.tv_planted_date.setText(""+arrayList.get(position).getPlanted_date());
        int plant_id = Integer.parseInt(arrayList.get(position).getCrop_type_subcatagory_id());
        holder.tv_saledtl_crop.setText(sqliteHelper.getNameById("crop_planning", "plant_name", "crop_type_subcatagory_id", plant_id));


        if (arrayList.get(position).getSeason_id()!=null && !arrayList.get(position).getSeason_id().equals("")) {
            holder.tv_saledtl_seassion.setText(sqliteHelper.getNameById("master", "master_name" , "caption_id", Integer.parseInt(arrayList.get(position).getSeason_id())));
        }
        if (archived.equals("1")){
            holder.img_crop_dele.setVisibility(View.GONE);
        }
        holder.img_crop_dele.setImageResource(R.drawable.ic_archive_black_24dp);
        holder.img_crop_dele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqliteHelper.updateCloseId("sale_details", Integer.parseInt(arrayList.get(position).getLocal_id().toString()),1);
                Intent intent =new Intent(context, CropSaleSubActivity.class);
                intent.putExtra("crop_sub_id",arrayList.get(position).getCrop_type_subcatagory_id());
                context.startActivity(intent);
                ((Activity)context).finish();
                Toast.makeText(context, "Archived Successfully..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_saledtl_years,tv_saledtl_price,tv_saledtl_seassion,tv_saledtl_quantity,tv_fruited_date,tv_planted_date,tv_saledtl_crop;
        // ImageView saledtl_img_crop;
        LinearLayout ll_cropsale;
        ImageView img_crop_dele;
        public ViewHolder(@NonNull View itemView) {
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
