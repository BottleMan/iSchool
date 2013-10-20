package org.ischool.config;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;


/**
 * 这个LocationListener是一个中转，即当用户的位置发生变化的时候，其检测到变化，然后发送一个广播，广播调用iSchoolService，
 * 并在service中实现显示Toast，显示顶部通知，更新数据库
 * @author Administrator
 *
 */
public class GPSServiceListener implements LocationListener
{
	private double latitude = 0;
	private double longitude = 0;
	
	private Context mContext;

	public int GPSCurrentStatus;

	public GPSServiceListener(String pUserName, String pPwd, Context pContext)
	{
		this.mContext = pContext;
	}

	@Override
	public void onLocationChanged(Location location)
	{
		if (location != null)
		{
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			Log.i("GPRS Data in Listener", "latitude:" + latitude + "longitude:" + longitude);
			
			Intent intent = new Intent();
			intent.setAction("com.ischool.Location.broadcast");
			mContext.sendBroadcast(intent);
		}
	}

	@Override
	public void onProviderDisabled(String provider)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// TODO Auto-generated method stub
		GPSCurrentStatus = status;
	}

}