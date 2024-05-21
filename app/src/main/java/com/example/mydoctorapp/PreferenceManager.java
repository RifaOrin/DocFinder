package com.example.mydoctorapp;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREF_NAME = "user_info";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER_ME = "remember_me";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUser(String phoneNumber, String password) {
        editor.putString(KEY_PHONE_NUMBER, phoneNumber);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public void rememberMe(boolean state) {
        editor.putBoolean(KEY_REMEMBER_ME, state);
        editor.apply();
    }

    public String getPhoneNumber() {return sharedPreferences.getString(KEY_PHONE_NUMBER, null);}

    public String getPassword() {return sharedPreferences.getString(KEY_PASSWORD, null);}

    public boolean getRememberMe() {
        return sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
    }

    public boolean hasPhoneNumber() {
        return sharedPreferences.contains(KEY_PHONE_NUMBER);
    }

    public boolean hasPassword(){
        return sharedPreferences.contains(KEY_PASSWORD);
    }


    public void clearUser() {
        editor.remove(KEY_PHONE_NUMBER);
        editor.remove(KEY_PASSWORD);
        editor.apply();
    }
}
