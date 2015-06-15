package com.shilangtech.diankan.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shilangtech.diankan.R;
import com.shilangtech.diankan.R.drawable;
import com.shilangtech.diankan.R.id;
import com.shilangtech.diankan.R.layout;
import com.shilangtech.diankan.serverproxy.DatabaseDetails;
import com.shilangtech.diankan.serverproxy.DatabaseDetails.Users;
import com.shilangtech.diankan.serverproxy.ServerProxy;
import com.shilangtech.diankan.serverproxy.DatabaseDetails.ShilangBBS;


public class MainListCustomAdapter extends BaseAdapter {  
	  
    private static final String TAG = "CustomAdapter";
	private List<Map<String, Object>> data;  
    private LayoutInflater layoutInflater;  
    private Context context;  
    public MainListCustomAdapter(Context context, List<Map<String, Object>> data){
        this.context=context;  
        this.data=data;  
        this.layoutInflater=LayoutInflater.from(context);  
    }  
    /**
     * 组件集合，对应list_main.xml中的控件
     * @author Administrator
     */
    public final class Items{
    	public ImageView head_pic;
        public TextView name;
        public TextView time;
        public TextView distance;
        public TextView title;
        public TextView detail;
        public ImageView pic1;
        public ImageView pic2;
        public ImageView pic3;
        public ImageView pic4;
        public TextView addr;
       
        public TextView like_count;
        public TextView comment_count;
    }
    @Override  
    public int getCount() {  
        return data.size();  
    }  
    /** 
     * 获得某一位置的数据 
     */  
    @Override  
    public Object getItem(int position) {  
        return data.get(position);  
    }  
    /** 
     * 获得唯一标识 
     */  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {
    	Log.e("test", "main getView ,position is " + position);
        Items items=null;  
        //if(convertView==null){  
            items=new Items();  
            //获得组件，实例化组件  
            convertView=layoutInflater.inflate(R.layout.list_main, null);
            View view = (View)convertView.findViewById(R.id.main_layout);
            if(position%2==0) {
            	view.setBackgroundResource(R.drawable.fill_blue_background);
            	
            	}
	        items.head_pic=(ImageView)convertView.findViewById(R.id.head_pic);
	        items.name=(TextView)convertView.findViewById(R.id.name);
	        items.time=(TextView)convertView.findViewById(R.id.time);
	        items.distance=(TextView)convertView.findViewById(R.id.distance);
	        items.title = (TextView)convertView.findViewById(R.id.title);
	        items.detail=(TextView)convertView.findViewById(R.id.details);
	        items.pic1=(ImageView)convertView.findViewById(R.id.pic1);
	        items.pic2=(ImageView)convertView.findViewById(R.id.pic2);
	        items.pic3=(ImageView)convertView.findViewById(R.id.pic3);
	        items.pic4=(ImageView)convertView.findViewById(R.id.pic4);
	        items.addr=(TextView)convertView.findViewById(R.id.addr);
	        items.like_count=(TextView)convertView.findViewById(R.id.like_count);
	        items.comment_count=(TextView)convertView.findViewById(R.id.comment_count);
            convertView.setTag(items);  
//        }else{  
//            items=(Items)convertView.getTag();  
//        }  
        //绑定数据  

        ServerProxy server = ServerProxy.getServerProxyInstance(context);
        Map<String, Object> searchComentMap = new HashMap<String, Object>();
        searchComentMap.put(ShilangBBS.COLUMN_NAME_MODULE, DatabaseDetails.MODUL.COMMENT);
        searchComentMap.put(ShilangBBS.COLUMN_NAME_FATHER, data.get(position).get(ShilangBBS._ID));
		int coment_count = server.getData(searchComentMap).size();
		items.comment_count.setText(coment_count+"");
		
		Map<String, Object> searchLikeMap = new HashMap<String, Object>();
		searchLikeMap.put(ShilangBBS.COLUMN_NAME_MODULE, DatabaseDetails.MODUL.LIKE);
		searchLikeMap.put(ShilangBBS.COLUMN_NAME_FATHER, data.get(position).get(ShilangBBS._ID));
		int like_count = server.getData(searchLikeMap).size();
		items.like_count.setText(like_count+"");
		
        Map<String, Object> map = data.get(position);
        
		Map<String, Object> userInfo = server.getUserInfo((String)map.get(ShilangBBS.COLUMN_NAME_USER));
		items.name.setText((String)userInfo.get(Users.COLUMN_NAME_NAME));
		ByteArrayInputStream headPicStream = new ByteArrayInputStream((byte[]) userInfo.get(Users.COLUMN_NAME_HEADPIC));   
    	items.head_pic.setImageDrawable(Drawable.createFromStream(headPicStream, "img"));   
    	
        items.title.setText((String)map.get(ShilangBBS.COLUMN_NAME_TITLE));
        items.detail.setText((String)map.get(ShilangBBS.COLUMN_NAME_DETAIL));
        items.addr.setText((String )map.get(ShilangBBS.COLUMN_NAME_ADDRESS));
        items.addr.setText((String )map.get(ShilangBBS.COLUMN_NAME_BUILDING));
        items.time.setText((String )map.get(ShilangBBS.COLUMN_NAME_TIME));
        if(map.get(ShilangBBS.COLUMN_NAME_PIC1) != null) {
        	Log.e("test", "the title is " + items.title.getText().toString());
        	Log.e("test", "the map is " + map);
        	items.pic1.setVisibility(View.VISIBLE);
        	ByteArrayInputStream stream = new ByteArrayInputStream((byte[]) map.get(ShilangBBS.COLUMN_NAME_PIC1));   
        	items.pic1.setImageDrawable(Drawable.createFromStream(stream, "img"));   
        }
        if(map.get(ShilangBBS.COLUMN_NAME_PIC2) != null) {
        	items.pic2.setVisibility(View.VISIBLE);
        	ByteArrayInputStream stream = new ByteArrayInputStream((byte[]) map.get(ShilangBBS.COLUMN_NAME_PIC2));   
        	items.pic2.setImageDrawable(Drawable.createFromStream(stream, "img"));   
        }
        if(map.get(ShilangBBS.COLUMN_NAME_PIC3) != null) {
        	items.pic3.setVisibility(View.VISIBLE);
        	ByteArrayInputStream stream = new ByteArrayInputStream((byte[]) map.get(ShilangBBS.COLUMN_NAME_PIC3));   
        	items.pic3.setImageDrawable(Drawable.createFromStream(stream, "img"));   
        }
        if(map.get(ShilangBBS.COLUMN_NAME_PIC4) != null) {
        	items.pic4.setVisibility(View.VISIBLE);
        	ByteArrayInputStream stream = new ByteArrayInputStream((byte[]) map.get(ShilangBBS.COLUMN_NAME_PIC4));   
        	items.pic4.setImageDrawable(Drawable.createFromStream(stream, "img"));   
        }
        //items.father.setText();
        return convertView;  
    }

  
}  