package com.example.mydoctorapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserInfoDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserInfo.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_DIVISION = "division";
    private static final String COLUMN_DISTRICT = "district";
    private static final String COLUMN_PASSWORD = "password";

    public UserInfoDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_PHONE_NUMBER + " TEXT PRIMARY KEY, "
                + COLUMN_FIRST_NAME + " TEXT, "
                + COLUMN_LAST_NAME + " TEXT, "
                + COLUMN_DATE_OF_BIRTH + " TEXT, "
                + COLUMN_GENDER + " TEXT, "
                + COLUMN_DIVISION + " TEXT, "
                + COLUMN_DISTRICT + " TEXT, "
                + COLUMN_PASSWORD + " TEXT"
                + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public long insertUser(String phoneNumber, String firstName, String lastName, String dateOfBirth,
                           String gender, String division, String district, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_DATE_OF_BIRTH, dateOfBirth);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_DIVISION, division);
        values.put(COLUMN_DISTRICT, district);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }

    public int updateUser(String phoneNumber, String firstName, String lastName, String dateOfBirth,
                          String gender, String division, String district, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_DATE_OF_BIRTH, dateOfBirth);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_DIVISION, division);
        values.put(COLUMN_DISTRICT, district);
        values.put(COLUMN_PASSWORD, password);
        int result = db.update(TABLE_USERS, values, COLUMN_PHONE_NUMBER + "=?", new String[]{phoneNumber});
        db.close();
        return result;
    }

    public int deleteUser(String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_USERS, COLUMN_PHONE_NUMBER + "=?", new String[]{phoneNumber});
        db.close();
        return result;
    }

    public Cursor selectUser(String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_PHONE_NUMBER + "=?", new String[]{phoneNumber});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor selectAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }
}
