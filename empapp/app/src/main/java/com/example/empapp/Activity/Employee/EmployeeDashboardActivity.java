package com.example.empapp.Activity.Employee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.Activity.Employee.Employee.MarkAttendanceActivity;
import com.example.empapp.Activity.Employee.Employee.ProfileActivity;
import com.example.empapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EmployeeDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnMarkAttendance, btnProfile, btnAttendanceHistory, btnLeaveRequest, btnPaySlip,btnChangePassword, btnLogout;
    private FloatingActionButton fabChatbot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        // Initialize views
        tvWelcome = findViewById(R.id.tvWelcome);
        btnMarkAttendance = findViewById(R.id.btnMarkAttendance);
        btnProfile = findViewById(R.id.btnProfile);
        btnAttendanceHistory = findViewById(R.id.btnAttendanceHistory);
        btnLeaveRequest = findViewById(R.id.btnLeaveRequest);
        btnPaySlip = findViewById(R.id.btnPaySlip);
        btnLogout = findViewById(R.id.btnLogout);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        fabChatbot = findViewById(R.id.fabChatbot);

        // Validate session
        validateSession();

        // Set OnClickListeners for each button
        setupButtonListeners();

        // Set OnClickListener for the Floating Action Button (FAB)
        fabChatbot.setOnClickListener(v -> {
            // Open Chatbot activity or dialog
            Intent intent = new Intent(EmployeeDashboardActivity.this, hrchatbot.class);
            startActivity(intent);
        });
    }

    private void validateSession() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int employeeId = sharedPreferences.getInt("employeeId", -1);

        if (employeeId == -1) {
            Toast.makeText(this, "No active session. Please log in.", Toast.LENGTH_SHORT).show();
            redirectToLogin();
        } else {
            String username = getIntent().getStringExtra("username");
            if (username != null) {
                tvWelcome.setText("Welcome, " + username);
            } else {
                tvWelcome.setText("Welcome, Employee!");
            }
        }
    }


    private void setupButtonListeners() {
        btnMarkAttendance.setOnClickListener(v -> navigateToActivity(MarkAttendanceActivity.class));
        btnProfile.setOnClickListener(v -> navigateToActivity(ProfileActivity.class));
        btnAttendanceHistory.setOnClickListener(v -> navigateToActivity(AttendanceHistoryActivity.class));
        btnLeaveRequest.setOnClickListener(v -> navigateToActivity(EmployeeLeaveActivity.class));
        btnPaySlip.setOnClickListener(v -> navigateToActivity(GeneratePayslipActivity.class));
        btnChangePassword.setOnClickListener(v -> navigateToActivity(PasswordOtpverificationActivity.class));
        btnLogout.setOnClickListener(v -> logout());
    }

    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(EmployeeDashboardActivity.this, targetActivity);
        startActivity(intent);
    }

    private void redirectToLogin() {
        Intent intent = new Intent(EmployeeDashboardActivity.this, EmployeeLogin.class);
        startActivity(intent);
        finish();
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear the session
        editor.apply();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        redirectToLogin(); // Redirect to login after logout
    }
}
