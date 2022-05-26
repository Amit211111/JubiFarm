package com.sanket.jubifarm.Livelihood.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Livelihood.Model.SkillTrackingPojo;
import com.sanket.jubifarm.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class AdapterSkillCenter extends RecyclerView.Adapter<AdapterSkillCenter.ViewHolder>
{
    Context context;
    ArrayList<SkillTrackingPojo> arrayList;

    public AdapterSkillCenter(Context context, ArrayList<SkillTrackingPojo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AdapterSkillCenter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_skill_center_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSkillCenter.ViewHolder holder, int position) {



        holder.center_name.setText(arrayList.get(position).getSkill());
        holder.address.setText(arrayList.get(position).getAddress());
        holder.contact_person.setText(arrayList.get(position).getContact());
        holder.mobileno.setText(arrayList.get(position).getMobileno());
        holder.latitude.setText(arrayList.get(position).getLatitude());
        holder.longitude.setText(arrayList.get(position).getLongitude());
        holder.state.setText(arrayList.get(position).getState());
        holder.district.setText(arrayList.get(position).getDistrict());
        holder.village.setText(arrayList.get(position).getVillage());


    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView center_name,address,contact_person,mobileno,latitude,longitude,state,district,village,pincode;


        public ViewHolder(View view) {
            super(view);
            center_name= itemView.findViewById(R.id.center_name);
            address= itemView.findViewById(R.id.address);
            contact_person= itemView.findViewById(R.id.contact_person);
            mobileno= itemView.findViewById(R.id.mobileno);
            latitude= itemView.findViewById(R.id.latitude);
            longitude= itemView.findViewById(R.id.longitude);
            state= itemView.findViewById(R.id.state);
            district= itemView.findViewById(R.id.district);
            village= itemView.findViewById(R.id.village);
            pincode= itemView.findViewById(R.id.pincode);


        }
    }
}
