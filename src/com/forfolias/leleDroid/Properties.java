package com.forfolias.leleDroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class Properties extends Activity {
	EditText str_name;
	EditText str_indate;
	EditText str_outdate;
	EditText str_adeia;
	EditText str_filaki;

	static final int DATE_DIALOG_ID = 0;

	private EditText activeDateDisplay;
	private Calendar activeDate;
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

		str_indate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDateDialog(str_indate, Calendar.getInstance());
			}
		});

		str_outdate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDateDialog(str_outdate, Calendar.getInstance());
			}
		});

		updateDisplay(str_indate, Calendar.getInstance());
		updateDisplay(str_outdate, Calendar.getInstance());

		if (b == null) {
			id = -1;
		} else {
			id = b.getInt("id");
			Str str = Str.getStrFromId(id);
			str_name.setText(str.getName());
			str_indate.setText(str.getDateIn());
			str_outdate.setText(str.getDateOut());
			str_adeia.setText(str.getAdeia().toString());
			str_filaki.setText(str.getFilaki().toString());
		}
	}

	private void updateDisplay(EditText dateDisplay, Calendar date) {
		dateDisplay.setText(new StringBuilder()
				.append(date.get(Calendar.DAY_OF_MONTH)).append("/")
				.append(date.get(Calendar.MONTH) + 1).append("/")
				.append(date.get(Calendar.YEAR)).append(" "));

	}

	public void showDateDialog(EditText dateDisplay, Calendar date) {
		activeDateDisplay = dateDisplay;
		activeDate = date;
		showDialog(DATE_DIALOG_ID);
	}

	private OnDateSetListener dateSetListener = new OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			activeDate.set(Calendar.YEAR, year);
			activeDate.set(Calendar.MONTH, monthOfYear);
			activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDisplay(activeDateDisplay, activeDate);
			unregisterDateDisplay();
		}
	};

	private void unregisterDateDisplay() {
		activeDateDisplay = null;
		activeDate = null;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, dateSetListener,
					activeDate.get(Calendar.YEAR),
					activeDate.get(Calendar.MONTH),
					activeDate.get(Calendar.DAY_OF_MONTH));
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(
					activeDate.get(Calendar.YEAR),
					activeDate.get(Calendar.MONTH),
					activeDate.get(Calendar.DAY_OF_MONTH));
			break;
		}
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
			Str.deleteStrFromId(id);
		}
		str = new Str();
		str.setName(str_name.getText().toString().replace(' ', '_'));
		str.setDateIn(str_indate.getText().toString());
		str.setDateOut(str_outdate.getText().toString());
		str.setAdeia(Integer.parseInt(str_adeia.getText().toString()));
		str.setFilaki(Integer.parseInt(str_filaki.getText().toString()));
		
		str.writeStr();

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
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
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
}
