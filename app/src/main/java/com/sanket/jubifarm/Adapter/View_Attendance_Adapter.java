package com.sanket.jubifarm.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Modal.Attendance_Approval;
import com.sanket.jubifarm.Modal.TrainingAttandancePojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class View_Attendance_Adapter extends RecyclerView.Adapter<View_Attendance_Adapter.ViewHolder> {
    private Context context;
    private ArrayList<TrainingAttandancePojo> arrayList;
    SqliteHelper sqliteHelper;
    String Id = "";
    Attendance_Approval attendance_approval;
    SharedPrefHelper sharedPrefHelper;
    String userID = "";

    // RecyclerView recyclerView;
    public View_Attendance_Adapter(Context context, ArrayList<TrainingAttandancePojo> arrayList, String userID) {
        this.context = context;
        this.arrayList = arrayList;
        sqliteHelper = new SqliteHelper(context);
        sharedPrefHelper = new SharedPrefHelper(context);
        attendance_approval = new Attendance_Approval();
        this.userID = userID;

    }

    @NonNull

    @Override
    public View_Attendance_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewattendance, parent, false);
        View_Attendance_Adapter.ViewHolder viewholder = new View_Attendance_Adapter.ViewHolder(view);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull View_Attendance_Adapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Attendance_Approval attendanceApproval = new Attendance_Approval();
        attendanceApproval = sqliteHelper.getFarmerDetails(arrayList.get(position).getAdded_by());
        holder.tv_fName.setText(attendanceApproval.getName());
        holder.F_mobile.setText(attendanceApproval.getMobile_no());




        String status = arrayList.get(position).getStatus();
        if (!status.equals("Pending")){
            holder.ll_main.setVisibility(View.GONE);
        }else {
            holder.ll_main.setVisibility(View.VISIBLE);
        }
        if (status.equals("Approve")) {
            holder.txt_cstatus.setTextColor(Color.GREEN);
            holder.txt_cstatus.setText(arrayList.get(position).getStatus());
        } else if (status.equals("Reject")) {
            holder.txt_cstatus.setTextColor(Color.YELLOW);
            holder.txt_cstatus.setText(arrayList.get(position).getStatus());
        } else if (status.equals("Pending")) {
            holder.txt_cstatus.setTextColor(Color.RED);
            holder.txt_cstatus.setText(arrayList.get(position).getStatus());
        }


        holder.tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendance_approval.setTraining_attendance_id(arrayList.get(position).getId());
                attendance_approval.setStatus("Reject");
                attendance_approval.setFarmer_id(arrayList.get(position).getFarmer_id());
                attendance_approval.setRole_id(sharedPrefHelper.getString("role_id",""));
                Gson gson = new Gson();
                String data = gson.toJson(attendance_approval);
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, data);
                Log.e("========", "" + data);
                attendance_approval(body,holder);
            }
        });

        holder.tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendance_approval.setTraining_attendance_id(arrayList.get(position).getId());
                attendance_approval.setStatus("Approve");
                attendance_approval.setFarmer_id(arrayList.get(position).getFarmer_id());
                attendance_approval.setRole_id(sharedPrefHelper.getString("role_id",""));
                Gson gson = new Gson();
                String data = gson.toJson(attendance_approval);
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, data);
                Log.e("========", "" + data);
                attendance_approval(body,holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
        //  return arrayList == null ? 0 : arrayList.size();
    }

    public void attendance_approval(RequestBody body,ViewHolder holder) {
        APIClient.getClient().create(JubiForm_API.class).training_attendance_approval(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    Log.e("subp", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    if (Integer.valueOf(status) == 1) {
                        String message = jsonObject.optString("message");
                        String training_attendance_id = jsonObject.optString("training_attendance_id");
                        sqliteHelper.updateStatusInTrainning("training_attendance", Integer.parseInt(training_attendance_id),attendance_approval.getStatus());
                        holder.ll_main.setVisibility(View.GONE);
                        notifyDataSetChanged();
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                        Log.e("subp", "vxghs " + status + "," + message + "," + training_attendance_id);
                    } else {
                        Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
               /* if (dialog.isShowing()) {
                    dialog.dismiss();
                }*/
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_reject, tv_accept, tv_fName, F_mobile, txt_cstatus;
        LinearLayout ll_main;
        // CardView crd_CM;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_reject = (TextView) itemView.findViewById(R.id.tv_reject);
            this.tv_accept = (TextView) itemView.findViewById(R.id.tv_accept);
            this.tv_fName = (TextView) itemView.findViewById(R.id.tv_fName);
            this.F_mobile = (TextView) itemView.findViewById(R.id.F_mobile);
            this.txt_cstatus = (TextView) itemView.findViewById(R.id.txt_cstatus);
            this.ll_main = (LinearLayout) itemView.findViewById(R.id.ll_main);


        }
    }
}
