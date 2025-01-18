package com.example.empapp.Activity.Manager.reports;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.empapp.DatabaseHelper.AttendanceDbHelper;

import java.io.FileNotFoundException;
import java.io.OutputStream;

public class DateWiseAttendanceReport {

    private final Context context;
    private final AttendanceDbHelper dbHelper;

    public DateWiseAttendanceReport(Context context) {
        this.context = context;
        this.dbHelper = new AttendanceDbHelper(context);
    }

    public void generateReport(String startDate, String endDate, Integer employeeId, Uri fileUri) {
        PdfDocument pdfDocument = null;
        OutputStream outputStream = null;

        try {
            // Initialize the PDF document
            pdfDocument = new PdfDocument();
            int pageWidth = 595; // A4 page width in points
            int pageHeight = 842; // A4 page height in points
            int marginX = 40;
            int marginY = 40;
            int cellHeight = 25; // Height for each row
            int headerHeight = 30; // Height for header row
            int tableWidth = pageWidth - 2 * marginX; // Table width within margins
            int[] columnWidths = {50, 70, 70, 70, 70, 80, 80}; // Column widths in points

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            // Title
            paint.setTextSize(22);
            paint.setFakeBoldText(true);
            canvas.drawText("Date-wise Attendance Report", (pageWidth / 2) - 120, marginY, paint);

            // Subtitle (Date range)
            paint.setTextSize(14);
            canvas.drawText("Date Range: " + startDate + " to " + endDate, marginX, marginY + 30, paint);

            // Draw table headers
            int currentY = marginY + 60;
            paint.setTextSize(12);
            paint.setFakeBoldText(true);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(marginX, currentY, marginX + tableWidth, currentY + headerHeight, paint);

            int currentX = marginX;
            String[] headers = {"ID", "Emp ID", "Date", "Check-In", "Check-Out", "Status", "Working Hours"};
            for (int i = 0; i < headers.length; i++) {
                paint.setStyle(Paint.Style.FILL);
                canvas.drawText(headers[i], currentX + 5, currentY + 20, paint);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawLine(currentX, currentY, currentX, currentY + headerHeight, paint);
                currentX += columnWidths[i];
            }
            canvas.drawLine(currentX, currentY, currentX, currentY + headerHeight, paint);
            currentY += headerHeight;

            // Query the database
            Cursor cursor;
            if (employeeId == null) {
                cursor = dbHelper.getAttendanceForSpecificDayForReport(startDate, endDate);
            } else {
                cursor = dbHelper.getAttendanceForSpecificDayWithIdForReport(employeeId, startDate, endDate);
            }

            // Fill the table with data
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    if (currentY + cellHeight > pageHeight - marginY) {
                        // Add a new page if the content exceeds current page
                        pdfDocument.finishPage(page);
                        pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
                        page = pdfDocument.startPage(pageInfo);
                        canvas = page.getCanvas();
                        currentY = marginY;
                    }

                    currentX = marginX;
                    String[] rowData = {
                            cursor.getString(cursor.getColumnIndexOrThrow(AttendanceDbHelper.COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(AttendanceDbHelper.COLUMN_EMPLOYEE_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(AttendanceDbHelper.COLUMN_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(AttendanceDbHelper.COLUMN_CHECK_IN_TIME)) != null ?
                                    cursor.getString(cursor.getColumnIndexOrThrow(AttendanceDbHelper.COLUMN_CHECK_IN_TIME)) : "N/A",
                            cursor.getString(cursor.getColumnIndexOrThrow(AttendanceDbHelper.COLUMN_CHECK_OUT_TIME)) != null ?
                                    cursor.getString(cursor.getColumnIndexOrThrow(AttendanceDbHelper.COLUMN_CHECK_OUT_TIME)) : "N/A",
                            cursor.getString(cursor.getColumnIndexOrThrow(AttendanceDbHelper.COLUMN_STATUS)) != null ?
                                    cursor.getString(cursor.getColumnIndexOrThrow(AttendanceDbHelper.COLUMN_STATUS)) : "N/A",
                            cursor.getString(cursor.getColumnIndexOrThrow(AttendanceDbHelper.COLUMN_WORKING_HOURS)) != null ?
                                    cursor.getString(cursor.getColumnIndexOrThrow(AttendanceDbHelper.COLUMN_WORKING_HOURS)) : "N/A"
                    };

                    for (int i = 0; i < rowData.length; i++) {
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawText(rowData[i], currentX + 5, currentY + 20, paint);
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawLine(currentX, currentY, currentX, currentY + cellHeight, paint);
                        currentX += columnWidths[i];
                    }
                    canvas.drawLine(currentX, currentY, currentX, currentY + cellHeight, paint);
                    currentY += cellHeight;
                } while (cursor.moveToNext());
                cursor.close();
            }

            canvas.drawLine(marginX, currentY, marginX + tableWidth, currentY, paint);

            // Finish the last page
            pdfDocument.finishPage(page);

            // Write the PDF to the selected URI
            outputStream = context.getContentResolver().openOutputStream(fileUri);
            if (outputStream == null) {
                throw new FileNotFoundException("Unable to open output stream for URI: " + fileUri);
            }
            pdfDocument.writeTo(outputStream);

            // Notify user of success
            Toast.makeText(context, "Date-wise Attendance Report Generated Successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("AttendanceReport", "Error generating report", e);
            Toast.makeText(context, "Error generating report", Toast.LENGTH_SHORT).show();
        } finally {
            // Close the document and output stream
            if (pdfDocument != null) {
                pdfDocument.close();
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    Log.e("AttendanceReport", "Error closing output stream", e);
                }
            }
        }
    }
}