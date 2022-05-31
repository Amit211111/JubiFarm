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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanket.jubifarm.Livelihood.LandHoldingDetailsView;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Livelihood.Model.SkillTrackingPojo;
import com.sanket.jubifarm.Livelihood.MonitoringView;
import com.sanket.jubifarm.Livelihood.SkillTrackingListView;
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
    public void onBindViewHolder(@NonNull AdapterSkillCenter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {



        holder.et_name.setText(arrayList.get(position).getName());
        holder.et_email.setText(arrayList.get(position).getEmail());
        holder.et_skill.setText(arrayList.get(position).getSkill_center());

        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SkillTrackingListView.class);
                intent.putExtra("id",arrayList.get(position).getId());
                intent.putExtra("namee",arrayList.get(position).getName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                context.startActivity(intent);
            }
        });

        holder.btn_monitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(context, MonitoringView.class);
                intent1.putExtra("id",arrayList.get(position).getId());
                intent1.putExtra("namee",arrayList.get(position).getName());
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView et_name,et_email,et_skill;
        Button btn_monitoring,btn_view;


        public ViewHolder(View view) {
            super(view);
            btn_view= itemView.findViewById(R.id.btn_view);
            btn_monitoring= itemView.findViewById(R.id.btn_monitoring);
            et_name= itemView.findViewById(R.id.et_name);
            et_email= itemView.findViewById(R.id.et_email);
            et_skill= itemView.findViewById(R.id.et_skill);


        }
    }
}
