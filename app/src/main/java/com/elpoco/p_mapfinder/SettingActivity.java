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
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class SettingActivity extends AppCompatActivity {

    Toolbar toolbar;

    ToggleButton tbSound,tbVibrate;
    SoundPool soundPool;
    Vibrator vibrator;

    ImageView ivSound,ivVibrate;

    int alarm;

    RewardedVideoAd rewardedVideoAd;

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
        ivSound=findViewById(R.id.iv_sound);
        ivVibrate=findViewById(R.id.iv_vibrate);

        tbSound.setOnCheckedChangeListener(checkedChangeListener);
        tbVibrate.setOnCheckedChangeListener(checkedChangeListener);

        soundPool=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        alarm=soundPool.load(this,R.raw.mococo_seed,1);

        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);

        tbSound.setChecked(G.isSound);
        tbVibrate.setChecked(G.isVibrate);

        MobileAds.initialize(this, String.valueOf(R.string.adMobId));
        rewardedVideoAd= MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
        rewardedVideoAd.loadAd(getResources().getString(R.string.adUnitIdReward),new AdRequest.Builder().build());
    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.tb_sound:
                    G.isSound=isChecked;
                    if (G.isSound){ soundPool.play(alarm, 1, 1, 10, 0, 1); ivSound.setImageResource(R.drawable.icon_sound_on);}
                    else ivSound.setImageResource(R.drawable.icon_sound_off);
                    break;
                case R.id.tb_vibrate:
                    G.isVibrate=isChecked;
                    if (G.isVibrate) {vibrator.vibrate(500); ivVibrate.setImageResource(R.drawable.icon_vibration_on);}
                    else ivVibrate.setImageResource(R.drawable.icon_vibration_off);
                    break;
            }
        }
    };

    public void clickLogo(View view) {
        finish();
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

    public void clickPlay(View view) {

        if(rewardedVideoAd.isLoaded()) rewardedVideoAd.show();
    }

    RewardedVideoAdListener rewardedVideoAdListener=new RewardedVideoAdListener() {
        @Override
        public void onRewardedVideoAdLoaded() {
        }

        @Override
        public void onRewardedVideoAdOpened() {

        }

        @Override
        public void onRewardedVideoStarted() {

        }

        @Override
        public void onRewardedVideoAdClosed() {

        }

        @Override
        public void onRewarded(RewardItem rewardItem) {

        }

        @Override
        public void onRewardedVideoAdLeftApplication() {

        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int i) {
        }

        @Override
        public void onRewardedVideoCompleted() {

        }
    };
}
