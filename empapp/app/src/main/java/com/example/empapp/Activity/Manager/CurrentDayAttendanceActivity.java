package com.example.empapp.Activity.Manager;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.DatabaseHelper.AttendanceDbHelper;
import com.example.empapp.Adapter.AttendanceAdapter2;
import com.example.empapp.R;
import com.example.empapp.model.AttendRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CurrentDayAttendanceActivity extends AppCompatActivity {

    private AttendanceDbHelper dbHelper;
    private ListView currentDayAttendanceListView;
    private AttendanceAdapter2 attendanceAdapter;
    private TextView currentDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_day_attendance);

        dbHelper = new AttendanceDbHelper(this);
        currentDayAttendanceListView = findViewById(R.id.currentDayAttendanceListView);
        currentDateTextView = findViewById(R.id.currentDate);

        // Display the current date
        displayCurrentDate();

        // Fetch and display today's attendance records
        displayCurrentDayAttendance();
    }

    private void displayCurrentDate() {
        // Fetch today's date in yyyy-MM-dd format
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        currentDateTextView.setText(todayDate);
    }

    private void displayCurrentDayAttendance() {
        // Fetch today's date in yyyy-MM-dd format
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        List<AttendRecord> records = dbHelper.getAttendanceForSpecificDayy(todayDate);

        if (records != null && !records.isEmpty()) {
            attendanceAdapter = new AttendanceAdapter2(this, records);
            currentDayAttendanceListView.setAdapter(attendanceAdapter);
        } else {
            // Optionally, handle when no attendance records are found
            // For example: show a message or a placeholder UI element
        }
    }
}
