package com.sanket.jubifarm.Livelihood.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class NeemPlantationAdapter extends RecyclerView.Adapter<NeemPlantationAdapter.ViewHolder> {

    Context context;
    ArrayList<PSNeemPlantationPojo> psNeemPlantationPojos;

    public NeemPlantationAdapter(Context context, ArrayList<PSNeemPlantationPojo> psNeemPlantationPojos)
    {
        this.context = context;
        this.psNeemPlantationPojos = psNeemPlantationPojos;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.neemplantationresource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NeemPlantationAdapter.ViewHolder holder, int position) {
        holder.Land_id.setText(psNeemPlantationPojos.get(position).getLand_id());
        holder.Neem_Id.setText(psNeemPlantationPojos.get(position).getLocal_id());
        holder.geo_cordinate.setText(psNeemPlantationPojos.get(position).getGeo_coordinates());
        if(psNeemPlantationPojos.get(position).getNeem_plantation_image() != null && psNeemPlantationPojos.get(position).getNeem_plantation_image().length() > 200)
        {
            byte[] decodedString = android.util.Base64.decode(psNeemPlantationPojos.get(position).getNeem_plantation_image(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.img_setimage.setImageBitmap(bitmap);
        }else {
            holder.img_setimage.setImageResource(R.drawable.ic_baseline_add_24);
        }
    }

    @Override
    public int getItemCount() {
        return psNeemPlantationPojos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_setimage;
        TextView Land_id, geo_cordinate, Neem_Id;
        public ViewHolder(View Itemview) {
            super(Itemview);
            img_setimage = Itemview.findViewById(R.id.img_setimage);
            Land_id = Itemview.findViewById(R.id.Land_id);
            Neem_Id = Itemview.findViewById(R.id.Neem_Id);
            geo_cordinate = Itemview.findViewById(R.id.geo_cordinate);
        }
    }
}