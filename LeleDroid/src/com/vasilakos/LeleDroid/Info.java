package com.vasilakos.LeleDroid;

import java.io.InputStream;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Info extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);

		TextView link = (TextView) findViewById(R.id.site);
		TextView cont = (TextView) findViewById(R.id.contact);
		TextView ver = (TextView) findViewById(R.id.version);
		try {
			ver.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		link.setText("<a href='" + link.getText() + "'>" + link.getText()
				+ "</a>");
		cont.setText(cont.getText()
				+ " <a href='mailto:giorg.vasilakos@gmail.com'></a>");
		Linkify.addLinks(cont, Pattern.compile("giorg.vasilakos@gmail.com"),
				"http://");
		Linkify.addLinks(link,
				Pattern.compile(getResources().getString(R.id.site)), "http://");
		cont.setText(Html.fromHtml(cont.getText()
				+ "<a href='mailto:giorg.vasilakos@gmail.com'>giorg.vasilakos@gmail.com</a>"));
		link.setText(Html.fromHtml((String) link.getText()));
		cont.setMovementMethod(LinkMovementMethod.getInstance());
		link.setMovementMethod(LinkMovementMethod.getInstance());

		ImageView img = (ImageView) findViewById(R.id.infoImage);
		
		img.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Info.this.openOptionsMenu();
			}
		});
		
		img.setOnLongClickListener(new View.OnLongClickListener() {

			public boolean onLongClick(View v) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Άκυρο ψαρά!", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 75, 35);
				toast.show();
				return true;
			}
		});
	}

	public void donateButtonClicked(View v) {
		Intent browse = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=M5HYBKFQYS84S&lc=GR&item_name=Donation%20to%20LeleDroid%20application&currency_code=EUR&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHosted"));
		startActivity(browse);
	}

	public void licenceButtonClicked(View v) {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setMessage(getResources().getString(R.string.licenceText))
				.setTitle(getResources().getString(R.string.licence));
		alertbox.setNeutralButton(getResources().getString(R.string.back),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		alertbox.show();
	}

	public void vathmoiButtonClicked(View v) {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		String text = "";
		try {
			InputStream in_s = getResources().openRawResource(
					R.raw.explain_vathmoi);

			byte[] b = new byte[in_s.available()];
			in_s.read(b);
			text = new String(b);
		} catch (Exception e) {
			text = "File not found!";
		}
		alertbox.setMessage(text).setTitle(
				getResources().getString(R.string.vathmoiInfo));
		alertbox.setNeutralButton(getResources().getString(R.string.back),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		alertbox.show();
	}

	public void shareButtonClicked() {
		Intent intent = new Intent(Intent.ACTION_SEND);

		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT,
				getResources().getString(R.string.share));
		intent.putExtra(Intent.EXTRA_TEXT,
				getResources().getString(R.string.shareAppText) + " "
						+ getResources().getString(R.string.site));

		startActivity(Intent.createChooser(intent, getString(R.string.share)));
	}

	public void onRateButtonClick(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=com.vasilakos.LeleDroid"));
		startActivity(intent);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, R.string.licence).setIcon(
				R.drawable.ic_menu_info_details);
		menu.add(0, 2, 0, R.string.share).setIcon(R.drawable.ic_menu_share);
		menu.add(0, 3, 0, R.string.vathmoiInfo)
				.setIcon(R.drawable.ic_menu_view);
		menu.add(0, 4, 0, R.string.rate).setIcon(R.drawable.ic_menu_star);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			licenceButtonClicked(null);
			return true;
		case 2:
			shareButtonClicked();
			return true;
		case 3:
			vathmoiButtonClicked(null);
			return true;
		case 4:
			onRateButtonClick(null);
			return true;
		}
		return false;
	}

}
