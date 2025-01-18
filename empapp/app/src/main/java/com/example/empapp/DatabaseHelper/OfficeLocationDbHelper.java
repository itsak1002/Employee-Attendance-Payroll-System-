package com.example.empapp.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OfficeLocationDbHelper extends SQLiteOpenHelper {

    // Table and column names for the office location table
    public static final String TABLE_OFFICE_LOCATION = "office_location";
    public static final String COLUMN_OFFICE_ID = "office_id";
    public static final String COLUMN_OFFICE_LAT = "office_latitude";
    public static final String COLUMN_OFFICE_LNG = "office_longitude";
    public static final String COLUMN_RADIUS = "radius";
    public static final String COLUMN_ID = "id";

    private static final String CREATE_OFFICE_LOCATION_TABLE = "CREATE TABLE " + TABLE_OFFICE_LOCATION + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_OFFICE_ID + " INTEGER, "
            + COLUMN_OFFICE_LAT + " REAL, "
            + COLUMN_OFFICE_LNG + " REAL, "
            + COLUMN_RADIUS + " REAL);";

    private static final String DROP_OFFICE_LOCATION_TABLE = "DROP TABLE IF EXISTS " + TABLE_OFFICE_LOCATION;

    public OfficeLocationDbHelper(Context context) {
        super(context, "officeLocationDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_OFFICE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_OFFICE_LOCATION_TABLE);
        onCreate(db);
    }

    // Insert office location details into the database
    public long insertOfficeLocationDetails(int officeId, double officeLat, double officeLng, float radius) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OFFICE_ID, officeId);
        values.put(COLUMN_OFFICE_LAT, officeLat);
        values.put(COLUMN_OFFICE_LNG, officeLng);
        values.put(COLUMN_RADIUS, radius);

        long result = db.insert(TABLE_OFFICE_LOCATION, null, values);
        db.close();
        return result;
    }

    // Update office location details by officeId
    public int updateOfficeLocationDetails(int officeId, double officeLat, double officeLng, float radius) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OFFICE_LAT, officeLat);
        values.put(COLUMN_OFFICE_LNG, officeLng);
        values.put(COLUMN_RADIUS, radius);

        String whereClause = COLUMN_OFFICE_ID + "=?";
        String[] whereArgs = {String.valueOf(officeId)};

        int result = db.update(TABLE_OFFICE_LOCATION, values, whereClause, whereArgs);
        db.close();
        return result;
    }

    // Delete office location details by officeId
    public void deleteOfficeLocationDetails(int officeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_OFFICE_ID + "=?";
        String[] whereArgs = {String.valueOf(officeId)};
        db.delete(TABLE_OFFICE_LOCATION, whereClause, whereArgs);
        db.close();
    }

    // Get office location details by officeId
    public Cursor getOfficeLocationDetails(int officeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_OFFICE_LOCATION + " WHERE " + COLUMN_OFFICE_ID + "=?";
        String[] selectionArgs = {String.valueOf(officeId)};
        return db.rawQuery(query, selectionArgs);
    }

    // Display all office location details
    public Cursor getAllOfficeLocations() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_OFFICE_LOCATION;
        return db.rawQuery(query, null);
    }
}

