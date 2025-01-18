package com.example.empapp.Activity.Manager;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.empapp.DatabaseHelper.AttendanceDbHelper;
import com.example.empapp.DatabaseHelper.DBHelper;
import com.example.empapp.R;
import com.example.empapp.Activity.Manager.reports.all_reportsActivity;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvAllReports, tvAddEmployee, tvLeaveRequest, tvAddBankDetails,
            tvAddEducationDetails, tvEmployeeDetails;
    private HalfCircleProgressBar progressBar;
    private DBHelper dbHelper;
    private AttendanceDbHelper attendanceDbHelper;
    private Button btnPresentEmployee, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvAllReports = findViewById(R.id.tvAllReports);
        tvAddEmployee = findViewById(R.id.tvAddEmployee);
        tvLeaveRequest = findViewById(R.id.tvLeaveRequest);
        tvAddBankDetails = findViewById(R.id.tvAddBankDetails);
        tvAddEducationDetails = findViewById(R.id.tvAddEducationDetails);
        tvEmployeeDetails = findViewById(R.id.tvEmployeeDetails);
        progressBar = findViewById(R.id.halfCircleProgressBar);

        btnPresentEmployee = findViewById(R.id.btnPresentEmployee);
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> logout());

        dbHelper = new DBHelper(this);
        attendanceDbHelper = new AttendanceDbHelper(this);

        setOnClickListeners();
        new FetchEmployeeDataTask().execute();
        animateTextViews();
    }

    private void setOnClickListeners() {
        tvAllReports.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, all_reportsActivity.class)));
        tvAddEmployee.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, ManageEmployeeActivity.class)));
        tvLeaveRequest.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, ManageLeaveActivity.class)));
        tvAddBankDetails.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, add_payment.class)));
        tvAddEducationDetails.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, OfficeLocationActivity.class)));
        btnPresentEmployee.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, CurrentDayAttendanceActivity.class)));
    }

    private void animateTextViews() {
        animateTextView(tvAllReports);
        animateTextView(tvAddEmployee);
        animateTextView(tvLeaveRequest);
        animateTextView(tvAddBankDetails);
        animateTextView(tvAddEducationDetails);
    }

    private void animateTextView(TextView textView) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(textView, "scaleX", 0.9f, 1.1f, 0.9f, 1f);
        scaleX.setDuration(300);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(textView, "scaleY", 0.9f, 1.1f, 0.9f, 1f);
        scaleY.setDuration(300);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }

    private void logout() {
        Intent intent = new Intent(DashboardActivity.this, ManagerLogin.class);
        startActivity(intent);
        finishAffinity();
    }

    private class FetchEmployeeDataTask extends AsyncTask<Void, Void, EmployeeData> {
        @Override
        protected EmployeeData doInBackground(Void... voids) {
            try {
                int employeeCount = attendanceDbHelper.countTodaysPresentEmployees();
                int totalEmployees = dbHelper.getEmployeeCount();
                return new EmployeeData(employeeCount, totalEmployees);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(EmployeeData data) {
            if (data == null) {
                Toast.makeText(DashboardActivity.this, "Failed to fetch employee data", Toast.LENGTH_SHORT).show();
                return;
            }

            int percentage = (data.getTotalEmployees() > 0) ? (data.getEmployeeCount() * 100) / data.getTotalEmployees() : 0;
            progressBar.setProgress(percentage);
            String employeeDetails = percentage + "%\n" + data.getEmployeeCount() + " Employees";
            tvEmployeeDetails.setText(employeeDetails);
        }
    }

    private static class EmployeeData {
        private final int employeeCount;
        private final int totalEmployees;

        public EmployeeData(int employeeCount, int totalEmployees) {
            this.employeeCount = employeeCount;
            this.totalEmployees = totalEmployees;
        }

        public int getEmployeeCount() {
            return employeeCount;
        }

        public int getTotalEmployees() {
            return totalEmployees;
        }
    }
}
