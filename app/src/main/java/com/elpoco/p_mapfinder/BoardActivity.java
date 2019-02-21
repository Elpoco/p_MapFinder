package com.elpoco.p_mapfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {

    Toolbar toolbar;

    TextView tvTitle,tvText;
    ImageView ivMap;
    RecyclerView recyclerView;

    ShareItem item;
    ArrayList<CommentItem> comments=new ArrayList<>();
    CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle=findViewById(R.id.tv_title);
        tvText=findViewById(R.id.tv_text);
        ivMap=findViewById(R.id.iv_map);

        recyclerView=findViewById(R.id.list_comment);
        adapter=new CommentAdapter(this,comments);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        item=getIntent().getExtras().getParcelable("item");

        tvTitle.setText(item.getTitle());
        tvText.setText(item.getText());
        Glide.with(this).load(item.getFilePath()).into(ivMap);
    }

    public void clickLogo(View view) {
        setResult(RESULT_OK, getIntent().putExtra("finish",0));
        finish();
    }

    public void clickSuccess(View view) {
    }
}
