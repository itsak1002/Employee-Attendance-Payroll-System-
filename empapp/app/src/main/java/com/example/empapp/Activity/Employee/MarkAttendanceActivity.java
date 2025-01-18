package com.example.empapp.Activity.Employee.Employee;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.empapp.DatabaseHelper.AttendanceDbHelper;
import com.example.empapp.R;
import com.example.empapp.DatabaseHelper.OfficeLocationDbHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MarkAttendanceActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private AttendanceDbHelper dbHelper;
    private OfficeLocationDbHelper officeDbHelper;
    private TextView tvStatus;
    private Button btnMarkIn, btnMarkOut;

    private static final float DEFAULT_RADIUS = 500; // Default radius in meters
    private double officeLatitude;
    private double officeLongitude;
    private float geofenceRadius;

    private String todayDate;
    private int employeeId;
    private int officeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        // Initialize views and database helpers
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        dbHelper = new AttendanceDbHelper(this);
        officeDbHelper = new OfficeLocationDbHelper(this);
        tvStatus = findViewById(R.id.tv_status);
        btnMarkIn = findViewById(R.id.btn_mark_in);
        btnMarkOut = findViewById(R.id.btn_mark_out);

        // Retrieve employee ID and office ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        employeeId = sharedPreferences.getInt("employeeId", -1);
        officeId = 1; //sharedPreferences.getInt("officeId", -1);

        fetchOfficeLocation(officeId);

        todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Set up button listeners
        btnMarkIn.setOnClickListener(v -> checkLocationAndProceed(true));
        btnMarkOut.setOnClickListener(v -> checkLocationAndProceed(false));

        updateUI();
    }

    private void fetchOfficeLocation(int officeId) {
        Cursor cursor = officeDbHelper.getOfficeLocationDetails(officeId);
        if (cursor != null && cursor.moveToFirst()) {
            officeLatitude = cursor.getDouble(cursor.getColumnIndex("office_latitude"));
            officeLongitude = cursor.getDouble(cursor.getColumnIndex("office_longitude"));
            geofenceRadius = cursor.getFloat(cursor.getColumnIndex("radius"));
            cursor.close();
        } else {
            // Default location and radius if no data found
            officeLatitude = 18.159538;
            officeLongitude = 74.579059;
            geofenceRadius = DEFAULT_RADIUS;
        }
    }

    private void checkLocationAndProceed(boolean isMarkIn) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Fetch current location
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                handleLocation(location, isMarkIn);
            } else {
                requestContinuousLocationUpdates(isMarkIn);
            }
        });
    }

    private void handleLocation(Location location, boolean isMarkIn) {
        if (isWithinGeofence(location.getLatitude(), location.getLongitude())) {
            if (isMarkIn) {
                markIn();
            } else {
                markOut();
            }
        } else {
            Toast.makeText(this, "You are outside the office geofence!", Toast.LENGTH_LONG).show();
        }
    }

    private void requestContinuousLocationUpdates(boolean isMarkIn) {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(2000);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    handleLocation(location, isMarkIn);
                    fusedLocationClient.removeLocationUpdates(this); // Stop updates after successful fetch
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private boolean isWithinGeofence(double latitude, double longitude) {
        float[] distance = new float[1];
        Location.distanceBetween(latitude, longitude, officeLatitude, officeLongitude, distance);
        return distance[0] <= geofenceRadius;
    }

    private void markIn() {
        if (dbHelper.isAttendanceMarked(employeeId, todayDate)) {
            Toast.makeText(this, "Attendance already marked for today!", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        long result = dbHelper.insertAttendance(employeeId, todayDate, currentTime, null, "Present", null, null, null);

        if (result != -1) {
            Toast.makeText(this, "Marked In successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to mark in.", Toast.LENGTH_SHORT).show();
        }

        updateUI();
    }

    private void markOut() {
        if (!dbHelper.hasCheckedIn(employeeId, todayDate)) {
            Toast.makeText(this, "You must mark in first!", Toast.LENGTH_SHORT).show();
            return;
        }

        String checkInTime = dbHelper.getCheckInTime(employeeId, todayDate);
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String workingHours = dbHelper.calculateWorkingHours(checkInTime, currentTime);

        int rowsUpdated = dbHelper.updateCheckOut(employeeId, todayDate, currentTime, workingHours);

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Marked Out successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to mark out.", Toast.LENGTH_SHORT).show();
        }

        updateUI();
    }

    private void updateUI() {
        if (dbHelper.isAttendanceMarked(employeeId, todayDate)) {
            String checkInTime = dbHelper.getCheckInTime(employeeId, todayDate);
            String checkOutTime = dbHelper.getCheckOutTime(employeeId, todayDate);

            String statusMessage = "Attendance marked for today!";
            String timeMessage = "Mark-in Time: " + (checkInTime != null ? checkInTime : "Not marked") + "\n" +
                    "Mark-out Time: " + (checkOutTime != null ? checkOutTime : "Not marked");

            tvStatus.setText(statusMessage + "\n" + timeMessage);
            btnMarkIn.setEnabled(false);

            if (checkInTime != null && checkOutTime == null) {
                btnMarkOut.setEnabled(true);
            } else {
                btnMarkOut.setEnabled(false);
            }
        } else {
            tvStatus.setText("No attendance marked for today.");
            btnMarkIn.setEnabled(true);
            btnMarkOut.setEnabled(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted. Try again.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Location permission is required.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        officeDbHelper.close();
        super.onDestroy();
    }
}