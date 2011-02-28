package com.forfolias.leleDroid;

import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

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

public class Details extends Activity {

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
	Str str;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		Bundle b = this.getIntent().getExtras();
		str = Str.getStrFromId(b.getInt("id"));

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
				if (mins > 59) {
					mins = 0;
					hoursTv.setText(""
							+ (Integer.parseInt((String) hoursTv.getText()) - 1));
				}
			}
		}
	}

	public void setLabels() {

		GregorianCalendar dateIn = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));
		GregorianCalendar dateOut = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));
		GregorianCalendar dateCur = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));

		Integer year = Integer.parseInt(str.getDateIn().split("/")[2]);
		Integer month = Integer.parseInt(str.getDateIn().split("/")[1]);
		Integer day = Integer.parseInt(str.getDateIn().split("/")[0]);
		dateIn.set(year, month - 1, day, 12, 00, 00);

		year = Integer.parseInt(str.getDateOut().split("/")[2]);
		month = Integer.parseInt(str.getDateOut().split("/")[1]);
		day = Integer.parseInt(str.getDateOut().split("/")[0]);
		dateOut.set(year, month - 1, day, 00, 00, 01);

		dateCur = (GregorianCalendar) GregorianCalendar.getInstance();

		dateOut = setFilakes(dateIn, dateOut, str.getFilaki());

		long total = Math.abs(dateOut.getTimeInMillis()
				- dateIn.getTimeInMillis()) / 1000;
		long rest = Math.abs(dateOut.getTimeInMillis()
				- dateCur.getTimeInMillis()) / 1000;
		long past = Math.abs(dateIn.getTimeInMillis()
				- dateCur.getTimeInMillis()) / 1000;

		Float totalPososto = (float) past * 100 / total;
		if (totalPososto > 100){
			totalPososto = (float) 100;
			rest = 0;
			past = total;
		}
		setName(totalPososto);
		setPososto(totalPososto);
		setRestOfTv(dateCur, dateOut);
		setChart(str.getAdeia(), (int) (rest / (60 * 60 * 24)), (int) ( past / (60 * 60 * 24)));

	}

	public void setChart(Integer adeia, Integer ipiretisimoYpoloipo, Integer perasan) {
		
		if (adeia >= ipiretisimoYpoloipo){
			adeia = ipiretisimoYpoloipo;
			ipiretisimoYpoloipo = 0;
		}else{
			ipiretisimoYpoloipo -= adeia;			
		}
		
		if (perasan < 0) {
			perasan = 0;
		}
		
		if (ipiretisimoYpoloipo < 0) {
			ipiretisimoYpoloipo = 0;
			adeia = 0;
		}
		if (adeia < 0) {
			adeia = 0;
		}

		String[] labels = { adeia.toString(), ipiretisimoYpoloipo.toString(),
				perasan.toString() };
		GraphicalView mChartView;
		LinearLayout layout = (LinearLayout) findViewById(R.id.pieChart);

		double[] values = new double[] { adeia, ipiretisimoYpoloipo, perasan };
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
				toastView.setDuration(Toast.LENGTH_SHORT);
				toastView.show();
			}
		});
	}

	public void editButtonClicked(View v) {
		Bundle b = new Bundle();
		Intent edit = new Intent(this, com.forfolias.leleDroid.Properties.class);
		b.putInt("id", str.getId());
		edit.putExtras(b);
		startActivity(edit);
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
				.setPositiveButton(R.string.ok, dialogClickListener)
				.setMessage(getResources().getString(R.string.deleteMessage))
				.setNegativeButton(R.string.cancel, dialogClickListener).show();
	}

	public void deleteButtonOk() {
		str.delete();
		finish();
	}

	public void setName(Float totalPososto) {

		String vath = "";
		ImageView img = (ImageView) findViewById(R.id.vathmosImg);

		if (totalPososto < 8) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img1));
			vath = "ΣΤΡΑΤΙΩΤΗΣ";
		} else if (totalPososto < 14) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img2));
			vath = "ΥΠΟΔΕΚΑΝΕΑΣ";
		} else if (totalPososto < 20) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img3));
			vath = "ΔΕΚΑΝΕΑΣ";
		} else if (totalPososto < 22) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img4));
			vath = "ΛΟΧΙΑΣ";
		} else if (totalPososto < 28) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img5));
			vath = "ΕΠΙΛΟΧΙΑΣ";
		} else if (totalPososto < 34) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img6));
			vath = "ΑΡΧΙΛΟΧΙΑΣ";
		} else if (totalPososto < 40) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img7));
			vath = "ΑΝΘΥΠΑΣΠΙΣΤΗΣ";
		} else if (totalPososto < 45) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img8));
			vath = "ΑΝΘΥΠΟΛΟΧΑΓΟΣ";
		} else if (totalPososto < 50) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img9));
			vath = "ΥΠΟΛΟΧΑΓΟΣ";
		} else if (totalPososto < 56) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img10));
			vath = "ΛΟΧΑΓΟΣ";
		} else if (totalPososto < 62) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img11));
			vath = "ΤΑΓΜΑΤΑΡΧΗΣ";
		} else if (totalPososto < 68) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img12));
			vath = "ΑΝΤΙΣΥΝΤΑΓΜΑΤΑΡΧΗΣ";
		} else if (totalPososto < 73) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img13));
			vath = "ΣΥΝΤΑΓΜΑΤΑΡΧΗΣ";
		} else if (totalPososto < 79) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img14));
			vath = "ΤΑΞΙΑΡΧΟΣ";
		} else if (totalPososto < 85) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img15));
			vath = "ΥΠΟΣΤΡΑΤΗΓΟΣ";
		} else if (totalPososto < 91) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img16));
			vath = "ΑΝΤΙΣΤΡΑΤΗΓΟΣ";
		} else if (totalPososto < 97) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img17));
			vath = "ΣΤΡΑΤΗΓΟΣ";
		} else {
			img.setImageDrawable(getResources().getDrawable(R.drawable.img18));
			vath = "ΠΟΛΙΤΗΣ";
		}
		nameTv.setText(vath + " " + str.getName());
	}

	public void setPososto(Float totalPososto) {
		if (totalPososto < 0)
			totalPososto = (float) 0;
		posostoPB.setProgress(totalPososto.intValue());
		posostoTv.setText(totalPososto.intValue() + "%");
	}

	public void setRestOfTv(GregorianCalendar dateCur, GregorianCalendar dateOut) {

		Long seconds = (long) (dateOut.getTimeInMillis()
				- dateCur.getTimeInMillis()) / 1000;
		if (seconds < 0) seconds = (long) 0; 
		Long minutes  = (long) seconds / 60;
		Long hours    = (long) seconds / (60 * 60);
		Integer meres = (int) (seconds / (60 * 60 * 24));

		secondsTv.setText(Long.toString(seconds));
		minutesTv.setText(Long.toString(minutes));
		hoursTv.setText(Long.toString(hours));
		daysTv.setText(Integer.toString(meres));
		weeksTv.setText(String.format("%.2f", (float) meres / 7));
		monthsTv.setText(String.format("%.2f", (float) meres / 30));
	}

	public GregorianCalendar setFilakes(GregorianCalendar inDate,
			GregorianCalendar outDate, Integer fil) {
		if (fil > 0) {
			if (fil > 40) {
				outDate.add(GregorianCalendar.DATE, fil);
			} else if (fil > 20) {
				outDate.add(GregorianCalendar.DATE, fil - 20);
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
