package com.example.empapp.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "empapp";  // Database name
    private static final int DATABASE_VERSION = 2;         // Database version

    // Table and column definitions for Managers
    private static final String TABLE_MANAGERS = "Managers";
    private static final String COLUMN_MANAGER_ID = "id";
    private static final String COLUMN_MANAGER_USERNAME = "username";
    private static final String COLUMN_MANAGER_PASSWORD = "password";

    // Table and column definitions for Employees
    public static final String TABLE_EMPLOYEES = "Employees";
    public static final String COLUMN_ID = "id";  // Auto-incrementing primary key
    public static final String COLUMN_EMPLOYEE_ID = "employee_id";  // Employee ID
    public static final String COLUMN_EMPLOYEE_USERNAME = "employee_username";
    public static final String COLUMN_EMPLOYEE_PASSWORD = "employee_password";
    public static final String COLUMN_EMPLOYEE_FIRST_NAME = "first_name";
    public static final String COLUMN_EMPLOYEE_LAST_NAME = "last_name";
    public static final String COLUMN_EMPLOYEE_DESIGNATION = "designation";
    public static final String COLUMN_EMPLOYEE_ADDRESS = "address";
    public static final String COLUMN_EMPLOYEE_DATE_OF_BIRTH = "date_of_birth";
    public static final String COLUMN_EMPLOYEE_DATE_OF_JOINING = "date_of_joining";
    public static final String COLUMN_EMPLOYEE_EMAIL = "email"; // New field
    public static final String COLUMN_EMPLOYEE_PHONE = "phone_number"; // New field

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Managers table
        String createManagersTable = "CREATE TABLE " + TABLE_MANAGERS + " (" +
                COLUMN_MANAGER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MANAGER_USERNAME + " TEXT UNIQUE, " +
                COLUMN_MANAGER_PASSWORD + " TEXT)";
        db.execSQL(createManagersTable);

        // Create Employees table with primary key, unique employee ID, and new columns
        String createEmployeesTable = "CREATE TABLE " + TABLE_EMPLOYEES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMPLOYEE_ID + " INTEGER UNIQUE, " +
                COLUMN_EMPLOYEE_USERNAME + " TEXT UNIQUE, " +
                COLUMN_EMPLOYEE_PASSWORD + " TEXT, " +
                COLUMN_EMPLOYEE_FIRST_NAME + " TEXT, " +
                COLUMN_EMPLOYEE_LAST_NAME + " TEXT, " +
                COLUMN_EMPLOYEE_DESIGNATION + " TEXT, " +
                COLUMN_EMPLOYEE_ADDRESS + " TEXT, " +
                COLUMN_EMPLOYEE_DATE_OF_BIRTH + " TEXT, " +
                COLUMN_EMPLOYEE_DATE_OF_JOINING + " TEXT, " +
                COLUMN_EMPLOYEE_EMAIL + " TEXT, " +
                COLUMN_EMPLOYEE_PHONE + " TEXT)";
        db.execSQL(createEmployeesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add email and phone number columns if upgrading from version 1 to 2
            db.execSQL("ALTER TABLE " + TABLE_EMPLOYEES + " ADD COLUMN " + COLUMN_EMPLOYEE_EMAIL + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_EMPLOYEES + " ADD COLUMN " + COLUMN_EMPLOYEE_PHONE + " TEXT");
        }
    }

    // Validate manager login
    public boolean validateManager(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MANAGERS, null,
                COLUMN_MANAGER_USERNAME + "=? AND " + COLUMN_MANAGER_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        boolean valid = cursor.getCount() > 0;
        cursor.close();
        return valid;
    }

    // Validate employee login using either employee ID or username and password
    public boolean validateEmployee(String employeeId, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        // Check if the input is numeric (Employee ID) or username
        if (isNumeric(employeeId)) {
            cursor = db.query(TABLE_EMPLOYEES, null,
                    COLUMN_EMPLOYEE_ID + "=? AND " + COLUMN_EMPLOYEE_PASSWORD + "=?",
                    new String[]{employeeId, password}, null, null, null);
        } else {
            cursor = db.query(TABLE_EMPLOYEES, null,
                    COLUMN_EMPLOYEE_USERNAME + "=? AND " + COLUMN_EMPLOYEE_PASSWORD + "=?",
                    new String[]{employeeId, password}, null, null, null);
        }

        boolean valid = cursor.getCount() > 0;
        cursor.close();
        return valid;
    }

    // Check if string is numeric
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Add new employee with uniqueness check
    public boolean addEmployee(int employeeId, String username, String password, String designation,
                               String firstName, String lastName, String address,
                               String dateOfBirth, String dateOfJoining,
                               String email, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if employee ID or username already exists
        String query = "SELECT * FROM " + TABLE_EMPLOYEES + " WHERE " + COLUMN_EMPLOYEE_ID + " = ? OR " + COLUMN_EMPLOYEE_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(employeeId), username});

        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // Employee ID or username already exists
        }
        cursor.close();

        // Insert new employee data
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMPLOYEE_ID, employeeId); // Employee ID as integer
        values.put(COLUMN_EMPLOYEE_USERNAME, username);
        values.put(COLUMN_EMPLOYEE_PASSWORD, password);
        values.put(COLUMN_EMPLOYEE_DESIGNATION, designation);
        values.put(COLUMN_EMPLOYEE_FIRST_NAME, firstName);
        values.put(COLUMN_EMPLOYEE_LAST_NAME, lastName);
        values.put(COLUMN_EMPLOYEE_ADDRESS, address);
        values.put(COLUMN_EMPLOYEE_DATE_OF_BIRTH, dateOfBirth);
        values.put(COLUMN_EMPLOYEE_DATE_OF_JOINING, dateOfJoining);
        values.put(COLUMN_EMPLOYEE_EMAIL, email);
        values.put(COLUMN_EMPLOYEE_PHONE, phoneNumber);

        try {
            long result = db.insert(TABLE_EMPLOYEES, null, values);
            return result != -1;
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if employee ID exists
    public boolean isEmployeeIdExists(int employeeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EMPLOYEES, new String[]{COLUMN_EMPLOYEE_ID},
                COLUMN_EMPLOYEE_ID + "=?", new String[]{String.valueOf(employeeId)}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Check if employee username exists
    public boolean isEmployeeUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EMPLOYEES, new String[]{COLUMN_EMPLOYEE_ID},
                COLUMN_EMPLOYEE_USERNAME + "=?", new String[]{username}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Retrieve employee data by ID
    public Cursor getEmployeeById(int employeeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_EMPLOYEES, null, COLUMN_EMPLOYEE_ID + "=?",
                new String[]{String.valueOf(employeeId)}, null, null, null);
    }

    // Retrieve employee details by ID as a string
    public String getEmployeeDetailsById(int employeeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EMPLOYEES, null, COLUMN_EMPLOYEE_ID + "=?",
                new String[]{String.valueOf(employeeId)}, null, null, null);

        String employeeDetails = "Not Found";

        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_LAST_NAME));
            String username = cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_USERNAME));
            String designation = cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_DESIGNATION));
            String address = cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_ADDRESS));
            String dateOfBirth = cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_DATE_OF_BIRTH));
            String dateOfJoining = cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_DATE_OF_JOINING));

            employeeDetails = "Name: " + firstName + " " + lastName + "\n" +
                    "Username: " + username + "\n" +
                    "Designation: " + designation + "\n" +
                    "Address: " + address + "\n" +
                    "Date of Birth: " + dateOfBirth + "\n" +
                    "Date of Joining: " + dateOfJoining;

            cursor.close();
        }
        db.close();
        return employeeDetails;
    }

    // Update employee details
    public int updateEmployee(int employeeId, String username, String password, String firstName, String lastName,
                              String designation, String address, String dateOfBirth, String dateOfJoining, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMPLOYEE_USERNAME, username);
        values.put(COLUMN_EMPLOYEE_PASSWORD, password);
        values.put(COLUMN_EMPLOYEE_FIRST_NAME, firstName);
        values.put(COLUMN_EMPLOYEE_LAST_NAME, lastName);
        values.put(COLUMN_EMPLOYEE_DESIGNATION, designation);
        values.put(COLUMN_EMPLOYEE_ADDRESS, address);
        values.put(COLUMN_EMPLOYEE_DATE_OF_BIRTH, dateOfBirth);
        values.put(COLUMN_EMPLOYEE_DATE_OF_JOINING, dateOfJoining);
        values.put(COLUMN_EMPLOYEE_EMAIL, email);
        values.put(COLUMN_EMPLOYEE_PHONE, phone);

        return db.update(TABLE_EMPLOYEES, values, COLUMN_EMPLOYEE_ID + "=?",
                new String[]{String.valueOf(employeeId)});
    }

    public String getEmployeeNameById(int employeeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EMPLOYEES,
                new String[]{COLUMN_EMPLOYEE_FIRST_NAME, COLUMN_EMPLOYEE_LAST_NAME},
                COLUMN_EMPLOYEE_ID + " = ?",
                new String[]{String.valueOf(employeeId)},
                null, null, null);

        String employeeName = null;

        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_LAST_NAME));
            employeeName = firstName + " " + lastName;
            cursor.close();
        }
        db.close();
        return employeeName;
    }

    public boolean addManager(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if manager already exists
        Cursor cursor = db.query(TABLE_MANAGERS, null,
                COLUMN_MANAGER_USERNAME + "=?", new String[]{username},
                null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // Manager already exists
        }
        cursor.close();

        // Insert new manager
        ContentValues values = new ContentValues();
        values.put(COLUMN_MANAGER_USERNAME, username);
        values.put(COLUMN_MANAGER_PASSWORD, password);
        long result = db.insert(TABLE_MANAGERS, null, values);
        db.close();
        return result != -1;
    }

    public int getEmployeeCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_EMPLOYEES;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public boolean deleteEmployee(int employeeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Attempt to delete the employee
        int rowsAffected = db.delete(TABLE_EMPLOYEES, COLUMN_EMPLOYEE_ID + "=?", new String[]{String.valueOf(employeeId)});
        db.close();
        // Return true if one or more rows were affected (deleted), false otherwise
        return rowsAffected > 0;
    }

    public Cursor getAllEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_EMPLOYEES, null, null, null, null, null, null);
    }

    public boolean updateEmployee(int employeeId, String newName, String newUsername, String newDesignation, String newAddress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMPLOYEE_FIRST_NAME, newName);
        values.put(COLUMN_EMPLOYEE_USERNAME, newUsername);
        values.put(COLUMN_EMPLOYEE_DESIGNATION, newDesignation);
        values.put(COLUMN_EMPLOYEE_ADDRESS, newAddress);

        int rowsAffected = db.update(TABLE_EMPLOYEES, values, COLUMN_EMPLOYEE_ID + " = ?", new String[]{String.valueOf(employeeId)});
        db.close();
        return rowsAffected > 0;
    }
    public String getEmployeePhoneById(int employeeId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String phoneNumber = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_EMPLOYEES,
                    new String[]{COLUMN_EMPLOYEE_PHONE},
                    COLUMN_EMPLOYEE_ID + " = ?",
                    new String[]{String.valueOf(employeeId)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_PHONE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return phoneNumber;
    }
    public int updateEmployeePassword(int employeeId, String currentPassword, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Verify the current password first
        Cursor cursor = db.query(TABLE_EMPLOYEES, null, COLUMN_EMPLOYEE_ID + "=? AND " + COLUMN_EMPLOYEE_PASSWORD + "=?",
                new String[]{String.valueOf(employeeId), currentPassword}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Current password is correct, proceed to update the password
            values.put(COLUMN_EMPLOYEE_PASSWORD, newPassword);
            int rowsAffected = db.update(TABLE_EMPLOYEES, values, COLUMN_EMPLOYEE_ID + "=?",
                    new String[]{String.valueOf(employeeId)});
            cursor.close();
            db.close();
            return rowsAffected;
        } else {
            // Current password validation failed
            if (cursor != null) cursor.close();
            db.close();
            return -1;  // Indicate failure (password incorrect)
        }
    }




}
