package com.example.empapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.Activity.Employee.EmployeeLogin;
import com.example.empapp.Activity.Manager.ManagerLogin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Manager Login Button
        Button btnManagerLogin = findViewById(R.id.btnManagerLogin);
        btnManagerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManagerLogin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left); // Slide in from right
            }
        });

        // Employee Login Button
        Button btnEmployeeLogin = findViewById(R.id.btnEmployeeLogin);
        btnEmployeeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmployeeLogin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // Slide in from left
            }
        });
    }
}
