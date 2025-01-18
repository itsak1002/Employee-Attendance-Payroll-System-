package com.example.empapp.Activity.Manager.reports;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.empapp.DatabaseHelper.DBHelper;

import java.io.OutputStream;

class employee_report {

    private final Context context;
    private final DBHelper dbHelper;

    public employee_report(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    public void generateEmployeeReport(Uri fileUri) {
        PdfDocument pdfDocument = null;
        OutputStream outputStream = null;

        try {
            // Initialize PDF document
            pdfDocument = new PdfDocument();

            int pageWidth = 595;
            int pageHeight = 842;
            int marginX = 40;
            int marginY = 40;
            int lineSpacing = 20;
            int rowHeight = 25;
            int currentY = marginY + 50;

            // Create a new page
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            // Draw the report title
            paint.setTextSize(18);
            paint.setFakeBoldText(true);
            canvas.drawText("Employee Report", (pageWidth / 2) - 80, marginY, paint);

            // Draw table headers
            paint.setTextSize(12);
            paint.setFakeBoldText(true);
            currentY += 30;
            int colXId = marginX;
            int colXName = colXId + 50;
            int colXDesignation = colXName + 150;
            int colXPhone = colXDesignation + 100;
            int colXDOJ = colXPhone + 100;

            canvas.drawText("ID", colXId, currentY, paint);
            canvas.drawText("Name", colXName, currentY, paint);
            canvas.drawText("Designation", colXDesignation, currentY, paint);
            canvas.drawText("Phone", colXPhone, currentY, paint);
            canvas.drawText("Date of Joining", colXDOJ, currentY, paint);

            // Draw a line below the headers
            currentY += 10;
            canvas.drawLine(marginX, currentY, pageWidth - marginX, currentY, paint);

            // Fetch employee data from the database
            Cursor cursor = dbHelper.getAllEmployees();

            if (cursor != null && cursor.moveToFirst()) {
                currentY += rowHeight;

                do {
                    // Check if the current page is full
                    if (currentY + rowHeight > pageHeight - marginY) {
                        pdfDocument.finishPage(page);

                        // Start a new page
                        pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
                        page = pdfDocument.startPage(pageInfo);
                        canvas = page.getCanvas();
                        currentY = marginY;

                        // Redraw headers on the new page
                        canvas.drawText("ID", colXId, currentY, paint);
                        canvas.drawText("Name", colXName, currentY, paint);
                        canvas.drawText("Designation", colXDesignation, currentY, paint);
                        canvas.drawText("Phone", colXPhone, currentY, paint);
                        canvas.drawText("Date of Joining", colXDOJ, currentY, paint);
                        currentY += 10;
                        canvas.drawLine(marginX, currentY, pageWidth - marginX, currentY, paint);
                        currentY += rowHeight;
                    }

                    // Fetch and format employee data
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EMPLOYEE_ID));
                    String firstName = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EMPLOYEE_FIRST_NAME));
                    String lastName = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EMPLOYEE_LAST_NAME));
                    String name = firstName + " " + lastName;
                    String designation = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EMPLOYEE_DESIGNATION));
                    String phone = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EMPLOYEE_PHONE));
                    String doj = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EMPLOYEE_DATE_OF_JOINING));

                    // Write data into the table
                    canvas.drawText(id, colXId, currentY, paint);
                    canvas.drawText(name, colXName, currentY, paint);
                    canvas.drawText(designation, colXDesignation, currentY, paint);
                    canvas.drawText(phone, colXPhone, currentY, paint);
                    canvas.drawText(doj, colXDOJ, currentY, paint);

                    // Move to the next row
                    currentY += rowHeight;

                } while (cursor.moveToNext());

                cursor.close();
            }

            pdfDocument.finishPage(page);

            // Write the PDF to the provided URI
            outputStream = context.getContentResolver().openOutputStream(fileUri);
            if (outputStream == null) {
                throw new Exception("Unable to open output stream for URI: " + fileUri);
            }

            pdfDocument.writeTo(outputStream);

            // Notify user of success
            Toast.makeText(context, "Employee report generated successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("EmployeeReport", "Error generating employee report", e);
            Toast.makeText(context, "Error generating employee report", Toast.LENGTH_SHORT).show();
        } finally {
            // Release resources
            if (pdfDocument != null) {
                pdfDocument.close();
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    Log.e("EmployeeReport", "Error closing output stream", e);
                }
            }
        }
    }
}
