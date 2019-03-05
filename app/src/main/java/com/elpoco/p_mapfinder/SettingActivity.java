package com.elpoco.p_mapfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class SettingActivity extends AppCompatActivity {

    Toolbar toolbar;

    ToggleButton tbSound,tbVibrate;
    SoundPool soundPool;
    Vibrator vibrator;

    int alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tbSound=findViewById(R.id.tb_sound);
        tbVibrate=findViewById(R.id.tb_vibrate);

        tbSound.setOnCheckedChangeListener(checkedChangeListener);
        tbVibrate.setOnCheckedChangeListener(checkedChangeListener);

        soundPool=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        alarm=soundPool.load(this,R.raw.mococo_seed,1);

        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);

        tbSound.setChecked(G.isSound);
        tbVibrate.setChecked(G.isVibrate);
    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.tb_sound:
                    G.isSound=isChecked;
                    break;
                case R.id.tb_vibrate:
                    G.isVibrate=isChecked;
                    break;
            }
        }
    };

    public void clickLogo(View view) {
        finish();
    }

    public void clickSetting(View view) {
        switch (view.getId()) {
            case R.id.tb_sound:
                if(G.isSound) soundPool.play(alarm,1,1,10,0,1);
                break;
            case R.id.tb_vibrate:
                if(G.isVibrate) vibrator.vibrate(500);
                break;
        }
    }


    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }

    void saveData() {
        SharedPreferences pref=getSharedPreferences("Data",MODE_PRIVATE);

        SharedPreferences.Editor editor=pref.edit();

        editor.putBoolean("Sound",G.isSound);
        editor.putBoolean("Vibrate",G.isVibrate);

        editor.commit();
    }

    public void clickQnA(View view) {
        startActivity(new Intent(this,QnAActivity.class));
    }
}
