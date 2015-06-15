package com.shilangtech.diankan.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shilangtech.diankan.DianKan;
import com.shilangtech.diankan.R;
import com.shilangtech.diankan.DianKan.UserInfo;
import com.shilangtech.diankan.R.id;
import com.shilangtech.diankan.R.layout;
import com.shilangtech.diankan.R.menu;
import com.shilangtech.diankan.serverproxy.DatabaseDetails.ShilangBBS;
import com.shilangtech.diankan.serverproxy.ServerProxy;
import com.shilangtech.diankan.serverproxy.DatabaseDetails;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements
		OnItemClickListener {

	ServerProxy mServerProxy;
	List<Map<String, Object>> data;


	EditText nameIDEditText;
	EditText pwdEditText; 
	private void showLoginDialog() {
        LayoutInflater factory = LayoutInflater 
                .from(this); 
        final View DialogView = factory.inflate( 
                R.layout.dialog, null); 
        nameIDEditText = (EditText)DialogView.findViewById(R.id.AccountEditText);
        pwdEditText = (EditText)DialogView.findViewById(R.id.PasswordEidtText);
        AlertDialog dlg = new AlertDialog.Builder(this) 
                .setTitle("登陆框") 
                .setView(DialogView) 
                .setPositiveButton("确定", 
                        new DialogInterface.OnClickListener() { 
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								String nameID = nameIDEditText.getText().toString();
								String pwd = pwdEditText.getText().toString();
								ServerProxy server = ServerProxy.getServerProxyInstance(MainActivity.this);
								int ret = server.login(nameID, pwd);
								if (ret == 0) {
									SharedPreferences pref = getSharedPreferences("ShilangBBS", 0);
									SharedPreferences.Editor editor = pref.edit();
									editor.putString("nameID", nameID);
									editor.putString("pwd", pwd);
							        editor.putLong("lastLogin", System.currentTimeMillis());
							        editor.commit();
							        DianKan.mUserInfo = new UserInfo(server.getUserInfo(nameID));
								} else {
									Toast.makeText(MainActivity.this, "用户名密码不正确，请重新登录。", Toast.LENGTH_LONG).show();
								}
									
								
							}
                        }) 
                .setNegativeButton("取消", 
                        new DialogInterface.OnClickListener() { 

                            @Override 
                            public void onClick( 
                                    DialogInterface dialog, 
                                    int which) { 
                            } 
                        }).create(); 
        dlg.show(); 	
		
	}
	
	private void showLogoutDialog() {
        AlertDialog dlg = new AlertDialog.Builder(this) 
                .setTitle("登陆框") 
                .setPositiveButton("确定", 
                        new DialogInterface.OnClickListener() { 
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								ServerProxy server = ServerProxy.getServerProxyInstance(MainActivity.this);
								DianKan.mUserInfo = null;
//								int ret = server.login(nameID, pwd);
//								if (ret == 0) {
//									SharedPreferences pref = getSharedPreferences("ShilangBBS", 0);
//									SharedPreferences.Editor editor = pref.edit();
//									editor.putString("nameID", nameID);
//									editor.putString("pwd", pwd);
//							        editor.putLong("lastLogin", System.currentTimeMillis());
//							        editor.commit();
//							        DianKan.mUserInfo = new UserInfo(server.getUserInfo(nameID));
//								} else {
//									Toast.makeText(MainActivity.this, "用户名密码不正确，请重新登录。", Toast.LENGTH_LONG).show();
//								}
//									
								
							}
                        }) 
                .setNegativeButton("取消", 
                        new DialogInterface.OnClickListener() { 

                            @Override 
                            public void onClick( 
                                    DialogInterface dialog, 
                                    int which) { 
                            } 
                        }).create(); 
        dlg.show(); 	
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		ImageButton new_post = (ImageButton) findViewById(R.id.new_post);
		new_post.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (DianKan.mUserInfo == null) {
					showLoginDialog();
				} else {
					startActivity(new Intent(MainActivity.this,
							CreateNewBoardActivity.class));
				}

			}

		});
		
		ImageButton settings = (ImageButton)findViewById(R.id.settings);
		settings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (DianKan.mUserInfo != null) {
					showLogoutDialog();
				} 

			}

		});
		mServerProxy = ServerProxy.getServerProxyInstance(this);
//
//		for (int i = 0; i < 11; i++) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			long timeStamp = System.currentTimeMillis();
//			Date today = new Date(timeStamp);
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			Log.e("test", "date is " + format.format(today));
//			Log.e("test",
//					"date is " + new SimpleDateFormat("HH:mm:ss").format(today));
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_DATE,
//					new SimpleDateFormat("yyyy-MM-dd").format(today));
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_TIME,
//					new SimpleDateFormat("HH:mm:ss").format(today));
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_TIMESTAMP, timeStamp);
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_TITLE, "第" + i
//					+ "条信息的标题");
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_DETAIL, "第" + i
//					+ "条信息");
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_COUNTRY, "中国");
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_PROVINCE, "辽宁");
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_CITY, "沈阳");
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_AREA, "和平");
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_ADDRESS, "鲁美");
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_BUILDING, "工业楼201");
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_LNG, 120.0988);
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_LAT, 44.8721);
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_USER, "test" + i % 3);
//			if (i != 0)
//				map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_FATHER, i - 1);
//			map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_MODULE, i % 3);
//			if (i % 2 == 0) {
//				Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(
//						R.drawable.pic_dir)).getBitmap();
//				map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_PIC1,
//						getIconData(bitmap));
//			}
//
//			mServerProxy.sendRequst(map);
//		}

//		listView.setAdapter(new MainListCustomAdapter(this, getData()));
//		listView.setOnItemClickListener(this);
	}
	
	protected void onResume() {
		super.onResume();
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(new MainListCustomAdapter(this, getData()));
		listView.setOnItemClickListener(this);
	}

	public List<Map<String, Object>> getData() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ShilangBBS.COLUMN_NAME_MODULE, DatabaseDetails.MODUL.MAIN);
		
		data = mServerProxy.getData(map);

		Log.e("test", "how many message :" + data.size());
		return data;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int positon,
			long id) {
		int infoID = (Integer) data.get(positon).get(
				DatabaseDetails.ShilangBBS._ID);
		Log.e("testapp", "on Item click is infoID=" + infoID);
		Intent intent = new Intent(MainActivity.this, DetailActivity.class);
		intent.putExtra("infoID", infoID);
		startActivity(intent);

	}
}
