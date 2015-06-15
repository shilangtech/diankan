package com.shilangtech.diankan.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.shilangtech.diankan.DianKan;
import com.shilangtech.diankan.R;
import com.shilangtech.diankan.R.anim;
import com.shilangtech.diankan.R.drawable;
import com.shilangtech.diankan.R.id;
import com.shilangtech.diankan.R.layout;
import com.shilangtech.diankan.message_send.BadgeView;
import com.shilangtech.diankan.message_send.EnlargeAnimationOut;
import com.shilangtech.diankan.message_send.FaceGVAdapter;
import com.shilangtech.diankan.message_send.FaceVPAdapter;
import com.shilangtech.diankan.message_send.ImageButtonExtend;
import com.shilangtech.diankan.message_send.ImageLoaderAdapter;
import com.shilangtech.diankan.message_send.RecordUtil;
import com.shilangtech.diankan.message_send.SelectPicActivity;
import com.shilangtech.diankan.message_send.ShrinkAnimationOut;
import com.shilangtech.diankan.message_send.SpringAnimation;
import com.shilangtech.diankan.message_send.ZoomAnimation;
import com.shilangtech.diankan.serverproxy.DatabaseDetails;
import com.shilangtech.diankan.serverproxy.ServerProxy;
import com.shilangtech.diankan.serverproxy.DatabaseDetails.MODUL;
import com.shilangtech.diankan.util.CommonUtils;
import com.shilangtech.diankan.util.DeviceUtility;
import com.shilangtech.diankan.util.Message;

/**
 * @author Google_acmer
 * 
 * 
 *
 */
public class MessageSend implements android.view.View.OnClickListener {

	private static final float FACE_SCALE = 0.6f;
	private int RESULT_CODE_1 = 1;
	private int RESULT_CODE_2 = 2;
	private EditText input;
	private int columns = 6;
	private int rows = 3;
	private List<View> views = new ArrayList<View>();
	private List<String> staticFacesList;
	private ImageView imageViewPlus;
	private ViewGroup menusWrapper;
	private Animation animRotateClockwise;
	private Animation animRotateAntiClockwise;
	private boolean areMenusShowing;
	private RecordUtil record;
	private ViewPager mViewPager;
	private LinearLayout mDotsLayout;
	private LinearLayout face_container;
	private LinearLayout record_container;
	private Button record_btn;
	private ViewGroup SendMode;
	private boolean areVerticalMenusShowing;
	private TextView sendMessageBtn;
	private long startVoiceT;
	private String voiceName;
	private Handler mHandler = new Handler();
	private Chronometer timer;
	private TextView timer_word;
	private TextView rerecord;
	int flag = 1;
	private boolean StartRecording;
	private TextView btn_to_word;
	private AlphaAnimation mAnimation_Alpha;
	private ImageView message_image1;
	private RelativeLayout Record_anim_layout;
	private RelativeLayout recording_editText_layout;
	private RelativeLayout message_editText_layout;
	private RelativeLayout record_anim_layout;
	private RelativeLayout record_anim_bg;
	private long TimeTotal;
	private TextView record_totalTime;
	private ImageView record_anim;
	Activity context;
	private ImageButtonExtend send_mode_public;
	private ImageButtonExtend send_mode_private;
	private String Message_RecordFile = null;// 录音文件路径
	private ArrayList<String> Message_PicPath;// 图片路径
	private String Message_text;// 消息文字
	private boolean Message_IsPublic;// 消息权限
	private int theme;
	Message message;
	private ImageView imageview_zan;
	private LinearLayout imageview_collect;
	private ImageButtonExtend menu_pic;
	private ImageButtonExtend menu_camera;
	private Button save_message;
	int fatherID;

	/**
	 * @param context
	 * @param theme
	 *            Theme=0 带有点赞，收藏按钮 Theme=1 不带点赞，收藏按钮 Theme=2 只含有 表情和语音
	 */
	public MessageSend(Activity context, int theme) {
		// super(context);
		this.context = context;
		this.theme = theme;

		initStaticFaces();
		initViews();
		InitTheme();

	}
	
	public MessageSend(Activity context, int theme, int id) {
		this(context, theme);
		fatherID = id;
		getLocation(context);
		
	}
	
