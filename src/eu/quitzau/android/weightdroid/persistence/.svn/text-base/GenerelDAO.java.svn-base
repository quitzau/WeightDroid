package eu.quitzau.android.weightdroid.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class GenerelDAO {

	private static final String DATABASE_NAME = "WeightDroid";
	protected static final String USER = "user";

	protected Context ctx;

	protected GenerelDAO() {

	}

	protected SQLiteDatabase openDB() {
		SQLiteDatabase db = ctx.openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);

		return db;
	}

	protected void closeDB(SQLiteDatabase db) {
		if (db != null) {
			db.close();
		}
	}
}
