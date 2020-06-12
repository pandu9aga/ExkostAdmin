package com.example.exkostadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class OpenActivity extends AppCompatActivity{

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(OpenActivity.this);

        if (sessionManager.isLogin() == true){
            Intent main = new Intent(OpenActivity.this, HomeActivity.class);
            startActivity(main);
        }else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

}

