package com.vep.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class Piece extends RelativeLayout {

	private MetroLayout mMl;

	public Piece(Context context) {
		super(context);
		init();
	}

	public Piece(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public Piece(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}
	
	private void init() {
		setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if(mMl.getSynPress())
				{
					mMl.setSynPress(false);
					bringToFront();
					setAlpha(0.5f);
					mIsLongPressed = true;
				}
				else
				{
					AnimationsUtils.startScaleUp(Piece.this, 500);
				}
				return true;
			}
		});
	}

	public void setMetroLayout(MetroLayout ml) {
		this.mMl = ml;
	}

	float startX;
	float startY;
	int time;
	boolean mIsLongPressed;
	long lastDownTime;

	private Metro.OnClickListener listener;
	private Metro metro;

	public void setOnClickListener(Metro.OnClickListener listener) {
		this.listener = listener;
	}

	boolean alphaFlag = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			mIsLongPressed = false;
			startX = event.getX();
			startY = event.getY();
			AnimationsUtils.startScaleDown(Piece.this);
			
			break;

		case MotionEvent.ACTION_MOVE:
			float endX = event.getX();
			float endY = event.getY();
			
			if (mIsLongPressed) {

				setAlpha(0.5f);
				alphaFlag = false;
				int l = this.getLeft();
				int t = this.getTop();

				l += endX - startX;
				t += endY - startY;

				l = l < 0 - getWidth() / 2 ? 0 - getWidth() / 2 : l;
				t = t < 0 - getHeight() / 2 ? 0 - getHeight() / 2 : t;
				if (mMl != null) {
					l = l + getWidth() > mMl.getWidth() + getWidth() / 2 ? mMl
							.getWidth() - getWidth() / 2 : l;

					t = t + getHeight() > mMl.getHeight() + getHeight() / 2 ? mMl
							.getHeight() - getHeight() / 2
							: t;
				}

				layout(l, t, l + getWidth(), t + getHeight());
				postInvalidate();
			}

			break;

		case MotionEvent.ACTION_UP:
			alphaFlag = false;
			setAlpha(1f);
			if (mIsLongPressed) {
				if (mMl != null)
					mMl.change((Integer) getTag(), getLeft() + getWidth() / 2,
							getTop() + getHeight() / 2);
				setAlpha(1f);
				mMl.setSynPress(true);
			} else {
				if (listener != null && metro != null)
					listener.onClick(metro);
			}

			mIsLongPressed = false;
			AnimationsUtils.startScaleUp(Piece.this, 500);

			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	public void setMetro(Metro metro) {
		this.metro = metro;
	}
}
