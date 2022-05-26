
package com.sanket.jubifarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sanket.jubifarm.service.MyJobService;


public class MyAlarmReceiver extends BroadcastReceiver {
	public static final int REQUEST_CODE = 12345;
	public static final String ACTION = "com.codepath.example.servicesdemo.alarm";

	// Triggered by the Alarm periodically (starts the service to run task)
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("DEBUG", "MyAlarmReceiver triggered");

		Intent i = new Intent(context, MyJobService.class);
		i.putExtra("foo", "alarm!!");
		i.putExtra("receiver", MySimpleReceiver.setupServiceReceiver(context));
		MyJobService.enqueueWork(context, i);
	}
}
