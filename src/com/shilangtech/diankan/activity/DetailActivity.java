package com.shilangtech.diankan.activity;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shilangtech.diankan.R;
import com.shilangtech.diankan.R.id;
import com.shilangtech.diankan.R.layout;
import com.shilangtech.diankan.R.menu;
import com.shilangtech.diankan.serverproxy.ServerProxy;
import com.shilangtech.diankan.serverproxy.DatabaseDetails;
import com.shilangtech.diankan.serverproxy.DatabaseDetails.ShilangBBS;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DetailActivity extends Activity {

	ServerProxy mServerProxy;
	int id;
	ListView listView;
	MessageSend ms;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		int infoID = getIntent().getIntExtra("infoID", -1);
		if (infoID == -1) {
			Log.e("ERROR", "app has ocurred error, finish the activity");
			finish();
		}
		id = infoID;
		mServerProxy = ServerProxy.getServerProxyInstance(this);
		listView = (ListView) findViewById(R.id.list);
		DetailListCustomAdapter adapter = new DetailListCustomAdapter(this, getMainData(), getDetailData());
		listView.setAdapter(adapter);
		ms=new MessageSend(this,0, id);
		
		ImageView back = (ImageView)findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
	}
	
	public void updateListView() {
		DetailListCustomAdapter adapter = new DetailListCustomAdapter(this, getMainData(), getDetailData());
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public List<Map<String, Object>> getMainData() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DatabaseDetails.ShilangBBS._ID, id);

		return mServerProxy.getData(map);
	}
	public List<Map<String, Object>> getDetailData() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_FATHER, id);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_MODULE, DatabaseDetails.MODUL.COMMENT);


		return mServerProxy.getData(map);
	}
	
	/* 
	 * 返回照片数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		ms.MyOnActivityResult(requestCode, resultCode, data);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
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
