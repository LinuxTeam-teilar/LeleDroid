package com.android.leledroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class properties extends Activity {

	EditText str_name;
	EditText str_indate;
	EditText str_outdate;
	EditText str_adeia;
	EditText str_filaki;
	Spinner str_oplo;

	static final int DATE_DIALOG_ID = 0;

	private EditText activeDateDisplay;
	private Calendar activeDate;

	int selectedItem;

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
		str_oplo = (Spinner) findViewById(R.id.oploSpinner);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.opla, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		str_oplo.setAdapter(adapter);

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
			selectedItem = -1;
		} else {
			selectedItem = Integer.parseInt(b.getString("selected"));
			str_name.setText(b.getString("onoma"));
			str_indate.setText(b.getString("indate"));
			str_outdate.setText(b.getString("outdate"));
			str_adeia.setText(b.getString("adeia"));
			str_filaki.setText(b.getString("filaki"));

			String[] oplo = getResources().getStringArray(R.array.opla);

			for (int i = 0; i < oplo.length; i++) {
				if (oplo[i].equals(b.getString("oplo"))) {
					str_oplo.setSelection(i);
				}
			}
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
		String dataToWrite = null;
		Toast toast = null;

		if (!checkOnoma())
			return;
		if (!checkAdeia())
			return;
		if (!checkFilak())
			return;
		if (!checkDates(str_indate.getText().toString(), str_outdate.getText().toString())) 
			return;
		

		dataToWrite = str_name.getText().toString().replace(' ', '_') + " * "
				+ str_indate.getText().toString() + " * "
				+ str_outdate.getText().toString() + " * "
				+ str_adeia.getText().toString() + " * "
				+ str_filaki.getText().toString() + " * "
				+ str_oplo.getSelectedItem().toString();

		try {
			OutputStreamWriter out = new OutputStreamWriter(openFileOutput(
					"data.txt.tmp", MODE_PRIVATE));
			InputStream instream = openFileInput("data.txt");
			String line = null;
			int counter = 0;

			if (instream != null) {
				InputStreamReader inputreader = new InputStreamReader(instream);
				BufferedReader buffreader = new BufferedReader(inputreader);

				while ((line = buffreader.readLine()) != null) {
					if (counter != selectedItem) {
						out.write(line + "\n");
					}
					counter++;
				}
			}
			instream.close();
			out.close();

			if (deleteFile("data.txt") == false) {
				toast = Toast.makeText(getApplicationContext(),
						"Error deleting data.txt", Toast.LENGTH_SHORT);
				toast.show();
			}

		} catch (java.io.FileNotFoundException e) {
			toast = Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT);
			toast.show();

		} catch (IOException e) {
			toast = Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT);
			toast.show();
		}

		try {
			OutputStreamWriter out = new OutputStreamWriter(openFileOutput(
					"data.txt", MODE_PRIVATE));
			InputStream instream = openFileInput("data.txt.tmp");
			String line = null;

			if (instream != null) {
				InputStreamReader inputreader = new InputStreamReader(instream);
				BufferedReader buffreader = new BufferedReader(inputreader);

				while ((line = buffreader.readLine()) != null) {
					out.write(line + "\n");
				}
			}
			instream.close();
			out.close();

			if (deleteFile("data.txt.tmp") == false) {
				toast = Toast.makeText(getApplicationContext(),
						"Error deleting data.txt.tmp", Toast.LENGTH_SHORT);
				toast.show();
			}
		} catch (java.io.FileNotFoundException e) {
			toast = Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT);
			toast.show();
		} catch (IOException e) {
			toast = Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT);
			toast.show();
		}

		try {
			OutputStreamWriter out = new OutputStreamWriter(openFileOutput(
					"data.txt", MODE_APPEND));
			out.write(dataToWrite + "\n");
			out.close();
		} catch (java.io.IOException e) {
			toast = Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT);
			toast.show();
		}
		
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
		String myFormatString = "dd/MM/yyyy";
		SimpleDateFormat df = new SimpleDateFormat(myFormatString);
		Date date1 = null;
		Date date2 = null;
		
		try {
			date1 = df.parse(dateIn);
		} catch (ParseException e) {
			toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
			toast.show();
		}

		try {
			date2 = df.parse(dateOut);
		} catch (ParseException e) {
			toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
			toast.show();
		}
		if (date1.compareTo(date2) >= 0 ){
			toast = Toast.makeText(getApplicationContext(), getResources()
					.getString(R.string.dateForward), Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		return true;
	}
}
