package com.example.exkostadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Rekening extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekening);
    }

    public void pindahcheckoutlagi(View v){
        Intent i = new Intent(Rekening.this, CheckoutActivity.class);
        startActivity(i);
    }
}
