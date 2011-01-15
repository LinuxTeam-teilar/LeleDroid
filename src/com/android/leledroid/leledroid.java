package com.android.leledroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class leledroid extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onStart() {
		Toast toast = null;
		ArrayAdapter<String> adapter;
		ListView strList;
		super.onStart();
		strList = (ListView) findViewById(R.id.strList);
		String line = null;
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);

		try {
			InputStream instream = openFileInput("data.txt");
			if (instream != null) {
				Integer c = 0;
				InputStreamReader inputreader = new InputStreamReader(instream);
				BufferedReader buffreader = new BufferedReader(inputreader);

				while ((line = buffreader.readLine()) != null) {
					adapter.add(line.split(" * ")[0].replace('_', ' '));
					c++;
				}
				if (c == 0) adapter.add(getResources().getString(R.string.emptyList));
			}
			instream.close();
		} catch (java.io.FileNotFoundException e) {
			adapter.add(getResources().getString(R.string.emptyList));
		} catch (IOException e) {
			toast = Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT);
			toast.show();
		}
		strList.setOnItemClickListener(new ListView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int i, long l) {
				try {
					Intent inte = new Intent(leledroid.this, details.class);
					Bundle b = new Bundle();
					String line1 = null;
					Toast toast;

					try {
						InputStream instream = openFileInput("data.txt");
						if (instream != null) {
							InputStreamReader inputreader = new InputStreamReader(
									instream);
							BufferedReader buffreader = new BufferedReader(
									inputreader);
							for (int k = 0; k < i + 1; k++) {
								line1 = buffreader.readLine();
							}
						}
						instream.close();
					} catch (java.io.FileNotFoundException e) {
						;
					} catch (IOException e) {
						toast = Toast.makeText(getApplicationContext(),
								e.getMessage(), Toast.LENGTH_SHORT);
						toast.show();

					}

					b.putString("selected", (String) Integer.toString(i));
					b.putString("onoma", (String) line1.split(" * ")[0].replace('_', ' '));
					b.putString("indate", (String) line1.split(" * ")[2]);
					b.putString("outdate", (String) line1.split(" * ")[4]);
					b.putString("adeia", (String) line1.split(" * ")[6]);
					b.putString("filaki", (String) line1.split(" * ")[8]);
					b.putString("oplo", (String) line1.split(" * ")[10]);
					inte.putExtras(b);
					startActivity(inte);
				} catch (Exception e) {
					;
				}
			}
		});
		strList.setAdapter(adapter);
	}

	public void onAddButtonClick(View v) {
		Intent str = new Intent(this, com.android.leledroid.properties.class);
		startActivity(str);
	}

	public void onInfoButtonClick(View v) {
		Intent str = new Intent(this, com.android.leledroid.info.class);
		startActivity(str);
	}
}
