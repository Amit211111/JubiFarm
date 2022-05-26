package com.sanket.jubifarm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sanket.jubifarm.Modal.PostPlantationPojo;
import com.sanket.jubifarm.Modal.SubPlantationPojo;
import com.sanket.jubifarm.R;
import java.util.ArrayList;
import java.util.HashMap;


public class PostPlantationAdapter extends RecyclerView.Adapter<PostPlantationAdapter.ViewHolder> {
    ArrayList<PostPlantationPojo> arrayList;
    ArrayList<PostPlantationPojo> arrayList1;
    Context context;
    HashMap<Integer,String> values =new HashMap<>();
    public HashMap<Integer,String> getCheckedValues(){
        return values;
    }

    public PostPlantationAdapter(ArrayList<PostPlantationPojo> arrayList, Context context,ArrayList<PostPlantationPojo> arrayList1) {
        this.arrayList = arrayList;
        this.arrayList1 = arrayList1;
        this.context = context;

    }

    @NonNull

    @Override
    public PostPlantationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_plantation_custom, parent, false);
        PostPlantationAdapter.ViewHolder viewholder = new PostPlantationAdapter.ViewHolder(view);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull PostPlantationAdapter.ViewHolder holder, final int position) {

        if (arrayList1.size()>0){
            if (arrayList1.get(position).getValue().equals("Yes")){
                holder.rb_yes.setChecked(true);
            }else  if (arrayList1.get(position).getValue().equals("No")){
                holder.rb_no.setChecked(true);
            }
        }

        holder.tv_option.setText(arrayList.get(position).getMaster_name());
        holder.rg_Plant.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (holder.rb_yes.isChecked()){
                    values.put(arrayList.get(position).getCaption_id(),"Yes");
                }else if (holder.rb_no.isChecked()){
                    values.put(arrayList.get(position).getCaption_id(),"No");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_option;
        RadioGroup rg_Plant;
        RadioButton rb_yes,rb_no;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_option=itemView.findViewById(R.id.tv_option);
            rg_Plant=itemView.findViewById(R.id.rg_Plant);
            rb_yes=itemView.findViewById(R.id.rb_Yes);
            rb_no=itemView.findViewById(R.id.rb_No);
        }
    }
}
