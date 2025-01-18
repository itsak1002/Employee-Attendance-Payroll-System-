package com.example.empapp.Activity.Employee;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.DatabaseHelper.AttendanceDbHelper;
import com.example.empapp.R;

public class InsertSampleAttendanceActivity extends AppCompatActivity {

    private Button insertAttendanceButton;
    private AttendanceDbHelper attendanceDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_sample_attendance);

        insertAttendanceButton = findViewById(R.id.btn_insert_attendance);
        attendanceDbHelper = new AttendanceDbHelper(this);

        insertAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get employeeId from SharedPreferences (session)
                int employeeId = getEmployeeIdFromSession();
                if (employeeId != -1) {
                    insertSampleAttendanceForEmployee(employeeId);
                } else {
                    Toast.makeText(InsertSampleAttendanceActivity.this, "Employee not logged in.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Function to get the employee ID from SharedPreferences (session)
    private int getEmployeeIdFromSession() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        return sharedPreferences.getInt("employeeId", -1); // Default value is -1 (if employee ID is not found)
    }

    // Function to insert sample attendance for an employee
    private void insertSampleAttendanceForEmployee(int employeeId) {
        // Insert the sample attendance
        attendanceDbHelper.insertSampleAttendanceForEmployee(employeeId);

        // Show a toast message after inserting
        Toast.makeText(InsertSampleAttendanceActivity.this, "Sample Attendance Inserted for Employee ID: " + employeeId, Toast.LENGTH_SHORT).show();
    }
}
