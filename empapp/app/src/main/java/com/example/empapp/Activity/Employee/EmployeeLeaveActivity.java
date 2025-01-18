package com.example.empapp.Activity.Employee;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.DatabaseHelper.LeaveDBHelper;
import com.example.empapp.R;

import java.util.Calendar;

public class EmployeeLeaveActivity extends AppCompatActivity {

    private LeaveDBHelper dbHelper;
    private EditText etLeaveDate, etLeaveReason;
    private Button btnSubmitLeave, btnlh;
    private int employeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_leave);

        dbHelper = new LeaveDBHelper(this);

        etLeaveDate = findViewById(R.id.etLeaveDate);
        etLeaveReason = findViewById(R.id.etLeaveReason);
        btnSubmitLeave = findViewById(R.id.btnSubmitLeave);
        btnlh = findViewById(R.id.btnlh);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        employeeId = sharedPreferences.getInt("employeeId", -1);

        if (employeeId == -1) {
            Toast.makeText(this, "No active session. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
        }

        etLeaveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeLeaveActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etLeaveDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnSubmitLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String leaveDate = etLeaveDate.getText().toString();
                String leaveReason = etLeaveReason.getText().toString();

                if (leaveDate.isEmpty() || leaveReason.isEmpty()) {
                    Toast.makeText(EmployeeLeaveActivity.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.addLeaveRequest(String.valueOf(employeeId), leaveDate, leaveReason)) {
                    Toast.makeText(EmployeeLeaveActivity.this, "Leave Request Submitted", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EmployeeLeaveActivity.this, "Submission Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnlh.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeLeaveActivity.this, ViewLeaveRequestsActivity.class);
            startActivity(intent);
        });
    }
}
