package com.example.empapp.Activity.Employee;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.Adapter.LeaveRequestAdapter;
import com.example.empapp.DatabaseHelper.LeaveDBHelper;
import com.example.empapp.R;
import com.example.empapp.model.LeaveRequest;

import java.util.List;

public class ViewLeaveRequestsActivity extends AppCompatActivity {

    private LeaveDBHelper dbHelper;
    private ListView lvLeaveRequests;
    private TextView tvNoRequests;
    private Button btnDelete;
    private int employeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leave_requests);

        dbHelper = new LeaveDBHelper(this);

        lvLeaveRequests = findViewById(R.id.lvLeaveRequests);
        tvNoRequests = findViewById(R.id.tvNoRequests);
        btnDelete = findViewById(R.id.btnDelete);

        // Retrieve employeeId from session
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        employeeId = sharedPreferences.getInt("employeeId", -1);

        if (employeeId == -1) {
            Toast.makeText(this, "No active session. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            loadLeaveRequests();
        }
    }

    private void loadLeaveRequests() {
        List<LeaveRequest> leaveRequests = dbHelper.getLeaveRequestsByEmployeeId(employeeId);

        if (leaveRequests.isEmpty()) {
            tvNoRequests.setVisibility(View.VISIBLE);
            lvLeaveRequests.setVisibility(View.GONE);
        } else {
            tvNoRequests.setVisibility(View.GONE);
            lvLeaveRequests.setVisibility(View.VISIBLE);

            LeaveRequestAdapter adapter = new LeaveRequestAdapter(this, leaveRequests);
            lvLeaveRequests.setAdapter(adapter);

            lvLeaveRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LeaveRequest selectedRequest = leaveRequests.get(position);
                    if (selectedRequest.isPending()) {
                        btnDelete.setVisibility(View.VISIBLE);  // Show the delete button for pending requests

                        btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dbHelper.deleteLeaveRequest(selectedRequest.getId());
                                Toast.makeText(ViewLeaveRequestsActivity.this, "Request deleted", Toast.LENGTH_SHORT).show();
                                loadLeaveRequests(); // Reload to update the list
                            }
                        });
                    } else {
                        Toast.makeText(ViewLeaveRequestsActivity.this, "Only pending requests can be deleted", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
