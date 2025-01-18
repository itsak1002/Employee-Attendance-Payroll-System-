package com.example.empapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.empapp.R;
import com.example.empapp.model.AttendRecord;

import java.util.List;

public class AttendanceAdapter2 extends ArrayAdapter<AttendRecord> {

    private Context context;

    public AttendanceAdapter2(Context context, List<AttendRecord> attendanceRecords) {
        super(context, 0, attendanceRecords);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.attendance_item2, parent, false);
            holder = new ViewHolder();
            holder.employeeId = convertView.findViewById(R.id.employeeId);
            holder.inTime = convertView.findViewById(R.id.inTime);
            holder.outTime = convertView.findViewById(R.id.outTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AttendRecord record = getItem(position);
        if (record != null) {
            holder.employeeId.setText(String.valueOf(record.getEmployeeId()));
            holder.inTime.setText(record.getCheckInTime());
            holder.outTime.setText(record.getCheckOutTime());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView employeeId;
        TextView inTime;
        TextView outTime;
    }
}
