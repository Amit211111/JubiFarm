package com.sanket.jubifarm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sanket.jubifarm.Activity.SubCropPlaninigActivity;
import com.sanket.jubifarm.Activity.SubMonitoring;
import com.sanket.jubifarm.Modal.CropPlaningPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;


public class CropMoneroringAdapter extends RecyclerView.Adapter<CropMoneroringAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CropPlaningPojo> arrayList;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    String land_id="";
    String screen_type;
    String farmer_id;
    // RecyclerView recyclerView;
    public CropMoneroringAdapter(Context context, ArrayList<CropPlaningPojo> arrayList,String land_id,String screen_type,String farmer_id) {
        this.context = context;
        this.arrayList = arrayList;
        sqliteHelper=new SqliteHelper(context);
        sharedPrefHelper=new SharedPrefHelper(context);
        this.screen_type = screen_type;
        this.land_id = land_id;
        this.farmer_id = farmer_id;


    }

    @NonNull

    @Override
    public CropMoneroringAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cropmonetoring, parent, false);
        CropMoneroringAdapter.ViewHolder viewholder = new CropMoneroringAdapter.ViewHolder(view);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull CropMoneroringAdapter.ViewHolder holder, final int position) {
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
        holder.tv_CM_treeName.setText(sqliteHelper.getCategotyName(Integer.parseInt(arrayList.get(position).getCrop_type_catagory_id()),language));
        holder.tv_CM_treeCount.setText(arrayList.get(position).getTotal_plant());

        holder.crd_CM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, SubMonitoring.class);
                intent.putExtra("cropCategory_id", arrayList.get(position).getCrop_type_catagory_id());
                intent.putExtra("land_id_id",land_id);
                intent.putExtra("screen_type",screen_type);
                intent.putExtra("farmer_id",farmer_id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_CM_treeName,tv_CM_treeCount;
        CardView crd_CM;
        ImageView img_tree;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_CM_treeName = (TextView) itemView.findViewById(R.id.tv_CM_treeName);
            this.tv_CM_treeCount = (TextView) itemView.findViewById(R.id.tv_CM_treeCount);
            this.crd_CM = (CardView) itemView.findViewById(R.id.crd_CM);
            this.img_tree = (ImageView) itemView.findViewById(R.id.img_tree);

        }
    }
}
