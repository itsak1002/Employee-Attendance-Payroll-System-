package com.example.empapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.empapp.R;
import com.example.empapp.model.LeaveRequest;

import java.util.List;

public class LeaveRequestAdapter extends BaseAdapter {

    private final Context context;
    private final List<LeaveRequest> leaveRequests;

    public LeaveRequestAdapter(Context context, List<LeaveRequest> leaveRequests) {
        this.context = context;
        this.leaveRequests = leaveRequests;
    }

    @Override
    public int getCount() {
        return leaveRequests == null ? 0 : leaveRequests.size();
    }

    @Override
    public LeaveRequest getItem(int position) {
        return leaveRequests != null && position >= 0 && position < leaveRequests.size()
                ? leaveRequests.get(position)
                : null;
    }

    @Override
    public long getItemId(int position) {
        LeaveRequest request = getItem(position);
        return request != null ? request.getId() : -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.leave_request_item, parent, false);

            // Initialize ViewHolder and cache the views
            holder = new ViewHolder();
            holder.tvLeaveDate = convertView.findViewById(R.id.tvLeaveDate);
            holder.tvLeaveReason = convertView.findViewById(R.id.tvLeaveReason);
            holder.tvLeaveStatus = convertView.findViewById(R.id.tvStatus);
            holder.tvRequestedOn = convertView.findViewById(R.id.tvRequestedOn);  // New field for displaying requested on

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current leave request
        LeaveRequest request = getItem(position);

        if (request != null) {
            holder.tvLeaveDate.setText("Date: " + request.getLeaveDate());
            holder.tvLeaveReason.setText("Reason: " + request.getLeaveReason());
            holder.tvLeaveStatus.setText("Status: " + request.getStatus());
            holder.tvRequestedOn.setText("Requested On: " + request.getRequestedOn());  // Display the requestedOn field
        } else {
            // Clear views if the data is null
            holder.tvLeaveDate.setText("");
            holder.tvLeaveReason.setText("");
            holder.tvLeaveStatus.setText("");
            holder.tvRequestedOn.setText("");
        }

        return convertView;
    }

    // ViewHolder class to cache the views
    static class ViewHolder {
        TextView tvLeaveDate;
        TextView tvLeaveReason;
        TextView tvLeaveStatus;
        TextView tvRequestedOn;  // New TextView for requested on
    }
}
