package com.example.empapp.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PaymentDbHelper extends SQLiteOpenHelper {

    // Database Name and Version
    public static final String DATABASE_NAME = "PaymentDB";
    public static final int DATABASE_VERSION = 1;

    // Table Name
    public static final String TABLE_PAYMENT = "payment";

    // Column Names
    public static final String COLUMN_EMPLOYEE_ID = "employee_id"; // Primary Key
    public static final String COLUMN_BANK_NAME = "bank_name";
    public static final String COLUMN_ACCOUNT_NO = "account_no";
    public static final String COLUMN_IFSC_CODE = "ifsc_code";
    public static final String COLUMN_BRANCH_NAME = "branch_name";
    public static final String COLUMN_PF_NUMBER = "pf_number";
    public static final String COLUMN_PAYMENT_PER_HOUR = "payment_per_hour";
    public static final String COLUMN_PERCENT_DA = "percent_da";
    public static final String COLUMN_PERCENT_HRA = "percent_hra";
    public static final String COLUMN_PERCENT_PF = "percent_pf";

    public PaymentDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_PAYMENT + "("
                + COLUMN_EMPLOYEE_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_BANK_NAME + " TEXT, "
                + COLUMN_ACCOUNT_NO + " TEXT, "
                + COLUMN_IFSC_CODE + " TEXT, "
                + COLUMN_BRANCH_NAME + " TEXT, "
                + COLUMN_PF_NUMBER + " TEXT, "
                + COLUMN_PAYMENT_PER_HOUR + " REAL, "
                + COLUMN_PERCENT_DA + " REAL, "
                + COLUMN_PERCENT_HRA + " REAL, "
                + COLUMN_PERCENT_PF + " REAL)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT);
        onCreate(db);
    }

    // Insert a new record
    // Insert a new record
    public long insertPayment(long employeeId, String bankName, String accountNo, String ifscCode, String branchName, String pfNumber,
                              double paymentPerHour, double percentDA, double percentHRA, double percentPF) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMPLOYEE_ID, employeeId); // Add employee_id
        values.put(COLUMN_BANK_NAME, bankName);
        values.put(COLUMN_ACCOUNT_NO, accountNo);
        values.put(COLUMN_IFSC_CODE, ifscCode);
        values.put(COLUMN_BRANCH_NAME, branchName);
        values.put(COLUMN_PF_NUMBER, pfNumber);
        values.put(COLUMN_PAYMENT_PER_HOUR, paymentPerHour);
        values.put(COLUMN_PERCENT_DA, percentDA);
        values.put(COLUMN_PERCENT_HRA, percentHRA);
        values.put(COLUMN_PERCENT_PF, percentPF);

        return db.insert(TABLE_PAYMENT, null, values);
    }


    // Update a record
    public int updatePayment(long employeeId, String bankName, String accountNo, String ifscCode, String branchName, String pfNumber,
                             double paymentPerHour, double percentDA, double percentHRA, double percentPF) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMPLOYEE_ID, employeeId); // Add employee_id
        values.put(COLUMN_BANK_NAME, bankName);
        values.put(COLUMN_ACCOUNT_NO, accountNo);
        values.put(COLUMN_IFSC_CODE, ifscCode);
        values.put(COLUMN_BRANCH_NAME, branchName);
        values.put(COLUMN_PF_NUMBER, pfNumber);
        values.put(COLUMN_PAYMENT_PER_HOUR, paymentPerHour);
        values.put(COLUMN_PERCENT_DA, percentDA);
        values.put(COLUMN_PERCENT_HRA, percentHRA);
        values.put(COLUMN_PERCENT_PF, percentPF);

        // Use the ID to update the specific record
        return db.update(TABLE_PAYMENT, values, COLUMN_EMPLOYEE_ID + " = ?", new String[]{String.valueOf(employeeId)});
    }

    // Delete a record
    public int deletePayment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PAYMENT, COLUMN_EMPLOYEE_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Fetch all records
    public Cursor getAllPayments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PAYMENT, null);
    }

    // Fetch a single record by ID
    // Fetch a single record by employee_id
    public Cursor getPaymentByEmployeeId(int employeeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PAYMENT + " WHERE " + COLUMN_EMPLOYEE_ID + " = ?", new String[]{String.valueOf(employeeId)});
    }

    public Cursor getAllPaymentsByEmployeeId(int employeeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PAYMENT + " WHERE " + COLUMN_EMPLOYEE_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(employeeId)});
    }
    public Cursor getAllPaymentPerHour() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COLUMN_PAYMENT_PER_HOUR + " FROM " + TABLE_PAYMENT, null);
    }
    public Cursor getPaymentPerHourByEmployeeId(int employeeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COLUMN_PAYMENT_PER_HOUR + " FROM " + TABLE_PAYMENT + " WHERE " + COLUMN_EMPLOYEE_ID + " = ?", new String[]{String.valueOf(employeeId)});
    }


}