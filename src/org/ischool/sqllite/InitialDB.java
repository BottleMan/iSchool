package org.ischool.sqllite;

import android.content.Context;
import android.util.Log;

public class InitialDB
{
	Context context;
	
	public InitialDB(Context pContext)
	{
		this.context = pContext;
	}
	
	/**
	 * 初始化用户信息的本地数据库
	 * @return
	 */
	public boolean setAppDB()
	{
		try
		{
			//setUsrIfoDB();
			setUserMsgDB();
			setScheduleDB();
			return true;
		} catch (Exception e)
		{
			Log.i("Initial DB", "Initial DB Error " + e.toString());
		}
		return false;
	}

	private void setScheduleDB()
	{
		ScheduDBOperation operation = new ScheduDBOperation(context);
		operation.CreatDB();
	}

	private void setUserMsgDB()
	{
		UserMsgDBOperation operation = new UserMsgDBOperation(context);
		operation.CreatDB();
	}

	/**
	 * 设置本地用户信息的数据库
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private void setUsrIfoDB() throws Exception
	{
		UsrIfoDBOperation usrIfoDBOperation = new UsrIfoDBOperation(context);
		usrIfoDBOperation.CreatDB();
	}

}
