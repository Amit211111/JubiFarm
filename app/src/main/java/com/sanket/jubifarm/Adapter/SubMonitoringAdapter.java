package com.sanket.jubifarm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanket.jubifarm.Activity.AddPlantGrowthActivity;
import com.sanket.jubifarm.Activity.CropDetailActivity;
import com.sanket.jubifarm.Modal.PlantSubCategoryPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class SubMonitoringAdapter extends RecyclerView.Adapter<SubMonitoringAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PlantSubCategoryPojo> arrayList;
    int id;
    private SqliteHelper sqliteHelper;
    // RecyclerView recyclerView;
    public SubMonitoringAdapter(Context context, ArrayList<PlantSubCategoryPojo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sqliteHelper=new SqliteHelper(context);
    }

    @NonNull

    @Override
    public SubMonitoringAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_monitoring, parent, false);
        SubMonitoringAdapter.ViewHolder viewholder = new SubMonitoringAdapter.ViewHolder(view);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull SubMonitoringAdapter.ViewHolder holder, final int position) {
        holder.SCMname.setText(arrayList.get(position).getName());
        String landId = arrayList.get(position).getLand_id();
        holder.SCMtv_landId.setText(sqliteHelper.getNameById("land_holding", "land_id", "id", Integer.parseInt(landId)));
        holder.tv_SCMPlantid.setText(arrayList.get(position).getPlant_id());
        holder.tv_SCMgeoCoordinate.setText(arrayList.get(position).getLatitude()+", "+arrayList.get(position).getLongitude());
        if (arrayList.get(position).getPlant_image() != null && arrayList.get(position).getPlant_image().length()>200) {
            byte[] decodedString = Base64.decode(arrayList.get(position).getPlant_image(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.img_tree.setImageBitmap(bitmap);
        } else if (arrayList.get(position).getPlant_image().length() <=200) {
            try {
                String url = APIClient.IMAGE_PLANTS_URL + arrayList.get(position).getPlant_image();
                Picasso.with(context).load(url).placeholder(R.drawable.land).into(holder.img_tree);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.img_tree.setImageResource(R.drawable.land);
        }
        holder.llsubmonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AddPlantGrowthActivity.class);

                String id = String.valueOf(arrayList.get(position).getId());
                intent.putExtra("crop_planing_id" ,id);
                intent.putExtra("farmer_id",arrayList.get(position).getFarmer_id());
                intent.putExtra("crop_type_catagory_id",arrayList.get(position).getCrop_type_catagory_id());
                intent.putExtra("crop_type_subcatagory_id",arrayList.get(position).getCrop_type_subcatagory_id());
                intent.putExtra("total_tree",arrayList.get(position).getTotal_tree());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_SCMPlantid,SCMname,SCMtv_landId,tv_SCMgeoCoordinate;
        LinearLayout llsubmonitoring;
        ImageView img_tree;
        public ViewHolder(View itemView) {
            super(itemView);

            this.llsubmonitoring = (LinearLayout) itemView.findViewById(R.id.llsubmonitoring);
            this.tv_SCMPlantid = (TextView) itemView.findViewById(R.id.tv_SCMPlantid);
            this.SCMname = (TextView) itemView.findViewById(R.id.SCMname);
            this.tv_SCMgeoCoordinate = (TextView) itemView.findViewById(R.id.tv_SCMgeoCoordinate);
            this.SCMtv_landId = (TextView) itemView.findViewById(R.id.SCMtv_landId);
            this.img_tree = (ImageView) itemView.findViewById(R.id.img_tree);

        }
    }

}
