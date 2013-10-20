package org.ischool.sqllite;

import java.util.ArrayList;
import java.util.List;

import org.ischool.mod.ClassIfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ScheduDBOperation
{
	private DatabaseHelper dbHelper;
	private Context context;
	private SQLiteDatabase db;

	public ScheduDBOperation(Context pContext)
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
		String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + "schedule" //表名称
				+ " (" + "classID" + " INTEGER PRIMARY KEY, " // 课节的id
				+ "time" + " VARCHAR, " //上课时间

				+ "name" + " VARCHAR, " //课程名称
				+ "place" + " VARCHAR, " //上课地点
				+ "duration" + " VARCHAR, " //起止周次
				+ "teacher" + " VARCHAR, " //任课教师
				+ "comClass" + " VARCHAR, " //合班标识
				+ "stuClass" + " VARCHAR, " //合班标识

				+ "name2" + " VARCHAR, " //上课时间2
				+ "place2" + " VARCHAR, " //上课地点2
				+ "duration2" + " VARCHAR,"//起止周次2
				+ "teacher2" + " VARCHAR,"//任课教师2
				+ "comClass2" + " VARCHAR,"//赫本标识2
				+ "stuClass2" + " VARCHAR);"; //合班标识
		try
		{
			db.execSQL(DATABASE_CREATE);
			db.close();
			Log.i("scheduleDB", "schedule表创建成功" + DATABASE_CREATE);

		} catch (SQLException ex)
		{
			Log.i("scheduleDB", "schedule表创建错误" + ex.toString());
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
		values2.put("classID", values.getAsInteger("classID"));
		values2.put("time", values.getAsString("time"));

		values2.put("name", values.getAsString("name"));
		values2.put("place", values.getAsString("place"));
		values2.put("duration", values.getAsString("duration"));
		values2.put("teacher", values.getAsString("teacher"));
		values2.put("comClass", values.getAsString("comClass"));
		values2.put("stuClass", values.getAsString("stuClass"));

		values2.put("name2", values.getAsString("name2"));
		values2.put("place2", values.getAsString("place2"));
		values2.put("duration2", values.getAsString("duration2"));
		values2.put("teacher2", values.getAsString("teacher2"));
		values2.put("comClass2", values.getAsString("comClass2"));
		values2.put("stuClass2", values.getAsString("stuClass2"));

		try
		{
			OpenDB();
			db.insertOrThrow("schedule", null, values2);
			Log.i("scheduleDB", "schedule成功插数据" + values2.toString());
			return true;
		} catch (SQLException ex)
		{
			Log.i("scheduleDB", "schedule插入数据失败" + ex.toString());
			return false;
		}
	}

	/**
	 * 更新数据库
	 * 
	 * @param classID
	 * @param values
	 */
	public void UpdateDB(String[] classID, ContentValues values)
	{
		ContentValues values2 = new ContentValues();
		values2.put("time", values.getAsString("time"));

		values2.put("name", values.getAsString("name"));
		values2.put("place", values.getAsString("place"));
		values2.put("duration", values.getAsString("duration"));
		values2.put("teacher", values.getAsString("teacher"));
		values2.put("comClass", values.getAsString("comClass"));
		values2.put("stuClass", values.getAsString("stuClass"));

		values2.put("name2", values.getAsString("name2"));
		values2.put("place2", values.getAsString("place2"));
		values2.put("duration2", values.getAsString("duration2"));
		values2.put("teacher2", values.getAsString("teacher2"));
		values2.put("comClass2", values.getAsString("comClass2"));
		values2.put("stuClass2", values.getAsString("stuClass2"));

		try
		{
			OpenDB();
			db.update("schedule", values2, "classID", classID);
			Log.i("scheduleDB", "schedule表更新成功");
		} catch (Exception e)
		{
			Log.i("scheduleDB", "schedule表更新错误" + e.toString());
		}
	}

	/**
	 * 根据星期返回该天的全部课程
	 * 
	 * @param pDayOfWeek
	 * @return
	 */
	public List<ClassIfo> FindClassIfosByDayOfWeek()
	{
		List<ClassIfo> classIfos = new ArrayList<ClassIfo>();

		String[] columns =
		{ "time", //上课时间
				"name",//课程名称
				"place",//上课地点
				"duration",//起止周次
				"teacher",//任课教师
				"comClass",//合班标识
				"stuClass",//上课班级
				"name2",//
				"place2",//
				"duration2",//
				"teacher2",//
				"comClass2",//
				"stuClass2" };
		try
		{
			Cursor cursor = db.query("schedule", columns, null, null, null, null, null, null);
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++)
			{
				ClassIfo classIfo = new ClassIfo();
				classIfo.setTime(cursor.getString(0));
				classIfo.setTime2(cursor.getString(0));
				classIfo.setName(cursor.getString(1));
				classIfo.setPlace(cursor.getString(2));
				classIfo.setDuration(cursor.getString(3));
				classIfo.setTeacher(cursor.getString(4));
				classIfo.setComClass(cursor.getString(5));
				classIfo.setStuClass(cursor.getString(6));
				classIfo.setName2(cursor.getString(7));
				classIfo.setPlace2(cursor.getString(8));
				classIfo.setDuration2(cursor.getString(9));
				classIfo.setTeacher2(cursor.getString(10));
				classIfo.setComClass2(cursor.getString(11));
				classIfo.setStuClass2(cursor.getString(12));
				cursor.moveToNext();
				classIfos.add(classIfo);
			}

		} catch (Exception e)
		{
			Log.i("DB", "schedule " + e.toString());
		}

		db.close();
		return classIfos;
	}

	/**
	 * 关闭数据库
	 */
	public void CloseDB()
	{
		this.db.close();
	}

}
