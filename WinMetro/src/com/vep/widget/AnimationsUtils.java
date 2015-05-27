package com.vep.widget;

import java.util.Random;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationsUtils {

	public static void startScaleDown(View view) {
		PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,
				0.9f);
		PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,
				0.9f);
		ObjectAnimator.ofPropertyValuesHolder(view, pvhY, pvhZ)
				.setDuration(300).start();
	}

	public static void startScaleUp(View view, int time) {
		PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX",
				0.9f, 1f);
		PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY",
				0.9f, 1f);
		ObjectAnimator.ofPropertyValuesHolder(view, pvhY, pvhZ)
				.setDuration(time).start();
	}

	private static TranslateAnimation trans(int fromXType, int fromXValue,
			int toXType, int toXValue, int fromYType, int fromYValue,
			int toYType, int toYValue) {
		TranslateAnimation anima = new TranslateAnimation(fromXType,
				fromXValue, toXType, toXValue, fromYType, fromYValue, toYType,
				toYValue);
		anima.setDuration(500);
		return anima;

	}

	public static int SELF = Animation.RELATIVE_TO_SELF;
	public static int PARENT = Animation.RELATIVE_TO_PARENT;

	public static void startMove(View v, int toX, int toY) {
		Random random = new Random();
		TranslateAnimation anima = null;
		switch (random.nextInt(2)) {
		case 0:
			switch (toX) {
			case MetroLayout.TOLEFT:
				anima = trans(SELF, -1, SELF, 0, PARENT, 0, PARENT, 0);
				break;
			case MetroLayout.TORIGHT:
				anima = trans(PARENT, 1, SELF, 0, PARENT, 0, PARENT, 0);
				break;
			default:
				break;
			}
			break;
		case 1:
		default:
			switch (toY) {
			case MetroLayout.TOBOTTOM:
				anima = trans(PARENT, 0, PARENT, 0, PARENT, 1, SELF, 0);
				break;
			case MetroLayout.TOTOP:
				anima = trans(PARENT, 0, PARENT, 0, SELF, -1, SELF, 0);
				break;
			default:
				break;
			}
			break;
		}
		if (anima != null) {
			v.startAnimation(anima);
		}
	}

}
