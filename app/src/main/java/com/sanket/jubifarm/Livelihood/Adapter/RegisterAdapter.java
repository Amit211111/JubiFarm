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
import com.sanket.jubifarm.Livelihood.PS_FarmerDetailsActivity;
//import com.sanket.jubifarm.Livelihood.PS_Farmer_details;
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
        if (arrayList.get(position).getFarmer_image() != null && arrayList.get(position).getFarmer_image().length() > 200) {
            byte[] decodedString = Base64.decode(arrayList.get(position).getFarmer_image(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.img_farmer.setImageBitmap(bitmap);
        } else {
            holder.img_farmer.setImageResource(R.drawable.farmer_female);
        }

        holder.txt_farmer_name.setText(arrayList.get(position).getFarmer_name());
        holder.txt_farmer_location.setText(arrayList.get(position).getAddress());
        //holder.text_mobile.setText(arrayList.get(position).getMobile());
        holder.txt_mobilenoo.setText(arrayList.get(position).getMobile());

        holder.ll_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PS_FarmerDetailsActivity.class);
                intent.putExtra("farmerId",arrayList.get(position).getLocal_id());
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_farmer_name, txt_farmer_location, txt_set_default,txt_mobilenoo;
        LinearLayout linearLayoutfarmer,ll_holder;
        ImageView img_farmer,IV_profile;
        TextView tv_EditDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_mobilenoo=(TextView) itemView.findViewById(R.id.txt_mobilenoo);
            this.txt_farmer_name = (TextView) itemView.findViewById(R.id.txt_farmer_name);
            this.txt_farmer_location = (TextView) itemView.findViewById(R.id.txt_farmer_location);
            this.txt_set_default = (TextView) itemView.findViewById(R.id.txt_set_default);
            this.linearLayoutfarmer = (LinearLayout) itemView.findViewById(R.id.linearLayoutfarmer);
            this.ll_holder = (LinearLayout) itemView.findViewById(R.id.ll_holder);
            //this.IV_profile = (ImageView) itemView.findViewById(R.id.IV_profile);
            this.img_farmer = (ImageView) itemView.findViewById(R.id.img_farmer);

        }
    }
}
