package com.vasilakos.LeleDroid;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Vathmoi extends Activity {
	Integer[] pics = { R.drawable.img2, R.drawable.img3, R.drawable.img4,
			R.drawable.img5, R.drawable.img6, R.drawable.img7, R.drawable.img8,
			R.drawable.img9, R.drawable.img10, R.drawable.img11,
			R.drawable.img12, R.drawable.img13, R.drawable.img14,
			R.drawable.img15, R.drawable.img16, R.drawable.img17 };
	TextView vathmoiText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vathmoi);

		vathmoiText = (TextView) findViewById(R.id.vathmosText);
	}

	public void onStart() {
		super.onStart();

		Gallery vathmoiGallery = (Gallery) findViewById(R.id.vathmoiGallery);
		vathmoiGallery.setAdapter(new ImageAdapter(this));
		vathmoiGallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				vathmoiText.setText(Str.getResourceVathmo(arg2+1));
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public class ImageAdapter extends BaseAdapter {

		private Context ctx;
		int imageBackground;

		public ImageAdapter(Context c) {
			ctx = c;
			TypedArray ta = obtainStyledAttributes(R.styleable.Gallery1);
			imageBackground = ta.getResourceId(
					R.styleable.Gallery1_android_galleryItemBackground, 1);
			ta.recycle();
		}

		public int getCount() {
			return pics.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ImageView iv = new ImageView(ctx);
			iv.setImageResource(pics[arg0]);
			iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			iv.setLayoutParams(new Gallery.LayoutParams(
					Gallery.LayoutParams.FILL_PARENT,
					Gallery.LayoutParams.WRAP_CONTENT));
			iv.setBackgroundResource(imageBackground);
			return iv;
		}
	}
}
