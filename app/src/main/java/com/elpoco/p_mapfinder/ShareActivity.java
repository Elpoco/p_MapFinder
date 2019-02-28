package com.elpoco.p_mapfinder;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<ShareItem> items = new ArrayList<>();
    RecyclerView recyclerView;
    ShareAdapter adapter;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.list_share);
        adapter = new ShareAdapter(items, this);
        recyclerView.setAdapter(adapter);

        progressBar=findViewById(R.id.progress_share);

        loadData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)){
                    loadData+=8;
                    progressBar.setVisibility(View.VISIBLE);
                    loadData();
                }
            }
        });
    }

    int loadData=8;
        String serverUrl = "http://elpoco1.dothome.co.kr/loadDB.php";
    public void loadData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, serverUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String title, text, filePath, boardNum,nickName,profileUrl;
                    if(!items.isEmpty()) items.clear();
                    if(response.length()<8) loadData=response.length();
                    if(response.length()<loadData) loadData=response.length();
                    for (int i=0; i < loadData; i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        boardNum = jsonObject.getString("num");
                        title = jsonObject.getString("title");
                        text = jsonObject.getString("text");
                        filePath = jsonObject.getString("filepath");
                        nickName=jsonObject.getString("nickName");
                        profileUrl=jsonObject.getString("profileUrl");

                        filePath = "http://elpoco1.dothome.co.kr/" + filePath;
                        items.add(0,new ShareItem(title, nickName,profileUrl, boardNum, text, filePath));
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 실패
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
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
                    if (data.getIntExtra("finish", G.DEFAULT) == G.FINISH) finish();
                    if (data.getIntExtra("write", G.DEFAULT) == G.WRITE_OK) {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessageAtTime(0, 2000);
                    }
                }
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadData();
            recyclerView.scrollToPosition(0);
        }
    };

}
