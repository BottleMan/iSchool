package org.ischool.service;

import java.sql.Date;
import java.util.Calendar;

import org.ischool.ISchool2Activity;
import org.ischool.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ScheduleAlarmService extends Service
{

	private static final int ID2 = 1212;
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		//这里加上Try-Catch，不然Service在后台运行的时候会报错
		try {
			boolean alarm = intent.getBooleanExtra("alarm", false);
			if (alarm)
			{
				Log.i("service", "start alarm on schedule");
				scheduleAlarm();
			}
			
		} catch (Exception e) {
			Log.i("service", "ScheduleAlarmService " + e.toString() );
		}
		return super.onStartCommand(intent, flags, startId);
		
	}
	
	/**
	 * 课程信息的提醒
	 */
	private void scheduleAlarm()
	{
		//获得NotificationManager实例
		String service = NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager) getSystemService(service);
		//实例化Notification
		Notification notification = new Notification();
		//设置显示图标
		int icon = R.drawable.ic_launcher;
		//设置提示信息
		String tickerText = "校园通课程提醒";
		//显示时间
		long when = System.currentTimeMillis();

		notification.icon = icon;
		notification.tickerText = tickerText;
		notification.when = when;

		//显示在“正在进行中”
		//  n.flags = Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_AUTO_CANCEL; //自动终止

		Calendar calendar = Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (index < 0)
		{
			index = 6;
		}

		//实例化Intent
		Intent intent = new Intent(this, ISchool2Activity.class);
		intent.putExtra("alarm", true);
		intent.putExtra("index", index);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		//设置事件信息，显示在拉开的里面
		notification.setLatestEventInfo(getApplicationContext(), "校园通课程提醒", "课程信息提醒，请点击查看", pendingIntent);

		//发出通知
		notificationManager.notify(ID2, notification);
	}

	
	

}
