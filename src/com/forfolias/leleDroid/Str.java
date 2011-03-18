package com.forfolias.leleDroid;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import android.util.Log;

@SuppressWarnings({ "rawtypes", "serial" })
public class Str extends HashMap {
	String name, dateIn, dateOut;
	Integer id, adeia, filaki;

	private static String dataFile = "/sdcard/leleDroid.txt";
	private static String TAG = "leleDroid";

	public static String KEY_NAME = "name";
	public static String KEY_DATE = "date";

	@Override
	public String get(Object k) {
		String key = (String) k;
		if (KEY_NAME.equals(key))
			return name;
		else if (KEY_DATE.equals(key))
			return dateOut;
		return null;
	}

	Str() {
		setStr("", "", "", 0, 0);
		this.setId(getLengh() + 1);
	}

	Str(String nam, String date1, String date2, Integer ad, Integer fi) {
		this.setId(getLengh() + 1);
		this.setName(nam);
		this.setDateIn(date1);
		this.setDateOut(date2);
		this.setAdeia(ad);
		this.setFilaki(fi);
	}

	public String getName() {
		return this.name;
	}

	public String getDateIn() {
		return this.dateIn;
	}

	public String getDateOut() {
		return this.dateOut;
	}

	public Integer getId() {
		return this.id;
	}

	public Integer getAdeia() {
		return this.adeia;
	}

	public Integer getFilaki() {
		return this.filaki;
	}
	
	public Integer getRestDays(){
		return (int) (getRestSeconds() / (60 * 60 * 24));
	}
	
	public Integer getPastDays(){
		return (int) (getPastSeconds() / (60 * 60 * 24));
	}
	
