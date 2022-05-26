package com.sanket.jubifarm.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sanket.jubifarm.R;

public class AlertDialogClass {
    public static android.app.Dialog info_alert;

    public static void showDialog(Context context, String message) {
        info_alert = new android.app.Dialog(context);

        info_alert.setContentView(R.layout.submitdialog);
        info_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = info_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        TextView tvDescription = (TextView) info_alert.findViewById(R.id.tv_description);
        Button btnOk = (Button) info_alert.findViewById(R.id.btnOk);

        tvDescription.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO
                info_alert.dismiss();
            }
        });

        info_alert.show();
        info_alert.setCanceledOnTouchOutside(false);
    }



}
