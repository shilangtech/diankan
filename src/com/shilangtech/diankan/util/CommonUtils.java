package com.shilangtech.diankan.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class CommonUtils {

	public static  byte[] getIconData(Bitmap bitmap) {
		if (bitmap == null) return null;
		Bitmap reducedBitmap = reduce(bitmap, 1280, 720, true);
		int size = reducedBitmap.getWidth() * bitmap.getHeight() * 4;
		ByteArrayOutputStream out = new ByteArrayOutputStream(size);
		try {
			reducedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	
	/** 
     * 压缩图片 
     * @param bitmap 源图片 
     * @param width 想要的宽度 
     * @param height 想要的高度 
     * @param isAdjust 是否自动调整尺寸, true图片就不会拉伸，false严格按照你的尺寸压缩 
     * @return Bitmap 
     */  
    public static Bitmap reduce(Bitmap bitmap, int width, int height, boolean isAdjust) {  
        // 如果想要的宽度和高度都比源图片小，就不压缩了，直接返回原图  
        if (bitmap.getWidth() < width && bitmap.getHeight() < height) {return bitmap;}  
        // 根据想要的尺寸精确计算压缩比例, 方法详解：public BigDecimal divide(BigDecimal divisor, int scale, int roundingMode);  
        // scale表示要保留的小数位, roundingMode表示如何处理多余的小数位，BigDecimal.ROUND_DOWN表示自动舍弃  
        float sx = new BigDecimal(width).divide(new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN).floatValue();  
        float sy = new BigDecimal(height).divide(new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN).floatValue();  
        if (isAdjust) {// 如果想自动调整比例，不至于图片会拉伸  
            sx = (sx < sy ? sx : sy);sy = sx;// 哪个比例小一点，就用哪个比例  
        }  
        Matrix matrix = new Matrix();  
        matrix.postScale(sx, sy);// 调用api中的方法进行压缩，就大功告成了  
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
    }  
}
