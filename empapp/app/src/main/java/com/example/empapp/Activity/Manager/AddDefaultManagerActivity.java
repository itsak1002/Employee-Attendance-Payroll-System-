package com.example.empapp.Activity.Manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.DatabaseHelper.DBHelper;
import com.example.empapp.MainActivity;
import com.example.empapp.R;

public class AddDefaultManagerActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonAddManager, buttonProceed;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_default_manager);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Initialize UI components
        editTextUsername = findViewById(R.id.editTextManagerUsername);
        editTextPassword = findViewById(R.id.editTextManagerPassword);
        buttonAddManager = findViewById(R.id.buttonAddManager);
        buttonProceed = findViewById(R.id.buttonProceed);

        // Add Manager button click listener
        buttonAddManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(AddDefaultManagerActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (addDefaultManager(username, password)) {
                        Toast.makeText(AddDefaultManagerActivity.this, "Manager added successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddDefaultManagerActivity.this, "Manager already exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Proceed button click listener
        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to main activity or dashboard
                Intent intent = new Intent(AddDefaultManagerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Add a default manager to the database
    private boolean addDefaultManager(String username, String password) {
        return dbHelper.addManager(username, password);
    }
}
