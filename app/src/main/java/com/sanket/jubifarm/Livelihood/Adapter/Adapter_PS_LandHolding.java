package com.sanket.jubifarm.Livelihood.Adapter;

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

import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Livelihood.RecycleDetailsView;
import com.sanket.jubifarm.Livelihood.TrainningSurveyForm;
import com.sanket.jubifarm.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class Adapter_PS_LandHolding extends RecyclerView.Adapter<Adapter_PS_LandHolding.ViewHolder>
{

    Context context;
    ArrayList<PSLandHoldingPojo> arrayList;

    public Adapter_PS_LandHolding(Context context, ArrayList<PSLandHoldingPojo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Adapter_PS_LandHolding.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_ps_landholding, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_PS_LandHolding.ViewHolder holder, int position) {


//
        if (arrayList.get(position).getBtn_upload_land() != null && arrayList.get(position).getBtn_upload_land().length() > 200) {
            byte[] decodedString = Base64.decode(arrayList.get(position).getBtn_upload_land(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.img_land.setImageBitmap(bitmap);
        } else {
            holder.img_land.setImageResource(R.drawable.ic_baseline_add_24);
        }

        holder.farmer_selection.setText(arrayList.get(position).getFarmer_Selection());
        holder.land_area.setText(arrayList.get(position).getLand_Area());
        holder.land_name.setText(arrayList.get(position).getLand_Name());

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecycleDetailsView.class);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView Land_id,land_name,land_area,farmer_selection,total_plant;
        ImageView img_land;
        LinearLayout ll_main;


        public ViewHolder(View view) {
            super(view);
            land_name= itemView.findViewById(R.id.land_name);
            Land_id= itemView.findViewById(R.id.Land_id);
            land_area= itemView.findViewById(R.id.land_area);
            farmer_selection= itemView.findViewById(R.id.farmer_selection);
            total_plant= itemView.findViewById(R.id.total_plant);
            img_land= itemView.findViewById(R.id.img_land);
            ll_main= itemView.findViewById(R.id.ll_main);

        }
    }
}
