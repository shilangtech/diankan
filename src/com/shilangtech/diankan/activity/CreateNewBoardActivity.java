package com.shilangtech.diankan.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.shilangtech.diankan.DianKan;
import com.shilangtech.diankan.R;
import com.shilangtech.diankan.R.drawable;
import com.shilangtech.diankan.R.id;
import com.shilangtech.diankan.R.layout;
import com.shilangtech.diankan.baidulocation.BaiduLocation;
import com.shilangtech.diankan.message_send.SelectPicActivity;
import com.shilangtech.diankan.serverproxy.DatabaseDetails;
import com.shilangtech.diankan.serverproxy.ServerProxy;
import com.shilangtech.diankan.util.CommonUtils;
import com.shilangtech.diankan.util.FaceDecode;

/**
 * @author Google_acmer
 * 
 *         新增留言类
 * 
 * 
 *
 */
public class CreateNewBoardActivity extends Activity implements OnClickListener {

	ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private int RESULT_CODE_1 = 1;
	int[] STATE = { 1, 0, 0, 0, 0 }; // 1代表添加按钮框 0代表灰色框 -1代表已有图片
	int i;
	MessageSend ms;
	private EditText board_title_editText;
	private EditText board_detail_editText;
	private ImageView locate_btn;
	public static EditText board_search_editText;
	private RelativeLayout message_send_menu;
	private RelativeLayout message_send_content;
	private Button save_message;
	private ViewGroup imageViewGroup;
	private SimpleAdapter adapter;
	ListView dingwei_list;
	private ImageView send;
	private ImageView cancel;
	private String Message_RecordFile = "";// 录音文件路径
	private ArrayList<String> getPic;// 图片路径
	
