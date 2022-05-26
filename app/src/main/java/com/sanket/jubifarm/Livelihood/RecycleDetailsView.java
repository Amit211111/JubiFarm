package com.sanket.jubifarm.Livelihood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sanket.jubifarm.Activity.AddLandActivity;
import com.sanket.jubifarm.Activity.AddPlantAcivity;
import com.sanket.jubifarm.Activity.CropMonitoring;
import com.sanket.jubifarm.Activity.CropPlanning;
import com.sanket.jubifarm.Activity.HomeAcivity;
import com.sanket.jubifarm.Activity.SoilInFoListActivity;
import com.sanket.jubifarm.Activity.ViewLandActivity;
import com.sanket.jubifarm.Livelihood.Adapter.Adapter_PS_LandHolding;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Modal.LandHoldingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class RecycleDetailsView extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_details_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    }
