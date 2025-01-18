package com.example.empapp.model;

public class AttendanceRecord {
    private int id;
    private String date;
    private String checkInTime;
    private String checkOutTime;
    private String status;
    private String leaveStatus;
    private String leaveReason;
    private String workingHours;  // Added working hours

    // Constructor with 8 parameters (including working hours)
    public AttendanceRecord(int id, String date, String checkInTime, String checkOutTime,
                            String status, String leaveStatus, String leaveReason, String workingHours) {
        this.id = id;
        this.date = date;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
        this.leaveStatus = leaveStatus;
        this.leaveReason = leaveReason;
        this.workingHours = workingHours;
    }

    // Getters and setters for all fields
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }

    public String getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(String checkOutTime) { this.checkOutTime = checkOutTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLeaveStatus() { return leaveStatus; }
    public void setLeaveStatus(String leaveStatus) { this.leaveStatus = leaveStatus; }

    public String getLeaveReason() { return leaveReason; }
    public void setLeaveReason(String leaveReason) { this.leaveReason = leaveReason; }

    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }
}
