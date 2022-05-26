package com.sanket.jubifarm.Livelihood.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.sanket.jubifarm.Activity.FarmerDeatilActivity;
import com.sanket.jubifarm.Activity.HomeAcivity;
import com.sanket.jubifarm.Adapter.AddCropSalesAdapter;
import com.sanket.jubifarm.Adapter.RegistrationListAdapter;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Livelihood.Model.ParyavaranSakhiRegistrationPojo;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class RegisterAdapter extends RecyclerView.Adapter<RegisterAdapter.ViewHolder>
{
    Context context;
    ArrayList<ParyavaranSakhiRegistrationPojo> arrayList;

    public RegisterAdapter(Context context, ArrayList<ParyavaranSakhiRegistrationPojo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull

    @Override
    public RegisterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.farmer_registration_custom, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RegisterAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
//        if (arrayList.get(position).get() != null && arrayList.get(position).getFarmer_image().length() > 200) {
//            byte[] decodedString = Base64.decode(arrayList.get(position).getFarmer_image(), Base64.NO_WRAP);
//            InputStream inputStream = new ByteArrayInputStream(decodedString);
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//            holder.img_farmer.setImageBitmap(bitmap);
//        } else {
//            holder.img_farmer.setImageResource(R.drawable.ic_baseline_add_24);
//        }

        holder.txt_farmer_name.setText(arrayList.get(position).getFarmer_name());
        holder.txt_farmer_location.setText(arrayList.get(position).getAddress());
        holder.txt_farmer_mobile_no.setText(arrayList.get(position).getMobile());


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
         TextView txt_farmer_name, txt_farmer_location, txt_farmer_mobile_no, txt_mobileno, txt_set_default;
        LinearLayout linearLayoutfarmer;
        ImageView img_farmer;
        TextView tv_EditDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_farmer_name = (TextView) itemView.findViewById(R.id.txt_farmer_name);
            this.txt_farmer_location = (TextView) itemView.findViewById(R.id.txt_farmer_location);
            this.txt_mobileno = (TextView) itemView.findViewById(R.id.txt_mobileno);
            this.txt_set_default = (TextView) itemView.findViewById(R.id.txt_set_default);
            this.txt_farmer_mobile_no = (TextView) itemView.findViewById(R.id.txt_farmer_mobile_no);
            this.linearLayoutfarmer = (LinearLayout) itemView.findViewById(R.id.linearLayoutfarmer);

            this.img_farmer = (ImageView) itemView.findViewById(R.id.img_farmer);
        }
    }
}
