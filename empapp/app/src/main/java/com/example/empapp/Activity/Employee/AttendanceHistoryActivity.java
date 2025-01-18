package com.example.empapp.Activity.Employee;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.empapp.DatabaseHelper.AttendanceDbHelper;
import com.example.empapp.Adapter.AttendanceAdapter;
import com.example.empapp.R;
import com.example.empapp.model.AttendanceRecord;

import java.util.List;

public class AttendanceHistoryActivity extends Activity {

    private ListView attendanceListView;
    private AttendanceDbHelper dbHelper;
    private AttendanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

        // Retrieve employee ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int employeeId = sharedPreferences.getInt("employeeId", -1);

        // Check if employee ID is valid
        if (employeeId == -1) {
            // Handle the case when employee ID is not found (e.g., show a message or redirect to login)
            Toast.makeText(this, "Please login first.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AttendanceHistoryActivity.this, EmployeeLogin.class));
            finish(); // Exit the activity
            return;
        }

        // Initialize views
        attendanceListView = findViewById(R.id.attendance_list_view);
        dbHelper = new AttendanceDbHelper(this);

        // Retrieve attendance records for the employee (use employeeId from SharedPreferences)
        List<AttendanceRecord> attendanceList = dbHelper.getAttendanceForLast7Days(employeeId);

        // Set up the adapter to display the attendance records
        adapter = new AttendanceAdapter(this, attendanceList);
        attendanceListView.setAdapter(adapter);
    }
}