	public Long getRestSeconds(){
		GregorianCalendar dateO = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));
		GregorianCalendar dateC = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));
		
		dateO.set(Integer.parseInt(dateOut.split("/")[2]),
				  Integer.parseInt(dateOut.split("/")[1]) - 1,
				  Integer.parseInt(dateOut.split("/")[0]),
				  00, 00, 01);
		
		dateC = (GregorianCalendar) GregorianCalendar.getInstance();
		
		if (filaki > 0) {
			if (filaki > 40) {
				dateO.add(GregorianCalendar.DATE, filaki);
			} else if (filaki > 20) {
				dateO.add(GregorianCalendar.DATE, filaki - 20);
			}
		}

		long rest = Math.abs(dateO.getTimeInMillis()
				- dateC.getTimeInMillis()) / 1000;
		
		if (getPososto() >= 100){
			rest = 0;
		}
		
		return rest;
	}
	
	public Long getTotalSeconds(){
		GregorianCalendar dateO = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));
		GregorianCalendar dateI = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));
		
		dateO.set(Integer.parseInt(dateOut.split("/")[2]),
				  Integer.parseInt(dateOut.split("/")[1]) - 1,
				  Integer.parseInt(dateOut.split("/")[0]),
				  00, 00, 01);
		
		dateI.set(Integer.parseInt(dateIn.split("/")[2]),
				  Integer.parseInt(dateIn.split("/")[1]) - 1,
				  Integer.parseInt(dateIn.split("/")[0]),
				  12, 00, 00);
		
		long total =  Math.abs(dateO.getTimeInMillis()
				- dateI.getTimeInMillis()) / 1000;
		
		return total;
	}
	
	public Long getPastSeconds(){
		GregorianCalendar dateI = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));
		GregorianCalendar dateC = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));
		
		dateI.set(Integer.parseInt(dateIn.split("/")[2]),
				  Integer.parseInt(dateIn.split("/")[1]) - 1,
				  Integer.parseInt(dateIn.split("/")[0]),
				  12, 00, 00);
		
		dateC = (GregorianCalendar) GregorianCalendar.getInstance();

		long past = Math.abs(dateI.getTimeInMillis()
				- dateC.getTimeInMillis()) / 1000;
		
		return past;
	}
	
	public Float getPososto(){

		Float totalPososto = (float) getPastSeconds() * 100 / getTotalSeconds();
		if (totalPososto > 100){
			totalPososto = (float) 100;
		}
		return totalPososto;
	}

	public static Str getStrFromId(Integer num) {
		
		String line = "0 * 0 * 0/0/0 * 0/0/0 * 0 * 0";
		Integer c = 0;
		Str strat = new Str();

		try {
			BufferedReader in = new BufferedReader(new FileReader(dataFile));
			while (c < num) {
				line = in.readLine();
				c++;
			}
			in.close();
		} catch (IOException e) {
			Log.e(TAG, "getStrFromId error : " + e.getLocalizedMessage());
			return strat;
		}
		strat.setStrFromString(line);
		
		return strat;
	}

	public static String getStrFromIdToString(Integer num) {
		String line = null;
		Integer c = 0;

		try {
			BufferedReader in = new BufferedReader(new FileReader(dataFile));
			while (c < num) {
				line = in.readLine();
				c++;
			}
			in.close();
		} catch (IOException e) {
			Log.e(TAG, "getStrToString error : " + e.getLocalizedMessage());
		}
		return line;
	}

	public static List<Str> getStrList() {	
		List<Str> list = new ArrayList<Str>();
		Integer N = getLengh();
		
		for (int i = 0 ; i < N ; i++){
			list.add(getStrFromId(i+1));
		}
		return list;
	}
	
	public String getVathmo(){
		Float totalPososto = getPososto();
		String vath = "";

		if (totalPososto < 8) {
			vath = "ΣΤΡΑΤΙΩΤΗΣ";
		} else if (totalPososto < 14) {
			vath = "ΥΠΟΔΕΚΑΝΕΑΣ";
		} else if (totalPososto < 20) {
			vath = "ΔΕΚΑΝΕΑΣ";
		} else if (totalPososto < 22) {
			vath = "ΛΟΧΙΑΣ";
		} else if (totalPososto < 28) {
			vath = "ΕΠΙΛΟΧΙΑΣ";
		} else if (totalPososto < 34) {
			vath = "ΑΡΧΙΛΟΧΙΑΣ";
		} else if (totalPososto < 40) {
			vath = "ΑΝΘΥΠΑΣΠΙΣΤΗΣ";
		} else if (totalPososto < 45) {
			vath = "ΑΝΘΥΠΟΛΟΧΑΓΟΣ";
		} else if (totalPososto < 50) {
			vath = "ΥΠΟΛΟΧΑΓΟΣ";
		} else if (totalPososto < 56) {
			vath = "ΛΟΧΑΓΟΣ";
		} else if (totalPososto < 62) {
			vath = "ΤΑΓΜΑΤΑΡΧΗΣ";
		} else if (totalPososto < 68) {
			vath = "ΑΝΤΙΣΥΝΤΑΓΜΑΤΑΡΧΗΣ";
		} else if (totalPososto < 73) {
			vath = "ΣΥΝΤΑΓΜΑΤΑΡΧΗΣ";
		} else if (totalPososto < 79) {
			vath = "ΤΑΞΙΑΡΧΟΣ";
		} else if (totalPososto < 85) {
			vath = "ΥΠΟΣΤΡΑΤΗΓΟΣ";
		} else if (totalPososto < 91) {
			vath = "ΑΝΤΙΣΤΡΑΤΗΓΟΣ";
		} else if (totalPososto < 97) {
			vath = "ΣΤΡΑΤΗΓΟΣ";
		} else {
			vath = "ΠΟΛΙΤΗΣ";
		}
		return vath;
	}

	public int getImg(){
		Float totalPososto = getPososto();
		int img = 0;

		if (totalPososto < 8) {
			img = R.drawable.img1;
		} else if (totalPososto < 14) {
			img = R.drawable.img2;
		} else if (totalPososto < 20) {
			img = R.drawable.img3;
		} else if (totalPososto < 22) {
			img = R.drawable.img4;
		} else if (totalPososto < 28) {
			img = R.drawable.img5;
		} else if (totalPososto < 34) {
			img = R.drawable.img6;
		} else if (totalPososto < 40) {
			img = R.drawable.img7;
		} else if (totalPososto < 45) {
			img = R.drawable.img8;
		} else if (totalPososto < 50) {
			img = R.drawable.img9;
		} else if (totalPososto < 56) {
			img = R.drawable.img10;
		} else if (totalPososto < 62) {
			img = R.drawable.img11;
		} else if (totalPososto < 68) {
			img = R.drawable.img12;
		} else if (totalPososto < 73) {
			img = R.drawable.img13;
		} else if (totalPososto < 79) {
			img = R.drawable.img14;
		} else if (totalPososto < 85) {
			img = R.drawable.img15;
		} else if (totalPososto < 91) {
			img = R.drawable.img16;
		} else if (totalPososto < 97) {
			img = R.drawable.img17;
		} else {
			img = R.drawable.img18;
		}
		return img;
	}
	
	public void setName(String nam) {
		this.name = nam;
	}

	public void setDateIn(String date) {
		this.dateIn = date;
	}

	public void setDateOut(String date) {
		this.dateOut = date;
	}

	public void setAdeia(Integer ad) {
		this.adeia = ad;
	}

	public void setFilaki(Integer fi) {
		this.filaki = fi;
	}

	public void setId(Integer i) {
		this.id = i;
	}

	public String toString() {
		return this.getId() + " * " + this.getName().replace(' ', '_') + " * "
				+ this.getDateIn() + " * " + this.getDateOut() + " * "
				+ this.getAdeia() + " * " + this.getFilaki();
	}

	public void setStr(String nam, String d1, String d2, Integer ad, Integer fi) {
		this.setName(nam);
		this.setDateIn(d1);
		this.setDateOut(d2);
		this.setAdeia(ad);
		this.setFilaki(fi);
	}

	public void setStrFromString(String line) {
		this.setId(Integer.parseInt(line.split(" * ")[0]));
		this.setName(line.split(" * ")[2].replace('_', ' '));
		this.setDateIn(line.split(" * ")[4]);
		this.setDateOut(line.split(" * ")[6]);
		this.setAdeia(Integer.parseInt(line.split(" * ")[8]));
		this.setFilaki(Integer.parseInt(line.split(" * ")[10]));
	}

	public static Integer getLengh() {
		byte[] c = new byte[1024];
		int count = 0;
		int readChars = 0;
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(dataFile));
		} catch (FileNotFoundException e) {
			Log.e(TAG, "getLengh error. FileNotFound : " + e.getLocalizedMessage());
			return 0;
		}
		try {
			while ((readChars = is.read(c)) != -1) {
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n')
						++count;
				}
			}
		} catch (IOException e) {
			Log.e(TAG, "getLengh error B : " + e.getLocalizedMessage());
			return 0;
		}
		return count;
	}

	public void writeStr() {

		try {
			File f = new File(dataFile);
			if (f.exists()) {
				Log.e(TAG, "writeStr File exists : " + dataFile);
			} else {
				Log.e(TAG,
						"writeStr File NOT exist! absolutepath : "
								+ f.getAbsolutePath());
				FileOutputStream out = new FileOutputStream(dataFile, true);
				out.close();
			}
		} catch (java.io.FileNotFoundException e) {
			Log.e(TAG, "writeStr FileNotFoundException error : "
							+ e.getLocalizedMessage());
		} catch (IOException e) {
			Log.e(TAG, "writeStr IOException : " + e.getLocalizedMessage());
		}

		String data = this.id + " * " + this.name.replace(' ', '_') + " * "
				+ this.dateIn + " * " + this.dateOut + " * " + this.adeia
				+ " * " + this.filaki + "\n";

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(dataFile,
					true));
			out.write(data);
			out.close();
		} catch (IOException e) {
			Log.e(TAG, "writeStr error B : " + e.getLocalizedMessage());
		}
		correctData();
	}

	public boolean delete() {
		if (deleteStrFromId(this.getId()))
			return true;
		return false;
	}

	public static boolean deleteStrFromId(Integer num) {
		String line = null;

		if (getLengh() == 0)
			return false;

		/* Copy data.txt to data.tmp.txt without the line with id num */

		try {
			BufferedReader in = new BufferedReader(new FileReader(dataFile));
			BufferedWriter out = new BufferedWriter(new FileWriter(dataFile
					+ ".tmp", true));
			while ((line = in.readLine()) != null) {
				if (num != Integer.parseInt(line.split(" * ")[0])) {
					out.write(line + "\n");
				}
			}
			in.close();
			out.close();
		} catch (IOException e) {
			Log.e(TAG, "deleteStrFromId error : " + e.getLocalizedMessage());
			return false;
		}

		/* Rename data.tmp.txt to data.txt */

		File tmp = new File(dataFile + ".tmp");
		File txt = new File(dataFile);
		if (tmp.renameTo(txt) == false)
			return false;

		correctData();

		return true;
	}

	public static void correctData() {
		String line = null;
		Integer c = 0;
		Str strat = new Str();

		try {
			BufferedReader in = new BufferedReader(new FileReader(dataFile));
			BufferedWriter out = new BufferedWriter(new FileWriter(dataFile
					+ ".tmp", true));
			while ((line = in.readLine()) != null) {
				strat.setStrFromString(line);
				strat.setId(++c);
				out.write(strat.toString() + "\n");
			}
			in.close();
			out.close();
		} catch (IOException e) {
			Log.e(TAG, "correctData error : " + e.getLocalizedMessage());
		}

		/* Rename data.tmp.txt to data.txt */

		File tmp = new File(dataFile + ".tmp");
		File txt = new File(dataFile);
		tmp.renameTo(txt);
	}

}
