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

import com.sanket.jubifarm.Activity.FarmerDeatilActivity;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

public class FarmerFamilyAdapter extends RecyclerView.Adapter<FarmerFamilyAdapter.ViewHolder> {
    private Context context;
    private ArrayList<FarmerRegistrationPojo> arrayList;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    String[] occuptionfamily_dtls = {"Select Occupation", "Occupation", "Doctor", "Engineer", "Business Man", "Student",
            "Other"};
    String[] genderAk = {"लिंग का चयन करें", "पुरुष", "महिला", "अन्य"};

    public FarmerFamilyAdapter(Context context, ArrayList<FarmerRegistrationPojo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sqliteHelper = new SqliteHelper(context);
        sharedPrefHelper=new SharedPrefHelper(context);
    }

    @NonNull

    @Override
    public FarmerFamilyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_family_deatails, parent, false);
        FarmerFamilyAdapter.ViewHolder viewholder = new FarmerFamilyAdapter.ViewHolder(view);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull FarmerFamilyAdapter.ViewHolder holder, final int position) {
        String language = sharedPrefHelper.getString("languageID","");
        holder.tv_Name.setText(arrayList.get(position).getName());
        holder.tv_Age.setText(arrayList.get(position).getAge());
        holder.tv_Occupation.setText(sqliteHelper.getmasterName( arrayList.get(position).getOccupation(),language));
        holder.tv_relationship.setText(sqliteHelper.getmasterName((arrayList.get(position).getRelation_id()),language));
        holder.tv_Education.setText(sqliteHelper.getmasterName(Integer.parseInt(arrayList.get(position).getEducation_id()),language));
        holder.tv_Income.setText(sqliteHelper.getmasterName(arrayList.get(position).getMonthly_income(),language));
        String gender = arrayList.get(position).getGender();
        if (language.equals("hin")){
            if (gender != null && !gender.equals(null) && !gender.equalsIgnoreCase("")) {
                if (gender.equals("Male")) {
                    holder.tv_gender.setText("पुरुष");
                } else if (gender.equals("Female")) {
                    holder.tv_gender.setText("महिला");
                } else if (gender.contains("Other")) {
                    holder.tv_gender.setText("अन्य");
                }else if (gender.equals("पुरुष")) {
                    holder.tv_gender.setText("पुरुष");
                } else if (gender.equals("महिला")) {
                    holder.tv_gender.setText("महिला");
                } else if (gender.contains("अन्य")) {
                    holder.tv_gender.setText("अन्य");
                }

            }
        }else {
            if (gender != null && !gender.equals(null) && !gender.equalsIgnoreCase("")) {
                holder.tv_gender.setText(gender);
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_Name,tv_relationship, tv_Age, tv_gender, tv_Occupation, tv_Education, tv_Income;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_Name = (TextView) itemView.findViewById(R.id.tv_Name);
            this.tv_Age = (TextView) itemView.findViewById(R.id.tv_Age);
            this.tv_gender = (TextView) itemView.findViewById(R.id.tv_gender);
            this.tv_Occupation = (TextView) itemView.findViewById(R.id.tv_Occupation);
            this.tv_Education = (TextView) itemView.findViewById(R.id.tv_Education);
            this.tv_Income = (TextView) itemView.findViewById(R.id.tv_Income);
            this.tv_relationship = (TextView) itemView.findViewById(R.id.tv_relationship);

        }
    }
}

