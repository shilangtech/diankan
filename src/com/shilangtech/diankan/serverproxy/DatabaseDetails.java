package com.shilangtech.diankan.serverproxy;

import android.net.Uri;

/**
 * 此类里面记录了数据库的表结构信息，及访问表需要的URI 里面的每个内部类对应一张表，所有的值都定义为静态常量
 * <p/>
 * 这些东西还是在类中定义完比较好，在引用一次properties 感觉也没有解决混乱的问题
 * <p/>
 * 同时在properties 中写了太多的信息后， 也会觉得properties 有些混乱
 * <p/>
 */
public class DatabaseDetails {

	/**
	 * URI definitions
	 */
	public static final String AUTHORITY = "com.shilangtech.diankandatabase.provider";

	/**
	 * The scheme part for this provider's URI
	 */
	private static final String SCHEME = "content://";

	/**
	 * The default sort order for this table
	 */
	public static final String DEFAULT_SORT_ORDER = "_ID ASC";

	/**
	 * 0-relative position of a note ID segment in the path part of a note ID
	 * URI
	 */
	public static final int _ID_PATH_POSITION = 1;

	/**
	 * ShilangBBS表
	 */
	public static final class ShilangBBS {
		/**
		 * Path part for the Notes URI
		 */
		private static final String PATH_ShilangBBS= "/ShilangBBS";

		/**
		 * Path part for the Note ID URI
		 */
		private static final String PATH_ShilangBBS_ID = "/ShilangBBS/";

		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY
				+ PATH_ShilangBBS);

		/**
		 * The content URI base for a single note. Callers must append a numeric
		 * note id to this Uri to retrieve a note
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME
				+ AUTHORITY + PATH_ShilangBBS_ID);

		public static final String TABLE_NAME = "ShilangBBS";

		public static final String _ID = "_id";

		public static final String COLUMN_NAME_DATE = "date";

		public static final String COLUMN_NAME_TIME = "time";
		
		// 留言的时间戳
		public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
		
		// 留言标题，评论和赞为空
		public static final String COLUMN_NAME_TITLE = "title";

		// 信息内容，赞为空
		public static final String COLUMN_NAME_DETAIL = "detail";

		// 国家
		public static final String COLUMN_NAME_COUNTRY = "country";

		// 省
		public static final String COLUMN_NAME_PROVINCE = "province";

		// 市
		public static final String COLUMN_NAME_CITY = "city";

		// 区
		public static final String COLUMN_NAME_AREA = "area";

		// 百度地图地址
		public static final String COLUMN_NAME_ADDRESS = "address";

		// 自定义位置
		public static final String COLUMN_NAME_BUILDING = "building";

		// 纬度
		public static final String COLUMN_NAME_LNG = "lng";

		// 经度
		public static final String COLUMN_NAME_LAT = "lat";

		// 信息所属的用户
		public static final String COLUMN_NAME_USER = "user";

		// 父信息，新帖为空
		public static final String COLUMN_NAME_FATHER = "father";

		// 信息类型，新帖、赞、评论
		public static final String COLUMN_NAME_MODULE = "module";

		// 留言中图片
		public static final String COLUMN_NAME_PIC1 = "pic1";
		public static final String COLUMN_NAME_PIC2 = "pic2";
		public static final String COLUMN_NAME_PIC3 = "pic3";
		public static final String COLUMN_NAME_PIC4 = "pic4";
		// 留言中语音
		public static final String COLUMN_NAME_AUDIO = "audio";
		
	}

	/**
	 * Users表
	 */
	public static final class Users {
		/**
		 * Path part for the Notes URI
		 */
		private static final String PATH_Users= "/Users";

		/**
		 * Path part for the Note ID URI
		 */
		private static final String PATH_Users_ID = "/Users/";
		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY
				+ PATH_Users);

		/**
		 * The content URI base for a single note. Callers must append a numeric
		 * note id to this Uri to retrieve a note
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME
				+ AUTHORITY + PATH_Users_ID);
 
		public static final String TABLE_NAME = "User";

		public static final String _ID = "_id";

		public static final String COLUMN_NAME_ID = "userID";
		
		public static final String COLUMN_NAME_PWD = "pwd";
		
		public static final String COLUMN_NAME_NAME = "name";
		
		public static final String COLUMN_NAME_HEADPIC = "headpic";

		// 生日
		public static final String COLUMN_NAME_BIRTHDAY = "birthday";
		
		// 性别
		public static final String COLUMN_NAME_GENDER = "gender";
		
		// 心情
		public static final String COLUMN_NAME_MOOD = "mood";

		// 家乡 省
		public static final String COLUMN_NAME_PROVINCE = "province";

		// 家乡 市
		public static final String COLUMN_NAME_CITY = "city";
		
		// 性格颜色
		public static final String COLUMN_NAME_COLOR = "color";
	}

	public static class MODUL {
		public static final int MAIN = 0;
		public static final int LIKE = 1;
		public static final int COMMENT = 2;
	}

}
