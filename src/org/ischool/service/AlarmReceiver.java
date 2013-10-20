package org.ischool.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.i("alarm receiver", "received broadcast" + intent.getAction());
		Toast.makeText(context, "新的课程提醒", Toast.LENGTH_SHORT).show();
		Intent service = new Intent(context, ScheduleAlarmService.class);
		service.putExtra("alarm", true);
		context.startService(service);
	}

}
