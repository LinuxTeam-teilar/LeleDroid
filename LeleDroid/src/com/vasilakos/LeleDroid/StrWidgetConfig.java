package com.vasilakos.LeleDroid;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

public class StrWidgetConfig extends Activity {

	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	public Integer textColor = 0xff000000;
	public Integer bgColor = 0x66ffffff;

	public StrWidgetConfig() {
		super();
	}

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setResult(RESULT_CANCELED);
		setContentView(R.layout.widget_conf);

		setStrSpinner();
		setActionSpinner();
		setbgColorSpinner();

		findViewById(R.id.ok).setOnClickListener(mOnClickListener);
		findViewById(R.id.widgetButtonTextColor).setOnClickListener(
				pickTextColor);
//		findViewById(R.id.widgetButtonBgColor).setOnClickListener(
//				pickBgColor);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}
	}

	View.OnClickListener mOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			final Context context = StrWidgetConfig.this;
			String Vpref = "";
			String Npref = "";
			String Ipref = "";
			String Dpref = "";
			String Ppref = "";
			Integer action = -1;
			CheckBox cb;
			
			Spinner strSpin = (Spinner) findViewById(R.id.strSpinner);
			Integer id = (int) (strSpin.getSelectedItemId() + 1);
			savePref(context, mAppWidgetId, id.toString(), "id_");
			
			savePref(context, mAppWidgetId, textColor.toString(), "textColor_");
			savePref(context, mAppWidgetId, bgColor.toString(), "bgColor_");
			
			strSpin = (Spinner) findViewById(R.id.actionSpinner);
			action = (int) (strSpin.getSelectedItemId());
			savePref(context, mAppWidgetId, action.toString(), "action_");
			
			Spinner bgSpin = (Spinner) findViewById(R.id.bgColorSpinner);
			Integer bg = (int) (bgSpin.getSelectedItemId());
			savePref(context, mAppWidgetId, bg.toString(), "bg_");
			
			cb = (CheckBox) findViewById(R.id.CBVathmos);
			if (cb.isChecked())
				Vpref = "1";
			else
				Vpref = "0";
			savePref(context, mAppWidgetId, Vpref, "dispVathmos_");
			
			cb = (CheckBox) findViewById(R.id.CBName);
			if (cb.isChecked())
				Npref = "1";
			else
				Npref = "0";
			savePref(context, mAppWidgetId, Npref, "dispName_");
			
			cb = (CheckBox) findViewById(R.id.CBImage);
			if (cb.isChecked())
				Ipref = "1";
			else
				Ipref = "0";
			savePref(context, mAppWidgetId, Ipref, "dispImage_");
			
			cb = (CheckBox) findViewById(R.id.CBDays);
			if (cb.isChecked())
				Dpref = "1";
			else
				Dpref = "0";
			savePref(context, mAppWidgetId, Dpref, "dispDays_");
			
			cb = (CheckBox) findViewById(R.id.CBProg);
			if (cb.isChecked())
				Ppref = "1";
			else
				Ppref = "0";
			savePref(context, mAppWidgetId, Dpref, "dispProg_");
			
			if (Vpref == "0" && Npref == "0" && Ipref == "0" && Dpref == "0" && Ppref == "0") {
				Toast.makeText(getApplicationContext(), R.string.emptyDisplay,
						Toast.LENGTH_LONG).show();
				return;
			}

			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			StrWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId,
					id, action, textColor.toString(), bg.toString(), Vpref, Npref, Ipref, Dpref, Ppref);

			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			finish();
		}
	};

	View.OnClickListener pickTextColor = new View.OnClickListener() {
		public void onClick(View v) {

			final ColorPickerDialog d = new ColorPickerDialog(
					StrWidgetConfig.this, textColor);
			d.setAlphaSliderVisible(true);

			d.setButton(getResources().getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							textColor = d.getColor();
							findViewById(R.id.widgetButtonTextColor)
									.setBackgroundColor(textColor);
						}
					});

			d.setButton2(getResources().getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			d.show();
		}
	};
	
//	View.OnClickListener pickBgColor = new View.OnClickListener() {
//		public void onClick(View v) {
//
//			final ColorPickerDialog d = new ColorPickerDialog(
//					StrWidgetConfig.this, bgColor);
//			d.setAlphaSliderVisible(true);
//
//			d.setButton(getResources().getString(R.string.ok),
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							bgColor = d.getColor();
//							findViewById(R.id.widgetButtonBgColor)
//									.setBackgroundColor(bgColor);
//						}
//					});
//
//			d.setButton2(getResources().getString(R.string.cancel),
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//						}
//					});
//			d.show();
//		}
//	};

	static void savePref(Context context, int appWidgetId, String text, String key) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(
				"com.vasilakos.LeleDroid.StrWidget", 0).edit();
		prefs.putString(key + appWidgetId, text);
		prefs.commit();
	}

	static String getPref(Context context, int appWidgetId, String pref) {
		SharedPreferences prefs = context.getSharedPreferences(
				"com.vasilakos.LeleDroid.StrWidget", 0);
		String prefix = prefs.getString(pref + appWidgetId, null);
		if (prefix != null){
			return prefix;			
		}else {
			return "-1";
		}
	}
	
	void setActionSpinner(){
		Spinner actionSpin = (Spinner) findViewById(R.id.actionSpinner);
		String[] actionList = { 
				getResources().getString(R.string.none),
				getResources().getString(R.string.details), 
				getResources().getString(R.string.chart)};
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, actionList);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		actionSpin.setAdapter(adapter1);
	}
	
	void setbgColorSpinner(){
		Spinner actionSpin = (Spinner) findViewById(R.id.bgColorSpinner);
		String[] actionList = { 
				getResources().getString(R.string.none),
				getResources().getString(R.string.light), 
				getResources().getString(R.string.dark)};
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, actionList);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		actionSpin.setAdapter(adapter1);
	}
	
	void setStrSpinner(){
		Spinner strSpin = (Spinner) findViewById(R.id.strSpinner);
		databaseHandler db = new databaseHandler(this);

		final Integer N = db.getStrCount();
		if (N == 0) {
			Toast.makeText(getApplicationContext(), R.string.emptyList,
					Toast.LENGTH_LONG).show();
			finish();
		}
		String[] strList = new String[N];
		for (int i = 0; i < N; i++) {
			strList[i] = Str.getStrFromId(i + 1, this).getName();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		strSpin.setAdapter(adapter);
	}
}


