package com.example.empapp.Activity.Manager.reports;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.empapp.DatabaseHelper.OfficeLocationDbHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class OfficeLocationReport {

    private Context context;
    private OfficeLocationDbHelper dbHelper;

    public OfficeLocationReport(Context context) {
        this.context = context;
        dbHelper = new OfficeLocationDbHelper(context);
    }

    public void generateOfficeLocationReport() {
        PdfDocument pdfDocument = null;
        FileOutputStream outputStream = null;

        try {
            pdfDocument = new PdfDocument();
            int pageWidth = 595;
            int pageHeight = 842;
            int marginX = 40;
            int marginY = 40;
            int lineSpacing = 20;
            int rowHeight = 25;
            int currentY = marginY + 50;

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            // Draw the report title
            paint.setTextSize(18);
            paint.setFakeBoldText(true);
            canvas.drawText("Office Location Report", (pageWidth / 2) - 80, marginY, paint);

            // Draw table headers
            paint.setTextSize(12);
            paint.setFakeBoldText(true);
            currentY += 30;
            int colXId = marginX;
            int colXLat = colXId + 100;
            int colXLng = colXLat + 150;
            int colXRadius = colXLng + 150;

            canvas.drawText("Office ID", colXId, currentY, paint);
            canvas.drawText("Latitude", colXLat, currentY, paint);
            canvas.drawText("Longitude", colXLng, currentY, paint);
            canvas.drawText("Radius", colXRadius, currentY, paint);

            currentY += 10;
            canvas.drawLine(marginX, currentY, pageWidth - marginX, currentY, paint);
            currentY += rowHeight;

            Cursor cursor = dbHelper.getAllOfficeLocations();

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    if (currentY + rowHeight > pageHeight - marginY) {
                        pdfDocument.finishPage(page);

                        pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
                        page = pdfDocument.startPage(pageInfo);
                        canvas = page.getCanvas();
                        currentY = marginY;

                        canvas.drawText("Office ID", colXId, currentY, paint);
                        canvas.drawText("Latitude", colXLat, currentY, paint);
                        canvas.drawText("Longitude", colXLng, currentY, paint);
                        canvas.drawText("Radius", colXRadius, currentY, paint);

                        currentY += 10;
                        canvas.drawLine(marginX, currentY, pageWidth - marginX, currentY, paint);
                        currentY += rowHeight;
                    }

                    int officeId = cursor.getInt(cursor.getColumnIndex(OfficeLocationDbHelper.COLUMN_OFFICE_ID));
                    double officeLat = cursor.getDouble(cursor.getColumnIndex(OfficeLocationDbHelper.COLUMN_OFFICE_LAT));
                    double officeLng = cursor.getDouble(cursor.getColumnIndex(OfficeLocationDbHelper.COLUMN_OFFICE_LNG));
                    float radius = cursor.getFloat(cursor.getColumnIndex(OfficeLocationDbHelper.COLUMN_RADIUS));

                    canvas.drawText(String.valueOf(officeId), colXId, currentY, paint);
                    canvas.drawText(String.valueOf(officeLat), colXLat, currentY, paint);
                    canvas.drawText(String.valueOf(officeLng), colXLng, currentY, paint);
                    canvas.drawText(String.valueOf(radius), colXRadius, currentY, paint);

                    currentY += rowHeight;

                } while (cursor.moveToNext());

                cursor.close();
            }

            pdfDocument.finishPage(page);

            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }

            File pdfFile = new File(directory, "Office_Location_Report.pdf");
            outputStream = new FileOutputStream(pdfFile);
            pdfDocument.writeTo(outputStream);

            Toast.makeText(context, "Office location report generated successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("OfficeLocationReport", "Error generating office location report", e);
            Toast.makeText(context, "Error generating office location report", Toast.LENGTH_SHORT).show();
        } finally {
            if (pdfDocument != null) {
                pdfDocument.close();
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e("OfficeLocationReport", "Error closing output stream", e);
                }
            }
        }
    }
}