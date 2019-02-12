package com.elpoco.p_mapfinder;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MapActivity extends AppCompatActivity {

    Spinner sp_1, sp_2, sp_3;
    Spinner sp_song, sp_hint;
    ArrayAdapter adapter_1, adapter_2, adapter_3;
    ArrayAdapter adapterSong,adapterHint;

    TextView tv;

    String[] mapNameSong = {"","루테란", "토토이크", "유디아", "베른", "아르데타인\n아르테미스", "애니츠", "슈샤이어"};
    String[] mapName = {"아르테미스", "유디아", "루테란 서부", "루테란 동부", "토토이크", "애니츠", "아르데타인", "베른", "슈사이어","",""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        tv = findViewById(R.id.tv);

        sp_1 = findViewById(R.id.sp_1);
        sp_2 = findViewById(R.id.sp_2);
        sp_3 = findViewById(R.id.sp_3);
        sp_song = findViewById(R.id.sp_song);
        sp_hint=findViewById(R.id.sp_hint);

        adapter_1 = ArrayAdapter.createFromResource(this, R.array.map_data_a, R.layout.spinner_item);
        adapter_2 = ArrayAdapter.createFromResource(this, R.array.map_data_b, R.layout.spinner_item);
        adapter_3 = ArrayAdapter.createFromResource(this, R.array.map_data_c, R.layout.spinner_item);
        adapterSong = ArrayAdapter.createFromResource(this, R.array.map_song, R.layout.spinner_item);
        adapterHint = ArrayAdapter.createFromResource(this,R.array.map_hint,R.layout.spinner_item);

        sp_1.setAdapter(adapter_1);
        sp_2.setAdapter(adapter_2);
        sp_3.setAdapter(adapter_3);
        sp_song.setAdapter(adapterSong);
        sp_hint.setAdapter(adapterHint);

        adapter_1.setDropDownViewResource(R.layout.spinner_drop);
        adapter_2.setDropDownViewResource(R.layout.spinner_drop);
        adapter_3.setDropDownViewResource(R.layout.spinner_drop);
        adapterSong.setDropDownViewResource(R.layout.spinner_drop);
        adapterHint.setDropDownViewResource(R.layout.spinner_drop);

        sp_1.setOnItemSelectedListener(selectedListener);
        sp_2.setOnItemSelectedListener(selectedListener);
        sp_3.setOnItemSelectedListener(selectedListener);
        sp_song.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv.setText(mapNameSong[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_hint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Resources res=getResources();
                String[] arr=res.getStringArray(R.array.map_answer);
                tv.setText(arr[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int[][] arr = new int[0][];
            switch (parent.getId()) {
                case R.id.sp_1:
                    arr = new int[][]{
                            {5, 13},
                            {},
                            {4, 12},
                            {1, 10, 11},
                            {7},
                            {3, 8, 9},
                            {2, 6,14}
                    };
                    break;
                case R.id.sp_2:
                    arr = new int[][]{
                            {},
                            {11},
                            {2, 10},
                            {7, 9},
                            {4, 13},
                            {14},
                            {3, 5},
                            {6, 8},
                            {1, 12}
                    };
                    break;
                case R.id.sp_3:
                    arr=new int[][]{
                            {1},
                            {4},
                            {5},
                            {9,13,15},
                            {6},
                            {14},
                            {10},
                            {8,11,12},
                            {2,3,7}
                    };
                    break;
            }
            int index = 9;
            for (int i = 0; i < arr.length; i++) {
                for (int k = 0; k < arr[i].length; k++) {
                    if (arr[i][k] == position) index = i;
                }
            }
            tv.setText(mapName[index]);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}
