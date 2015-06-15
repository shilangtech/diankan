package com.shilangtech.diankan.serverproxy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by libaocai on 5/6/15.
 */

public  class MyDatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static String DATABASE_FILENAME = "shilangbbs.db";

	public MyDatabaseHelper(Context context) {
		super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.e("DatabaseHelper", "onCreate is *************");

		StringBuffer strtemp = new StringBuffer();

		/**
		 * 建表时 create table 语句加上if not exists 语句 防止反复安装APP时，表已经存在的情况
		 *
		 */
//
//		db.execSQL("create table if not exists hero_info("
//				+ "id integer primary key,"
//				+ "name varchar,"
//				+ "level integer)");
		// 建立ShilangBBS表
		strtemp.append("CREATE TABLE if not exists ")
						.append(DatabaseDetails.ShilangBBS.TABLE_NAME)
						.append(" (")
						.append(DatabaseDetails.ShilangBBS._ID)
						.append(" INTEGER PRIMARY KEY,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_DATE)
						.append(" TEXT,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_TIME)
						.append(" TEXT,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_TIMESTAMP)
						.append(" REAL,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_TITLE)
						.append(" TEXT,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_DETAIL)
						.append(" TEXT,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_COUNTRY)
						.append(" TEXT,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_PROVINCE)
						.append(" TEXT,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_CITY)
						.append(" TEXT,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_AREA)
						.append(" TEXT,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_ADDRESS)
						.append(" TEXT,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_BUILDING)
						.append(" TEXT,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_LNG)
						.append(" REAL,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_LAT)
						.append(" REAL,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_USER)
						.append(" TEXT,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_FATHER)
						.append(" INTEGER,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_MODULE)
						.append(" INTEGER,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_PIC1)
						.append(" BLOB,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_PIC2)
						.append(" BLOB,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_PIC3)
						.append(" BLOB,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_PIC4)
						.append(" BLOB,")
						.append(DatabaseDetails.ShilangBBS.COLUMN_NAME_AUDIO)
						.append(" BLOB")
						.append(" );");
						Log.e("DatabaseHelper", "sql is " + strtemp.toString());
		db.execSQL(strtemp.toString());
		
		// 建立Users表
		strtemp = new StringBuffer();
		strtemp.append("CREATE TABLE if not exists ")
						.append(DatabaseDetails.Users.TABLE_NAME)
						.append(" (")
						.append(DatabaseDetails.Users._ID)
						.append(" INTEGER PRIMARY KEY,")
						.append(DatabaseDetails.Users.COLUMN_NAME_ID)
						.append(" TEXT,")
						.append(DatabaseDetails.Users.COLUMN_NAME_PWD)
						.append(" TEXT,")
						.append(DatabaseDetails.Users.COLUMN_NAME_NAME)
						.append(" REAL,")
						.append(DatabaseDetails.Users.COLUMN_NAME_BIRTHDAY)
						.append(" TEXT,")
						.append(DatabaseDetails.Users.COLUMN_NAME_GENDER)
						.append(" TEXT,")
						.append(DatabaseDetails.Users.COLUMN_NAME_MOOD)
						.append(" TEXT,")
						.append(DatabaseDetails.Users.COLUMN_NAME_PROVINCE)
						.append(" TEXT,")
						.append(DatabaseDetails.Users.COLUMN_NAME_CITY)
						.append(" TEXT,")
						.append(DatabaseDetails.Users.COLUMN_NAME_COLOR)
						.append(" TEXT,")
						.append(DatabaseDetails.Users.COLUMN_NAME_HEADPIC)
						.append(" BLOB")
						.append(" );");
						Log.e("DatabaseHelper", "sql is " + strtemp.toString());
		db.execSQL(strtemp.toString());
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}