	LocationClient mLocClient;
	private double lng = 0;
	private double lat = 0;
	private String province;
	private String city;
	private String district;
	private String address;
	ProgressDialog mProDialog;
	private void getLocation(Activity context) {
		mLocClient = new LocationClient(context);
		mLocClient.registerLocationListener(new MyBDLocationListener());
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setAddrType("all");
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
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
			address = location.getAddrStr();
			Log.e("test", "location is " + location.getAddrStr());
			mLocClient.stop();
		}
	}
	public Message getMessage() {
		return message;
	}

	/**
	 * Theme=0 带有点赞，收藏按钮 Theme=1 不带点赞，收藏按钮 Theme=2 只含有 表情和语音
	 */
	private void InitTheme() {
		if (0 == theme) {
			imageview_zan.setVisibility(View.VISIBLE);
			imageview_collect.setVisibility(View.VISIBLE);
			input.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					imageview_zan.setVisibility(View.GONE);
					imageview_collect.setVisibility(View.GONE);
					imageViewPlus.setVisibility(View.VISIBLE);
					sendMessageBtn.setVisibility(View.VISIBLE);
					if (face_container.getVisibility() == View.VISIBLE) {
						face_container.setVisibility(View.GONE);
					}
					return false;
				}
			});

			menusWrapper = (ViewGroup) context.findViewById(R.id.menus_wrapper);

			for (int i = 0; i < menusWrapper.getChildCount(); i++) {
				menusWrapper.getChildAt(i).setOnClickListener(
						new SpringMenuLauncher(null, i));
			}
		} else if (1 == theme) {
			imageViewPlus.setVisibility(View.VISIBLE);
			sendMessageBtn.setVisibility(View.VISIBLE);
			input.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (face_container.getVisibility() == View.VISIBLE) {
						face_container.setVisibility(View.GONE);
					}
					return false;
				}
			});
			menusWrapper = (ViewGroup) context.findViewById(R.id.menus_wrapper);

			for (int i = 0; i < menusWrapper.getChildCount(); i++) {
				menusWrapper.getChildAt(i).setOnClickListener(
						new SpringMenuLauncher(null, i));
			}

		} else if (2 == theme) {
			imageViewPlus.setVisibility(View.VISIBLE);
			sendMessageBtn.setVisibility(View.GONE);
			save_message = (Button) context.findViewById(R.id.save_message);
			save_message.setVisibility(View.VISIBLE);

			save_message.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
			menusWrapper = (ViewGroup) context.findViewById(R.id.menus_wrapper);
			menusWrapper.removeViews(2, 2);
			for (int i = 0; i < menusWrapper.getChildCount(); i++) {
				menusWrapper.getChildAt(i).setOnClickListener(
						new SpringMenuLauncher(null, i));
			}

			input.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (face_container.getVisibility() == View.VISIBLE) {
						face_container.setVisibility(View.GONE);
					}
					return false;
				}
			});
		}
	}

	/**
	 * 初始化布局
	 */
	private void initViews() {

		Message_PicPath = new ArrayList<String>();
		record_anim = (ImageView) context.findViewById(R.id.record_anim);
		sendMessageBtn = (TextView) context.findViewById(R.id.send_message);
		message_editText_layout = (RelativeLayout) context
				.findViewById(R.id.message_editText_layout);
		record_anim_layout = (RelativeLayout) context
				.findViewById(R.id.record_anim_layout);
		recording_editText_layout = (RelativeLayout) context
				.findViewById(R.id.recording_editText_layout);
		record_anim_bg = (RelativeLayout) context
				.findViewById(R.id.record_anim_bg);

		timer = (Chronometer) context.findViewById(R.id.timer);
		timer.setFormat("%s");
		timer_word = (TextView) context.findViewById(R.id.message_press_record);
		rerecord = (TextView) context.findViewById(R.id.message_rerecord);

		imageViewPlus = (ImageView) context.findViewById(R.id.imageview_plus);
		imageview_zan = (ImageView) context.findViewById(R.id.imageview_zan);
		imageview_collect = (LinearLayout) context
				.findViewById(R.id.imageview_collect);

		SendMode = (ViewGroup) context.findViewById(R.id.message_send_mode);
		input = (EditText) context.findViewById(R.id.message_editText);

		btn_to_word = (TextView) context.findViewById(R.id.btn_toWord);

		face_container = (LinearLayout) context.findViewById(R.id.face_content);
		record_container = (LinearLayout) context
				.findViewById(R.id.record_content);
		record_btn = (Button) record_container.findViewById(R.id.record_btn);
		message_image1 = (ImageView) context.findViewById(R.id.message_image1);

		mViewPager = (ViewPager) context.findViewById(R.id.face_viewpager);
		send_mode_public = (ImageButtonExtend) SendMode
				.findViewById(R.id.send_mode_public);
		send_mode_private = (ImageButtonExtend) SendMode
				.findViewById(R.id.send_mode_private);

		send_mode_public.setOnClickListener(this);
		send_mode_private.setOnClickListener(this);

		rerecord.setOnClickListener(this);
		record_anim_bg.setOnClickListener(this);
		input.setOnClickListener(this);
		btn_to_word.setOnClickListener(this);
		imageViewPlus.setOnClickListener(this);
		imageview_collect.setOnClickListener(this);
		imageview_zan.setOnClickListener(this);
		sendMessageBtn.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(new PageChange());

		// 表情下小圆点
		mDotsLayout = (LinearLayout) context
				.findViewById(R.id.face_dots_container);
		InitViewPager();

		animRotateClockwise = AnimationUtils.loadAnimation(context,
				R.anim.rotate_clockwise);
		animRotateAntiClockwise = AnimationUtils.loadAnimation(context,
				R.anim.rotate_anticlockwise);
		mAnimation_Alpha = new AlphaAnimation(1f, 0f);

		record_btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO 自动生成的方法存根
				if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
					flag = 2;

				}
				if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {
					flag = 1;
					if (StartRecording) {
						stopRecord();
						if (TimeTotal >= 1)
							startRecordAnimition();
						else
							Toast.makeText(context, "TOO Short!", 1).show();
						Message_RecordFile = Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/record.amr";
						StartRecording = false;
					}
				}
				return false;

			}
		});
		record_btn.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// TODO 自动生成的方法存根
				startRecord();
				StartRecording = true;
				// Log.e("TAG1","q----");
				return true;
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		// listener.onClick(v);

		switch (v.getId()) {
		case R.id.send_mode_public:
			Log.d("TAG", "---public");
			SpringAnimation.startAnimations(this.SendMode,
					ZoomAnimation.Direction.DOWN,
					DeviceUtility.getScreenSize(context));
			areVerticalMenusShowing = !areVerticalMenusShowing;
			message = new Message(input.getText().toString(),
					Message_RecordFile, Message_PicPath, true);
			String pic_path = null;
			if (!message.getPic_path().isEmpty()) {
				for (int i = 0; i < message.getPic_path().size(); i++)
					pic_path += message.getPic_path().get(i) + " | ";
			}
			String location = null;
			// Toast.makeText(context,"内容:"+message.getText()+"录音:"+message.getRecord_path()+"图片："+pic_path+"权限:"+message.isIsPublic(),1).show();
			sendComment(message.getText(), Message_RecordFile,
					message.getPic_path(), location, message.isIsPublic());
			((DetailActivity)context).updateListView();
			input.setText("");
			message.CleanMessage();
			break;
		case R.id.send_mode_private:
			// Log.d("TAG", "---private");
			SpringAnimation.startAnimations(this.SendMode,
					ZoomAnimation.Direction.DOWN,
					DeviceUtility.getScreenSize(context));
			areVerticalMenusShowing = !areVerticalMenusShowing;
			message = new Message(input.getText().toString(),
					Message_RecordFile, Message_PicPath, false);
			String pic_path2 = null;
			if (!message.getPic_path().isEmpty()) {
				for (int i = 0; i < message.getPic_path().size(); i++)
					pic_path2 += message.getPic_path().get(i) + " | ";
			}
//			Toast.makeText(
//					context,
//					"内容:" + message.getText() + "录音:"
//							+ message.getRecord_path() + "图片：" + pic_path2
//							+ "权限:" + message.isIsPublic(), 1).show();
			String building = null;
			sendComment(message.getText(), Message_RecordFile,
					message.getPic_path(), building, message.isIsPublic());
			((DetailActivity)context).updateListView();
			input.setText("");
			message.CleanMessage();
			break;
		case R.id.send_message:
			showVerticalMenus();
			break;
		case R.id.imageview_plus:
			showLinearMenus();
			break;
		case R.id.imageview_zan:
			if (true == imageview_zan.isSelected()) {
				imageview_zan.setSelected(false);
				sendLike(false);
			} else {
				imageview_zan.setSelected(true);
				sendLike(true);
			}
			Toast.makeText(context, "点赞了", 1).show();
			break;
		case R.id.imageview_collect:
			if (true == imageview_collect.isSelected()) {
				imageview_collect.setSelected(false);
			} else {
				imageview_collect.setSelected(true);
			}
			Toast.makeText(context, "收藏了", 1).show();
			break;
		case R.id.message_editText:
			// System.out.println("---调用了");
			face_container.setVisibility(View.GONE);
			record_container.setVisibility(View.GONE);
			break;

		case R.id.message_rerecord:
			timer.setBase(SystemClock.elapsedRealtime());
			timer_word.setVisibility(View.VISIBLE);
			timer.setVisibility(View.VISIBLE);
			rerecord.setVisibility(View.GONE);
			record_anim_layout.setVisibility(View.GONE);
			record.release();
			Message_RecordFile = null;
			break;
		case R.id.btn_toWord:
			imageViewPlus.setVisibility(View.VISIBLE);
			btn_to_word.setVisibility(View.GONE);
			record_container.setVisibility(View.GONE);
			message_editText_layout.setVisibility(View.VISIBLE);
			recording_editText_layout.setVisibility(View.GONE);
			record_anim_layout.setVisibility(View.GONE);
			break;
		case R.id.record_anim_bg:
			record_anim.setBackgroundResource(R.anim.record_play_anim);
			final AnimationDrawable animation = (AnimationDrawable) record_anim
					.getBackground();
			animation.start();
			record.recordPlay();
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO 自动生成的方法存根
					animation.stop();
					record_anim.setBackgroundResource(R.drawable.record_amp3);
				}
			}, TimeTotal * 1000);

			break;
		}

	}

	private void sendLike(boolean like) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_FATHER, fatherID);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_MODULE, MODUL.LIKE);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_USER, DianKan.mUserInfo.nameID);
		ServerProxy server = ServerProxy.getServerProxyInstance(context);
		server.sendRequst(map);
	}
	
	
	private void sendComment(String detail, String record,
			ArrayList<String> pic, String building, boolean isPublic) {
		ServerProxy server = ServerProxy.getServerProxyInstance(context);
		Map<String, Object> map = new HashMap<String, Object>();
		long timeStamp = System.currentTimeMillis();
		Date today = new Date(timeStamp);

		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_DATE,
				new SimpleDateFormat("yyyy-MM-dd").format(today));
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_TIME,
				new SimpleDateFormat("HH:mm:ss").format(today));
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_USER, DianKan.mUserInfo.nameID);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_TIMESTAMP, timeStamp);

		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_DETAIL, detail);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_COUNTRY, "中国");
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_PROVINCE, province);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_CITY, city);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_AREA, district);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_ADDRESS, address);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_BUILDING, address);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_LNG, lng);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_LAT, lat);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_FATHER, fatherID);
		map.put(DatabaseDetails.ShilangBBS.COLUMN_NAME_MODULE, MODUL.COMMENT);
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

	/**
	 * 开始录音
	 */
	private void startRecord() {
		// record.start(name);
		record = new RecordUtil();
		record.start();
		timer.setBase(SystemClock.elapsedRealtime());
		timer.start();
		timer_word.setVisibility(View.VISIBLE);
		timer.setVisibility(View.VISIBLE);
		rerecord.setVisibility(View.GONE);

	}

	/**
	 * 结束录音
	 */
	private void stopRecord() {

		record.stop();
		String timelength = timer.getText().toString();
		timer.stop();
		TimeTotal = (SystemClock.elapsedRealtime() - timer.getBase()) / 1000;
		timer.setBase(SystemClock.elapsedRealtime());
		timer.setText(timelength);

		timer_word.setVisibility(View.GONE);
		rerecord.setVisibility(View.VISIBLE);

	}

	/**
	 * 录音动画
	 */
	private void startRecordAnimition() {
		record_anim_layout.setVisibility(View.VISIBLE);

		record_totalTime = (TextView) record_anim_layout
				.findViewById(R.id.record_time);
		String space = "";
		for (int i = 0; i < (int) (TimeTotal / 2); i++) {
			if (i < 25)
				space += " ";
		}
		record_totalTime.setText(space + TimeTotal);
		ScaleAnimation scale = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setDuration(500);
		scale.setInterpolator(new AccelerateDecelerateInterpolator());
		// 初始化
		Animation translate = new TranslateAnimation(
				((record_btn.getX() + record_btn.getWidth()) - recording_editText_layout
						.getX()),
				0.1f,
				(recording_editText_layout.getY() - (record_btn.getY() - record_btn
						.getHeight())), 0.1f);
		translate.setDuration(500);
		translate.setInterpolator(new AccelerateDecelerateInterpolator());
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(scale);
		set.addAnimation(translate);
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0f);
		controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
		record_anim_layout.setLayoutAnimation(controller);

	}

	/**
	 * 以横向直线型展开菜单,按钮旋转
	 */
	public void showLinearMenus() {

		int[] size = DeviceUtility.getScreenSize(context);

		if (!areMenusShowing) {
			SpringAnimation.startAnimations(this.menusWrapper,
					ZoomAnimation.Direction.SHOW, size);
			this.imageViewPlus.startAnimation(this.animRotateClockwise);
		} else {
			SpringAnimation.startAnimations(this.menusWrapper,
					ZoomAnimation.Direction.HIDE, size);
			this.imageViewPlus.startAnimation(this.animRotateAntiClockwise);
		}

		areMenusShowing = !areMenusShowing;
	}

	/**
	 * 以纵向直线型展开菜单
	 */
	public void showVerticalMenus() {
		// System.out.print("----发送了");
		int[] size = DeviceUtility.getScreenSize(context);
		if (!areVerticalMenusShowing) {
			SpringAnimation.startAnimations(this.SendMode,
					ZoomAnimation.Direction.UP, size);
		} else {
			SpringAnimation.startAnimations(this.SendMode,
					ZoomAnimation.Direction.DOWN, size);
		}

		areVerticalMenusShowing = !areVerticalMenusShowing;
	}

	// 横向分布菜单事件监听器
	private class SpringMenuLauncher implements OnClickListener {

		private int postion;

		private SpringMenuLauncher(Class<?> c, int postion) {

			this.postion = postion;
		}

		public void onClick(View v) {
			// TODO Auto-generated method stub
			startSpringMenuAnimations(v);
			setCheckState(postion);
			switch (v.getId()) {
			case R.id.menu_face:
				hideSoftInputView();// 隐藏软键盘
				if (face_container.getVisibility() == View.GONE) {
					face_container.setVisibility(View.VISIBLE);
					record_container.setVisibility(View.GONE);
				}

				break;
			case R.id.menu_record:
				hideSoftInputView();// 隐藏软键盘
				if (record_container.getVisibility() == View.GONE) {
					record_container.setVisibility(View.VISIBLE);
					face_container.setVisibility(View.GONE);
				}
				SpringAnimation.startAnimations(menusWrapper,
						ZoomAnimation.Direction.HIDE,
						DeviceUtility.getScreenSize(context));
				imageViewPlus.startAnimation(mAnimation_Alpha);
				areMenusShowing = !areMenusShowing;

				imageViewPlus.setVisibility(View.GONE);
				btn_to_word.setVisibility(View.VISIBLE);

				message_editText_layout.setVisibility(View.GONE);
				recording_editText_layout.setVisibility(View.VISIBLE);
				break;
			case R.id.menu_pic:
				Message_PicPath.clear();

				Intent intent1 = new Intent(context, SelectPicActivity.class);
				intent1.putExtra("From", context.getPackageName());
				context.startActivityForResult(intent1, RESULT_CODE_1);

				break;
			case R.id.menu_camera:

				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				context.startActivityForResult(intent2, RESULT_CODE_2);

				break;
			}
		}
	}

	/**
	 * 返回照片数据
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void MyOnActivityResult(int requestCode, int resultCode, Intent data) {

		// TODO 自动生成的方法存根
		{
			if (data != null) {
				if (resultCode == RESULT_CODE_1) {
					// Log.e("TAG1","从相册取数据");
					Bundle bundle = data.getExtras();
					ArrayList<String> getPic = bundle
							.getStringArrayList("PicSelect");

					for (int i = 0; i < getPic.size(); i++) {
						// Log.e("TAG1", getPic.get(i).toString());

						String picPath = getPic.get(i).toString();
						if (!Message_PicPath.contains(picPath))
							Message_PicPath.add(picPath);

					}
					// Message_PicPath=RemoveRepeat(Message_PicPath);
					if (Message_PicPath.size() != 0) {
						message_image1.setVisibility(View.VISIBLE);
						Bitmap bitmap = BitmapFactory
								.decodeFile(Message_PicPath.get(0).toString());
						Bitmap newbitmap = Bitmap.createScaledBitmap(bitmap,
								65, 65, false);
						BadgeView bv = new BadgeView();
						bv.draw(newbitmap, Message_PicPath.size());

						message_image1
								.setBackgroundDrawable(new BitmapDrawable(
										context.getResources(), newbitmap));
					} else {
						message_image1.setVisibility(View.GONE);
					}
				} else if (requestCode == RESULT_CODE_2) {
					// Log.e("TAG1", "照相机取数据");

					if (data.getExtras() != null) {
						Bundle bundle = data.getExtras();
						Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

						String name = DateFormat.format("yyyyMMdd_hhmmss",
								Calendar.getInstance(Locale.CHINA)) + ".jpg";
						FileOutputStream b = null;
						File file = new File(Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/Image/");
						file.mkdirs();// 创建文件夹
						String fileName = Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/Image/" + name;

						try {
							b = new FileOutputStream(fileName);
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} finally {
							try {
								b.flush();
								b.close();
								Message_PicPath.add(fileName);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						message_image1.setVisibility(View.VISIBLE);
						Bitmap newbitmap = Bitmap.createScaledBitmap(bitmap,
								65, 65, false);
						BadgeView bv = new BadgeView();
						bv.draw(newbitmap, Message_PicPath.size());
						message_image1
								.setBackgroundDrawable(new BitmapDrawable(
										context.getResources(), newbitmap));

					}
				}
			}
		}

	}

	/*
	 * 初始表情 *
	 */
	private void InitViewPager() {
		// 获取页数
		for (int i = 0; i < getPagerCount(); i++) {
			views.add(viewPagerItem(i));
			LayoutParams params = new LayoutParams(16, 16);
			mDotsLayout.addView(dotsItem(i), params);
		}
		FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
		mViewPager.setAdapter(mVpAdapter);
		mDotsLayout.getChildAt(0).setSelected(true);
	}

	private View viewPagerItem(int position) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.message_face_gridview, null);// 表情布局
		GridView gridview = (GridView) layout.findViewById(R.id.face_gv);
		/**
		 * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
		 * */
		List<String> subList = new ArrayList<String>();
		subList.addAll(staticFacesList
				.subList(position * (columns * rows - 1),
						(columns * rows - 1) * (position + 1) > staticFacesList
								.size() ? staticFacesList.size() : (columns
								* rows - 1)
								* (position + 1)));
		/**
		 * 末尾添加删除图标
		 * */
		subList.add("emotion_del_normal.png");
		FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, context);
		gridview.setAdapter(mGvAdapter);
		gridview.setNumColumns(columns);
		// 单击表情执行的操作
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					String png = ((TextView) ((LinearLayout) view)
							.getChildAt(1)).getText().toString();

					if (!png.contains("emotion_del_normal")) {// 如果不是删除图标
						insert(getFace(png));
					} else {
						delete();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		return gridview;
	}

	private SpannableStringBuilder getFace(String png) {
		// Log.d("TAG", "PNG---------"+png);
		SpannableStringBuilder sb = new SpannableStringBuilder();
		try {
			/**
			 * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
			 * 所以这里对这个tempText值做特殊处理
			 * 格式：#[face/png/f_static_000.png]#，以方便判斷當前圖片是哪一個
			 * */
			String tempText = "#[" + png + "]#";
			sb.append(tempText);
			Bitmap bf = BitmapFactory.decodeStream(context.getAssets()
					.open(png));
			Matrix matrix = new Matrix();
			matrix.postScale(FACE_SCALE, FACE_SCALE);
			Bitmap bm = Bitmap.createBitmap(bf, 0, 0, bf.getWidth(),
					bf.getHeight(), matrix, false);

			sb.setSpan(new ImageSpan(context, bm),
					sb.length() - tempText.length(), sb.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb;
	}

	/**
	 * 向输入框里添加表情
	 * */
	private void insert(CharSequence text) {
		int iCursorStart = Selection.getSelectionStart((input.getText()));
		int iCursorEnd = Selection.getSelectionEnd((input.getText()));
		if (iCursorStart != iCursorEnd) {
			((Editable) input.getText()).replace(iCursorStart, iCursorEnd, "");
		}
		int iCursor = Selection.getSelectionEnd((input.getText()));
		((Editable) input.getText()).insert(iCursor, text);

	}

	/**
	 * 删除图标执行事件
	 * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
	 * */
	private void delete() {
		if (input.getText().length() != 0) {
			int iCursorEnd = Selection.getSelectionEnd(input.getText());
			int iCursorStart = Selection.getSelectionStart(input.getText());
			if (iCursorEnd > 0) {
				if (iCursorEnd == iCursorStart) {
					if (isDeletePng(iCursorEnd)) {
						String st = "#[face/png/f_static_000.png]#";
						((Editable) input.getText()).delete(
								iCursorEnd - st.length(), iCursorEnd);
					} else {
						((Editable) input.getText()).delete(iCursorEnd - 1,
								iCursorEnd);
					}
				} else {
					((Editable) input.getText()).delete(iCursorStart,
							iCursorEnd);
				}
			}
		}
	}

	/**
	 * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
	 * **/
	private boolean isDeletePng(int cursor) {
		String st = "#[face/png/f_static_000.png]#";
		String content = input.getText().toString().substring(0, cursor);
		if (content.length() >= st.length()) {
			String checkStr = content.substring(content.length() - st.length(),
					content.length());
			String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(checkStr);
			return m.matches();
		}
		return false;
	}

	private ImageView dotsItem(int position) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dot_image, null);
		ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
		iv.setId(position);
		return iv;
	}

	/**
	 * 根据表情数量以及GridView设置的行数和列数计算Pager数量
	 * 
	 * @return
	 */
	private int getPagerCount() {
		int count = staticFacesList.size();
		return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
				: count / (columns * rows - 1) + 1;
	}

	/**
	 * 初始化表情列表staticFacesList
	 */
	private void initStaticFaces() {
		try {
			staticFacesList = new ArrayList<String>();
			String[] faces = context.getAssets().list("face/png");
			// 将Assets中的表情名称转为字符串一一添加进staticFacesList
			for (int i = 0; i < faces.length; i++) {
				staticFacesList.add(faces[i]);
			}
			// 去掉删除图片
			staticFacesList.remove("emotion_del_normal.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 表情页改变时，dots效果也要跟着改变
	 * */
	class PageChange implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
				mDotsLayout.getChildAt(i).setSelected(false);
			}
			mDotsLayout.getChildAt(arg0).setSelected(true);
		}

	}

	private void setCheckState(int postion) {
		for (int i = 0; i < menusWrapper.getChildCount(); i++) {
			if (i == postion) {
				menusWrapper.getChildAt(i).setSelected(true);
				// System.out.println(i+"---"+menusWrapper.getChildAt(i).isSelected());
			} else
				menusWrapper.getChildAt(i).setSelected(false);
			// System.out.println(i+"---"+menusWrapper.getChildAt(i).isSelected());

		}

	}

	/**
	 * 展现菜单动画效果
	 * 
	 * @param view
	 * @param runnable
	 */
	private void startSpringMenuAnimations(View view) {
		areMenusShowing = true;
		Animation shrinkOut1 = new ShrinkAnimationOut(300);
		Animation growOut = new EnlargeAnimationOut(300);
		shrinkOut1.setInterpolator(new AnticipateInterpolator(2.0F));
		shrinkOut1.setAnimationListener(new Animation.AnimationListener() {

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				imageViewPlus.clearAnimation();
			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}
		});

		view.startAnimation(growOut);
	}

	/**
	 * 隐藏软键盘
	 */
	public void hideSoftInputView() {

		InputMethodManager manager = ((InputMethodManager) context
				.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (context.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (context.getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(context.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * @return 完成按钮
	 */
	public Button getSaveButton() {

		return save_message;

	}

	/**
	 * @return 获得输入框内容
	 */
	public EditText getInput() {

		return input;

	}

	public String getRecordPath() {
		return Message_RecordFile + "";
	}

	public void clearRecordPath() {
		timer.setBase(SystemClock.elapsedRealtime());
		timer_word.setVisibility(View.VISIBLE);
		timer.setVisibility(View.VISIBLE);
		rerecord.setVisibility(View.GONE);
		record_anim_layout.setVisibility(View.GONE);
		record.release();
		Message_RecordFile = null;
	}

}
