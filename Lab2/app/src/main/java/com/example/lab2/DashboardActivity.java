package com.example.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Patient Management Card
        findViewById(R.id.cardPatientManagement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Patient Management Activity
                Intent intent = new Intent(DashboardActivity.this, PatientManagementActivity.class);
                startActivity(intent);
            }
        });

        // Doctor Management Card
        findViewById(R.id.cardDoctorManagement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Doctor Management Activity
                Intent intent = new Intent(DashboardActivity.this, DoctorManagementActivity.class);
                startActivity(intent);
            }
        });

        // Appointment Management Card
        findViewById(R.id.cardAppointmentScheduling).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Appointment Management Activity
                Intent intent = new Intent(DashboardActivity.this, AppointmentManagementActivity.class);
                startActivity(intent);
            }
        });

    }
}