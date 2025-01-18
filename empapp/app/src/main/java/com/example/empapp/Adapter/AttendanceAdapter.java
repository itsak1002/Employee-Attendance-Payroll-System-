package com.example.empapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.empapp.R;
import com.example.empapp.model.AttendanceRecord;

import java.util.List;

public class AttendanceAdapter extends BaseAdapter {

    private Context context;
    private List<AttendanceRecord> attendanceList;

    public AttendanceAdapter(Context context, List<AttendanceRecord> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @Override
    public int getCount() {
        return attendanceList.size();
    }

    @Override
    public Object getItem(int position) {
        return attendanceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return attendanceList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout for each item
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.attendance_item, parent, false);
        }

        // Get the current attendance record
        AttendanceRecord record = attendanceList.get(position);

        // Set the data in the views
        TextView dateTextView = convertView.findViewById(R.id.date_text_view);
        TextView checkInTextView = convertView.findViewById(R.id.check_in_text_view);
        TextView checkOutTextView = convertView.findViewById(R.id.check_out_text_view);
        TextView statusTextView = convertView.findViewById(R.id.status_text_view);
        TextView workingHoursTextView = convertView.findViewById(R.id.working_hours_text_view);  // New TextView for working hours

        dateTextView.setText(record.getDate());
        checkInTextView.setText(record.getCheckInTime());
        checkOutTextView.setText(record.getCheckOutTime());
        statusTextView.setText(record.getStatus());
        workingHoursTextView.setText(record.getWorkingHours());  // Set the working hours

        return convertView;
    }
}
