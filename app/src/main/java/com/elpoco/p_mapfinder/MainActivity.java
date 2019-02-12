package com.elpoco.p_mapfinder;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    NavigationView navView;
    DrawerLayout drawerLayout;

    ImageView myPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);
        navView=findViewById(R.id.nav_view);
        myPage=findViewById(R.id.btn_my_page);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    public void clickMap(View view) {
        startActivity(new Intent(this,MapActivity.class));
    }

    public void clickSeaMap(View view) {
        startActivity(new Intent(this,MapSeaActivity.class));
    }

    public void clickShop(View view) {
        String shopUrl="https://lostark.game.onstove.com/Shop/Mari";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(shopUrl)));
    }

    public void clickShare(View view) {
        startActivity(new Intent(this, ShareActivity.class));
    }

    public void clickMyPage(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
