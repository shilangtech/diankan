package com.shilangtech.diankan.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shilangtech.diankan.R;
import com.shilangtech.diankan.R.id;
import com.shilangtech.diankan.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class CreateNewBoard2SearchActivity extends Activity{

	private ImageView back_btn;
	private ListView mList;
	private SimpleAdapter adapter;
	ArrayList<Map<String, Object>> mDatas = new ArrayList<Map<String, Object>>();//这个才是真正数据源
	ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private EditText board_search_editText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_newboard2search);
		back_btn=(ImageView)findViewById(R.id.backbtn);
		board_search_editText=(EditText)findViewById(R.id.board_search_editText);
		int nameSize = getIntent().getIntExtra("nameSize", 0);
		List<String> name = new ArrayList<String>();
		if (nameSize > 0) {			
			for (int i = 0; i < nameSize; i++) {
				String nameExtra = getIntent().getStringExtra("name" + i);
				name.add(nameExtra);
			}
		}
		
		
		getData(name);//初始化Map数据

		setListAdapter();
		setListListener();
		setEditTextListener();
           UpdateDatas();
		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				CreateNewBoardActivity.board_search_editText.setText(board_search_editText.getText().toString());
			}
		});
	}
	private void UpdateDatas() {
		// TODO Auto-generated method stub
		mDatas.clear();

		for(int i=0;i<mData.size();i++){
			if(mData.get(i).toString().contains(board_search_editText.getText().toString())){
				mDatas.add(mData.get(i));
			}
		}
		adapter.notifyDataSetChanged();
	}
	private void setEditTextListener() {
		// TODO Auto-generated method stub
		board_search_editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				UpdateDatas();


			}

		
		});
	}
	private void setListListener(){
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(!mDatas.get(arg2).get("text").toString().equals(""))
					board_search_editText.setText(mDatas.get(arg2).get("text").toString());

			}
		});
	}

	private void setListAdapter() {
		// TODO Auto-generated method stub
		mDatas.clear();

		mList=(ListView)findViewById(R.id.dingwei_search_list);
		adapter= new SimpleAdapter(getApplicationContext(), mDatas, R.layout.message_dingwei_search_list_items
				, new String[]{"text"}, new int[]{R.id.text});
		mList.setAdapter(adapter);
	}

	/**
	 * 这里应该从定位服务获得的三个备选地址，这里用仅供测试
	 * @param name 
	 */

	private void getData(List<String> name) {

		for (int i = 0; i < name.size(); i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("text", name.get(i));
			mData.add(item);
		}
	}

}
