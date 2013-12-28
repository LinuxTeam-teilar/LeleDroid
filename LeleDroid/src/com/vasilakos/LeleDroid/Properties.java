package com.vasilakos.LeleDroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class Properties extends Activity {
	EditText str_name;
	EditText str_indate;
	EditText str_outdate;
	EditText str_adeia;
	EditText str_filaki;
	
	String DATE_FORMAT = "dd/MM/yyyy";

	static final int DATE_DIALOG_ID = 0;

	private Calendar startDate;
	private Calendar endDate;

	int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.properties);
		Bundle b = this.getIntent().getExtras();

		str_name = (EditText) findViewById(R.id.str_name_et);
		str_indate = (EditText) findViewById(R.id.str_indate_et);
		str_outdate = (EditText) findViewById(R.id.str_outdate_et);
		str_adeia = (EditText) findViewById(R.id.str_adeia_et);
		str_filaki = (EditText) findViewById(R.id.str_filaki_et);

		

		startDate = Calendar.getInstance();
		endDate = Calendar.getInstance();
		if (b == null) {
			id = -1;
			
			str_indate.setText(startDate.get(Calendar.DAY_OF_MONTH) + "/"
					+ startDate.get(Calendar.MONTH) + "/"
					+ startDate.get(Calendar.YEAR));
			str_outdate.setText(endDate.get(Calendar.DAY_OF_MONTH) + "/"
					+ endDate.get(Calendar.MONTH) + "/"
					+ endDate.get(Calendar.YEAR));
		} else {
			id = b.getInt("id");
			Str str = Str.getStrFromId(id, this);
			str_name.setText(str.getName());
			str_indate.setText(str.getDateIn());
			str_outdate.setText(str.getDateOut());
			str_adeia.setText(str.getAdeia().toString());
			str_filaki.setText(str.getFilaki().toString());
			
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, getResources().getConfiguration().locale);
	        Date mydatein = null;
	        Date mydateout = null;
			try {
				mydatein = sdf.parse(str.getDateIn());
			} catch (ParseException e1) {
				mydatein = new Date();
			}
			startDate.setTime(mydatein);
			try {
				mydateout = sdf.parse(str.getDateOut());
			} catch (ParseException e1) {
				mydateout = new Date();
			}
			endDate.setTime(mydateout);
		}

		str_indate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(Properties.this, dateIn, startDate
						.get(Calendar.YEAR), startDate.get(Calendar.MONTH),
						startDate.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		str_outdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(Properties.this, dateOut, endDate
						.get(Calendar.YEAR), endDate.get(Calendar.MONTH),
						endDate.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		
	}

	public void okButtonClicked(View v) {
		if (!checkOnoma())
			return;
		if (!checkAdeia())
			return;
		if (!checkFilak())
			return;
		if (!checkDates(str_indate.getText().toString(), str_outdate.getText()
				.toString()))
			return;

		Str str = null;

		if (id != -1) {
			Str.deleteStrFromId(id, this);
		}
		str = new Str();
		str.setName(str_name.getText().toString().replace(' ', '_'));
		str.setDateIn(str_indate.getText().toString());
		str.setDateOut(str_outdate.getText().toString());
		str.setAdeia(Integer.parseInt(str_adeia.getText().toString()));
		str.setFilaki(Integer.parseInt(str_filaki.getText().toString()));

		str.writeStr(this);

		finish();
	}

	public void cancelButtonClicked(View v) {
		finish();
	}

	public boolean checkAdeia() {
		Toast toast = null;
		if (str_adeia.getText().toString().equals(new String(""))) {
			toast = Toast.makeText(getApplicationContext(), getResources()
					.getString(R.string.emptyAdeia), Toast.LENGTH_SHORT);
			toast.show();
			return false;
		}
		return true;
	}

	public boolean checkOnoma() {
		Toast toast = null;
		if (str_name.getText().toString().equals(new String(""))) {
			toast = Toast.makeText(getApplicationContext(), getResources()
					.getString(R.string.emptyName), Toast.LENGTH_SHORT);
			toast.show();
			return false;
		}
		return true;
	}

	public boolean checkFilak() {
		Toast toast = null;
		if (str_filaki.getText().toString().equals(new String(""))) {
			toast = Toast.makeText(getApplicationContext(), getResources()
					.getString(R.string.emptyFilaki), Toast.LENGTH_SHORT);
			toast.show();
			return false;
		}
		return true;
	}

	public boolean checkDates(String dateIn, String dateOut) {
		Toast toast = null;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", getResources().getConfiguration().locale);
		Date date1 = null;
		Date date2 = null;

		try {
			date1 = df.parse(dateIn);
		} catch (ParseException e) {
			toast = Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT);
			toast.show();
		}

		try {
			date2 = df.parse(dateOut);
		} catch (ParseException e) {
			toast = Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT);
			toast.show();
		}
		if (date1.compareTo(date2) >= 0) {
			toast = Toast.makeText(getApplicationContext(), getResources()
					.getString(R.string.dateForward), Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		return true;
	}

	DatePickerDialog.OnDateSetListener dateIn = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			startDate.set(Calendar.YEAR, year);
			startDate.set(Calendar.MONTH, monthOfYear);
			startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			
		    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, getResources().getConfiguration().locale);

		    str_indate.setText(sdf.format(startDate.getTime()));
		}
	};
	
	DatePickerDialog.OnDateSetListener dateOut = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			endDate.set(Calendar.YEAR, year);
			endDate.set(Calendar.MONTH, monthOfYear);
			endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			
		    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, getResources().getConfiguration().locale);

		    str_outdate.setText(sdf.format(endDate.getTime()));
		}
	};
}
