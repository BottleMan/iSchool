package org.ischool.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver
{

	private Context mContext;
	@Override
	public void onReceive(Context context, Intent intent)
	{
		this.mContext = context;
		String action = intent.getAction();
		if (action.equals("com.ischool.Location.broadcast"))
		{
			Intent service = new Intent(mContext, ISchoolService.class);
			mContext.startService(service);
		}
	}

}
