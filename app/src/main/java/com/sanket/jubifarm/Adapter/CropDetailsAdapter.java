package com.sanket.jubifarm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanket.jubifarm.Modal.CropVegitableDetails;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class CropDetailsAdapter extends RecyclerView.Adapter<CropDetailsAdapter.ViewHolder> {

    ArrayList<CropVegitableDetails> arrayList;
    Context context;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    ArrayList<String> monthlyList = new ArrayList<>();
    ArrayList<String> monthlyList1 = new ArrayList<>();

    public CropDetailsAdapter(ArrayList<CropVegitableDetails> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        sqliteHelper=new SqliteHelper(context);
        monthlyList.add(0,"Kharif");
        monthlyList.add(1,"Rabi");
        monthlyList.add(2,"Zaid");
        monthlyList.add(3,"Annual");
        monthlyList1.add(0,"खरीफ");
        monthlyList1.add(1,"रबी");
        monthlyList1.add(2,"ज़ैद");
        monthlyList1.add(3,"सालाना");
        sharedPrefHelper=new SharedPrefHelper(context);

    }

    @NonNull
    @Override
    public CropDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_crop_details, parent, false);
        CropDetailsAdapter.ViewHolder viewholder = new CropDetailsAdapter.ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull CropDetailsAdapter.ViewHolder holder, int position) {
        String language = sharedPrefHelper.getString("languageID","");
        holder.tv_Name.setText(sqliteHelper.getCategotyName(Integer.parseInt(arrayList.get(position).getCrop_name()),language));
        holder.tv_area.setText(arrayList.get(position).getArea()+" ("+sqliteHelper.getmasterName(Integer.parseInt(arrayList.get(position).getUnit_id()),language)+")");
        holder.tv_quantity.setText(arrayList.get(position).getQuantity()+" ("+sqliteHelper.getmasterName(Integer.parseInt(arrayList.get(position).getUnits_id()),language)+")");
        if (Integer.parseInt(arrayList.get(position).getSeason_id()) > 4){
            holder.tv_season.setText(sqliteHelper.getmasterName((Integer.parseInt(arrayList.get(position).getSeason_id())),language));

        }else {
            if (language.equals("hin"))
            holder.tv_season.setText(monthlyList1.get(Integer.parseInt(arrayList.get(position).getSeason_id())));
            else
            holder.tv_season.setText(monthlyList.get(Integer.parseInt(arrayList.get(position).getSeason_id())));

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_Name, tv_area, tv_season, tv_quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_Name = (TextView) itemView.findViewById(R.id.tv_Name);
            this.tv_area = (TextView) itemView.findViewById(R.id.tv_Area);
            this.tv_season = (TextView) itemView.findViewById(R.id.tv_season);
            this.tv_quantity = (TextView) itemView.findViewById(R.id.tv_qty);
        }
    }
}
