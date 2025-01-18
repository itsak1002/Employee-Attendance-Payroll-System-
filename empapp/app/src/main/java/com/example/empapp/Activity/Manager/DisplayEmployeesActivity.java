package com.example.empapp.Activity.Manager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.DatabaseHelper.DBHelper;
import com.example.empapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayEmployeesActivity extends AppCompatActivity {

    private ListView lvEmployees;
    private DBHelper dbHelper;
    private ArrayList<HashMap<String, String>> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_employees);

        // Initialize views and helper class
        lvEmployees = findViewById(R.id.lvEmployees);
        dbHelper = new DBHelper(this);

        // Load data into the ListView
        loadEmployeeData();
    }

    private void loadEmployeeData() {
        employeeList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllEmployees();

        if (cursor.moveToFirst()) {
            do {
                // Create a HashMap for each employee
                HashMap<String, String> employee = new HashMap<>();
                employee.put("id", "ID: " + cursor.getInt(cursor.getColumnIndex("employee_id")));
                employee.put("name", cursor.getString(cursor.getColumnIndex("first_name")) + " " + cursor.getString(cursor.getColumnIndex("last_name")));
                employee.put("designation", "Designation: " + cursor.getString(cursor.getColumnIndex("designation")));
                employee.put("address", "Address: " + cursor.getString(cursor.getColumnIndex("address")));

                // Add the employee details to the list
                employeeList.add(employee);
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (employeeList.isEmpty()) {
            Toast.makeText(this, "No Employees Found", Toast.LENGTH_SHORT).show();
        } else {
            // Bind the data to the ListView using a SimpleAdapter
            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    employeeList,
                    android.R.layout.simple_list_item_2,  // layout for displaying each list item
                    new String[]{"name", "id"},            // fields to display
                    new int[]{android.R.id.text1, android.R.id.text2} // TextView IDs in simple_list_item_2
            );
            lvEmployees.setAdapter(adapter);
        }
    }
}
