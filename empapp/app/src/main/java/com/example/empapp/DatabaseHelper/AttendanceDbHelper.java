package com.example.empapp.DatabaseHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.empapp.model.AttendRecord;
import com.example.empapp.model.AttendanceRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "empapp.db"; // Changed to public
    public static final int DATABASE_VERSION = 1; // Changed to public

    public static final String TABLE_ATTENDANCE = "attendance"; // Changed to public

    // Columns of the Attendance Table
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CHECK_IN_TIME = "check_in_time";
    public static final String COLUMN_CHECK_OUT_TIME = "check_out_time";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_LEAVE_STATUS = "leave_status";
    public static final String COLUMN_LEAVE_REASON = "leave_reason";
    public static final String COLUMN_WORKING_HOURS = "working_hours";

    public AttendanceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ATTENDANCE_TABLE = "CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_EMPLOYEE_ID + " INTEGER NOT NULL, " +
                COLUMN_DATE + " DATE NOT NULL, " +
                COLUMN_CHECK_IN_TIME + " TIME, " +
                COLUMN_CHECK_OUT_TIME + " TIME, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_LEAVE_STATUS + " TEXT, " +
                COLUMN_LEAVE_REASON + " TEXT, " +
                COLUMN_WORKING_HOURS + " TIME);";
        db.execSQL(CREATE_ATTENDANCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        onCreate(db);
    }

    // Insert Attendance Record
    public long insertAttendance(int employeeId, String date, String checkInTime, String checkOutTime, String status, String leaveStatus, String leaveReason, String workingHours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMPLOYEE_ID, employeeId);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_CHECK_IN_TIME, checkInTime);
        values.put(COLUMN_CHECK_OUT_TIME, checkOutTime);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_LEAVE_STATUS, leaveStatus);
        values.put(COLUMN_LEAVE_REASON, leaveReason);
        values.put(COLUMN_WORKING_HOURS, workingHours);

        long result = db.insert(TABLE_ATTENDANCE, null, values);
        db.close();
        return result;
    }

    // Check if Attendance is already marked for the employee on a given date
    public boolean isAttendanceMarked(int employeeId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTENDANCE, null,
                COLUMN_EMPLOYEE_ID + " = ? AND " + COLUMN_DATE + " = ?",
                new String[]{String.valueOf(employeeId), date}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    // Check if Employee has already checked-in
    public boolean hasCheckedIn(int employeeId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTENDANCE, new String[]{COLUMN_CHECK_IN_TIME},
                COLUMN_EMPLOYEE_ID + " = ? AND " + COLUMN_DATE + " = ? AND " + COLUMN_CHECK_IN_TIME + " IS NOT NULL",
                new String[]{String.valueOf(employeeId), date}, null, null, null);
        boolean hasCheckedIn = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return hasCheckedIn;
    }

    // Calculate Working Hours
    public String calculateWorkingHours(String checkInTime, String checkOutTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            Date checkIn = sdf.parse(checkInTime);
            Date checkOut = sdf.parse(checkOutTime);

            if (checkIn != null && checkOut != null) {
                long diffInMillis = checkOut.getTime() - checkIn.getTime();
                long diffInHours = diffInMillis / (1000 * 60 * 60);
                long diffInMinutes = (diffInMillis % (1000 * 60 * 60)) / (1000 * 60);

                return String.format(Locale.getDefault(), "%02d:%02d", diffInHours, diffInMinutes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "00:00"; // Default if there's an error in calculation
    }

    // Update Check-Out Time and Working Hours
    public int updateCheckOut(int employeeId, String date, String checkOutTime, String workingHours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHECK_OUT_TIME, checkOutTime);
        values.put(COLUMN_WORKING_HOURS, workingHours); // Save working hours

        int rowsUpdated = db.update(TABLE_ATTENDANCE, values,
                COLUMN_EMPLOYEE_ID + " = ? AND " + COLUMN_DATE + " = ?",
                new String[]{String.valueOf(employeeId), date});
        db.close();
        return rowsUpdated;
    }

    // Get all Attendance records
    public Cursor getAllAttendance() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ATTENDANCE, null, null, null, null, null, null);
    }

    // Get Attendance by Employee ID
    public Cursor getAttendanceByEmployeeId(int employeeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ATTENDANCE, null,
                COLUMN_EMPLOYEE_ID + " = ?", new String[]{String.valueOf(employeeId)},
                null, null, null);
    }

    // Delete Attendance record by ID
    public int deleteAttendance(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_ATTENDANCE, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }

    // Get Check-In Time for a specific Employee and Date
    public String getCheckInTime(int employeeId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTENDANCE, new String[]{COLUMN_CHECK_IN_TIME},
                COLUMN_EMPLOYEE_ID + " = ? AND " + COLUMN_DATE + " = ?",
                new String[]{String.valueOf(employeeId), date}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String checkInTime = cursor.getString(cursor.getColumnIndex(COLUMN_CHECK_IN_TIME));
            cursor.close();
            db.close();
            return checkInTime;
        }
        cursor.close();
        db.close();
        return null;
    }

    // Get Check-Out Time for a specific Employee and Date
    public String getCheckOutTime(int employeeId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTENDANCE, new String[]{COLUMN_CHECK_OUT_TIME},
                COLUMN_EMPLOYEE_ID + " = ? AND " + COLUMN_DATE + " = ?",
                new String[]{String.valueOf(employeeId), date}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String checkOutTime = cursor.getString(cursor.getColumnIndex(COLUMN_CHECK_OUT_TIME));
            cursor.close();
            db.close();
            return checkOutTime;
        }
        cursor.close();
        db.close();
        return null;
    }

    // Get Attendance Status for a specific Employee and Date
    public String getAttendanceStatus(int employeeId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTENDANCE, new String[]{COLUMN_STATUS},
                COLUMN_EMPLOYEE_ID + " = ? AND " + COLUMN_DATE + " = ?",
                new String[]{String.valueOf(employeeId), date}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS));
            cursor.close();
            db.close();
            return status != null ? status : "Absent"; // Default to "Absent" if no status is found
        }
        cursor.close();
        db.close();
        return "Absent";
    }

    // Get Attendance for Last 7 Days for an Employee
    public List<AttendanceRecord> getAttendanceForLast7Days(int employeeId) {
        List<AttendanceRecord> attendanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_ATTENDANCE +
                " WHERE " + COLUMN_EMPLOYEE_ID + " = ? AND " +
                COLUMN_DATE + " >= DATE('now', '-7 days') ORDER BY " + COLUMN_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(employeeId)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                AttendanceRecord record = new AttendanceRecord(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CHECK_IN_TIME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CHECK_OUT_TIME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LEAVE_STATUS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LEAVE_REASON)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_WORKING_HOURS))  // Include working hours
                );
                attendanceList.add(record);
            }
            cursor.close();
        }
        db.close();
        return attendanceList;
    }

    // Get Attendance for a Specific Day for an Employee
    @SuppressLint("Range")
    public List<AttendanceRecord> getAttendanceForSpecificDay(int employeeId, String date) {
        List<AttendanceRecord> attendanceRecords = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the query to fetch attendance for a specific employee on a given date
        String selectQuery = "SELECT * FROM " + TABLE_ATTENDANCE + " WHERE "
                + COLUMN_EMPLOYEE_ID + " = ? AND " + COLUMN_DATE + " = ?";

        try (Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(employeeId), date})) {
            // Iterate through the results and add each record to the list
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                String timeIn = cursor.getString(cursor.getColumnIndex(COLUMN_CHECK_IN_TIME));
                String timeOut = cursor.getString(cursor.getColumnIndex(COLUMN_CHECK_OUT_TIME));
                String status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS));
                String leaveStatus = cursor.getString(cursor.getColumnIndex(COLUMN_LEAVE_STATUS));
                String leaveReason = cursor.getString(cursor.getColumnIndex(COLUMN_LEAVE_REASON));
                String workingHours = cursor.getString(cursor.getColumnIndex(COLUMN_WORKING_HOURS));

                // Create an AttendanceRecord object for the current row
                AttendanceRecord record = new AttendanceRecord(id, date, timeIn, timeOut, status, leaveStatus, leaveReason, workingHours);
                attendanceRecords.add(record);
            }
        } catch (SQLException e) {
            // Handle database error
            e.printStackTrace();
        } finally {
            db.close();  // Close the database connection
        }

        return attendanceRecords;
    }

    @SuppressLint("Range")
    public List<AttendRecord> getAttendanceForSpecificDayy(String date) {
        List<AttendRecord> attendanceRecords = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the query to fetch attendance on a specific date
        String selectQuery = "SELECT " + COLUMN_EMPLOYEE_ID + ", " + COLUMN_CHECK_IN_TIME + ", " + COLUMN_CHECK_OUT_TIME
                + " FROM " + TABLE_ATTENDANCE + " WHERE " + COLUMN_DATE + " = ?";

        try (Cursor cursor = db.rawQuery(selectQuery, new String[]{date})) {
            // Iterate through the results and add each record to the list
            while (cursor.moveToNext()) {
                int employeeId = cursor.getInt(cursor.getColumnIndex(COLUMN_EMPLOYEE_ID));
                String timeIn = cursor.getString(cursor.getColumnIndex(COLUMN_CHECK_IN_TIME));
                String timeOut = cursor.getString(cursor.getColumnIndex(COLUMN_CHECK_OUT_TIME));

                // Create an AttendRecord object with employee ID, IN, and OUT times
                AttendRecord record = new AttendRecord(employeeId, date, timeIn, timeOut);
                attendanceRecords.add(record);
            }
        } catch (SQLException e) {
            // Handle database error
            e.printStackTrace();
        } finally {
            db.close();  // Close the database connection
        }

        return attendanceRecords;
    }


    // Count today's present employees
    public int countTodaysPresentEmployees() {
        int presentCount = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String query = "SELECT COUNT(*) FROM " + TABLE_ATTENDANCE +
                " WHERE " + COLUMN_DATE + " = ? AND " + COLUMN_STATUS + " = ?";
        try (Cursor cursor = db.rawQuery(query, new String[]{todayDate, "Present"})) {
            if (cursor != null && cursor.moveToFirst()) {
                presentCount = cursor.getInt(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return presentCount;
    }
    public void insertSampleAttendanceForEmployee(int employeeId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Sample attendance for December 2024
        String[] dates = {
                "2024-12-01", "2024-12-02", "2024-12-03", "2024-12-04", "2024-12-05", "2024-12-06", "2024-12-07",
                "2024-12-08", "2024-12-09", "2024-12-10", "2024-12-11", "2024-12-12", "2024-12-13", "2024-12-14",
                "2024-12-15", "2024-12-16", "2024-12-17", "2024-12-18", "2024-12-19", "2024-12-20", "2024-12-21",
                "2024-12-22", "2024-12-23", "2024-12-24", "2024-12-25", "2024-12-26", "2024-12-27", "2024-12-28",
                "2024-12-29", "2024-12-30", "2024-12-31"
        };

        // Sample Check-In and Check-Out Times for each day
        String checkInTime = "09:00:00";
        String checkOutTime = "18:00:00";
        String status = "Present";
        String leaveStatus = "None";
        String leaveReason = "";
        String workingHours = "09:00:00";  // 9 hours per day

        // Insert Attendance records for each day in December
        for (String date : dates) {
            if (!isAttendanceMarked(employeeId, date)) {
                insertAttendance(employeeId, date, checkInTime, checkOutTime, status, leaveStatus, leaveReason, workingHours);
            }
        }

        db.close();
    }
    public Cursor getAttendanceForSpecificDayForReport(String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ATTENDANCE +
                " WHERE " + COLUMN_DATE + " BETWEEN ? AND ?";
        return db.rawQuery(query, new String[]{startDate, endDate});
    }
    public Cursor getAttendanceForSpecificDayWithIdForReport(int employeeId, String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ATTENDANCE +
                " WHERE " + COLUMN_EMPLOYEE_ID + " = ? AND " +
                COLUMN_DATE + " BETWEEN ? AND ?";
        return db.rawQuery(query, new String[]{String.valueOf(employeeId), startDate, endDate});
    }

}
