package com.elpoco.p_mapfinder;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    Timer timer=new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        if(!G.isToken) getToken();
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
        G.isSound=pref.getBoolean("Sound",true);
        G.isVibrate=pref.getBoolean("Vibrate",true);
        G.isToken=pref.getBoolean("isToken",false);
        G.nickName=pref.getString("nickName","닉네임");
        G.profileUrl=pref.getString("profileUrl","https://firebasestorage.googleapis.com/v0/b/loa-map.appspot.com/o/profileImages%2Fprofile_image.png?alt=media&token=8f7a3d8d-114d-4d0e-9e04-2c89ff2d2afd");
        G.login=pref.getBoolean("login",false);
        G.isFirst=pref.getBoolean("isFirst",true);
        G.versionName=pref.getString("version","1.0");
    }

    void getToken() {
        // Get token
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        G.token=token;
                        G.isToken=true;
                    }
                });
        // [END retrieve_current_token]
    }
}
