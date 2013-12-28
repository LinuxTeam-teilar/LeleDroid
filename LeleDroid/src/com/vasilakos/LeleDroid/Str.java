package com.vasilakos.LeleDroid;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;

@SuppressWarnings({ "rawtypes", "serial" })
public class Str extends HashMap {
	String name, dateIn, dateOut;
	Integer id, adeia, filaki;

//	private static String TAG = "leleDroid";

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
	}

	Str(String nam, String date1, String date2, Integer ad, Integer fi) {
		this.setId(0);
		this.setName(nam);
		this.setDateIn(date1);
		this.setDateOut(date2);
		this.setAdeia(ad);
		this.setFilaki(fi);
	}

	Str(Integer id, String nam, String date1, String date2, Integer ad,
			Integer fi) {
		this.setId(id);
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

	public Integer getRestDays() {
		return (int) (getRestSeconds() / (60 * 60 * 24));
	}

	public Integer getPastDays() {
		return (int) (getPastSeconds() / (60 * 60 * 24));
	}

	public Long getRestSeconds() {
		GregorianCalendar dateO = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));
		GregorianCalendar dateC = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));

		dateO.set(Integer.parseInt(dateOut.split("/")[2]),
				Integer.parseInt(dateOut.split("/")[1]) - 1,
				Integer.parseInt(dateOut.split("/")[0]), 00, 00, 01);

		dateC = (GregorianCalendar) GregorianCalendar.getInstance();

		if (filaki > 0) {
			if (filaki > 40) {
				dateO.add(GregorianCalendar.DATE, filaki);
			} else if (filaki > 20) {
				dateO.add(GregorianCalendar.DATE, filaki - 20);
			}
		}

		long rest = Math.abs(dateO.getTimeInMillis() - dateC.getTimeInMillis()) / 1000;

		if (getPososto() >= 100) {
			rest = 0;
		}

		return rest;
	}

	public Long getTotalSeconds() {
		GregorianCalendar dateO = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));
		GregorianCalendar dateI = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));

		dateO.set(Integer.parseInt(dateOut.split("/")[2]),
				Integer.parseInt(dateOut.split("/")[1]) - 1,
				Integer.parseInt(dateOut.split("/")[0]), 00, 00, 01);

		dateI.set(Integer.parseInt(dateIn.split("/")[2]),
				Integer.parseInt(dateIn.split("/")[1]) - 1,
				Integer.parseInt(dateIn.split("/")[0]), 12, 00, 00);

		long total = Math
				.abs(dateO.getTimeInMillis() - dateI.getTimeInMillis()) / 1000;

		return total;
	}

	public Long getPastSeconds() {
		GregorianCalendar dateI = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));
		GregorianCalendar dateC = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Athens"));

		int year = Integer.parseInt(dateIn.split("/")[2]);
		if (year < 100) {
			year += 2000;
		}
		dateI.set(year,
				Integer.parseInt(dateIn.split("/")[1]) - 1,
				Integer.parseInt(dateIn.split("/")[0]), 12, 00, 00);

		dateC = (GregorianCalendar) GregorianCalendar.getInstance();
		
		return Math.abs(dateI.getTimeInMillis() - dateC.getTimeInMillis()) / 1000;
	}

	public Float getPososto() {

		Float totalPososto = (float) getPastSeconds() * 100 / getTotalSeconds();
		if (totalPososto > 100) {
			totalPososto = (float) 100;
		}
		return totalPososto;
	}

	public static Str getStrFromId(Integer num, Context context) {
		databaseHandler db = new databaseHandler(context);
		return db.getStr(num);
	}

	public static List<Str> getStrList(Context context) {
		databaseHandler db = new databaseHandler(context);
		return db.getAllStr();
	}

	public int getVathmo() {
		Float totalPososto = getPososto();
		int vath = 0;

		if (totalPososto < 8) {
			vath = getResourceVathmo(0);
		} else if (totalPososto < 14) {
			vath = getResourceVathmo(1);
		} else if (totalPososto < 20) {
			vath = getResourceVathmo(2);
		} else if (totalPososto < 22) {
			vath = getResourceVathmo(3);
		} else if (totalPososto < 28) {
			vath = getResourceVathmo(4);
		} else if (totalPososto < 34) {
			vath = getResourceVathmo(5);
		} else if (totalPososto < 40) {
			vath = getResourceVathmo(6);
		} else if (totalPososto < 45) {
			vath = getResourceVathmo(7);
		} else if (totalPososto < 50) {
			vath = getResourceVathmo(8);
		} else if (totalPososto < 56) {
			vath = getResourceVathmo(9);
		} else if (totalPososto < 62) {
			vath = getResourceVathmo(10);
		} else if (totalPososto < 68) {
			vath = getResourceVathmo(11);
		} else if (totalPososto < 73) {
			vath = getResourceVathmo(12);
		} else if (totalPososto < 79) {
			vath = getResourceVathmo(13);
		} else if (totalPososto < 85) {
			vath = getResourceVathmo(14);
		} else if (totalPososto < 91) {
			vath = getResourceVathmo(15);
		} else if (totalPososto < 97) {
			vath = getResourceVathmo(16);
		} else {
			vath = getResourceVathmo(17);
		}
		return vath;
	}

	public int getImg() {
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

	public void writeStr(Context context) {
		databaseHandler db = new databaseHandler(context);
		db.addStr(this);
	}

	public boolean delete(Context context) {
		if (deleteStrFromId(this.getId(), context))
			return true;
		return false;
	}
	
	public static boolean deleteStrFromId(Integer num, Context context) { 
		databaseHandler db = new databaseHandler(context);
		db.deleteStr(num);
		return true;
	}

	public static int getResourceVathmo(int vathmos) {
		int va = 0;
		switch (vathmos) {
		case 0:
			va = R.string.v0;
			break;
		case 1:
			va = R.string.v1;
			break;
		case 2:
			va = R.string.v2;
			break;
		case 3:
			va = R.string.v3;
			break;
		case 4:
			va = R.string.v4;
			break;
		case 5:
			va = R.string.v5;
			break;
		case 6:
			va = R.string.v6;
			break;
		case 7:
			va = R.string.v7;
			break;
		case 8:
			va = R.string.v8;
			break;
		case 9:
			va = R.string.v9;
			break;
		case 10:
			va = R.string.v10;
			break;
		case 11:
			va = R.string.v11;
			break;
		case 12:
			va = R.string.v12;
			break;
		case 13:
			va = R.string.v13;
			break;
		case 14:
			va = R.string.v14;
			break;
		case 15:
			va = R.string.v15;
			break;
		case 16:
			va = R.string.v16;
			break;
		case 17:
			va = R.string.v17;
			break;
		default:
			break;
		}
		return va;
	}

}
