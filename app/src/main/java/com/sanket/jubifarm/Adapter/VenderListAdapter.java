package com.sanket.jubifarm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanket.jubifarm.Activity.SubCropPlaninigActivity;
import com.sanket.jubifarm.Modal.SupplierRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.interfaces.ClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class VenderListAdapter extends RecyclerView.Adapter<VenderListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SupplierRegistrationPojo> arrayList;
    private SqliteHelper sqliteHelper;
    private String categoryId="";
    private String farmerLogin="";
    boolean isSelectedAll;


    HashMap<Integer,String> values =new HashMap<>();
    public HashMap<Integer,String> getCheckedValues(){
        return values;
    }

    public VenderListAdapter(Context context, ArrayList<SupplierRegistrationPojo> arrayList,
                             String category_id, String farmer_login) {
        this.context = context;
        this.arrayList = arrayList;
        sqliteHelper=new SqliteHelper(context);
        categoryId=category_id;
        farmerLogin=farmer_login;
    }

    @NonNull
    @Override
    public VenderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_vender, parent, false);
        VenderListAdapter.ViewHolder viewholder = new VenderListAdapter.ViewHolder(view);
        return viewholder;
    }
    public void selectAll(){
        isSelectedAll=true;
        notifyDataSetChanged();
    }
    public void unselectall(){
        isSelectedAll=false;
        notifyDataSetChanged();
    }



    @Override
    public void onBindViewHolder(@NonNull VenderListAdapter.ViewHolder holder, final int position) {
        holder.tv_VenderName.setText(": " + arrayList.get(position).getName());
        holder.tv_mobile.setText(": " + arrayList.get(position).getMobile());
        String category_name = sqliteHelper.getNameById("master", "master_name", "caption_id", Integer.parseInt(categoryId));
        holder.tv_tCategory.setText(": " + category_name);

        if (farmerLogin.equals("farmer_login")) {
            holder.checkBox.setVisibility(View.GONE);
        }

        if (!isSelectedAll)
            holder.checkBox.setChecked(false);
        else
            holder.checkBox.setChecked(true);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                  values.put(Integer.valueOf(arrayList.get(position).getId()), "checked");
                } else {
                    values.remove(arrayList.get(position).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_VenderName, tv_tCategory, tv_mobile;
        LinearLayout ll_croplaning;
        CheckBox checkBox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_VenderName = (TextView) itemView.findViewById(R.id.tv_VenderName);
            this.tv_tCategory = (TextView) itemView.findViewById(R.id.tv_tCategory);
            this.tv_mobile = (TextView) itemView.findViewById(R.id.tv_mobile);
            this.ll_croplaning = (LinearLayout) itemView.findViewById(R.id.ll_croplaning);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }
}
