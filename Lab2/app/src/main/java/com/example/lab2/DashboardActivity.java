package com.example.lab2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.Manifest;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // check permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        // create a notification channel
        NotificationHelper.createNotificationChannel(this);

        LinearLayout btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> showLogoutConfirmation());

        // Patient Management Card
        findViewById(R.id.cardPatientManagement).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, PatientManagementActivity.class);
            startActivity(intent);
        });

        // Doctor Management Card
        findViewById(R.id.cardDoctorManagement).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, DoctorManagementActivity.class);
            startActivity(intent);
        });

        // Appointment Management Card
        findViewById(R.id.cardAppointmentScheduling).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AppointmentManagementActivity.class);
            startActivity(intent);
        });

        // Notification Card
        findViewById(R.id.cardNotifications).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

        // Settings Card
        findViewById(R.id.cardSettings).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    // show dialog confirmation
    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> logout())
                .setNegativeButton("No", null)
                .show();
    }

    // logout user
    private void logout() {
        // Clear SharedPreferences or session data
        SharedPreferences preferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to LoginActivity
        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}
