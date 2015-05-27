package com.vep.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.vep.widget.Metro;
import com.vep.widget.Metro.OnClickListener;
import com.vep.widget.Metro.Type;
import com.vep.widget.MetroLayout;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MetroLayout layout = new MetroLayout(this, 6, 3, 5, 5);
		
		Metro m1 = new Metro(this, Type.BIG);
		m1.setImg(R.drawable.login);
		m1.setTxt("Log in");
		
		Metro m2 = new Metro(this, Type.SMALL);
		m2.setImg(R.drawable.remberme);
		m2.setTxt("Diary");
		
		Metro m3 = new Metro(this, Type.SMALL);
		m3.setTxt("E-Mail");
		m3.setImg(R.drawable.e_mail);
		m3.setBackgroundColor(Color.GRAY);
		
		Metro m4 = new Metro(this, Type.SMALL);
		m4.setImg(R.drawable.weather);
		m4.setTxt("Weather");
		m4.setTextColor(Color.RED);
		m4.setBackground(R.drawable.yellow_bg);
		
		Metro m5 = new Metro(this, Type.MIDDLE);
		m5.setImg(R.drawable.delete_button);
		
		Metro m6 = new Metro(this, Type.SMALL);
		m6.setImg(R.drawable.menu);
		m6.setTxt("Other");
		m6.setBackground(R.drawable.black_bg);
		
		Metro m7 = new Metro(this, Type.BIG);
		//m7.setImg(R.drawable.logo);
		m7.setBackgroundColor(Color.WHITE);
		
		Metro m8 = new Metro(this, Type.SMALL);
		m8.setBackgroundColor(Color.DKGRAY);
		m8.setImg(R.drawable.password);
		m8.setTxt("Lock");

		m1.addInMetro(layout);
		m2.addInMetro(layout);
		m3.addInMetro(layout);
		m4.addInMetro(layout);
		m5.addInMetro(layout);
		m6.addInMetro(layout);
		m7.addInMetro(layout);
		m8.addInMetro(layout);
		
	
		
		for (int i = 0; i < MetroLayout.metros.size(); i++) {
			MetroLayout.metros.get(MetroLayout.metros.keyAt(i))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(Metro m) {
							Toast.makeText(getApplicationContext(),
									"Click" + m.getTitle(), Toast.LENGTH_SHORT)
									.show();
						}
					});

		}
		layout.useGraph();
		FrameLayout box = (FrameLayout) findViewById(R.id.box);
		box.addView(layout);

	}

}
