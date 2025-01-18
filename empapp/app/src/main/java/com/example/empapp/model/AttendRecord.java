package com.example.empapp.model;

public class AttendRecord {
    private int employeeId;
    private String date;
    private String checkInTime;
    private String checkOutTime;

    // Constructor with parameters
    public AttendRecord(int employeeId, String date, String checkInTime, String checkOutTime) {
        this.employeeId = employeeId;
        this.date = date;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    // Getters and setters for all fields
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }

    public String getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(String checkOutTime) { this.checkOutTime = checkOutTime; }
}
