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

import com.sanket.jubifarm.Activity.FarmerDeatilActivity;
import com.sanket.jubifarm.Activity.ViewGrowthActivity;
import com.sanket.jubifarm.Modal.CropPlanningModal;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.Modal.PlantGrowthPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;



        public class PlantGrowthAdapter extends RecyclerView.Adapter<PlantGrowthAdapter.ViewHolder> {
            private Context context;
            private ArrayList<PlantGrowthPojo> arrayList;
            int id;
            private SqliteHelper sqliteHelper;

            // RecyclerView recyclerView;
            public PlantGrowthAdapter(Context context, ArrayList<PlantGrowthPojo> arrayList) {
                this.context = context;
                this.arrayList = arrayList;
                sqliteHelper=new SqliteHelper(context);
            }

            @NonNull

            @Override
            public PlantGrowthAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(context).inflate(R.layout.custom_plant_growth, parent, false);
                PlantGrowthAdapter.ViewHolder viewholder = new PlantGrowthAdapter.ViewHolder(view);
                return viewholder;
            }


            @Override
            public void onBindViewHolder(@NonNull PlantGrowthAdapter.ViewHolder holder, final int position) {
                id = arrayList.get(position).getId();
                String farmerId= arrayList.get(position).getFarmer_id();
                holder.namefarmer.setText(sqliteHelper.getNameById("farmer_registration", "farmer_name", "id", Integer.parseInt(farmerId)));
                holder.tv_Date.setText(arrayList.get(position).getDate());
                holder.tv_remarks.setText(sqliteHelper.getNameById("crop_type_sub_catagory_language", "name", "id", Integer.parseInt(arrayList.get(position).getCrop_type_subcategory_id())));
                if (arrayList.get(position).getPlant_image() != null && arrayList.get(position).getPlant_image().length()>200) {
                    byte[] decodedString = Base64.decode(arrayList.get(position).getPlant_image(), Base64.NO_WRAP);
                    InputStream inputStream = new ByteArrayInputStream(decodedString);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    holder.img_tree1.setImageBitmap(bitmap);
                } else if (arrayList.get(position).getPlant_image().length() <=200) {
                    try {
                        String url = APIClient.IMAGE_PLANTS_URL + arrayList.get(position).getPlant_image();
                        Picasso.with(context).load(url).placeholder(R.drawable.mango).into(holder.img_tree1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.img_tree1.setImageResource(R.drawable.mango);
                }

                holder.linearLayoutgrowth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ViewGrowthActivity.class);
                        String id = String.valueOf(arrayList.get(position).getId());
                        String localId=arrayList.get(position).getLocal_id();
                        intent.putExtra("id", id);
                        intent.putExtra("localId", localId);
                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return arrayList.size();
            }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_trainigName, trainername, tv_treeCordinate, tv_treeviewDetails, tv_editPlant;
            ImageView img_tree1;
            TextView tv_EditDetail,tv_Date,tv_remarks,namefarmer;
            LinearLayout linearLayoutgrowth;
            public ViewHolder(View itemView) {
                super(itemView);
                this.tv_Date = (TextView) itemView.findViewById(R.id.tv_Date);
                this.tv_remarks = (TextView) itemView.findViewById(R.id.tv_remarks);
                this.namefarmer = (TextView) itemView.findViewById(R.id.namefarmer);
                this.linearLayoutgrowth = (LinearLayout) itemView.findViewById(R.id.linearLayoutgrowth);
                this.img_tree1 = (ImageView) itemView.findViewById(R.id.img_tree1);
              //  this.img_tree = (ImageView) itemView.findViewById(R.id.img_tree);
                // this.tv_EditDetail = (TextView) itemView.findViewById(R.id.tv_EditDetail);
            }
        }

        }


