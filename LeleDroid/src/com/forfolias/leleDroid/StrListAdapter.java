package com.forfolias.leleDroid;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class StrListAdapter extends SimpleAdapter {
	private int[] colors = new int[] { 0x30ffffff, 0x30808080 };

	@SuppressWarnings("unchecked")
	public StrListAdapter(Context context, 
	        List<Str> strs, 
	        int resource, 
	        String[] from, 
	        int[] to) {
	  super(context, (List<? extends Map<String, ?>>) strs, resource, from, to);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	  View view = super.getView(position, convertView, parent);

	  int colorPos = position % colors.length;
	  view.setBackgroundColor(colors[colorPos]);
	  return view;
	}
}
