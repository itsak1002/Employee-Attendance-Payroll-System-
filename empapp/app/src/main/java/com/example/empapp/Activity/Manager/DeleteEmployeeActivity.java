package com.example.empapp.Activity.Manager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.DatabaseHelper.DBHelper;
import com.example.empapp.R;

public class DeleteEmployeeActivity extends AppCompatActivity {

    private EditText employeeIdEditText;
    private Button deleteButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_employee); // Your XML layout file

        // Initialize the views
        employeeIdEditText = findViewById(R.id.employeeIdEditText); // EditText to enter Employee ID
        deleteButton = findViewById(R.id.deleteButton); // Button to trigger deletion
        dbHelper = new DBHelper(this); // DBHelper instance

        // Set up the delete button onClickListener
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String employeeIdStr = employeeIdEditText.getText().toString().trim();

                // Check if the input is empty
                if (employeeIdStr.isEmpty()) {
                    Toast.makeText(DeleteEmployeeActivity.this, "Please enter an Employee ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convert Employee ID to integer
                int employeeId = Integer.parseInt(employeeIdStr);

                // Show confirmation dialog before deleting
                showConfirmationDialog(employeeId);
            }
        });
    }

    // Show confirmation dialog before performing the delete operation
    private void showConfirmationDialog(final int employeeId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Employee");
        builder.setMessage("Are you sure you want to delete this employee?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Call the deleteEmployee method from DBHelper
                boolean isDeleted = dbHelper.deleteEmployee(employeeId);

                // Show appropriate message based on the result
                if (isDeleted) {
                    Toast.makeText(DeleteEmployeeActivity.this, "Employee deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DeleteEmployeeActivity.this, "Failed to delete employee. Check if the Employee ID is correct.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
