package com.elpoco.p_mapfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.elpoco.p_mapfinder.board.share.ShareAdapter;
import com.elpoco.p_mapfinder.board.share.ShareItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPageActivity extends AppCompatActivity {

    Toolbar toolbar;

    String serverUrl = "http://elpoco1.dothome.co.kr/loadDB.php";
    ArrayList<ShareItem> items=new ArrayList<>();
    RecyclerView recyclerView;
    ShareAdapter adapter;

    CircleImageView myPageIv;
    TextView myPageTv;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myPageIv=findViewById(R.id.mypage_iv);
        myPageTv=findViewById(R.id.mypage_nickname);
        progressBar=findViewById(R.id.mypage_progressbar);

        Glide.with(this).load(G.profileUrl).into(myPageIv);
        myPageTv.setText(G.nickName);

        recyclerView=findViewById(R.id.mypage_rv);
        adapter=new ShareAdapter(items,this);
        recyclerView.setAdapter(adapter);
        loadData();
    }

    public void loadData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, serverUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String title, text, filePath, boardNum, nickName, profileUrl,token;
                    if (!items.isEmpty()) items.clear();
                    for (int i = response.length() - 1; i >= 0; i--) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        boardNum = jsonObject.getString("num");
                        title = jsonObject.getString("title");
                        text = jsonObject.getString("text");
                        filePath = jsonObject.getString("filepath");
                        nickName = jsonObject.getString("nickName");
                        profileUrl = jsonObject.getString("profileUrl");
                        token = jsonObject.getString("token");

                        filePath = "http://elpoco1.dothome.co.kr/" + filePath;
                        if(!G.token.equals(token)) continue;
                        items.add(new ShareItem(title, nickName, profileUrl, boardNum, text, filePath,token));
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

    public void clickLogo(View view) {
        finish();
    }
}
