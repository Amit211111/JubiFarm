package com.sanket.jubifarm.Livelihood.Adapter;

import android.annotation.SuppressLint;
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
import com.sanket.jubifarm.Livelihood.LandHoldingDetailsView;
import com.sanket.jubifarm.Livelihood.NeemPlantation;
import com.sanket.jubifarm.Livelihood.PS_NeemPlantationList;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class Adapter_PS_LandHolding extends RecyclerView.Adapter<Adapter_PS_LandHolding.ViewHolder>
{

    Context context;
    ArrayList<PSLandHoldingPojo> arrayList;
    private SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    String screenType = "";

    public Adapter_PS_LandHolding(Context context, ArrayList<PSLandHoldingPojo> arrayList, String screenType) {
        this.context = context;
        this.arrayList = arrayList;
        this.screenType = screenType;
        sqliteHelper=new SqliteHelper(context);
        sharedPrefHelper = new SharedPrefHelper(context);

    }

    @NonNull
    @Override
    public Adapter_PS_LandHolding.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_ps_landholding, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_PS_LandHolding.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


//
        if (arrayList.get(position).getLand_image() != null && arrayList.get(position).getLand_image().length() > 200) {
            byte[] decodedString = Base64.decode(arrayList.get(position).getLand_image(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.img_land.setImageBitmap(bitmap);
        } else {
            holder.img_land.setImageResource(R.drawable.apple);
        }
        holder.Land_id.setText(arrayList.get(position).getLand_id());
       // holder.farmer_selection.setText(arrayList.get(position).getFarmer_Selection());
      //holder.land_area.setText(arrayList.get(position).getLand_Area());
       // holder.land_area.setText(arrayList.get(position).getLand_Area()+" ("+sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(arrayList.get(position).getLand_unit()))+")");
        holder.land_area.setText(arrayList.get(position).getLand_area()+" ("+sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(arrayList.get(position).getLand_unit()))+")");
        holder.farmer_name.setText(sqliteHelper.getNameById("ps_farmer_registration", "farmer_name", "local_id", Integer.parseInt(arrayList.get(position).getFarmer_id())));
        holder.land_name.setText(arrayList.get(position).getLand_name());



        screenType = sharedPrefHelper.getString("prayawran_screenType", "");

      holder.ll_land.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              if(screenType.equals("land")) {
                  Intent intent = new Intent(context,LandHoldingDetailsView.class);
                  intent.putExtra("land_Id",arrayList.get(position).getLand_id());
                  intent.putExtra("farmerId",arrayList.get(position).getFarmer_id());
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  context.startActivity(intent);
              } else {
                  sharedPrefHelper.setString("prayawran_screenType", "monitoring");
                  sharedPrefHelper.setString("plantation_screenType", "monitoring");
                  Intent intent = new Intent(context, PS_NeemPlantationList.class);
                  context.startActivity(intent);
              }
          }
      });

    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView Land_id,land_name,land_area,farmer_name,total_plant;
        ImageView img_land;
        LinearLayout ll_main,ll_land;


        public ViewHolder(View view) {
            super(view);

            land_name= itemView.findViewById(R.id.land_name);
            Land_id= itemView.findViewById(R.id.Land_id);
            land_area= itemView.findViewById(R.id.land_area);
            farmer_name= itemView.findViewById(R.id.farmer_name);
            total_plant= itemView.findViewById(R.id.total_plant);
            img_land= itemView.findViewById(R.id.img_land);
            ll_land= itemView.findViewById(R.id.ll_land);
            ll_main= itemView.findViewById(R.id.ll_main);

        }
    }
}
