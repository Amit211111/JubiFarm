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



        holder.sp_center_name.setText(arrayList.get(position).getSkill_center());
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_email.setText(arrayList.get(position).getEmail());
        holder.tv_mobileno.setText(arrayList.get(position).getMobileno());
        holder.tv_qualification.setText(arrayList.get(position).getQualification());
        holder.tv_stream.setText(arrayList.get(position).getTraining_stream());
        holder.tv_date_complitation.setText(arrayList.get(position).getDate_of_completion_of_training());



    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView sp_center_name,tv_name,tv_email,tv_mobileno,tv_qualification,tv_stream,tv_date_complitation;


        public ViewHolder(View view) {
            super(view);
            sp_center_name= itemView.findViewById(R.id.sp_center_name);
            tv_name= itemView.findViewById(R.id.tv_name);
            tv_email= itemView.findViewById(R.id.tv_email);
            tv_mobileno= itemView.findViewById(R.id.tv_mobileno);
            tv_qualification= itemView.findViewById(R.id.tv_qualification);
            tv_stream= itemView.findViewById(R.id.tv_stream);
            tv_date_complitation= itemView.findViewById(R.id.tv_date_complitation);

        }
    }
}
