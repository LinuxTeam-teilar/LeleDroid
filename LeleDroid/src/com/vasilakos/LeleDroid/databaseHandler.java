package com.vasilakos.LeleDroid;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "strManager";
	private static final String TABLE_STR = "str";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_START_DATE = "start_date";
	private static final String KEY_END_DATE = "end_date";
	private static final String KEY_DAYS_OFF = "days_off";
	private static final String KEY_EXTRA_DAYS = "extra_days";

	public databaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_STR + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_START_DATE + " TEXT," + KEY_END_DATE + " TEXT,"
				+ KEY_DAYS_OFF + " INTEGER," + KEY_EXTRA_DAYS + " INTEGER)";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STR);
		onCreate(db);
	}

	public void addStr(Str soldier) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, soldier.getName());
		values.put(KEY_START_DATE, soldier.getDateIn());
		values.put(KEY_END_DATE, soldier.getDateOut());
		values.put(KEY_DAYS_OFF, soldier.getAdeia());
		values.put(KEY_EXTRA_DAYS, soldier.getFilaki());

		db.insert(TABLE_STR, null, values);
		db.close();
	}

	public Str getStr(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STR + " WHERE " + KEY_ID + " = " + id, null);
		
		if (cursor != null)
			cursor.moveToFirst();
		
		Str str = new Str();
		
		str.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
		str.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
		str.setDateIn(cursor.getString(cursor.getColumnIndex(KEY_START_DATE)));
		str.setDateOut(cursor.getString(cursor.getColumnIndex(KEY_END_DATE)));
		str.setAdeia(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_DAYS_OFF))));
		str.setFilaki(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_EXTRA_DAYS))));
		
		cursor.close();
		db.close();
		
		return str;
	}

	public List<Str> getAllStr() {
		List<Str> strList = new ArrayList<Str>();
		String selectQuery = "SELECT  * FROM " + TABLE_STR;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Str str = new Str();
				str.setId(Integer.parseInt(cursor.getString(0)));
				str.setName(cursor.getString(1));
				str.setDateIn(cursor.getString(2));
				str.setDateOut(cursor.getString(3));
				str.setAdeia(Integer.parseInt(cursor.getString(4)));
				str.setFilaki(Integer.parseInt(cursor.getString(5)));

				strList.add(str);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return strList;
	}
	
	public int getStrCount() {
        String countQuery = "SELECT  * FROM " + TABLE_STR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
 
        return count;
    }

	public int updateStr(Str soldier) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, soldier.getName());
		values.put(KEY_START_DATE, soldier.getDateIn());
		values.put(KEY_END_DATE, soldier.getDateOut());
		values.put(KEY_DAYS_OFF, soldier.getAdeia());
		values.put(KEY_EXTRA_DAYS, soldier.getFilaki());

		
		int result = db.update(TABLE_STR, values, KEY_ID + " = ?",
				new String[] { String.valueOf(soldier.getId()) });
		
		db.close();
		return result;
	}

	public void deleteStr(Str str) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_STR, KEY_ID + " = ?",
				new String[] { String.valueOf(str.getId()) });
		db.close();
	}

	public void deleteStr(Integer id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_STR, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}
}
