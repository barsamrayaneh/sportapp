package com.example.feed;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Detail extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/BKoodakBold.ttf");
		Intent intent = getIntent();
		TextView title = (TextView) findViewById(R.id.dtitle);
		TextView lead = (TextView) findViewById(R.id.dlead);
		TextView body = (TextView) findViewById(R.id.dbody);
		ImageView iv = (ImageView) findViewById(R.id.image);
		
		title.setText(intent.getStringExtra("title"));
		lead.setText(intent.getStringExtra("lead"));
		body.setText(intent.getStringExtra("body"));
		
		Bitmap b =Save_Load_Image.loadBitmap(Detail.this,intent.getStringExtra("image_uri").toString());
		title.setTypeface(tf);
		lead.setTypeface(tf);
		body.setTypeface(tf);
		iv.setImageBitmap(b);
		

		
	}

}
