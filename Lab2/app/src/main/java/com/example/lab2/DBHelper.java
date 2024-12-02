package com.example.lab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "HospitalsDB.db";

    // Table names
    private static final String TABLE_NAME = "users";
    private static final String TABLE_PATIENTS = "patients";

    // User Table columns
    private static final String COL_ID = "id";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";

    // Patient table column
    private static final String COL_PATIENT_ID = "id";
    private  static  final String COL_PATIENT_NAME = "name";
    private static final String COL_PATIENT_AGE = "age";
    private static final String COL_PATIENT_GENDER = "gender";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the users table
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT UNIQUE, " +
                "password TEXT)";
        db.execSQL(createUsersTable);

        // Create the patients table
        String createPatientsTable = "CREATE TABLE IF NOT EXISTS patients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "age INTEGER, " +
                "gender TEXT)";
        db.execSQL(createPatientsTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        onCreate(db);
    }

    // Insert user data
    public boolean insertUserData(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Check if email exists
    public boolean checkUserEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Validate email and password
    public boolean checkUserPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?", new String[]{email, password});
        boolean valid = cursor.getCount() > 0;
        cursor.close();
        return valid;
    }

    public boolean resetUserPassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PASSWORD, newPassword);
        int result = db.update(TABLE_NAME, contentValues, COL_EMAIL + " = ?", new String[]{email}); // Match by email

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
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PATIENT_NAME, name);
        values.put(COL_PATIENT_AGE, age);
        values.put(COL_PATIENT_GENDER, gender);

        long result = db.insert(TABLE_PATIENTS, null, values);
        return result != -1;
    }

    // Get all patients
    public Cursor getPatients() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PATIENTS, null);
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
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PATIENTS, COL_PATIENT_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}
