package com.shilangtech.diankan;

import java.io.ByteArrayInputStream;
import java.util.Map;

import android.app.Application;
import android.graphics.drawable.Drawable;

import com.baidu.mapapi.SDKInitializer;
import com.shilangtech.diankan.serverproxy.DatabaseDetails.Users;

public class DianKan extends Application{

	public static UserInfo mUserInfo = null;
	@Override
	public void onCreate() {
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
	}
	
	public static class UserInfo {
		public String nameID;
		public String pwd;
		public String name;
		public Drawable headPic;
		public String birthday;
		public String gender;
		public String mood;
		public String province;
		public String city;
		public String color;
		public  UserInfo(String nameID, String pwd, String name, Drawable headPic, String birthday,
				String gender, String mood, String province, String city, String color) {
			this.nameID = nameID;
			this.pwd = pwd;
			this.name = name;
			this.headPic = headPic;
			this.birthday = birthday;
			this.gender = gender;
			this.mood = mood;
			this.province = province;
			this.city = city;
			this.color = color;
		}
		public UserInfo(Map<String, Object>map) {
			this.nameID = (String) map.get(Users.COLUMN_NAME_ID);
			this.pwd = (String) map.get(Users.COLUMN_NAME_PWD);
			this.name = (String) map.get(Users.COLUMN_NAME_NAME);
			if (map.get(Users.COLUMN_NAME_HEADPIC) != null) {
				ByteArrayInputStream stream = new ByteArrayInputStream(
						(byte[]) map.get(Users.COLUMN_NAME_HEADPIC));
				this.headPic = Drawable.createFromStream(stream, "img");
			}
			this.birthday = (String) map.get(Users.COLUMN_NAME_BIRTHDAY);
			this.gender = (String) map.get(Users.COLUMN_NAME_GENDER);
			this.mood = (String) map.get(Users.COLUMN_NAME_MOOD);
			this.province = (String) map.get(Users.COLUMN_NAME_PROVINCE);
			this.city = (String) map.get(Users.COLUMN_NAME_CITY);
			this.color = (String) map.get(Users.COLUMN_NAME_COLOR);
		}
	}
}
