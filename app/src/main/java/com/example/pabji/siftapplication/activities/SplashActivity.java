package com.example.pabji.siftapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.pabji.siftapplication.R;


public class SplashActivity extends AppCompatActivity {

    public ProgressBar progressBar;
    public static final int seconds = 4;
    public static final int delay = 1;
    public static  final int miliseconds= seconds * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(max_progress());
        startAnimation();
    }

    private int max_progress() {
        return seconds-delay;
    }

    public void startAnimation(){
        new CountDownTimer(miliseconds,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(set_progress(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }


    private int set_progress(long milisegundos) {
        return (int)((miliseconds-milisegundos)/1000);
    }
}
