package com.example.empapp.Activity.Manager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.empapp.DatabaseHelper.DBHelper;
import com.example.empapp.R;
import java.util.Calendar;

public class AddEmployeeActivity extends AppCompatActivity {

    private EditText etEmployeeId, etUsername, etPassword, etFirstName, etLastName,
            etDesignation, etAddress, etDateOfBirth, etDateOfJoining, etEmail, etPhoneNumber;
    private Button btnAddEmployee;
    private DBHelper dbHelper;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        // Initialize views
        etEmployeeId = findViewById(R.id.etEmployeeId);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etDesignation = findViewById(R.id.etDesignation);
        etAddress = findViewById(R.id.etAddress);
        etDateOfBirth = findViewById(R.id.etDateOfBirth);
        etDateOfJoining = findViewById(R.id.etDateOfJoining);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnAddEmployee = findViewById(R.id.btnAddEmployee);

        dbHelper = new DBHelper(this);

        // Set onClick listener for the button
        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmployee();
            }
        });

        // Set up DatePickerDialog for Date of Birth and Date of Joining fields
        etDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(etDateOfBirth);
            }
        });

        etDateOfJoining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(etDateOfJoining);
            }
        });
    }

    private void openDatePicker(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(AddEmployeeActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        editText.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void addEmployee() {
        // Get input values
        String employeeIdStr = etEmployeeId.getText().toString();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String designation = etDesignation.getText().toString();
        String address = etAddress.getText().toString();
        String dateOfBirth = etDateOfBirth.getText().toString();
        String dateOfJoining = etDateOfJoining.getText().toString();
        String email = etEmail.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();

        // Validate input
        if (TextUtils.isEmpty(employeeIdStr) || TextUtils.isEmpty(username) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(firstName) ||
                TextUtils.isEmpty(lastName) || TextUtils.isEmpty(designation) ||
                TextUtils.isEmpty(address) || TextUtils.isEmpty(dateOfBirth) ||
                TextUtils.isEmpty(dateOfJoining) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Employee ID must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add employee to the database
        boolean isAdded = dbHelper.addEmployee(employeeId, username, password, designation,
                firstName, lastName, address, dateOfBirth, dateOfJoining, email, phoneNumber);

        if (isAdded) {
            Toast.makeText(this, "Employee added successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Failed to add employee. ID or Username already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        etEmployeeId.setText("");
        etUsername.setText("");
        etPassword.setText("");
        etFirstName.setText("");
        etLastName.setText("");
        etDesignation.setText("");
        etAddress.setText("");
        etDateOfBirth.setText("");
        etDateOfJoining.setText("");
        etEmail.setText("");
        etPhoneNumber.setText("");
    }
}
