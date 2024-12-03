package com.example.lab2;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;


public class DoctorManagementActivity extends AppCompatActivity {


    EditText etName, etSpeciality;
    Button btnAddOrUpdate, btnDelete;
    ListView lvDoctors;
    Toolbar toolbar;

    DBHelper dbHelper;
    ArrayAdapter<String> adapter;
    ArrayList<String> doctorList;
    int selectedDoctorId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_management);

        etName = findViewById(R.id.et_doctor_name);
        etSpeciality = findViewById(R.id.et_doctor_speciality);
        btnAddOrUpdate = findViewById(R.id.btn_add_or_update);
        btnDelete = findViewById(R.id.btn_delete_doctor);
        lvDoctors = findViewById(R.id.lv_doctors);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);
        doctorList = new ArrayList<>();

        loadDoctors();

        // Enable the back button
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }

        btnAddOrUpdate.setOnClickListener(v -> {
            if (selectedDoctorId == -1) {
                addDoctor();
            } else {
                updateDoctor();
            }
        });

        btnDelete.setOnClickListener(v -> {
            if (selectedDoctorId == -1) {
                Toast.makeText(this, "No doctor selected to delete", Toast.LENGTH_SHORT).show();
                return;
            }
            deleteDoctor(selectedDoctorId);
            resetFields();
        });

        lvDoctors.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = doctorList.get(position);
            String[] parts = selectedItem.split(" - ");
            selectedDoctorId = Integer.parseInt(parts[0]);
            String[] details = parts[1].split(", ");
            etName.setText(details[0]);
            etSpeciality.setText(details[1]);

            btnAddOrUpdate.setText("Update Doctor");
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

    private void addDoctor() {
        String name = etName.getText().toString().trim();
        String speciality = etSpeciality.getText().toString().trim();

        if (name.isEmpty() || speciality.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.insertDoctor(name, speciality);
        if (success) {
            Toast.makeText(this, "Doctor added", Toast.LENGTH_SHORT).show();
            resetFields();
            loadDoctors();
        } else {
            Toast.makeText(this, "Failed to add doctor", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDoctor() {
        String name = etName.getText().toString().trim();
        String speciality = etSpeciality.getText().toString().trim();

        if (name.isEmpty() || speciality.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.updateDoctor(selectedDoctorId, name, speciality);
        if (success) {
            Toast.makeText(this, "Doctor updated", Toast.LENGTH_SHORT).show();
            resetFields();
            loadDoctors();
        } else {
            Toast.makeText(this, "Failed to update doctor", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteDoctor(int doctorId) {
        boolean success = dbHelper.deleteDoctor(doctorId);
        if (success) {
            Toast.makeText(this, "Doctor deleted", Toast.LENGTH_SHORT).show();
            loadDoctors();
        } else {
            Toast.makeText(this, "Failed to delete doctor", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDoctors() {
        doctorList.clear();
        Cursor cursor = dbHelper.getDoctors();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String speciality = cursor.getString(2);
                doctorList.add(id + " - " + name + ", " + speciality);
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, doctorList);
        lvDoctors.setAdapter(adapter);
    }

    private void resetFields() {
        etName.setText("");
        etSpeciality.setText("");
        btnAddOrUpdate.setText("Add Doctor");
        btnDelete.setVisibility(View.GONE);
        selectedDoctorId = -1;
    }

}