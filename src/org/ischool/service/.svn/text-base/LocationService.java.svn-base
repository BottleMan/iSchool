package org.ischool.service;

import java.util.ArrayList;
import java.util.List;

import org.ischool.config.GPSServiceListener;
import org.ischool.config.JsonParase;
import org.ischool.config.StateManager;
import org.ischool.config.UrlConfig;
import org.ischool.mod.MsgInfo;
import org.ischool.sqllite.InitialDB;
import org.ischool.sqllite.UserMsgDBOperation;
import org.ischool.web.SyncHttp;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service
{
	private static final String PREF_FILE_NAME = "user_profiles";

	//120000ms = 2min
	private static final long minTime = 2000;
	//最小变更距离 0m
	//也就是说没隔2000ms就会取一次数据，不论是否变换了位置
	private static final float minDistance = 0;

	private LocationManager locationManager;
	private LocationListener locationListener;
	private UserMsgDBOperation userMsgDBOperation;
	private InitialDB initialDB;

	private final IBinder mBinder = new GPSServiceBinder();

	private String userName;

	@Override
	public void onCreate()
	{
		startService();
		Log.i("GPS", "GPSService Started.");
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		return mBinder;
	}

	public class GPSServiceBinder extends Binder
	{
		public LocationService getService()
		{
			return LocationService.this;
		}
	}

	@Override
	public void onDestroy()
	{
		//关闭GPRS
		StateManager stateManager = new StateManager(LocationService.this);
		try
		{
			stateManager.gprsEnable(false);
			Log.i("Close GPS", "Close GPS");
		} catch (Exception e)
		{
			Log.i("Close GPS", e.toString());
		}

		endService();
		Log.i("GPS", "GPSService Ended.");

	}

	public void startService()
	{
		userMsgDBOperation = new UserMsgDBOperation(getApplicationContext());

		SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
		userName = preferences.getString("username_2", "");
		String pwd = preferences.getString("password_2", "");
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

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new GPSServiceListener(userName, pwd, LocationService.this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);

		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null)
		{
			double latitude = location.getLatitude(); //经度
			double longitude = location.getLongitude(); //纬度
			//double altitude =  location.getAltitude();     //海拔

			if (!userName.equals(""))
			{
				syncLocation(latitude, longitude);
				getData(latitude, longitude);
			}

			Log.i("GPRS Data", "latitude:" + latitude + "longitude:" + longitude);
		}

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
	 * 终止service
	 */
	public void endService()
	{
		if (locationManager != null && locationListener != null)
		{
			locationManager.removeUpdates(locationListener);
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
			if (!retStr.equals(""))
			{
				List<MsgInfo> msgInfos = new ArrayList<MsgInfo>();
				JsonParase jsonParase = new JsonParase();
				msgInfos = jsonParase.ParaseMsgInfo(retStr);
				setDataBase(msgInfos);
			}

		} catch (Exception e)
		{
			Log.i("GPS Service", "GetData Error " + e.toString());
		}
	}

	/**
	 * 当同步位置的时候如果发现在当前位置有指定的消息，就将消息写到本地的数据库中，同时显示Toast提示
	 * 
	 * @param msgInfos
	 */
	private void setDataBase(List<MsgInfo> msgInfos)
	{
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
			
			userMsgDBOperation.InsertDB(values);
		}
		
		
		Toast.makeText(getApplicationContext(), "您在当前位置有新消息", Toast.LENGTH_SHORT).show();
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
				Log.i("GPS Sync Service", "insert data");
			} else if (retStr.equals("update data"))
			{
				Log.i("GPS Sync Service", "update data");
			} else
			{
				Log.i("GPS Sync Service", "Sync Error, reStr = " + retStr);
			}
		} catch (Exception e)
		{
			Log.i("GPS Service", e.toString());
		}
	}
}