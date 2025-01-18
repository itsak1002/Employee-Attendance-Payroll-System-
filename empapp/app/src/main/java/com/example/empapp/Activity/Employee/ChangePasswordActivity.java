package com.example.empapp.Activity.Employee;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.empapp.DatabaseHelper.DBHelper;
import com.example.empapp.R;

public class ChangePasswordActivity extends Activity {

    private DBHelper dbHelper;
    private EditText etCurrentPassword, etNewPassword, etConfirmNewPassword;
    private Button btnChangePassword;
    private int employeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        dbHelper = new DBHelper(this);

        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        // Retrieve employeeId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        employeeId = sharedPreferences.getInt("employeeId", -1);

        if (employeeId == -1) {
            Toast.makeText(this, "Employee ID not found in session.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChangePassword();
            }
        });
    }

    private void handleChangePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmNewPassword = etConfirmNewPassword.getText().toString().trim();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            Toast.makeText(ChangePasswordActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(ChangePasswordActivity.this, "New Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate the current password using employeeId
        if (dbHelper.validateEmployee(String.valueOf(employeeId), currentPassword)) {
            // Directly update the password since OTP is skipped
            int rowsAffected = dbHelper.updateEmployeePassword(employeeId, currentPassword, newPassword);
            if (rowsAffected > 0) {
                Toast.makeText(ChangePasswordActivity.this, "Password successfully changed", Toast.LENGTH_SHORT).show();
                // Start the dashboard activity
                Intent intent = new Intent(ChangePasswordActivity.this, EmployeeDashboardActivity.class);
                startActivity(intent);
                finish();  // Close this activity
            } else {
                Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ChangePasswordActivity.this, "Invalid current password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {  // Handle result from OTP verification
            if (resultCode == RESULT_OK) {
                // OTP verified successfully
                String newPassword = etNewPassword.getText().toString().trim();
                String currentPassword = data.getStringExtra("currentPassword");

                int rowsAffected = dbHelper.updateEmployeePassword(employeeId, currentPassword, newPassword);
                if (rowsAffected > 0) {
                    Toast.makeText(ChangePasswordActivity.this, "Password successfully changed", Toast.LENGTH_SHORT).show();
                    // Start the dashboard activity
                    Intent intent = new Intent(ChangePasswordActivity.this, EmployeeDashboardActivity.class);
                    startActivity(intent);
                    finish();  // Close this activity
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ChangePasswordActivity.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