	LocationClient mLocClient;
	GeoCoder mSearch;
	private double lng = 0;
	private double lat = 0;
	private String province;
	private String city;
	private String district;
	private Map<String, Object> mLocationMap;
	ProgressDialog mProDialog;
	private static boolean searchOK = false;
	private static boolean locationOK = false;
	private boolean networkAvailable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_newboard);
		hideSoftInputView();
		ms = new MessageSend(this, 2);

		initView();
		setImageViewListener();
		setImageViewGroupClickable();

		getLocation();
	}

	private void setImageViewListener() {
		// TODO 自动生成的方法存根

		imageViewGroup = (ViewGroup) findViewById(R.id.imageViewGroup);

		for (i = 0; i < imageViewGroup.getChildCount(); i++) {
			imageViewGroup.getChildAt(i).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							{
								Intent intent1 = new Intent(
										CreateNewBoardActivity.this,
										SelectPicActivity.class);
								intent1.putExtra("From", getPackageName());
								startActivityForResult(intent1, RESULT_CODE_1);

							}
						}
					});
		}

	}

	/**
	 * 设置图片选择Group状态
	 */
	private void setImageViewGroupClickable() {

		for (int i = 0; i < imageViewGroup.getChildCount(); i++) {
			if (STATE[i] == 1) {
				imageViewGroup.getChildAt(i).setClickable(true);
				imageViewGroup.getChildAt(i).setBackgroundResource(
						R.drawable.addpicture);
			} else if (STATE[i] == 0) {
				imageViewGroup.getChildAt(i).setClickable(false);
				imageViewGroup.getChildAt(i).setBackgroundResource(
						R.drawable.addpicturegray);
			} else if (STATE[i] == -1) {

				if (i == 3)
					imageViewGroup.getChildAt(i).setClickable(true);
				else
					imageViewGroup.getChildAt(i).setClickable(false);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CODE_1) {
			Bundle bundle = data.getExtras();
			getPic = bundle.getStringArrayList("PicSelect");
			int i;

			for (int j = 0; j < 4; j++) {
				STATE[j] = 0;
				STATE[0] = 1;
			}
			for (i = 0; i < getPic.size(); i++) {
				// Log.e("TAG1", getPic.get(i).toString());
				imageViewGroup.getChildAt(i).setBackgroundDrawable(
						new BitmapDrawable(getPic.get(i).toString()));
				STATE[i] = -1;
			}
			if (i < 4)
				STATE[i] = 1;
			setImageViewGroupClickable();

		}
	}

	private void getLocation() {
//		BaiduLocation bd = BaiduLocation.getBaiduLocationInstance(this);
//		bd.startSearch();

		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(new MyBDLocationListener());
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(new MyOnGetGeoCoderResultListener());
		
		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setAddrType("all");
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		mLocClient.start();
		locationOK = true;
		
		
	}

	class MyOnGetGeoCoderResultListener implements OnGetGeoCoderResultListener {

		@Override
		public void onGetGeoCodeResult(GeoCodeResult result) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				return;
			}
			
			List<PoiInfo> poiList = result.getPoiList();
			Log.e("test", "the poiList size is " + poiList.size());
			mLocationMap = new HashMap<String, Object>();
			ArrayList<String> address = new ArrayList<String>();
			ArrayList<String> name = new ArrayList<String>();
			
			for (int i=0; i<poiList.size(); i++) {
				address.add(poiList.get(i).address);
				name.add(poiList.get(i).name);
				Log.e("test", "address is " + poiList.get(i).address);
				Log.e("test", "the name is " + poiList.get(i).name);
				Log.e("test", "the city is " + poiList.get(i).city);
				Log.e("test", "poi is " + poiList.get(i).toString());
			}
			mLocationMap.put("address", address);
			mLocationMap.put("name", name);
			mLocationMap.put("lng", lng);
			mLocationMap.put("lat", lat);
			mLocationMap.put("province", province);
			mLocationMap.put("city", city);
			mLocationMap.put("district", district);

			mData.clear();
			getData();

			dingwei_list = (ListView) findViewById(R.id.dingwei_list);
			dingwei_list.setVisibility(View.VISIBLE);
			adapter = new SimpleAdapter(CreateNewBoardActivity.this, mData,
					R.layout.message_dingwei_list_items, new String[] { "text" },
					new int[] { R.id.text });
			dingwei_list.setAdapter(adapter);
			setListListener();
			board_search_editText.setText(((List<String>)mLocationMap.get("name")).get(0));
			if (mProDialog != null) {
				mProDialog.dismiss();
			}
			Log.e("test", "search ok");
		}

	}
	class MyBDLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			lng = location.getLongitude();
			lat = location.getLatitude();
			province = location.getProvince();
			city = location.getCity();
			district = location.getDistrict();
			Log.e("test", "location is " + location.getAddrStr());
			if (locationOK && lng!=0) {
				LatLng ptCenter = new LatLng(lat, lng);
				// 反Geo搜索
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(ptCenter));
				mLocClient.stop();
				locationOK = false;
			}
		}
	}

	private void setListListener() {
		dingwei_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (!mData.get(arg2).get("text").toString().equals("")) {
					board_search_editText.setText(mData.get(arg2).get("text")
							.toString());

				}
				dingwei_list.setVisibility(View.GONE);
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		board_title_editText = (EditText) findViewById(R.id.board_title_editText);
		board_detail_editText = (EditText) findViewById(R.id.board_detail_editText);
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(board_detail_editText.getWindowToken(), 0);
		locate_btn = (ImageView) findViewById(R.id.locate_btn);
		board_search_editText = (EditText) findViewById(R.id.board_search_editText);
		save_message = ms.getSaveButton();

		message_send_menu = (RelativeLayout) findViewById(R.id.message_send_menu);
		message_send_content = (RelativeLayout) findViewById(R.id.message_send_content);
		send = (ImageView) findViewById(R.id.board_send);
		cancel = (ImageView) findViewById(R.id.board_cancel);

		board_detail_editText.setOnClickListener(this);
		send.setOnClickListener(this);
		cancel.setOnClickListener(this);
		locate_btn.setOnClickListener(this);
		board_search_editText.setOnClickListener(this);
		save_message.setOnClickListener(this);
		setEditTextListener();

	}

	/**
	 * 这里应该从定位服务获得的三个备选地址，这里用仅供测试
	 */
	private void getData() {
		if (mLocationMap == null) return;
		List<String> name = (List<String>) mLocationMap.get("name");
		for (int i = 0; i < name.size(); i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("text", name.get(i));
			mData.add(item);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.board_cancel:
			finish();
			break;
		case R.id.board_send:
			Log.e("TAG", "---标题---" + board_title_editText.getText().toString()
					+ "---内容---" + board_detail_editText.getText().toString()
					+ "---地点---" + board_search_editText.getText().toString()
					+ "---录音---" + Message_RecordFile);
			if (!networkAvailable) {
				Toast.makeText(this, "网络不可用", Toast.LENGTH_LONG).show();
				return;
			}
			sendNewPost(board_title_editText.getText().toString(),
					board_detail_editText.getText().toString(),
					Message_RecordFile, getPic, board_search_editText.getText()
							.toString());
			finish();
			break;
		case R.id.board_detail_editText:
			hideSoftInputView();
			break;
		case R.id.save_message:
			hideSoftInputView();
			message_send_menu.setVisibility(View.VISIBLE);
			message_send_content.setVisibility(View.GONE);
			String voice_string = "";
			if (!ms.getRecordPath().equals("null")) {
				voice_string = "#[face/png/f_static_666.png]#";
				Message_RecordFile = ms.getRecordPath();
			}
			Log.d("TAG", "" + ms.getRecordPath());
			board_detail_editText.setText(FaceDecode.DecodeAndInsert(
					voice_string + board_detail_editText.getText().toString()
							+ ms.getInput().getText().toString(),
					CreateNewBoardActivity.this));
			ms.getInput().setText("");
			if (!ms.getRecordPath().equals("null"))
				ms.clearRecordPath();

			break;
		case R.id.locate_btn:
			 ConnectivityManager mConnectivityManager = (ConnectivityManager) this
             .getSystemService(Context.CONNECTIVITY_SERVICE);
			 NetworkInfo[] networkInfos = mConnectivityManager.getAllNetworkInfo();
             for (int i=0; i<networkInfos.length;i++) {
            	 if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
            		 networkAvailable = true;
            	 }
             }
             if (!networkAvailable) {
            	 board_search_editText.setText("定位失败");
			} else {
				board_search_editText.setText("正在定位…");
			}
             getLocation();
             mProDialog = android.app.ProgressDialog.show(this, null, "正在定位....");
             new Thread() {
            	 public void run() {
            		 try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		 if (mProDialog != null) {
            			 mProDialog.dismiss();
            		 }
            	 }
            	 
             }.start();
			break;
		case R.id.board_search_editText:
			Intent intent = new Intent(CreateNewBoardActivity.this,
					CreateNewBoard2SearchActivity.class);
			if (mLocationMap != null) {
				List<String> name = (List<String>)mLocationMap.get("name");
				intent.putExtra("nameSize", name.size());
				for (int i=0; i<name.size(); i++) {
					intent.putExtra("name"+i, name.get(i));
				}
			} else {
				intent.putExtra("nameSize", 0);
			}
			startActivity(intent);

			break;
		}
	}

	private void sendNewPost(String title, String detail, String record,
			ArrayList<String> pic, String building) {
		ServerProxy server = ServerProxy.getServerProxyInstance(this);
		Map<String, Object> map = new HashMap<String, Object>();
		long timeStamp = System.currentTimeMillis();
		Date today = new Date(timeStamp);

		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_DATE,
				new SimpleDateFormat("yyyy-MM-dd").format(today));
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_TIME,
				new SimpleDateFormat("HH:mm:ss").format(today));
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_TIMESTAMP, timeStamp);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_USER, DianKan.mUserInfo.nameID);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_TITLE, title);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_DETAIL, detail);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_COUNTRY, "中国");
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_PROVINCE, mLocationMap.get("province"));
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_CITY, mLocationMap.get("city"));
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_AREA, mLocationMap.get("district"));
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_ADDRESS, ((List)(mLocationMap.get("address"))).get(0));
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_BUILDING, building);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_LNG, mLocationMap.get("lng"));
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_LAT, mLocationMap.get("lat"));

		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_MODULE, DatabaseDetails.MODUL.MAIN);

		if (pic != null) {
			for (int i = 0; i < pic.size(); i++) {
				Bitmap bitmap = BitmapFactory.decodeFile(pic.get(i));
				String key = DatabaseDetails.ShilangBBS.COLUMN_NAME_PIC1;
				switch (i) {
				case 0:
					key = DatabaseDetails.ShilangBBS.COLUMN_NAME_PIC1;
					break;
				case 1:
					key = DatabaseDetails.ShilangBBS.COLUMN_NAME_PIC2;
					break;
				case 2:
					key = DatabaseDetails.ShilangBBS.COLUMN_NAME_PIC3;
					break;
				case 3:
					key = DatabaseDetails.ShilangBBS.COLUMN_NAME_PIC4;
					break;
				}

				map.put(key, CommonUtils.getIconData(bitmap));
			}

		}
		server.sendRequst(map);

	}

	private void setEditTextListener() {
		board_detail_editText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				hideSoftInputView();
				message_send_menu.setVisibility(View.GONE);
				message_send_content.setVisibility(View.VISIBLE);
				return false;

			}
		});
		board_title_editText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				message_send_content.setVisibility(View.GONE);

				return false;

			}
		});
	}

	private void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}