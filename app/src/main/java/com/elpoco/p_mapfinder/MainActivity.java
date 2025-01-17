package com.elpoco.p_mapfinder;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elpoco.p_mapfinder.board.share.ShareActivity;
import com.elpoco.p_mapfinder.calendar.CalendarActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    NavigationView navView;
    DrawerLayout drawerLayout;

    boolean backBtn = false;
    backBtnThread thread;

    EditText etNickname;
    Button btnNickname;

    TextView hTvNickname;
    CircleImageView hIvProfile;
    AlertDialog dialog,dialogMap;

    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;

    RelativeLayout dialogNotify;

    String version;

    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.nav_view);

        navView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        navView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        View headerView = navView.getHeaderView(0);
        hTvNickname = headerView.findViewById(R.id.header_tv_nickname);
        hIvProfile = headerView.findViewById(R.id.header_iv);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        dialogNotify = findViewById(R.id.dialog_notify);
        if (!G.login) login();

        version = "1.0";
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        version();

        if (!G.versionName.equals(version)) dialogNotify.setVisibility(View.VISIBLE);

        Glide.with(this).load(G.profileUrl).into(hIvProfile);
        hTvNickname.setText(G.nickName);

        hIvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, G.SELECT_IMAGE);
            }
        });

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void clickMap(View view) {
        startActivity(new Intent(this, MapActivity.class));
        dialogMap.dismiss();
    }

    public void clickSeaMap(View view) {
        startActivity(new Intent(this, MapSeaActivity.class));
        dialogMap.dismiss();
    }

    public void clickShop(View view) {
        String shopUrl = "https://lostark.game.onstove.com/Shop/Mari";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(shopUrl)));
    }

    public void clickShare(View view) {
        startActivity(new Intent(this, ShareActivity.class));
    }

    public void clickMyPage(View view) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, G.PERMISSION);
            }
        }
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void clickSetting(View view) {
        startActivity(new Intent(this, SettingActivity.class));
    }

    public void clickDialogClose(View view) {
        dialogNotify.setVisibility(View.GONE);
        G.isFirst = false;
        G.versionName=version;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (backBtn) super.onBackPressed();
            backBtn = true;
            if (thread == null) {
                Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                thread = new backBtnThread();
                thread.start();
            }
        }
    }

    public void clickCalendar(View view) {
        startActivity(new Intent(this, CalendarActivity.class));
    }

    public void clickMaps(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View layout=getLayoutInflater().inflate(R.layout.dialog_maps,null);
        builder.setView(layout);
        dialogMap=builder.create();
        dialogMap.show();
    }

    class backBtnThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(2000);
                backBtn = false;
                thread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

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

    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }

    void saveData() {
        SharedPreferences pref = getSharedPreferences("Data", MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("Sound", G.isSound);
        editor.putBoolean("Vibrate", G.isVibrate);
        editor.putString("nickName", G.nickName);
        editor.putString("profileUrl", G.profileUrl);
        editor.putString("Token", G.token);
        editor.putBoolean("isToken", G.isToken);
        editor.putBoolean("login", G.login);
        editor.putBoolean("isFirst", G.isFirst);
        editor.putString("version", G.versionName);

        editor.commit();
    }

    void login() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_nickname, null);
        etNickname = view.findViewById(R.id.dialog_et_nickname);
        btnNickname = view.findViewById(R.id.dialog_btn_nickname);
        btnNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etNickname.getText().toString();
                if (text.length() < 2) {
                    Toast.makeText(MainActivity.this, "2글자 이상 가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                G.nickName = text;
                hTvNickname.setText(text);
                saveData();
                firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference profileRef = firebaseDatabase.getReference("profiles");
                DatabaseReference profileToken = profileRef.child(G.nickName).child("Token");
                profileToken.setValue(G.token);
                G.login = true;
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                return;
        }
        switch (requestCode) {
            case G.SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        Glide.with(this).load(uri).into(hIvProfile);
                        G.profileUrl = uri + "";
                        saveFirebaseData();
                        saveData();
                    }
                }
                break;
        }
    }

    void saveFirebaseData() {
        if (G.profileUrl == null) return;
        firebaseStorage = FirebaseStorage.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String fileName = G.nickName + simpleDateFormat.format(new Date()) + ".png";

        final StorageReference imgRef = firebaseStorage.getReference("profileImages/" + fileName);

        UploadTask uploadTask = imgRef.putFile(Uri.parse(G.profileUrl));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        G.profileUrl = uri.toString();
                        Toast.makeText(MainActivity.this, "설정 완료", Toast.LENGTH_SHORT).show();
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference profileRef = firebaseDatabase.getReference("profiles").child(G.nickName);
                        profileRef.push();
                        DatabaseReference profileUrlRef = profileRef.child("profileUrl");
                        profileUrlRef.setValue(G.profileUrl);
                    }
                });
            }
        });
    }

    NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_map:
                    startActivity(new Intent(MainActivity.this,MapActivity.class));
                    break;
                case R.id.menu_seamap:
                    startActivity(new Intent(MainActivity.this, MapSeaActivity.class));
                    break;
                case R.id.menu_calendar:
                    startActivity(new Intent(MainActivity.this,CalendarActivity.class));
                    break;
                case R.id.menu_share:
                    startActivity(new Intent(MainActivity.this, ShareActivity.class));
                    break;
                case R.id.menu_my_page:
                    startActivity(new Intent(MainActivity.this,MyPageActivity.class));
                    break;
            }
            drawerLayout.closeDrawer(navView);
            return false;
        }
    };

    void version() {
        DatabaseReference versionRef=FirebaseDatabase.getInstance().getReference().child("version");
        versionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String serverVersion;
                serverVersion=dataSnapshot.getValue().toString();
                if(serverVersion.equals(version)) return;
                new AlertDialog.Builder(MainActivity.this).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.elpoco.p_mapfinder")));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.elpoco.p_mapfinder")));
                        }
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setMessage("새로운 버전이 나왔습니다. \n업데이트 하시겠습니까?").show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
