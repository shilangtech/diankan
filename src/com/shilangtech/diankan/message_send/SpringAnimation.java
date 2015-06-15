package com.shilangtech.diankan.message_send;



import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * 分布菜单加载和伸缩动画
 * @Description: 分布菜单加载和伸缩动画

 * @File: SpringAnimation.java

  * @Package com.shilangtech.diankan.message_send

 * @Author google_acmer

 * @Date 2015-4-24 下午10:17:54

 * @Version V1.0
 */
public class SpringAnimation extends ZoomAnimation {
	private static int[] size;
	private static int xOffset = 3;
	private static int yOffset = 105;
	public static final int	DURATION = 300;
	
	/**
	 * 构造器
	 * @param direction
	 * @param duration
	 * @param view
	 */
	public SpringAnimation(Direction direction, long duration, View view) {
		super(direction, duration, new View[] { view });
		SpringAnimation.xOffset = SpringAnimation.size[0] / 2 - 30;
	}

	/**
	 * 开始显示动画效果
	 * @param viewgroup
	 * @param direction
	 * @param size
	 */
	public static void startAnimations(ViewGroup viewgroup,
			ZoomAnimation.Direction direction, int[] size) {
		SpringAnimation.size = size;
		
		switch (direction) {
		case HIDE:
			startShrinkAnimations(viewgroup);
			break;
		case SHOW:
			startEnlargeAnimations(viewgroup);
			break;
		case UP:
			startMenuUpAnimations(viewgroup);
			break;
		case DOWN:
			startMenuDownAnimations(viewgroup);
		
			break;
		}
	}

	private static void startMenuDownAnimations(ViewGroup viewgroup) {
		// TODO 自动生成的方法存根
		//Log.e("TAG1", "StartMenuDown");
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			if (viewgroup.getChildAt(i) instanceof ImageButtonExtend) {
				ImageButtonExtend inoutimagebutton = (ImageButtonExtend) viewgroup
						.getChildAt(i);
				SpringAnimation animation = new SpringAnimation(
						ZoomAnimation.Direction.SHOW, DURATION, inoutimagebutton);
				animation.setDuration(100);
				animation.setInterpolator(new AccelerateDecelerateInterpolator());
				inoutimagebutton.startAnimation(animation);
			}
		}
	}

	private static void startMenuUpAnimations(ViewGroup viewgroup) {
		// TODO 自动生成的方法存根
		//Log.e("TAG1", "StartMenuUP");
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			if (viewgroup.getChildAt(i) instanceof ImageButtonExtend) {
				ImageButtonExtend inoutimagebutton = (ImageButtonExtend) viewgroup
						.getChildAt(i);
				SpringAnimation animation = new SpringAnimation(
						ZoomAnimation.Direction.HIDE, DURATION,
						inoutimagebutton);
			animation.setDuration(100);
				animation.setInterpolator(new AccelerateDecelerateInterpolator());
				inoutimagebutton.startAnimation(animation);
			}
		}
	}

	/**
	 * 开始呈现菜单
	 * @param viewgroup
	 */
	private static void startEnlargeAnimations(ViewGroup viewgroup) {
		//Log.e("TAG1", "StartHIDE");
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			if (viewgroup.getChildAt(i) instanceof ImageButtonExtend) {
				ImageButtonExtend inoutimagebutton = (ImageButtonExtend) viewgroup
						.getChildAt(i);
				SpringAnimation animation = new SpringAnimation(
						ZoomAnimation.Direction.HIDE, DURATION, inoutimagebutton);
				animation.setStartOffset((i * 200)
						/ (-1 + viewgroup.getChildCount()));
				animation.setInterpolator(new OvershootInterpolator(4F));
				inoutimagebutton.startAnimation(animation);
			}
		}
	}

	/**
	 * 开始隐藏菜单
	 * @param viewgroup
	 */
	private static void startShrinkAnimations(ViewGroup viewgroup) {
		//Log.e("TAG1", "StartMenuDown");
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			if (viewgroup.getChildAt(i) instanceof ImageButtonExtend) {
				ImageButtonExtend inoutimagebutton = (ImageButtonExtend) viewgroup
						.getChildAt(i);
				SpringAnimation animation = new SpringAnimation(
						ZoomAnimation.Direction.SHOW, DURATION,
						inoutimagebutton);
				animation.setStartOffset((100 * ((-1 + viewgroup
						.getChildCount()) - i))
						/ (-1 + viewgroup.getChildCount()));
				animation.setInterpolator(new AnticipateOvershootInterpolator(2F));
				inoutimagebutton.startAnimation(animation);
			}
		}
	}

	@Override
	protected void addShrinkAnimation(View[] views) {
		// TODO Auto-generated method stub
		MarginLayoutParams mlp = (MarginLayoutParams) views[0].
				getLayoutParams();
		addAnimation(new TranslateAnimation(
				0f, 
				0F,yOffset + mlp.bottomMargin, 0F));
	}

	@Override
	protected void addEnlargeAnimation(View[] views) {
		// TODO Auto-generated method stub
		MarginLayoutParams mlp = (MarginLayoutParams) views[0].
				getLayoutParams();
		addAnimation(new TranslateAnimation(
				0F,0f,
				 0f,yOffset + mlp.bottomMargin));
	}

	
}
