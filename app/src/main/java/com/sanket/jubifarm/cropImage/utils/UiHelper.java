package com.sanket.jubifarm.cropImage.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;


import com.sanket.jubifarm.R;
import com.sanket.jubifarm.cropImage.enums.ImagePickerEnum;
import com.sanket.jubifarm.cropImage.listeners.IImagePickerLister;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.sanket.jubifarm.Activity.VendorRegistration.CAMERA_STORAGE_REQUEST_CODE;
import static com.sanket.jubifarm.Activity.VendorRegistration.ONLY_CAMERA_REQUEST_CODE;
import static com.sanket.jubifarm.Activity.VendorRegistration.ONLY_STORAGE_REQUEST_CODE;

/**
 * Ahsen Saeed}
 * ahsansaeed067@gmail.com}
 * 2/6/19}
 */

public class UiHelper {

    public void showImagePickerDialog(@NonNull Context callingClassContext, IImagePickerLister imagePickerLister) {
        /*new MaterialDialog.Builder(callingClassContext)
                .items(R.array.imagePicker)
                .canceledOnTouchOutside(true)
                .itemsCallback((dialog, itemView, position, text) -> {
                    if (position == 0)
                        imagePickerLister.onOptionSelected(ImagePickerEnum.FROM_GALLERY);
                    else if (position == 1)
                        imagePickerLister.onOptionSelected(ImagePickerEnum.FROM_CAMERA);
                    dialog.dismiss();
                }).show();*/

                LayoutInflater layoutInflater = (LayoutInflater) callingClassContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.update_pic_layout, null);
                ImageView camera = (ImageView) popupView.findViewById(R.id.camera);
                ImageView gallery = (ImageView) popupView.findViewById(R.id.gallery);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(callingClassContext);
                alertDialog.setTitle("Choose Image");
                alertDialog.setView(popupView);
                final AlertDialog dialog = alertDialog.show();
                alertDialog.setCancelable(true);
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            dialog.dismiss();
                            if (Build.VERSION.SDK_INT > 19) {
                                imagePickerLister.onOptionSelected(ImagePickerEnum.FROM_CAMERA);
                            }
                            if (Build.VERSION.SDK_INT < 20) {
                                imagePickerLister.onOptionSelected(ImagePickerEnum.FROM_CAMERA);
                            }
                        } catch (Exception ex) {
                            Log.d("exp_result:", ex.getMessage().toString());
                        }
                    }
                });
                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        imagePickerLister.onOptionSelected(ImagePickerEnum.FROM_GALLERY);
                    }
                });

    }

    public void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkSelfPermissions(@NonNull Activity activity) {
        if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_STORAGE_REQUEST_CODE);
            return false;
        } else if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, ONLY_CAMERA_REQUEST_CODE);
            return false;
        } else if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ONLY_STORAGE_REQUEST_CODE);
            return false;
        }
        return true;
    }
}
