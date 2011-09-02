package com.vasilakos.LeleDroid;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

public class StrWidget extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {

			int appWidgetId = appWidgetIds[i];
			String id = StrWidgetConfig.getPref(context, appWidgetId, "id_");
			String act = StrWidgetConfig.getPref(context, appWidgetId,
					"action_");
			String TextColor = StrWidgetConfig.getPref(context, appWidgetId,
					"textColor_");
			String Vathm = StrWidgetConfig.getPref(context, appWidgetId,
					"displayVathmos_");
			String Name = StrWidgetConfig.getPref(context, appWidgetId,
					"displayName_");
			String Image = StrWidgetConfig.getPref(context, appWidgetId,
					"displayImage_");
			String Days = StrWidgetConfig.getPref(context, appWidgetId,
					"displayDays_");
			String Prog = StrWidgetConfig.getPref(context, appWidgetId,
					"displayProg_");

			if (Integer.parseInt(id) <= 0)
				return;

			updateAppWidget(context, appWidgetManager, appWidgetId,
					Integer.parseInt(id), Integer.parseInt(act), TextColor,
					Vathm, Name, Image, Days, Prog);
		}
	}

	static void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId, Integer id,
			Integer act, String tC, String V, String Nam, String Image,
			String Days, String Prog) {

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget);

		Str theOne = Str.getStrFromId(id);
		String vathmos = theOne.getVathmo();
		String onoma = theOne.getName();
		String meres = theOne.getRestDays().toString();

		views.setTextColor(R.id.strVathmos, Integer.parseInt(tC));
		views.setTextColor(R.id.strName, Integer.parseInt(tC));
		views.setTextColor(R.id.strMeres, Integer.parseInt(tC));

		views.setTextViewText(R.id.strVathmos, vathmos);
		views.setTextViewText(R.id.strName, onoma);
		views.setTextViewText(R.id.strMeres,
				meres + " " + context.getString(R.string.plusToday));
		views.setImageViewResource(R.id.strImg, theOne.getImg());
		views.setProgressBar(R.id.strProg, 100, theOne.getPososto().intValue(),
				false);

		if (V == "0")
			views.setViewVisibility(R.id.strVathmos, View.GONE);
		if (Nam == "0")
			views.setViewVisibility(R.id.strName, View.GONE);
		if (Image == "0")
			views.setViewVisibility(R.id.strImg, View.GONE);
		if (Days == "0")
			views.setViewVisibility(R.id.strMeres, View.GONE);
		if (Prog == "0")
			views.setViewVisibility(R.id.strProg, View.GONE);

		
		if (act != 0) {
			Intent action;
			if (act == 2) {
				action = new Intent(context, ChartView.class);
			} else {
				action = new Intent(context, Details.class);
			}
			action.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			action.putExtra("id", id);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, id,
					action, 0);
			views.setOnClickPendingIntent(R.id.widg, pendingIntent);
			views.setOnClickPendingIntent(R.id.strImg, pendingIntent);
		}

		appWidgetManager.updateAppWidget(appWidgetId, views);
	}
}
