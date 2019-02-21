package com.elpoco.p_mapfinder;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {

    Toolbar toolbar;

    TextView tvTitle, tvText;
    ImageView ivMap;
    RecyclerView recyclerView;
    EditText etComment;

    ShareItem item;
    ArrayList<CommentItem> comments = new ArrayList<>();
    CommentAdapter adapter;

    int thisBoardNum;

    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tv_title);
        tvText = findViewById(R.id.tv_text);
        ivMap = findViewById(R.id.iv_map);
        etComment = findViewById(R.id.et_comment);

        recyclerView = findViewById(R.id.list_comment);
        adapter = new CommentAdapter(this, comments);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        item = getIntent().getExtras().getParcelable("item");

        tvTitle.setText(item.getTitle());
        tvText.setText(item.getText());
        Glide.with(this).load(item.getFilePath()).into(ivMap);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        thisBoardNum = Integer.parseInt(item.getBoardNum());
        loadComment();
        recyclerView.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    public void clickLogo(View view) {
        setResult(RESULT_OK, getIntent().putExtra("finish", 0));
        finish();
    }

    public void clickSuccess(View view) {
        String serverUrl = "http://elpoco1.dothome.co.kr/insertDBcomment.php";
        String boardNum = item.getBoardNum();
        String comment = etComment.getText().toString();
        if (comment.length() <= 1) {
            new AlertDialog.Builder(this).setMessage("2글자이상 입력하세요.").show();
            return;
        }
        SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        multiPartRequest.addStringParam("boardNum", boardNum);
        multiPartRequest.addStringParam("comment", comment);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);
        etComment.setText("");

        imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);
        handler.sendEmptyMessageAtTime(0, 500);
    }

    public void loadComment() {
        String serverUrl = "http://elpoco1.dothome.co.kr/loadDBcomment.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, serverUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String boardNum, comment;
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        boardNum = jsonObject.getString("boardNum");
                        if (thisBoardNum != Integer.parseInt(boardNum)) continue;
                        comment = jsonObject.getString("comment");
                        comments.add(0, new CommentItem("익명", comment, "aa"));
                    }
                    adapter.notifyItemInserted(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            loadComment();
        }
    };
}
