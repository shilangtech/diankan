package com.shilangtech.diankan.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.EditText;

/**
 * @author Google_acmer
 * 
 * 表情解码类
 * 
 * 用法： View.setText(FaceDecode.DecodeAndInsert( String text,Context context));
 *
 */
public class FaceDecode {
	
 private static final float FACE_SCALE = 0.6f;
	
	public static SpannableStringBuilder DecodeAndInsert( String text,Context context){
		SpannableStringBuilder sb = new SpannableStringBuilder(text);
		String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		while (m.find()) {
			String tempText = m.group();
			String png = tempText.substring("#[".length(),tempText.length() - "]#".length());

			try {
				Bitmap bf;
				bf = BitmapFactory
						.decodeStream(context.getAssets().open(png));
				Matrix matrix = new Matrix();  
				matrix.postScale(FACE_SCALE,FACE_SCALE);  
				Bitmap bm=Bitmap.createBitmap(bf,0,0,bf.getWidth(), bf.getHeight(),matrix,false);   

				sb.setSpan(new ImageSpan(context, bm), 
						m.start(), m.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb;
	}

}
