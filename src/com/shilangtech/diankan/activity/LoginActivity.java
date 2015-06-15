package com.shilangtech.diankan.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.shilangtech.diankan.DianKan;
import com.shilangtech.diankan.DianKan.UserInfo;
import com.shilangtech.diankan.R;
import com.shilangtech.diankan.R.id;
import com.shilangtech.diankan.R.layout;
import com.shilangtech.diankan.R.menu;
import com.shilangtech.diankan.serverproxy.ServerProxy;
import com.shilangtech.diankan.serverproxy.DatabaseDetails.Users;
import com.shilangtech.diankan.util.CommonUtils;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		new Thread() {
			public void run() {
				autoLogin();
			}
		}.start();
	}

	private void autoLogin() {
		
		SharedPreferences pref = getSharedPreferences("ShilangBBS", 0);
		String nameID = pref.getString("nameID", null);
		String pwd = pref.getString("pwd", null);
		long lastLogin = pref.getLong("lastLogin", 0);
		if (nameID != null && pwd != null && lastLogin != 0) {
			ServerProxy server = ServerProxy.getServerProxyInstance(this);
			int ret = server.login(nameID, pwd);
			
			if (ret == 0 && ((System.currentTimeMillis() - lastLogin) < (7*24*60*60*1000))) {// 7天内登录过
				SharedPreferences.Editor editor = pref.edit();
		        editor.putLong("lastLogin", System.currentTimeMillis());
		        editor.commit();
		        DianKan.mUserInfo = new UserInfo(server.getUserInfo(nameID));
			}
		} else {
			// 测试用
			demoData();
		}
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}

	private void demoData() {
		ServerProxy server = ServerProxy.getServerProxyInstance(this);
		for (int i = 1; i < 10; i++) {
			server.register("1000" + i, "1000" + i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Users.COLUMN_NAME_NAME, "user" + i );
			map.put(Users.COLUMN_NAME_BIRTHDAY, "198" + i + "-" + i + "-"
					+ (i + 10));
			map.put(Users.COLUMN_NAME_CITY, "沈阳");
			map.put(Users.COLUMN_NAME_COLOR, "蓝色");
			map.put(Users.COLUMN_NAME_GENDER, i % 2 == 0 ? "男" : "女");
//			String filePath = "/sdcard/resource/Camera/CoolShow_Z00000" + i
//					+ ".jpg";
//			Log.e("test", "the picture path is " + filePath);
			Bitmap bitmap;
			try {
				String file = "coolShow_Z00000" + i + ".jpg";
				Log.e("test", "file is " + file);
				bitmap = BitmapFactory
						.decodeStream(this.getAssets().open(file));
				map.put(Users.COLUMN_NAME_HEADPIC, CommonUtils.getIconData(bitmap));
			} catch (IOException e) {
				Log.e("test", "bitmap decode error");
				e.printStackTrace();
			}

			
			map.put(Users.COLUMN_NAME_MOOD, "第" + i + "天的心情.");
			map.put(Users.COLUMN_NAME_PROVINCE, "辽宁");

			server.updateUserInfo("1000" + i, map);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
