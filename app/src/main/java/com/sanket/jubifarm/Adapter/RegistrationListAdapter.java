package com.sanket.jubifarm.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class RegistrationListAdapter extends RecyclerView.Adapter<RegistrationListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<FarmerRegistrationPojo> farmerRegistrationPojoArrayList;
    SharedPrefHelper sharedPrefHelper;
    public RegistrationListAdapter(Context context, ArrayList<FarmerRegistrationPojo> arrayList) {
        this.context = context;
        this.farmerRegistrationPojoArrayList = arrayList;
        sharedPrefHelper=new SharedPrefHelper(context);

    }

    @NonNull

    @Override
    public RegistrationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_regsitration, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txt_farmer_name.setText(farmerRegistrationPojoArrayList.get(position).getFarmer_name());
        holder.txt_farmer_location.setText(farmerRegistrationPojoArrayList.get(position).getAddress());
        holder.txt_farmer_mobile_no.setText(farmerRegistrationPojoArrayList.get(position).getMobile());

        if (!sharedPrefHelper.getString("selected_farmer","").equals("")){
            if (sharedPrefHelper.getInt("position",0)==position) {
                holder.txt_set_default.setBackground(context.getResources().getDrawable(R.drawable.farmer_selected));
            }
        }else {
            holder.txt_set_default.setBackground(context.getResources().getDrawable(R.drawable.viewdetailfarmer));

        }
        if (farmerRegistrationPojoArrayList.get(position).getProfile_photo() != null && farmerRegistrationPojoArrayList.get(position).getProfile_photo().length()>200) {
            byte[] decodedString = Base64.decode(farmerRegistrationPojoArrayList.get(position).getProfile_photo(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.img_farmer.setImageBitmap(bitmap);
        } else if (farmerRegistrationPojoArrayList.get(position).getProfile_photo().length() <=200) {
            try {
                String base64 = APIClient.IMAGE_PROFILE_URL + farmerRegistrationPojoArrayList.get(position).getProfile_photo();
                Picasso.with(context).load(base64).placeholder(R.drawable.farmer_female).into(holder.img_farmer);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.img_farmer.setImageResource(R.drawable.farmer_female);
        }



        holder.linearLayoutfarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FarmerDeatilActivity.class);
                String user_id = farmerRegistrationPojoArrayList.get(position).getUser_id();
                intent.putExtra("user_id", user_id);
                context.startActivity(intent);
            }
        });
        holder.txt_set_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.txt_set_default.setBackground(context.getResources().getDrawable(R.drawable.farmer_selected));
                sharedPrefHelper.setInt("position",position);
                String user_id = String.valueOf(farmerRegistrationPojoArrayList.get(position).getId());
                sharedPrefHelper.setString("selected_farmer",user_id);
                Intent intent = new Intent(context, HomeAcivity.class);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

    }


    @Override
    public int getItemCount() {
        return farmerRegistrationPojoArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_farmer_name, txt_farmer_location, txt_farmer_mobile_no, txt_mobileno, txt_set_default;
        public ImageView img_farmer;
        public LinearLayout linearLayoutfarmer;
//        ImageView img_farmer;
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
