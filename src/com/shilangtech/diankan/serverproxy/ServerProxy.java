package com.shilangtech.diankan.serverproxy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.shilangtech.diankan.serverproxy.DatabaseDetails.ShilangBBS;
import com.shilangtech.diankan.serverproxy.DatabaseDetails.Users;

/**
 * Created by libaocai on 5/14/15.
 */
public class ServerProxy {

	private SQLiteDatabase mDb;
	private static Context mContext;
	private static ServerProxy mServerProxy;

	private ServerProxy() {

		createDB();
	}

	public static ServerProxy getServerProxyInstance(Context context) {
		mContext = context;
		if (mServerProxy == null) {
			mServerProxy = new ServerProxy();
		}

		return mServerProxy;
	}

	private void createDB() {
		MyDatabaseHelper databaseHelper = new MyDatabaseHelper(mContext);
		mDb = databaseHelper.getWritableDatabase();
	}
	
	/**
	 * 注册用户
	 * @param nameID
	 * @param pwd
	 * @return 0 注册成功， -1 id已经存在
	 */
	public int register(String nameID, String pwd) {
		String selection = Users.COLUMN_NAME_ID + "=" + nameID;
		Cursor cs = mDb.query(false, DatabaseDetails.Users.TABLE_NAME, null, selection, null, null, null, null, null);
		if (cs.getCount() == 0) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(Users.COLUMN_NAME_ID, nameID);
			contentValues.put(Users.COLUMN_NAME_PWD, pwd);
			mDb.insert(DatabaseDetails.Users.TABLE_NAME, null, contentValues);
		} else {
			return -1;
		}
		return 0;
	}
	
	/**
	 * 用户登录
	 * @param nameID
	 * @param pwd 
	 * @return 0 登录成功， -1 用户不存在， -2 密码错误， -3 其他错误
	 */
	public int login(String nameID, String pwd) {
		String selection = Users.COLUMN_NAME_ID + "=" + nameID;
		Cursor cs = mDb.query(false, DatabaseDetails.Users.TABLE_NAME, null, selection, null, null, null, null, null);
		if (cs.getCount() == 0) {
			return -1;
		}
		selection = Users.COLUMN_NAME_ID + "=" + nameID + " and " + Users.COLUMN_NAME_PWD + "=" + pwd;
		cs = mDb.query(false, DatabaseDetails.Users.TABLE_NAME, null, selection, null, null, null, null, null);
		if (cs.getCount() == 0) {
			return -2;
		} else if (cs.getCount() != 1) {
			return -3;
		}
		
		return 0;
	}

	/**
	 * 修改个人资料
	 * @param nameID map
	 */
	public void updateUserInfo(String nameID, Map<String, Object>map) {
		ContentValues contentValues = new ContentValues();

		//contentValues.put(DatabaseDetails.ShilangBBS._ID, i);
		if (map.get(Users.COLUMN_NAME_NAME) != null) {
			contentValues.put(DatabaseDetails.Users.COLUMN_NAME_NAME, map.get(Users.COLUMN_NAME_NAME).toString());
		}
		if (map.get(Users.COLUMN_NAME_HEADPIC) != null) {
			contentValues.put(DatabaseDetails.Users.COLUMN_NAME_HEADPIC, (byte[])(map.get(Users.COLUMN_NAME_HEADPIC)));
		}
		if (map.get(Users.COLUMN_NAME_BIRTHDAY) != null) {
			contentValues.put(DatabaseDetails.Users.COLUMN_NAME_BIRTHDAY, map.get(Users.COLUMN_NAME_BIRTHDAY).toString());
		}
		if (map.get(Users.COLUMN_NAME_GENDER) != null) {
			contentValues.put(DatabaseDetails.Users.COLUMN_NAME_GENDER, map.get(Users.COLUMN_NAME_GENDER).toString());
		}
		if (map.get(Users.COLUMN_NAME_COLOR) != null) {
			contentValues.put(DatabaseDetails.Users.COLUMN_NAME_COLOR, map.get(Users.COLUMN_NAME_COLOR).toString());
		}
		if (map.get(Users.COLUMN_NAME_MOOD) != null) {
			contentValues.put(DatabaseDetails.Users.COLUMN_NAME_MOOD, map.get(Users.COLUMN_NAME_MOOD).toString());
		}
		if (map.get(Users.COLUMN_NAME_PROVINCE) != null) {
			contentValues.put(DatabaseDetails.Users.COLUMN_NAME_PROVINCE, map.get(Users.COLUMN_NAME_PROVINCE).toString());
		}
		if (map.get(Users.COLUMN_NAME_CITY) != null) {
			contentValues.put(DatabaseDetails.Users.COLUMN_NAME_CITY, map.get(Users.COLUMN_NAME_CITY).toString());
		}
		mDb.update(DatabaseDetails.Users.TABLE_NAME,  contentValues, Users.COLUMN_NAME_ID + "=" + nameID, null);
	}
	
	/**
	 * 彻底注销用户
	 * @param nameID
	 */
	public void deleteUserInfo(String nameID, String pwd) {
		mDb.delete(DatabaseDetails.Users.TABLE_NAME, Users.COLUMN_NAME_ID + "=" + nameID, null);
		
	}
	public Map<String, Object> getUserInfo(String nameID) {
		String selection = Users.COLUMN_NAME_ID + "=" + nameID;
		Log.e("test", "get User info the selection is " + selection);
		Cursor cs = mDb.query(false, DatabaseDetails.Users.TABLE_NAME, null, selection, null, null, null, null, null);
		if (cs == null) {
			Log.e("ServerProxy", "getUserInfo error, cusor is null.");
			return null;
		}
		if (cs.getCount() != 1) {
			Log.e("ServerProxy", "getUserInfo error, cusor count must be 1.");
			return null;
		}
		cs.moveToFirst();
		Map<String, Object> newMap = new HashMap<String, Object>();
		newMap.put(Users.COLUMN_NAME_ID, nameID);
		newMap.put(Users.COLUMN_NAME_PWD, cs.getString(cs.getColumnIndex(Users.COLUMN_NAME_PWD)));
		newMap.put(Users.COLUMN_NAME_NAME, cs.getString(cs.getColumnIndex(Users.COLUMN_NAME_NAME)));
		newMap.put(Users.COLUMN_NAME_HEADPIC, cs.getBlob(cs.getColumnIndex(Users.COLUMN_NAME_HEADPIC)));
		newMap.put(Users.COLUMN_NAME_BIRTHDAY, cs.getString(cs.getColumnIndex(Users.COLUMN_NAME_BIRTHDAY)));
		newMap.put(Users.COLUMN_NAME_GENDER, cs.getString(cs.getColumnIndex(Users.COLUMN_NAME_GENDER)));
		newMap.put(Users.COLUMN_NAME_COLOR, cs.getString(cs.getColumnIndex(Users.COLUMN_NAME_COLOR)));
		newMap.put(Users.COLUMN_NAME_MOOD, cs.getString(cs.getColumnIndex(Users.COLUMN_NAME_MOOD)));
		newMap.put(Users.COLUMN_NAME_PROVINCE, cs.getString(cs.getColumnIndex(Users.COLUMN_NAME_PROVINCE)));
		newMap.put(Users.COLUMN_NAME_CITY, cs.getString(cs.getColumnIndex(Users.COLUMN_NAME_CITY)));

		return newMap;
	}
	/**
	 * 上传信息
	 * @param map 信息map
	 */
	public void sendRequst(Map<String, Object> map) {
		ContentValues contentValues = new ContentValues();

		//contentValues.put(DatabaseDetails.ShilangBBS._ID, i);
		if (map.get("date") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_DATE, map.get("date").toString());
		}
		if (map.get("time") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_TIME, map.get("time").toString());
		}
		if (map.get("timestamp") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_TIMESTAMP, map.get("timestamp").toString());
		}
		if (map.get("title") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_TITLE, map.get("title").toString());
		}
		if (map.get("detail") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_DETAIL, map.get("detail").toString());
		}
		if (map.get("country") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_COUNTRY, map.get("country").toString());
		}
		if (map.get("province") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_PROVINCE, map.get("province").toString());
		}
		if (map.get("city") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_CITY, map.get("city").toString());
		}
		if (map.get("area") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_AREA, map.get("area").toString());
		}
		if (map.get("address") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_ADDRESS, map.get("address").toString());
		}
		if (map.get("building") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_BUILDING, map.get("building").toString());
		}
		if (map.get("lng") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_LNG, ((Double) map.get("lng")).doubleValue());
		}
		if (map.get("lat") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_LAT, ((Double) map.get("lat")).doubleValue());
		}
		if (map.get("user") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_USER, map.get("user").toString());
		}
		if (map.get("father") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_FATHER, ((Integer) map.get("father")).intValue());
		}
		if (map.get("module") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_MODULE, ((Integer) map.get("module")).intValue());
		}
		if (map.get("pic1") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_PIC1,  (byte[])(map.get("pic1")));
		}
		if (map.get("pic2") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_PIC2,  (byte[])(map.get("pic2")));
		}
		if (map.get("pic3") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_PIC3,  (byte[])(map.get("pic3")));
		}
		if (map.get("pic4") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_PIC4,  (byte[])(map.get("pic4")));
		}
		if (map.get("audio") != null) {
			contentValues.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_AUDIO,  (byte[])(map.get("audio")));
		}

		mDb.insert(DatabaseDetails.ShilangBBS.TABLE_NAME, null, contentValues);
	}

	/**
	 * 获取信息
	 * @param map 请求条件的map
	 * @return  符合条件的map列表
	 */

	public List<Map<String, Object>>getData(Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Object country = map.get("country");
		Object city = map.get("city");
		Object area = map.get("area");
		Object address = map.get("address");
		Object building = map.get("building");
		Object lng = map.get("lng");
		Object lat = map.get("lat");
		Object user = map.get("user");
		Object father = map.get("father");
		Object module = map.get("module");
		Object id = map.get("_id");

		StringBuffer selectionTemp = new StringBuffer();
		selectionTemp.append("_id>0 and ");
		if (user != null) {
			selectionTemp.append("user='").append(user.toString()).append("' and ");
		}
		if (country != null) {
			selectionTemp.append("country='").append(country.toString()).append("' and ");
		}
		if (city != null) {
			selectionTemp.append("city='").append(city.toString()).append("' and ");
		}
		if (area != null) {
			selectionTemp.append("area='").append(area.toString()).append("' and ");
		}
		if (address != null) {
			selectionTemp.append("address='").append(address.toString()).append("' and ");
		}
		if (building != null) {
			selectionTemp.append("building='").append(building.toString()).append("' and ");
		}
		if (lng != null) {
			selectionTemp.append("lng=").append(((Double)(lng)).doubleValue()).append(" and ");
		}
		if (lat != null) {
			selectionTemp.append("lat=").append(((Double)(lat)).doubleValue()).append(" and ");
		}
		if (father != null) {
			selectionTemp.append("father=").append(((Integer)(father)).intValue()).append(" and ");
		}
		if (module != null) {
			selectionTemp.append("module=").append(((Integer)(module)).intValue()).append(" and ");
		}
		if (id != null) {
			selectionTemp.append("_id=").append(((Integer)(id)).intValue()).append(" and ");
		}
		selectionTemp.append("_id>0");
		String selection = selectionTemp.toString();

		//String[] args = {map.get("user").toString(), map.get("city").toString()};
		Log.e("test", "the selection is " + selection);
		Cursor cs = mDb.query(false, DatabaseDetails.ShilangBBS.TABLE_NAME, null, selection, null, null, null, ShilangBBS.COLUMN_NAME_TIMESTAMP + " desc", null);
		cs.moveToFirst();
		Log.e("test", "the cout is " + cs.getCount());
		for (int i = 0; i < cs.getCount(); i++) {
			Log.e("test", "get data user is " + cs.getString(cs.getColumnIndex("user")));
			Log.e("test", "get data detail is " + cs.getString(cs.getColumnIndex("detail")));
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put("user", cs.getString(cs.getColumnIndex("user")));
			newMap.put("title", cs.getString(cs.getColumnIndex("title")));
			newMap.put("detail", cs.getString(cs.getColumnIndex("detail")));
			newMap.put("module", cs.getString(cs.getColumnIndex("module")));
			newMap.put("date", cs.getString(cs.getColumnIndex("date")));
			newMap.put("time", cs.getString(cs.getColumnIndex("time")));
			newMap.put("timestamp", cs.getLong(cs.getColumnIndex("timestamp")));
			newMap.put("country", cs.getString(cs.getColumnIndex("country")));
			newMap.put("province", cs.getString(cs.getColumnIndex("province")));
			newMap.put("city", cs.getString(cs.getColumnIndex("city")));
			newMap.put("area", cs.getString(cs.getColumnIndex("area")));
			newMap.put("address", cs.getString(cs.getColumnIndex("address")));
			newMap.put("building", cs.getString(cs.getColumnIndex("building")));
			newMap.put("lng", cs.getDouble(cs.getColumnIndex("lng")));
			newMap.put("lat", cs.getDouble(cs.getColumnIndex("lat")));
			newMap.put("_id", cs.getInt(cs.getColumnIndex("_id")));
			newMap.put("pic1", cs.getBlob(cs.getColumnIndex("pic1")));
			newMap.put("pic2", cs.getBlob(cs.getColumnIndex("pic2")));
			newMap.put("pic3", cs.getBlob(cs.getColumnIndex("pic3")));
			newMap.put("pic4", cs.getBlob(cs.getColumnIndex("pic4")));
			newMap.put("audio", cs.getBlob(cs.getColumnIndex("audio")));

			cs.moveToNext();
			list.add(newMap);
		}
		return list;

	}
}
