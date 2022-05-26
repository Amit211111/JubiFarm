package com.sanket.jubifarm.Adapter;

import android.app.Activity;
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

import com.sanket.jubifarm.Modal.HelplinePojo;
import com.sanket.jubifarm.Activity.QueryViewActivity;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class QueryListAdapter extends RecyclerView.Adapter<QueryListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<HelplinePojo> arrayList;
    int id;
    SqliteHelper sqliteHelper;
    // RecyclerView recyclerView;
    public QueryListAdapter(Context context, ArrayList<HelplinePojo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sqliteHelper=new SqliteHelper(context);

    }

    @NonNull

    @Override
    public QueryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_query, parent, false);
        QueryListAdapter.ViewHolder viewholder = new QueryListAdapter.ViewHolder(view);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull QueryListAdapter.ViewHolder holder, final int position) {
        holder.tv_FormerName.setText(sqliteHelper.getFarmerName(arrayList.get(position).getFarmer_id ()));
        id=arrayList.get(position).getLocal_id();
        holder.tv_FormerName.setText(sqliteHelper.getFarmerName(arrayList.get(position).getFarmer_id ()));
        holder.tv_query.setText(arrayList.get(position).getQuery());
        holder.tv_date.setText(arrayList.get(position).getQuery_date());
        if (arrayList.get(position).getImage() != null && arrayList.get(position).getImage().length()>200) {
            byte[] decodedString = Base64.decode(arrayList.get(position).getImage(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.img_tree.setImageBitmap(bitmap);
        } else if (arrayList.get(position).getImage().length() <=200) {
            try {
                String url = APIClient.IMAGE_PLANTS_HELPLINE + arrayList.get(position).getImage();
                Picasso.with(context).load(url).placeholder(R.drawable.land).into(holder.img_tree);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.img_tree.setImageResource(R.drawable.land);
        }


        if (arrayList.get(position).getResponse() !=null && !arrayList.get(position).getResponse().equals("")){
            holder.tv_response.setVisibility(View.VISIBLE);
        }else {
            holder.tv_response.setVisibility(View.GONE);
        }

//        if (arrayList.get(position).getResponse() !=null && !arrayList.get(position).getResponse().equals("")) {
            holder.linearLayoutquery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, QueryViewActivity.class);
                    String id = String.valueOf(arrayList.get(position).getLocal_id());
                    intent.putExtra("id", id);
                    intent.putExtra("audio", arrayList.get(position).getAudio_file());
                    context.startActivity(intent);
                    ((Activity) context).finish();
                 }
            });
//        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void updateData(ArrayList<HelplinePojo> list) {
        this.arrayList=list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_FormerName, tv_query, tv_date,tv_response;
        ImageView img_tree;
        TextView tv_EditDetail;
        LinearLayout linearLayoutquery;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_FormerName = (TextView) itemView.findViewById(R.id.tv_FormerName);
            this.tv_query = (TextView) itemView.findViewById(R.id.tv_query);
            this.tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            this.tv_response = (TextView) itemView.findViewById(R.id.tv_response);
            this.img_tree = (ImageView) itemView.findViewById(R.id.img_tree);
            this.linearLayoutquery = (LinearLayout) itemView.findViewById(R.id.linearLayoutquery);
            // this.tv_EditDetail = (TextView) itemView.findViewById(R.id.tv_EditDetail);
        }
    }

}