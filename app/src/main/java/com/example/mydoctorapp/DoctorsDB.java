package com.example.mydoctorapp;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DoctorsDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "doctorsList.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    private static final String TABLE_DOCTORS = "doctorsList";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUALIFICATION = "qualification";
    private static final String COLUMN_SPECIALITY = "speciality";
    private static final String COLUMN_FEE = "fee";

    private static final String COLUMN_WORK = "work";
    private static final String COLUMN_EXPERIENCE = "experience";

    public DoctorsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_DOCTORS + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_QUALIFICATION + " TEXT, " +
                COLUMN_SPECIALITY + " TEXT, " +
                COLUMN_FEE + " TEXT, " +
                COLUMN_WORK + " TEXT, " +
                COLUMN_EXPERIENCE + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades here (if needed)
        // For now, we'll just drop and recreate the table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        onCreate(db);
    }

    // Insert a new doctor
    public boolean insertDoctor(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, doctor.getId());
        values.put(COLUMN_NAME, doctor.getName());
        values.put(COLUMN_QUALIFICATION, doctor.getQualification());
        values.put(COLUMN_SPECIALITY, doctor.getSpeciality());
        values.put(COLUMN_FEE, doctor.getFee());
        values.put(COLUMN_WORK, doctor.getWork());
        values.put(COLUMN_EXPERIENCE, doctor.getExperience());
        long result = db.insert(TABLE_DOCTORS, null, values);
        db.close();
        return result != -1; // Returns true if insertion was successful
    }

    // Update an existing doctor
    public boolean updateDoctor(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, doctor.getName());
        values.put(COLUMN_QUALIFICATION, doctor.getQualification());
        values.put(COLUMN_SPECIALITY, doctor.getSpeciality());
        values.put(COLUMN_FEE, doctor.getFee());
        values.put(COLUMN_WORK, doctor.getWork());
        values.put(COLUMN_EXPERIENCE, doctor.getExperience());
        int rowsAffected = db.update(TABLE_DOCTORS, values, COLUMN_ID + " = ?", new String[]{doctor.getId()});
        db.close();
        return rowsAffected > 0; // Returns true if update was successful
    }

    // Retrieve all doctors
    public Cursor selectAllDoctors() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_DOCTORS, null, null, null, null, null, null);
    }

    // Retrieve doctors by speciality
    public Cursor selectDoctorBySpeciality(String speciality) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_DOCTORS, null, COLUMN_SPECIALITY + " = ?", new String[]{speciality}, null, null, null);
    }

    public int getDoctorCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int count = 0;

        try {
            String query = "SELECT COUNT(*) FROM " + TABLE_DOCTORS;
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting doctor count: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return count;
    }
}

