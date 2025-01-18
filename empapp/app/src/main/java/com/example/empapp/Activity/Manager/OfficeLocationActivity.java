package com.example.empapp.Activity.Manager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.empapp.DatabaseHelper.OfficeLocationDbHelper;
import com.example.empapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class OfficeLocationActivity extends AppCompatActivity {

    private EditText etOfficeId, etLatitude, etLongitude, etRadius;
    private Button btnInsert, btnUpdate, btnDelete, btnDisplay, btnGetCurrentLocation, btnOpenMap;
    private ListView listView;
    private FusedLocationProviderClient fusedLocationClient;
    private OfficeLocationDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_location);

        // Initialize views
        etOfficeId = findViewById(R.id.et_office_id);
        etLatitude = findViewById(R.id.et_latitude);
        etLongitude = findViewById(R.id.et_longitude);
        etRadius = findViewById(R.id.et_radius);
        btnInsert = findViewById(R.id.btn_insert);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        btnDisplay = findViewById(R.id.btn_display);
        btnGetCurrentLocation = findViewById(R.id.btn_get_current_location);
        btnOpenMap = findViewById(R.id.btn_open_map);
        listView = findViewById(R.id.list_view);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        dbHelper = new OfficeLocationDbHelper(this);

        // Button listeners
        btnInsert.setOnClickListener(v -> insertOfficeLocation());
        btnUpdate.setOnClickListener(v -> updateOfficeLocation());
        btnDelete.setOnClickListener(v -> deleteOfficeLocation());
        btnDisplay.setOnClickListener(v -> displayAllOfficeLocations());
        btnGetCurrentLocation.setOnClickListener(v -> getCurrentLocation());

        // Open Map button listener
        btnOpenMap.setOnClickListener(v -> openMap());
    }

    private void openMap() {
        // Get latitude and longitude from input fields
        String latitude = etLatitude.getText().toString();
        String longitude = etLongitude.getText().toString();

        if (latitude.isEmpty() || longitude.isEmpty()) {
            Toast.makeText(this, "Please enter valid coordinates", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an Intent to open Google Maps
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude));

        // Check if there is a map app available
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No map app available", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertOfficeLocation() {
        try {
            int officeId = Integer.parseInt(etOfficeId.getText().toString());
            double latitude = Double.parseDouble(etLatitude.getText().toString());
            double longitude = Double.parseDouble(etLongitude.getText().toString());
            float radius = Float.parseFloat(etRadius.getText().toString());

            long result = dbHelper.insertOfficeLocationDetails(officeId, latitude, longitude, radius);

            if (result != -1) {
                Toast.makeText(this, "Office Location Inserted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Insertion Failed", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOfficeLocation() {
        try {
            int officeId = Integer.parseInt(etOfficeId.getText().toString());
            double latitude = Double.parseDouble(etLatitude.getText().toString());
            double longitude = Double.parseDouble(etLongitude.getText().toString());
            float radius = Float.parseFloat(etRadius.getText().toString());

            int rowsUpdated = dbHelper.updateOfficeLocationDetails(officeId, latitude, longitude, radius);

            if (rowsUpdated > 0) {
                Toast.makeText(this, "Office Location Updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteOfficeLocation() {
        try {
            int officeId = Integer.parseInt(etOfficeId.getText().toString());

            dbHelper.deleteOfficeLocationDetails(officeId);
            Toast.makeText(this, "Office Location Deleted", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayAllOfficeLocations() {
        Cursor cursor = dbHelper.getAllOfficeLocations();
        if (cursor != null) {
            String[] from = { "office_id", "office_latitude", "office_longitude", "radius" };
            int[] to = { R.id.tv_office_id, R.id.tv_latitude, R.id.tv_longitude, R.id.tv_radius };

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this, R.layout.item_office_location, cursor, from, to, 0);
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                etLatitude.setText(String.valueOf(location.getLatitude()));
                etLongitude.setText(String.valueOf(location.getLongitude()));
                Toast.makeText(this, "Location fetched successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to fetch location!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted. Try again.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Location permission is required.", Toast.LENGTH_SHORT).show();
        }
    }
}
