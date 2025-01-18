package com.example.empapp.Activity.Employee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.Activity.Employee.Employee.PaySlipActivity;
import com.example.empapp.R;

public class GeneratePayslipActivity extends AppCompatActivity {

    private static final int CREATE_FILE_REQUEST_CODE = 100;

    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private Button generatePayslipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_payslip);

        monthSpinner = findViewById(R.id.spinner_month);
        yearSpinner = findViewById(R.id.spinner_year);
        generatePayslipButton = findViewById(R.id.btn_generate_payslip);

        generatePayslipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the required fields are selected
                generatePayslip();
            }
        });
    }

    private void generatePayslip() {
        int selectedMonth = monthSpinner.getSelectedItemPosition(); // 0 - January, 1 - February, etc.
        int selectedYear = Integer.parseInt(yearSpinner.getSelectedItem().toString());

        // Log selected month and year for debugging
        Log.d("GeneratePayslipActivity", "Selected Month: " + selectedMonth + ", Selected Year: " + selectedYear);

        // Ensure selected month and year are valid
        if (selectedMonth != -1 && selectedYear != -1) {
            // Request the user to choose a location to save the PDF file
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_TITLE, "Payslip_" + (selectedMonth + 1) + "_" + selectedYear + ".pdf");
            startActivityForResult(intent, CREATE_FILE_REQUEST_CODE);
        } else {
            Toast.makeText(GeneratePayslipActivity.this, "Please select a valid month and year", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                // Generate the payslip and save it to the selected location
                GeneratePayslipPdf generatePayslipPdf = new GeneratePayslipPdf(GeneratePayslipActivity.this, new PaySlipActivity(GeneratePayslipActivity.this));
                int selectedMonth = monthSpinner.getSelectedItemPosition() + 1; // 0-indexed month, so +1
                int selectedYear = Integer.parseInt(yearSpinner.getSelectedItem().toString());
                generatePayslipPdf.generatePdf(selectedMonth, selectedYear, fileUri);
            } else {
                Toast.makeText(this, "Error selecting file location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
