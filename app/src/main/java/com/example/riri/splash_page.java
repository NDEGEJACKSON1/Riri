package com.example.riri;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class splash_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_page_layout);


        Handler handler = new Handler();

        handler.postDelayed(() -> {
            startActivity(new Intent(splash_page.this, home_page.class));
            finish();
        }, 1500);
    }
}