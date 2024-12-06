package com.example.lab2;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DBHelperTest {

    private DBHelper dbHelper;



    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DBHelper(context);

        // Clear the appointments table
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM appointments");
    }

    @Test
    public void testInsertAndRetrieveAppointment() throws Exception {
        // Insert an appointment
        boolean isInserted = dbHelper.insertAppointment(
                "2024-12-12",
                "14:30",
                "Test Reason",
                1, // Patient ID
                1  // Doctor ID
        );
        assertTrue(isInserted);

        // Retrieve and verify the appointment
        Cursor cursor = dbHelper.getAppointments();
        assertNotNull(cursor);

        if (cursor.moveToFirst()) {
            String date = cursor.getString(cursor.getColumnIndex("appointment_date"));
            String time = cursor.getString(cursor.getColumnIndex("appointment_time"));
            String encryptedReason = cursor.getString(cursor.getColumnIndex("appointment_reason"));
            String decryptedReason = dbHelper.decryptReason(encryptedReason);

            assertEquals("2024-12-12", date);
            assertEquals("14:30", time);
            assertEquals("Test Reason", decryptedReason);
        }

        cursor.close();
    }

    @Test
    public void testDecryptReason() throws Exception {
        // Encrypt a reason and save it
        String originalReason = "Test Decrypt";
        String encryptedReason = AESEncryptionHelper.encrypt(originalReason);

        // Decrypt the reason and verify
        String decryptedReason = dbHelper.decryptReason(encryptedReason);
        assertEquals(originalReason, decryptedReason);
    }

    @Test
    public void testInvalidBase64Decryption() {
        String invalidBase64 = "NotAValidBase64String";
        String result = dbHelper.decryptReason(invalidBase64);
        assertEquals("Invalid reason format", result); // Match the updated logic
    }
}

