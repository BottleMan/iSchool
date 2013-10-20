package org.ischool.sqllite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	
	public DatabaseHelper(Context context, String name, CursorFactory factory,	int version) {
		super(context, name, factory, version);	 
	}	 
	
	// 创建数据库
	@Override	 
	public void onCreate(SQLiteDatabase db) {
 
	}	

	// 升级数据库
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	// 打开数据库，返回数据库对象
	@Override 
    public void onOpen(SQLiteDatabase db) throws SQLException{ 
            super.onOpen(db); 
    } 

}