package com.sanket.jubifarm.Adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanket.jubifarm.Modal.VisitPlantModel;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.interfaces.ClickListener;
import com.sanket.jubifarm.restAPI.APIClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class VisitPlantAdapter extends RecyclerView.Adapter<VisitPlantAdapter.ViewHolder> {
    private Context context;
    private ArrayList<VisitPlantModel> arrayList;
    private SqliteHelper sqliteHelper;
    ClickListener clickListener;
    boolean isSelectedAll;
    HashMap<Integer,String> values =new HashMap<>();
    public HashMap<Integer,String> getCheckedValues(){
        return values;
    }

    public VisitPlantAdapter(Context context, ArrayList<VisitPlantModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sqliteHelper=new SqliteHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_visit_plant_adapter, parent, false);
        return new ViewHolder(view);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getPlant_name());
        holder.tv_landId.setText(sqliteHelper.getLandIDbyid(arrayList.get(position).getLand_id()));
        holder.tv_Plantid.setText(arrayList.get(position).getPlant_id());
        holder.namefarmer.setText(sqliteHelper.getNameById("farmer_registration", "farmer_name", "id", Integer.parseInt(arrayList.get(position).getFarmer_id())));
        holder.tv_category_name.setText(sqliteHelper.getNameById("crop_type_catagory_language", "name", "id", Integer.parseInt(arrayList.get(position).getCrop_type_catagory_id())));
        holder.tv_sub_category_name.setText(sqliteHelper.getNameById("crop_type_sub_catagory_language", "name", "id", Integer.parseInt(arrayList.get(position).getCrop_type_subcatagory_id())));
        holder.tv_geoCoordinate.setText(arrayList.get(position).getLatitude()+", "+arrayList.get(position).getLongitude());

       if (!isSelectedAll)
            holder.checkBox.setChecked(false);
        else
         holder.checkBox.setChecked(true);

        if (arrayList.get(position).getPlant_image() != null && arrayList.get(position).getPlant_image().length()>200) {
            byte[] decodedString = Base64.decode(arrayList.get(position).getPlant_image(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.img_tree.setImageBitmap(bitmap);
        } else if (arrayList.get(position).getPlant_image().length() <=200) {
            try {
                String url = APIClient.IMAGE_PLANTS_URL + arrayList.get(position).getPlant_image();
                Picasso.with(context).load(url).placeholder(R.drawable.land).into(holder.img_tree);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.img_tree.setImageResource(R.drawable.land);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    clickListener.onCheckBoxClick(position);
                    /*values.put(Integer.valueOf(arrayList.get(position).getId()), "crop_planing_id");
                    values.put(Integer.valueOf(arrayList.get(position).getFarmer_id()), "farmer_id");
                    values.put(Integer.valueOf(arrayList.get(position).getCrop_type_catagory_id()), "crop_type_catagory_id");
                    values.put(Integer.valueOf(arrayList.get(position).getCrop_type_subcatagory_id()), "crop_type_subcatagory_id");
                    values.put(Integer.parseInt(arrayList.get(position).getPlant_image()), "plant_image");*/
                } else {
                    /*values.remove(arrayList.get(position).getId());
                    values.remove(arrayList.get(position).getFarmer_id());
                    values.remove(arrayList.get(position).getCrop_type_catagory_id());
                    values.remove(arrayList.get(position).getCrop_type_subcatagory_id());
                    values.remove(arrayList.get(position).getPlant_image());*/
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_Plantid,name,tv_landId,
                tv_geoCoordinate,namefarmer, tv_category_name, tv_sub_category_name;
        ImageView img_tree;
        LinearLayout linearLayoutsubcrop;
        public CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutsubcrop = itemView.findViewById(R.id.linearLayoutsubcrop);
            tv_Plantid = itemView.findViewById(R.id.tv_Plantid);
            tv_landId = itemView.findViewById(R.id.tv_landId);
            name = itemView.findViewById(R.id.name);
            namefarmer = itemView.findViewById(R.id.namefarmer);
            tv_geoCoordinate = itemView.findViewById(R.id.tv_geoCoordinate);
            tv_landId = itemView.findViewById(R.id.tv_landId);
            img_tree = itemView.findViewById(R.id.img_tree);
            tv_category_name=itemView.findViewById(R.id.tv_category_name);
            tv_sub_category_name=itemView.findViewById(R.id.tv_sub_category_name);
            checkBox=itemView.findViewById(R.id.checkBox);
        }
    }

    public void onCheckBoxClick(ClickListener listener) {
        this.clickListener=listener;
    }

}
