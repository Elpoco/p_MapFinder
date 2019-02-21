package com.elpoco.p_mapfinder;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
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

        loadData();
    }

    public void loadData() {
        String serverUrl = "http://elpoco1.dothome.co.kr/loadDB.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, serverUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // 응답을 성공적으로 받았을 때...
                // 서버로부터 echo 된 데이터... : 매개변수로 온 JsonArray
                try {
                    String title, text, filePath;
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        title = jsonObject.getString("title");
                        text = jsonObject.getString("text");
                        filePath = jsonObject.getString("filepath");

                        // 파일경로의 경우 서버 IP 가 제외된 주소(uploads/*.*)로 전달되어옴.
                        // 그래서 바로 사용할 수가 없음
                        filePath = "http://elpoco1.dothome.co.kr/" + filePath;
                        items.add(0, new ShareItem(title, text, filePath));
                        adapter.notifyItemInserted(0);
                    }
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
                    if (data.getIntExtra("write", G.DEFAULT) == G.WRITE_OK) adapter.notifyDataSetChanged();
                }
                break;
        }
    }

}
