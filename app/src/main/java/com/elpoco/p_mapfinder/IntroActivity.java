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

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, G.PERMISSION);
            }
        }

        timer.schedule(task,2000);
    }

    TimerTask task=new TimerTask() {
        @Override
        public void run() {
            startActivity(new Intent(IntroActivity.this,MainActivity.class));
            finish();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case G.PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                }
                break;
        }
    }

    void loadData() {
        SharedPreferences pref=getSharedPreferences("Data",MODE_PRIVATE);
        G.isSound=pref.getBoolean("Sound",true);
        G.isVibrate=pref.getBoolean("Vibrate",true);
        G.isToken=pref.getBoolean("isToken",false);
        G.nickName=pref.getString("nickName","닉네임");
        G.profileUrl=pref.getString("profileUrl","https://firebasestorage.googleapis.com/v0/b/loa-map.appspot.com/o/profile_image.png?alt=media&token=00d5b549-2641-4bbc-ad9e-67a7768ebf53");
        G.login=pref.getBoolean("login",false);
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
