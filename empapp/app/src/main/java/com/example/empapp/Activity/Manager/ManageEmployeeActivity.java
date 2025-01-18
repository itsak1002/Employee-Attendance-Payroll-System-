package com.example.empapp.Activity.Manager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.R;

public class ManageEmployeeActivity extends AppCompatActivity {

    private Button btnAddEmployee, btnDisplayEmployee, btnUpdateEmployee, btnDeleteEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employee);

        // Initialize Buttons
        btnAddEmployee = findViewById(R.id.btnAddEmployee);
        btnDisplayEmployee = findViewById(R.id.btnDisplayEmployee);
        btnUpdateEmployee = findViewById(R.id.btnUpdateEmployee);
        btnDeleteEmployee = findViewById(R.id.btnDeleteEmployee);

        // Set click listener for Add Employee button
        btnAddEmployee.setOnClickListener(v -> {
            Toast.makeText(ManageEmployeeActivity.this, "Navigating to Add Employee", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ManageEmployeeActivity.this, AddEmployeeActivity.class);
            startActivity(intent);
        });

        // Set click listener for Display Employee button
        btnDisplayEmployee.setOnClickListener(v -> {
            Toast.makeText(ManageEmployeeActivity.this, "Navigating to Display Employees", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ManageEmployeeActivity.this, DisplayEmployeesActivity.class);
            startActivity(intent);
        });

        // Set click listener for Update Employee button
        btnUpdateEmployee.setOnClickListener(v -> {
            Toast.makeText(ManageEmployeeActivity.this, "Navigating to Update Employee", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ManageEmployeeActivity.this, UpdateEmployeeActivity.class);
            startActivity(intent);
        });

        // Set click listener for Delete Employee button
        btnDeleteEmployee.setOnClickListener(v -> {
            Toast.makeText(ManageEmployeeActivity.this, "Navigating to Delete Employee", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ManageEmployeeActivity.this, DeleteEmployeeActivity.class);
            startActivity(intent);
        });
    }
}
