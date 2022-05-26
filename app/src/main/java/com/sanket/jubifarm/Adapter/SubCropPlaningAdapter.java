package com.sanket.jubifarm.Adapter;

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

import com.sanket.jubifarm.Activity.CropDetailActivity;
import com.sanket.jubifarm.Activity.FarmerDeatilActivity;
import com.sanket.jubifarm.Modal.CropPlanningModal;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.Modal.PlantSubCategoryPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;


    public class SubCropPlaningAdapter extends RecyclerView.Adapter<SubCropPlaningAdapter.ViewHolder> {
        private Context context;
        private ArrayList<PlantSubCategoryPojo> arrayList;
        SqliteHelper sqliteHelper;
        int id;
        // RecyclerView recyclerView;
        public SubCropPlaningAdapter(Context context, ArrayList<PlantSubCategoryPojo> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
            sqliteHelper=new SqliteHelper(context);

        }

        @NonNull

        @Override
        public SubCropPlaningAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.cropplaning_seccustom, parent, false);
            SubCropPlaningAdapter.ViewHolder viewholder = new SubCropPlaningAdapter.ViewHolder(view);
            return viewholder;
        }


        @Override
        public void onBindViewHolder(@NonNull SubCropPlaningAdapter.ViewHolder holder, final int position) {
            holder.name.setText(arrayList.get(position).getName());
            String land_id = arrayList.get(position).getLand_id();
            holder.tv_landId.setText(sqliteHelper.getNameById("land_holding", "land_id", "id", Integer.parseInt(land_id)));
            holder.tv_Plantid.setText(arrayList.get(position).getPlant_id());
            holder.namefarmer.setText(sqliteHelper.getNameById("farmer_registration", "farmer_name", "id", Integer.parseInt(arrayList.get(position).getFarmer_id())));
            holder.tv_geoCoordinate.setText(arrayList.get(position).getLatitude()+", "+arrayList.get(position).getLongitude());
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

            holder.linearLayoutsubcrop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, CropDetailActivity.class);
                    String id = String.valueOf(arrayList.get(position).getId());
                    intent.putExtra("plant_id", id);
                    context.startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }


        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_Plantid,name,tv_landId,tv_geoCoordinate,namefarmer;
            ImageView img_tree;
            LinearLayout linearLayoutsubcrop;
            public ViewHolder(View itemView) {
                super(itemView);

                this.linearLayoutsubcrop = (LinearLayout) itemView.findViewById(R.id.linearLayoutsubcrop);
                this.tv_Plantid = (TextView) itemView.findViewById(R.id.tv_Plantid);
                this.tv_landId = (TextView) itemView.findViewById(R.id.tv_landId);
                this.name = (TextView) itemView.findViewById(R.id.name);
                this.namefarmer = (TextView) itemView.findViewById(R.id.namefarmer);
                this.tv_geoCoordinate = (TextView) itemView.findViewById(R.id.tv_geoCoordinate);
                this.tv_landId = (TextView) itemView.findViewById(R.id.tv_landId);
                this.img_tree = (ImageView) itemView.findViewById(R.id.img_tree);
            }
        }

    }
