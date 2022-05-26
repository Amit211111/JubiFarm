package com.sanket.jubifarm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sanket.jubifarm.Activity.TranningDetails;
import com.sanket.jubifarm.Modal.TrainingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;



    public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {
        private Context context;
        private ArrayList<TrainingPojo> arrayList;
        String time;
        SqliteHelper sqliteHelper;
        SharedPrefHelper sharedPrefHelper;
        // RecyclerView recyclerView;
        public TrainingAdapter(Context context, ArrayList<TrainingPojo> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
            sqliteHelper=new SqliteHelper(context);
            sharedPrefHelper=new SharedPrefHelper(context);
        }

        @NonNull

        @Override
        public TrainingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_training, parent, false);
            TrainingAdapter.ViewHolder viewholder = new TrainingAdapter.ViewHolder(view);
            return viewholder;
        }


        @Override
        public void onBindViewHolder(@NonNull TrainingAdapter.ViewHolder holder, final int position) {
          //time=arrayList.get(position).get();

         /*   String ids = arrayList.get(position).getId();
            String fids = arrayList.get(position).getUser_id();
            Log.e("========","===="+ids+","+fids);

            sharedPrefHelper.setString("trainning_Id",ids);
            sharedPrefHelper.setString("farmerID",fids);*/

            holder.txt_date.setText(arrayList.get(position).getFrom_date());
            holder.txt_todate.setText(arrayList.get(position).getTo_date());
            holder.txt_fromtime.setText(arrayList.get(position).getFrom_time());
            holder.txt_totime.setText(arrayList.get(position).getTo_time());
            holder.txt_Location.setText(arrayList.get(position).getAddress());
            holder.txt_mobileno.setText(arrayList.get(position).getMobile());
            holder.txt_Header.setText(arrayList.get(position).getTraining_name());
          //  holder.txt_description.setText(arrayList.get(position).getBrief_description());
         //   holder.spn_village.setText(arrayList.get(position).getVillage_id());

            holder.linearLayoutChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, TranningDetails.class);
                    intent.putExtra("id",arrayList.get(position).getId());
                    intent.putExtra("userID",arrayList.get(position).getUser_id());
                    intent.putExtra("TO_DATE",holder.txt_date.getText().toString());
                    intent.putExtra("FROM_DATE",holder.txt_todate.getText().toString());
                    intent.putExtra("FROM_TIME",holder.txt_fromtime.getText().toString());
                    intent.putExtra("TO_TIME",holder.txt_totime.getText().toString());
                    intent.putExtra("LOCATION",holder.txt_Location.getText().toString());
                    intent.putExtra("MOBILENO",holder.txt_mobileno.getText().toString());
                    intent.putExtra("TRANNINGHEADER",holder.txt_Header.getText().toString());
                  //  intent.putExtra("brief_description",holder.txt_description.getText().toString());
                   // intent.putExtra("village_id",holder.spn_village.getText().toString());
                  //  intent.putExtra("TRANNERS",Tranner);

                  //  intent.putExtra("DEST",Destination);
                    context.startActivity(intent);
                }
            });
      }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView txt_Header,txt_date,txt_Location,txt_mobileno,txt_todate, txt_fromtime, txt_totime , txt_description, spn_village;
            ImageView img_tree;
            LinearLayout linearLayoutChild;
            public ViewHolder(View itemView) {
                super(itemView);
                this.linearLayoutChild = (LinearLayout) itemView.findViewById(R.id.linearLayoutChild);
                this.txt_Header = (TextView) itemView.findViewById(R.id.txt_Header);
                this.txt_date = (TextView) itemView.findViewById(R.id.txt_date);
                this.txt_Location = (TextView) itemView.findViewById(R.id.txt_Location);
                this.txt_mobileno = (TextView) itemView.findViewById(R.id.txt_mobileno);
                this.txt_todate = (TextView) itemView.findViewById(R.id.txt_todate);
                this.txt_fromtime = (TextView) itemView.findViewById(R.id.txt_fromtime);
                this.txt_totime = (TextView) itemView.findViewById(R.id.txt_totime);
                //this.img_tree = (ImageView) itemView.findViewById(R.id.img_tree);
               // this.ll_holder = (LinearLayout) itemView.findViewById(R.id.ll_holder);
            }
        }

    }
