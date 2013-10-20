package org.ischool.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UsrIfoDBOperation
{
	private DatabaseHelper dbHelper;
	private Context context;
	SQLiteDatabase db;

	/*
	 * 初始化
	 */
	public UsrIfoDBOperation(Context pContext)
	{
		this.context = pContext;
		dbHelper = new DatabaseHelper(context, "usrifo.db", null, 1);
		db = dbHelper.getWritableDatabase();
	}
	
	/*
	 * 创建数据库
	 */
	public void CreatDB()
	{
		String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + "usrifo"
				+ " (" + "usr_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " // 用户的Id
				+ "renren_name" + " VARCHAR," //人人账号
				+ "renren_pwd" + " VARCHAR," //人人密码
				+ "stu_name" + " VARCHAR," //学号
				+ "stu_pwd" + " VARCHAR," //密码
				+ "sina_name" + " VARCHAR," //新浪账号
				+ "sina_pwd" + " VARCHAR);" ;//新浪密码
		try
		{
			db.execSQL(DATABASE_CREATE);
			Log.i("DB", "usr表创建成功" + DATABASE_CREATE);

		} catch (SQLException ex)
		{
			Log.i("DB", "usr表创建错误" + ex.toString());
		}
	}
	
	/*
	 * 数据库插入
	 */
	public void InsertDB(ContentValues values)
	{
		ContentValues values2 = new ContentValues();
		values2.put("usr_id", values.getAsInteger("usr_id"));
		values2.put("renren_name", values.getAsString("renren_name"));
		values2.put("renren_pwd", values.getAsString("renren_pwd"));
		values2.put("stu_num", values.getAsString("stu_num"));
		values2.put("stu_pwd", values.getAsString("stu_pwd"));
		values2.put("sina_name", values.getAsString("sina_name"));
		values2.put("sina_pwd", values.getAsString("sina_pwd"));

		try
		{
			db.insertOrThrow("usrifo", null, values2);
			Log.i("DB", "usrifo成功插数据" + values2.toString());
		} catch (SQLException ex)
		{
			Log.i("DB", "usrifo插入数据失败" + ex.toString());
		}
	}

	/*
	 * 更新数据库
	 */
	public void UpdateDB(String keyWord, ContentValues values)
	{
		ContentValues values2 = values;
		values2.put(keyWord+"_name", values.getAsString("name"));
		values2.put(keyWord+"_pwd", values.getAsString("pwd"));
		
		try
		{
			db.update("usrifo", values2, null, null);
		} catch (Exception e)
		{
			Log.i("DB", "usr表更新错误" + e.toString());
		}
	}
}
