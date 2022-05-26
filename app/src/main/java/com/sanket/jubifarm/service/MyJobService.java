package com.sanket.jubifarm.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.sanket.jubifarm.utils.CommonFunctions;


/**
 * Created by RAM on 2/15/2019.
 */

public class MyJobService extends JobIntentService {
    public static final int JOB_ID = 101;

    private Context context = this;
    public static CommonFunctions cf=null;
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MyJobService.class, JOB_ID, work);
        cf=new CommonFunctions(context);
    }
    public MyJobService() {
        super();

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.e("start","started");

        /*if (cf.isInternetOn()){
            cf.sendLandData();
            cf.addPlantData();
        }*/
    }
}