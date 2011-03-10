package com.forfolias.leleDroid;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class StrWidget extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
	
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			String id = StrWidgetConfig.getStrId(context, appWidgetId);
			updateAppWidget(context, appWidgetManager, appWidgetId, id);
		}
	}

	static void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId, String id) {

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget);

		Str theOne = Str.getStrFromId(Integer.parseInt(id));
		String vathmos = theOne.getVathmo();
		String onoma = theOne.getName();
		String meres = theOne.getRestDays().toString();

		views.setTextViewText(R.id.strVathmos, vathmos);
		views.setTextViewText(R.id.strName, onoma);
		views.setTextViewText(R.id.strMeres, meres + " " + context.getString(R.string.plusToday));
		views.setImageViewResource(R.id.strImg, theOne.getImg());

		appWidgetManager.updateAppWidget(appWidgetId, views);
	}
}
