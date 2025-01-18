package com.example.empapp.Activity.Employee.Employee;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.DatabaseHelper.DBHelper;
import com.example.empapp.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView empName, empId, empUsername, empDesignation, empAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize TextViews
        empName = findViewById(R.id.empName);
        empId = findViewById(R.id.empId);
        empUsername = findViewById(R.id.empUsername);
        empDesignation = findViewById(R.id.empDesignation);
        empAddress = findViewById(R.id.empAddress);

        // Retrieve the logged-in employee ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int loggedInEmployeeId = sharedPreferences.getInt("employeeId", -1);

        if (loggedInEmployeeId != -1) {
            // Retrieve employee data from the database
            DBHelper dbHelper = new DBHelper(this);

            try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
                Cursor cursor = dbHelper.getEmployeeById(loggedInEmployeeId);

                if (cursor != null && cursor.moveToFirst()) {
                    // Get employee data from the cursor
                    String firstName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMPLOYEE_FIRST_NAME));
                    String lastName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMPLOYEE_LAST_NAME));                    String employeeUsername = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMPLOYEE_USERNAME));
                    String employeeDesignation = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMPLOYEE_DESIGNATION));
                    String employeeAddress = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMPLOYEE_ADDRESS));

                    // Set the employee data to the TextViews
                    empName.setText("Employee Name: " + firstName +" "+ lastName);
                    empId.setText("Employee ID: " + loggedInEmployeeId);
                    empUsername.setText("Username: " + employeeUsername);
                    empDesignation.setText("Designation: " + employeeDesignation);
                    empAddress.setText("Address: " + employeeAddress);
                }

                if (cursor != null) {
                    cursor.close(); // Close cursor to avoid memory leaks
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Handle case where no session exists (redirect to login or show error)
            empName.setText("No profile data available. Please log in.");
        }
    }
}
