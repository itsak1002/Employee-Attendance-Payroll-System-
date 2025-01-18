package com.example.empapp.Activity.Employee.Employee;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.empapp.DatabaseHelper.AttendanceDbHelper;
import com.example.empapp.DatabaseHelper.DBHelper;
import com.example.empapp.DatabaseHelper.PaymentDbHelper;
import com.example.empapp.model.Employee;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PaySlipActivity {

    private final AttendanceDbHelper dbHelper;
    private final PaymentDbHelper paymentDbHelper;
    private final Context context;

    public PaySlipActivity(Context context) {
        this.context = context;
        dbHelper = new AttendanceDbHelper(context);
        paymentDbHelper = new PaymentDbHelper(context);
    }

    public int getEmployeeIdFromSession() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("employeeId", -1);
    }

    public String calculateWorkingHoursForMonth(int month, int year) {
        int employeeId = getEmployeeIdFromSession();
        if (employeeId == -1) {
            return "Session expired. Please log in again.";
        }

        String startDate = getFormattedDate(year, month, 1);
        String endDate = getLastDayOfMonth(year, month);
        return calculateWorkingHoursForDateRange(employeeId, startDate, endDate);
    }

    private String calculateWorkingHoursForDateRange(int employeeId, String startDate, String endDate) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + AttendanceDbHelper.COLUMN_WORKING_HOURS +
                " FROM " + AttendanceDbHelper.TABLE_ATTENDANCE +
                " WHERE " + AttendanceDbHelper.COLUMN_EMPLOYEE_ID + " = ? AND " +
                AttendanceDbHelper.COLUMN_DATE + " BETWEEN ? AND ?";

        long totalMinutes = 0;

        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(employeeId), startDate, endDate})) {
            while (cursor.moveToNext()) {
                String workingHours = cursor.getString(cursor.getColumnIndex(AttendanceDbHelper.COLUMN_WORKING_HOURS));
                if (!TextUtils.isEmpty(workingHours)) {
                    totalMinutes += parseWorkingHoursToMinutes(workingHours);
                }
            }
        } catch (Exception e) {
            Log.e("PaySlipActivity", "Error calculating working hours", e);
            return "Error calculating working hours.";
        } finally {
            db.close();
        }

        return formatMinutesToHours(totalMinutes);
    }

    public String calculateTotalPayment(int month, int year) {
        int employeeId = getEmployeeIdFromSession();
        if (employeeId == -1) {
            return "Session expired. Please log in again.";
        }

        String workingHours = calculateWorkingHoursForMonth(month, year);
        if (workingHours.contains("Error") || workingHours.contains("Session expired")) {
            return workingHours;
        }

        long totalMinutes = parseWorkingHoursToMinutes(workingHours);

        PaymentDetails paymentDetails = getPaymentDetailsByEmployeeId(employeeId);
        if (paymentDetails == null) {
            return "Payment details not found for employee.";
        }

        double basicSalary = totalMinutes / 60.0 * paymentDetails.paymentPerHour;
        double da = (basicSalary * paymentDetails.percentDA) / 100;
        double hra = (basicSalary * paymentDetails.percentHRA) / 100;
        double pf = (basicSalary * paymentDetails.percentPF) / 100;

        double totalPayment = basicSalary + da + hra - pf;

        return String.format(Locale.getDefault(),
                "Bank Name: %s\nAccount No: %s\nIFSC Code: %s\nBranch: %s\nPF No: %s\nBasic Salary: %.2f\nDA: %.2f\nHRA: %.2f\nPF: %.2f\nTotal Payment: %.2f",
                paymentDetails.bankName, paymentDetails.accountNo, paymentDetails.ifscCode, paymentDetails.branchName, paymentDetails.pfNumber,
                basicSalary, da, hra, pf, totalPayment);
    }

    public PaymentDetails getPaymentDetailsByEmployeeId(int employeeId) {
        SQLiteDatabase db = paymentDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + PaymentDbHelper.TABLE_PAYMENT + " WHERE " + PaymentDbHelper.COLUMN_EMPLOYEE_ID + " = ?";
        PaymentDetails paymentDetails = null;

        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(employeeId)})) {
            if (cursor.moveToFirst()) {
                paymentDetails = new PaymentDetails(
                        cursor.getString(cursor.getColumnIndex(PaymentDbHelper.COLUMN_BANK_NAME)),
                        cursor.getString(cursor.getColumnIndex(PaymentDbHelper.COLUMN_ACCOUNT_NO)),
                        cursor.getString(cursor.getColumnIndex(PaymentDbHelper.COLUMN_IFSC_CODE)),
                        cursor.getString(cursor.getColumnIndex(PaymentDbHelper.COLUMN_BRANCH_NAME)),
                        cursor.getString(cursor.getColumnIndex(PaymentDbHelper.COLUMN_PF_NUMBER)),
                        cursor.getDouble(cursor.getColumnIndex(PaymentDbHelper.COLUMN_PAYMENT_PER_HOUR)),
                        cursor.getDouble(cursor.getColumnIndex(PaymentDbHelper.COLUMN_PERCENT_DA)),
                        cursor.getDouble(cursor.getColumnIndex(PaymentDbHelper.COLUMN_PERCENT_HRA)),
                        cursor.getDouble(cursor.getColumnIndex(PaymentDbHelper.COLUMN_PERCENT_PF))
                );
            }
        } catch (Exception e) {
            Log.e("PaySlipActivity", "Error retrieving payment details", e);
        } finally {
            db.close();
        }

        return paymentDetails;
    }

    private long parseWorkingHoursToMinutes(String workingHours) {
        try {
            String[] parts = workingHours.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return (hours * 60L) + minutes;
        } catch (NumberFormatException e) {
            Log.e("PaySlipActivity", "Error parsing working hours", e);
            return 0;
        }
    }

    private String getLastDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    }

    private String getFormattedDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    }

    private String formatMinutesToHours(long totalMinutes) {
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

    public static class PaymentDetails {
        public String bankName;
        public String accountNo;
        public String ifscCode;
        public String branchName;
        public String pfNumber;
        double paymentPerHour;
        double percentDA;
        double percentHRA;
        double percentPF;

        public PaymentDetails(String bankName, String accountNo, String ifscCode, String branchName, String pfNumber,
                              double paymentPerHour, double percentDA, double percentHRA, double percentPF) {
            this.bankName = bankName;
            this.accountNo = accountNo;
            this.ifscCode = ifscCode;
            this.branchName = branchName;
            this.pfNumber = pfNumber;
            this.paymentPerHour = paymentPerHour;
            this.percentDA = percentDA;
            this.percentHRA = percentHRA;
            this.percentPF = percentPF;
        }
    }

    public Employee getEmployeeDetails(int employeeId) {
        DBHelper dbHelper = new DBHelper(this.context);
        Employee employeeDetails = null;

        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            Cursor cursor = dbHelper.getEmployeeById(employeeId);

            if (cursor != null && cursor.moveToFirst()) {
                String firstName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMPLOYEE_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMPLOYEE_LAST_NAME));
                String username = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMPLOYEE_USERNAME));
                String designation = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMPLOYEE_DESIGNATION));
                String address = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMPLOYEE_ADDRESS));

                employeeDetails = new Employee(firstName, lastName, username, designation, address);
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employeeDetails;
    }

}
