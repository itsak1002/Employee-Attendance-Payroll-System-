package com.example.empapp.Activity.Employee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.DatabaseHelper.DBHelper;
import com.example.empapp.Activity.Employee.Employee.OtpverificationActivity;
import com.example.empapp.R;

public class EmployeeLogin extends AppCompatActivity {

    private EditText etEmployeeId, etEmployeePassword;
    private Button btnEmployeeLogin;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_login);

        // Initialize views
        etEmployeeId = findViewById(R.id.etEmployeeId);
        etEmployeePassword = findViewById(R.id.etEmployeePassword);
        btnEmployeeLogin = findViewById(R.id.btnEmployeeLogin);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Apply bounce animation to all fields
        applyBounceAnimation(etEmployeeId);
        applyBounceAnimation(etEmployeePassword);
        applyBounceAnimation(btnEmployeeLogin);

        // Set login button click listener
        btnEmployeeLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String employeeIdStr = etEmployeeId.getText().toString().trim();
        String password = etEmployeePassword.getText().toString().trim();

        if (employeeIdStr.isEmpty() || password.isEmpty()) {
            showToast("Please enter Employee ID and Password");
            return;
        }

        try {
            int employeeId = Integer.parseInt(employeeIdStr);
            if (dbHelper.validateEmployee(String.valueOf(employeeId), password)) {
                saveEmployeeSession(employeeId);
                navigateToOtpVerification();
            } else {
                showToast("Invalid Employee ID or Password");
            }
        } catch (NumberFormatException e) {
            showToast("Invalid Employee ID");
        }
    }

    private void saveEmployeeSession(int employeeId) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        sharedPreferences.edit().putInt("employeeId", employeeId).apply();
    }

    private void navigateToOtpVerification() {
        showToast("Login Successful");
        Intent intent = new Intent(EmployeeLogin.this, OtpverificationActivity.class);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(EmployeeLogin.this, message, Toast.LENGTH_SHORT).show();
    }

    // Apply bounce animation to a view
    private void applyBounceAnimation(View view) {
        TranslateAnimation bounce = new TranslateAnimation(0, 0, -50, 0);
        bounce.setDuration(600); // Duration of the animation
        bounce.setRepeatCount(0); // No repetition
        bounce.setFillAfter(true); // Keeps the view in its final position
        view.startAnimation(bounce);
    }
}
