package com.example.empapp.Activity.Employee.Employee;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.empapp.Activity.Employee.EmployeeDashboardActivity;
import com.example.empapp.DatabaseHelper.DBHelper;
import com.example.empapp.R;
import com.google.android.material.button.MaterialButton;
import android.os.Handler;


import java.util.Random;

public class OtpverificationActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 1;
    private static final long OTP_RESEND_INTERVAL_MS = 60 * 1000; // 1 minute
    private static final long OTP_TIMEOUT_MS = 5 * 60 * 1000; // 5 minutes timeout

    private String generatedOtp;
    private String registeredPhoneNumber;
    private int employeeId;
    private boolean isOtpValid = false;

    private EditText inputOtp;
    private TextView tvMaskedPhoneNumber, tvTimer;
    private MaterialButton verifyOtpButton, resendOtpButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        inputOtp = findViewById(R.id.inputOtp);
        tvMaskedPhoneNumber = findViewById(R.id.tvMaskedPhoneNumber);
        tvTimer = findViewById(R.id.tvTimer);
        verifyOtpButton = findViewById(R.id.verifyOtpButton);
        resendOtpButton = findViewById(R.id.resendOtpButton);

        dbHelper = new DBHelper(this);

        checkSmsPermission();

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        employeeId = sharedPreferences.getInt("employeeId", -1);

        if (employeeId != -1) {
            registeredPhoneNumber = dbHelper.getEmployeePhoneById(employeeId);
            if (registeredPhoneNumber != null) {
                tvMaskedPhoneNumber.setText(maskPhoneNumber(registeredPhoneNumber));
                generatedOtp = generateOtp();
                isOtpValid = true;
                sendOtp(generatedOtp);
                startOtpTimeout();
                startResendOtpTimer();
            } else {
                Toast.makeText(this, "Phone number not found for the given Employee ID.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Employee ID not found in SharedPreferences.", Toast.LENGTH_SHORT).show();
        }

        verifyOtpButton.setOnClickListener(view -> {
            String enteredOtp = inputOtp.getText().toString();
            if (isOtpValid && enteredOtp.equals(generatedOtp)) {
                Toast.makeText(OtpverificationActivity.this, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OtpverificationActivity.this, EmployeeDashboardActivity.class);
                intent.putExtra("employeeId", employeeId);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(OtpverificationActivity.this, "Invalid or Expired OTP. Try Again.", Toast.LENGTH_SHORT).show();
            }
        });

        resendOtpButton.setOnClickListener(view -> {
            generatedOtp = generateOtp();
            isOtpValid = true;
            sendOtp(generatedOtp);
            startOtpTimeout();
            startResendOtpTimer();
        });
    }

    private void checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                showPermissionRationale();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
            }
        }
    }

    private void showPermissionRationale() {
        new AlertDialog.Builder(this)
                .setTitle("SMS Permission Required")
                .setMessage("This app needs SMS permissions to send OTP for verification. Please grant the permission.")
                .setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE))
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() >= 4) {
            return "xxxxxxxx" + phoneNumber.substring(phoneNumber.length() - 4);
        } else {
            return "Invalid Phone Number";
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate a 6-digit OTP
        return String.valueOf(otp);
    }

    private void sendOtp(String otp) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();
            String message = "Your OTP for login to Smart Technology with employee ID " + employeeId + " is: " + otp;
            smsManager.sendTextMessage(registeredPhoneNumber, null, message, null, null);
            Toast.makeText(this, "OTP Sent Successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "SMS Permission Denied. Unable to send OTP.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startOtpTimeout() {
        new Handler().postDelayed(() -> {
            isOtpValid = false;
            Toast.makeText(OtpverificationActivity.this, "OTP has expired. Please request a new one.", Toast.LENGTH_SHORT).show();
        }, OTP_TIMEOUT_MS);
    }

    private void startResendOtpTimer() {
        resendOtpButton.setEnabled(false);
        resendOtpButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.disabled));

        new CountDownTimer(OTP_RESEND_INTERVAL_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                tvTimer.setText("Resend OTP in " + seconds / 60 + ":" + String.format("%02d", seconds % 60));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("");
                resendOtpButton.setEnabled(true);
                resendOtpButton.setBackgroundTintList(ContextCompat.getColorStateList(OtpverificationActivity.this, R.color.colorPrimary));
            }
        }.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS Permission Granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS Permission Denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
