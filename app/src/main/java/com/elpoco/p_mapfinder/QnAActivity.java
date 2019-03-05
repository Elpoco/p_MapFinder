package com.elpoco.p_mapfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class QnAActivity extends AppCompatActivity {

    EditText etTitle,etText;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);

        etTitle=findViewById(R.id.qna_et_title);
        etText=findViewById(R.id.qna_et_text);
    }

    public void clickLogo(View view) {
        finish();
    }

    public void clickSuccess(View view) {
        firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference QnARef=firebaseDatabase.getReference("QnA").child(G.nickName);
        DatabaseReference tokenRef=QnARef.child("Token");
        tokenRef.setValue(G.token);
        DatabaseReference QRef=QnARef.child(etTitle.getText().toString());
        QRef.push();
        QRef.setValue(etText.getText().toString());

        finish();
    }

    public void clickCancel(View view) {
        finish();
    }
}
