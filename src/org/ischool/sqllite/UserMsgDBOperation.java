package org.ischool.sqllite;

import org.ischool.mod.MsgInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserMsgDBOperation
{
	private DatabaseHelper dbHelper;
	private Context context;
	private SQLiteDatabase db;

	public UserMsgDBOperation(Context pContext)
	{
		this.context = pContext;
		dbHelper = new DatabaseHelper(context, "ischool.db", null, 1);
		db = dbHelper.getWritableDatabase();
	}

	public void OpenDB()
	{
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * 创建数据库
	 */
	public void CreatDB()
	{
		String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + "userMsg" //表名称
				+ " (" + "msgID" + " INTEGER PRIMARY KEY, " // 消息的Id
				+ "msgTitle" + " VARCHAR, " //消息的标题
				+ "msgType" + " VARCHAR, " //消息的类型
				+ "content" + " VARCHAR, " //消息的内容
				+ "userID" + " VARCHAR, " //发送者id
				+ "latitude" + " VARCHAR, " //发送者当时的经度
				+ "longitude" + " VARCHAR, " //纬度
				+ "radius" + " VARCHAR, " //比南京
				+ "readed" + " VARCHAR, " //是否已读 0:未读 1：已读
				+ "postTime" + " VARCHAR);";//发送的时间
		try
		{
			db.execSQL(DATABASE_CREATE);
			db.close();
			Log.i("DB", "userMsg表创建成功" + DATABASE_CREATE);

		} catch (SQLException ex)
		{
			Log.i("DB", "userMsg表创建错误" + ex.toString());
		}
	}

	/**
	 * 数据库插入
	 * 
	 * @param values
	 */
	public boolean InsertDB(ContentValues values)
	{
		ContentValues values2 = new ContentValues();
		values2.put("msgID", values.getAsInteger("msgID"));
		values2.put("msgTitle", values.getAsString("msgTitle"));
		values2.put("msgType", values.getAsString("msgType"));
		values2.put("content", values.getAsString("content"));
		values2.put("userID", values.getAsString("userID"));
		values2.put("latitude", values.getAsString("latitude"));
		values2.put("longitude", values.getAsString("longitude"));
		values2.put("radius", values.getAsString("radius"));
		values2.put("readed", values.getAsString("readed"));
		values2.put("postTime", values.getAsString("postTime"));

		try
		{
			db.insertOrThrow("userMsg", null, values2);
			db.close();
			Log.i("DB", "userMsg成功插数据" + values2.toString());
			return true;
		} catch (SQLException ex)
		{
			Log.i("DB", "userMsg插入数据失败" + ex.toString());
			return false;
		}
	}

	/**
	 * 更新数据库
	 * 
	 * @param keyWord
	 * @param values
	 */
	public void UpdateDB(String[] msgID)
	{
		ContentValues values2 = new ContentValues();
		values2.put("readed", "1");
		try
		{
			db.update("userMsg", values2, "msgID", msgID);
			db.close();
			Log.i("DB", "userMsg表更新成功");
		} catch (Exception e)
		{
			Log.i("DB", "userMsg表更新错误" + e.toString());
		}
	}

	/**
	 * 根据msgID查询msg的信息
	 * 
	 * @param pMsgID
	 * @return
	 */
	public MsgInfo SearchByMsgID(String pMsgID)
	{
		MsgInfo msgInfo = new MsgInfo();
		String[] colums =
		{ "msgTitle", "msgType", "content", "userID", "postTime" };
		String like = "%" + pMsgID + "%";
		String[] selectionArgs =
		{ like };
		try
		{
			Cursor cursor = db.query("userMsg", colums, "msgID like?", selectionArgs, null, null, null);
			cursor.moveToFirst();
			msgInfo.setMsgTitle(cursor.getString(0));
			msgInfo.setMsgType(cursor.getString(1));
			msgInfo.setContent(cursor.getString(2));
			msgInfo.setUserName(cursor.getString(3));
			msgInfo.setPostTime(cursor.getString(4));
		} catch (Exception e)
		{
			Log.i("MsgDBOperation", e.toString());
		}
		db.close();
		return msgInfo;
	}

	/**
	 * 关闭数据库
	 */
	public void CloseDB()
	{
		this.db.close();
	}
}
