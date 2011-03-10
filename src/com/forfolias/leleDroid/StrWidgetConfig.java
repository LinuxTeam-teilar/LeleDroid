package com.forfolias.leleDroid;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class StrWidgetConfig extends Activity {

	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	public StrWidgetConfig() {
		super();
	}
	
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);
        setContentView(R.layout.widget_conf);

        Spinner strSpin = (Spinner) findViewById(R.id.strSpinner);
        final Integer N = Str.getLengh();
        if (N == 0){
        	Toast.makeText(getApplicationContext(), R.string.emptyList, Toast.LENGTH_LONG).show();
        	finish();
        }
		String[] strList = new String[N];
		for (int i = 0; i < N; i++) {
			strList[i] = Str.getStrFromId(i + 1).getName();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		strSpin.setAdapter(adapter);
        
        findViewById(R.id.ok).setOnClickListener(mOnClickListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }
    
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context =StrWidgetConfig.this;
            
            Spinner strSpin = (Spinner) findViewById(R.id.strSpinner);

            Long id = strSpin.getSelectedItemId() + 1;

            saveIdPref(context, mAppWidgetId, id.toString());

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            StrWidget.updateAppWidget(context, appWidgetManager,
                    mAppWidgetId, id.toString());

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    static void saveIdPref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences("com.forfolias.leleDroid.StrWidget", 0).edit();
        prefs.putString("prefix_" + appWidgetId, text);
        prefs.commit();
    }
    
	static String getStrId(Context context, int appWidgetId) {
		SharedPreferences prefs = context.getSharedPreferences(
				"com.forfolias.leleDroid.StrWidget", 0);
		String prefix = prefs.getString("prefix_" + appWidgetId, null);
		if (prefix != null) {
			return prefix;
		} else {
			return "0";
		}
	}
}
