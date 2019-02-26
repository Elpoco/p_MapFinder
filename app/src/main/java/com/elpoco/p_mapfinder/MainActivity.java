package com.elpoco.p_mapfinder;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    NavigationView navView;
    DrawerLayout drawerLayout;

    boolean backBtn = false;
    backBtnThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    public void clickMap(View view) {
        startActivity(new Intent(this, MapActivity.class));
    }

    public void clickSeaMap(View view) {
        startActivity(new Intent(this, MapSeaActivity.class));
    }

    public void clickShop(View view) {
        String shopUrl = "https://lostark.game.onstove.com/Shop/Mari";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(shopUrl)));
    }

    public void clickShare(View view) {
//        loading();
        startActivity(new Intent(this, ShareActivity.class));
    }

    public void clickMyPage(View view) {
        loading();
//        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void clickSetting(View view) {
//        loading();
        startActivity(new Intent(this, SettingActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (backBtn) {saveData(); super.onBackPressed();}
        backBtn = true;
        if (thread == null) {
            Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            thread = new backBtnThread();
            thread.start();
        }
    }

    class backBtnThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(2000);
                backBtn = false;
                thread=null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    void loading() {
        new AlertDialog.Builder(this).setMessage("준비중 입니다.").show();
    }

    void saveData() {
        SharedPreferences pref=getSharedPreferences("Data",MODE_PRIVATE);

        SharedPreferences.Editor editor=pref.edit();

        editor.putBoolean("Sound",G.isSound);
        editor.putBoolean("Vibrate",G.isVibrate);

        editor.commit();
    }
}
