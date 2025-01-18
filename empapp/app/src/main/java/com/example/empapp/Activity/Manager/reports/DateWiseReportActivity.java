package com.example.empapp.Activity.Manager.reports;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.DatabaseHelper.AttendanceDbHelper;
import com.example.empapp.R;

import java.util.Calendar;

public class DateWiseReportActivity extends AppCompatActivity {

    private EditText editStartDate, editEndDate, editEmployeeId;
    private Button generateReportButton;
    private AttendanceDbHelper dbHelper;

    private String startDate;
    private String endDate;
    private Integer employeeId;

    private final ActivityResultLauncher<Intent> saveFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri fileUri = result.getData().getData(); // Retrieve the selected file's URI
                    if (fileUri != null) {
                        // Generate the report
                        new DateWiseAttendanceReport(this)
                                .generateReport(startDate, endDate, employeeId, fileUri);
                        Toast.makeText(this, "Report saved successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Invalid file URI", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Operation canceled", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_input);

        editStartDate = findViewById(R.id.edit_start_date);
        editEndDate = findViewById(R.id.edit_end_date);
        editEmployeeId = findViewById(R.id.edit_employee_id);
        generateReportButton = findViewById(R.id.button_generate_report);

        dbHelper = new AttendanceDbHelper(this);

        editStartDate.setOnClickListener(v -> openDatePicker(editStartDate));
        editEndDate.setOnClickListener(v -> openDatePicker(editEndDate));

        generateReportButton.setOnClickListener(this::onGenerateReportClicked);
    }

    private void openDatePicker(final EditText dateField) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    String date = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                    dateField.setText(date);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void onGenerateReportClicked(View view) {
        startDate = editStartDate.getText().toString();
        endDate = editEndDate.getText().toString();
        String employeeIdStr = editEmployeeId.getText().toString();

        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please select both start and end dates", Toast.LENGTH_SHORT).show();
            return;
        }

        employeeId = null;
        if (!employeeIdStr.isEmpty()) {
            try {
                employeeId = Integer.parseInt(employeeIdStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid Employee ID", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Launch the SAF to create a new PDF document
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "Date_Wise_Attendance_Report.pdf");
        saveFileLauncher.launch(intent);
    }
}
