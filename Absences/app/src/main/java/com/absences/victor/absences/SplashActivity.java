package com.absences.victor.absences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            Intent activityIntent;

            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("userObject", Context.MODE_PRIVATE);
                String token = prefs.getString("token", "");

                if (token == "") {
                    activityIntent = new Intent(SplashActivity.this, LoginActivity.class);
                } else {
                    activityIntent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(activityIntent);
                finish();
            }
        }, 0);
    }
}