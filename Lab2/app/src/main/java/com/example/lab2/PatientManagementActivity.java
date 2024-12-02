package com.example.lab2;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class PatientManagementActivity extends AppCompatActivity {

    EditText etName, etAge, etGender;
    Button btnAddOrUpdate, btnDelete;
    ListView lvPatients;

    Toolbar toolbar;

    DBHelper dbHelper;
    ArrayAdapter<String> adapter;
    ArrayList<String> patientList;
    int selectedPatientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_management);

        etName = findViewById(R.id.et_patient_name);
        etAge = findViewById(R.id.et_patient_age);
        etGender = findViewById(R.id.et_patient_gender);
        btnAddOrUpdate = findViewById(R.id.btn_add_patient);
        btnDelete = findViewById(R.id.btn_delete_patient);
        lvPatients = findViewById(R.id.lv_patients);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);
        patientList = new ArrayList<>();

        loadPatients();

        // Enable the back button
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }

        // Add or update patient functionality
        btnAddOrUpdate.setOnClickListener(v -> {
            if (selectedPatientId == -1) {
                addPatient();
            } else {
                updatePatient();
            }
        });

        // Delete patient
        btnDelete.setOnClickListener(v -> {
            if (selectedPatientId == -1) {
                Toast.makeText(this, "No patient selected to delete", Toast.LENGTH_SHORT).show();
                return;
            }
            deletePatient(selectedPatientId);
            resetFields();
        });

        // Handle item click to load patient details for update and delete
        lvPatients.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = patientList.get(position);
            String[] parts = selectedItem.split(" - ");
            selectedPatientId = Integer.parseInt(parts[0]);
            String[] details = parts[1].split(", ");
            etName.setText(details[0]); // Name
            etAge.setText(details[1].split(" ")[0]);
            etGender.setText(details[2]); // Gender

            btnAddOrUpdate.setText("Update Patient");
            btnDelete.setVisibility(View.VISIBLE);

        });

        // Handle long press for delete
        lvPatients.setOnItemLongClickListener((parent, view, position, id) -> {
            String selectedItem = patientList.get(position);
            int patientId = Integer.parseInt(selectedItem.split(" - ")[0]);
            deletePatient(patientId);
            return true;
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


    private void addPatient() {
        String name = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String gender = etGender.getText().toString().trim();

        if (name.isEmpty() || ageStr.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        boolean success = dbHelper.insertPatient(name, age, gender);
        if (success) {
            Toast.makeText(this, "Patient added", Toast.LENGTH_SHORT).show();
            resetFields();
            loadPatients();
        } else {
            Toast.makeText(this, "Failed to add patient", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePatient() {
        String name = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String gender = etGender.getText().toString().trim();

        if (name.isEmpty() || ageStr.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        boolean success = dbHelper.updatePatient(selectedPatientId, name, age, gender);
        if (success) {
            Toast.makeText(this, "Patient updated", Toast.LENGTH_SHORT).show();
            resetFields();
            loadPatients();
        } else {
            Toast.makeText(this, "Failed to update patient", Toast.LENGTH_SHORT).show();
        }
    }

    private void deletePatient(int patientId) {
        boolean success = dbHelper.deletePatient(patientId);
        if (success) {
            Toast.makeText(this, "Patient deleted", Toast.LENGTH_SHORT).show();
            loadPatients();
        } else {
            Toast.makeText(this, "Failed to delete patient", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPatients() {
        patientList.clear();
        Cursor cursor = dbHelper.getPatients();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int age = cursor.getInt(2);
                String gender = cursor.getString(3);
                patientList.add(id + " - " + name + ", " + age + " years, " + gender);
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, patientList);
        lvPatients.setAdapter(adapter);
    }

    private void resetFields() {
        etName.setText("");
        etAge.setText("");
        etGender.setText("");
        btnAddOrUpdate.setText("Add Patient");
        btnDelete.setVisibility(View.GONE);
        selectedPatientId = -1;
    }
}


