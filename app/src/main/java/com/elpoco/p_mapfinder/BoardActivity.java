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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BoardActivity extends AppCompatActivity {

    Toolbar toolbar;

    ScrollView scrollView;
    CircleImageView ivProfile;
    TextView tvTitle, tvNickname, tvText;
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

        scrollView=findViewById(R.id.scroll_board);
        ivProfile=findViewById(R.id.iv_board_user);
        tvTitle = findViewById(R.id.tv_title);
        tvNickname=findViewById(R.id.tv_nickname);
        tvText = findViewById(R.id.tv_text);
        ivMap = findViewById(R.id.iv_map);
        etComment = findViewById(R.id.et_comment);

        recyclerView = findViewById(R.id.list_comment);
        adapter = new CommentAdapter(this, comments);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        item = getIntent().getExtras().getParcelable("item");

        Glide.with(this).load(item.getProfileUrl()).into(ivProfile);
        tvNickname.setText(item.getNickName());
        tvTitle.setText(item.getTitle());
        tvText.setText(item.getText());
        Glide.with(this).load(item.getFilePath()).into(ivMap);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        thisBoardNum = Integer.parseInt(item.getBoardNum());

        loadComment();

        recyclerView.setLayoutManager(new LinearLayoutManager(this) {
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
        imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);

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
        multiPartRequest.addStringParam("profileUrl",G.profileUrl);
        multiPartRequest.addStringParam("nickName",G.nickName);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);
        etComment.setText("");

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessageAtTime(0, 1000);
            }
        }.start();
    }

    public void loadComment() {
        String serverUrl = "http://elpoco1.dothome.co.kr/loadDBcomment.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, serverUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String boardNum, comment,nickName,profileUrl;
                    if (!comments.isEmpty()) comments.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        boardNum = jsonObject.getString("boardNum");
                        if (thisBoardNum != Integer.parseInt(boardNum)) continue;
                        comment = jsonObject.getString("comment");
                        nickName=jsonObject.getString("nickName");
                        profileUrl=jsonObject.getString("profileUrl");
                        comments.add(new CommentItem(nickName, comment, profileUrl));
                    }
                    adapter.notifyDataSetChanged();
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
            Toast.makeText(BoardActivity.this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    public void clickBoardImage(View view) {
        View dialogView=getLayoutInflater().inflate(R.layout.dialog_board_imageclick,null);
        PhotoView pv=dialogView.findViewById(R.id.dialog_pv);
        Glide.with(this).load(item.getFilePath()).into(pv);
        new AlertDialog.Builder(this).setView(dialogView).show();
    }
}
