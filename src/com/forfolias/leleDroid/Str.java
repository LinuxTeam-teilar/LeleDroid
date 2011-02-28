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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
		Str item = new Str();
		String line = "0 * 0 * 0/0/0 * 0/0/0 * 0 * 0";

		try {
			BufferedReader in = new BufferedReader(new FileReader(dataFile));
			while ((line = in.readLine()) != null) {
				item.setStrFromString(line);
				list.add(item);
				item = new Str();
			}
			in.close();
		} catch (IOException e) {
			Log.e(TAG, "getStrList error : " + e.getLocalizedMessage());
		}
		return list;
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
		return this.getId() + " * " + this.getName() + " * " + this.getDateIn()
				+ " * " + this.getDateOut() + " * " + this.getAdeia() + " * "
				+ this.getFilaki();
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
			Log.e(TAG, "getLengh error A : " + e.getLocalizedMessage());
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
			Log.e(TAG,
					"writeStr FileNotFoundException error : "
							+ e.getLocalizedMessage());
		} catch (IOException e) {
			Log.e(TAG, "writeStr IOException : " + e.getLocalizedMessage());
		}

		String data = this.id + " * " + this.name + " * " + this.dateIn + " * "
				+ this.dateOut + " * " + this.adeia + " * " + this.filaki
				+ "\n";

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
	
	public boolean delete(){
		if(deleteStrFromId(this.getId()))
			return true;
		return false;
	}

	public static boolean deleteStrFromId(Integer num) {
		String line = null;
		
		if(getLengh() == 0) return false;

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

	public static void sortData() {
		String line = null;
		List<String> listOfItems = null;

		/* Read text file and store it at an Array */

		try {
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			listOfItems = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				listOfItems.add(line);
			}
			reader.close();
		} catch (Exception e) {
			Log.e(TAG, "Sort Error A : " + e.getLocalizedMessage());
		}

		/* Sort the array */

		Collections.sort(listOfItems);

		/* Write the sorted array at the data.tmp.txt */

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(dataFile
					+ ".tmp", true));
			for (String item : listOfItems) {
				out.write(item + "\n");
			}
			out.close();
		} catch (IOException e) {
			Log.e(TAG, "Sort Error B : " + e.getLocalizedMessage());
		}

		/* Rename data.tmp.txt to data.txt */

		File tmp = new File(dataFile + ".tmp");
		File txt = new File(dataFile);
		tmp.renameTo(txt);
	}
}
