package com.example.exkostadmin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_newlogin);

// get the reference of Button's

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, new LogFirstFragment());
        fragmentTransaction.commit(); // save the changes

        Intent intent = getIntent();
        String extras = intent.getStringExtra("reset");
        if (extras != null){
            if (extras.equals("sukses")){
                Toast.makeText(LoginActivity.this, "Reset Password sukses",
                        Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(LoginActivity.this, "Reset Password gagal",
                        Toast.LENGTH_LONG).show();
            }
        }

    }


    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

}
