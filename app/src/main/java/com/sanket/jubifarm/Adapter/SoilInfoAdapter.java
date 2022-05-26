package com.sanket.jubifarm.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanket.jubifarm.Activity.UpdatedSoilInFo;
import com.sanket.jubifarm.Modal.SoilPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;

import java.util.ArrayList;


public class SoilInfoAdapter extends RecyclerView.Adapter<SoilInfoAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SoilPojo> arrayList;
    SharedPrefHelper sharedPrefHelper;
    String id;

    public SoilInfoAdapter(Context context, ArrayList<SoilPojo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sharedPrefHelper = new SharedPrefHelper(context);

    }

    @NonNull

    @Override
    public SoilInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_soil_in_fo, parent, false);
        SoilInfoAdapter.ViewHolder viewholder = new SoilInfoAdapter.ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull SoilInfoAdapter.ViewHolder holder, final int position) {
        id = (arrayList.get(position).getId());
        holder.txt_date.setText(arrayList.get(position).getSoil_updated_date());


        holder.linearLayoutChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdatedSoilInFo.class);
                String id = arrayList.get(position).getId();
                intent.putExtra("id", id);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_date;
        LinearLayout linearLayoutChild;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            this.linearLayoutChild = (LinearLayout) itemView.findViewById(R.id.linearLayoutChild);
            // this.img_farmer = (ImageView) itemView.findViewById(R.id.img_farmer);
        }
    }
}
