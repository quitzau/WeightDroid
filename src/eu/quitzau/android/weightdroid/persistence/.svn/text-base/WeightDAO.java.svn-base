package eu.quitzau.android.weightdroid.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import eu.quitzau.android.weightdroid.dto.WeightDTO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WeightDAO extends GenerelDAO{

	private static WeightDAO instance;

	private static final String WEIGHT_DATE = "weight_date";
	private static final String SIZE = "size";
	private static final String WEIGHT = "weight";
	private static final String WEIGHT_ID = "weight_id";
	private static final String TABLE_WEIGHT = "WeightTable";
	private static final String CREATE_TABLE_WEIGHT = "CREATE TABLE IF NOT EXISTS " + TABLE_WEIGHT + " (" + WEIGHT_ID + " INTEGER primary key autoincrement, " + WEIGHT + " TEXT not null, " + SIZE + " TEXT not null, " + WEIGHT_DATE + " TEXT not null);";

	private WeightDAO() {
	}

	public static WeightDAO getInstance(Context ctx) {
		if (instance == null) {
			instance = new WeightDAO();
		}
		instance.ctx = ctx;
		return instance;
	}

	protected SQLiteDatabase openDB()
	{
		SQLiteDatabase db = super.openDB();
		db.execSQL(CREATE_TABLE_WEIGHT);
		return db;
	}
	
	public boolean insertWeightData(WeightDTO weightData) {
		SQLiteDatabase db = openDB();
		ContentValues values = getContentValuesFromWeightData(weightData);

		boolean ok = db.insert(TABLE_WEIGHT, null, values) > 0;
		closeDB(db);
		return ok;
	}

	private ContentValues getContentValuesFromWeightData(WeightDTO weightData) {
		ContentValues values = new ContentValues();
		values.put(WEIGHT, "" + weightData.getWeight());
		values.put(SIZE, "" + weightData.getSize());
		values.put(WEIGHT_DATE, String.valueOf(weightData.getDate().getTime()));
		return values;
	}
	
	public void deleteWeightDataForCurrentProfile(WeightDTO weightData) {
		SQLiteDatabase db = openDB();
		db.execSQL("DELETE FROM " + TABLE_WEIGHT + " WHERE " + WEIGHT_ID +  "=" + weightData.getId());
		closeDB(db);
	}

	public void updateWeightDataForCurrentProfile(WeightDTO weightData) {
		SQLiteDatabase db = openDB();
		db.update(TABLE_WEIGHT, getContentValuesFromWeightData(weightData), WEIGHT_ID + "=" + weightData.getId(), null);
		closeDB(db);
	}

	public WeightDTO findById(int id) {
		SQLiteDatabase db = openDB();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WEIGHT + " WHERE " + WEIGHT_ID + "=" + id, null);
		WeightDTO weightData = new WeightDTO();
		weightData.setId(cursor.getInt(cursor.getColumnIndex(WEIGHT_ID)));
		weightData.setWeight(Float.parseFloat(cursor.getString(cursor.getColumnIndex(WEIGHT))));
		weightData.setDate(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(WEIGHT_DATE)))));
		weightData.setSize(Float.parseFloat(cursor.getString(cursor.getColumnIndex(SIZE))));
		cursor.close();
		closeDB(db);
		return weightData;
	}
	
	public List<WeightDTO> getAllWeightDataForCurrentProfile() {
		SQLiteDatabase db = openDB();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WEIGHT, null);
		List<WeightDTO> weightDataList = mapCursorToWeightDataList(cursor);
		Collections.sort(weightDataList);
		cursor.close();
		closeDB(db);
		return weightDataList;
	}
	
	public List<WeightDTO> getWeightDataForCurrentProfile(Date date) {
		
		List<WeightDTO> weights = getAllWeightDataForCurrentProfile();
		List<WeightDTO> weightsAfterDate = new ArrayList<WeightDTO>();
		for (WeightDTO weight : weights) {
			if(weight.getDate().after(date))
			{
				weightsAfterDate.add(weight);
			}
		}
		
		return weightsAfterDate;
	}

	public void deleteAllRecordsFromCurrentProfile() {
		SQLiteDatabase db = openDB();
		db.execSQL("DELETE FROM " + TABLE_WEIGHT);
	}

	private List<WeightDTO> mapCursorToWeightDataList(Cursor cursor) {
		List<WeightDTO> weights = new ArrayList<WeightDTO>();
		while (cursor.moveToNext()) {
			WeightDTO weightData = new WeightDTO();
			weightData.setId(cursor.getInt(cursor.getColumnIndex(WEIGHT_ID)));
			weightData.setWeight(Float.parseFloat(cursor.getString(cursor.getColumnIndex(WEIGHT))));
			weightData.setDate(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(WEIGHT_DATE)))));
			weightData.setSize(Float.parseFloat(cursor.getString(cursor.getColumnIndex(SIZE))));
			weights.add(weightData);
		}
		return weights;
	}
}
