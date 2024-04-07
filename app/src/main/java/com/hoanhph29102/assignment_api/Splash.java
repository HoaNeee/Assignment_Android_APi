package com.hoanhph29102.assignment_api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Splash extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 5000; // 5 seconds
    private static final int PROGRESS_DELAY = 2000; // 2 seconds

    private ImageView imageView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.animate().translationYBy(-100).setDuration(400).start();
                progressBar.setVisibility(ProgressBar.VISIBLE);
            }
        }, PROGRESS_DELAY);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this,Login.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}