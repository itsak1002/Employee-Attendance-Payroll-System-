package com.example.empapp.Activity.Manager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.DatabaseHelper.DBHelper;
import com.example.empapp.R;

public class UpdateEmployeeActivity extends AppCompatActivity {

    private EditText etEmployeeId, etNewName, etNewUsername, etNewDesignation, etNewAddress;
    private Button btnFetchEmployee, btnUpdateEmployee;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employee);

        // Initialize views
        etEmployeeId = findViewById(R.id.etEmployeeId);
        etNewName = findViewById(R.id.etNewName);
        etNewUsername = findViewById(R.id.etNewUsername);
        etNewDesignation = findViewById(R.id.etNewDesignation);
        etNewAddress = findViewById(R.id.etNewAddress);
        btnFetchEmployee = findViewById(R.id.btnFetchEmployee);
        btnUpdateEmployee = findViewById(R.id.btnUpdateEmployee);

        dbHelper = new DBHelper(this);

        // Fetch employee details
        btnFetchEmployee.setOnClickListener(v -> {
            String employeeIdStr = etEmployeeId.getText().toString().trim();
            if (employeeIdStr.isEmpty()) {
                Toast.makeText(UpdateEmployeeActivity.this, "Enter Employee ID", Toast.LENGTH_SHORT).show();
                return;
            }

            int employeeId = Integer.parseInt(employeeIdStr);
            fetchEmployeeDetails(employeeId);
        });

        // Update employee details
        btnUpdateEmployee.setOnClickListener(v -> {
            String employeeIdStr = etEmployeeId.getText().toString().trim();
            String newName = etNewName.getText().toString().trim();
            String newUsername = etNewUsername.getText().toString().trim();
            String newDesignation = etNewDesignation.getText().toString().trim();
            String newAddress = etNewAddress.getText().toString().trim();

            if (employeeIdStr.isEmpty() || newName.isEmpty() || newUsername.isEmpty() || newDesignation.isEmpty() || newAddress.isEmpty()) {
                Toast.makeText(UpdateEmployeeActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int employeeId = Integer.parseInt(employeeIdStr);
            updateEmployeeDetails(employeeId, newName, newUsername, newDesignation, newAddress);
        });
    }

    // Fetch and display employee details
    private void fetchEmployeeDetails(int employeeId) {
        Cursor cursor = dbHelper.getEmployeeById(employeeId);
        if (cursor != null && cursor.moveToFirst()) {
            etNewName.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMPLOYEE_FIRST_NAME)));
            etNewUsername.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMPLOYEE_USERNAME)));
            etNewDesignation.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMPLOYEE_DESIGNATION)));
            etNewAddress.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMPLOYEE_ADDRESS)));
            cursor.close();
        } else {
            Toast.makeText(this, "Employee not found", Toast.LENGTH_SHORT).show();
        }
    }

    // Update employee details
    private void updateEmployeeDetails(int employeeId, String newName, String newUsername, String newDesignation, String newAddress) {
        boolean isUpdated = dbHelper.updateEmployee(employeeId, newName, newUsername, newDesignation, newAddress);
        if (isUpdated) {
            Toast.makeText(this, "Employee updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}
