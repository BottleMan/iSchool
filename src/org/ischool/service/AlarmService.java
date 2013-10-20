package org.ischool.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service
{

	private static final String PREF_FILE_NAME = "user_profiles";
	SharedPreferences preferences;

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate()
	{
		preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		boolean doService = preferences.getBoolean("doService", true);
		if (doService)
		{
			//设置闹铃的时间
			int mHour = preferences.getInt("hour", 7);
			int mMinute = preferences.getInt("minute", 30);
			
			Calendar c = Calendar.getInstance();//创建实例 默认是当前时刻
			c.set(Calendar.HOUR, 24);
			c.set(Calendar.HOUR_OF_DAY, mHour);
			c.set(Calendar.MINUTE, mMinute);
			c.set(Calendar.SECOND, 0);

			Intent intent2 = new Intent(this, AlarmReceiver.class);
			
			//设置一个PendingIntent对象，发送广播
			PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent2, 0);
			
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			
			//120000ms = 2min
			//当当前时间超过设定时间的2min之内的时候会提示新课程
			if(c.getTimeInMillis() - System.currentTimeMillis() > 0 && c.getTimeInMillis() - System.currentTimeMillis() < 120000 )
			{
				//获取AlarmManager对象
				Log.i("Alarm Service",
						"exe " + 
						"Set Time " + String.valueOf(c.getTimeInMillis()
						+ " System Time " + System.currentTimeMillis()));
				am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
			}else {
				Log.i("Alarm Service",
						"unexe " + 
						"Set Time " + String.valueOf(c.getTimeInMillis()
						+ " System Time " + System.currentTimeMillis()));
			}
		}
		return super.onStartCommand(intent, flags, startId);

	}

}
