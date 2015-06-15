package com.shilangtech.diankan.message_send;

import android.R;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


/**
 * @author Google_acmer
 * 图片数字下标类
 *
 */
public class BadgeView {

	public void  draw(Bitmap bitmap,int num){
		
		if(num>1){
			Paint p=new Paint();
			p.setColor(Color.rgb(234, 94, 91));
			p.setAntiAlias(true);
			Canvas canvas=new Canvas(bitmap);
			canvas.drawCircle(50, 50,13, p);
			p.setColor(Color.WHITE);
			p.setAntiAlias(true);
			p.setTextSize(20);
			canvas.drawText(""+num, 45, 55, p);
		}
	}

}
