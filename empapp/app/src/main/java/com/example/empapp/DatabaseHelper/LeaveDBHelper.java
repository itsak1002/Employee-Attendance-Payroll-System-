package com.example.empapp.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.empapp.model.LeaveRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LeaveDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LeaveDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_LEAVE = "leave_requests";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMPLOYEE_ID = "employee_id";
    private static final String COLUMN_LEAVE_DATE = "leave_date";
    private static final String COLUMN_LEAVE_REASON = "leave_reason";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_REQUESTED_ON = "requested_on"; // New column added

    public LeaveDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_LEAVE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMPLOYEE_ID + " TEXT, " +
                COLUMN_LEAVE_DATE + " TEXT, " +
                COLUMN_LEAVE_REASON + " TEXT, " +
                COLUMN_STATUS + " TEXT DEFAULT 'Pending', " +
                COLUMN_REQUESTED_ON + " TEXT" +  // New column for timestamp
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAVE);
        onCreate(db);
    }

    // Add leave request with current date and time in requested_on
    public boolean addLeaveRequest(String employeeId, String leaveDate, String leaveReason) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMPLOYEE_ID, employeeId);
        values.put(COLUMN_LEAVE_DATE, leaveDate);  // Leave Date formatted in dd-MM-yyyy
        values.put(COLUMN_LEAVE_REASON, leaveReason);
        values.put(COLUMN_REQUESTED_ON, getCurrentDateTime()); // Automatically set the requested_on field
        long result = db.insert(TABLE_LEAVE, null, values);
        return result != -1;
    }

    // Fetch leave requests with formatted dates
    public Cursor getLeaveRequests() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_LEAVE, null);
    }

    // Fetch leave requests by employee id
    public List<LeaveRequest> getLeaveRequestsByEmployeeId(int employeeId) {
        List<LeaveRequest> requests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM leave_requests WHERE employee_id = ?", new String[]{String.valueOf(employeeId)});
        if (cursor.moveToFirst()) {
            do {
                String leaveDate = cursor.getString(2);  // Get the leave_date field
                String requestedOn = cursor.getString(5);  // Get the requested_on field

                String formattedLeaveDate = formatDate(leaveDate);  // Format leave_date to dd-MM-yyyy
                String formattedRequestedOn = formatDate(requestedOn);  // Format requested_on to dd-MM-yyyy

                requests.add(new LeaveRequest(
                        cursor.getInt(0), // id
                        cursor.getInt(1), // employee_id
                        formattedLeaveDate, // formatted leave_date
                        cursor.getString(3), // leave_reason
                        cursor.getString(4),  // status
                        formattedRequestedOn  // formatted requested_on
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return requests;
    }

    private String formatDate(String rawDate) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = originalFormat.parse(rawDate);
            return targetFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return rawDate;  // Return the original date if parsing fails
        }
    }

    // Update leave request status
    public boolean updateLeaveStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        int result = db.update(TABLE_LEAVE, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Delete all records from leave_requests table
    public boolean deleteAllLeaveRequests() {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_LEAVE, null, null);
        return rowsDeleted > 0;
    }

    // Helper method to get current date and time
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public Cursor getLeaveRequestsByStatus(String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_LEAVE + " WHERE " + COLUMN_STATUS + "=?", new String[]{status});
    }

    public void deleteLeaveRequest(int requestId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LEAVE, "id = ?", new String[]{String.valueOf(requestId)});
        db.close();
    }
}
