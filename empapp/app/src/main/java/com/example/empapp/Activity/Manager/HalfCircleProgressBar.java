package com.example.empapp.Activity.Manager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class HalfCircleProgressBar extends View {

    private Paint progressPaint;
    private Paint backgroundPaint;
    private float progress = 0; // Represents progress in percentage
    private int progressColor = 0xFF00FF00; // Green color for progress
    private int backgroundColor = 0xFFDDDDDD; // Light gray background color

    // Constructor
    public HalfCircleProgressBar(Context context) {
        super(context);
        init();
    }

    public HalfCircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HalfCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // Initialize the paint objects
    private void init() {
        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(20);
        progressPaint.setAntiAlias(true); // Smooth out edges

        backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(20);
        backgroundPaint.setAntiAlias(true); // Smooth out edges
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Calculate the dimensions of the arc (half circle)
        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height);

        // Set the rectangle bounds for the half circle
        RectF rectF = new RectF(0, 0, diameter, diameter);

        // Draw the background (empty portion of the half circle)
        canvas.drawArc(rectF, 180, 180, false, backgroundPaint);

        // Calculate the sweep angle based on progress (percentage)
        float sweepAngle = 180f * (progress / 100f); // Progress as a percentage of 180 degrees

        // Draw the progress (filled portion of the half circle)
        canvas.drawArc(rectF, 180, sweepAngle, false, progressPaint);
    }

    // Set progress value (0 to 100)
    public void setProgress(float progress) {
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;
        this.progress = progress;
        invalidate(); // Redraw the view to reflect the new progress value
    }
}
