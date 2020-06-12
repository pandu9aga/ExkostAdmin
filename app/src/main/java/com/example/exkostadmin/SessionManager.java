package com.example.exkostadmin;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public Context context;

    public static final String PREF_NAME = "LOGIN";
    public static final String LOGIN_STATUS = "false";
    public static final String ID = "ID";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createSession(String id) {
        editor.putBoolean(LOGIN_STATUS, true);
        editor.putString(ID, id);
        editor.apply();
    }

    public boolean isLogin(){
        return pref.getBoolean(LOGIN_STATUS, false);
    }

    public void logout(){
        editor.clear();
        editor.commit();

        Intent login = new Intent(context, LoginActivity.class);
        context.startActivity(login);
        ((HomeActivity)context).finish();
    }

    public String getIdAkun() {
        return pref.getString(ID, null);
    }

}
