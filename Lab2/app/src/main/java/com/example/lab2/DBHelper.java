package com.example.lab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {


    // Database name
    private static final String DATABASE_NAME = "HospitalsDB.db";

    // Table names
    private static final String TABLE_NAME = "users";
    private static final String TABLE_PATIENTS = "patients";
    private static final String TABLE_DOCTORS = "doctors";
    private static final String TABLE_APPOINTMENTS = "appointments";

    // User Table columns
    private static final String COL_ID = "user_id";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";


    // Patient table column
    private static final String COL_PATIENT_ID = "patient_id";
    private  static  final String COL_PATIENT_NAME = "patient_name";
    private static final String COL_PATIENT_AGE = "age";
    private static final String COL_PATIENT_GENDER = "gender";

    // Doctors table column
    private static final String COL_DOCTOR_ID = "doctor_id";
    private static final String COL_DOCTOR_NAME = "doctor_name";
    private static final String COL_DOCTOR_SPECIALITY = "speciality";

    // Appointments table columns
    private static final String COL_APPOINTMENT_ID = "appointment_id";
    private static final String COL_APPOINTMENT_DATE = "appointment_date";
    private static final String COL_APPOINTMENT_TIME = "appointment_time";
    private static final String COL_APPOINTMENT_REASON = "appointment_reason";
    private static final String COL_APPOINTMENT_PATIENT_ID = "patient_id";
    private static final String COL_APPOINTMENT_DOCTOR_ID = "doctor_id";


    private static DBHelper instance;

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the users table
        String createUsersTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);

        // Create the patients table
        String createPatientsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_PATIENTS + " (" +
                COL_PATIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PATIENT_NAME + " TEXT, " +
                COL_PATIENT_AGE + " INTEGER, " +
                COL_PATIENT_GENDER + " TEXT)";
        db.execSQL(createPatientsTable);

        // Create the doctors table
        String createDoctorsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_DOCTORS + " (" +
                COL_DOCTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DOCTOR_NAME + " TEXT, " +
                COL_DOCTOR_SPECIALITY + " TEXT)";
        db.execSQL(createDoctorsTable);

        // Create the appointments table
        String createAppointmentsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_APPOINTMENTS + " (" +
                COL_APPOINTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_APPOINTMENT_DATE + " TEXT, " +
                COL_APPOINTMENT_TIME + " TEXT, " +
                COL_APPOINTMENT_REASON + " TEXT, " +
                COL_APPOINTMENT_PATIENT_ID + " INTEGER, " +
                COL_APPOINTMENT_DOCTOR_ID + " INTEGER, " +
                "FOREIGN KEY (" + COL_APPOINTMENT_PATIENT_ID + ") REFERENCES " + TABLE_PATIENTS + " (" + COL_PATIENT_ID + "), " +
                "FOREIGN KEY (" + COL_APPOINTMENT_DOCTOR_ID + ") REFERENCES " + TABLE_DOCTORS + " (" + COL_DOCTOR_ID + "), " +
                "UNIQUE (" + COL_APPOINTMENT_DATE + ", " + COL_APPOINTMENT_TIME + ", " + COL_APPOINTMENT_PATIENT_ID + ", " + COL_APPOINTMENT_DOCTOR_ID + ") ON CONFLICT IGNORE)";
        db.execSQL(createAppointmentsTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        onCreate(db);
    }


    // Insert user data
    public boolean insertUserData(String email, String password) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            String hashedPassword = PasswordEncryption.hashPassword(password);
            contentValues.put(COL_EMAIL, email);
            contentValues.put(COL_PASSWORD, hashedPassword);

            long result = db.insert(TABLE_NAME, null, contentValues);
            return result != -1;
        }
    }

    // Check if email exists
    public boolean checkUserEmail(String email) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ?", new String[]{email})) {
            return cursor.getCount() > 0;
        }
    }

    // Validate email and password
    public boolean checkUserPassword(String email, String password) {
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            String hashedPassword = PasswordEncryption.hashPassword(password);
            try (Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?", new String[]{email, hashedPassword})) {
                return cursor.getCount() > 0;
            }
        }
    }

    // Reset user password
    public boolean resetUserPassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String hashedPassword = PasswordEncryption.hashPassword(newPassword);
        contentValues.put(COL_PASSWORD, hashedPassword);
        int result = db.update(TABLE_NAME, contentValues, COL_EMAIL + " = ?", new String[]{email});

        if (result > 0) {
            Log.d("DBHelper", "Password reset successfully for Email: " + email);
            db.close();
            return true;
        } else {
            Log.e("DBHelper", "Failed to reset password for Email: " + email);
            db.close();
            return false;
        }
    }

    // Insert patient
    public boolean insertPatient(String name, int age, String gender) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COL_PATIENT_NAME, name);
            values.put(COL_PATIENT_AGE, age);
            values.put(COL_PATIENT_GENDER, gender);

            long result = db.insert(TABLE_PATIENTS, null, values);
            return result != -1;
        }
    }

    // Get all patients
    public Cursor getPatients() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PATIENTS, null);
    }

    // Get all patients
    public List<String> getPatientNames() {
        // initialize an empty list to hold patient names
        List<String> patientNames = new ArrayList<>();
        // open the database in read mode and execute the query to retrieve all patient names
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT " + COL_PATIENT_NAME + " FROM " + TABLE_PATIENTS, null)) {
            // check if the cursor has at least one row
            if (cursor.moveToFirst()) {
                do {
                    // retrieve the patient name from the current row and add it to the list
                    patientNames.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        }
        // if no patient names were retrieved, add a default message to indicate no data is available
        if (patientNames.isEmpty()) {
            patientNames.add("No patients available");
        }
        return patientNames;
    }

    // Update patient
    public boolean updatePatient(int id, String name, int age, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PATIENT_NAME, name);
        values.put(COL_PATIENT_AGE, age);
        values.put(COL_PATIENT_GENDER, gender);

        int result = db.update(TABLE_PATIENTS, values, COL_PATIENT_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Delete patient
    public boolean deletePatient(int id) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            return db.delete(TABLE_PATIENTS, COL_PATIENT_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
        }
    }

    // Insert Doctor
    public boolean insertDoctor(String name, String speciality) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DOCTOR_NAME, name);
        values.put(COL_DOCTOR_SPECIALITY, speciality);
        long result = db.insert(TABLE_DOCTORS, null, values);
        return result != -1;
    }

    // Get All Doctors
    public Cursor getDoctors() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_DOCTORS, null);
    }

    // Get all doctor names
    public List<String> getDoctorNames() {
        List<String> doctorNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_DOCTOR_NAME + " FROM " + TABLE_DOCTORS, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String doctorName = "Dr. " + cursor.getString(0);
                doctorNames.add(doctorName);
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (doctorNames.isEmpty()) {
            doctorNames.add("No doctors available");
        }
        return doctorNames;
    }

    // Update Doctor
    public boolean updateDoctor(int id, String name, String speciality) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DOCTOR_NAME, name);
        values.put(COL_DOCTOR_SPECIALITY, speciality);
        int rows = db.update(TABLE_DOCTORS, values, COL_DOCTOR_ID + " = ?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // Delete Doctor
    public boolean deleteDoctor(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_DOCTORS, COL_DOCTOR_ID + " = ?", new String[]{String.valueOf(id)});
        return rows > 0;
    }



    // check for duplicated appointment
    public boolean isDuplicateAppointment(String date, String time, int patientId, int doctorId) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_APPOINTMENTS +
                             " WHERE " + COL_APPOINTMENT_DATE + " = ? AND " +
                             COL_APPOINTMENT_TIME + " = ? AND " +
                             COL_APPOINTMENT_PATIENT_ID + " = ? AND " +
                             COL_APPOINTMENT_DOCTOR_ID + " = ?",
                     new String[]{date, time, String.valueOf(patientId), String.valueOf(doctorId)})) {
            return cursor.getCount() > 0; // Return true if a duplicate exists
        }
    }



    // insert appointments
    public boolean insertAppointment(String date, String time, String reason, int patientId, int doctorId) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Log the original reason before encryption for debugging purposes
            Log.d("DBHelper", "Original Reason Before Encryption: " + reason);

            ContentValues values = new ContentValues();
            values.put(COL_APPOINTMENT_DATE, date);
            values.put(COL_APPOINTMENT_TIME, time);

            String encryptedReason;
            // Check if the reason is already encrypted (valid Base64 string)
            if (AESEncryptionHelper.isValidBase64(reason)) {
                Log.w("DBHelper", "Reason is already encrypted: " + reason);
                encryptedReason = reason;
            } else {
                // Encrypt the reason if it's not already encrypted
                encryptedReason = AESEncryptionHelper.encrypt(reason);
            }

            // Log the encrypted reason for debugging
            Log.d("DBHelper", "Encrypted Reason: " + encryptedReason);

            // Add the encrypted reason and other details to the ContentValues object
            values.put(COL_APPOINTMENT_REASON, encryptedReason);
            values.put(COL_APPOINTMENT_PATIENT_ID, patientId);
            values.put(COL_APPOINTMENT_DOCTOR_ID, doctorId);

            // Insert the appointment data into the database
            long result = db.insert(TABLE_APPOINTMENTS, null, values);

            // Check the result of the insertion
            if (result != -1) {
                Log.d("DBHelper", "Appointment inserted successfully with ID: " + result);
                return true;
            } else {
                Log.e("DBHelper", "Failed to insert appointment.");
                return false;
            }
        } catch (Exception e) {
            // log any exceptions that occur during the insertion process
            Log.e("DBHelper", "Error inserting appointment: " + e.getMessage());
            return false;
        }
    }

    
    // Get All Appointments
    public Cursor getAppointments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " +
                "a." + COL_APPOINTMENT_ID + ", " +
                "a." + COL_APPOINTMENT_DATE + ", " +
                "a." + COL_APPOINTMENT_TIME + ", " +
                "a." + COL_APPOINTMENT_REASON + ", " +
                "p." + COL_PATIENT_NAME + ", " +
                "'Dr. ' || d." + COL_DOCTOR_NAME +
                " FROM " + TABLE_APPOINTMENTS + " a" +
                " JOIN " + TABLE_PATIENTS + " p ON a." + COL_APPOINTMENT_PATIENT_ID + " = p." + COL_PATIENT_ID +
                " JOIN " + TABLE_DOCTORS + " d ON a." + COL_APPOINTMENT_DOCTOR_ID + " = d." + COL_DOCTOR_ID, null);
    }

    // Update Appointment
    public boolean updateAppointment(int appointmentId, String date, String time, String reason, int patientId, int doctorId) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // create a ContentValues object to store the updated values
            ContentValues values = new ContentValues();
            values.put(COL_APPOINTMENT_DATE, date);
            values.put(COL_APPOINTMENT_TIME, time);
            // encrypt the reason before storing it in the database
            String encryptedReason = AESEncryptionHelper.encrypt(reason);
            values.put(COL_APPOINTMENT_REASON, encryptedReason);
            values.put(COL_APPOINTMENT_PATIENT_ID, patientId);
            values.put(COL_APPOINTMENT_DOCTOR_ID, doctorId);

            // execute the update query and check if any rows were affected
            return db.update(TABLE_APPOINTMENTS, values, COL_APPOINTMENT_ID + " = ?", new String[]{String.valueOf(appointmentId)}) > 0;
        } catch (Exception e) {
            // log any exceptions that occur during update
            Log.e("DBHelper", "Error updating appointment: " + e.getMessage());
            return false;
        }
    }


    // appointment's reason decryption
    public String decryptReason(String encryptedReason) {
        try {
            if (encryptedReason == null || encryptedReason.isEmpty()) {
                return "No reason provided";
            }

            // Use helper method to validate Base64
            if (!AESEncryptionHelper.isValidBase64(encryptedReason)) {
                return "Invalid reason format";
            }

            return AESEncryptionHelper.decrypt(encryptedReason);
        } catch (Exception e) {
            Log.e("DBHelper", "Error decrypting reason: " + e.getMessage());
            return "Error decrypting reason";
        }
    }


    // Delete Appointment
    public boolean deleteAppointment(int appointmentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_APPOINTMENTS, COL_APPOINTMENT_ID + " = ?", new String[]{String.valueOf(appointmentId)});
        return rows > 0;
    }

}