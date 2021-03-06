package com.sanket.jubifarm.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonArray;
import com.sanket.jubifarm.Activity.HomeAcivity;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
 public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    SharedPreferences sharedPreferences;
    private NotificationUtils notificationUtils;
    SharedPrefHelper sharedPrefHelper;
    SqliteHelper sqliteHelper;
    private String[] tables = {"land_holding","crop_planning","sale_details","production_details","sub_plantation",
            "post_plantation","training","training_attendance","help_line", "supplier_registration",
            "input_ordering", "input_ordering_vender","crop_vegetable_details"};

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sharedPrefHelper=new SharedPrefHelper(getApplicationContext());
        sqliteHelper=new SqliteHelper(getApplicationContext());
        if (remoteMessage == null)
            return;
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in app_bg_theme, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");
            String  jsond  = data.optString("json");
            for (int i = 0; i < tables.length ; i++) {
                JSONObject datsf= new JSONObject(jsond);
                if (datsf.has(tables[i])) {
                    JSONArray gg = datsf.getJSONArray(tables[i]);
                    for (int j = 0; j < gg.length(); j++) {
                        JSONObject singledata = new JSONObject(gg.get(j).toString());
                        // JSONObject singledata = data.getJSONObject(i);
                        //singledata.getString("id");
                        Iterator keys = singledata.keys();
                        ContentValues contentValues = new ContentValues();
                        while (keys.hasNext()) {
                            String currentDynamicKey = (String) keys.next();
                            contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                        }
                        boolean eids=false;
                        /*if(tables[i].equals("land_holding")){
                            eids = sqliteHelper.checkIdExist(tables[i],singledata.getString("land_id_primarykey"));
                        }else{*/
                            eids = sqliteHelper.checkIdExist(tables[i],singledata.getString("id"));
                        //}
                        if (eids){
                            sqliteHelper.updateMasterTable(contentValues, tables[i], singledata.getString("id"));
                        }else {
                            sqliteHelper.saveMasterTable(contentValues, tables[i]);
                        }

                    }

                }
            }

                Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();

                Intent resultIntent = new Intent(getApplicationContext(), HomeAcivity.class);
                resultIntent.putExtra("message", message);
                // check for image attachmentr
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            } else {
                // app is in app_bg_theme, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), HomeAcivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}


