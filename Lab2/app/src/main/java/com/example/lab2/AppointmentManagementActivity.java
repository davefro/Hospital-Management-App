package com.example.lab2;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class AppointmentManagementActivity extends AppCompatActivity {

    Spinner spinnerPatient, spinnerDoctor;
    EditText etDate, etTime, etReason;
    Button btnAdd, btnUpdate, btnDelete;
    ListView lvAppointments;
    Toolbar toolbar;

    DBHelper dbHelper;
    ArrayList<String> appointmentList, patientList, doctorList;
    ArrayAdapter<String> adapter, patientAdapter, doctorAdapter;

    int selectedAppointmentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_management);

        spinnerPatient = findViewById(R.id.spinner_patient);
        spinnerDoctor = findViewById(R.id.spinner_doctor);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);
        etReason = findViewById(R.id.et_reason);
        btnAdd = findViewById(R.id.btn_add);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        lvAppointments = findViewById(R.id.lv_appointments);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);

        patientList = new ArrayList<>();
        doctorList = new ArrayList<>();
        appointmentList = new ArrayList<>();

        loadPatients();
        loadDoctors();
        loadAppointments();

        // Enable the back button
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }


        // Add Appointment
        btnAdd.setOnClickListener(v -> addAppointment());

        // Update Appointment
        btnUpdate.setOnClickListener(v -> {
            if (selectedAppointmentId == -1) {
                Toast.makeText(this, "No appointment selected", Toast.LENGTH_SHORT).show();
            } else {
                updateAppointment();
            }
        });

        // Delete Appointment
        btnDelete.setOnClickListener(v -> {
            if (selectedAppointmentId == -1) {
                Toast.makeText(this, "No appointment selected", Toast.LENGTH_SHORT).show();
            } else {
                deleteAppointment(selectedAppointmentId);
            }
        });

        // Handle item click to load appointment details for update
        lvAppointments.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = appointmentList.get(position);
            String[] parts = selectedItem.split(" - ");
            selectedAppointmentId = Integer.parseInt(parts[0]);

            String[] details = parts[1].split(", ");
            etDate.setText(details[0]);
            etTime.setText(details[1]);
            etReason.setText(details[2]);

            spinnerPatient.setSelection(getSpinnerPosition(patientList, details[3]));
            spinnerDoctor.setSelection(getSpinnerPosition(doctorList, details[4]));

            btnDelete.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addAppointment() {
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();
        String reason = etReason.getText().toString();
        int patientId = spinnerPatient.getSelectedItemPosition() + 1;
        int doctorId = spinnerDoctor.getSelectedItemPosition() + 1;

        if (date.isEmpty() || time.isEmpty() || reason.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.insertAppointment(date, time, reason, patientId, doctorId);
        if (success) {
            Toast.makeText(this, "Appointment added", Toast.LENGTH_SHORT).show();
            resetFields();
            loadAppointments();
        } else {
            Toast.makeText(this, "Failed to add appointment", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAppointment() {
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();
        String reason = etReason.getText().toString();
        int patientId = spinnerPatient.getSelectedItemPosition() + 1;
        int doctorId = spinnerDoctor.getSelectedItemPosition() + 1;

        boolean success = dbHelper.updateAppointment(selectedAppointmentId, date, time, reason, patientId, doctorId);
        if (success) {
            Toast.makeText(this, "Appointment updated", Toast.LENGTH_SHORT).show();
            resetFields();
            loadAppointments();
        } else {
            Toast.makeText(this, "Failed to update appointment", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAppointment(int appointmentId) {
        boolean success = dbHelper.deleteAppointment(appointmentId);
        if (success) {
            Toast.makeText(this, "Appointment deleted", Toast.LENGTH_SHORT).show();
            resetFields();
            loadAppointments();
        } else {
            Toast.makeText(this, "Failed to delete appointment", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPatients() {
        patientList.clear();
        patientList.addAll(dbHelper.getPatientNames());

        patientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patientList);
        patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPatient.setAdapter(patientAdapter);
    }

    private void loadDoctors() {
        doctorList.clear();
        doctorList.addAll(dbHelper.getDoctorNames());

        doctorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, doctorList);
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoctor.setAdapter(doctorAdapter);
    }

    private void loadAppointments() {
        appointmentList.clear();
        Cursor cursor = dbHelper.getAppointments();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                String time = cursor.getString(2);
                String reason = cursor.getString(3);
                String patient = cursor.getString(4);
                String doctor = cursor.getString(5);
                appointmentList.add(id + " - " + date + ", " + time + ", " + reason + ", " + patient + ", " + doctor);
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointmentList);
        lvAppointments.setAdapter(adapter);
    }

    private int getSpinnerPosition(ArrayList<String> list, String value) {
        return list.indexOf(value);
    }

    private void resetFields() {
        etDate.setText("");
        etTime.setText("");
        etReason.setText("");
        spinnerPatient.setSelection(0);
        spinnerDoctor.setSelection(0);
        btnDelete.setVisibility(View.GONE);
        selectedAppointmentId = -1;
    }
}
