package com.example.empapp.Activity.Manager;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.empapp.DatabaseHelper.PaymentDbHelper;
import com.example.empapp.R;

public class add_payment extends AppCompatActivity {

    private PaymentDbHelper dbHelper;
    private EditText etId, etBankName, etAccountNo, etIfscCode, etBranchName, etPfNumber, etPaymentPerHour, etDa, etHra, etPf;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        dbHelper = new PaymentDbHelper(this);

        // Initialize views
        etId = findViewById(R.id.etEmployeeId);
        etBankName = findViewById(R.id.etBankName);
        etAccountNo = findViewById(R.id.etAccountNo);
        etIfscCode = findViewById(R.id.etIfscCode);
        etBranchName = findViewById(R.id.etBranchName);
        etPfNumber = findViewById(R.id.etPfNumber);
        etPaymentPerHour = findViewById(R.id.etPaymentPerHour);
        etDa = findViewById(R.id.etDa);
        etHra = findViewById(R.id.etHra);
        etPf = findViewById(R.id.etPf);
        tvResult = findViewById(R.id.tvResult);

        Button btnInsert = findViewById(R.id.btnInsert);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnDisplay = findViewById(R.id.btnDisplay);

        // Set up button click listeners
        btnInsert.setOnClickListener(this::insertRecord);
        btnUpdate.setOnClickListener(this::updateRecord);
        btnDelete.setOnClickListener(this::deleteRecord);
        btnDisplay.setOnClickListener(this::displayRecords);
    }

    private void insertRecord(View view) {
        String id = etId.getText().toString();
        String bankName = etBankName.getText().toString();
        String accountNo = etAccountNo.getText().toString();
        String ifscCode = etIfscCode.getText().toString();
        String branchName = etBranchName.getText().toString();
        String pfNumber = etPfNumber.getText().toString();
        String paymentPerHour = etPaymentPerHour.getText().toString();
        String da = etDa.getText().toString();
        String hra = etHra.getText().toString();
        String pf = etPf.getText().toString();

        if (TextUtils.isEmpty(bankName) || TextUtils.isEmpty(accountNo) || TextUtils.isEmpty(paymentPerHour)) {
            Toast.makeText(this, "Bank Name, Account No, and Payment Per Hour are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this, "Please enter the Employee ID", Toast.LENGTH_SHORT).show();
            return;
        }

        long employeeId = Integer.parseInt(id);

        long idInserted = dbHelper.insertPayment(employeeId, bankName, accountNo, ifscCode, branchName, pfNumber,
                Double.parseDouble(paymentPerHour), Double.parseDouble(da), Double.parseDouble(hra), Double.parseDouble(pf));

        Toast.makeText(this, "Inserted record with ID: " + idInserted, Toast.LENGTH_SHORT).show();
    }

    private void updateRecord(View view) {
        String id = etId.getText().toString();
        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this, "Please enter the ID of the record to update", Toast.LENGTH_SHORT).show();
            return;
        }

        String bankName = etBankName.getText().toString();
        String accountNo = etAccountNo.getText().toString();
        String ifscCode = etIfscCode.getText().toString();
        String branchName = etBranchName.getText().toString();
        String pfNumber = etPfNumber.getText().toString();
        String paymentPerHour = etPaymentPerHour.getText().toString();
        String da = etDa.getText().toString();
        String hra = etHra.getText().toString();
        String pf = etPf.getText().toString();

        long employeeId = Long.parseLong(id);  // Convert from String to long

        int rowsUpdated = dbHelper.updatePayment(
                employeeId,    // Use the long employeeId
                bankName,
                accountNo,
                ifscCode,
                branchName,
                pfNumber,
                Double.parseDouble(paymentPerHour),
                Double.parseDouble(da),
                Double.parseDouble(hra),
                Double.parseDouble(pf)
        );

        Toast.makeText(this, rowsUpdated + " record(s) updated", Toast.LENGTH_SHORT).show();
    }


    private void deleteRecord(View view) {
        String id = etId.getText().toString();
        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this, "Please enter the ID of the record to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        int rowsDeleted = dbHelper.deletePayment(Integer.parseInt(id));
        Toast.makeText(this, rowsDeleted + " record(s) deleted", Toast.LENGTH_SHORT).show();
    }

    private void displayRecords(View view) {
        Cursor cursor = dbHelper.getAllPayments();
        if (cursor.getCount() == 0) {
            tvResult.setText("No records found");
            return;
        }

        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()) {
            builder.append("Employee ID: ").append(cursor.getInt(0)).append("\n")
                    .append("Bank Name: ").append(cursor.getString(1)).append("\n")
                    .append("Account No: ").append(cursor.getString(2)).append("\n")
                    .append("IFSC Code: ").append(cursor.getString(3)).append("\n")
                    .append("Branch Name: ").append(cursor.getString(4)).append("\n")
                    .append("PF Number: ").append(cursor.getString(5)).append("\n")
                    .append("Payment Per Hour: ").append(cursor.getDouble(6)).append("\n")
                    .append("DA: ").append(cursor.getDouble(7)).append("\n")
                    .append("HRA: ").append(cursor.getDouble(8)).append("\n")
                    .append("PF: ").append(cursor.getDouble(9)).append("\n\n");
        }
        tvResult.setText(builder.toString());
    }
}
