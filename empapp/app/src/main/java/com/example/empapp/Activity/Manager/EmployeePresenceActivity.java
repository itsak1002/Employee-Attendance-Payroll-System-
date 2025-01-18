package com.example.empapp.Activity.Manager;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.R;

public class EmployeePresenceActivity extends AppCompatActivity {

    private HalfCircleProgressBar halfCircleProgressBar;
    private TextView tvEmployeeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_presence);

        // Initialize the custom view and TextView
        halfCircleProgressBar = findViewById(R.id.halfCircleProgressBar);
        tvEmployeeDetails = findViewById(R.id.tvEmployeeDetails);

        // Example values
        int totalEmployees = 20; // Example total employee count
        int presentEmployees = 15; // Example present employee count

        // Calculate percentage
        int progress = (int) ((presentEmployees / (float) totalEmployees) * 100);

        // Animate progress bar and update text
        animateProgress(progress, presentEmployees);
    }

    private void animateProgress(int targetProgress, int presentEmployees) {
        ValueAnimator animator = ValueAnimator.ofInt(0, targetProgress);
        animator.setDuration(1000); // 1 second animation
        animator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();
            halfCircleProgressBar.setProgress(progress);
            String detailsText = progress + "%\n" + presentEmployees + " Employees";
            tvEmployeeDetails.setText(detailsText);
        });
        animator.start();
    }
}
