package com.example.empapp.model;

public class LeaveRequest {

    private int id;
    private int employeeId;
    private String leaveDate;
    private String leaveReason;
    private String status;
    private String requestedOn;  // New field to store the timestamp when the leave request was made

    // Constructor
    public LeaveRequest(int id, int employeeId, String leaveDate, String leaveReason, String status, String requestedOn) {
        this.id = id;
        this.employeeId = employeeId;
        this.leaveDate = leaveDate;
        this.leaveReason = leaveReason;
        this.status = status;
        this.requestedOn = requestedOn;  // Assigning the provided timestamp
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(String requestedOn) {
        this.requestedOn = requestedOn;
    }

    // toString Method for Debugging
    @Override
    public String toString() {
        return "LeaveRequest{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", leaveDate='" + leaveDate + '\'' +
                ", leaveReason='" + leaveReason + '\'' +
                ", status='" + status + '\'' +
                ", requestedOn='" + requestedOn + '\'' +
                '}';
    }
    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);  // Assuming the status string has "pending"
    }
}
