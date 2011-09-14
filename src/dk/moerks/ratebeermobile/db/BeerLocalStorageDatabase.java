/*
 * Copyright 2011, Tatu Lahtela
 * 
 * This file is part of Ratebeer Mobile for Android.
 *
 * Ratebeer Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ratebeer Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ratebeer Mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.moerks.ratebeermobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import dk.moerks.ratebeermobile.vo.RatingData;

public class BeerLocalStorageDatabase {
	private Context context;
	private SQLiteDatabase database;
	private BeerLocalStorageHelper dbHelper;

	public static final String BEER_ID = "beer_id";
	public static final String AROMA = "aroma";
	public static final String APPEARANCE = "appearance";
	public static final String TASTE = "taste";
	public static final String PALATE = "palate";
	public static final String OVERALL = "overall";
	
	
	public BeerLocalStorageDatabase(Context context) {
		this.context = context;
		
	}

	
	public BeerLocalStorageDatabase open() throws SQLException {
		dbHelper = new BeerLocalStorageHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public RatingData getBeer(String beerId) {
		Cursor query = database.query(BeerLocalStorageHelper.DB_NAME, 
				new String[] { APPEARANCE,
				AROMA, PALATE,
				TASTE, OVERALL
				}, BEER_ID + " = "  +beerId,  null, null,
				null, null);
		if (query.moveToFirst()) {
			RatingData data = new RatingData();
			int index = 0;
			data.setAppearance(query.getInt(index++));
			data.setAroma(query.getInt(index++));
			data.setPalate(query.getInt(index++));
			data.setFlavor(query.getInt(index++));
			data.setOverall(query.getInt(index++));
			data.setId(beerId);
			return data;
		} else {
			return null;
			
		}
	}
	
	public long saveBeer(RatingData data) {
		ContentValues vals  = new ContentValues();
		vals.put(BEER_ID, data.getId());
		vals.put(AROMA, data.getAroma());
		vals.put(TASTE, data.getFlavor());
		vals.put(PALATE, data.getPalate());
		vals.put(OVERALL, data.getOverall());
		return database.insert(BeerLocalStorageHelper.DB_NAME, null, vals);
	}
	
	
	public void close() {
		if (dbHelper != null) {
			dbHelper.close();	
		}
	}
	
	
	
	private class BeerLocalStorageHelper extends SQLiteOpenHelper {
		public static final String DB_NAME = "RateBeerMobile";
		public static final int VERSION = 1;
		
		
		
		
		
		public static final String CREATE_SQL = 
			   "CREATE TABLE " + DB_NAME + " (" +
	           BEER_ID + " TEXT, " +
	           AROMA + " INTEGER, " + 
	           APPEARANCE + " INTEGER, " + 
	           TASTE + " INTEGER, " + 
	           PALATE + " INTEGER, " + 
	           OVERALL + " INTEGER " + 
	          " );";

		public BeerLocalStorageHelper(Context context) {
			super(context, DB_NAME, null, VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			 db.execSQL(CREATE_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		}
		

	}
}
