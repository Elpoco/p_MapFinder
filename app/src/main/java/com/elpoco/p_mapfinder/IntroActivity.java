package com.elpoco.p_mapfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    Timer timer=new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        loadData();
        timer.schedule(task,2000);
    }

    TimerTask task=new TimerTask() {
        @Override
        public void run() {
            startActivity(new Intent(IntroActivity.this,MainActivity.class));
            finish();
        }
    };


    void loadData() {
        SharedPreferences pref=getSharedPreferences("Data",MODE_PRIVATE);

        G.isSound=pref.getBoolean("Sound",false);
        G.isVibrate=pref.getBoolean("Vibrate",true);
    }
}
