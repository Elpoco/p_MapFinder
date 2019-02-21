package com.elpoco.p_mapfinder;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

public class WriteActivity extends AppCompatActivity {

    Toolbar toolbar;

    ProgressBar progressBar;

    EditText etTitle, etText;
    ImageView ivMap;
    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progress_write);

        etTitle = findViewById(R.id.et_title);
        etText = findViewById(R.id.et_text);
        ivMap = findViewById(R.id.iv_map);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, G.PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case G.PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "이미지 업로드 불가", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void clickLogo(View view) {
        setResult(RESULT_OK, getIntent().putExtra("finish", G.FINISH));
        exit();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        exit();
    }

    public void clickSuccess(View view) {
        String serverUrl = "http://elpoco1.dothome.co.kr/insertDB.php";
        String title = etTitle.getText().toString();
        String text = etText.getText().toString();

        if (title.length() == 0) {
            makeDialog("제목을 입력하세요.");
            return;
        }
        if (text.length() == 0) {
            makeDialog("내용을 입력하세요.");
            return;
        }
        if (imgPath == null) {
            makeDialog("사진을 선택하세요.");
            return;
        }

        SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // 서버로부터 응답을 받았을때 자동 실행..
                // 매개변수로 받은 String 이 echo 된 결과값...
//                new AlertDialog.Builder(WriteActivity.this).setMessage(response).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 서버요청 중 에러가 발생하면 자동 실행..
//                Toast.makeText(WriteActivity.this, error.getMessage(), Toast.LENGTH_SHORT);
            }
        });

        multiPartRequest.addStringParam("title", title);
        multiPartRequest.addStringParam("text", text);
        multiPartRequest.addFile("upload", imgPath);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);
        setResult(RESULT_OK, getIntent().putExtra("write", G.WRITE_OK));

        progressBar.setVisibility(View.VISIBLE);

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WriteActivity.this, "게시물이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();
    }

    public void makeDialog(String msg) {
        new AlertDialog.Builder(this).setMessage(msg).show();
    }

    public void clickCancel(View view) {
        exit();
    }

    void exit() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("작성을 종료 하시겠습니까?");
        dialog.setNegativeButton("취소", null);
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();
    }

    public void clickSelectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, G.SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case G.SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        Glide.with(this).load(uri).into(ivMap);
                        imgPath = getRealPathFromUri(uri);
                    }
                }
                break;
        }
    }

    //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    String getRealPathFromUri(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}
