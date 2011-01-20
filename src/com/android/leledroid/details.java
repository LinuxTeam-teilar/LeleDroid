package com.android.leledroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class details extends Activity {

	int selectedItem;

	TextView nameTv;
	TextView secondsTv;
	TextView minutesTv;
	TextView hoursTv;
	TextView daysTv;
	TextView weeksTv;
	TextView monthsTv;
	TextView posostoTv;
	ProgressBar posostoPB;
	Bundle b;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		b = this.getIntent().getExtras();

		nameTv = (TextView) findViewById(R.id.onoma);
		secondsTv = (TextView) findViewById(R.id.secs);
		minutesTv = (TextView) findViewById(R.id.mins);
		hoursTv = (TextView) findViewById(R.id.hours);
		daysTv = (TextView) findViewById(R.id.days);
		weeksTv = (TextView) findViewById(R.id.weeks);
		monthsTv = (TextView) findViewById(R.id.months);
		posostoPB = (ProgressBar) findViewById(R.id.posostoProgress);
		posostoTv = (TextView) findViewById(R.id.pososto);

		setLabels();

		MyCount counter = new MyCount(Long.parseLong((String) secondsTv
				.getText()) * 1000, 1000);
		counter.start();

	}

	public class MyCount extends CountDownTimer {
		int secs = 0;
		int mins = 0;

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		public void onFinish() {
			secondsTv.setText("0");
			minutesTv.setText("0");
			hoursTv.setText("0");
			daysTv.setText("0");
			weeksTv.setText("0");
			monthsTv.setText("0");
			posostoPB.setProgress(100);
			posostoTv.setText("100%");
		}

		public void onTick(long millisUntilFinished) {
			secondsTv.setText("" + millisUntilFinished / 1000);
			secs++;
			if (secs > 59) {
				secs = 0;
				mins++;
				minutesTv.setText(""
						+ (Integer.parseInt((String) minutesTv.getText()) - 1));
				if (mins > 59){
					mins = 0;
					hoursTv.setText(""
							+ (Integer.parseInt((String) hoursTv.getText()) - 1));
				}
			}
		}
	}

	public void setLabels() {

		DateTime curDate = new DateTime();
		DateTime inDate = setDateFromString(b.getString("indate"));
		DateTime outDate = setDateFromString(b.getString("outdate"));

		outDate = setFilakes(inDate, outDate,
				Integer.parseInt(b.getString("filaki")));

		Float totalPososto = getPososto(Seconds.secondsBetween(inDate, curDate)
				.getSeconds(), Seconds.secondsBetween(inDate, outDate)
				.getSeconds());
		setName(totalPososto);
		setPososto(totalPososto);
		setRestOfTv(curDate, outDate);
		Integer ipolipo = Days.daysBetween(curDate, outDate).getDays()
				- Integer.parseInt(b.getString("adeia"));
		Integer perasan = Days.daysBetween(inDate, curDate).getDays();

		setChart(Integer.parseInt(b.getString("adeia")), ipolipo, perasan);

	}

	public void setChart(Integer adeia, Integer ipolipo, Integer perasan) {
		if (ipolipo < 0) {
			adeia += ipolipo;
			ipolipo = 0;
		}
		if (adeia < 0) {
			adeia = 0;
		}
		if (perasan < 0) {
			perasan = 0;
		}
		String[] labels = { adeia.toString(), ipolipo.toString(),
				perasan.toString() };
		GraphicalView mChartView;
		LinearLayout layout = (LinearLayout) findViewById(R.id.pieChart);

		double[] values = new double[] { adeia, ipolipo, perasan };
		int[] colors = new int[] { Color.BLUE, Color.RED, Color.GREEN };
		DefaultRenderer renderer = buildCategoryRenderer(colors);
		mChartView = ChartFactory.getPieChartView(this,
				buildCategoryDataset("Γράφημα", labels, values), renderer);

		layout.addView(mChartView);
		layout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.chart,
						(ViewGroup) findViewById(R.id.LL1));

				Toast toastView = new Toast(getBaseContext());
				toastView.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toastView.setView(layout);
				toastView.setDuration(Toast.LENGTH_LONG);
				toastView.show();
			}
		});
	}

	public void backButtonClicked(View v) {
		finish();
	}

	public void editButtonClicked(View v) {
		Intent str = new Intent(this, com.android.leledroid.properties.class);
		str.putExtras(b);
		startActivity(str);
		finish();
	}

	public void deleteButtonClicked(View v) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					deleteButtonOk();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					return;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.delete))
				.setPositiveButton("Yes", dialogClickListener)
				.setMessage(getResources().getString(R.string.deleteMessage))
				.setNegativeButton("No", dialogClickListener).show();

	}

	public void deleteButtonOk() {
		Toast toast = null;

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
					if (counter != Integer.parseInt(b.getString("selected"))) {
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
		finish();
	}

	public void setName(Float totalPososto) {

		String vath = "", oplo = "";
		ImageView vathmosI = (ImageView) findViewById(R.id.vathmosImg);

		if (b.getString("oplo").equals("Πεζικό")) {
			oplo = "ΠΖ";
		} else if (b.getString("oplo").equals("Πυροβολικό")) {
			oplo = "ΠΒ";
		} else if (b.getString("oplo").equals("Τεθωρακισμένα")) {
			oplo = "ΤΘ";
		} else if (b.getString("oplo").equals("Μηχανικό")) {
			oplo = "ΜΧ";
		} else if (b.getString("oplo").equals("Διαβιβάσεις")) {
			oplo = "ΔΒ";
		} else if (b.getString("oplo").equals("Αεροπορία_Στρατού")) {
			oplo = "ΑΣ";
		} else if (b.getString("oplo").equals("Εφοδιασμού_Μεταφορών")) {
			oplo = "ΕΜ";
		} else if (b.getString("oplo").equals("Υλικού_Πολέμου")) {
			oplo = "ΥΠ";
		} else if (b.getString("oplo").equals("Τεχνικό")) {
			oplo = "ΤΧ";
		} else if (b.getString("oplo").equals("Υγειονομικό")) {
			oplo = "ΥΓ";
		}

		if (totalPososto < 8) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img1));
			vath = "ΣΤΡ";
		} else if (totalPososto < 14) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img2));
			vath = "ΥΔΝΕΑΣ";
		} else if (totalPososto < 20) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img3));
			vath = "ΔΝΕΑΣ";
		} else if (totalPososto < 22) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img4));
			vath = "ΛΧΙΑΣ";
		} else if (totalPososto < 28) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img5));
			vath = "ΕΠΧΙΑΣ";
		} else if (totalPososto < 34) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img6));
			vath = "ΑΛΧΙΑΣ";
		} else if (totalPososto < 40) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img7));
			vath = "ΑΝΘΣΤΗΣ";
		} else if (totalPososto < 45) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img8));
			vath = "ΑΝΘΛΓΟΣ";
		} else if (totalPososto < 50) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img9));
			vath = "ΥΠΛΓΟΣ";
		} else if (totalPososto < 56) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img10));
			vath = "ΛΓΟΣ";
		} else if (totalPososto < 62) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img11));
			vath = "ΤΧΗΣ";
		} else if (totalPososto < 68) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img12));
			vath = "ΑΝΧΗΣ";
		} else if (totalPososto < 73) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img13));
			vath = "ΣΧΗΣ";
		} else if (totalPososto < 79) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img14));
			vath = "ΤΞΧΟΣ";
		} else if (totalPososto < 85) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img15));
			vath = "ΥΣΤΓΟΣ";
		} else if (totalPososto < 91) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img16));
			vath = "ΑΝΤΓΟΣ";
		} else if (totalPososto < 97) {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img17));
			vath = "ΣΤΓΟΣ";
		} else {
			vathmosI.setImageDrawable(getResources().getDrawable(
					R.drawable.img18));
			vath = "ΠΟΛΙΤΗΣ";
			oplo = "ΚΟΣΜΟΥ";
		}
		nameTv.setText(vath + "(" + oplo + ") " + b.getString("onoma"));
	}

	public void setPososto(Float totalPososto) {
		if (totalPososto < 0)
			totalPososto = (float) 0;
		posostoPB.setProgress(totalPososto.intValue());
		posostoTv.setText(totalPososto.intValue() + "%");
	}

	public Float getPososto(Integer a, Integer b) {
		return (float) a * 100 / b;
	}

	public void setRestOfTv(DateTime curDate, DateTime outDate) {

		Integer meres = Days.daysBetween(curDate, outDate).getDays();

		secondsTv.setText(Integer.toString(Seconds.secondsBetween(curDate,
				outDate).getSeconds()));
		minutesTv.setText(Integer.toString(Minutes.minutesBetween(curDate,
				outDate).getMinutes()));
		hoursTv.setText(Integer.toString(Hours.hoursBetween(curDate, outDate)
				.getHours()));
		daysTv.setText(Integer.toString(meres));
		weeksTv.setText(String.format("%.2f", (float) meres / 7));
		monthsTv.setText(String.format("%.2f", (float) meres / 30));
	}

	public DateTime setDateFromString(String temp) {
		Integer d = Integer.parseInt(temp.split("/")[0]);
		Integer m = Integer.parseInt(temp.split("/")[1]);
		Integer y = Integer.parseInt(temp.split("/")[2]);

		return new DateTime(y, m, d, 12, 0, 0, 0);
	}

	public DateTime setFilakes(DateTime inDate, DateTime outDate, Integer fil) {
		if (fil > 0) {
			if (fil > 40) {
				return outDate.plusDays(fil);
			} else if (fil > 20) {
				return outDate.plusDays(fil - 20);
			}
		}
		return outDate;
	}

	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		renderer.setLabelsTextSize(10);
		renderer.setShowLegend(false);
		return renderer;
	}

	protected CategorySeries buildCategoryDataset(String title,
			String[] labels, double[] values) {
		CategorySeries series = new CategorySeries(title);
		series.add(labels[0], values[0]);
		series.add(labels[1], values[1]);
		series.add(labels[2], values[2]);
		return series;
	}

}
