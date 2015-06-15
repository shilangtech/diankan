package com.shilangtech.diankan.baidulocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class BaiduLocation implements OnGetGeoCoderResultListener {

	private static Context mContext;
	private static BaiduLocation mBaiduLocation;
	private static boolean getLocationOK = false;
	private static boolean searchOK = false;
	private static double lng = 0;
	private static double lat = 0;
	private String province;
	private String city;
	private String district;
	private Map<String, Object> locationMap;
	private ProgressDialog mProDialog;
	// 定位相关
	static LocationClient mLocClient;
	static GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	private BaiduLocation() {

		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
	}

	public static BaiduLocation getBaiduLocationInstance(Context context) {
		mContext = context;
		if (mBaiduLocation == null) {
			mBaiduLocation = new BaiduLocation();
		}
		return mBaiduLocation;
	}

	public void startSearch() {
		mProDialog = android.app.ProgressDialog.show(mContext, null, "正在定位....");
		Log.e("test", "start search is 111111*********************************************************");
		mLocClient = new LocationClient(mContext);
		mLocClient.registerLocationListener(new MyBDLocationListener());
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setAddrType("all");
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		mLocClient.start();

		getLocationOK = false;
		LatLng ptCenter = new LatLng(lat, lng);
		// 反Geo搜索
		mSearch.reverseGeoCode(new ReverseGeoCodeOption()
				.location(ptCenter));
	}

	public Map<String, Object> getSearchMap() {
		mSearch.destroy();
		searchOK = false;
		return locationMap;
	}
	public void desdroyLocationResource() {
		mLocClient.stop();
		mSearch.destroy();
	}

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
		locationMap = new HashMap<String, Object>();
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
		locationMap.put("address", address);
		locationMap.put("name", name);
		locationMap.put("lng", lng);
		locationMap.put("lat", lat);
		locationMap.put("province", province);
		locationMap.put("city", city);
		locationMap.put("district", district);
		mLocClient.stop();
		searchOK = true;
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
			
			getLocationOK = true;
		}
	}
}
