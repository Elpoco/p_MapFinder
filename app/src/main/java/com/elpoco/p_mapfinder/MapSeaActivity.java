package com.elpoco.p_mapfinder;

import android.app.AlertDialog;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class MapSeaActivity extends AppCompatActivity {

    PhotoView pv;
    ImageView pin;

    boolean scaleChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_sea);

        pv = findViewById(R.id.pv);
        pin = findViewById(R.id.pin);

        pv.setOnScaleChangeListener(new OnScaleChangedListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                if (scaleFactor > 1.0) scaleChange = true;
                if(scaleFactor<=1.0) scaleChange=false;
            }
        });

        pv.setOnMatrixChangeListener(new OnMatrixChangedListener() {
            @Override
            public void onMatrixChanged(RectF rect) {

            }
        });

        pin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        pin.setY(event.getRawY() - pin.getHeight() * 2);
                        pin.setX(event.getRawX() - pin.getWidth() * 1.5f);
                        break;
                }
                return false;
            }
        });

    }

    void pinMove(float x,float y) {

    }

}