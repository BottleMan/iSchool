package org.ischool.service;

import java.util.ArrayList;
import java.util.List;

import org.ischool.ISchool2Activity;
import org.ischool.R;
import org.ischool.config.GPSServiceListener;
import org.ischool.config.JsonParase;
import org.ischool.config.UrlConfig;
import org.ischool.mod.MsgInfo;
import org.ischool.sqllite.InitialDB;
import org.ischool.sqllite.UserMsgDBOperation;
import org.ischool.web.SyncHttp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ISchoolService extends Service
{
	private static final int ID = 1213;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private UserMsgDBOperation userMsgDBOperation;
	private InitialDB initialDB;
	private static final String PREF_FILE_NAME = "user_profiles";
	SharedPreferences preferences;

	private boolean doService;

	//120000ms = 2min
	private long minTime = 120000;
	//最小变更距离 0m
	//也就是说每隔2minutes就会取一次数据，不论是否变换了位置
	private static final float minDistance = 0;

	private String userName;

	private List<MsgInfo> msgInfos;
	private String msgIDFlag;

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{

		super.onCreate();

		userMsgDBOperation = new UserMsgDBOperation(getApplicationContext());
		preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
		boolean isFirstTime = preferences.getBoolean("isFirstTime", true);
		if (isFirstTime)
		{
			if (initialMsgDB())
			{
				Editor editor = preferences.edit();
				editor.putBoolean("isFirstTime", false);
				editor.commit();
			}
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		userName = preferences.getString("username_2", "");
		doService = preferences.getBoolean("doService", true);
		syncLocationAndGetData();

		//定时，每隔minTime执行一次同步数据和取数据
		final Handler handler = new Handler();
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				// 在此处添加执行的代码
				Log.i("service", "handler & runnable");
				minTime = preferences.getLong("minTime", 120000);
				syncLocationAndGetData();
				handler.postDelayed(this, minTime);// 50是延时时长
			}
		};
		handler.postDelayed(runnable, minTime);// 打开定时器，执行操作

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 同步位置并获取数据
	 */
	private void syncLocationAndGetData()
	{
		Log.i("service", "doService=" + doService);
		if (doService)
		{
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationListener = new GPSServiceListener(userName, "", ISchoolService.this);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);

			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			if (location != null)
			{
				double latitude = location.getLatitude(); //经度
				double longitude = location.getLongitude(); //纬度

				if (!userName.equals(""))
				{
					syncLocation(latitude, longitude);
					getData(latitude, longitude);
				}
				savaLocation(latitude, longitude);
				Log.i("service", "latitude:" + latitude + "longitude:" + longitude);
			} else
			{
				double latitude; //经度
				double longitude; //纬度
				latitude = Double.valueOf(preferences.getString("latitude", "38"));
				longitude = Double.valueOf(preferences.getString("longitude", "120"));

				if (!userName.equals(""))
				{
					syncLocation(latitude, longitude);
					getData(latitude, longitude);
				}
				savaLocation(latitude, longitude);
				Log.i("service", "latitude:" + latitude + "longitude:" + longitude);
			}

		}
	}

	/**
	 * 保存用户的经纬度信息到文件中
	 * 
	 * @param latitude
	 * @param longitude
	 */
	private void savaLocation(double latitude, double longitude)
	{
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("latitude", String.valueOf(latitude));
		editor.putString("longitude", String.valueOf(longitude));
		editor.commit();
	}

	/**
	 * 初始化用户消息数据库
	 * 
	 * @return
	 */
	private boolean initialMsgDB()
	{
		initialDB = new InitialDB(getApplicationContext());
		if (initialDB.setAppDB())
		{
			return true;
		} else
		{
			return false;
		}

	}

	/**
	 * 向数据库同步位置信息
	 * 
	 * @param platitude
	 * @param plongitude
	 */
	private void syncLocation(Double platitude, Double plongitude)
	{
		String params = "action=syncLocation" + "&userName=" + userName + "&latitude=" + platitude + "&longitude=" + plongitude;
		String url = UrlConfig.SMSURL;
		SyncHttp syncHttp = new SyncHttp();
		try
		{
			String retStr = syncHttp.httpGet(url, params);
			if (retStr.equals("insert data"))
			{
				Log.i("service", "insert data");
			} else if (retStr.equals("update data"))
			{
				Log.i("service", "update data");
			} else
			{
				Log.i("service", "Sync Error, reStr = " + retStr);
			}
		} catch (Exception e)
		{
			Log.i("service", e.toString());
		}
	}

	/**
	 * 查看数据库是否有信息未获取
	 * 
	 * @param platitude
	 * @param plongitude
	 */
	private void getData(double platitude, double plongitude)
	{
		String params = "action=getData" + "&userName=" + userName + "&latitude=" + platitude + "&longitude=" + plongitude;
		String url = UrlConfig.SMSURL;
		SyncHttp syncHttp = new SyncHttp();
		try
		{
			String retStr = syncHttp.httpGet(url, params);
			//如果返回的值是空，则不进行通知等操作
			if (!retStr.equals("[]"))
			{
				msgInfos = new ArrayList<MsgInfo>();
				JsonParase jsonParase = new JsonParase();
				msgInfos = jsonParase.ParaseMsgInfo(retStr);

				if (setDataBase(msgInfos) > 0)
				{
					Log.i("service", "new MSG");
					addNotification();
				} else
				{
					Log.i("service", "no new msg");
				}
			}
			Log.i("GPS Service", "Get Data " + retStr);

		} catch (Exception e)
		{
			Log.i("GPS Service", "GetData Error " + e.toString());
		}
	}

	/**
	 * 顶部通知栏显示通知
	 */
	private void addNotification()
	{
		//获得NotificationManager实例
		String service = NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager) getSystemService(service);
		//实例化Notification
		Notification notification = new Notification();
		//设置显示图标
		int icon = R.drawable.ic_launcher;
		//设置提示信息
		String tickerText = "校园通新消息";
		//显示时间
		long when = System.currentTimeMillis();

		notification.icon = icon;
		notification.tickerText = tickerText;
		notification.when = when;

		//显示在“正在进行中”
		//  n.flags = Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_AUTO_CANCEL; //自动终止

		//实例化Intent
		Intent intent = new Intent(this, ISchool2Activity.class);
		intent.putExtra("notification", true);
		intent.putExtra("msgID", msgIDFlag);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		//设置事件信息，显示在拉开的里面
		notification.setLatestEventInfo(getApplicationContext(), "iSchool新消息", "您在当前位置有新消息，请点击查看", pendingIntent);

		//发出通知
		notificationManager.notify(ID, notification);
	}

	/**
	 * 当同步位置的时候如果发现在当前位置有指定的消息，就将消息写到本地的数据库中，同时显示Toast提示
	 * 
	 * @param msgInfos
	 */
	private int setDataBase(List<MsgInfo> msgInfos)
	{
		int result = 0;
		for (int i = 0; i < msgInfos.size(); i++)
		{
			ContentValues values = new ContentValues();
			MsgInfo msgInfo = msgInfos.get(i);
			values.put("msgID", msgInfo.getMsgID());
			values.put("msgTitle", msgInfo.getMsgTitle());
			values.put("msgType", msgInfo.getMsgType());
			values.put("content", msgInfo.getContent());
			values.put("userID", msgInfo.getUserName());
			values.put("latitude", msgInfo.getLatitude());
			values.put("longitude", msgInfo.getLongitude());
			values.put("radius", msgInfo.getRadius());
			values.put("readed", "0");
			values.put("postTime", msgInfo.getPostTime());

			userMsgDBOperation.OpenDB();
			if (userMsgDBOperation.InsertDB(values))
			{
				msgIDFlag = msgInfo.getMsgID();
				result++;
			}
		}
		if (result > 0)
		{
			Toast.makeText(getApplicationContext(), "您在当前位置有新消息", Toast.LENGTH_SHORT).show();
		}

		return result;
	}
}
