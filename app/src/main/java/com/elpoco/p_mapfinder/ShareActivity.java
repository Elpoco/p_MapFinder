package com.elpoco.p_mapfinder;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<ShareItem> items = new ArrayList<>();
    RecyclerView recyclerView;
    ShareAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.list_recycler);
        adapter = new ShareAdapter(items, this);
        recyclerView.setAdapter(adapter);

        for (int i = 0; i < 10; i++) {
            items.add(new ShareItem(i + ""));
        }

        adapter.notifyDataSetChanged();
    }

    public void clickAdd(View view) {
        startActivityForResult(new Intent(this, WriteActivity.class), 10);
    }

    public void clickLogo(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    if (data.getIntExtra("finish", 1) == 0) {
                        finish();
                    }
                }
                break;
        }
    }
}
