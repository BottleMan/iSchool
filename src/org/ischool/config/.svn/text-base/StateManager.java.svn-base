package org.ischool.config;

import java.lang.reflect.Method;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

public class StateManager
{

	private Context mContext;
	private ConnectivityManager mCM;

	/**
	 * 默认构造函数
	 * 
	 * @param pContext
	 */
	public StateManager(Context pContext)
	{
		this.mContext = pContext;
		mCM = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * 自动打开GPS
	 */
	public void OpenGPS()
	{
		LocationManager alm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))
		{
//			Toast.makeText(mContext, "GPS已打开", Toast.LENGTH_SHORT).show();
		} else
		{
//			Toast.makeText(mContext, "GPS打开中", Toast.LENGTH_SHORT).show();
			Intent gpsIntent = new Intent();
			gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
			gpsIntent.setData(Uri.parse("custom:3"));
			try
			{
				PendingIntent.getBroadcast(mContext, 0, gpsIntent, 0).send();
			} catch (CanceledException e)
			{
				Log.i("StateManager", "OpenGPS " + e.toString());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断程序是否联网
	 * 
	 * @param context
	 * @return
	 */
	public boolean IsHaveInternet()
	{
		try
		{
			if (mCM == null)
			{
				return false;
			}
			NetworkInfo info = mCM.getActiveNetworkInfo();

			if (info != null && info.isConnected())
			{
				return true;
			} else
			{
				return false;
			}
		} catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * 查看手机的联网方式
	 * 
	 * @param context
	 * @return
	 */
	public String NetInfo()
	{
		try
		{
			if (mCM == null)
			{
				return "Error";
			}

			NetworkInfo info = mCM.getActiveNetworkInfo();

			return info.getTypeName();

		} catch (Exception e)
		{
			return "Error";
		}
	}

	/**
	 * 打开或关闭GPRS
	 * 
	 * @param bEnable
	 * @return
	 */
	public boolean gprsEnable(boolean bEnable)
	{
		//Object[] argObjects = null;

		boolean isOpen = gprsIsOpenMethod("getMobileDataEnabled");
		if (isOpen == !bEnable)
		{
			setGprsEnable("setMobileDataEnabled", bEnable);
		}

		return isOpen;
	}

	/**
	 * 从framework中取得getMobileDataEnabled这个方法, 主要用来检测GPRS是否打开
	 * 
	 * @param methodName
	 * @return
	 */
	public boolean gprsIsOpenMethod(String methodName)
	{
		@SuppressWarnings("rawtypes")
		Class cmClass = mCM.getClass();
		@SuppressWarnings("rawtypes")
		Class[] argClasses = null;
		Object[] argObject = null;

		Boolean isOpen = false;
		try
		{
			Method method = cmClass.getMethod(methodName, argClasses);

			isOpen = (Boolean) method.invoke(mCM, argObject);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return isOpen;
	}

	/**
	 * 开启/关闭GPRS 取得setMobileDataEnabled方法, 用来打开或关闭GPRS
	 * 
	 * @param methodName
	 * @param isEnable
	 */
	public void setGprsEnable(String methodName, boolean isEnable)
	{
		@SuppressWarnings("rawtypes")
		Class cmClass = mCM.getClass();
		@SuppressWarnings("rawtypes")
		Class[] argClasses = new Class[1];
		argClasses[0] = boolean.class;

		try
		{
			//Class.getMethod是从framework搜索指定的方法，  返回的Method就可以使用该方法
			//第二个参数是该方法的参数类型。
			Method method = cmClass.getMethod(methodName, argClasses);

			//Method.invoke  使用从framework里搜索到的方法, 第二个是参数。
			method.invoke(mCM, isEnable);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
