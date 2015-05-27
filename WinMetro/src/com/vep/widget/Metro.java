package com.vep.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.vep.ui.R;

public class Metro {

	private Type type;
	private Context context;
	private Piece view;
	private ImageView iv;
	private TextView title;
	private TextView value;

	public void visibleTitle(boolean b) {
		if (b) {
			title.setVisibility(View.VISIBLE);
			value.setVisibility(View.VISIBLE);
		} else {
			title.setVisibility(View.INVISIBLE);
			value.setVisibility(View.INVISIBLE);
		}
	}

	public OnClickListener getListener() {
		return listener;
	}

	public String getTitle() {
		return title.getText().toString();
	}

	public void setTextColor(int color) {
		title.setTextColor(color);
		value.setTextColor(color);
	}

	public void setTag(Object tag) {
		view.setTag(tag);
	}

	public Object getTag() {
		return view.getTag();
	}

	public void setId(int id) {
		view.setId(id);
	}

	public Metro(Context context, Type t) {

		this.type = t;
		this.context = context;
		init();
	}

	public View getView() {
		return view;
	}

	public void setLayoutParams(LayoutParams params) {
		view.setLayoutParams(params);
	}

	public void setImg(int id) {
		iv.setBackgroundDrawable(context.getResources().getDrawable(id));
	}

	public void setBackground(int id) {
		view.setBackgroundDrawable(context.getResources().getDrawable(id));
	}

	public void setBackgroundColor(int color) {
		view.setBackgroundColor(color);
	}

	public void setTxt(String text) {
		title.setText(text);
	}

	public void setValue(String value) {
		this.value.setText(value);
	}

	private void init() {
		view = (Piece) View.inflate(context, R.layout.piece, null);
		iv = (ImageView) view.findViewById(R.id.iv_ico);
		title = (TextView) view.findViewById(R.id.tv_title);
		value = (TextView) view.findViewById(R.id.tv_style);
		title.setTextColor(Color.WHITE);
		value.setTextColor(Color.WHITE);
		switch (type) {

		case BIG:
			view.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.blue_bg));
			break;
		case MIDDLE:
			view.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.red_bg));
			break;
		case SMALL:
		default:
			view.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.tblue_bg));
			break;
		}

	}

	public Type getType() {
		return this.type;
	}

	private OnClickListener listener;

	public void setOnClickListener(OnClickListener l) {
		this.listener = l;
		view.setOnClickListener(l);
	}

	public interface OnClickListener {
		void onClick(Metro m);
	}

	public enum Type {
		SMALL(0), MIDDLE(1), BIG(2);
		Type(int type) {
			this.type = type;
		}

		private int type;

		public int getType() {
			return type;
		}
	}

	public void addInMetro(MetroLayout ml) {
		ml.addMetroView(this);
		view.setMetro(this);
		view.setMetroLayout(ml);
	}

}
