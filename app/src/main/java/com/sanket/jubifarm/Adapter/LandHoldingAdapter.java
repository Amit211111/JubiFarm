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

import com.sanket.jubifarm.Activity.AddLandActivity;
import com.sanket.jubifarm.Modal.LandHoldingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.interfaces.ClickListener;
import com.sanket.jubifarm.restAPI.APIClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class LandHoldingAdapter extends RecyclerView.Adapter<LandHoldingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<LandHoldingPojo> arrayList;
    private SqliteHelper sqliteHelper;

    ClickListener clickListener;

    public LandHoldingAdapter(Context context, ArrayList<LandHoldingPojo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sqliteHelper = new SqliteHelper(context);
    }

    @NonNull
    @Override
    public LandHoldingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customlandholding, parent, false);
        LandHoldingAdapter.ViewHolder viewholder = new LandHoldingAdapter.ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull LandHoldingAdapter.ViewHolder holder, final int position) {
        holder.tv_landId.setText(arrayList.get(position).getLand_id());
        holder.tv_landArea.setText(arrayList.get(position).getArea()+" ("+sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(arrayList.get(position).getLand_unit()))+")");
        holder.tv_FarmerName.setText(sqliteHelper.getNameById("farmer_registration", "farmer_name", "id", Integer.parseInt(arrayList.get(position).getFarmer_id())));
        holder.tv_totalpalnt.setText(sqliteHelper.getTotalPlantbyid(arrayList.get(position).getId()));
        holder.tv_landName.setText((arrayList.get(position).getLand_name()));

        if (arrayList.get(position).getImage() != null && arrayList.get(position).getImage().length()>200) {
            byte[] decodedString = Base64.decode(arrayList.get(position).getImage(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.img_land.setImageBitmap(bitmap);
        } else if (arrayList.get(position).getImage().length() <=200) {
            try {
                String url = APIClient.LAND_IMAGE_URL + arrayList.get(position).getImage();
                Picasso.with(context).load(url).placeholder(R.drawable.land).into(holder.img_land);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.img_land.setImageResource(R.drawable.land);
        }

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onViewLandClick(position);
            }
        });

        holder.tv_editland.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onEditLandClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_totalpalnt, tv_FarmerName, tv_landArea, tv_landId, tv_view_land, tv_editland,tv_landName;
        ImageView img_land;
        LinearLayout ll_main;
        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_totalpalnt = (TextView) itemView.findViewById(R.id.tv_totalpalnt);
            this.tv_FarmerName = (TextView) itemView.findViewById(R.id.tv_FarmerName);
            this.tv_landArea = (TextView) itemView.findViewById(R.id.tv_landArea);
            this.ll_main = (LinearLayout) itemView.findViewById(R.id.ll_main);
            this.tv_landId = (TextView) itemView.findViewById(R.id.tv_landId);
            this.tv_view_land = (TextView) itemView.findViewById(R.id.tv_view_land);
            this.tv_editland = (TextView) itemView.findViewById(R.id.tv_editland);
            this.tv_landName = (TextView) itemView.findViewById(R.id.tv_landName);
            this.img_land = (ImageView) itemView.findViewById(R.id.img_land);
        }
    }

    public void onViewLandClick(ClickListener listener) {
        this.clickListener = listener;
    }

    public void onEditLandClick(ClickListener listener) {
        this.clickListener = listener;
    }
}
