package com.sanket.jubifarm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanket.jubifarm.Modal.InputOrderingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.interfaces.ClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InputOrdiringListAdapter extends RecyclerView.Adapter<InputOrdiringListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<InputOrderingPojo> arrayList;
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sharedPrefHelper;
    ClickListener clickListener;

    public InputOrdiringListAdapter(Context context, ArrayList<InputOrderingPojo> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        sqliteHelper = new SqliteHelper(context);
        sharedPrefHelper = new SharedPrefHelper(context);
    }

    @NonNull
    @Override
    public InputOrdiringListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_inputordiring, parent, false);
        InputOrdiringListAdapter.ViewHolder viewHolder = new InputOrdiringListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InputOrdiringListAdapter.ViewHolder holder, int position) {
        int itemsId = Integer.parseInt(arrayList.get(position).getInput_type_id());
        String items_name = sqliteHelper.getNameById("master", "master_name", "caption_id", itemsId);
        holder.inputOrd_header.setText(context.getString(R.string.Items)+": "+items_name);
        String outputDateRequestDateTo="";
        String requestDateTo = arrayList.get(position).getRequest_date_to();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date newDate = sdf.parse(requestDateTo);
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            outputDateRequestDateTo = sdf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateRequestDateFrom="";
        String requestDateFrom = arrayList.get(position).getRequest_date_from();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date newDate = sdf1.parse(requestDateFrom);
            sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            outputDateRequestDateFrom = sdf1.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (requestDateTo.length() > 10 && requestDateFrom.length() > 10) {
            holder.inputOrd_ToFromdate.setText(context.getString(R.string.Date)+": "+ outputDateRequestDateFrom + " - "+outputDateRequestDateTo);
        } else {
            holder.inputOrd_ToFromdate.setText(context.getString(R.string.Date)+": "+ requestDateFrom + " - "+requestDateTo);
        }

        holder.inputOrd_ToFromdate.setText(context.getString(R.string.Date)+": "+arrayList.get(position).getRequest_date_from()+
                " - "+arrayList.get(position).getRequest_date_to());
        holder.inputOrd_quantity.setText(context.getString(R.string.Quantity)+": "+arrayList.get(position).getQuantity());
        String status = arrayList.get(position).getStatus();
        if (status!=null) {
                holder.tv_status.setText(context.getString(R.string.Status)+": "+status);
        }

        if (sharedPrefHelper.getString("user_type", "").equals("Farmer") ||
                sharedPrefHelper.getString("user_type", "").equals("Supplier")) {
            holder.farmer_name.setVisibility(View.GONE);
        } else {
            holder.farmer_name.setVisibility(View.VISIBLE);
            holder.farmer_name.setText(context.getString(R.string.Farmer)+ ": "+sqliteHelper.getNameById("farmer_registration",
                    "farmer_name", "id", Integer.parseInt(arrayList.get(position).getFarmer_id())));
        }

        holder.ll_inputording_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView inputOrd_header, inputOrd_reqitem,
                inputOrd_ToFromdate, inputOrd_quantity,
                tv_status, farmer_name;
        ImageView img_tree;
        LinearLayout ll_inputording_holder;

        public ViewHolder(View itemView) {
            super(itemView);
            this.inputOrd_header = (TextView) itemView.findViewById(R.id.inputOrd_header);
            this.inputOrd_reqitem = (TextView) itemView.findViewById(R.id.inputOrd_reqitem);
            this.inputOrd_ToFromdate = (TextView) itemView.findViewById(R.id.inputOrd_ToFromdate);
            this.inputOrd_quantity = (TextView) itemView.findViewById(R.id.inputOrd_quantity);
            this.img_tree = (ImageView) itemView.findViewById(R.id.img_tree);
            ll_inputording_holder = (LinearLayout) itemView.findViewById(R.id.ll_inputording_holder);
            tv_status = itemView.findViewById(R.id.tv_status);
            farmer_name = itemView.findViewById(R.id.farmer_name);
        }
    }

    public void onItemClick(ClickListener listener) {
        this.clickListener = listener;
    }
}
