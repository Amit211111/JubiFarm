package com.sanket.jubifarm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanket.jubifarm.Activity.FarmerDeatilActivity;
import com.sanket.jubifarm.Activity.SubCropPlaninigActivity;
import com.sanket.jubifarm.Activity.AddPlantAcivity;
import com.sanket.jubifarm.Modal.CropPlaningPojo;
import com.sanket.jubifarm.Modal.CropPlanningModal;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;


    public class CropPlanningAdapter extends RecyclerView.Adapter<CropPlanningAdapter.ViewHolder> {
        private Context context;
        private ArrayList<CropPlaningPojo> arrayList;
        SqliteHelper sqliteHelper;
        String land_id="";
        String screen_type;
        String farmer_id;
        SharedPrefHelper sharedPrefHelper;
        // RecyclerView recyclerView;
        public CropPlanningAdapter(Context context, ArrayList<CropPlaningPojo> arrayList,String land_id,String screen_type,String farmer_id) {
            this.context = context;
            this.arrayList = arrayList;
            this.screen_type = screen_type;
            this.land_id = land_id;
            this.farmer_id = farmer_id;
            sqliteHelper=new SqliteHelper(context);
            sharedPrefHelper=new SharedPrefHelper(context);

        }

        @NonNull

        @Override
        public CropPlanningAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.cropplanninglist, parent, false);
            CropPlanningAdapter.ViewHolder viewholder = new CropPlanningAdapter.ViewHolder(view);
            return viewholder;
        }


        @Override
        public void onBindViewHolder(@NonNull CropPlanningAdapter.ViewHolder holder, final int position) {
            //Id=arrayList.get(position).getId();
            String language = sharedPrefHelper.getString("languageID","");
            if (arrayList.get(position).getCrop_type_catagory_id().equals("1")){
                holder.img_tree.setImageResource(R.drawable.hoticulture);

            }else if (arrayList.get(position).getCrop_type_catagory_id().equals("2")){
                holder.img_tree.setImageResource(R.drawable.pulses);

            }else if (arrayList.get(position).getCrop_type_catagory_id().equals("3")){
                holder.img_tree.setImageResource(R.drawable.cereals);

            }else if (arrayList.get(position).getCrop_type_catagory_id().equals("4")){
                holder.img_tree.setImageResource(R.drawable.fruits);

            }else if (arrayList.get(position).getCrop_type_catagory_id().equals("5")){
                holder.img_tree.setImageResource(R.drawable.medicinal_plants);

            }else if (arrayList.get(position).getCrop_type_catagory_id().equals("6")){
                holder.img_tree.setImageResource(R.drawable.timber);

            }else if (arrayList.get(position).getCrop_type_catagory_id().equals("7")){
                holder.img_tree.setImageResource(R.drawable.vegetables);

            }





            if (arrayList.get(position).getCrop_type_catagory_id()!=null && !arrayList.get(position).getTotal_plant().equals("0")){
                holder.tv_treeName.setText(sqliteHelper.getCategotyName(Integer.parseInt(arrayList.get(position).getCrop_type_catagory_id()),language));
                holder.tv_treeCount.setText(arrayList.get(position).getTotal_plant());

            }

            holder.ll_croplaning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, SubCropPlaninigActivity.class);
                    intent.putExtra("cropCategory_id", arrayList.get(position).getCrop_type_catagory_id());
                    intent.putExtra("land_id", arrayList.get(position).getLand_id());
                    intent.putExtra("land_id_id",land_id);
                    intent.putExtra("screen_type",screen_type);
                    intent.putExtra("fromcropPlaniong","from_cropPlaning");
                    intent.putExtra("farmer_id",farmer_id);
                    context.startActivity(intent);
                }
            });
            holder.tv_CSaddplant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, AddPlantAcivity.class);
                    context.startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_treeName,tv_treeCount,tv_CSaddplant;
            LinearLayout ll_croplaning;
            ImageView img_tree;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);


                this.tv_treeName = (TextView) itemView.findViewById(R.id.tv_treeName);
                this.tv_treeCount = (TextView) itemView.findViewById(R.id.tv_treeCount);
                this.ll_croplaning = (LinearLayout) itemView.findViewById(R.id.ll_croplaning);
                this.img_tree = (ImageView) itemView.findViewById(R.id.img_tree);

                this.tv_CSaddplant = (TextView) itemView.findViewById(R.id.tv_CSaddplant);
            }
        }
    }
