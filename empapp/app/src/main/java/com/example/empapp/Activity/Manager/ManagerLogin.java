package com.example.empapp.Activity.Manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.empapp.DatabaseHelper.DBHelper;
import com.example.empapp.R;

public class ManagerLogin extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnAddManager;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);

        dbHelper = new DBHelper(this);
        etUsername = findViewById(R.id.etManagerUsername);
        etPassword = findViewById(R.id.etManagerPassword);
        btnLogin = findViewById(R.id.btnManagerLogin);
        btnAddManager = findViewById(R.id.btnAddManager);

        View loginAnimationView = findViewById(R.id.loginAnimationView);
        View tvManagerLoginTitle = findViewById(R.id.tvManagerLoginTitle);
        View usernameInputLayout = findViewById(R.id.usernameInputLayout);
        View passwordInputLayout = findViewById(R.id.passwordInputLayout);

        animateViewBounce(loginAnimationView, 0);
        animateViewBounce(tvManagerLoginTitle, 100);
        animateViewBounce(usernameInputLayout, 200);
        animateViewBounce(passwordInputLayout, 300);
        animateViewBounce(btnLogin, 400);
        animateViewBounce(btnAddManager, 500);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(ManagerLogin.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.validateManager(username, password)) {
                Toast.makeText(ManagerLogin.this, "Manager Login Successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ManagerLogin.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ManagerLogin.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddManager.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerLogin.this, AddDefaultManagerActivity.class);
            startActivity(intent);
        });
    }

    private void animateViewBounce(View view, int delay) {
        view.setAlpha(0f);
        view.setScaleX(0.5f);
        view.setScaleY(0.5f);

        view.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setStartDelay(delay)
                .setDuration(800)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }
}
