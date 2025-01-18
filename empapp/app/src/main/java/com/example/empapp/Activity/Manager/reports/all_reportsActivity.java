package com.example.empapp.Activity.Manager.reports;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.R;

public class all_reportsActivity extends AppCompatActivity {

    private Button btnAllEmployeeReport;
    private Button btnAttendanceReport;
    private Button btnOfficeLocationReport;

    private final ActivityResultLauncher<Intent> createPdfLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri fileUri = result.getData().getData();
                    if (fileUri != null) {
                        employee_report reportGenerator = new employee_report(this);
                        reportGenerator.generateEmployeeReport(fileUri);
                        Toast.makeText(this, "Employee report saved successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to save report. Invalid file URI.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Operation canceled", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reports);

        // Initialize buttons
        btnAllEmployeeReport = findViewById(R.id.btn_all_employee_report);
        btnAttendanceReport = findViewById(R.id.btn_date_wise_attendance_report);
        btnOfficeLocationReport = findViewById(R.id.btn_office_location_report);

        // Set button click listeners
        btnAllEmployeeReport.setOnClickListener(v -> createEmployeeReport());
        btnAttendanceReport.setOnClickListener(v -> {
            Toast.makeText(this, "Generating Attendance Report", Toast.LENGTH_SHORT).show();
            // Intent to launch DateWiseAttendanceReport
            Intent intent = new Intent(this, DateWiseReportActivity.class);
            startActivity(intent);
        });

        btnOfficeLocationReport.setOnClickListener(v -> {
            Toast.makeText(this, "Generating Office Location Report", Toast.LENGTH_SHORT).show();
            generateOfficeLocationReport();
        });

    }

    private void createEmployeeReport() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "Employee_Report.pdf");
        createPdfLauncher.launch(intent);
    }

    private void generateOfficeLocationReport() {
        OfficeLocationReport officeLocationReport = new OfficeLocationReport(this);
        officeLocationReport.generateOfficeLocationReport();
        Toast.makeText(this, "Office Location report saved successfully", Toast.LENGTH_SHORT).show();
    }
}
