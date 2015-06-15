package com.shilangtech.diankan.util;

import android.content.Context;
import android.view.WindowManager;

/**
 * �豸����������
 * @Description: �豸����������

 * @File: DeviceUtility.java

 * @Package com.shilangtech.diankan.Util

 * @Author google_acmer

 * @Date 2015-4-20 ����23:26:26

 * @Version V1.0
 */
public class DeviceUtility{
	/**
	 * ��ȡ��Ļ�ĳߴ�
	 * @param context
	 * @return
	 */
	public static int[] getScreenSize(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(
						    Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();//��Ļ���
		int height = wm.getDefaultDisplay().getHeight();//��Ļ�߶�
		int[] size = {width,height};
		
		return size;
	}
	
	/**
	 * ��ȡ״̬���߶�
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context){
		int statusBarHeight = 0;
		// ����ֻ��Ҫ��ȡ��Ļ�߶�
		int screenHeight = getScreenSize(context)[1];
		
		switch(screenHeight){
		case 240:
			statusBarHeight = 20;
			break;
		case 480:
			statusBarHeight = 25;
			break;
		case 800:
			statusBarHeight = 38;
			break;
		case  1280:
			statusBarHeight = 48;
		default:
			break;
		}
		
		return statusBarHeight;
	}
}
