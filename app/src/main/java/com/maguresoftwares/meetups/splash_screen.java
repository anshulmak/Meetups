package com.maguresoftwares.meetups;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class splash_screen extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1200 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            
            @Override
            public void run() {
                
                Intent i = new Intent(splash_screen.this, Login_Activity.class);
                startActivity(i);
                
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
