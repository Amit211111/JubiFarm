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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.Livelihood.NeemMonitoring;
import com.sanket.jubifarm.Livelihood.NeemPlantationViewActivity;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class NeemPlantationAdapter extends RecyclerView.Adapter<NeemPlantationAdapter.ViewHolder> {

    Context context;
    ArrayList<PSNeemPlantationPojo> psNeemPlantationPojos;
    public String screenType = "";
    SharedPrefHelper sharedPrefHelper;

    public NeemPlantationAdapter(Context context, ArrayList<PSNeemPlantationPojo> psNeemPlantationPojos, String screenType)
    {
        this.context = context;
        this.psNeemPlantationPojos = psNeemPlantationPojos;
        this.screenType = screenType;
        sharedPrefHelper = new SharedPrefHelper(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.neemplantationresource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NeemPlantationAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.Land_id.setText(psNeemPlantationPojos.get(position).getLand_id());
        holder.Neem_Id.setText(psNeemPlantationPojos.get(position).getNeem_id());
        holder.geo_cordinate.setText(psNeemPlantationPojos.get(position).getLatitude() + ", " +psNeemPlantationPojos.get(position).getLongitude());
        if(psNeemPlantationPojos.get(position).getNeem_plantation_image() != null && psNeemPlantationPojos.get(position).getNeem_plantation_image().length() > 200)
        {
            byte[] decodedString = android.util.Base64.decode(psNeemPlantationPojos.get(position).getNeem_plantation_image(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.img_setimage.setImageBitmap(bitmap);
        }else {
            holder.img_setimage.setImageResource(R.drawable.neem);
        }

        screenType = sharedPrefHelper.getString("plantation_screenType", "");

        holder.cv_student_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(screenType.equals("view")) {
                    Intent intent = new Intent(context, NeemPlantationViewActivity.class);
                    intent.putExtra("id",psNeemPlantationPojos.get(position).getLocal_id());
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, NeemMonitoring.class);
                    intent.putExtra("id",psNeemPlantationPojos.get(position).getLocal_id());
                    intent.putExtra("landId",psNeemPlantationPojos.get(position).getLand_id());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return psNeemPlantationPojos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_setimage;
        CardView cv_student_listing;
        TextView Land_id, geo_cordinate, Neem_Id;
        public ViewHolder(View Itemview) {
            super(Itemview);
            cv_student_listing = Itemview.findViewById(R.id.cv_student_listing);
            img_setimage = Itemview.findViewById(R.id.img_setimage);
            Land_id = Itemview.findViewById(R.id.Land_id);
            Neem_Id = Itemview.findViewById(R.id.Neem_Id);
            geo_cordinate = Itemview.findViewById(R.id.geo_cordinate);
        }
    }
}
