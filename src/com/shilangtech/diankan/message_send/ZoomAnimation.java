package com.shilangtech.diankan.message_send;

import android.view.View;
import android.view.animation.AnimationSet;

/**
 * 放大缩小动画类
 * @Description: 放大缩小动画类

 * @File: ZoomAnimation.java

 * @Package com.shilangtech.diankan.message_send

 * @Author google_acmer

 * @Date 2015-4-24 下午10:17:54

 * @Version V1.0
 */
public abstract class ZoomAnimation extends AnimationSet {
	public Direction direction;

	public enum Direction {
		HIDE, SHOW,UP,DOWN;
	}

	public ZoomAnimation(Direction direction, long duration, View[] views) {
		super(true);
		this.direction = direction;

		switch (this.direction) {
		case HIDE:
			addShrinkAnimation(views);
			break;
		case SHOW:
			addEnlargeAnimation(views);
			break;
		
		
		}

		setDuration(duration);
	}

	protected abstract void addShrinkAnimation(View[] views);

	protected abstract void addEnlargeAnimation(View[] views);
	
}
