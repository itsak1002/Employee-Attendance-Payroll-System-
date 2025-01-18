package com.example.empapp.Activity.Employee;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.empapp.model.Employee;
import com.example.empapp.Activity.Employee.Employee.PaySlipActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GeneratePayslipPdf {

    private final Context context;
    private final PaySlipActivity paySlipActivity;

    public GeneratePayslipPdf(Context context, PaySlipActivity paySlipActivity) {
        this.context = context;
        this.paySlipActivity = paySlipActivity;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void generatePdf(int month, int year, Uri fileUri) {
        PdfDocument document = null;
        OutputStream outputStream = null;

        try {
            // Create a new PdfDocument
            document = new PdfDocument();

            // Define page size (A4: 595 x 842 pixels)
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 642, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);

            // Set up Canvas and Paint
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);

            // Draw Title Section
            paint.setTextSize(25);
            paint.setFakeBoldText(true);
            canvas.drawText("Employee Payslip" +"  : - "+ getMonthName(month) + " " + year, 80, 80, paint);

            // Draw a line below the title
            paint.setStrokeWidth(2);
            canvas.drawLine(50, 100, 550, 100, paint);


            // Fetch employee details
            int employeeId = paySlipActivity.getEmployeeIdFromSession();
            Employee employee = paySlipActivity.getEmployeeDetails(employeeId);

            if (employee == null) {
                Toast.makeText(context, "Employee details not found", Toast.LENGTH_SHORT).show();
                return;
            }

            // Employee Information Section
            paint.setTextSize(14);
            paint.setFakeBoldText(false);
            int yOffset = 120;
            canvas.drawText("Employee Information", 50, yOffset, paint);
            yOffset += 20;
            String firstName = validateField(employee.firstName, "N/A");
            String lastName = validateField(employee.lastName, "N/A");
            String designation = validateField(employee.designation, "N/A");
            String username = validateField(employee.username, "N/A");
            String address = validateField(employee.lastName, "N/A");

            canvas.drawText("Name: " + username , 50, yOffset, paint);
            yOffset += 20;
            canvas.drawText("Designation: " + firstName, 50, yOffset, paint);
            yOffset += 20;
            canvas.drawText("Username: " + designation, 50, yOffset, paint);
            yOffset += 20;
            canvas.drawText("Address: " + lastName, 50, yOffset, paint);
            yOffset += 30;
// Working Hours Section
            String workingHours = paySlipActivity.calculateWorkingHoursForMonth(month, year);
            canvas.drawText("Working Hours for " + getMonthName(month) + " " + year, 50, yOffset, paint);
            yOffset += 20;
            canvas.drawText("Total Hours Worked: " + validateField(workingHours, "0:00"), 50, yOffset, paint);
            yOffset += 20;
            // Set up Paint for a dotted line
            paint.setStrokeWidth(2);
            paint.setPathEffect(new android.graphics.DashPathEffect(new float[]{10, 10}, 0)); // 10px dash, 10px gap

// Draw the dotted line
            canvas.drawLine(50, yOffset, 550, yOffset, paint);

// Reset PathEffect for subsequent drawing
            paint.setPathEffect(null);
            yOffset += 40;


            // Salary Details Section
            String[] paymentDetails = paySlipActivity.calculateTotalPayment(month, year).split("\n");
            paint.setFakeBoldText(true);
            canvas.drawText("Salary Details for " + getMonthName(month) + " " + year, 50, yOffset, paint);
            yOffset += 20; paint.setFakeBoldText(false);
            for (String detail : paymentDetails) {
                yOffset += 20;
                canvas.drawText(detail, 50, yOffset, paint);
            }
            yOffset += 40;


            // Footer Section
            paint.setStrokeWidth(2);
            canvas.drawLine(50, yOffset, 550, yOffset, paint);
            yOffset += 20;

            String currentDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    .format(Calendar.getInstance().getTime());
            canvas.drawText("Generated on: " + currentDate, 50, yOffset, paint);

            // Finish the page
            document.finishPage(page);

            // Write the PDF to the output stream
            outputStream = context.getContentResolver().openOutputStream(fileUri);
            if (outputStream == null) {
                throw new IOException("Unable to open output stream for URI: " + fileUri);
            }
            document.writeTo(outputStream);

            // Inform the user
            Toast.makeText(context, "Payslip generated successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("GeneratePayslipPdf", "Error generating PDF", e);
            Toast.makeText(context, "Error generating payslip", Toast.LENGTH_SHORT).show();
        } finally {
            // Ensure resources are closed
            if (document != null) {
                document.close();
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e("GeneratePayslipPdf", "Error closing output stream", e);
                }
            }
        }
    }

    // Utility to validate fields
    private String validateField(String field, String defaultValue) {
        return (field != null && !field.isEmpty()) ? field : defaultValue;
    }

    // Utility to get month name
    private String getMonthName(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        return new SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.getTime());
    }
}
