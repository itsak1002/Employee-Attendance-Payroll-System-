package com.example.empapp.Activity.Manager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.DatabaseHelper.LeaveDBHelper;
import com.example.empapp.R;

public class ManageLeaveActivity extends AppCompatActivity {

    private LeaveDBHelper dbHelper;
    private LinearLayout layoutRequests;
    private Button btnShowAllRequests;
    private boolean showAllRequests = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dashboard);

        dbHelper = new LeaveDBHelper(this);
        layoutRequests = findViewById(R.id.layoutRequests);
        btnShowAllRequests = findViewById(R.id.btnShowAllRequests);

        btnShowAllRequests.setOnClickListener(v -> {
            showAllRequests = !showAllRequests;
            loadRequests();
        });

        loadRequests();
    }

    private void loadRequests() {
        layoutRequests.removeAllViews();
        Cursor cursor;

        if (showAllRequests) {
            cursor = dbHelper.getLeaveRequests();
        } else {
            cursor = dbHelper.getLeaveRequestsByStatus("Pending");  // Fetching only pending requests
        }

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String employeeId = cursor.getString(cursor.getColumnIndexOrThrow("employee_id"));
                String leaveDate = cursor.getString(cursor.getColumnIndexOrThrow("leave_date"));
                String leaveReason = cursor.getString(cursor.getColumnIndexOrThrow("leave_reason"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String requestedOn = cursor.getString(cursor.getColumnIndexOrThrow("requested_on"));  // Added this line

                View requestView = getLayoutInflater().inflate(R.layout.item_leave_request, null);

                TextView tvDetails = requestView.findViewById(R.id.tvDetails);
                TextView tvRequestedOn = requestView.findViewById(R.id.tvRequestedOn);  // Added this TextView

                tvDetails.setText("ID: " + id + "\nEmployee: " + employeeId + "\nDate: " + leaveDate + "\nReason: " + leaveReason + "\nStatus: " + status);
                tvRequestedOn.setText("Requested On: " + requestedOn);  // Displaying the "Requested On" date

                // Remove Approve and Reject buttons for non-pending requests
                if ("Pending".equalsIgnoreCase(status)) {
                    Button btnApprove = requestView.findViewById(R.id.btnApprove);
                    Button btnReject = requestView.findViewById(R.id.btnReject);

                    btnApprove.setOnClickListener(v -> {
                        if (dbHelper.updateLeaveStatus(id, "Approved")) {
                            Toast.makeText(ManageLeaveActivity.this, "Request Approved", Toast.LENGTH_SHORT).show();
                            loadRequests();
                        }
                    });

                    btnReject.setOnClickListener(v -> {
                        if (dbHelper.updateLeaveStatus(id, "Rejected")) {
                            Toast.makeText(ManageLeaveActivity.this, "Request Rejected", Toast.LENGTH_SHORT).show();
                            loadRequests();
                        }
                    });
                } else {
                    Button btnApprove = requestView.findViewById(R.id.btnApprove);
                    Button btnReject = requestView.findViewById(R.id.btnReject);

                    btnApprove.setVisibility(View.GONE);  // Hide button for non-pending requests
                    btnReject.setVisibility(View.GONE);   // Hide button for non-pending requests
                }

                layoutRequests.addView(requestView);
            } while (cursor.moveToNext());
        }
    }
}
