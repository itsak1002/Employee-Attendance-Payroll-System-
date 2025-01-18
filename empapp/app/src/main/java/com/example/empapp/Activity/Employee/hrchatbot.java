package com.example.empapp.Activity.Employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.example.empapp.Adapter.ChatAdapter;
import com.example.empapp.R;
import com.example.empapp.model.Message;

import java.util.ArrayList;
import java.util.List;

public class hrchatbot extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;
    private EditText inputMessage;
    private Button sendButton;
    private Button suggestion1, suggestion2, suggestion3, suggestion4, insertAttendanceButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        inputMessage = findViewById(R.id.inputMessage);
        sendButton = findViewById(R.id.sendButton);
        suggestion1 = findViewById(R.id.suggestion1);
        suggestion2 = findViewById(R.id.suggestion2);
        suggestion3 = findViewById(R.id.suggestion3);
        suggestion4 = findViewById(R.id.suggestion4);
        insertAttendanceButton = findViewById(R.id.btn_insert_attendance);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput(inputMessage.getText().toString().trim());
            }
        });

        suggestion1.setOnClickListener(v -> handleUserInput("Check Attendance"));
        suggestion2.setOnClickListener(v -> handleUserInput("View Payslip"));
        suggestion3.setOnClickListener(v -> handleUserInput("Apply for Leave"));
        suggestion4.setOnClickListener(v -> handleUserInput("How to change password"));

        // Handling the new Insert Attendance button
        insertAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the InsertSampleAttendanceActivity
                Intent intent = new Intent(hrchatbot.this, InsertSampleAttendanceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleUserInput(String userInput) {
        if (!userInput.isEmpty()) {
            messageList.add(new Message(userInput, true));
            inputMessage.setText("");

            String botResponse = getBotResponse(userInput);
            messageList.add(new Message(botResponse, false));

            chatAdapter.notifyDataSetChanged();
            chatRecyclerView.scrollToPosition(messageList.size() - 1);
        }
    }

    private String getBotResponse(String input) {
        switch (input.toLowerCase()) {
            case "hii":
                return "Hello! How can I assist you today?";
            case "check attendance":
                return "You can check your attendance in the Attendance section.";
            case "view payslip":
                return "Payslips are available in the Payroll section.";
            case "apply for leave":
                return "Apply for leave in the Leave Management section.";
            case "how to change password":
                return "You can change your password in the Change Password section.";
            default:
                return "Sorry, I didn't understand that. Try asking about attendance, payslips, or leave.";
        }
    }
}
